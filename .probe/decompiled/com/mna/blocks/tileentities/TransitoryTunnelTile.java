package com.mna.blocks.tileentities;

import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TransitoryTunnelTile extends BlockEntity {

    private int duration = 20;

    private int age;

    private BlockState previousBlockState = Blocks.AIR.defaultBlockState();

    public TransitoryTunnelTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.TRANSITORY_TUNNEL.get(), pos, state);
    }

    public void setDurationAndPreviousState(int duration, BlockState previous) {
        this.duration = duration;
        this.previousBlockState = previous;
    }

    public static void ServerTick(Level level, BlockPos pos, BlockState state, TransitoryTunnelTile tile) {
        tile.age++;
        if (tile.age > tile.duration) {
            level.setBlock(pos, tile.previousBlockState, 2);
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("duration", this.duration);
        nbt.putInt("age", this.age);
        nbt.putString("blockState", BlockStateParser.serialize(this.previousBlockState));
        compound.put("tunnelData", nbt);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("tunnelData")) {
            CompoundTag nbt = compound.getCompound("tunnelData");
            if (nbt.contains("duration")) {
                this.duration = nbt.getInt("duration");
            }
            if (nbt.contains("age")) {
                this.age = nbt.getInt("age");
            }
            if (nbt.contains("blockState")) {
                try {
                    this.previousBlockState = BlockStateParser.parseForBlock(this.f_58857_.m_246945_(Registries.BLOCK), nbt.getString("blockState"), true).blockState();
                } catch (CommandSyntaxException var4) {
                    this.previousBlockState = Blocks.AIR.defaultBlockState();
                    var4.printStackTrace();
                }
            }
            if (this.previousBlockState == null) {
                this.previousBlockState = Blocks.AIR.defaultBlockState();
            }
        }
    }
}