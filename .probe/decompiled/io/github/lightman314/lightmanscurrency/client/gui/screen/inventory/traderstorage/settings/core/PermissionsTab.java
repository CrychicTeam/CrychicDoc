package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.core;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.permissions.PermissionOption;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.SettingsSubTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.settings.TraderSettingsClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;

public class PermissionsTab extends SettingsSubTab {

    List<PermissionOption> options;

    public PermissionsTab(@Nonnull TraderSettingsClientTab parent) {
        super(parent);
    }

    protected int startHeight() {
        return 5;
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.of(Items.BOOKSHELF);
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_TRADER_SETTINGS_ALLY_PERMS.get();
    }

    @Override
    public boolean canOpen() {
        return this.menu.hasPermission("editPermissions");
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.options = new ArrayList();
        TraderData trader = this.menu.getTrader();
        if (trader != null) {
            this.options.addAll(trader.getPermissionOptions());
        }
        int startHeight = screenArea.y + this.startHeight();
        for (int i = 0; i < this.options.size(); i++) {
            int xPos = this.getXPos(i) + screenArea.x;
            int yPos = this.getYPosOffset(i) + startHeight;
            PermissionOption option = (PermissionOption) this.options.get(i);
            option.initWidgets(this, xPos, yPos, this::addChild);
        }
    }

    private int getYPosOffset(int index) {
        int yIndex = index / 2;
        return 18 * yIndex;
    }

    private int getXPos(int index) {
        return index % 2 == 0 ? 5 : 105;
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        int startHeight = this.startHeight();
        for (int i = 0; i < this.options.size(); i++) {
            PermissionOption option = (PermissionOption) this.options.get(i);
            int xPos = this.getXPos(i) + option.widgetWidth();
            int yPos = this.getYPosOffset(i) + startHeight;
            int textWidth = 90 - option.widgetWidth();
            int textHeight = gui.font.wordWrapHeight(option.widgetName().getString(), textWidth);
            int yStart = (20 - textHeight) / 2 + yPos;
            gui.drawWordWrap(option.widgetName(), xPos, yStart, textWidth, 16777215);
        }
    }

    @Override
    public void tick() {
        for (PermissionOption option : this.options) {
            option.tick();
        }
    }

    @Override
    public boolean shouldRenderInventoryText() {
        return this.options.size() < 15;
    }
}