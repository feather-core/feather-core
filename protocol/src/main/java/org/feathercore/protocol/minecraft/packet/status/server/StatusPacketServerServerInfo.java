/*
 * Copyright 2019 Feather Core
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.feathercore.protocol.minecraft.packet.status.server;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import lombok.*;
import lombok.experimental.FieldDefaults;
import net.md_5.bungee.api.chat.BaseComponent;
import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.shared.util.json.JsonUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by k.shandurenko on 09/04/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class StatusPacketServerServerInfo implements Packet {

    public static final int ID = 0x00;

    // FIXME: 09.04.2019
    private static final Gson GSON = new GsonBuilder().create();
            //(new GsonBuilder()).registerTypeAdapter(MinecraftProtocolVersionIdentifier.class, new
            // MinecraftProtocolVersionIdentifier.Serializer()).registerTypeAdapter(PlayerCountData.class, new
            // PlayerCountData.Serializer()).registerTypeAdapter(ServerStatusResponse.class, new Serializer())
            // .registerTypeHierarchyAdapter(ChatComponent.class, new ChatComponent.Serializer())
            // .registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer()).registerTypeAdapterFactory
            // (new TypeAdapterFactory()).create();
    ServerStatusResponse response;

    @Override
    public void write(@NotNull final Buffer buffer) {
        buffer.writeString(GSON.toJson(this.response));
    }

    @Override
    public void read(@NotNull final Buffer buffer) {
        this.response = GSON.fromJson(buffer.readString(32767), ServerStatusResponse.class);
    }

    @Override
    public int getId() {
        return ID;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServerStatusResponse {

        private BaseComponent serverMotd;
        private PlayerCountData playerCountData;
        private MinecraftProtocolVersionIdentifier protocolVersionIdentifier;
        private String favicon;

        public BaseComponent getServerMotd() {
            return serverMotd;
        }

        public void setServerMotd(BaseComponent serverMotd) {
            this.serverMotd = serverMotd;
        }

        public PlayerCountData getPlayerCountData() {
            return playerCountData;
        }

        public void setPlayerCountData(PlayerCountData playerCountData) {
            this.playerCountData = playerCountData;
        }

        public MinecraftProtocolVersionIdentifier getProtocolVersionIdentifier() {
            return protocolVersionIdentifier;
        }

        public void setProtocolVersionIdentifier(MinecraftProtocolVersionIdentifier protocolVersionIdentifier) {
            this.protocolVersionIdentifier = protocolVersionIdentifier;
        }

        public String getFavicon() {
            return favicon;
        }

        public void setFavicon(String favicon) {
            this.favicon = favicon;
        }
    }

    public static class MinecraftProtocolVersionIdentifier {

        private final String name;
        private final int protocol;

        public MinecraftProtocolVersionIdentifier(String nameIn, int protocolIn) {
            this.name = nameIn;
            this.protocol = protocolIn;
        }

        public String getName() {
            return this.name;
        }

        public int getProtocol() {
            return this.protocol;
        }

        public static class Serializer
                implements JsonDeserializer<MinecraftProtocolVersionIdentifier>,
                JsonSerializer<MinecraftProtocolVersionIdentifier> {

            public MinecraftProtocolVersionIdentifier deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_,
                                                                  JsonDeserializationContext p_deserialize_3_)
                    throws JsonParseException {
                JsonObject jsonobject = JsonUtil.getJsonObject(p_deserialize_1_, "version");
                return new MinecraftProtocolVersionIdentifier(
                        JsonUtil.getString(jsonobject, "name"), JsonUtil.getInt(jsonobject, "protocol"));
            }

            public JsonElement serialize(MinecraftProtocolVersionIdentifier p_serialize_1_, Type p_serialize_2_,
                                         JsonSerializationContext p_serialize_3_) {
                JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("name", p_serialize_1_.getName());
                jsonobject.addProperty("protocol", p_serialize_1_.getProtocol());
                return jsonobject;
            }
        }
    }

    @RequiredArgsConstructor
    public static class PlayerCountData {

        private final int maxPlayers;
        private final int onlinePlayerCount;
        private GameProfile[] players;

        public int getMaxPlayers() {
            return this.maxPlayers;
        }

        public int getOnlinePlayerCount() {
            return this.onlinePlayerCount;
        }

        public GameProfile[] getPlayers() {
            return this.players;
        }

        public void setPlayers(GameProfile[] playersIn) {
            this.players = playersIn;
        }

        public static class Serializer implements JsonDeserializer<PlayerCountData>, JsonSerializer<PlayerCountData> {

            public PlayerCountData deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_,
                                               JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
                JsonObject jsonobject = JsonUtil.getJsonObject(p_deserialize_1_, "players");
                PlayerCountData serverstatusresponse$playercountdata = new PlayerCountData(
                        JsonUtil.getInt(jsonobject, "max"), JsonUtil.getInt(jsonobject, "online"));

                if (JsonUtil.isJsonArray(jsonobject, "sample")) {
                    JsonArray jsonarray = JsonUtil.getJsonArray(jsonobject, "sample");

                    if (jsonarray.size() > 0) {
                        GameProfile[] agameprofile = new GameProfile[jsonarray.size()];

                        for (int i = 0; i < agameprofile.length; ++i) {
                            JsonObject jsonobject1 = JsonUtil.getJsonObject(jsonarray.get(i), "player[" + i + "]");
                            String s = JsonUtil.getString(jsonobject1, "id");
                            agameprofile[i] = new GameProfile(UUID.fromString(s),
                                    JsonUtil.getString(jsonobject1, "name"));
                        }

                        serverstatusresponse$playercountdata.setPlayers(agameprofile);
                    }
                }

                return serverstatusresponse$playercountdata;
            }

            public JsonElement serialize(PlayerCountData p_serialize_1_, Type p_serialize_2_,
                                         JsonSerializationContext p_serialize_3_) {
                JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("max", p_serialize_1_.getMaxPlayers());
                jsonobject.addProperty("online", p_serialize_1_.getOnlinePlayerCount());

                if (p_serialize_1_.getPlayers() != null && p_serialize_1_.getPlayers().length > 0) {
                    JsonArray jsonarray = new JsonArray();

                    for (int i = 0; i < p_serialize_1_.getPlayers().length; ++i) {
                        JsonObject jsonobject1 = new JsonObject();
                        UUID uuid = p_serialize_1_.getPlayers()[i].getId();
                        jsonobject1.addProperty("id", uuid == null ? "" : uuid.toString());
                        jsonobject1.addProperty("name", p_serialize_1_.getPlayers()[i].getName());
                        jsonarray.add(jsonobject1);
                    }

                    jsonobject.add("sample", jsonarray);
                }

                return jsonobject;
            }
        }
    }

    public static class Serializer
            implements JsonDeserializer<ServerStatusResponse>, JsonSerializer<ServerStatusResponse> {

        public ServerStatusResponse deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_,
                                                JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            JsonObject jsonobject = JsonUtil.getJsonObject(p_deserialize_1_, "status");
            ServerStatusResponse serverstatusresponse = new ServerStatusResponse();

            if (jsonobject.has("description")) {
                serverstatusresponse.setServerMotd(
                        p_deserialize_3_.deserialize(jsonobject.get("description"), BaseComponent.class));
            }

            if (jsonobject.has("players")) {
                serverstatusresponse.setPlayerCountData(
                        p_deserialize_3_.deserialize(jsonobject.get("players"), PlayerCountData.class));
            }

            if (jsonobject.has("version")) {
                serverstatusresponse.setProtocolVersionIdentifier(p_deserialize_3_
                        .deserialize(jsonobject.get("version"), MinecraftProtocolVersionIdentifier.class));
            }

            if (jsonobject.has("favicon")) {
                serverstatusresponse.setFavicon(JsonUtil.getString(jsonobject, "favicon"));
            }

            return serverstatusresponse;
        }

        public JsonElement serialize(ServerStatusResponse p_serialize_1_, Type p_serialize_2_,
                                     JsonSerializationContext p_serialize_3_) {
            JsonObject jsonobject = new JsonObject();

            if (p_serialize_1_.getServerMotd() != null) {
                jsonobject.add("description", p_serialize_3_.serialize(p_serialize_1_.getServerMotd()));
            }

            if (p_serialize_1_.getPlayerCountData() != null) {
                jsonobject.add("players", p_serialize_3_.serialize(p_serialize_1_.getPlayerCountData()));
            }

            if (p_serialize_1_.getProtocolVersionIdentifier() != null) {
                jsonobject.add("version", p_serialize_3_.serialize(p_serialize_1_.getProtocolVersionIdentifier()));
            }

            if (p_serialize_1_.getFavicon() != null) {
                jsonobject.addProperty("favicon", p_serialize_1_.getFavicon());
            }

            return jsonobject;
        }
    }

}
