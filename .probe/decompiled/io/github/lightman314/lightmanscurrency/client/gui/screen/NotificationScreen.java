package io.github.lightman314.lightmanscurrency.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationCategory;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationData;
import io.github.lightman314.lightmanscurrency.client.data.ClientNotificationData;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyScreen;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.notifications.MarkAsSeenButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.notifications.NotificationTabButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.TabButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.ScrollBarWidget;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.network.message.notifications.CPacketFlagNotificationsSeen;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.common.util.NonNullSupplier;

public class NotificationScreen extends EasyScreen implements IScrollable {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/notifications.png");

    private static final int SCREEN_WIDTH = 200;

    private static final int SCREEN_HEIGHT = 200;

    public final int TABS_PER_PAGE = 8;

    public final int NOTIFICATIONS_PER_PAGE = 8;

    public final int NOTIFICATION_HEIGHT = 22;

    List<NotificationTabButton> tabButtons;

    int tabScroll = 0;

    NotificationCategory selectedCategory = NotificationCategory.GENERAL;

    ScrollBarWidget notificationScroller = null;

    EasyButton buttonMarkAsSeen;

    int notificationScroll = 0;

    private Component cachedTooltip = null;

    public final NotificationData getNotifications() {
        return ClientNotificationData.GetNotifications();
    }

    public NotificationScreen() {
        this.resize(225, 200);
    }

    public List<NotificationCategory> getCategories() {
        List<NotificationCategory> categories = Lists.newArrayList(new NotificationCategory[] { NotificationCategory.GENERAL });
        categories.addAll(this.getNotifications().getCategories().stream().filter(cat -> cat != NotificationCategory.GENERAL).toList());
        return categories;
    }

    public void reinit() {
        this.m_169413_();
        this.validateSelectedCategory();
        this.m_7856_();
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        this.tabButtons = new ArrayList();
        for (NotificationCategory cat : this.getCategories()) {
            this.tabButtons.add(this.addChild(new NotificationTabButton(this::SelectTab, this::getNotifications, cat)));
        }
        this.positionTabButtons();
        this.notificationScroller = this.addChild(new ScrollBarWidget(screenArea.pos.offset(screenArea.width - 15, 15), 8 * 22, this));
        this.buttonMarkAsSeen = this.addChild(new MarkAsSeenButton(screenArea.x + screenArea.width - 15, screenArea.y + 4, LCText.BUTTON_NOTIFICATIONS_MARK_AS_READ.get(), this::markAsRead).withAddons(new WidgetAddon[] { EasyAddonHelper.activeCheck((NonNullSupplier<Boolean>) (() -> this.getNotifications().unseenNotification(this.selectedCategory))) }));
    }

    private void validateSelectedCategory() {
        List<NotificationCategory> categories = this.getCategories();
        boolean categoryFound = false;
        for (int i = 0; i < categories.size() && !categoryFound; i++) {
            if (((NotificationCategory) categories.get(i)).matches(this.selectedCategory)) {
                categoryFound = true;
            }
        }
        if (!categoryFound || this.selectedCategory == null) {
            this.selectedCategory = NotificationCategory.GENERAL;
        }
    }

