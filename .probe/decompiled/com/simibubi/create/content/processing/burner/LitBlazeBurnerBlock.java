package com.simibubi.create.content.processing.burner;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;

public class LitBlazeBurnerBlock extends Block implements IWrenchable {

    public static final ToolAction EXTINGUISH_FLAME_ACTION = ToolAction.get(Create.asResource("extinguish_flame").toString());

    public static final EnumProperty<LitBlazeBurnerBlock.FlameType> FLAME_TYPE = EnumProperty.create("flame_type", LitBlazeBurnerBlock.FlameType.class);

    public LitBlazeBurnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(FLAME_TYPE, LitBlazeBurnerBlock.FlameType.REGULAR));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FLAME_TYPE);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockRayTraceResult) {
        ItemStack heldItem = player.m_21120_(hand);
        if (heldItem.getItem() instanceof ShovelItem || heldItem.getItem().canPerformAction(heldItem, EXTINGUISH_FLAME_ACTION)) {
            world.playSound(player, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.5F, 2.0F);
            if (world.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                heldItem.hurtAndBreak(1, player, p -> p.m_21190_(hand));
                world.setBlockAndUpdate(pos, AllBlocks.BLAZE_BURNER.getDefaultState());
                return InteractionResult.SUCCESS;
            }
        } else if (state.m_61143_(FLAME_TYPE) == LitBlazeBurnerBlock.FlameType.REGULAR && heldItem.is(ItemTags.SOUL_FIRE_BASE_BLOCKS)) {
            world.playSound(player, pos, SoundEvents.SOUL_SAND_PLACE, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
            if (world.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                world.setBlockAndUpdate(pos, (BlockState) this.m_49966_().m_61124_(FLAME_TYPE, LitBlazeBurnerBlock.FlameType.SOUL));
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return ((BlazeBurnerBlock) AllBlocks.BLAZE_BURNER.get()).getShape(state, reader, pos, context);
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return AllItems.EMPTY_BLAZE_BURNER.asStack();
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        world.addAlwaysVisibleParticle(ParticleTypes.LARGE_SMOKE, true, (double) pos.m_123341_() + 0.5 + random.nextDouble() / 3.0 * (double) (random.nextBoolean() ? 1 : -1), (double) pos.m_123342_() + random.nextDouble() + random.nextDouble(), (double) pos.m_123343_() + 0.5 + random.nextDouble() / 3.0 * (double) (random.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
        if (random.nextInt(10) == 0) {
            world.playLocalSound((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.25F + random.nextFloat() * 0.25F, random.nextFloat() * 0.7F + 0.6F, false);
        }
        if (state.m_61143_(FLAME_TYPE) == LitBlazeBurnerBlock.FlameType.SOUL) {
            if (random.nextInt(8) == 0) {
                world.addParticle(ParticleTypes.SOUL, (double) ((float) pos.m_123341_() + 0.5F) + random.nextDouble() / 4.0 * (double) (random.nextBoolean() ? 1 : -1), (double) ((float) pos.m_123342_() + 0.3F) + random.nextDouble() / 2.0, (double) ((float) pos.m_123343_() + 0.5F) + random.nextDouble() / 4.0 * (double) (random.nextBoolean() ? 1 : -1), 0.0, random.nextDouble() * 0.04 + 0.04, 0.0);
            }
        } else {
            if (random.nextInt(5) == 0) {
                for (int i = 0; i < random.nextInt(1) + 1; i++) {
                    world.addParticle(ParticleTypes.LAVA, (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), (double) (random.nextFloat() / 2.0F), 5.0E-5, (double) (random.nextFloat() / 2.0F));
                }
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level p_180641_2_, BlockPos p_180641_3_) {
        return state.m_61143_(FLAME_TYPE) == LitBlazeBurnerBlock.FlameType.REGULAR ? 1 : 2;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return ((BlazeBurnerBlock) AllBlocks.BLAZE_BURNER.get()).getCollisionShape(state, reader, pos, context);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    public static int getLight(BlockState state) {
        return state.m_61143_(FLAME_TYPE) == LitBlazeBurnerBlock.FlameType.SOUL ? 9 : 12;
    }

    public static enum FlameType implements StringRepresentable {

        REGULAR, SOUL;

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}