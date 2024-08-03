package dev.architectury.registry.menu;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.menu.forge.MenuRegistryImpl;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public final class MenuRegistry {

    private MenuRegistry() {
    }

    public static void openExtendedMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> bufWriter) {
        openExtendedMenu(player, new ExtendedMenuProvider() {

            @Override
            public void saveExtraData(FriendlyByteBuf buf) {
                bufWriter.accept(buf);
            }

            @Override
            public Component getDisplayName() {
                return provider.getDisplayName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return provider.m_7208_(i, inventory, player);
            }
        });
    }

    @ExpectPlatform
    @Transformed
    public static void openExtendedMenu(ServerPlayer player, ExtendedMenuProvider provider) {
        MenuRegistryImpl.openExtendedMenu(player, provider);
    }

    public static void openMenu(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    @Deprecated(forRemoval = true)
    @ExpectPlatform
    @Transformed
    public static <T extends AbstractContainerMenu> MenuType<T> of(MenuRegistry.SimpleMenuTypeFactory<T> factory) {
        return MenuRegistryImpl.of(factory);
    }

    @ExpectPlatform
    @Transformed
    public static <T extends AbstractContainerMenu> MenuType<T> ofExtended(MenuRegistry.ExtendedMenuTypeFactory<T> factory) {
        return MenuRegistryImpl.ofExtended(factory);
    }

    @OnlyIn(Dist.CLIENT)
    @ExpectPlatform
    @Transformed
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreenFactory(MenuType<? extends H> type, MenuRegistry.ScreenFactory<H, S> factory) {
        MenuRegistryImpl.registerScreenFactory(type, factory);
    }

    @FunctionalInterface
    public interface ExtendedMenuTypeFactory<T extends AbstractContainerMenu> {

        T create(int var1, Inventory var2, FriendlyByteBuf var3);
    }

    @FunctionalInterface
    @OnlyIn(Dist.CLIENT)
    public interface ScreenFactory<H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> {

        S create(H var1, Inventory var2, Component var3);
    }

    @Deprecated(forRemoval = true)
    @FunctionalInterface
    public interface SimpleMenuTypeFactory<T extends AbstractContainerMenu> {

        T create(int var1, Inventory var2);
    }
}