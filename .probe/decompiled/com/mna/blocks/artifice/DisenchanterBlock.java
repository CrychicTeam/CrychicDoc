package com.mna.blocks.artifice;

import com.mna.api.blocks.WizardLabBlock;
import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.blocks.tileentities.wizard_lab.DisenchanterTile;
import com.mna.gui.containers.block.ContainerDisenchanter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class DisenchanterBlock extends WizardLabBlock implements ICutoutBlock, EntityBlock {

    private static final Component CONTAINER_TITLE = Component.translatable(RLoc.create("container.disenchanter").toString());

    public DisenchanterBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DisenchanterTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.DISENCHANTER.get() ? (lvl, pos, state1, be) -> DisenchanterTile.Tick(lvl, pos, state1, (T) ((DisenchanterTile) be)) : null;
    }

    @Override
    protected MenuProvider getProvider(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        return be != null && be instanceof DisenchanterTile ? new SimpleMenuProvider((id, playerInv, user) -> new ContainerDisenchanter(id, playerInv, (DisenchanterTile) be), CONTAINER_TITLE) : null;
    }
}