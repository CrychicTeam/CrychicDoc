package io.github.lightman314.lightmanscurrency.client.gui.widget.notifications;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.List;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.NotNull;

public class NotificationDisplayWidget extends EasyWidget implements IScrollable, ITooltipWidget {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/notifications.png");

    public static final int HEIGHT_PER_ROW = 22;

    private final NonNullSupplier<List<Notification>> notificationSource;

    private final int rowCount;

    public boolean colorIfUnseen = false;

    public int backgroundColor = -3750202;

    Component tooltip = null;

    private int scroll = 0;

    public static int CalculateHeight(int rowCount) {
        return rowCount * 22;
    }

    private List<Notification> getNotifications() {
        return this.notificationSource.get();
    }

    public NotificationDisplayWidget(ScreenPosition pos, int width, int rowCount, NonNullSupplier<List<Notification>> notificationSource) {
        this(pos.x, pos.y, width, rowCount, notificationSource);
    }

    public NotificationDisplayWidget(int x, int y, int width, int rowCount, NonNullSupplier<List<Notification>> notificationSource) {
        super(x, y, width, CalculateHeight(rowCount));
        this.notificationSource = notificationSource;
        this.rowCount = rowCount;
    }

    public NotificationDisplayWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public void renderWidget(@NotNull EasyGuiGraphics gui) {
        this.validateScroll();
        this.tooltip = null;
        List<Notification> notifications = this.getNotifications();
        int index = this.scroll;
        gui.fill(0, 0, this.f_93618_, this.f_93619_, this.backgroundColor);
        for (int y = 0; y < this.rowCount && index < notifications.size(); y++) {
            int yPos = y * 22;
            Notification n = (Notification) notifications.get(index++);
            RenderSystem.setShaderTexture(0, GUI_TEXTURE);
            gui.resetColor();
            int vPos = n.wasSeen() && this.colorIfUnseen ? 222 : 200;
            gui.blit(GUI_TEXTURE, 0, yPos, 0, vPos, 2, 22);
            int xPos = 2;
            while (xPos < this.f_93618_ - 2) {
                int thisWidth = Math.min(166, this.f_93618_ - 2 - xPos);
                gui.blit(GUI_TEXTURE, xPos, yPos, 2, vPos, thisWidth, 22);
                xPos += thisWidth;
            }
            gui.blit(GUI_TEXTURE, this.f_93618_ - 2, yPos, 168, 200, 2, 22);
            int textXPos = 2;
            int textWidth = this.f_93618_ - 4;
            int textColor = n.wasSeen() ? 16777215 : 0;
            if (n.getCount() > 1) {
                String countText = String.valueOf(n.getCount());
                int quantityWidth = gui.font.width(countText);
                gui.blit(GUI_TEXTURE, 1 + quantityWidth, yPos, 170, vPos, 3, 22);
                gui.drawString(countText, textXPos, yPos + 11 - 9 / 2, textColor);
                textXPos += quantityWidth + 2;
                textWidth -= quantityWidth + 2;
            }
            Component message = n.getMessage();
            List<FormattedCharSequence> lines = gui.font.split(message, textWidth);
            if (lines.size() == 1) {
                gui.drawString((FormattedCharSequence) lines.get(0), textXPos, yPos + 11 - 9 / 2, textColor);
            } else {
                for (int l = 0; l < lines.size() && l < 2; l++) {
                    gui.drawString((FormattedCharSequence) lines.get(l), textXPos, yPos + 2 + l * 10, textColor);
                }
                if (this.tooltip == null && gui.mousePos.x >= this.m_252754_() && gui.mousePos.x < this.m_252754_() + this.f_93618_ && gui.mousePos.y >= yPos && gui.mousePos.y < yPos + 22) {
                    if (lines.size() > 2) {
                        if (n.hasTimeStamp()) {
                            this.tooltip = Component.empty().append(n.getTimeStampMessage()).append(Component.literal("\n")).append(message);
                        } else {
                            this.tooltip = message;
                        }
                    } else if (n.hasTimeStamp()) {
                        this.tooltip = n.getTimeStampMessage();
                    }
                }
            }
        }
    }

    @Override
    public List<Component> getTooltipText() {
        return this.tooltip != null ? ImmutableList.of(this.tooltip) : null;
    }

    @Override
    public int currentScroll() {
        return this.scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        this.scroll = newScroll;
        this.validateScroll();
    }

    @Override
    public int getMaxScroll() {
        return Math.max(0, this.getNotifications().size() - this.rowCount);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrator) {
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        this.handleScrollWheel(scroll);
        return true;
    }

    @Override
    public void playDownSound(@NotNull SoundManager manager) {
    }
}