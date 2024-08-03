package net.minecraft.client.gui.screens;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.gui.screens.inventory.HopperScreen;
import net.minecraft.client.gui.screens.inventory.LecternScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.gui.screens.inventory.SmokerScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.slf4j.Logger;

public class MenuScreens {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<MenuType<?>, MenuScreens.ScreenConstructor<?, ?>> SCREENS = Maps.newHashMap();

    public static <T extends AbstractContainerMenu> void create(@Nullable MenuType<T> menuTypeT0, Minecraft minecraft1, int int2, Component component3) {
        if (menuTypeT0 == null) {
            LOGGER.warn("Trying to open invalid screen with name: {}", component3.getString());
        } else {
            MenuScreens.ScreenConstructor<T, ?> $$4 = getConstructor(menuTypeT0);
            if ($$4 == null) {
                LOGGER.warn("Failed to create screen for menu type: {}", BuiltInRegistries.MENU.getKey(menuTypeT0));
            } else {
                $$4.fromPacket(component3, menuTypeT0, minecraft1, int2);
            }
        }
    }

    @Nullable
    private static <T extends AbstractContainerMenu> MenuScreens.ScreenConstructor<T, ?> getConstructor(MenuType<T> menuTypeT0) {
        return (MenuScreens.ScreenConstructor<T, ?>) SCREENS.get(menuTypeT0);
    }

    private static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> menuTypeExtendsM0, MenuScreens.ScreenConstructor<M, U> menuScreensScreenConstructorMU1) {
        MenuScreens.ScreenConstructor<?, ?> $$2 = (MenuScreens.ScreenConstructor<?, ?>) SCREENS.put(menuTypeExtendsM0, menuScreensScreenConstructorMU1);
        if ($$2 != null) {
            throw new IllegalStateException("Duplicate registration for " + BuiltInRegistries.MENU.getKey(menuTypeExtendsM0));
        }
    }

    public static boolean selfTest() {
        boolean $$0 = false;
        for (MenuType<?> $$1 : BuiltInRegistries.MENU) {
            if (!SCREENS.containsKey($$1)) {
                LOGGER.debug("Menu {} has no matching screen", BuiltInRegistries.MENU.getKey($$1));
                $$0 = true;
            }
        }
        return $$0;
    }

    static {
        register(MenuType.GENERIC_9x1, ContainerScreen::new);
        register(MenuType.GENERIC_9x2, ContainerScreen::new);
        register(MenuType.GENERIC_9x3, ContainerScreen::new);
        register(MenuType.GENERIC_9x4, ContainerScreen::new);
        register(MenuType.GENERIC_9x5, ContainerScreen::new);
        register(MenuType.GENERIC_9x6, ContainerScreen::new);
        register(MenuType.GENERIC_3x3, DispenserScreen::new);
        register(MenuType.ANVIL, AnvilScreen::new);
        register(MenuType.BEACON, BeaconScreen::new);
        register(MenuType.BLAST_FURNACE, BlastFurnaceScreen::new);
        register(MenuType.BREWING_STAND, BrewingStandScreen::new);
        register(MenuType.CRAFTING, CraftingScreen::new);
        register(MenuType.ENCHANTMENT, EnchantmentScreen::new);
        register(MenuType.FURNACE, FurnaceScreen::new);
        register(MenuType.GRINDSTONE, GrindstoneScreen::new);
        register(MenuType.HOPPER, HopperScreen::new);
        register(MenuType.LECTERN, LecternScreen::new);
        register(MenuType.LOOM, LoomScreen::new);
        register(MenuType.MERCHANT, MerchantScreen::new);
        register(MenuType.SHULKER_BOX, ShulkerBoxScreen::new);
        register(MenuType.SMITHING, SmithingScreen::new);
        register(MenuType.SMOKER, SmokerScreen::new);
        register(MenuType.CARTOGRAPHY_TABLE, CartographyTableScreen::new);
        register(MenuType.STONECUTTER, StonecutterScreen::new);
    }

    interface ScreenConstructor<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {

        default void fromPacket(Component component0, MenuType<T> menuTypeT1, Minecraft minecraft2, int int3) {
            U $$4 = this.create(menuTypeT1.create(int3, minecraft2.player.m_150109_()), minecraft2.player.m_150109_(), component0);
            minecraft2.player.f_36096_ = $$4.getMenu();
            minecraft2.setScreen($$4);
        }

        U create(T var1, Inventory var2, Component var3);
    }
}