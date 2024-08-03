package net.blay09.mods.waystones.compat;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import net.blay09.mods.waystones.block.WaystoneBlockBase;

public class WTHITIntegration implements IWailaPlugin {

    public void register(IRegistrar registrar) {
        registrar.addComponent(new WTHITIntegration.WaystoneDataProvider(), TooltipPosition.BODY, WaystoneBlockBase.class);
    }

    private static class WaystoneDataProvider implements IBlockComponentProvider {

        public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
            WaystonesWailaUtils.appendTooltip(accessor.getBlockEntity(), accessor.getPlayer(), tooltip::addLine);
        }
    }
}