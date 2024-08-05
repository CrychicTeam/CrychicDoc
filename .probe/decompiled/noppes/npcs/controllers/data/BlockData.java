package noppes.npcs.controllers.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockData {

    public BlockPos pos;

    public BlockState state;

    public CompoundTag tile;

    private ItemStack stack;

    public BlockData(BlockPos pos, BlockState state, CompoundTag tile) {
        this.pos = pos;
        this.state = state;
        this.tile = tile;
    }

    public CompoundTag getNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("BuildX", this.pos.m_123341_());
        compound.putInt("BuildY", this.pos.m_123342_());
        compound.putInt("BuildZ", this.pos.m_123343_());
        compound.putString("Block", ForgeRegistries.BLOCKS.getKey(this.state.m_60734_()).toString());
        if (this.tile != null) {
            compound.put("Tile", this.tile);
        }
        return compound;
    }

    public static BlockData getData(CompoundTag compound) {
        BlockPos pos = new BlockPos(compound.getInt("BuildX"), compound.getInt("BuildY"), compound.getInt("BuildZ"));
        Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("Block")));
        if (b == null) {
            return null;
        } else {
            CompoundTag tile = null;
            if (compound.contains("Tile")) {
                tile = compound.getCompound("Tile");
            }
            return new BlockData(pos, b.defaultBlockState(), tile);
        }
    }

    public ItemStack getStack() {
        if (this.stack == null) {
            this.stack = new ItemStack(this.state.m_60734_(), 1);
        }
        return this.stack;
    }
}