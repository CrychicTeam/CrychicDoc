package org.violetmoon.quark.content.building.block;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.building.module.HedgesModule;
import org.violetmoon.quark.content.world.block.BlossomLeavesBlock;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.block.ZetaFenceBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class HedgeBlock extends ZetaFenceBlock implements IZetaBlock, IZetaBlockColorProvider {

    private static final VoxelShape WOOD_SHAPE = m_49796_(6.0, 0.0, 6.0, 10.0, 15.0, 10.0);

    private static final VoxelShape HEDGE_CENTER_SHAPE = m_49796_(2.0, 1.0, 2.0, 14.0, 16.0, 14.0);

    private static final VoxelShape NORTH_SHAPE = m_49796_(2.0, 1.0, 0.0, 14.0, 16.0, 2.0);

    private static final VoxelShape SOUTH_SHAPE = m_49796_(2.0, 1.0, 14.0, 14.0, 16.0, 15.0);

    private static final VoxelShape EAST_SHAPE = m_49796_(14.0, 1.0, 2.0, 16.0, 16.0, 14.0);

    private static final VoxelShape WEST_SHAPE = m_49796_(0.0, 1.0, 2.0, 2.0, 16.0, 14.0);

    private static final VoxelShape EXTEND_SHAPE = m_49796_(2.0, 0.0, 2.0, 14.0, 1.0, 14.0);

    private final Object2IntMap<BlockState> hedgeStateToIndex = new Object2IntOpenHashMap();

    private final VoxelShape[] hedgeShapes;

    public final BlockState leafState;

    public static final BooleanProperty EXTEND = BooleanProperty.create("extend");

    public HedgeBlock(String regname, @Nullable ZetaModule module, Block fence, Block leaf) {
        super(regname, module, BlockBehaviour.Properties.copy(fence));
        this.leafState = leaf.defaultBlockState();
        this.m_49959_((BlockState) this.m_49966_().m_61124_(EXTEND, false));
        this.hedgeShapes = this.cacheHedgeShapes(this.f_49792_.getPossibleStates());
        if (module != null) {
            CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.NATURAL_BLOCKS, this, leaf, false);
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
        }
    }

    @Deprecated(forRemoval = true)
    public HedgeBlock(@Nullable ZetaModule module, Block fence, Block leaf) {
        this(legacyComputeRegistryName(leaf), module, fence, leaf);
    }

    @Deprecated(forRemoval = true)
    private static String legacyComputeRegistryName(Block leaf) {
        ResourceLocation leafRes = Quark.ZETA.registry.getRegistryName(leaf, BuiltInRegistries.BLOCK);
        return leaf instanceof BlossomLeavesBlock ? leafRes.getPath().replaceAll("_blossom_leaves", "") + "_blossom_hedge" : leafRes.getPath().replaceAll("_leaves", "_hedge");
    }

    public BlockState getLeaf() {
        return this.leafState;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return this.hedgeShapes[this.getHedgeAABBIndex(state)];
    }

    private VoxelShape[] cacheHedgeShapes(ImmutableList<BlockState> possibleStates) {
        VoxelShape[] shapes = new VoxelShape[possibleStates.size()];
        for (int i = 0; i < shapes.length; i++) {
            BlockState state = (BlockState) possibleStates.get(i);
            int realIndex = this.getHedgeAABBIndex(state);
            VoxelShape finishedShape = Shapes.or(state.m_61143_(EXTEND) ? EXTEND_SHAPE : WOOD_SHAPE, HEDGE_CENTER_SHAPE);
            if ((Boolean) state.m_61143_(FenceBlock.f_52309_)) {
                finishedShape = Shapes.or(finishedShape, NORTH_SHAPE);
            }
            if ((Boolean) state.m_61143_(FenceBlock.f_52311_)) {
                finishedShape = Shapes.or(finishedShape, SOUTH_SHAPE);
            }
            if ((Boolean) state.m_61143_(FenceBlock.f_52310_)) {
                finishedShape = Shapes.or(finishedShape, EAST_SHAPE);
            }
            if ((Boolean) state.m_61143_(FenceBlock.f_52312_)) {
                finishedShape = Shapes.or(finishedShape, WEST_SHAPE);
            }
            shapes[realIndex] = finishedShape;
        }
        return shapes;
    }

    protected int getHedgeAABBIndex(BlockState curr) {
        return this.hedgeStateToIndex.computeIntIfAbsent(curr, state -> {
            int i = 0;
            if ((Boolean) state.m_61143_(FenceBlock.f_52309_)) {
                i |= 1;
            }
            if ((Boolean) state.m_61143_(FenceBlock.f_52311_)) {
                i |= 2;
            }
            if ((Boolean) state.m_61143_(FenceBlock.f_52310_)) {
                i |= 4;
            }
            if ((Boolean) state.m_61143_(FenceBlock.f_52312_)) {
                i |= 8;
            }
            if ((Boolean) state.m_61143_(EXTEND)) {
                i |= 16;
            }
            return i;
        });
    }

    @Override
    public boolean connectsTo(BlockState state, boolean isSideSolid, @NotNull Direction direction) {
        return state.m_204336_(HedgesModule.hedgesTag);
    }

    @Override
    public boolean canSustainPlantZeta(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull Direction facing, @NotNull String plantabletype) {
        return facing == Direction.UP && !(Boolean) state.m_61143_(f_52313_) && "plains".equals(plantabletype);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter iblockreader = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        BlockPos down = blockpos.below();
        BlockState downState = iblockreader.getBlockState(down);
        return (BlockState) super.m_5573_(context).m_61124_(EXTEND, downState.m_60734_() instanceof HedgeBlock);
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(f_52313_)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return facing == Direction.DOWN ? (BlockState) stateIn.m_61124_(EXTEND, facingState.m_60734_() instanceof HedgeBlock) : super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(EXTEND);
    }

    @Nullable
    @Override
    public String getBlockColorProviderName() {
        return "hedge";
    }

    @Nullable
    @Override
    public String getItemColorProviderName() {
        return "hedge";
    }
}