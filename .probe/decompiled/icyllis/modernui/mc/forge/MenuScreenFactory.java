package icyllis.modernui.mc.forge;

import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.mc.UIManager;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

@FunctionalInterface
public interface MenuScreenFactory<T extends AbstractContainerMenu> extends MenuScreens.ScreenConstructor<T, AbstractContainerScreen<T>> {

    static <T extends AbstractContainerMenu> MenuScreenFactory<T> create(MenuScreenFactory<T> factory) {
        return factory;
    }

    @Nonnull
    default AbstractContainerScreen<T> create(@Nonnull T menu, @Nonnull Inventory inventory, @Nonnull Component title) {
        return new MenuScreen<>(UIManager.getInstance(), (Fragment) Objects.requireNonNullElseGet(this.createFragment(menu), Fragment::new), this.createCallback(menu), menu, inventory, title);
    }

    @Nonnull
    Fragment createFragment(T var1);

    @Nullable
    default icyllis.modernui.mc.ScreenCallback createCallback(T menu) {
        return null;
    }
}