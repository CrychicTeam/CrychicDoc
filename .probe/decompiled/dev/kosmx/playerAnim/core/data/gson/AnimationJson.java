package dev.kosmx.playerAnim.core.data.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import dev.kosmx.playerAnim.core.data.AnimationFormat;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Easing;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

public class AnimationJson implements JsonDeserializer<List<KeyframeAnimation>>, JsonSerializer<KeyframeAnimation> {

    private static final int modVersion = 3;

    public static Type getListedTypeToken() {
        return (new TypeToken<List<KeyframeAnimation>>() {
        }).getType();
    }

    public List<KeyframeAnimation> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject node = json.getAsJsonObject();
        if (!node.has("emote")) {
            return GeckoLibSerializer.serialize(node);
        } else {
            int version = 1;
            if (node.has("version")) {
                version = node.get("version").getAsInt();
            }
            KeyframeAnimation.AnimationBuilder emote = this.emoteDeserializer(node.getAsJsonObject("emote"), version);
            for (Entry<String, JsonElement> entry : node.entrySet()) {
                String string = (String) entry.getKey();
                if (!string.equals("uuid") && !string.equals("comment") && !string.equals("version")) {
                    JsonElement value = (JsonElement) entry.getValue();
                    if (value.isJsonPrimitive()) {
                        JsonPrimitive p = value.getAsJsonPrimitive();
                        if (p.isBoolean()) {
                            emote.extraData.put(string, p.getAsBoolean());
                        } else if (p.isString()) {
                            emote.extraData.put(string, p.getAsString());
                        } else if (p.isNumber()) {
                            emote.extraData.put(string, p.getAsDouble());
                        } else {
                            emote.extraData.put(string, p.toString());
                        }
                    }
                }
            }
            emote.name = node.get("name").toString();
            if (node.has("author")) {
                emote.author = node.get("author").toString();
            }
            if (node.has("description")) {
                emote.description = node.get("description").toString();
            }
            if (3 < version) {
                throw new JsonParseException(emote.name + " is version " + version + ". PlayerAnimator library can only process version " + 3 + ".");
            } else {
                if (node.has("uuid")) {
                    emote.uuid = UUID.fromString(node.get("uuid").getAsString());
                }
                emote.optimizeEmote();
                List<KeyframeAnimation> list = new ArrayList();
                list.add(emote.build());
                return list;
            }
        }
    }

    private KeyframeAnimation.AnimationBuilder emoteDeserializer(JsonObject node, int version) throws JsonParseException {
        KeyframeAnimation.AnimationBuilder builder = new KeyframeAnimation.AnimationBuilder(AnimationFormat.JSON_EMOTECRAFT);
        if (node.has("beginTick")) {
            builder.beginTick = node.get("beginTick").getAsInt();
        }
        builder.endTick = node.get("endTick").getAsInt();
        if (builder.endTick <= 0) {
            throw new JsonParseException("endTick must be bigger than 0");
        } else {
            if (node.has("isLoop") && node.has("returnTick")) {
                builder.isLooped = node.get("isLoop").getAsBoolean();
                builder.returnTick = node.get("returnTick").getAsInt();
                if (builder.isLooped && (builder.returnTick > builder.endTick || builder.returnTick < 0)) {
                    throw new JsonParseException("return tick have to be smaller than endTick and not smaller than 0");
                }
            }
            if (node.has("nsfw")) {
                builder.nsfw = node.get("nsfw").getAsBoolean();
            }
            builder.stopTick = node.has("stopTick") ? node.get("stopTick").getAsInt() : builder.endTick;
            boolean degrees = !node.has("degrees") || node.get("degrees").getAsBoolean();
            if (node.has("easeBeforeKeyframe")) {
                builder.isEasingBefore = node.get("easeBeforeKeyframe").getAsBoolean();
            }
            this.moveDeserializer(builder, node.getAsJsonArray("moves"), degrees, version);
            builder.fullyEnableParts();
            return builder;
        }
    }

    private void moveDeserializer(KeyframeAnimation.AnimationBuilder emote, JsonArray node, boolean degrees, int version) {
        for (JsonElement n : node) {
            JsonObject obj = n.getAsJsonObject();
            int tick = obj.get("tick").getAsInt();
            String easing = obj.has("easing") ? obj.get("easing").getAsString() : "linear";
            int turn = obj.has("turn") ? obj.get("turn").getAsInt() : 0;
            for (Entry<String, JsonElement> entry : obj.entrySet()) {
                if (!((String) entry.getKey()).equals("tick") && !((String) entry.getKey()).equals("comment") && !((String) entry.getKey()).equals("easing") && !((String) entry.getKey()).equals("turn")) {
                    this.addBodyPartIfExists(emote, (String) entry.getKey(), (JsonElement) entry.getValue(), degrees, tick, easing, turn, version);
                }
            }
        }
    }

    private void addBodyPartIfExists(KeyframeAnimation.AnimationBuilder emote, String name, JsonElement node, boolean degrees, int tick, String easing, int turn, int version) {
        if (version < 3 && name.equals("torso")) {
            name = "body";
        }
        KeyframeAnimation.StateCollection part = emote.getOrCreatePart(name);
        JsonObject partNode = node.getAsJsonObject();
        this.addPartIfExists(part.x, "x", partNode, degrees, tick, easing, turn);
        this.addPartIfExists(part.y, "y", partNode, degrees, tick, easing, turn);
        this.addPartIfExists(part.z, "z", partNode, degrees, tick, easing, turn);
        this.addPartIfExists(part.pitch, "pitch", partNode, degrees, tick, easing, turn);
        this.addPartIfExists(part.yaw, "yaw", partNode, degrees, tick, easing, turn);
        this.addPartIfExists(part.roll, "roll", partNode, degrees, tick, easing, turn);
        this.addPartIfExists(part.bend, "bend", partNode, degrees, tick, easing, turn);
        this.addPartIfExists(part.bendDirection, "axis", partNode, degrees, tick, easing, turn);
    }

    private void addPartIfExists(KeyframeAnimation.StateCollection.State part, String name, JsonObject node, boolean degrees, int tick, String easing, int turn) {
        if (node.has(name)) {
            part.addKeyFrame(tick, node.get(name).getAsFloat(), Easing.easeFromString(easing), turn, degrees);
        }
    }

    public JsonElement serialize(KeyframeAnimation emote, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject node = new JsonObject();
        KeyframeAnimation.StateCollection torso = emote.getPart("torso");
        if (torso != null && torso.isEnabled()) {
            node.addProperty("version", 3);
        } else {
            node.addProperty("version", emote.isEasingBefore ? 2 : 1);
        }
        emote.extraData.forEach((s, o) -> {
            if (o instanceof String) {
                node.add(s, asJson((String) o));
            } else if (o instanceof Number) {
                node.addProperty(s, (Number) o);
            } else if (o instanceof Boolean) {
                node.addProperty(s, (Boolean) o);
            } else if (o instanceof JsonElement) {
                node.add(s, (JsonElement) o);
            }
        });
        node.add("emote", emoteSerializer(emote));
        return node;
    }

    public static JsonElement asJson(String str) {
        return JsonParser.parseString(str);
    }

    public static JsonObject emoteSerializer(KeyframeAnimation emote) {
        JsonObject node = new JsonObject();
        node.addProperty("beginTick", emote.beginTick);
        node.addProperty("endTick", emote.endTick);
        node.addProperty("stopTick", emote.stopTick);
        node.addProperty("isLoop", emote.isInfinite);
        node.addProperty("returnTick", emote.returnToTick);
        node.addProperty("nsfw", emote.nsfw);
        node.addProperty("degrees", false);
        if (emote.isEasingBefore) {
            node.addProperty("easeBeforeKeyframe", true);
        }
        node.add("moves", moveSerializer(emote));
        return node;
    }

    public static JsonArray moveSerializer(KeyframeAnimation emote) {
        JsonArray node = new JsonArray();
        emote.getBodyParts().forEach((s, stateCollection) -> bodyPartSerializer(node, stateCollection, s));
        return node;
    }

    private static void bodyPartSerializer(JsonArray node, KeyframeAnimation.StateCollection bodyPart, String partName) {
        partSerialize(node, bodyPart.x, partName);
        partSerialize(node, bodyPart.y, partName);
        partSerialize(node, bodyPart.z, partName);
        partSerialize(node, bodyPart.pitch, partName);
        partSerialize(node, bodyPart.yaw, partName);
        partSerialize(node, bodyPart.roll, partName);
        if (bodyPart.isBendable) {
            partSerialize(node, bodyPart.bend, partName);
            partSerialize(node, bodyPart.bendDirection, partName);
        }
    }

    private static void partSerialize(JsonArray array, KeyframeAnimation.StateCollection.State part, String partName) {
        for (KeyframeAnimation.KeyFrame keyFrame : part.getKeyFrames()) {
            JsonObject node = new JsonObject();
            node.addProperty("tick", keyFrame.tick);
            node.addProperty("easing", keyFrame.ease.toString());
            JsonObject jsonMove = new JsonObject();
            jsonMove.addProperty(part.name, keyFrame.value);
            node.add(partName, jsonMove);
            array.add(node);
        }
    }
}