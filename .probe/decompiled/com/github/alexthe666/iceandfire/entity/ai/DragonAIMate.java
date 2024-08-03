package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DragonAIMate extends Goal {

    private static final BlockState NEST = IafBlockRegistry.NEST.get().defaultBlockState();

    private final EntityDragonBase dragon;

    Level theWorld;

    int spawnBabyDelay;

    double moveSpeed;

    private EntityDragonBase targetMate;

    public DragonAIMate(EntityDragonBase dragon, double speedIn) {
        this.dragon = dragon;
        this.theWorld = dragon.m_9236_();
        this.moveSpeed = speedIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.dragon.m_27593_() && this.dragon.canMove()) {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        } else {
            return false;
        }
    }

    public boolean continueExecuting() {
        return this.targetMate.isAlive() && this.targetMate.m_27593_() && this.spawnBabyDelay < 60;
    }

    @Override
    public void stop() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    @Override
    public void tick() {
        this.dragon.m_21563_().setLookAt(this.targetMate, 10.0F, (float) this.dragon.m_8132_());
        this.dragon.m_21573_().moveTo(this.targetMate.m_20185_(), this.targetMate.m_20186_(), this.targetMate.m_20189_(), this.moveSpeed);
        this.dragon.setFlying(false);
        this.dragon.setHovering(false);
        this.spawnBabyDelay++;
        if (this.spawnBabyDelay >= 60 && this.dragon.m_20270_(this.targetMate) < 35.0F) {
            this.spawnBaby();
        }
    }

    private EntityDragonBase getNearbyMate() {
        List<? extends EntityDragonBase> list = this.theWorld.m_45976_(this.dragon.getClass(), this.dragon.m_20191_().inflate(180.0, 180.0, 180.0));
        double d0 = Double.MAX_VALUE;
        EntityDragonBase mate = null;
        for (EntityDragonBase partner : list) {
            if (this.dragon.canMate(partner)) {
                double d1 = this.dragon.m_20280_(partner);
                if (d1 < d0) {
                    mate = partner;
                    d0 = d1;
                }
            }
        }
        return mate;
    }

    private void spawnBaby() {
        EntityDragonEgg egg = this.dragon.createEgg(this.targetMate);
        if (egg != null) {
            this.dragon.m_146762_(6000);
            this.targetMate.m_146762_(6000);
            this.dragon.m_27594_();
            this.targetMate.m_27594_();
            int nestX = (int) (this.dragon.isMale() ? this.targetMate.m_20185_() : this.dragon.m_20185_());
            int nestY = (int) (this.dragon.isMale() ? this.targetMate.m_20186_() : this.dragon.m_20186_()) - 1;
            int nestZ = (int) (this.dragon.isMale() ? this.targetMate.m_20189_() : this.dragon.m_20189_());
            egg.m_7678_((double) ((float) nestX - 0.5F), (double) ((float) nestY + 1.0F), (double) ((float) nestZ - 0.5F), 0.0F, 0.0F);
            this.theWorld.m_7967_(egg);
            RandomSource random = this.dragon.m_217043_();
            for (int i = 0; i < 17; i++) {
                double d0 = random.nextGaussian() * 0.02;
                double d1 = random.nextGaussian() * 0.02;
                double d2 = random.nextGaussian() * 0.02;
                double d3 = random.nextDouble() * (double) this.dragon.m_20205_() * 2.0 - (double) this.dragon.m_20205_();
                double d4 = 0.5 + random.nextDouble() * (double) this.dragon.m_20206_();
                double d5 = random.nextDouble() * (double) this.dragon.m_20205_() * 2.0 - (double) this.dragon.m_20205_();
                this.theWorld.addParticle(ParticleTypes.HEART, this.dragon.m_20185_() + d3, this.dragon.m_20186_() + d4, this.dragon.m_20189_() + d5, d0, d1, d2);
            }
            BlockPos eggPos = new BlockPos(nestX - 2, nestY, nestZ - 2);
            BlockPos dirtPos = eggPos.offset(1, 0, 1);
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    BlockPos add = eggPos.offset(x, 0, z);
                    BlockState prevState = this.theWorld.getBlockState(add);
                    if (prevState.m_247087_() || this.theWorld.getBlockState(add).m_204336_(BlockTags.DIRT) || this.theWorld.getBlockState(add).m_60800_(this.theWorld, add) < 5.0F || this.theWorld.getBlockState(add).m_60800_(this.theWorld, add) >= 0.0F) {
                        this.theWorld.setBlockAndUpdate(add, NEST);
                    }
                }
            }
            if (this.theWorld.getBlockState(dirtPos).m_247087_() || this.theWorld.getBlockState(dirtPos) == NEST) {
                this.theWorld.setBlockAndUpdate(dirtPos, Blocks.DIRT_PATH.defaultBlockState());
            }
            if (this.theWorld.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.theWorld.m_7967_(new ExperienceOrb(this.theWorld, this.dragon.m_20185_(), this.dragon.m_20186_(), this.dragon.m_20189_(), random.nextInt(15) + 10));
            }
        }
    }
}