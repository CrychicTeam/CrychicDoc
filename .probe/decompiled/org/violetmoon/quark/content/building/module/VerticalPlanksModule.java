package org.violetmoon.quark.content.building.module;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.VanillaWoods;

@ZetaLoadModule(category = "building")
public class VerticalPlanksModule extends ZetaModule {

    @LoadEvent
    public final void register(ZRegister event) {
        for (VanillaWoods.Wood type : VanillaWoods.ALL) {
            add(type.name(), type.planks(), this);
        }
    }

    public static ZetaBlock add(String name, Block base, ZetaModule module) {
        return (ZetaBlock) new ZetaBlock("vertical_" + name + "_planks", module, BlockBehaviour.Properties.copy(base)).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, base, false);
    }
}