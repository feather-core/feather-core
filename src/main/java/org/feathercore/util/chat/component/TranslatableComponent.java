package org.feathercore.util.chat.component;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.feathercore.util.chat.ChatStyle;
import org.feathercore.util.chat.StatCollector;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class TranslatableComponent extends BaseComponent {

    private final String key;
    private final Object[] formatArgs;
    private final Object syncLock = new Object();
    private long lastTranslationUpdateTimeInMilliseconds = -1L;
    List<ChatComponent> children = Lists.<ChatComponent>newArrayList();
    public static final Pattern stringVariablePattern = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public TranslatableComponent(String translationKey, Object... args) {
        this.key = translationKey;
        this.formatArgs = args;

        for (Object object : args) {
            if (object instanceof ChatComponent) {
                ((ChatComponent) object).getChatStyle().setParentStyle(this.getChatStyle());
            }
        }
    }

    /**
     * ensures that our children are initialized from the most recent string
     * translation mapping.
     */
    synchronized void ensureInitialized() {
        synchronized (this.syncLock) {
            long i = StatCollector.getLastTranslationUpdateTimeInMilliseconds();

            if (i == this.lastTranslationUpdateTimeInMilliseconds) {
                return;
            }

            this.lastTranslationUpdateTimeInMilliseconds = i;
            this.children.clear();
        }

        try {
            this.initializeFromFormat(StatCollector.translateToLocal(this.key));
        } catch (IllegalArgumentException ex) {
            this.children.clear();

            try {
                this.initializeFromFormat(StatCollector.translateToFallback(this.key));
            } catch (IllegalArgumentException ex2) {
                throw ex;
            }
        }
    }

    /**
     * initializes our children from a format string, using the format args to
     * fill in the placeholder variables.
     */
    protected void initializeFromFormat(String format) {
        boolean flag = false;
        Matcher matcher = stringVariablePattern.matcher(format);
        int i = 0;
        int j = 0;

        try {
            int l;

            for (; matcher.find(j); j = l) {
                int k = matcher.start();
                l = matcher.end();

                if (k > j) {
                    TextComponent chatcomponenttext = new TextComponent(String.format(format.substring(j, k), new Object[0]));
                    chatcomponenttext.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(chatcomponenttext);
                }

                String s2 = matcher.group(2);
                String s = format.substring(k, l);

                if ("%".equals(s2) && "%%".equals(s)) {
                    TextComponent chatcomponenttext2 = new TextComponent("%");
                    chatcomponenttext2.getChatStyle().setParentStyle(this.getChatStyle());
                    this.children.add(chatcomponenttext2);
                } else {
                    if (!"s".equals(s2)) {
                        throw new IllegalArgumentException("Unsupported translation format: \'" + s + "\'");
                    }

                    String s1 = matcher.group(1);
                    int i1 = s1 != null ? Integer.parseInt(s1) - 1 : i++;

                    if (i1 < this.formatArgs.length) {
                        this.children.add(this.getFormatArgumentAsComponent(i1));
                    }
                }
            }

            if (j < format.length()) {
                TextComponent chatcomponenttext1 = new TextComponent(String.format(format.substring(j), new Object[0]));
                chatcomponenttext1.getChatStyle().setParentStyle(this.getChatStyle());
                this.children.add(chatcomponenttext1);
            }
        } catch (IllegalFormatException ex) {
            throw new IllegalArgumentException("Incorrect translation format", ex);
        }
    }

    private ChatComponent getFormatArgumentAsComponent(int index) {
        if (index >= this.formatArgs.length) {
            throw new IllegalArgumentException("Incorrect translation format at index " + index);
        } else {
            Object object = this.formatArgs[index];
            ChatComponent ichatcomponent;

            if (object instanceof ChatComponent) {
                ichatcomponent = (ChatComponent) object;
            } else {
                ichatcomponent = new TextComponent(object == null ? "null" : object.toString());
                ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
            }

            return ichatcomponent;
        }
    }

    public ChatComponent setChatStyle(ChatStyle style) {
        super.setChatStyle(style);

        for (Object object : this.formatArgs) {
            if (object instanceof ChatComponent) {
                ((ChatComponent) object).getChatStyle().setParentStyle(this.getChatStyle());
            }
        }

        if (this.lastTranslationUpdateTimeInMilliseconds > -1L) {
            for (ChatComponent ichatcomponent : this.children) {
                ichatcomponent.getChatStyle().setParentStyle(style);
            }
        }

        return this;
    }

    public Iterator<ChatComponent> iterator() {
        this.ensureInitialized();
        return Iterators.<ChatComponent>concat(createDeepCopyIterator(this.children), createDeepCopyIterator(this.siblings));
    }

    /**
     * Gets the text of this component, without any special formatting codes
     * added, for chat. TODO: why is this two different methods?
     */
    public String getUnformattedTextForChat() {
        this.ensureInitialized();
        StringBuilder stringbuilder = new StringBuilder();

        for (ChatComponent ichatcomponent : this.children) {
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
        }

        return stringbuilder.toString();
    }

    /**
     * Creates a copy of this component. Almost a deep copy, except the style is
     * shallow-copied.
     */
    public TranslatableComponent createCopy() {
        Object[] aobject = new Object[this.formatArgs.length];

        for (int i = 0; i < this.formatArgs.length; ++i) {
            if (this.formatArgs[i] instanceof ChatComponent) {
                aobject[i] = ((ChatComponent) this.formatArgs[i]).createCopy();
            } else {
                aobject[i] = this.formatArgs[i];
            }
        }

        TranslatableComponent chatcomponenttranslation = new TranslatableComponent(this.key, aobject);
        chatcomponenttranslation.setChatStyle(this.getChatStyle().createShallowCopy());

        for (ChatComponent ichatcomponent : this.getSiblings()) {
            chatcomponenttranslation.appendSibling(ichatcomponent.createCopy());
        }

        return chatcomponenttranslation;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof TranslatableComponent)) {
            return false;
        } else {
            TranslatableComponent chatcomponenttranslation = (TranslatableComponent) p_equals_1_;
            return Arrays.equals(this.formatArgs, chatcomponenttranslation.formatArgs) && this.key.equals(chatcomponenttranslation.key) && super.equals(p_equals_1_);
        }
    }

    public int hashCode() {
        int i = super.hashCode();
        i = 31 * i + this.key.hashCode();
        i = 31 * i + Arrays.hashCode(this.formatArgs);
        return i;
    }

    public String toString() {
        return "TranslatableComponent{key=\'" + this.key + '\'' + ", args=" + Arrays.toString(this.formatArgs) + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }

    public String getKey() {
        return this.key;
    }

    public Object[] getFormatArgs() {
        return this.formatArgs;
    }
}
