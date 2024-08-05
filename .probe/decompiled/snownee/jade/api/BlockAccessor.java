package snownee.jade.api;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

public interface BlockAccessor extends Accessor<BlockHitResult> {

    Block getBlock();

    BlockState getBlockState();

    BlockEntity getBlockEntity();

    BlockPos getPosition();

    Direction getSide();

    boolean isFakeBlock();

    ItemStack getFakeBlock();

    @Override
    default Class<? extends Accessor<?>> getAccessorType() {
        return BlockAccessor.class;
    }

    @NonExtendable
    public interface Builder {

        BlockAccessor.Builder level(Level var1);

        BlockAccessor.Builder player(Player var1);

        BlockAccessor.Builder serverData(CompoundTag var1);

        BlockAccessor.Builder serverConnected(boolean var1);

        BlockAccessor.Builder showDetails(boolean var1);

        BlockAccessor.Builder hit(BlockHitResult var1);

        BlockAccessor.Builder blockState(BlockState var1);

        default BlockAccessor.Builder blockEntity(BlockEntity blockEntity) {
            return this.blockEntity(() -> blockEntity);
        }

        BlockAccessor.Builder blockEntity(Supplier<BlockEntity> var1);

        BlockAccessor.Builder fakeBlock(ItemStack var1);

        BlockAccessor.Builder from(BlockAccessor var1);

        BlockAccessor.Builder requireVerification();

        BlockAccessor build();
    }
}