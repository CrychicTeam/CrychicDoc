package io.github.lightman314.lightmanscurrency.client.gui.widget.button;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.misc.player.OwnerData;
import io.github.lightman314.lightmanscurrency.api.ownership.listing.PotentialOwner;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class OwnerSelectButton extends EasyButton implements ITooltipWidget {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/buttons.png");

    private final Supplier<OwnerData> currentOwner;

    private final Supplier<PotentialOwner> ownerSupplier;

    private final Supplier<Boolean> parentVisible;

    public static final int HEIGHT = 20;

    public PotentialOwner getOwner() {
        return (PotentialOwner) this.ownerSupplier.get();
    }

    public OwnerSelectButton(ScreenPosition pos, int width, @Nonnull Runnable press, @Nonnull Supplier<OwnerData> currentOwner, @Nonnull Supplier<PotentialOwner> ownerSupplier, @Nonnull Supplier<Boolean> parentVisible) {
        super(ScreenArea.of(pos, width, 20), press);
        this.currentOwner = currentOwner;
        this.ownerSupplier = ownerSupplier;
        this.parentVisible = parentVisible;
    }

    public OwnerSelectButton withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    protected void renderTick() {
        PotentialOwner owner = this.getOwner();
        this.setVisible((Boolean) this.parentVisible.get() && owner != null);
        if (this.f_93624_) {
            OwnerData data = (OwnerData) this.currentOwner.get();
            if (data != null) {
                this.setActive(!data.getValidOwner().matches(owner.asOwner()));
            }
        }
    }

    @Override
    protected void renderWidget(@Nonnull EasyGuiGraphics gui) {
        PotentialOwner owner = this.getOwner();
        if (owner == null) {
            this.setVisible(false);
        } else {
            float color = this.m_142518_() ? 1.0F : 0.5F;
            gui.setColor(color, color, color);
            gui.blitBackgroundOfSize(GUI_TEXTURE, 0, 0, this.f_93618_, this.f_93619_, 0, 0, 256, 20, 2);
            IconData icon = owner.getIcon();
            if (icon != null) {
                icon.render(gui, 2, 2);
            }
            Component name = TextRenderUtil.fitString(owner.getName(), this.f_93618_ - 22);
            int textColor = this.m_142518_() ? 16777215 : 4177855;
            gui.drawShadowed(name, 22, 6, textColor);
            gui.resetColor();
        }
    }

    @Override
    public List<Component> getTooltipText() {
        List<Component> tooltip = new ArrayList();
        PotentialOwner owner = this.getOwner();
        if (owner != null) {
            owner.appendTooltip(tooltip);
        }
        return tooltip;
    }
}