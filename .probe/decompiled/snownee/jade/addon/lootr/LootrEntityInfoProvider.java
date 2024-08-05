package snownee.jade.addon.lootr;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum LootrEntityInfoProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        LootrInfoProvider.appendTooltip(tooltip, accessor);
    }

    public void appendServerData(CompoundTag data, EntityAccessor accessor) {
        LootrInfoProvider.appendServerData(data, accessor.getEntity().getUUID());
    }

    @Override
    public ResourceLocation getUid() {
        return LootrPlugin.INFO;
    }
}