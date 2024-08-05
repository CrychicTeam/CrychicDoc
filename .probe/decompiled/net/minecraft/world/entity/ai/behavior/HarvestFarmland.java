package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class HarvestFarmland extends Behavior<Villager> {

    private static final int HARVEST_DURATION = 200;

    public static final float SPEED_MODIFIER = 0.5F;

    @Nullable
    private BlockPos aboveFarmlandPos;

    private long nextOkStartTime;

    private int timeWorkedSoFar;

    private final List<BlockPos> validFarmlandAroundVillager = Lists.newArrayList();

    public HarvestFarmland() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryStatus.VALUE_PRESENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Villager villager1) {
        if (!serverLevel0.m_46469_().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        } else if (villager1.getVillagerData().getProfession() != VillagerProfession.FARMER) {
            return false;
        } else {
            BlockPos.MutableBlockPos $$2 = villager1.m_20183_().mutable();
            this.validFarmlandAroundVillager.clear();
            for (int $$3 = -1; $$3 <= 1; $$3++) {
                for (int $$4 = -1; $$4 <= 1; $$4++) {
                    for (int $$5 = -1; $$5 <= 1; $$5++) {
                        $$2.set(villager1.m_20185_() + (double) $$3, villager1.m_20186_() + (double) $$4, villager1.m_20189_() + (double) $$5);
                        if (this.validPos($$2, serverLevel0)) {
                            this.validFarmlandAroundVillager.add(new BlockPos($$2));
                        }
                    }
                }
            }
            this.aboveFarmlandPos = this.getValidFarmland(serverLevel0);
            return this.aboveFarmlandPos != null;
        }
    }

    @Nullable
    private BlockPos getValidFarmland(ServerLevel serverLevel0) {
        return this.validFarmlandAroundVillager.isEmpty() ? null : (BlockPos) this.validFarmlandAroundVillager.get(serverLevel0.m_213780_().nextInt(this.validFarmlandAroundVillager.size()));
    }

    private boolean validPos(BlockPos blockPos0, ServerLevel serverLevel1) {
        BlockState $$2 = serverLevel1.m_8055_(blockPos0);
        Block $$3 = $$2.m_60734_();
        Block $$4 = serverLevel1.m_8055_(blockPos0.below()).m_60734_();
        return $$3 instanceof CropBlock && ((CropBlock) $$3).isMaxAge($$2) || $$2.m_60795_() && $$4 instanceof FarmBlock;
    }

    protected void start(ServerLevel serverLevel0, Villager villager1, long long2) {
        if (long2 > this.nextOkStartTime && this.aboveFarmlandPos != null) {
            villager1.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
            villager1.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5F, 1));
        }
    }

    protected void stop(ServerLevel serverLevel0, Villager villager1, long long2) {
        villager1.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        villager1.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.timeWorkedSoFar = 0;
        this.nextOkStartTime = long2 + 40L;
    }

    protected void tick(ServerLevel serverLevel0, Villager villager1, long long2) {
        if (this.aboveFarmlandPos == null || this.aboveFarmlandPos.m_203195_(villager1.m_20182_(), 1.0)) {
            if (this.aboveFarmlandPos != null && long2 > this.nextOkStartTime) {
                BlockState $$3 = serverLevel0.m_8055_(this.aboveFarmlandPos);
                Block $$4 = $$3.m_60734_();
                Block $$5 = serverLevel0.m_8055_(this.aboveFarmlandPos.below()).m_60734_();
                if ($$4 instanceof CropBlock && ((CropBlock) $$4).isMaxAge($$3)) {
                    serverLevel0.m_46953_(this.aboveFarmlandPos, true, villager1);
                }
                if ($$3.m_60795_() && $$5 instanceof FarmBlock && villager1.hasFarmSeeds()) {
                    SimpleContainer $$6 = villager1.m_35311_();
                    for (int $$7 = 0; $$7 < $$6.getContainerSize(); $$7++) {
                        ItemStack $$8 = $$6.getItem($$7);
                        boolean $$9 = false;
                        if (!$$8.isEmpty() && $$8.is(ItemTags.VILLAGER_PLANTABLE_SEEDS) && $$8.getItem() instanceof BlockItem $$10) {
                            BlockState $$11 = $$10.getBlock().defaultBlockState();
                            serverLevel0.m_46597_(this.aboveFarmlandPos, $$11);
                            serverLevel0.m_220407_(GameEvent.BLOCK_PLACE, this.aboveFarmlandPos, GameEvent.Context.of(villager1, $$11));
                            $$9 = true;
                        }
                        if ($$9) {
                            serverLevel0.m_6263_(null, (double) this.aboveFarmlandPos.m_123341_(), (double) this.aboveFarmlandPos.m_123342_(), (double) this.aboveFarmlandPos.m_123343_(), SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
                            $$8.shrink(1);
                            if ($$8.isEmpty()) {
                                $$6.setItem($$7, ItemStack.EMPTY);
                            }
                            break;
                        }
                    }
                }
                if ($$4 instanceof CropBlock && !((CropBlock) $$4).isMaxAge($$3)) {
                    this.validFarmlandAroundVillager.remove(this.aboveFarmlandPos);
                    this.aboveFarmlandPos = this.getValidFarmland(serverLevel0);
                    if (this.aboveFarmlandPos != null) {
                        this.nextOkStartTime = long2 + 20L;
                        villager1.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(this.aboveFarmlandPos), 0.5F, 1));
                        villager1.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(this.aboveFarmlandPos));
                    }
                }
            }
            this.timeWorkedSoFar++;
        }
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Villager villager1, long long2) {
        return this.timeWorkedSoFar < 200;
    }
}