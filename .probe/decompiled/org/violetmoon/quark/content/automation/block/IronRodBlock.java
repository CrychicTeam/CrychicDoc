package org.violetmoon.quark.content.automation.block;

import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.automation.module.IronRodModule;
import org.violetmoon.zeta.api.ICollateralMover;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class IronRodBlock extends EndRodBlock implements ICollateralMover, IZetaBlock {

    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public IronRodBlock(ZetaModule module) {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY).strength(5.0F, 10.0F).sound(SoundType.METAL).noOcclusion().forceSolidOn());
        module.zeta.registry.registerBlock(this, "iron_rod", true);
        CreativeTabManager.addToCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS, this);
        module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
        this.module = module;
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    public IronRodBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED);
    }

    @Override
    public boolean isCollateralMover(Level world, BlockPos source, Direction moveDirection, BlockPos pos) {
        return moveDirection == world.getBlockState(pos).m_61143_(f_52588_) && !world.getBlockState(pos.relative(moveDirection)).m_204336_(IronRodModule.ironRodImmuneTag);
    }

    @Override
    public ICollateralMover.MoveResult getCollateralMovement(Level world, BlockPos source, Direction moveDirection, Direction side, BlockPos pos) {
        return side == moveDirection && !world.getBlockState(pos.relative(side)).m_204336_(IronRodModule.ironRodImmuneTag) ? ICollateralMover.MoveResult.BREAK : ICollateralMover.MoveResult.SKIP;
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
    }
}