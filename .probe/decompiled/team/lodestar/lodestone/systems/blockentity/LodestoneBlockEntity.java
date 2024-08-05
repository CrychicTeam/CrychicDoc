package team.lodestar.lodestone.systems.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class LodestoneBlockEntity extends BlockEntity {

    public boolean needsSync;

    public LodestoneBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void onBreak(@Nullable Player player) {
        this.invalidateCaps();
    }

    public void onPlace(LivingEntity placer, ItemStack stack) {
    }

    public void onNeighborUpdate(BlockState state, BlockPos pos, BlockPos neighbor) {
    }

    public ItemStack onClone(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return ItemStack.EMPTY;
    }

    public InteractionResult onUse(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    public void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            super.handleUpdateTag(tag);
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        this.needsSync = true;
        super.load(pTag);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.handleUpdateTag(this.getUpdatePacket().getTag());
    }

    public void tick() {
        if (this.needsSync) {
            this.init();
            this.needsSync = false;
        }
    }

    public void init() {
    }
}