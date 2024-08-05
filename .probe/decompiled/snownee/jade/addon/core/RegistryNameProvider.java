package snownee.jade.addon.core;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.util.CommonProxy;

public enum RegistryNameProvider implements IBlockComponentProvider, IEntityComponentProvider {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        this.append(tooltip, CommonProxy.getId(accessor.getBlock()).toString(), config);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        this.append(tooltip, CommonProxy.getId(accessor.getEntity().getType()).toString(), config);
    }

    public void append(ITooltip tooltip, String id, IPluginConfig config) {
        RegistryNameProvider.Mode mode = config.getEnum(Identifiers.CORE_REGISTRY_NAME);
        if (mode != RegistryNameProvider.Mode.OFF) {
            if (mode != RegistryNameProvider.Mode.ADVANCED_TOOLTIPS || Minecraft.getInstance().options.advancedItemTooltips) {
                tooltip.add(IWailaConfig.get().getFormatting().registryName(id));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.CORE_REGISTRY_NAME;
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public int getDefaultPriority() {
        return -9900;
    }

    public static enum Mode {

        ON, OFF, ADVANCED_TOOLTIPS
    }
}