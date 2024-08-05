package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaLeavesBlock extends LeavesBlock implements IZetaBlock {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaLeavesBlock(String name, @Nullable ZetaModule module, MapColor color) {
        super(BlockBehaviour.Properties.of().mapColor(color).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((s, r, p, t) -> false).isSuffocating((s, r, p) -> false).isViewBlocking((s, r, p) -> false).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor((s, r, p) -> false));
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerBlock(this, name + "_leaves", true);
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT_MIPPED);
            this.setCreativeTab(CreativeModeTabs.NATURAL_BLOCKS, Blocks.BROWN_MUSHROOM_BLOCK, true);
        }
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    public ZetaLeavesBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }
}