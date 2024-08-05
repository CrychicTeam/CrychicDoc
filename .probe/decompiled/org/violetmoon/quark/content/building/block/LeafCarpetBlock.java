package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class LeafCarpetBlock extends ZetaBlock implements IZetaBlockColorProvider {

    private static final VoxelShape SHAPE = m_49796_(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public final BlockState baseState;

    public LeafCarpetBlock(String name, Block base, @Nullable ZetaModule module) {
        super(name, module, BlockBehaviour.Properties.of().mapColor(base.defaultBlockState().f_283893_).noCollission().strength(0.0F).sound(SoundType.GRASS).noOcclusion().ignitedByLava());
        this.baseState = base.defaultBlockState();
        if (module != null) {
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT_MIPPED);
            this.setCreativeTab(CreativeModeTabs.NATURAL_BLOCKS, base, false);
        }
    }

    public BlockState getBaseState() {
        return this.baseState;
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext useContext) {
        return useContext.m_43722_().isEmpty() || useContext.m_43722_().getItem() != this.m_5456_();
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        return !state.m_60710_(world, pos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, facing, facingState, world, pos, facingPos);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader world, BlockPos pos) {
        return !world.isEmptyBlock(pos.below());
    }

    @Nullable
    @Override
    public String getBlockColorProviderName() {
        return "leaf_carpet";
    }

    @Nullable
    @Override
    public String getItemColorProviderName() {
        return "leaf_carpet";
    }
}