package se.mickelus.tetra.effect.potion;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import se.mickelus.mutil.effect.EffectTooltipRenderer;
import se.mickelus.mutil.util.ParticleHelper;

@ParametersAreNonnullByDefault
public class PriedPotionEffect extends MobEffect {

    public static final String identifier = "pried";

    public static PriedPotionEffect instance;

    public PriedPotionEffect() {
        super(MobEffectCategory.HARMFUL, 8912896);
        this.m_19472_(Attributes.ARMOR, "8ce1d367-cb9f-48a3-a748-e6b73ef686e2", -1.0, AttributeModifier.Operation.ADDITION);
        instance = this;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.m_20193_().isClientSide) {
            ParticleHelper.spawnArmorParticles(entity);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new EffectTooltipRenderer(effect -> I18n.get("effect.tetra.pried.tooltip", effect.getAmplifier() + 1)));
    }
}