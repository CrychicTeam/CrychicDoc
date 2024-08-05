package org.violetmoon.quark.content.building.module;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "building")
public class MoreBrickTypesModule extends ZetaModule {

    @Config(flag = "blue_nether_bricks", description = "This also comes with a utility recipe for Red Nether Bricks")
    public boolean enableBlueNetherBricks = true;

    @Config(flag = "sandstone_bricks", description = "This also includes Red Sandstone Bricks and Soul Sandstone Bricks")
    public boolean enableSandstoneBricks = true;

    @Config(flag = "cobblestone_bricks", description = "This also includes Mossy Cobblestone Bricks")
    private static boolean enableCobblestoneBricks = true;

    @Config(flag = "blackstone_bricks", description = "Requires Cobblestone Bricks to be enabled")
    private static boolean enableBlackstoneBricks = true;

    @Config(flag = "dirt_bricks", description = "Requires Cobblestone Bricks to be enabled")
    private static boolean enableDirtBricks = true;

    @Config(flag = "netherrack_bricks", description = "Requires Cobblestone Bricks to be enabled")
    private static boolean enableNetherrackBricks = true;

    @LoadEvent
    public final void register(ZRegister event) {
        this.add(event, "blue_nether", Blocks.NETHER_BRICKS, () -> this.enableBlueNetherBricks, Blocks.BASALT);
        this.add(event, "sandstone", Blocks.SANDSTONE, () -> this.enableSandstoneBricks, Blocks.RED_SANDSTONE);
        this.add(event, "red_sandstone", Blocks.RED_SANDSTONE, () -> this.enableSandstoneBricks, Blocks.SEA_LANTERN);
        this.add(event, "soul_sandstone", Blocks.SANDSTONE, () -> this.enableSandstoneBricks && Quark.ZETA.modules.isEnabled(SoulSandstoneModule.class), Blocks.SEA_LANTERN);
        this.add(event, "cobblestone", Blocks.COBBLESTONE, () -> enableCobblestoneBricks, Blocks.MOSSY_COBBLESTONE);
        this.add(event, "mossy_cobblestone", Blocks.MOSSY_COBBLESTONE, () -> enableCobblestoneBricks, Blocks.SMOOTH_STONE);
        this.add(event, "blackstone", Blocks.BLACKSTONE, () -> enableBlackstoneBricks && enableCobblestoneBricks, Blocks.END_STONE);
        this.add(event, "dirt", Blocks.DIRT, () -> enableDirtBricks && enableCobblestoneBricks, Blocks.PACKED_MUD);
        this.add(event, "netherrack", Blocks.NETHERRACK, () -> enableNetherrackBricks && enableCobblestoneBricks, Blocks.NETHER_BRICKS);
    }

    private void add(ZRegister event, String name, Block parent, BooleanSupplier cond, Block placeBehind) {
        event.getVariantRegistry().addSlabStairsWall((IZetaBlock) new ZetaBlock(name + "_bricks", this, BlockBehaviour.Properties.copy(parent).requiresCorrectToolForDrops()).setCondition(cond).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, placeBehind, true), null);
    }
}