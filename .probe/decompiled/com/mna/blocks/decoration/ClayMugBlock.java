package com.mna.blocks.decoration;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.items.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClayMugBlock extends HorizontalDirectionalBlock implements IDontCreateBlockItem {

    public static final BooleanProperty HAS_LIQUID = BooleanProperty.create("has_liquid");

    private static final VoxelShape BASE = Block.box(4.5, 0.0, 4.5, 11.5, 5.0, 11.5);

    public ClayMugBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BROWN).noOcclusion());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_54117_);
        builder.add(HAS_LIQUID);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if ((Boolean) state.m_61143_(HAS_LIQUID)) {
            Vec3 pPos = Vec3.atCenterOf(pos);
            level.addParticle(new MAParticleType(ParticleInit.DUST.get()).setGravity(0.0F).setColor(24, 24, 24).setMaxAge(40).setScale(0.05F), pPos.x - 0.1F + Math.random() * 0.2, pPos.y - 0.2F, pPos.z - 0.1F + Math.random() * 0.2, 0.0, 0.005, 0.0);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(f_54117_, ctx.m_8125_())).m_61124_(HAS_LIQUID, ctx.m_43722_().getItem() == ItemInit.MANA_TEA.get());
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return BASE;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (world.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            ItemStack stack = ItemStack.EMPTY;
            if ((Boolean) state.m_61143_(HAS_LIQUID)) {
                stack = new ItemStack(ItemInit.MANA_TEA.get());
            } else {
                stack = new ItemStack(ItemInit.CLAY_MUG.get());
            }
            if (player.addItem(stack)) {
                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
            return InteractionResult.SUCCESS;
        }
    }
}