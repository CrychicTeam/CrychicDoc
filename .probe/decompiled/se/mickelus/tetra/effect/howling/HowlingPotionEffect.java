package se.mickelus.tetra.effect.howling;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import se.mickelus.mutil.effect.EffectTooltipRenderer;

@ParametersAreNonnullByDefault
public class HowlingPotionEffect extends MobEffect {

    public static final String identifier = "howling";

    public static HowlingPotionEffect instance;

    public HowlingPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 15658734);
        this.m_19472_(Attributes.MOVEMENT_SPEED, "f80b9432-480d-4846-b9f9-178157dbac07", -0.05, AttributeModifier.Operation.MULTIPLY_BASE);
        instance = this;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.m_9236_().isClientSide) {
            double offset = (Math.PI * 4) / (double) (amplifier + 1);
            for (int i = 0; i < (amplifier + 1) / 2; i++) {
                double time = (double) System.currentTimeMillis() / 1000.0 * Math.PI + offset * (double) i;
                double xOffset = -Math.cos(time);
                double zOffset = Math.sin(time);
                Vec3 pos = entity.m_20182_().add(xOffset, 0.1 + Math.random() * (double) entity.m_20206_(), zOffset);
                entity.m_20193_().addParticle(ParticleTypes.POOF, pos.x, pos.y, pos.z, -Math.cos(time - (Math.PI / 2)) * 0.1, 0.01, Math.sin(time - (Math.PI / 2)) * 0.1);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new EffectTooltipRenderer(effect -> {
            int amp = effect.getAmplifier() + 1;
            return I18n.get("effect.tetra.howling.tooltip", String.format("%d", amp * -5), String.format("%.01f", Math.min((double) amp * 12.5, 100.0)), String.format("%.01f", (double) amp * 2.5));
        }));
    }
}