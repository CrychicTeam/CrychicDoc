package se.mickelus.tetra.effect.potion;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import se.mickelus.tetra.TetraDamageTypes;
import se.mickelus.tetra.effect.gui.EffectUnRenderer;

@ParametersAreNonnullByDefault
public class BleedingPotionEffect extends MobEffect {

    public static final String identifier = "bleeding";

    public static BleedingPotionEffect instance;

    public BleedingPotionEffect() {
        super(MobEffectCategory.HARMFUL, 8912896);
        instance = this;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        DamageSource source = entity.m_9236_().damageSources().source(TetraDamageTypes.bleeding);
        entity.hurt(source, (float) amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(EffectUnRenderer.INSTANCE);
    }
}