    private void positionTabButtons() {
        this.tabScroll = Math.min(this.tabScroll, this.getMaxTabScroll());
        int startIndex = this.tabScroll;
        ScreenPosition pos = this.getCorner();
        List<NotificationCategory> categories = this.getCategories();
        for (int i = 0; i < this.tabButtons.size(); i++) {
            TabButton tab = (TabButton) this.tabButtons.get(i);
            if (i >= startIndex && i < startIndex + 8) {
                tab.f_93624_ = true;
                tab.reposition(pos, 3);
                if (i < categories.size()) {
                    tab.f_93623_ = !((NotificationCategory) categories.get(i)).matches(this.selectedCategory);
                } else {
                    tab.f_93623_ = true;
                }
                pos = pos.offset(0, 25);
            } else {
                tab.f_93624_ = false;
            }
        }
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        int screenLeft = 25;
        gui.resetColor();
        gui.blit(GUI_TEXTURE, screenLeft, 0, 0, 0, 200, this.getYSize());
        this.notificationScroll = Math.min(this.notificationScroll, this.getMaxNotificationScroll());
        List<Notification> notifications = this.getNotifications().getNotifications(this.selectedCategory);
        this.cachedTooltip = null;
        int index = this.notificationScroll;
        for (int y = 0; y < 8 && index < notifications.size(); y++) {
            Notification not = (Notification) notifications.get(index++);
            int yPos = 15 + y * 22;
            gui.resetColor();
            int vPos = not.wasSeen() ? this.getYSize() : this.getYSize() + 22;
            int textColor = not.wasSeen() ? 16777215 : 0;
            gui.blit(GUI_TEXTURE, screenLeft + 15, yPos, 0, vPos, 170, 22);
            int textXPos = screenLeft + 17;
            int textWidth = 166;
            if (not.getCount() > 1) {
                String countText = String.valueOf(not.getCount());
                int quantityWidth = this.f_96547_.width(countText);
                gui.blit(GUI_TEXTURE, screenLeft + 16 + quantityWidth, yPos, 170, vPos, 3, 22);
                gui.drawString(countText, textXPos, yPos + 11 - 9 / 2, textColor);
                textXPos += quantityWidth + 2;
                textWidth -= quantityWidth + 2;
            }
            Component message = this.selectedCategory == NotificationCategory.GENERAL ? not.getGeneralMessage() : not.getMessage();
            List<FormattedCharSequence> lines = this.f_96547_.split(message, textWidth);
            if (lines.size() == 1) {
                gui.drawString((FormattedCharSequence) lines.get(0), textXPos, yPos + 11 - 9 / 2, textColor);
            } else {
                for (int l = 0; l < lines.size() && l < 2; l++) {
                    gui.drawString((FormattedCharSequence) lines.get(l), textXPos, yPos + 2 + l * 10, textColor);
                }
                if (this.cachedTooltip == null && this.getCorner().offset(screenLeft + 15, yPos).isMouseInArea(gui.mousePos, 170, 22)) {
                    if (lines.size() > 2) {
                        if (not.hasTimeStamp()) {
                            this.cachedTooltip = EasyText.empty().append(not.getTimeStampMessage()).append(EasyText.literal("\n")).append(message);
                        } else {
                            this.cachedTooltip = message;
                        }
                    } else if (not.hasTimeStamp()) {
                        this.cachedTooltip = not.getTimeStampMessage();
                    }
                }
            }
        }
    }

    @Override
    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        if (this.cachedTooltip != null) {
            gui.renderTooltip(gui.font.split(this.cachedTooltip, 170));
        }
    }

    private void SelectTab(EasyButton button) {
        int tabIndex = -1;
        if (button instanceof NotificationTabButton) {
            tabIndex = this.tabButtons.indexOf(button);
        }
        if (tabIndex >= 0) {
            List<NotificationCategory> categories = this.getCategories();
            if (tabIndex < categories.size()) {
                NotificationCategory newCategory = (NotificationCategory) categories.get(tabIndex);
                if (!newCategory.matches(this.selectedCategory)) {
                    this.selectedCategory = newCategory;
                    this.notificationScroll = 0;
                    this.positionTabButtons();
                }
            }
        }
    }

    public int getMaxTabScroll() {
        return Math.max(0, this.tabButtons.size() - 8);
    }

    public boolean tabScrolled(double delta) {
        if (delta < 0.0) {
            if (this.tabScroll >= this.getMaxTabScroll()) {
                return false;
            }
            this.tabScroll++;
            this.positionTabButtons();
        } else if (delta > 0.0) {
            if (this.tabScroll <= 0) {
                return false;
            }
            this.tabScroll--;
            this.positionTabButtons();
        }
        return true;
    }

    public int getMaxNotificationScroll() {
        return Math.max(0, this.getNotifications().getNotifications(this.selectedCategory).size() - 8);
    }

    public boolean notificationScrolled(double delta) {
        if (delta < 0.0) {
            if (this.notificationScroll >= this.getMaxNotificationScroll()) {
                return false;
            }
            this.notificationScroll++;
        } else if (delta > 0.0) {
            if (this.notificationScroll <= 0) {
                return false;
            }
            this.notificationScroll--;
        }
        return true;
    }

    public void markAsRead(EasyButton button) {
        new CPacketFlagNotificationsSeen(this.selectedCategory).send();
    }

    @Override
    public int currentScroll() {
        return this.notificationScroll;
    }

    @Override
    public void setScroll(int newScroll) {
        this.notificationScroll = newScroll;
    }

    @Override
    public int getMaxScroll() {
        return this.getMaxNotificationScroll();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.getCorner().offset(25, 0).isMouseInArea(mouseX, mouseY, this.getXSize() - 25, this.getYSize())) {
            return this.notificationScrolled(delta) ? true : super.mouseScrolled(mouseX, mouseY, delta);
        } else {
            return this.tabScrolled(delta) ? true : super.mouseScrolled(mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int mods) {
        InputConstants.Key mouseKey = InputConstants.getKey(key, scanCode);
        if (this.f_96541_.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.f_96541_.setScreen(null);
            return true;
        } else {
            return super.keyPressed(key, scanCode, mods);
        }
    }
}