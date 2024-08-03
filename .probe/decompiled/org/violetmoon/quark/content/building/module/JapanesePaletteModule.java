package org.violetmoon.quark.content.building.module;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.CreativeModeTabs;
import org.violetmoon.quark.content.building.block.BambooMatBlock;
import org.violetmoon.quark.content.building.block.BambooMatCarpetBlock;
import org.violetmoon.quark.content.building.block.PaperLanternBlock;
import org.violetmoon.quark.content.building.block.PaperWallBlock;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;

@ZetaLoadModule(category = "building")
public class JapanesePaletteModule extends ZetaModule {

    @Config(flag = "paper_decor")
    public static boolean enablePaperBlocks = true;

    @Config(flag = "bamboo_mat")
    public static boolean enableBambooMats = true;

    @LoadEvent
    public final void register(ZRegister event) {
        BooleanSupplier paperBlockCond = () -> enablePaperBlocks;
        BooleanSupplier bambooMatCond = () -> enableBambooMats;
        IZetaBlock paperLantern = new PaperLanternBlock("paper_lantern", this).setCondition(paperBlockCond);
        IZetaBlock paperLanternSakura = new PaperLanternBlock("paper_lantern_sakura", this).setCondition(paperBlockCond);
        CreativeTabManager.daisyChain();
        new PaperWallBlock(paperLantern, "paper_wall").setCondition(paperBlockCond).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, paperLantern.getBlock(), false);
        new PaperWallBlock(paperLantern, "paper_wall_big").setCondition(paperBlockCond).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, paperLantern.getBlock(), false);
        CreativeTabManager.endDaisyChain();
        new PaperWallBlock(paperLantern, "paper_wall_sakura").setCondition(paperBlockCond).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, paperLanternSakura.getBlock(), false);
        new BambooMatBlock("bamboo_mat", this).setCondition(bambooMatCond);
        new BambooMatCarpetBlock("bamboo_mat_carpet", this).setCondition(bambooMatCond);
    }
}