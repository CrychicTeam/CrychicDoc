package software.bernie.geckolib.loading.json.typeadapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import net.minecraft.util.GsonHelper;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.keyframe.event.data.CustomInstructionKeyframeData;
import software.bernie.geckolib.core.keyframe.event.data.ParticleKeyframeData;
import software.bernie.geckolib.core.keyframe.event.data.SoundKeyframeData;
import software.bernie.geckolib.util.JsonUtil;

public class KeyFramesAdapter implements JsonDeserializer<Animation.Keyframes> {

    public Animation.Keyframes deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        SoundKeyframeData[] sounds = buildSoundFrameData(obj);
        ParticleKeyframeData[] particles = buildParticleFrameData(obj);
        CustomInstructionKeyframeData[] customInstructions = buildCustomFrameData(obj);
        return new Animation.Keyframes(sounds, particles, customInstructions);
    }

    private static SoundKeyframeData[] buildSoundFrameData(JsonObject rootObj) {
        JsonObject soundsObj = GsonHelper.getAsJsonObject(rootObj, "sound_effects", new JsonObject());
        SoundKeyframeData[] sounds = new SoundKeyframeData[soundsObj.size()];
        int index = 0;
        for (Entry<String, JsonElement> entry : soundsObj.entrySet()) {
            sounds[index] = new SoundKeyframeData(Double.parseDouble((String) entry.getKey()) * 20.0, GsonHelper.getAsString(((JsonElement) entry.getValue()).getAsJsonObject(), "effect"));
            index++;
        }
        return sounds;
    }

    private static ParticleKeyframeData[] buildParticleFrameData(JsonObject rootObj) {
        JsonObject particlesObj = GsonHelper.getAsJsonObject(rootObj, "particle_effects", new JsonObject());
        ParticleKeyframeData[] particles = new ParticleKeyframeData[particlesObj.size()];
        int index = 0;
        for (Entry<String, JsonElement> entry : particlesObj.entrySet()) {
            JsonObject obj = ((JsonElement) entry.getValue()).getAsJsonObject();
            String effect = GsonHelper.getAsString(obj, "effect", "");
            String locator = GsonHelper.getAsString(obj, "locator", "");
            String script = GsonHelper.getAsString(obj, "pre_effect_script", "");
            particles[index] = new ParticleKeyframeData(Double.parseDouble((String) entry.getKey()) * 20.0, effect, locator, script);
            index++;
        }
        return particles;
    }

    private static CustomInstructionKeyframeData[] buildCustomFrameData(JsonObject rootObj) {
        JsonObject customInstructionsObj = GsonHelper.getAsJsonObject(rootObj, "timeline", new JsonObject());
        CustomInstructionKeyframeData[] customInstructions = new CustomInstructionKeyframeData[customInstructionsObj.size()];
        int index = 0;
        for (Entry<String, JsonElement> entry : customInstructionsObj.entrySet()) {
            String instructions = "";
            if (entry.getValue() instanceof JsonArray array) {
                instructions = ((ObjectArrayList) JsonUtil.GEO_GSON.fromJson(array, ObjectArrayList.class)).toString();
            } else if (entry.getValue() instanceof JsonPrimitive primitive) {
                instructions = primitive.getAsString();
            }
            customInstructions[index] = new CustomInstructionKeyframeData(Double.parseDouble((String) entry.getKey()) * 20.0, instructions);
            index++;
        }
        return customInstructions;
    }
}