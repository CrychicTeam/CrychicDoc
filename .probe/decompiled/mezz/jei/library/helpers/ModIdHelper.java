package mezz.jei.library.helpers;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.config.DebugConfig;
import mezz.jei.common.platform.IPlatformModHelper;
import mezz.jei.common.platform.Services;
import mezz.jei.library.config.IModIdFormatConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

public final class ModIdHelper implements IModIdHelper {

    private final IModIdFormatConfig modIdFormattingConfig;

    private final IIngredientManager ingredientManager;

    public ModIdHelper(IModIdFormatConfig modIdFormattingConfig, IIngredientManager ingredientManager) {
        this.modIdFormattingConfig = modIdFormattingConfig;
        this.ingredientManager = ingredientManager;
    }

    @Override
    public boolean isDisplayingModNameEnabled() {
        String modNameFormat = this.modIdFormattingConfig.getModNameFormat();
        return !modNameFormat.isEmpty();
    }

    @Override
    public <T> List<Component> addModNameToIngredientTooltip(List<Component> tooltip, T ingredient, IIngredientHelper<T> ingredientHelper) {
        if (DebugConfig.isDebugModeEnabled() && Minecraft.getInstance().options.advancedItemTooltips) {
            tooltip = addDebugInfo(tooltip, ingredient, ingredientHelper);
        }
        if (!this.isDisplayingModNameEnabled()) {
            return tooltip;
        } else if (this.modIdFormattingConfig.isModNameFormatOverrideActive() && ingredient instanceof ItemStack) {
            return tooltip;
        } else {
            String modId = ingredientHelper.getDisplayModId(ingredient);
            String modName = this.getFormattedModNameForModId(modId);
            List<Component> tooltipCopy = new ArrayList(tooltip);
            tooltipCopy.add(Component.literal(modName));
            return tooltipCopy;
        }
    }

    @Override
    public <T> List<Component> addModNameToIngredientTooltip(List<Component> tooltip, ITypedIngredient<T> typedIngredient) {
        IIngredientType<T> type = typedIngredient.getType();
        T ingredient = typedIngredient.getIngredient();
        IIngredientHelper<T> ingredientHelper = this.ingredientManager.getIngredientHelper(type);
        return this.addModNameToIngredientTooltip(tooltip, ingredient, ingredientHelper);
    }

    private static String removeChatFormatting(String string) {
        String withoutFormattingCodes = ChatFormatting.stripFormatting(string);
        return withoutFormattingCodes == null ? "" : withoutFormattingCodes;
    }

    private static <T> List<Component> addDebugInfo(List<Component> tooltip, T ingredient, IIngredientHelper<T> ingredientHelper) {
        List<Component> var6 = new ArrayList(tooltip);
        MutableComponent jeiDebug = Component.literal("JEI Debug:");
        MutableComponent info = Component.literal("info: " + ingredientHelper.getErrorInfo(ingredient));
        MutableComponent uid = Component.literal("uid: " + ingredientHelper.getUniqueId(ingredient, UidContext.Ingredient));
        var6.add(jeiDebug.withStyle(ChatFormatting.DARK_GRAY));
        var6.add(info.withStyle(ChatFormatting.DARK_GRAY));
        var6.add(uid.withStyle(ChatFormatting.DARK_GRAY));
        return var6;
    }

    @Override
    public String getFormattedModNameForModId(String modId) {
        String modName = this.getModNameForModId(modId);
        modName = removeChatFormatting(modName);
        String modNameFormat = this.modIdFormattingConfig.getModNameFormat();
        if (!modNameFormat.isEmpty()) {
            return modNameFormat.contains("%MODNAME%") ? StringUtils.replaceOnce(modNameFormat, "%MODNAME%", modName) : modNameFormat + modName;
        } else {
            return modName;
        }
    }

    @Override
    public String getModNameForModId(String modId) {
        IPlatformModHelper modHelper = Services.PLATFORM.getModHelper();
        return modHelper.getModNameForModId(modId);
    }
}