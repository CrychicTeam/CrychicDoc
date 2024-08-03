package noppes.npcs.blocks.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.CustomBlocks;

public class TileDoor extends TileNpcEntity {

    public int tickCount = 0;

    public Block blockModel = CustomBlocks.scripted_door;

    public boolean needsClientUpdate = false;

    public TileDoor(BlockEntityType<?> p_i48289_1_, BlockPos pos, BlockState state) {
        super(p_i48289_1_, pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.setDoorNBT(compound);
    }

    public void setDoorNBT(CompoundTag compound) {
        this.blockModel = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("ScriptDoorBlockModel")));
        if (this.blockModel == null || !(this.blockModel instanceof DoorBlock)) {
            this.blockModel = CustomBlocks.scripted_door;
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        this.getDoorNBT(compound);
        super.saveAdditional(compound);
    }

    public CompoundTag getDoorNBT(CompoundTag compound) {
        compound.putString("ScriptDoorBlockModel", ForgeRegistries.BLOCKS.getKey(this.blockModel) + "");
        return compound;
    }

    public void setItemModel(Block block) {
        if (block == null || !(block instanceof DoorBlock)) {
            block = CustomBlocks.scripted_door;
        }
        if (this.blockModel != block) {
            this.blockModel = block;
            this.needsClientUpdate = true;
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileDoor tile) {
        tile.tickCount++;
        if (tile.tickCount >= 10) {
            tile.tickCount = 0;
            if (tile.needsClientUpdate) {
                tile.m_6596_();
                level.setBlockAndUpdate(pos, state);
                tile.needsClientUpdate = false;
            }
        }
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.handleUpdateTag(pkt.getTag());
    }

    public void handleUpdateTag(CompoundTag compound) {
        this.setDoorNBT(compound);
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
        this.getDoorNBT(compound);
        return compound;
    }
}