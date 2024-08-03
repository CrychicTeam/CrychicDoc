package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateAnchor;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockEndPirateAnchor extends BaseEntityBlock implements AMSpecialRenderBlock {

    public static final BooleanProperty EASTORWEST = BooleanProperty.create("eastorwest");

    public static final EnumProperty<BlockEndPirateAnchor.PieceType> PIECE = EnumProperty.create("piece", BlockEndPirateAnchor.PieceType.class);

    protected static final VoxelShape FULL_AABB_EW = Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 12.0);

    protected static final VoxelShape FULL_AABB_NS = Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 16.0);

    protected static final VoxelShape CHAIN_AABB = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    protected BlockEndPirateAnchor() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).friction(0.97F).strength(10.0F).lightLevel(i -> 6).sound(SoundType.STONE).noOcclusion());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(EASTORWEST, false)).m_61124_(PIECE, BlockEndPirateAnchor.PieceType.ANCHOR));
    }

    public static boolean isClearForPlacement(LevelReader reader, BlockPos center, boolean eastOrWest) {
        for (BlockPos offset : TileEntityEndPirateAnchor.getValidBBPositions(eastOrWest)) {
            BlockPos check = center.offset(offset);
            if (!reader.isEmptyBlock(check) || !reader.m_8055_(check).m_247087_()) {
                return false;
            }
        }
        return true;
    }

    public static void placeAnchor(Level level, BlockPos pos, BlockState state) {
        for (BlockPos offset : TileEntityEndPirateAnchor.getValidBBPositions((Boolean) state.m_61143_(EASTORWEST))) {
            if (!offset.equals(BlockPos.ZERO)) {
                level.setBlock(pos.offset(offset), (BlockState) state.m_61124_(PIECE, BlockEndPirateAnchor.PieceType.ANCHOR_SIDE), 2);
            }
        }
    }

    public static void removeAnchor(Level level, BlockPos pos, BlockState state) {
        for (BlockPos offset : TileEntityEndPirateAnchor.getValidBBPositions((Boolean) state.m_61143_(EASTORWEST))) {
            level.setBlock(pos.offset(offset), Blocks.AIR.defaultBlockState(), 67);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader levelreader = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        BlockPos actualPos = context.getClickedPos().relative(context.m_43719_().getOpposite());
        BlockPos u = blockpos.above();
        BlockPos d = blockpos.below();
        BlockState clickState = levelreader.m_8055_(actualPos);
        boolean axis = context.m_8125_().getAxis() == Direction.Axis.X;
        if (clickState.m_60734_() instanceof BlockEndPirateAnchor) {
            axis = (Boolean) clickState.m_61143_(EASTORWEST);
        }
        return isClearForPlacement(levelreader, blockpos, axis) ? (BlockState) this.m_49966_().m_61124_(EASTORWEST, axis) : null;
    }

    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.CHAIN;
    }

    public boolean isScaffolding(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.CHAIN;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity && state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.CHAIN) {
            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity.f_19862_ && !livingEntity.m_20069_()) {
                livingEntity.f_19789_ = 0.0F;
                Vec3 motion = livingEntity.m_20184_();
                double d0 = Mth.clamp(motion.x, -0.15F, 0.15F);
                double d1 = Mth.clamp(motion.z, -0.15F, 0.15F);
                double d2 = 0.3;
                if (d2 < 0.0 && livingEntity.isSuppressingSlidingDownLadder()) {
                    d2 = 0.0;
                }
                motion = new Vec3(d0, d2, d1);
                livingEntity.m_20256_(motion);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos blockPos0, boolean boolean1) {
        if (state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.ANCHOR_SIDE) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -3; j <= 3; j++) {
                    for (int k = -2; k <= 2; k++) {
                        BlockPos offsetPos = pos.offset(i, j, k);
                        BlockEntity var12 = level.getBlockEntity(offsetPos);
                        if (var12 instanceof TileEntityEndPirateAnchor) {
                            TileEntityEndPirateAnchor anchor = (TileEntityEndPirateAnchor) var12;
                            if (!anchor.hasAllAnchorBlocks()) {
                                removeAnchor(level, offsetPos, level.getBlockState(offsetPos));
                                level.m_46961_(offsetPos, true);
                            }
                        }
                    }
                }
            }
        }
        if (!this.canSurviveAnchor(state, level, pos)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity player, ItemStack stack) {
        placeAnchor(level, pos, state);
    }

    public boolean canSurviveAnchor(BlockState state, LevelReader world, BlockPos pos) {
        if (state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.ANCHOR) {
            return true;
        } else {
            if (state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.ANCHOR_SIDE) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -3; j <= 0; j++) {
                        for (int k = -1; k <= 1; k++) {
                            BlockPos offsetPos = pos.offset(i, j, k);
                            BlockState anchorState = world.m_8055_(offsetPos);
                            if (anchorState.m_60734_() instanceof BlockEndPirateAnchor && anchorState.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.ANCHOR && this.isPartOfAnchor(anchorState, world, offsetPos, pos, (Boolean) state.m_61143_(EASTORWEST))) {
                                return true;
                            }
                        }
                    }
                }
            } else if (state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.CHAIN) {
                BlockPos below = pos.below();
                BlockState chainBelow = world.m_8055_(below);
                BlockState chainAbove = world.m_8055_(below);
                return chainBelow.m_60734_() instanceof BlockEndPirateAnchor && (chainAbove.m_60734_() instanceof BlockEndPirateAnchor || chainAbove.m_60734_() instanceof BlockEndPirateAnchorWinch);
            }
            return false;
        }
    }

    public boolean isPartOfAnchor(BlockState anchor, LevelReader level, BlockPos center, BlockPos pos, boolean eastOrWest) {
        if ((Boolean) anchor.m_61143_(EASTORWEST) == eastOrWest) {
            BlockPos offset = pos.subtract(center);
            return TileEntityEndPirateAnchor.getValidBBPositions(eastOrWest).contains(offset);
        } else {
            return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(EASTORWEST, PIECE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if (state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.CHAIN) {
            return CHAIN_AABB;
        } else {
            return state.m_61143_(EASTORWEST) ? FULL_AABB_NS : FULL_AABB_EW;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.ANCHOR ? new TileEntityEndPirateAnchor(pos, state) : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState state, BlockEntityType<T> blockEntityTypeT1) {
        return state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.ANCHOR ? m_152132_(blockEntityTypeT1, AMTileEntityRegistry.END_PIRATE_ANCHOR.get(), TileEntityEndPirateAnchor::commonTick) : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.ANCHOR_SIDE ? RenderShape.INVISIBLE : RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return state.m_61143_(PIECE) == BlockEndPirateAnchor.PieceType.ANCHOR ? super.m_49635_(state, builder) : Collections.emptyList();
    }

    public static enum PieceType implements StringRepresentable {

        ANCHOR, ANCHOR_SIDE, CHAIN;

        public String toString() {
            return this.getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}