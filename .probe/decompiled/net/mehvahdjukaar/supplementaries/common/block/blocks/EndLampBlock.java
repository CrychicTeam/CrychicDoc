package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class EndLampBlock extends Block {

    public static final EnumProperty<ModBlockProperties.Rune> RUNE = ModBlockProperties.RUNE;

    public EndLampBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(RUNE, ModBlockProperties.Rune.A));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(RUNE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        RandomSource r = RandomSource.create(context.getClickedPos().asLong());
        return (BlockState) this.m_49966_().m_61124_(RUNE, ModBlockProperties.Rune.values()[r.nextInt(ModBlockProperties.Rune.values().length)]);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide) {
            int inc = player.m_6144_() ? -1 : 1;
            worldIn.setBlockAndUpdate(pos, (BlockState) state.m_61124_(RUNE, ModBlockProperties.Rune.values()[(((ModBlockProperties.Rune) state.m_61143_(RUNE)).ordinal() + inc + ModBlockProperties.Rune.values().length) % ModBlockProperties.Rune.values().length]));
            worldIn.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 0.25F, 1.7F);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.SUCCESS;
        }
    }
}