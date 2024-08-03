package snownee.loquat.spawner;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;
import java.util.function.ToIntFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import snownee.loquat.util.LoquatUtil;

public class Difficulty {

    public static final ResourceLocation DEFAULT_ID = new ResourceLocation("default");

    public Difficulty.Provider provider;

    public Difficulty.DifficultyLevel[] levels;

    public Difficulty.DifficultyLevel getLevel(ServerLevel world) {
        int level = this.provider.applyAsInt(world);
        return this.levels[Mth.clamp(level, 1, this.levels.length) - 1];
    }

    public static class DifficultyLevel {

        public float amount = 1.0F;

        public float hp = 1.0F;

        public LivingEntity apply(LivingEntity entity) {
            float hpRatio = entity.getHealth() / entity.getMaxHealth();
            entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double) (entity.getMaxHealth() * this.hp));
            entity.setHealth(entity.getMaxHealth() * hpRatio);
            return entity;
        }
    }

    public static class DifficultyProviderSerializer implements JsonDeserializer<Difficulty.Provider> {

        public Difficulty.Provider deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    return level -> primitive.getAsInt();
                }
                if (primitive.isString()) {
                    String cmd = primitive.getAsString();
                    return level -> LoquatUtil.runCommandSilently(level, cmd);
                }
            }
            throw new JsonParseException("Invalid difficulty provider");
        }
    }

    public interface Provider extends ToIntFunction<ServerLevel> {
    }
}