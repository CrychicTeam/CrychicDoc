package se.mickelus.tetra.items.modular.impl.toolbelt.suspend;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.minecraftforge.common.ForgeMod;
import se.mickelus.tetra.effect.gui.EffectUnRenderer;

@ParametersAreNonnullByDefault
public class SuspendPotionEffect extends MobEffect {

    public static final String identifier = "suspended";

    public static SuspendPotionEffect instance;

    public SuspendPotionEffect() {
        super(MobEffectCategory.BENEFICIAL, 26112);
        this.m_19472_(ForgeMod.ENTITY_GRAVITY.get(), "07607dcd-4ee5-42b1-bc39-90a7bf06b4b5", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        instance = this;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.f_19789_ = 0.0F;
        if (entity.m_20096_()) {
            entity.removeEffect(this);
        } else {
            Vec3 motion = entity.m_20184_();
            double dy = motion.y;
            if (entity.m_6047_()) {
                entity.m_20334_(motion.x, Math.max(-0.3, dy - 0.05), motion.z);
            } else {
                entity.m_20334_(motion.x, Math.abs(dy) > 0.02 ? dy * 0.9 : 0.0, motion.z);
            }
            MobEffectInstance effectInstance = entity.getEffect(this);
            if (effectInstance != null && effectInstance.getDuration() < 20) {
                if (SuspendEffect.canSuspend((Player) entity)) {
                    entity.addEffect(new MobEffectInstance(instance, 100, 0, false, false));
                } else {
                    entity.removeEffect(this);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(EffectUnRenderer.INSTANCE);
    }
}