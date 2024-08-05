package com.github.alexthe666.alexsmobs.effect;

import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityTarantulaHawk;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

public class EffectDebilitatingSting extends MobEffect {

    private int lastDuration = -1;

    protected EffectDebilitatingSting() {
        super(MobEffectCategory.NEUTRAL, 16774021);
        this.m_19472_(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -1.0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        if (entityLivingBaseIn.getMobType() == MobType.ARTHROPOD) {
            super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        }
    }

    @Override
    public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        if (entityLivingBaseIn.getMobType() == MobType.ARTHROPOD) {
            super.addAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        }
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getMobType() != MobType.ARTHROPOD) {
            if (entity.getHealth() > entity.getMaxHealth() * 0.5F) {
                entity.hurt(entity.m_269291_().magic(), 1.0F);
            }
        } else {
            boolean suf = this.isEntityInsideOpaqueBlock(entity);
            if (suf) {
                entity.m_20256_(Vec3.ZERO);
                entity.f_19794_ = true;
            }
            entity.m_20242_(suf);
            entity.setJumping(false);
            if (!entity.m_20159_() && entity instanceof Mob && ((Mob) entity).getMoveControl().getClass() != MoveControl.class) {
                entity.m_20256_(new Vec3(0.0, -1.0, 0.0));
            }
            if (this.lastDuration == 1) {
                entity.hurt(entity.m_269291_().magic(), (float) ((amplifier + 1) * 30));
                if (amplifier > 0) {
                    BlockPos surface = entity.m_20183_();
                    while (!entity.m_9236_().m_46859_(surface) && surface.m_123342_() < 256) {
                        surface = surface.above();
                    }
                    EntityTarantulaHawk baby = AMEntityRegistry.TARANTULA_HAWK.get().create(entity.m_9236_());
                    baby.m_6863_(true);
                    baby.m_6034_(entity.m_20185_(), (double) ((float) surface.m_123342_() + 0.1F), entity.m_20189_());
                    if (!entity.m_9236_().isClientSide) {
                        baby.finalizeSpawn((ServerLevelAccessor) entity.m_9236_(), entity.m_9236_().getCurrentDifficultyAt(entity.m_20183_()), MobSpawnType.BREEDING, null, null);
                        entity.m_9236_().m_7967_(baby);
                    }
                }
                entity.m_20242_(false);
                entity.f_19794_ = false;
            }
        }
    }

    public boolean isEntityInsideOpaqueBlock(Entity entity) {
        Vec3 vec3 = entity.getEyePosition();
        float f = entity.getDimensions(entity.getPose()).width * 0.8F;
        AABB axisalignedbb = AABB.ofSize(vec3, (double) f, 1.0E-6, (double) f);
        return entity.level().m_45556_(axisalignedbb).filter(Predicate.not(BlockBehaviour.BlockStateBase::m_60795_)).anyMatch(p_185969_ -> {
            BlockPos blockpos = AMBlockPos.fromVec3(vec3);
            return p_185969_.m_60828_(entity.level(), blockpos) && Shapes.joinIsNotEmpty(p_185969_.m_60812_(entity.level(), blockpos).move(vec3.x, vec3.y, vec3.z), Shapes.create(axisalignedbb), BooleanOp.AND);
        });
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        this.lastDuration = duration;
        return duration > 0;
    }

    @Override
    public String getDescriptionId() {
        return "alexsmobs.potion.debilitating_sting";
    }
}