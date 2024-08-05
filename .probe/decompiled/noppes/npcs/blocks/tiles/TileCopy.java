package noppes.npcs.blocks.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import noppes.npcs.CustomBlocks;

public class TileCopy extends BlockEntity {

    public short length = 10;

    public short width = 10;

    public short height = 10;

    public String name = "";

    public TileCopy(BlockPos pos, BlockState state) {
        super(CustomBlocks.tile_copy, pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.length = compound.getShort("Length");
        this.width = compound.getShort("Width");
        this.height = compound.getShort("Height");
        this.name = compound.getString("Name");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putShort("Length", this.length);
        compound.putShort("Width", this.width);
        compound.putShort("Height", this.height);
        compound.putString("Name", this.name);
        super.saveAdditional(compound);
    }

    public void handleUpdateTag(CompoundTag compound) {
        this.length = compound.getShort("Length");
        this.width = compound.getShort("Width");
        this.height = compound.getShort("Height");
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.handleUpdateTag(pkt.getTag());
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("x", this.f_58858_.m_123341_());
        compound.putInt("y", this.f_58858_.m_123342_());
        compound.putInt("z", this.f_58858_.m_123343_());
        compound.putShort("Length", this.length);
        compound.putShort("Width", this.width);
        compound.putShort("Height", this.height);
        return compound;
    }

    public AABB getRenderBoundingBox() {
        return new AABB((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), (double) (this.f_58858_.m_123341_() + this.width + 1), (double) (this.f_58858_.m_123342_() + this.height + 1), (double) (this.f_58858_.m_123343_() + this.length + 1));
    }
}