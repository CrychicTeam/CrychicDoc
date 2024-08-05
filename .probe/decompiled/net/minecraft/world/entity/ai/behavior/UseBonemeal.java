package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class UseBonemeal extends Behavior<Villager> {

    private static final int BONEMEALING_DURATION = 80;

    private long nextWorkCycleTime;

    private long lastBonemealingSession;

    private int timeWorkedSoFar;

    private Optional<BlockPos> cropPos = Optional.empty();

    public UseBonemeal() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Villager villager1) {
        if (villager1.f_19797_ % 10 == 0 && (this.lastBonemealingSession == 0L || this.lastBonemealingSession + 160L <= (long) villager1.f_19797_)) {
            if (villager1.m_35311_().m_18947_(Items.BONE_MEAL) <= 0) {
                return false;
            } else {
                this.cropPos = this.pickNextTarget(serverLevel0, villager1);
                return this.cropPos.isPresent();
            }
        } else {
            return false;
        }
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Villager villager1, long long2) {
        return this.timeWorkedSoFar < 80 && this.cropPos.isPresent();
    }

    private Optional<BlockPos> pickNextTarget(ServerLevel serverLevel0, Villager villager1) {
        BlockPos.MutableBlockPos $$2 = new BlockPos.MutableBlockPos();
        Optional<BlockPos> $$3 = Optional.empty();
        int $$4 = 0;
        for (int $$5 = -1; $$5 <= 1; $$5++) {
            for (int $$6 = -1; $$6 <= 1; $$6++) {
                for (int $$7 = -1; $$7 <= 1; $$7++) {
                    $$2.setWithOffset(villager1.m_20183_(), $$5, $$6, $$7);
                    if (this.validPos($$2, serverLevel0)) {
                        if (serverLevel0.f_46441_.nextInt(++$$4) == 0) {
                            $$3 = Optional.of($$2.immutable());
                        }
                    }
                }
            }
        }
        return $$3;
    }

    private boolean validPos(BlockPos blockPos0, ServerLevel serverLevel1) {
        BlockState $$2 = serverLevel1.m_8055_(blockPos0);
        Block $$3 = $$2.m_60734_();
        return $$3 instanceof CropBlock && !((CropBlock) $$3).isMaxAge($$2);
    }

    protected void start(ServerLevel serverLevel0, Villager villager1, long long2) {
        this.setCurrentCropAsTarget(villager1);
        villager1.m_8061_(EquipmentSlot.MAINHAND, new ItemStack(Items.BONE_MEAL));
        this.nextWorkCycleTime = long2;
        this.timeWorkedSoFar = 0;
    }

    private void setCurrentCropAsTarget(Villager villager0) {
        this.cropPos.ifPresent(p_24484_ -> {
            BlockPosTracker $$2 = new BlockPosTracker(p_24484_);
            villager0.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, $$2);
            villager0.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget($$2, 0.5F, 1));
        });
    }

    protected void stop(ServerLevel serverLevel0, Villager villager1, long long2) {
        villager1.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.lastBonemealingSession = (long) villager1.f_19797_;
    }

    protected void tick(ServerLevel serverLevel0, Villager villager1, long long2) {
        BlockPos $$3 = (BlockPos) this.cropPos.get();
        if (long2 >= this.nextWorkCycleTime && $$3.m_203195_(villager1.m_20182_(), 1.0)) {
            ItemStack $$4 = ItemStack.EMPTY;
            SimpleContainer $$5 = villager1.m_35311_();
            int $$6 = $$5.getContainerSize();
            for (int $$7 = 0; $$7 < $$6; $$7++) {
                ItemStack $$8 = $$5.getItem($$7);
                if ($$8.is(Items.BONE_MEAL)) {
                    $$4 = $$8;
                    break;
                }
            }
            if (!$$4.isEmpty() && BoneMealItem.growCrop($$4, serverLevel0, $$3)) {
                serverLevel0.m_46796_(1505, $$3, 0);
                this.cropPos = this.pickNextTarget(serverLevel0, villager1);
                this.setCurrentCropAsTarget(villager1);
                this.nextWorkCycleTime = long2 + 40L;
            }
            this.timeWorkedSoFar++;
        }
    }
}