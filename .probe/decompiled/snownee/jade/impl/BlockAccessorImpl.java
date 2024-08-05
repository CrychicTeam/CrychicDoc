package snownee.jade.impl;

import com.google.common.base.Suppliers;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import snownee.jade.Jade;
import snownee.jade.api.AccessorImpl;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.util.CommonProxy;
import snownee.jade.util.WailaExceptionHandler;

public class BlockAccessorImpl extends AccessorImpl<BlockHitResult> implements BlockAccessor {

    private final BlockState blockState;

    @Nullable
    private final Supplier<BlockEntity> blockEntity;

    private ItemStack fakeBlock;

    private BlockAccessorImpl(BlockAccessorImpl.Builder builder) {
        super(builder.level, builder.player, builder.serverData, Suppliers.ofInstance(builder.hit), builder.connected, builder.showDetails);
        this.blockState = builder.blockState;
        this.blockEntity = builder.blockEntity;
        this.fakeBlock = builder.fakeBlock;
    }

    public static void handleRequest(FriendlyByteBuf buf, ServerPlayer player, Consumer<Runnable> executor, Consumer<CompoundTag> responseSender) {
        BlockAccessor accessor;
        try {
            accessor = fromNetwork(buf, player);
        } catch (Exception var6) {
            WailaExceptionHandler.handleErr(var6, null, null, null);
            return;
        }
        executor.accept((Runnable) () -> {
            BlockPos pos = accessor.getPosition();
            ServerLevel world = player.serverLevel();
            if (!(pos.m_123331_(player.m_20183_()) > (double) Jade.MAX_DISTANCE_SQR) && world.m_46749_(pos)) {
                BlockEntity tile = accessor.getBlockEntity();
                if (tile != null) {
                    List<IServerDataProvider<BlockAccessor>> providers = WailaCommonRegistration.INSTANCE.getBlockNBTProviders(tile);
                    if (!providers.isEmpty()) {
                        CompoundTag tag = accessor.getServerData();
                        for (IServerDataProvider<BlockAccessor> provider : providers) {
                            try {
                                provider.appendServerData(tag, accessor);
                            } catch (Exception var11) {
                                WailaExceptionHandler.handleErr(var11, provider, null, null);
                            }
                        }
                        tag.putInt("x", pos.m_123341_());
                        tag.putInt("y", pos.m_123342_());
                        tag.putInt("z", pos.m_123343_());
                        tag.putString("id", CommonProxy.getId(tile.getType()).toString());
                        responseSender.accept(tag);
                    }
                }
            }
        });
    }

    public static BlockAccessor fromNetwork(FriendlyByteBuf buf, ServerPlayer player) {
        BlockAccessorImpl.Builder builder = new BlockAccessorImpl.Builder();
        builder.level(player.m_9236_());
        builder.player(player);
        builder.showDetails(buf.readBoolean());
        builder.hit(buf.readBlockHitResult());
        builder.blockState(Block.stateById(buf.readVarInt()));
        builder.fakeBlock(buf.readItem());
        if (builder.blockState.m_155947_()) {
            builder.blockEntity(Suppliers.memoize(() -> builder.level.getBlockEntity(builder.hit.getBlockPos())));
        }
        return builder.build();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeBoolean(this.showDetails());
        buf.writeBlockHitResult(this.getHitResult());
        buf.writeVarInt(Block.getId(this.blockState));
        buf.writeItem(this.fakeBlock);
    }

    @Override
    public Block getBlock() {
        return this.getBlockState().m_60734_();
    }

    @Override
    public BlockState getBlockState() {
        return this.blockState;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this.blockEntity == null ? null : (BlockEntity) this.blockEntity.get();
    }

    @Override
    public BlockPos getPosition() {
        return this.getHitResult().getBlockPos();
    }

    @Override
    public Direction getSide() {
        return this.getHitResult().getDirection();
    }

    @Override
    public ItemStack getPickedResult() {
        return CommonProxy.getBlockPickedResult(this.blockState, this.getPlayer(), this.getHitResult());
    }

    @Override
    public Object getTarget() {
        return this.getBlockEntity();
    }

    @Override
    public boolean isFakeBlock() {
        return !this.fakeBlock.isEmpty();
    }

    @Override
    public ItemStack getFakeBlock() {
        return this.fakeBlock;
    }

    public void setFakeBlock(ItemStack fakeBlock) {
        this.fakeBlock = fakeBlock;
    }

    @Override
    public boolean verifyData(CompoundTag data) {
        if (!this.verify) {
            return true;
        } else {
            int x = data.getInt("x");
            int y = data.getInt("y");
            int z = data.getInt("z");
            BlockPos hitPos = this.getPosition();
            return x == hitPos.m_123341_() && y == hitPos.m_123342_() && z == hitPos.m_123343_();
        }
    }

    public static class Builder implements BlockAccessor.Builder {

        private Level level;

        private Player player;

        private CompoundTag serverData;

        private boolean connected;

        private boolean showDetails;

        private BlockHitResult hit;

        private BlockState blockState = Blocks.AIR.defaultBlockState();

        private Supplier<BlockEntity> blockEntity;

        private ItemStack fakeBlock = ItemStack.EMPTY;

        private boolean verify;

        public BlockAccessorImpl.Builder level(Level level) {
            this.level = level;
            return this;
        }

        public BlockAccessorImpl.Builder player(Player player) {
            this.player = player;
            return this;
        }

        public BlockAccessorImpl.Builder serverData(CompoundTag serverData) {
            this.serverData = serverData;
            return this;
        }

        public BlockAccessorImpl.Builder serverConnected(boolean connected) {
            this.connected = connected;
            return this;
        }

        public BlockAccessorImpl.Builder showDetails(boolean showDetails) {
            this.showDetails = showDetails;
            return this;
        }

        public BlockAccessorImpl.Builder hit(BlockHitResult hit) {
            this.hit = hit;
            return this;
        }

        public BlockAccessorImpl.Builder blockState(BlockState blockState) {
            this.blockState = blockState;
            return this;
        }

        public BlockAccessorImpl.Builder blockEntity(Supplier<BlockEntity> blockEntity) {
            this.blockEntity = blockEntity;
            return this;
        }

        public BlockAccessorImpl.Builder fakeBlock(ItemStack stack) {
            this.fakeBlock = stack;
            return this;
        }

        public BlockAccessorImpl.Builder from(BlockAccessor accessor) {
            this.level = accessor.getLevel();
            this.player = accessor.getPlayer();
            this.serverData = accessor.getServerData();
            this.connected = accessor.isServerConnected();
            this.showDetails = accessor.showDetails();
            this.hit = accessor.getHitResult();
            this.blockEntity = accessor::getBlockEntity;
            this.blockState = accessor.getBlockState();
            this.fakeBlock = accessor.getFakeBlock();
            return this;
        }

        @Override
        public BlockAccessor.Builder requireVerification() {
            this.verify = true;
            return this;
        }

        @Override
        public BlockAccessor build() {
            BlockAccessorImpl accessor = new BlockAccessorImpl(this);
            if (this.verify) {
                accessor.requireVerification();
            }
            return accessor;
        }
    }
}