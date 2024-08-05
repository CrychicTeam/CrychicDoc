package com.mna.blocks.tileentities;

import com.mna.blocks.sorcery.SpectralCraftingTableBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SimpleSpectralTile extends BlockEntity {

    private static final int MAX_AGE = 400;

    private int age = 0;

    public SimpleSpectralTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    public SimpleSpectralTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.SIMPLE_SPECTRAL_TILE.get(), pos, state);
    }

    public static void ServerTick(Level level, BlockPos pos, BlockState state, SimpleSpectralTile tile) {
        if (!state.m_61148_().containsKey(SpectralCraftingTableBlock.PERMANENT) || !(Boolean) state.m_61143_(SpectralCraftingTableBlock.PERMANENT)) {
            tile.age++;
            if (tile.age > 400 && tile.age % 20 == 0 && level.m_45976_(Player.class, new AABB(pos).inflate(8.0)).size() == 0) {
                level.m_46961_(pos, false);
            }
        }
    }
}