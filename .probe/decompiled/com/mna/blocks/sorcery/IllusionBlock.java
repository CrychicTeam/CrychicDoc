package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.tileentities.IllusionBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IllusionBlock extends Block implements ITranslucentBlock, EntityBlock {

    public IllusionBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.m_43719_().getOpposite();
        if (context.m_43723_().m_6047_()) {
            face = face.getOpposite();
        }
        return (BlockState) super.getStateForPlacement(context).m_61124_(BlockStateProperties.FACING, face);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new IllusionBlockTile(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void spawnDestroyParticles(Level world, Player player, BlockPos pos, BlockState state) {
        for (int i = 0; i < 20; i++) {
            world.addParticle(ParticleInit.BLUE_SPARKLE_VELOCITY.get(), (double) pos.m_123341_() + Math.random(), (double) pos.m_123342_() + Math.random(), (double) pos.m_123343_() + Math.random(), 0.0, 0.05 * Math.random(), 0.0);
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return false;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext ecc && ecc.getEntity() instanceof LivingEntity && !(ecc.getEntity() instanceof Player)) {
            return Shapes.block();
        }
        return Shapes.empty();
    }
}