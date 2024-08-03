package dev.xkmc.l2library.base.tile;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SerialClass
public class BaseBlockEntity extends BlockEntity {

    public BaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("auto-serial")) {
            Wrappers.run(() -> TagCodec.fromTag(tag.getCompound("auto-serial"), this.getClass(), this, f -> true));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag ser = (CompoundTag) Wrappers.get(() -> TagCodec.toTag(new CompoundTag(), this.getClass(), this, f -> true));
        if (ser != null) {
            tag.put("auto-serial", ser);
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void sync() {
        if (this.f_58857_ != null) {
            this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag ans = super.getUpdateTag();
        CompoundTag ser = (CompoundTag) Wrappers.get(() -> TagCodec.toTag(new CompoundTag(), this.getClass(), this, SerialField::toClient));
        if (ser != null) {
            ans.put("auto-serial", ser);
        }
        return ans;
    }
}