package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;
import org.violetmoon.zeta.registry.IZetaItemColorProvider;
import org.violetmoon.zeta.registry.VariantRegistry;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaSlabBlock extends SlabBlock implements IZetaBlock, IZetaBlockColorProvider {

    public final IZetaBlock parent;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaSlabBlock(IZetaBlock parent, @Nullable ResourceKey<CreativeModeTab> tab) {
        super(VariantRegistry.realStateCopy(parent));
        this.parent = parent;
        ZetaModule module = parent.getModule();
        if (module == null) {
            throw new IllegalArgumentException("Can only create ZetaSlabBlock with blocks belonging to a module");
        } else {
            String resloc = module.zeta.registryUtil.inheritQuark(parent, "%s_slab");
            parent.getModule().zeta.registry.registerBlock(this, resloc, true);
            parent.getModule().zeta.renderLayerRegistry.mock(this, parent.getBlock());
            this.setCreativeTab(tab == null ? CreativeModeTabs.BUILDING_BLOCKS : tab, parent.getBlock(), false);
        }
    }

    @Override
    public boolean isFlammableZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        BlockState parentState = this.parent.getBlock().defaultBlockState();
        return this.parent.getModule().zeta.blockExtensions.get(parentState).isFlammableZeta(parentState, world, pos, face);
    }

    @Override
    public int getFlammabilityZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        BlockState parentState = this.parent.getBlock().defaultBlockState();
        return this.parent.getModule().zeta.blockExtensions.get(parentState).getFlammabilityZeta(parentState, world, pos, face);
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.parent.getModule();
    }

    public ZetaSlabBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplierZeta(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
        BlockState parentState = this.parent.getBlock().defaultBlockState();
        return this.parent.getModule().zeta.blockExtensions.get(parentState).getBeaconColorMultiplierZeta(parentState, world, pos, beaconPos);
    }

    @Nullable
    @Override
    public String getBlockColorProviderName() {
        return this.parent instanceof IZetaBlockColorProvider prov ? prov.getBlockColorProviderName() : null;
    }

    @Nullable
    @Override
    public String getItemColorProviderName() {
        return this.parent instanceof IZetaItemColorProvider prov ? prov.getItemColorProviderName() : null;
    }
}