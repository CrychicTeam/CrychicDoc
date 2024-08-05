package yesman.epicfight.world.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public interface EpicFightDamageType {

    TagKey<DamageType> FINISHER = create("finisher");

    TagKey<DamageType> COUNTER = create("counter");

    TagKey<DamageType> EXECUTION = create("execution");

    TagKey<DamageType> WEAPON_INNATE = create("weapon_innate");

    TagKey<DamageType> GUARD_PUNCTURE = create("guard_puncture");

    TagKey<DamageType> PARTIAL_DAMAGE = create("partial_damage");

    private static TagKey<DamageType> create(String tagName) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("epicfight", tagName));
    }
}