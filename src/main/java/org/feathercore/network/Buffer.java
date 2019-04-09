package org.feathercore.network;

import org.feathercore.util.chat.component.ChatComponent;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.*;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public abstract class Buffer {

    public abstract byte readByte();

    public abstract void writeByte(byte val);

    public abstract short readShort();

    public abstract void writeShort(short val);

    public abstract int readInt();

    public abstract void writeInt(int val);

    public abstract long readLong();

    public abstract void writeLong(long val);

    public abstract float readFloat();

    public abstract void writeFloat(float val);

    public abstract double readDouble();

    public abstract void writeDouble(double val);

    public boolean readBoolean() {
        return readByte() == (byte) 1;
    }

    public void writeBoolean(boolean val) {
        writeByte(val ? (byte) 1 : (byte) 0);
    }

    public abstract byte[] readBytes(int length);

    /**
     * Without array size.
     *
     * @param bytes bytes to be written.
     */
    public abstract void writeBytes(byte[] bytes);

    /**
     * Reads an unsigned varint
     */
    public int readVarInt() {
        int tmp;
        if ((tmp = this.readByte()) >= 0) {
            return tmp;
        }
        int result = tmp & 0x7f;
        if ((tmp = this.readByte()) >= 0) {
            result |= tmp << 7;
        } else {
            result |= (tmp & 0x7f) << 7;
            if ((tmp = this.readByte()) >= 0) {
                result |= tmp << 14;
            } else {
                result |= (tmp & 0x7f) << 14;
                if ((tmp = this.readByte()) >= 0) {
                    result |= tmp << 21;
                } else {
                    result |= (tmp & 0x7f) << 21;
                    result |= this.readByte() << 28;
                }
            }
        }
        return result;
    }

    /**
     * Writes an unsigned varint
     */
    public void writeVarInt(int val) {
        while (true) {
            int bits = val & 0x7f;
            val >>>= 7;
            if (val == 0) {
                this.writeByte((byte) bits);
                return;
            }
            this.writeByte((byte) (bits | 0x80));
        }
    }

    /**
     * Writes a signed varint
     */
    @SuppressWarnings("NumericOverflow")
    public int readSignedVarInt() {
        int raw = readVarInt();
        // This undoes the trick in writeSignedVarInt()
        int temp = (((raw << 31) >> 31) ^ raw) >> 1;
        // This extra step lets us deal with the largest signed values by treating
        // negative results from read unsigned methods as like unsigned values.
        // Must re-flip the top bit if the original read value had it set.
        return temp ^ (raw & (1 << 31));
    }

    /**
     * Reads a signed varint
     */
    public void writeSignedVarInt(int val) {
        // Great trick from http://code.google.com/apis/protocolbuffers/docs/encoding.html#types
        writeVarInt((val << 1) ^ (val >> 31));
    }

    /**
     * Reads an unsigned varlong
     */
    public long readVarLong() {
        long value = 0;
        byte temp;
        for (int i = 0; i < 10; i++) {
            temp = this.readByte();
            value |= ((long) (temp & 0x7F)) << (i * 7);
            if ((temp & 0x80) != 0x80) {
                break;
            }
        }
        return value;
    }

    /**
     * Writes an unsigned varlong
     */
    public void writeVarLong(long l) {
        byte temp;
        do {
            temp = (byte) (l & 0x7F);
            l >>>= 7;
            if (l != 0) {
                temp |= 0x80;
            }
            this.writeByte(temp);
        } while (l != 0);
    }

    /**
     * Reads a signed varlong
     */
    @SuppressWarnings("NumericOverflow")
    public long readSignedVarLong() {
        long raw = readVarLong();
        // This undoes the trick in writeSignedVarInt()
        long temp = (((raw << 63) >> 63) ^ raw) >> 1;
        // This extra step lets us deal with the largest signed values by treating
        // negative results from read unsigned methods as like unsigned values.
        // Must re-flip the top bit if the original read value had it set.
        return temp ^ (raw & (1L << 63));
    }

    /**
     * Writes a signed varlong
     */
    public void writeSignedVarLong(long val) {
        // Great trick from http://code.google.com/apis/protocolbuffers/docs/encoding.html#types
        writeVarLong((val << 1) ^ (val >> 63));
    }

    public String readString(int maxLength) {
        int length = readVarInt();
        if (length > maxLength) {
            throw new IllegalStateException("Trying to read string with length " + length + " when the max is " + maxLength);
        }
        return new String(readBytes(length), StandardCharsets.UTF_8);
    }

    public String readString() {
        return new String(readBytes(readVarInt()), StandardCharsets.UTF_8);
    }

    public void writeString(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeVarInt(bytes.length);
        writeBytes(bytes);
    }

    public String readStringNullable(int maxLength) {
        if (readBoolean()) {
            return readString(maxLength);
        }
        return null;
    }

    public void writeStringNullable(String s) {
        if (s != null) {
            writeBoolean(true);
            writeString(s);
        } else {
            writeBoolean(false);
        }
    }

    public <T extends Enum> T readEnum(Class<T> clazz) {
        return clazz.getEnumConstants()[readVarInt()];
    }

    public void writeEnum(Enum e) {
        writeVarInt(e.ordinal());
    }

    /**
     * Examples:
     * <pre>
     * List&lt;String&gt; list = buffer.readCollection(255, ArrayList::new, () -> buffer.readString(255));
     * List&lt;User&gt; list = buffer.readCollection(255, ArrayList::new, this::readUser);
     * </pre>
     *
     * @param maxSize           max collection size
     * @param collectionCreator collection constructor
     * @param reader            function to read collection
     */
    public <T, C extends Collection<T>> C readCollection(int maxSize, Function<Integer, C> collectionCreator, Supplier<T> reader) {
        int size = readVarInt();
        if (size > maxSize) {
            throw new IllegalStateException("Trying to read collection with size " + size + " when the max is " + maxSize);
        }
        C collection = collectionCreator.apply(size);
        for (int i = 0; i < size; ++i) {
            collection.add(reader.get());
        }
        return collection;
    }

    public <T> void writeCollection(Collection<T> collection, Consumer<T> writer) {
        writeVarInt(collection.size());
        collection.forEach(writer);
    }

    public <T> T[] readArray(int maxSize, IntFunction<T[]> creator, Supplier<T> reader) {
        int size = readVarInt();
        if (size > maxSize) {
            throw new IllegalStateException("Trying to read array with size " + size + " when the max is " + maxSize);
        }
        T[] arr = creator.apply(size);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = reader.get();
        }
        return arr;
    }

    public List<String> readStringList(int maxListSize, int maxStringLength) {
        return readCollection(maxListSize, ArrayList::new, () -> readString(maxStringLength));
    }

    public void writeStringList(List<String> list) {
        writeCollection(list, this::writeString);
    }

    public int[] readIntArray(int maxSize, IntSupplier reader) {
        int size = readVarInt();
        if (size > maxSize) {
            throw new IllegalStateException("Trying to read int array with size " + size + " when the max is " + maxSize);
        }
        int[] arr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = reader.getAsInt();
        }
        return arr;
    }

    public void writeIntArray(int[] arr, IntConsumer writer) {
        writeVarInt(arr.length);
        for (int val : arr) {
            writer.accept(val);
        }
    }

    public long[] readLongArray(int maxSize, LongSupplier reader) {
        int size = readVarInt();
        if (size > maxSize) {
            throw new IllegalStateException("Trying to read long array with size " + size + " when the max is " + maxSize);
        }
        long[] arr = new long[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = reader.getAsLong();
        }
        return arr;
    }

    public void writeLongArray(long[] arr, LongConsumer writer) {
        writeVarInt(arr.length);
        for (long val : arr) {
            writer.accept(val);
        }
    }

    public byte[] readByteArray() {
        int size = readVarInt();
        byte[] arr = new byte[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = readByte();
        }
        return arr;
    }

    public byte[] readByteArray(int maxSize) {
        int size = readVarInt();
        if (size > maxSize) {
            throw new IllegalStateException("Trying to read byte array with size " + size + " when the max is " + maxSize);
        }
        byte[] arr = new byte[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = readByte();
        }
        return arr;
    }

    public void writeByteArray(byte[] arr) {
        writeVarInt(arr.length);
        for (byte val : arr) {
            writeByte(val);
        }
    }

    public UUID readUUID() {
        return new UUID(readLong(), readLong());
    }

    public void writeUUID(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public UUID readUUIDNullable() {
        if (readBoolean()) {
            return readUUID();
        }
        return null;
    }

    public void writeUUIDNullable(UUID uuid) {
        if (uuid != null) {
            writeBoolean(true);
            writeUUID(uuid);
        } else {
            writeBoolean(false);
        }
    }

    public ChatComponent readChatComponent() {
        return ChatComponent.Serializer.jsonToComponent(readString(32767));
    }

    public void writeChatComponent(ChatComponent component) {
        writeString(ChatComponent.Serializer.componentToJson(component));
    }

    public Buffer newBuffer() {
        return newBuffer(256);
    }

    public abstract Buffer newBuffer(int size);

    public void release() {
    }

}