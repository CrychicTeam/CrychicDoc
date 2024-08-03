package se.mickelus.tetra.effect.potion;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import se.mickelus.tetra.effect.gui.EffectUnRenderer;

@ParametersAreNonnullByDefault
public class StunPotionEffect extends MobEffect {

    public static final String identifier = "stun";

    public static StunPotionEffect instance;

    public StunPotionEffect() {
        super(MobEffectCategory.HARMFUL, 15658734);
        this.m_19472_(Attributes.MOVEMENT_SPEED, "c2e930ec-9683-4bd7-bc04-8e6ff6587def", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.m_19472_(Attributes.ATTACK_DAMAGE, "d59dc254-beb1-4db6-8dfd-c55c0f5554af", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.m_19472_(Attributes.ATTACK_KNOCKBACK, "b23dcb72-baf6-4f57-b96a-60d4b629cfd6", -1.0, AttributeModifier.Operation.MULTIPLY_TOTAL);
        instance = this;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.m_20193_().isClientSide) {
            Vec3 pos = entity.m_20299_(0.0F);
            double time = (double) System.currentTimeMillis() / 1000.0 * Math.PI;
            double xOffset = Math.cos(time) * 0.4;
            double zOffset = Math.sin(time) * 0.4;
            ((ServerLevel) entity.m_20193_()).sendParticles(ParticleTypes.ENTITY_EFFECT, pos.x + xOffset, pos.y + 0.1, pos.z + zOffset, 1, 0.0, 0.0, 0.0, 0.0);
            ((ServerLevel) entity.m_20193_()).sendParticles(ParticleTypes.ENTITY_EFFECT, pos.x - xOffset, pos.y + 0.4, pos.z - zOffset, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 4 == 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(EffectUnRenderer.INSTANCE);
    }
}