package io.github.lightman314.lightmanscurrency.client.gui.widget.taxes;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.taxes.ITaxCollector;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.rendering.Sprite;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.NonNullSupplier;

public class TaxInfoWidget extends EasyWidgetWithChildren {

    public static final Sprite SPRITE_IGNORE_TC = Sprite.SimpleSprite(IconAndButtonUtil.WIDGET_TEXTURE, 10, 0, 10, 10);

    public static final Sprite SPRITE_STOP_IGNORING_TC = Sprite.SimpleSprite(IconAndButtonUtil.WIDGET_TEXTURE, 0, 0, 10, 10);

    public static final int HEIGHT = 30;

    public static final int WIDTH = 176;

    private final Supplier<ITaxCollector> entrySource;

    private final ITaxInfoInteractable parent;

    public TaxInfoWidget(@Nonnull ScreenPosition pos, @Nonnull Supplier<ITaxCollector> entry, @Nonnull ITaxInfoInteractable parent) {
        this(pos.x, pos.y, entry, parent);
    }

    public TaxInfoWidget(int x, int y, @Nonnull Supplier<ITaxCollector> entry, @Nonnull ITaxInfoInteractable parent) {
        super(x, y, 176, 30);
        this.entrySource = entry;
        this.parent = parent;
    }

    public TaxInfoWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    protected void renderWidget(@Nonnull EasyGuiGraphics gui) {
        ITaxCollector entry = (ITaxCollector) this.entrySource.get();
        TraderData trader = this.parent.getTrader();
        if (entry != null && trader != null) {
            int statusColor = 4210752;
            if (trader.ShouldIgnoreTaxEntryOnly(entry)) {
                statusColor = 16711680;
            } else if (trader.ShouldIgnoreAllTaxes()) {
                statusColor = 16744192;
            } else if (entry.ShouldTax(trader)) {
                statusColor = 65280;
            }
            gui.drawString(entry.getName(), 0, 0, statusColor);
            gui.drawString(LCText.GUI_TAX_COLLECTOR_TAX_RATE.get(entry.getTaxRate()), this.parent.canPlayerForceIgnore() ? 12 : 0, 15, 4210752);
        }
    }

    @Override
    public void addChildren() {
        this.addChild(new EasyTextButton(this.getPosition().offset(80, 10), 60, 16, LCText.GUI_TAX_COLLECTOR_TAXABLE_ACCEPT_COLLECTOR.get(), this::AcceptTaxCollector).withAddons(EasyAddonHelper.visibleCheck(this::shouldAcceptBeVisible)));
        this.addChild(new PlainButton(this.getPosition().offset(0, 13), this::ToggleIgnoreState, this::getForceIgnoreSprite).withAddons(EasyAddonHelper.visibleCheck((NonNullSupplier<Boolean>) (() -> this.parent.canPlayerForceIgnore() && this.entrySource.get() != null)), EasyAddonHelper.tooltip(this::getForceIgnoreTooltip)));
    }

    private boolean shouldAcceptBeVisible() {
        ITaxCollector entry = (ITaxCollector) this.entrySource.get();
        TraderData trader = this.parent.getTrader();
        return entry != null && trader != null ? entry.IsInArea(trader) && !entry.ShouldTax(trader) : false;
    }

    private boolean isCurrentlyIgnored() {
        ITaxCollector entry = (ITaxCollector) this.entrySource.get();
        TraderData trader = this.parent.getTrader();
        return entry != null && trader != null ? trader.ShouldIgnoreTaxEntryOnly(entry) : false;
    }

    private Sprite getForceIgnoreSprite() {
        return this.isCurrentlyIgnored() ? SPRITE_STOP_IGNORING_TC : SPRITE_IGNORE_TC;
    }

    private Component getForceIgnoreTooltip() {
        return this.isCurrentlyIgnored() ? LCText.TOOLTIP_TAX_COLLECTOR_TAXABLE_PARDON_IGNORED.get() : LCText.TOOLTIP_TAX_COLLECTOR_TAXABLE_FORCE_IGNORE.get();
    }

    private void AcceptTaxCollector(EasyButton button) {
        ITaxCollector entry = (ITaxCollector) this.entrySource.get();
        if (entry != null) {
            this.parent.AcceptTaxCollector(entry.getID());
        }
    }

    private void ToggleIgnoreState(EasyButton button) {
        ITaxCollector entry = (ITaxCollector) this.entrySource.get();
        TraderData trader = this.parent.getTrader();
        if (entry != null && trader != null) {
            if (trader.ShouldIgnoreTaxEntryOnly(entry)) {
                this.parent.PardonIgnoredTaxCollector(entry.getID());
            } else {
                this.parent.ForceIgnoreTaxCollector(entry.getID());
            }
        }
    }
}