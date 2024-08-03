package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.block.BlockLeafcutterAntChamber;
import com.github.alexthe666.alexsmobs.block.BlockLeafcutterAnthill;
import com.github.alexthe666.alexsmobs.entity.EntityAnteater;
import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityLeafcutterAnthill;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class AnteaterAIRaidNest extends MoveToBlockGoal {

    public static final ResourceLocation ANTEATER_REWARD = new ResourceLocation("alexsmobs", "gameplay/anteater_reward");

    private final EntityAnteater anteater;

    private int idleAtHiveTime = 0;

    private boolean isAboveDestinationAnteater;

    private boolean shootTongue;

    private int maxEatingTime = 0;

    public AnteaterAIRaidNest(EntityAnteater anteater) {
        super(anteater, 1.0, 32, 8);
        this.anteater = anteater;
    }

    private static List<ItemStack> getItemStacks(EntityAnteater anteater) {
        LootTable loottable = anteater.m_9236_().getServer().getLootData().m_278676_(ANTEATER_REWARD);
        return loottable.getRandomItems(new LootParams.Builder((ServerLevel) anteater.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, anteater).create(LootContextParamSets.PIGLIN_BARTER));
    }

    private void dropDigItems() {
        List<ItemStack> lootList = getItemStacks(this.anteater);
        if (lootList.size() > 0) {
            for (ItemStack stack : lootList) {
                ItemEntity e = this.anteater.m_19983_(stack.copy());
                e.f_19812_ = true;
                e.m_20256_(e.m_20184_().multiply(0.2, 0.2, 0.2));
            }
        }
    }

    @Override
    public boolean canUse() {
        return !this.anteater.m_6162_() && super.canUse() && this.anteater.eatAntCooldown <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.anteater.eatAntCooldown <= 0;
    }

    @Override
    public void start() {
        super.start();
        this.maxEatingTime = 150 + this.anteater.m_217043_().nextInt(200);
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.idleAtHiveTime = 0;
        this.maxEatingTime = 150 + this.anteater.m_217043_().nextInt(200);
        this.anteater.setLeaning(false);
        this.anteater.resetAntCooldown();
    }

    @Override
    public double acceptedDistance() {
        return 1.2;
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos blockpos = this.m_6669_();
        if (!this.isWithinXZDist(blockpos, this.f_25598_.m_20182_(), this.acceptedDistance())) {
            this.isAboveDestinationAnteater = false;
            this.f_25601_++;
            if (this.m_8064_()) {
                this.f_25598_.m_21573_().moveTo((double) ((float) blockpos.m_123341_()) + 0.5, (double) blockpos.m_123342_(), (double) ((float) blockpos.m_123343_()) + 0.5, this.f_25599_);
            }
        } else {
            this.isAboveDestinationAnteater = true;
            this.f_25601_--;
        }
        if (this.isReachedTarget()) {
            this.anteater.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double) this.f_25602_.m_123341_() + 0.5, (double) (this.f_25602_.m_123342_() - 1), (double) this.f_25602_.m_123343_() + 0.5));
            if (this.idleAtHiveTime >= 20 && this.idleAtHiveTime % 20 == 0) {
                this.shootTongue = this.anteater.m_217043_().nextInt(2) == 0;
                if (this.shootTongue) {
                    this.eatHive();
                } else {
                    this.breakHiveEffect();
                }
            }
            this.idleAtHiveTime++;
            if (this.shootTongue && this.anteater.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.anteater.setLeaning(false);
                this.anteater.setAnimation(EntityAnteater.ANIMATION_TOUNGE_IDLE);
            } else if (this.anteater.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.anteater.setLeaning(true);
                this.anteater.setAnimation(this.anteater.m_217043_().nextBoolean() ? EntityAnteater.ANIMATION_SLASH_L : EntityAnteater.ANIMATION_SLASH_R);
            }
            if (this.idleAtHiveTime > this.maxEatingTime) {
                this.stop();
            }
        }
    }

    private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
        return blockpos.m_123331_(AMBlockPos.fromCoords(positionVec.x(), (double) blockpos.m_123342_(), positionVec.z())) < distance * distance;
    }

    @Override
    protected boolean isReachedTarget() {
        return this.isAboveDestinationAnteater;
    }

    private void breakHiveEffect() {
        if (ForgeEventFactory.getMobGriefingEvent(this.anteater.m_9236_(), this.anteater)) {
            BlockState blockstate = this.anteater.m_9236_().getBlockState(this.f_25602_);
            if (blockstate.m_60713_(AMBlockRegistry.LEAFCUTTER_ANTHILL.get())) {
                if (this.anteater.m_9236_().getBlockEntity(this.f_25602_) instanceof TileEntityLeafcutterAnthill) {
                    TileEntityLeafcutterAnthill anthill = (TileEntityLeafcutterAnthill) this.anteater.m_9236_().getBlockEntity(this.f_25602_);
                    anthill.angerAntsBecauseAnteater(this.anteater, blockstate, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                    this.anteater.m_9236_().m_46961_(this.f_25602_, false);
                    if (blockstate.m_60734_() instanceof BlockLeafcutterAnthill) {
                        this.anteater.m_9236_().setBlockAndUpdate(this.f_25602_, blockstate);
                    }
                    this.dropDigItems();
                }
            } else if (blockstate.m_60713_(AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get())) {
                this.anteater.m_9236_().m_46961_(this.f_25602_, false);
                this.anteater.m_9236_().setBlockAndUpdate(this.f_25602_, blockstate);
            }
        }
    }

    private void eatHive() {
        if (ForgeEventFactory.getMobGriefingEvent(this.anteater.m_9236_(), this.anteater)) {
            BlockState blockstate = this.anteater.m_9236_().getBlockState(this.f_25602_);
            if (blockstate.m_60713_(AMBlockRegistry.LEAFCUTTER_ANTHILL.get())) {
                if (this.anteater.m_9236_().getBlockEntity(this.f_25602_) instanceof TileEntityLeafcutterAnthill) {
                    RandomSource rand = this.anteater.m_217043_();
                    TileEntityLeafcutterAnthill anthill = (TileEntityLeafcutterAnthill) this.anteater.m_9236_().getBlockEntity(this.f_25602_);
                    anthill.angerAntsBecauseAnteater(this.anteater, blockstate, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                    this.anteater.m_9236_().updateNeighbourForOutputSignal(this.f_25602_, blockstate.m_60734_());
                    if (!anthill.hasNoAnts()) {
                        BlockState state = anthill.shrinkFungus();
                        if (state != null && state.m_60713_(AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get()) && (Integer) state.m_61143_(BlockLeafcutterAntChamber.FUNGUS) >= 5) {
                            ItemStack stack = new ItemStack(AMItemRegistry.GONGYLIDIA.get());
                            ItemEntity itementity = new ItemEntity(this.anteater.m_9236_(), (double) ((float) this.f_25602_.m_123341_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123342_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123343_() + rand.nextFloat()), stack);
                            itementity.setDefaultPickUpDelay();
                            this.anteater.m_9236_().m_7967_(itementity);
                        }
                        this.anteater.setAntOnTongue(true);
                    }
                }
            } else if (blockstate.m_60713_(AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get())) {
                this.anteater.m_9236_().m_46961_(this.f_25602_, false);
                if ((Integer) blockstate.m_61143_(BlockLeafcutterAntChamber.FUNGUS) >= 5) {
                    RandomSource rand = this.anteater.m_217043_();
                    ItemStack stack = new ItemStack(AMItemRegistry.GONGYLIDIA.get());
                    ItemEntity itementity = new ItemEntity(this.anteater.m_9236_(), (double) ((float) this.f_25602_.m_123341_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123342_() + rand.nextFloat()), (double) ((float) this.f_25602_.m_123343_() + rand.nextFloat()), stack);
                    itementity.setDefaultPickUpDelay();
                    this.anteater.m_9236_().m_7967_(itementity);
                }
                this.anteater.m_9236_().setBlockAndUpdate(this.f_25602_, Blocks.COARSE_DIRT.defaultBlockState());
                this.anteater.setAntOnTongue(true);
            }
            double d0 = 15.0;
            for (EntityLeafcutterAnt leafcutter : this.anteater.m_9236_().m_45976_(EntityLeafcutterAnt.class, new AABB((double) this.f_25602_.m_123341_() - d0, (double) this.f_25602_.m_123342_() - d0, (double) this.f_25602_.m_123343_() - d0, (double) this.f_25602_.m_123341_() + d0, (double) this.f_25602_.m_123342_() + d0, (double) this.f_25602_.m_123343_() + d0))) {
                leafcutter.setRemainingPersistentAngerTime(100);
                leafcutter.setTarget(this.anteater);
                leafcutter.setStayOutOfHiveCountdown(400);
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return worldIn.m_8055_(pos).m_60713_(AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get()) || worldIn.m_8055_(pos).m_60713_(AMBlockRegistry.LEAFCUTTER_ANTHILL.get()) && worldIn.m_7702_(pos) instanceof TileEntityLeafcutterAnthill && this.isValidAnthill(pos, (TileEntityLeafcutterAnthill) worldIn.m_7702_(pos));
    }

    private boolean isValidAnthill(BlockPos pos, TileEntityLeafcutterAnthill blockEntity) {
        return blockEntity.hasAtleastThisManyAnts(2);
    }
}