package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.ILightable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public abstract class LightUpBlock extends Block implements ILightable {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected LightUpBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isLitUp(BlockState state, BlockGetter level, BlockPos pos) {
        return (Boolean) state.m_61143_(LIT);
    }

    @Override
    public void setLitUp(BlockState state, LevelAccessor world, BlockPos pos, boolean lit) {
        world.m_7731_(pos, (BlockState) state.m_61124_(LIT, lit), 3);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        return this.interactWithPlayer(state, worldIn, pos, player, handIn);
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult pHit, Projectile projectile) {
        BlockPos pos = pHit.getBlockPos();
        this.interactWithProjectile(level, state, projectile, pos);
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof Projectile projectile) {
            this.interactWithProjectile(worldIn, state, projectile, pos);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        BlockState state = this.m_49966_();
        return (BlockState) state.m_61124_(LIT, !flag);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}