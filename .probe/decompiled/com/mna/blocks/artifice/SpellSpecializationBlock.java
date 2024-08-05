package com.mna.blocks.artifice;

import com.mna.api.blocks.WizardLabBlock;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.blocks.tileentities.wizard_lab.SpellSpecializationTile;
import com.mna.gui.containers.block.ContainerSpellSpecialization;
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

public class SpellSpecializationBlock extends WizardLabBlock implements EntityBlock {

    private static final Component CONTAINER_TITLE = Component.translatable(RLoc.create("container.spell_specialization").toString());

    public SpellSpecializationBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpellSpecializationTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.SPELL_SPECIALIZAITON.get() ? (lvl, pos, state1, be) -> SpellSpecializationTile.Tick(lvl, pos, state1, (T) ((SpellSpecializationTile) be)) : null;
    }

    @Override
    protected MenuProvider getProvider(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        return be != null && be instanceof SpellSpecializationTile ? new SimpleMenuProvider((id, playerInv, user) -> new ContainerSpellSpecialization(id, playerInv, (SpellSpecializationTile) be), CONTAINER_TITLE) : null;
    }
}