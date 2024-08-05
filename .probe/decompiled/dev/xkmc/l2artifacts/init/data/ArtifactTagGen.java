package dev.xkmc.l2artifacts.init.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider.IntrinsicImpl;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2complements.init.data.TagGen;
import net.minecraft.world.effect.MobEffect;

public class ArtifactTagGen {

    public static void onEffectTagGen(IntrinsicImpl<MobEffect> pvd) {
        pvd.addTag(TagGen.SKILL_EFFECT).add((MobEffect) ArtifactEffects.THERMAL_MOTIVE.get()).add((MobEffect) ArtifactEffects.FROST_SHIELD.get());
    }
}