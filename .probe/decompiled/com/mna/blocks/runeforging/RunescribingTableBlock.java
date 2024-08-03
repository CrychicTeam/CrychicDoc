package com.mna.blocks.runeforging;

import com.mna.api.blocks.WizardLabBlock;
import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.blocks.tileentities.wizard_lab.RunescribingTableTile;
import com.mna.items.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class RunescribingTableBlock extends WizardLabBlock implements ICutoutBlock, EntityBlock {

    public RunescribingTableBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RunescribingTableTile(pos, state);
    }

    @Override
    protected MenuProvider getProvider(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity workbench = level.getBlockEntity(pos);
        return workbench != null && workbench instanceof RunescribingTableTile ? (RunescribingTableTile) workbench : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        if (player.m_6047_()) {
            if (worldIn.isClientSide) {
                return InteractionResult.SUCCESS;
            }
            ItemStack activeStack = player.m_21120_(InteractionHand.MAIN_HAND);
            ItemStack offhandStack = player.m_21120_(InteractionHand.OFF_HAND);
            BlockEntity be = worldIn.getBlockEntity(pos);
            if (be != null && be instanceof RunescribingTableTile tile) {
                ItemStack hammerStack = tile.m_8020_(0);
                if (hammerStack.isEmpty()) {
                    if (activeStack.getItem() == ItemInit.RUNESMITH_HAMMER.get()) {
                        tile.m_6836_(0, activeStack.copy());
                        activeStack.shrink(1);
                        return InteractionResult.SUCCESS;
                    }
                    if (offhandStack.getItem() == ItemInit.RUNESMITH_HAMMER.get()) {
                        tile.m_6836_(0, offhandStack.copy());
                        offhandStack.shrink(1);
                        return InteractionResult.SUCCESS;
                    }
                } else if (hammerStack.getItem() == ItemInit.RUNESMITH_HAMMER.get()) {
                    tile.m_6836_(0, ItemStack.EMPTY);
                    if (!player.addItem(hammerStack.copy())) {
                        player.drop(hammerStack.copy(), true);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
    }
}