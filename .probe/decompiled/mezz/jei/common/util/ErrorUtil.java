package mezz.jei.common.util;

import java.util.Collection;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.platform.IPlatformModHelper;
import mezz.jei.common.platform.IPlatformRegistry;
import mezz.jei.common.platform.Services;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public final class ErrorUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private ErrorUtil() {
    }

    public static <T> String getIngredientInfo(T ingredient, IIngredientType<T> ingredientType, IIngredientManager ingredientManager) {
        IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(ingredientType);
        return ingredientHelper.getErrorInfo(ingredient);
    }

    public static String getItemStackInfo(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return "null";
        } else {
            Item item = itemStack.getItem();
            IPlatformRegistry<Item> itemRegistry = Services.PLATFORM.getRegistry(Registries.ITEM);
            String itemName = (String) itemRegistry.getRegistryName(item).map(ResourceLocation::toString).orElseGet(() -> {
                if (item instanceof BlockItem) {
                    Block block = ((BlockItem) item).getBlock();
                    String blockName;
                    if (block == null) {
                        blockName = "null";
                    } else {
                        IPlatformRegistry<Block> blockRegistry = Services.PLATFORM.getRegistry(Registries.BLOCK);
                        blockName = (String) blockRegistry.getRegistryName(block).map(ResourceLocation::toString).orElseGet(() -> block.getClass().getName());
                    }
                    return "BlockItem(" + blockName + ")";
                } else {
                    return item.getClass().getName();
                }
            });
            CompoundTag nbt = itemStack.getTag();
            return nbt != null ? itemStack + " " + itemName + " nbt:" + nbt : itemStack + " " + itemName;
        }
    }

    public static void checkNotEmpty(ItemStack itemStack) {
        if (itemStack == null) {
            throw new NullPointerException("ItemStack must not be null.");
        } else if (itemStack.isEmpty()) {
            String info = getItemStackInfo(itemStack);
            throw new IllegalArgumentException("ItemStack value must not be empty. " + info);
        }
    }

    public static void checkNotEmpty(ItemStack itemStack, String name) {
        if (itemStack == null) {
            throw new NullPointerException(name + " must not be null.");
        } else if (itemStack.isEmpty()) {
            String info = getItemStackInfo(itemStack);
            throw new IllegalArgumentException("ItemStack " + name + " must not be empty. " + info);
        }
    }

    public static <T> void checkNotEmpty(T[] values, String name) {
        if (values == null) {
            throw new NullPointerException(name + " must not be null.");
        } else if (values.length == 0) {
            throw new IllegalArgumentException(name + " must not be empty.");
        } else {
            for (T value : values) {
                if (value == null) {
                    throw new NullPointerException(name + " must not contain null values.");
                }
            }
        }
    }

    public static void checkNotEmpty(Collection<?> values, String name) {
        if (values == null) {
            throw new NullPointerException(name + " must not be null.");
        } else if (values.isEmpty()) {
            throw new IllegalArgumentException(name + " must not be empty.");
        } else {
            if (!(values instanceof NonNullList)) {
                for (Object value : values) {
                    if (value == null) {
                        throw new NullPointerException(name + " must not contain null values.");
                    }
                }
            }
        }
    }

    public static <T> void checkNotNull(@Nullable T object, String name) {
        if (object == null) {
            throw new NullPointerException(name + " must not be null.");
        }
    }

    public static void checkNotNull(Collection<?> values, String name) {
        if (values == null) {
            throw new NullPointerException(name + " must not be null.");
        } else {
            if (!(values instanceof NonNullList)) {
                for (Object value : values) {
                    if (value == null) {
                        throw new NullPointerException(name + " must not contain null values.");
                    }
                }
            }
        }
    }

    public static void assertMainThread() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null && !minecraft.m_18695_()) {
            Thread currentThread = Thread.currentThread();
            throw new IllegalStateException("A JEI API method is being called by another mod from the wrong thread:\n" + currentThread + "\nIt must be called on the main thread by using Minecraft.addScheduledTask.");
        }
    }

    public static <T> CrashReport createIngredientCrashReport(Throwable throwable, String title, IIngredientManager ingredientManager, ITypedIngredient<T> typedIngredient) {
        CrashReport crashReport = CrashReport.forThrowable(throwable, title);
        IIngredientType<T> ingredientType = typedIngredient.getType();
        IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(ingredientType);
        CrashReportCategory category = crashReport.addCategory("Ingredient");
        setIngredientCategoryDetails(category, typedIngredient, ingredientHelper);
        return crashReport;
    }

    public static <T> void logIngredientCrash(Throwable throwable, String title, IIngredientManager ingredientManager, ITypedIngredient<T> typedIngredient) {
        CrashReportCategory category = new CrashReportCategory("Ingredient");
        IIngredientType<T> ingredientType = typedIngredient.getType();
        IIngredientHelper<T> ingredientHelper = ingredientManager.getIngredientHelper(ingredientType);
        setIngredientCategoryDetails(category, typedIngredient, ingredientHelper);
        LOGGER.error(crashReportToString(throwable, title, category));
    }

    private static <T> void setIngredientCategoryDetails(CrashReportCategory category, ITypedIngredient<T> typedIngredient, IIngredientHelper<T> ingredientHelper) {
        T ingredient = typedIngredient.getIngredient();
        IIngredientType<T> ingredientType = typedIngredient.getType();
        IPlatformModHelper modHelper = Services.PLATFORM.getModHelper();
        category.setDetail("Name", (CrashReportDetail<String>) (() -> ingredientHelper.getDisplayName(ingredient)));
        category.setDetail("Mod's Name", (CrashReportDetail<String>) (() -> {
            String modId = ingredientHelper.getDisplayModId(ingredient);
            return modHelper.getModNameForModId(modId);
        }));
        category.setDetail("Registry Name", (CrashReportDetail<String>) (() -> ingredientHelper.getResourceLocation(ingredient).toString()));
        category.setDetail("Class Name", (CrashReportDetail<String>) (() -> ingredient.getClass().toString()));
        category.setDetail("toString Name", ingredient::toString);
        category.setDetail("Unique Id for JEI (for JEI Blacklist)", (CrashReportDetail<String>) (() -> ingredientHelper.getUniqueId(ingredient, UidContext.Ingredient)));
        category.setDetail("Ingredient Type for JEI", (CrashReportDetail<String>) (() -> ingredientType.getIngredientClass().toString()));
        category.setDetail("Error Info gathered from JEI", (CrashReportDetail<String>) (() -> ingredientHelper.getErrorInfo(ingredient)));
    }

    private static String crashReportToString(Throwable t, String title, CrashReportCategory... categories) {
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append(":\n\n");
        for (CrashReportCategory category : categories) {
            category.getDetails(sb);
            sb.append("\n\n");
        }
        sb.append("-- Stack Trace --\n\n");
        sb.append(ExceptionUtils.getStackTrace(t));
        return sb.toString();
    }
}