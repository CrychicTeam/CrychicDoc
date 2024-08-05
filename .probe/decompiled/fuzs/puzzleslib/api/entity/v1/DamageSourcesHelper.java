package fuzs.puzzleslib.api.entity.v1;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

public final class DamageSourcesHelper {

    private DamageSourcesHelper() {
    }

    public static DamageSource source(LevelReader level, ResourceKey<DamageType> damageType) {
        return source(level, damageType, null, null);
    }

    public static DamageSource source(LevelReader level, ResourceKey<DamageType> damageType, @Nullable Entity directEntity) {
        return source(level, damageType, directEntity, directEntity);
    }

    public static DamageSource source(LevelReader level, ResourceKey<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        return source(level.registryAccess(), damageType, directEntity, causingEntity);
    }

    public static DamageSource source(RegistryAccess registryAccess, ResourceKey<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType), directEntity, causingEntity);
    }
}