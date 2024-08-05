package vazkii.patchouli.common.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.patchouli.api.PatchouliAPI;

public final class EntityUtil {

    private EntityUtil() {
    }

    public static String getEntityName(String entityId) {
        Pair<String, String> nameAndNbt = splitNameAndNBT(entityId);
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation((String) nameAndNbt.getLeft()));
        return type.getDescriptionId();
    }

    public static Function<Level, Entity> loadEntity(String entityId) {
        Pair<String, String> nameAndNbt = splitNameAndNBT(entityId);
        entityId = (String) nameAndNbt.getLeft();
        String nbtStr = (String) nameAndNbt.getRight();
        CompoundTag nbt = null;
        if (!nbtStr.isEmpty()) {
            try {
                nbt = TagParser.parseTag(nbtStr);
            } catch (CommandSyntaxException var9) {
                PatchouliAPI.LOGGER.error("Failed to load entity data", var9);
            }
        }
        ResourceLocation key = new ResourceLocation(entityId);
        Optional<EntityType<?>> maybeType = BuiltInRegistries.ENTITY_TYPE.m_6612_(key);
        if (maybeType.isEmpty()) {
            throw new RuntimeException("Unknown entity id: " + entityId);
        } else {
            EntityType<?> type = (EntityType<?>) maybeType.get();
            CompoundTag useNbt = nbt;
            return world -> {
                try {
                    Entity entity = type.create(world);
                    if (useNbt != null) {
                        entity.load(useNbt);
                    }
                    return entity;
                } catch (Exception var6x) {
                    throw new IllegalArgumentException("Can't load entity " + entityId, var6x);
                }
            };
        }
    }

    private static Pair<String, String> splitNameAndNBT(String entityId) {
        int nbtStart = entityId.indexOf("{");
        String nbtStr = "";
        if (nbtStart > 0) {
            nbtStr = entityId.substring(nbtStart).replaceAll("([^\\\\])'", "$1\"").replaceAll("\\\\'", "'");
            entityId = entityId.substring(0, nbtStart);
        }
        return Pair.of(entityId, nbtStr);
    }
}