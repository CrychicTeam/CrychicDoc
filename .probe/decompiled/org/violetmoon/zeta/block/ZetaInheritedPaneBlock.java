package org.violetmoon.zeta.block;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;
import org.violetmoon.zeta.registry.IZetaItemColorProvider;

public class ZetaInheritedPaneBlock extends ZetaPaneBlock implements IZetaBlock, IZetaBlockColorProvider {

    public final IZetaBlock parent;

    public ZetaInheritedPaneBlock(IZetaBlock parent, String name, BlockBehaviour.Properties properties) {
        super(name, parent.getModule(), properties, null);
        this.parent = parent;
        if (this.module != null && parent.getModule() != null) {
            parent.getModule().zeta.renderLayerRegistry.mock(this, parent.getBlock());
        }
    }

    public ZetaInheritedPaneBlock(IZetaBlock parent, BlockBehaviour.Properties properties) {
        this(parent, ((ZetaModule) Objects.requireNonNull(parent.getModule(), "Can only use this constructor on blocks with a ZetaModule")).zeta.registryUtil.inheritQuark(parent, "%s_pane"), properties);
    }

    public ZetaInheritedPaneBlock(IZetaBlock parent) {
        this(parent, BlockBehaviour.Properties.copy(parent.getBlock()));
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && this.parent.isEnabled();
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