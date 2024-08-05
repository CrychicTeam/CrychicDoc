package io.github.lightman314.lightmanscurrency.api.traders.permissions;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.SettingsSubTab;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public abstract class PermissionOption {

    public final String permission;

    protected SettingsSubTab tab;

    protected PermissionOption(String permission) {
        this.permission = permission;
    }

    public MutableComponent widgetName() {
        return Component.translatable("permission.lightmanscurrency." + this.permission);
    }

    protected final boolean hasPermission() {
        return this.permissionValue() > 0;
    }

    protected final int permissionValue() {
        return this.tab.menu.getTrader() == null ? 0 : this.tab.menu.getTrader().getAllyPermissionLevel(this.permission);
    }

    public final void setValue(boolean newValue) {
        this.setValue(newValue ? 1 : 0);
    }

    public final void setValue(int newValue) {
        if (this.tab.menu.getTrader() != null) {
            this.tab.menu.getTrader().setAllyPermissionLevel(this.tab.menu.getPlayer(), this.permission, newValue);
            this.tab.sendMessage(LazyPacketData.builder().setString("ChangeAllyPermissions", this.permission).setInt("NewLevel", newValue));
        }
    }

    public final void initWidgets(SettingsSubTab tab, int x, int y, Consumer<Object> addWidgets) {
        this.tab = tab;
        this.createWidget(x, y, addWidgets);
    }

    protected abstract void createWidget(int var1, int var2, Consumer<Object> var3);

    public void tick() {
    }

    public void render(EasyGuiGraphics gui) {
    }

    public abstract int widgetWidth();
}