package com.mna.blocks.artifice;

import com.mna.api.blocks.WizardLabBlock;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.blocks.tileentities.wizard_lab.EldrinFumeTile;
import com.mna.gui.containers.block.ContainerEldrinFume;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
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

public class EldrinFumeBlock extends WizardLabBlock implements EntityBlock {

    private static final Component CONTAINER_TITLE = Component.translatable(RLoc.create("container.eldrin_fume").toString());

    public EldrinFumeBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).noOcclusion().strength(3.0F));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EldrinFumeTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.ELDRIN_FUME.get() ? (lvl, pos, state1, be) -> EldrinFumeTile.Tick(lvl, pos, state1, (T) ((EldrinFumeTile) be)) : null;
    }

    @Override
    protected MenuProvider getProvider(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        return be != null && be instanceof EldrinFumeTile ? new SimpleMenuProvider((id, playerInv, user) -> new ContainerEldrinFume(id, playerInv, (EldrinFumeTile) be), CONTAINER_TITLE) : null;
    }

    @Override
    protected boolean shouldPlayAmbient(BlockState state, Level world, BlockPos pos, RandomSource rnd) {
        BlockEntity be = world.getBlockEntity(pos);
        return be != null && be instanceof EldrinFumeTile ? ((EldrinFumeTile) be).isActive() : false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAMPFIRE_CRACKLE;
    }
}