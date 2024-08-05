package me.lucko.spark.common.command.sender;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.UUID;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.proto.SparkProtos;

public interface CommandSender {

    String getName();

    UUID getUniqueId();

    void sendMessage(Component var1);

    boolean hasPermission(String var1);

    default CommandSender.Data toData() {
        return new CommandSender.Data(this.getName(), this.getUniqueId());
    }

    public static final class Data {

        private final String name;

        private final UUID uniqueId;

        public Data(String name, UUID uniqueId) {
            this.name = name;
            this.uniqueId = uniqueId;
        }

        public String getName() {
            return this.name;
        }

        public UUID getUniqueId() {
            return this.uniqueId;
        }

        public boolean isPlayer() {
            return this.uniqueId != null;
        }

        public JsonObject serialize() {
            JsonObject user = new JsonObject();
            user.add("type", new JsonPrimitive(this.isPlayer() ? "player" : "other"));
            user.add("name", new JsonPrimitive(this.name));
            if (this.uniqueId != null) {
                user.add("uniqueId", new JsonPrimitive(this.uniqueId.toString()));
            }
            return user;
        }

        public SparkProtos.CommandSenderMetadata toProto() {
            SparkProtos.CommandSenderMetadata.Builder proto = SparkProtos.CommandSenderMetadata.newBuilder().setType(this.isPlayer() ? SparkProtos.CommandSenderMetadata.Type.PLAYER : SparkProtos.CommandSenderMetadata.Type.OTHER).setName(this.name);
            if (this.uniqueId != null) {
                proto.setUniqueId(this.uniqueId.toString());
            }
            return proto.build();
        }

        public static CommandSender.Data deserialize(JsonElement element) {
            JsonObject userObject = element.getAsJsonObject();
            String user = userObject.get("name").getAsJsonPrimitive().getAsString();
            UUID uuid;
            if (userObject.has("uniqueId")) {
                uuid = UUID.fromString(userObject.get("uniqueId").getAsJsonPrimitive().getAsString());
            } else {
                uuid = null;
            }
            return new CommandSender.Data(user, uuid);
        }
    }
}