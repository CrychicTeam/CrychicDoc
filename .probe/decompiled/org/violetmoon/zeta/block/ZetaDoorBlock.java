package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.item.ZetaDoubleHighBlockItem;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.registry.IZetaBlockItemProvider;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaDoorBlock extends DoorBlock implements IZetaBlock, IZetaBlockItemProvider {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaDoorBlock(BlockSetType setType, String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(properties, setType);
        this.module = module;
        if (module != null) {
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
            module.zeta.registry.registerBlock(this, regname, true);
            CreativeTabManager.addToCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, this);
            CreativeTabManager.addToCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS, this);
        }
    }

    public ZetaDoorBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    @Override
    public BlockItem provideItemBlock(Block block, Item.Properties props) {
        return new ZetaDoubleHighBlockItem(this, props);
    }
}