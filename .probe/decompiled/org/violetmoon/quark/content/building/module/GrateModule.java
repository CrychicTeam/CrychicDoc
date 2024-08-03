package org.violetmoon.quark.content.building.module;

import net.minecraft.world.level.block.Block;
import org.violetmoon.quark.content.building.block.GrateBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "building")
public class GrateModule extends ZetaModule {

    @Hint
    public static Block grate;

    @LoadEvent
    public final void register(ZRegister event) {
        grate = new GrateBlock(this);
    }
}