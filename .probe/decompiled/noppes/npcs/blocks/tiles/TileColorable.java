package noppes.npcs.blocks.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TileColorable extends TileNpcEntity {

    public int color = 14;

    public int rotation;

    public TileColorable(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.color = compound.getInt("BannerColor");
        this.rotation = compound.getInt("BannerRotation");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putInt("BannerColor", this.color);
        compound.putInt("BannerRotation", this.rotation);
        super.saveAdditional(compound);
    }

    public boolean canUpdate() {
        return false;
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag compound = pkt.getTag();
        this.load(compound);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compound = new CompoundTag();
        this.saveAdditional(compound);
        compound.remove("Items");
        compound.remove("ExtraData");
        return compound;
    }

    public AABB getRenderBoundingBox() {
        return new AABB((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), (double) (this.f_58858_.m_123341_() + 1), (double) (this.f_58858_.m_123342_() + 1), (double) (this.f_58858_.m_123343_() + 1));
    }

    public int powerProvided() {
        return 0;
    }
}