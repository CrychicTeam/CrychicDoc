package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntitySeal;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class SealAIDiveForItems extends Goal {

    private final EntitySeal seal;

    private Player thrower;

    private BlockPos digPos;

    private boolean returnToPlayer = false;

    private int digTime = 0;

    public static final ResourceLocation SEAL_REWARD = new ResourceLocation("alexsmobs", "gameplay/seal_reward");

    public SealAIDiveForItems(EntitySeal seal) {
        this.seal = seal;
    }

    private static List<ItemStack> getItemStacks(EntitySeal seal) {
        LootTable loottable = seal.m_9236_().getServer().getLootData().m_278676_(SEAL_REWARD);
        return loottable.getRandomItems(new LootParams.Builder((ServerLevel) seal.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, seal).create(LootContextParamSets.PIGLIN_BARTER));
    }

    @Override
    public boolean canUse() {
        if (this.seal.feederUUID != null && this.seal.m_9236_().m_46003_(this.seal.feederUUID) != null && this.seal.revengeCooldown <= 0) {
            this.thrower = this.seal.m_9236_().m_46003_(this.seal.feederUUID);
            this.digPos = this.genDigPos();
            return this.thrower != null && this.digPos != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.seal.m_5448_() == null && this.seal.revengeCooldown == 0 && this.seal.m_21188_() == null && this.thrower != null && this.seal.feederUUID != null && this.digPos != null && this.seal.m_9236_().getFluidState(this.digPos.above()).is(FluidTags.WATER);
    }

    @Override
    public void tick() {
        this.seal.setBasking(false);
        if (this.returnToPlayer) {
            this.seal.m_21573_().moveTo(this.thrower, 1.0);
            if ((double) this.seal.m_20270_(this.thrower) < 2.0) {
                ItemStack stack = this.seal.m_21205_().copy();
                this.seal.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                ItemEntity item = this.seal.m_19983_(stack);
                if (item != null) {
                    double d0 = this.thrower.m_20185_() - this.seal.m_20185_();
                    double d1 = this.thrower.m_20188_() - this.seal.m_20188_();
                    double d2 = this.thrower.m_20189_() - this.seal.m_20189_();
                    double lvt_7_1_ = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
                    float pitch = (float) (-(Mth.atan2(d1, lvt_7_1_) * 180.0F / (float) Math.PI));
                    float yaw = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                    float f8 = Mth.sin(pitch * (float) (Math.PI / 180.0));
                    float f2 = Mth.cos(pitch * (float) (Math.PI / 180.0));
                    float f3 = Mth.sin(yaw * (float) (Math.PI / 180.0));
                    float f4 = Mth.cos(yaw * (float) (Math.PI / 180.0));
                    float f5 = this.seal.m_217043_().nextFloat() * (float) (Math.PI * 2);
                    float f6 = 0.02F * this.seal.m_217043_().nextFloat();
                    item.m_20334_((double) (-f3 * f2 * 0.5F) + Math.cos((double) f5) * (double) f6, (double) (-f8 * 0.2F + 0.1F + (this.seal.m_217043_().nextFloat() - this.seal.m_217043_().nextFloat()) * 0.1F), (double) (f4 * f2 * 0.5F) + Math.sin((double) f5) * (double) f6);
                }
                this.seal.feederUUID = null;
                this.stop();
            }
        } else {
            double dist = this.seal.m_20238_(Vec3.atCenterOf(this.digPos.above()));
            double d0 = (double) this.digPos.m_123341_() + 0.5 - this.seal.m_20185_();
            double d1 = (double) this.digPos.m_123342_() + 0.5 - this.seal.m_20188_();
            double d2 = (double) this.digPos.m_123343_() + 0.5 - this.seal.m_20189_();
            float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
            if (dist < 2.0) {
                this.seal.m_21573_().stop();
                this.digTime++;
                if (this.digTime % 5 == 0) {
                    SoundEvent sound = this.seal.m_9236_().getBlockState(this.digPos).m_60827_().getHitSound();
                    this.seal.m_5496_(sound, 1.0F, 0.5F + this.seal.m_217043_().nextFloat() * 0.5F);
                }
                if (this.digTime >= 100) {
                    List<ItemStack> lootList = getItemStacks(this.seal);
                    if (lootList.size() > 0) {
                        ItemStack copy = (ItemStack) lootList.remove(0);
                        copy = copy.copy();
                        this.seal.m_21008_(InteractionHand.MAIN_HAND, copy);
                        for (ItemStack stack : lootList) {
                            this.seal.m_19983_(stack.copy());
                        }
                        this.returnToPlayer = true;
                    }
                    this.seal.setDigging(false);
                    this.digTime = 0;
                } else {
                    this.seal.setDigging(true);
                }
            } else {
                this.seal.setDigging(false);
                this.seal.m_21573_().moveTo((double) this.digPos.m_123341_(), (double) this.digPos.m_123342_(), (double) this.digPos.m_123343_(), 1.0);
                this.seal.m_146922_(f);
            }
        }
    }

    @Override
    public void stop() {
        this.seal.setDigging(false);
        this.digPos = null;
        this.thrower = null;
        this.digTime = 0;
        this.returnToPlayer = false;
        this.seal.fishFeedings = 0;
        if (!this.seal.m_21205_().isEmpty()) {
            this.seal.m_19983_(this.seal.m_21205_().copy());
            this.seal.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
    }

    private BlockPos genSeafloorPos(BlockPos parent) {
        LevelAccessor world = this.seal.m_9236_();
        RandomSource random = this.seal.m_217043_();
        int range = 15;
        for (int i = 0; i < 15; i++) {
            BlockPos seafloor = parent.offset(random.nextInt(range) - range / 2, 0, random.nextInt(range) - range / 2);
            while (world.m_6425_(seafloor).is(FluidTags.WATER) && seafloor.m_123342_() > 1) {
                seafloor = seafloor.below();
            }
            BlockState state = world.m_8055_(seafloor);
            if (state.m_204336_(AMTagRegistry.SEAL_DIGABLES)) {
                return seafloor;
            }
        }
        return null;
    }

    private BlockPos genDigPos() {
        RandomSource random = this.seal.m_217043_();
        int range = 15;
        if (this.seal.m_20069_()) {
            return this.genSeafloorPos(this.seal.m_20183_());
        } else {
            for (int i = 0; i < 15; i++) {
                BlockPos blockpos1 = this.seal.m_20183_().offset(random.nextInt(range) - range / 2, 3, random.nextInt(range) - range / 2);
                while (this.seal.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 1) {
                    blockpos1 = blockpos1.below();
                }
                if (this.seal.m_9236_().getFluidState(blockpos1).is(FluidTags.WATER)) {
                    BlockPos pos3 = this.genSeafloorPos(blockpos1);
                    if (pos3 != null) {
                        return pos3;
                    }
                }
            }
            return null;
        }
    }
}