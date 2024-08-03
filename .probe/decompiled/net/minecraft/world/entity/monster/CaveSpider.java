package net.minecraft.world.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class CaveSpider extends Spider {

    public CaveSpider(EntityType<? extends CaveSpider> entityTypeExtendsCaveSpider0, Level level1) {
        super(entityTypeExtendsCaveSpider0, level1);
    }

    public static AttributeSupplier.Builder createCaveSpider() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 12.0);
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        if (super.m_7327_(entity0)) {
            if (entity0 instanceof LivingEntity) {
                int $$1 = 0;
                if (this.m_9236_().m_46791_() == Difficulty.NORMAL) {
                    $$1 = 7;
                } else if (this.m_9236_().m_46791_() == Difficulty.HARD) {
                    $$1 = 15;
                }
                if ($$1 > 0) {
                    ((LivingEntity) entity0).addEffect(new MobEffectInstance(MobEffects.POISON, $$1 * 20, 0), this);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        return spawnGroupData3;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 0.45F;
    }
}