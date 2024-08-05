package net.blay09.mods.waystones.compat;

import net.blay09.mods.waystones.block.WaystoneBlockBase;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin("waystones")
public class JadeIntegration implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new JadeIntegration.WaystoneDataProvider(), WaystoneBlockBase.class);
    }

    private static class WaystoneDataProvider implements IBlockComponentProvider {

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            WaystonesWailaUtils.appendTooltip(accessor.getBlockEntity(), accessor.getPlayer(), tooltip::add);
        }

        @Override
        public ResourceLocation getUid() {
            return WaystonesWailaUtils.WAYSTONE_UID;
        }
    }
}