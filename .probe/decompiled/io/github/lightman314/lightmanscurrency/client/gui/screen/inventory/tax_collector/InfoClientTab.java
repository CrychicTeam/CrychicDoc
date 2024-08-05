package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.tax_collector;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.taxes.ITaxable;
import io.github.lightman314.lightmanscurrency.api.taxes.reference.TaxableReference;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.tax_collector.TaxCollectorClientTab;
import io.github.lightman314.lightmanscurrency.common.menus.tax_collector.tabs.InfoTab;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxEntry;
import io.github.lightman314.lightmanscurrency.common.taxes.data.TaxStats;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class InfoClientTab extends TaxCollectorClientTab<InfoTab> {

    public InfoClientTab(Object screen, InfoTab commonTab) {
        super(screen, commonTab);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_TRADER;
    }

    @Nullable
    @Override
    public Component getTooltip() {
        return LCText.TOOLTIP_TAX_COLLECTOR_INFO.get();
    }

    @Override
    protected void initialize(ScreenArea screenArea, boolean firstOpen) {
        Component clearLabel = LCText.BUTTON_TAX_COLLECTOR_STATS_CLEAR.get();
        int buttonWidth = this.getFont().width(clearLabel) + 6;
        this.addChild(new EasyTextButton(screenArea.pos.offset(screenArea.width - buttonWidth - 8, 15), buttonWidth, 12, clearLabel, this.commonTab::ClearInfoCache).withAddons(EasyAddonHelper.visibleCheck(this::canClearStats)));
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.drawString(this.getTooltip(), 8, 6, 4210752);
        TaxEntry entry = this.getEntry();
        if (entry != null) {
            TaxStats stats = entry.stats;
            gui.drawString(LCText.GUI_TAX_COLLECTOR_STATS_TOTAL_COLLECTED.get(), 10, 35, 4210752);
            gui.drawString(stats.getTotalCollected().getRandomValueText(), 10, 45, 4210752);
            gui.drawString(LCText.GUI_TAX_COLLECTOR_STATS_UNIQUE_TAXABLES.get(stats.getUniqueTaxableCount()), 10, 65, 4210752);
            TaxableReference mostTaxed = stats.getMostTaxed();
            gui.drawString(LCText.GUI_TAX_COLLECTOR_STATS_MOST_TAXED_LABEL.get(), 10, 85, 4210752);
            if (mostTaxed != null) {
                ITaxable taxable = mostTaxed.getTaxable(true);
                if (taxable != null) {
                    gui.drawString(taxable.getName(), 10, 95, 4210752);
                    gui.drawString(LCText.GUI_TAX_COLLECTOR_STATS_MOST_TAXED_FORMAT.get(stats.getMostTaxedCount()), 10, 104, 4210752);
                }
            }
        }
    }

    private boolean canClearStats() {
        return this.commonTab.CanClearCache(this.getEntry());
    }
}