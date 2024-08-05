package net.minecraft.world.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public class Giant extends Monster {

    public Giant(EntityType<? extends Giant> entityTypeExtendsGiant0, Level level1) {
        super(entityTypeExtendsGiant0, level1);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 10.440001F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100.0).add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.ATTACK_DAMAGE, 50.0);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        return levelReader1.getPathfindingCostFromLightLevels(blockPos0);
    }
}