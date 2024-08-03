package team.lodestar.lodestone.systems.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.helpers.BlockHelper;
import team.lodestar.lodestone.registry.common.LodestoneBlockEntityRegistry;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class MultiBlockComponentEntity extends LodestoneBlockEntity {

    public BlockPos corePos;

    public MultiBlockComponentEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public MultiBlockComponentEntity(BlockPos pos, BlockState state) {
        super(LodestoneBlockEntityRegistry.MULTIBLOCK_COMPONENT.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if (this.corePos != null) {
            BlockHelper.saveBlockPos(tag, this.corePos, "core_position_");
        }
        super.m_183515_(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        this.corePos = BlockHelper.loadBlockPos(tag, "core_position_");
        super.load(tag);
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        return this.corePos != null && this.f_58857_.getBlockEntity(this.corePos) instanceof MultiBlockCoreEntity core ? core.onUse(player, hand) : super.onUse(player, hand);
    }

    @Override
    public void onBreak(@Nullable Player player) {
        if (this.corePos != null && this.f_58857_.getBlockEntity(this.corePos) instanceof MultiBlockCoreEntity core) {
            core.onBreak(player);
        }
        super.onBreak(player);
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return this.corePos != null && this.f_58857_.getBlockEntity(this.corePos) instanceof MultiBlockCoreEntity core ? core.getCapability(cap) : super.getCapability(cap);
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return this.corePos != null && this.f_58857_.getBlockEntity(this.corePos) instanceof MultiBlockCoreEntity core ? core.getCapability(cap, side) : super.getCapability(cap, side);
    }
}