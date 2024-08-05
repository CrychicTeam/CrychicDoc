package snownee.jade.addon.vanilla;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum ItemDisplayProvider implements IEntityComponentProvider {

    INSTANCE;

    @Nullable
    @Override
    public IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
        Display.ItemDisplay itemDisplay = (Display.ItemDisplay) accessor.getEntity();
        return itemDisplay.getSlot(0).get().isEmpty() ? null : IElementHelper.get().item(itemDisplay.getSlot(0).get());
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.MC_ITEM_DISPLAY;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}