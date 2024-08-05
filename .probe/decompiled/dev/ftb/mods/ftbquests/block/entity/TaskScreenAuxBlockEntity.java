package dev.ftb.mods.ftbquests.block.entity;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TaskScreenAuxBlockEntity extends BlockEntity implements ITaskScreen, Nameable {

    @NotNull
    private WeakReference<TaskScreenBlockEntity> coreScreen = new WeakReference(null);

    private BlockPos corePosPending;

    public TaskScreenAuxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super((BlockEntityType<?>) FTBQuestsBlockEntities.AUX_TASK_SCREEN.get(), blockPos, blockState);
    }

    @Override
    public Component getName() {
        return (Component) this.getCoreScreen().map(s -> s.m_58900_().m_60734_().getName()).orElse(Component.literal("Task Screen"));
    }

    @Override
    public Optional<TaskScreenBlockEntity> getCoreScreen() {
        if (this.corePosPending != null) {
            TaskScreenBlockEntity core = (TaskScreenBlockEntity) this.f_58857_.m_141902_(this.corePosPending, (BlockEntityType) FTBQuestsBlockEntities.CORE_TASK_SCREEN.get()).orElse(null);
            if (core != null) {
                this.coreScreen = new WeakReference(core);
                this.corePosPending = null;
            } else {
                this.f_58857_.m_46953_(this.m_58899_(), false, null);
            }
        }
        return Optional.ofNullable((TaskScreenBlockEntity) this.coreScreen.get());
    }

    public void setCoreScreen(@NotNull TaskScreenBlockEntity coreScreen) {
        if (this.coreScreen.get() != null) {
            throw new IllegalStateException("coreScreen is already set and can't be changed!");
        } else {
            this.coreScreen = new WeakReference(coreScreen);
            this.m_6596_();
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.corePosPending = compoundTag.contains("CorePos") ? NbtUtils.readBlockPos(compoundTag.getCompound("CorePos")) : null;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (this.corePosPending != null) {
            compoundTag.put("CorePos", NbtUtils.writeBlockPos(this.corePosPending));
        } else {
            TaskScreenBlockEntity cs = (TaskScreenBlockEntity) this.coreScreen.get();
            if (cs != null) {
                compoundTag.put("CorePos", NbtUtils.writeBlockPos(cs.m_58899_()));
            }
        }
    }

    @Override
    public boolean isIndestructible() {
        return (Boolean) this.getCoreScreen().map(TaskScreenBlockEntity::isIndestructible).orElse(false);
    }

    @Override
    public ItemStack getSkin() {
        return (ItemStack) this.getCoreScreen().map(TaskScreenBlockEntity::getSkin).orElse(ItemStack.EMPTY);
    }

    @NotNull
    @Override
    public UUID getTeamId() {
        return (UUID) this.getCoreScreen().map(TaskScreenBlockEntity::getTeamId).orElse(Util.NIL_UUID);
    }

    @Override
    public boolean isInputOnly() {
        return (Boolean) this.getCoreScreen().map(TaskScreenBlockEntity::isInputOnly).orElse(false);
    }
}