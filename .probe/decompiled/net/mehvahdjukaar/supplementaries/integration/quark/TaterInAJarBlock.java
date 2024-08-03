package net.mehvahdjukaar.supplementaries.integration.quark;

import java.util.Collections;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.blocks.JarBlock;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.block.TinyPotatoBlock;
import org.violetmoon.quark.addons.oddities.block.be.TinyPotatoBlockEntity;

public class TaterInAJarBlock extends TinyPotatoBlock {

    private static final VoxelShape SHAPE = JarBlock.SHAPE;

    public TaterInAJarBlock() {
        super(null);
    }

    @Override
    public SoundType getSoundType(BlockState state) {
        return ModSounds.JAR;
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TaterInAJarBlock.Tile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof TaterInAJarBlock.Tile tile) {
            tile.interact(player, hand, player.m_21120_(hand), hit.getDirection());
            if (world instanceof ServerLevel serverLevel) {
                AABB box = SHAPE.bounds();
                serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, (double) pos.m_123341_() + box.minX + Math.random() * (box.maxX - box.minX), (double) pos.m_123342_() + box.maxY - 1.0, (double) pos.m_123343_() + box.minZ + Math.random() * (box.maxZ - box.minZ), 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack stack = new ItemStack(this);
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof TaterInAJarBlock.Tile te && te.m_8077_()) {
            stack.setHoverName(te.m_7770_());
        }
        return Collections.singletonList(stack);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level level = ctx.m_43725_();
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.m_43723_();
        if (player != null && !player.m_6144_()) {
            FluidState fluidState = level.getFluidState(pos);
            Item i = (Item) ModRegistry.JAR_ITEM.get();
            if (!level.isClientSide) {
                Utils.swapItemNBT(player, ctx.m_43724_(), ctx.m_43722_(), new ItemStack(i));
            }
            BlockState state = (BlockState) ((Block) CompatObjects.TATER.get()).defaultBlockState().m_61124_(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
            return (BlockState) state.m_61124_(BlockStateProperties.HORIZONTAL_FACING, ctx.m_8125_().getOpposite());
        } else {
            return super.getStateForPlacement(ctx);
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return Utils.getTicker(pBlockEntityType, (BlockEntityType) QuarkCompat.TATER_IN_A_JAR_TILE.get(), TinyPotatoBlockEntity::commonTick);
    }

    public static class Tile extends TinyPotatoBlockEntity {

        public Tile(BlockPos pos, BlockState state) {
            super(pos, state);
            this.angry = true;
        }

        @Override
        public BlockEntityType<TaterInAJarBlock.Tile> getType() {
            return (BlockEntityType<TaterInAJarBlock.Tile>) QuarkCompat.TATER_IN_A_JAR_TILE.get();
        }
    }
}