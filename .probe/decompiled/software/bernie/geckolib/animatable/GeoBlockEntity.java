package software.bernie.geckolib.animatable;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.network.GeckoLibNetwork;
import software.bernie.geckolib.network.SerializableDataTicket;
import software.bernie.geckolib.network.packet.BlockEntityAnimDataSyncPacket;
import software.bernie.geckolib.network.packet.BlockEntityAnimTriggerPacket;
import software.bernie.geckolib.util.RenderUtils;

public interface GeoBlockEntity extends GeoAnimatable {

    @Nullable
    default <D> D getAnimData(SerializableDataTicket<D> dataTicket) {
        return this.getAnimatableInstanceCache().getManagerForId(0L).getData(dataTicket);
    }

    default <D> void setAnimData(SerializableDataTicket<D> dataTicket, D data) {
        BlockEntity blockEntity = (BlockEntity) this;
        Level level = blockEntity.getLevel();
        if (level == null) {
            GeckoLib.LOGGER.error("Attempting to set animation data for BlockEntity too early! Must wait until after the BlockEntity has been set in the world. (" + blockEntity.getClass().toString() + ")");
        } else {
            if (level.isClientSide()) {
                this.getAnimatableInstanceCache().getManagerForId(0L).setData(dataTicket, data);
            } else {
                BlockPos pos = blockEntity.getBlockPos();
                GeckoLibNetwork.send(new BlockEntityAnimDataSyncPacket<>(pos, dataTicket, data), PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)));
            }
        }
    }

    default void triggerAnim(@Nullable String controllerName, String animName) {
        BlockEntity blockEntity = (BlockEntity) this;
        Level level = blockEntity.getLevel();
        if (level == null) {
            GeckoLib.LOGGER.error("Attempting to trigger an animation for a BlockEntity too early! Must wait until after the BlockEntity has been set in the world. (" + blockEntity.getClass().toString() + ")");
        } else {
            if (level.isClientSide()) {
                this.getAnimatableInstanceCache().getManagerForId(0L).tryTriggerAnimation(controllerName, animName);
            } else {
                BlockPos pos = blockEntity.getBlockPos();
                GeckoLibNetwork.send(new BlockEntityAnimTriggerPacket(pos, controllerName, animName), PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)));
            }
        }
    }

    @Override
    default double getTick(Object blockEntity) {
        return RenderUtils.getCurrentTick();
    }
}