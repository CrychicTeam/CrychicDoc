package dev.architectury.registry.menu.forge;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkHooks;

public class MenuRegistryImpl {

    public static void openExtendedMenu(ServerPlayer player, ExtendedMenuProvider provider) {
        NetworkHooks.openScreen(player, provider, provider::saveExtraData);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> of(MenuRegistry.SimpleMenuTypeFactory<T> factory) {
        return new MenuType<>(factory::create, FeatureFlags.VANILLA_SET);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> ofExtended(MenuRegistry.ExtendedMenuTypeFactory<T> factory) {
        return IForgeMenuType.create(factory::create);
    }

    @OnlyIn(Dist.CLIENT)
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreenFactory(MenuType<? extends H> type, MenuRegistry.ScreenFactory<H, S> factory) {
        MenuScreens.register(type, factory::create);
    }
}