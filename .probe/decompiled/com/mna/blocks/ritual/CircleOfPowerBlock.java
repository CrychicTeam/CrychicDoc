package com.mna.blocks.ritual;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.blocks.tileentities.SanctumTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class CircleOfPowerBlock extends Block implements IDontCreateBlockItem, EntityBlock {

    public CircleOfPowerBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SanctumTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.CIRCLE_OF_POWER.get() && !level.isClientSide() ? (lvl, pos, state1, be) -> SanctumTile.ServerTick(lvl, pos, state1, (SanctumTile) be) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if (te instanceof SanctumTile) {
                ((SanctumTile) te).setGhostMultiblock();
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity living, ItemStack stack) {
        if (living instanceof Player) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te != null && te instanceof SanctumTile) {
                ((Player) living).getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> ((SanctumTile) te).setFaction(p.getAlliedFaction()));
            }
        }
        super.setPlacedBy(world, pos, state, living, stack);
    }
}