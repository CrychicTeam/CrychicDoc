package snownee.jade.addon.create;

import com.simibubi.create.content.equipment.blueprint.BlueprintOverlayRenderer;
import java.lang.reflect.Field;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.addon.JadeAddons;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IDisplayHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum CraftingBlueprintProvider implements IEntityComponentProvider {

    INSTANCE;

    public static Field RESULT;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        ItemStack result = getResult();
        if (!result.isEmpty()) {
            tooltip.add(IDisplayHelper.get().stripColor(result.getHoverName()));
        }
    }

    @Override
    public IElement getIcon(EntityAccessor accessor, IPluginConfig config, IElement currentIcon) {
        ItemStack result = getResult();
        return !result.isEmpty() ? IElementHelper.get().item(result) : null;
    }

    public static ItemStack getResult() {
        if (RESULT != null) {
            try {
                return (ItemStack) RESULT.get(null);
            } catch (Throwable var1) {
                JadeAddons.LOGGER.trace("Error getting blueprint result", var1);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getUid() {
        return CreatePlugin.CRAFTING_BLUEPRINT;
    }

    static {
        try {
            RESULT = BlueprintOverlayRenderer.class.getDeclaredField("result");
            RESULT.setAccessible(true);
        } catch (Throwable var1) {
            JadeAddons.LOGGER.trace("Error accessing blueprint result field", var1);
            RESULT = null;
        }
    }
}