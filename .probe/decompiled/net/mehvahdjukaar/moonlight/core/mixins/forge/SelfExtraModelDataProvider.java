package net.mehvahdjukaar.moonlight.core.mixins.forge;

import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.Overwrite;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.IExtraModelDataProvider;
import net.mehvahdjukaar.moonlight.api.client.model.forge.ExtraModelDataImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ IExtraModelDataProvider.class })
public interface SelfExtraModelDataProvider extends IForgeBlockEntity, IExtraModelDataProvider {

    @Overwrite
    @Override
    default void requestModelReload() {
        BlockEntity be = (BlockEntity) this;
        be.requestModelDataUpdate();
        Level level = be.getLevel();
        if (level != null && level.isClientSide) {
            level.sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 2);
        }
    }

    @Override
    default ModelData getModelData() {
        return this.getExtraModelData() instanceof ExtraModelDataImpl data ? data.data() : ModelData.EMPTY;
    }

    @Override
    default void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        BlockEntity be = (BlockEntity) this;
        Level level = be.getLevel();
        if (level != null && level.isClientSide) {
            ExtraModelData oldData = this.getExtraModelData();
            CompoundTag tag = pkt.getTag();
            if (tag != null) {
                be.load(tag);
                this.afterDataPacket(oldData);
            }
        }
    }
}