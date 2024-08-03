package se.mickelus.tetra.gui;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiBar;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class HoneProgressGui extends GuiElement {

    protected GuiString labelString = new GuiStringSmall(0, 0, I18n.get("item.tetra.modular.hone_progress.label"));

    protected GuiString valueString;

    protected GuiBar bar;

    protected List<Component> tooltip;

    protected List<Component> extendedTooltip;

    public HoneProgressGui(int x, int y) {
        super(x, y, 45, 12);
        this.addChild(this.labelString);
        this.valueString = new GuiStringSmall(0, 0, "");
        this.valueString.setAttachment(GuiAttachment.topRight);
        this.addChild(this.valueString);
        this.bar = new GuiBar(0, 0, this.width, 0.0, 1.0);
        this.addChild(this.bar);
    }

    public void update(ItemStack itemStack, boolean isPlaceholder) {
        boolean shouldShow = !isPlaceholder && itemStack.getItem() instanceof IModularItem && ConfigHandler.moduleProgression.get() && ((IModularItem) itemStack.getItem()).canGainHoneProgress(itemStack);
        this.setVisible(shouldShow);
        if (shouldShow) {
            IModularItem item = (IModularItem) itemStack.getItem();
            int limit = item.getHoningLimit(itemStack);
            int progress = limit - item.getHoningProgress(itemStack);
            float factor = Mth.clamp(1.0F * (float) progress / (float) limit, 0.0F, 1.0F);
            float workableFactor = (float) (-item.getEffectLevel(itemStack, ItemEffect.workable));
            String factorString = String.format("%.0f%%", 100.0F * factor);
            String tooltipBase = I18n.get("item.tetra.modular.hone_progress.description", progress, limit, factorString, item.getHoneBase(itemStack), item.getHoningIntegrityPenalty(itemStack));
            if (workableFactor < 0.0F) {
                tooltipBase = tooltipBase + I18n.get("item.tetra.modular.hone_progress.description_workable", String.format("%.0f%%", workableFactor));
            }
            this.tooltip = ImmutableList.of(Component.translatable(tooltipBase), Component.literal(""), Tooltips.expand);
            this.extendedTooltip = ImmutableList.of(Component.translatable(tooltipBase), Component.literal(""), Tooltips.expanded, Component.literal(""), Component.translatable("item.tetra.modular.hone_progress.description_extended").withStyle(ChatFormatting.GRAY));
            this.valueString.setString(factorString);
            this.bar.setValue((double) factor, (double) factor);
            if (factor < 1.0F) {
                this.labelString.setColor(16777215);
                this.valueString.setColor(16777215);
                this.bar.setColor(16777215);
            } else {
                this.labelString.setColor(13553407);
                this.valueString.setColor(13553407);
                this.bar.setColor(13553407);
            }
        }
    }

    public void showAnimation() {
        if (this.isVisible()) {
            new KeyframeAnimation(100, this).withDelay(600).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX(-3.0F, 0.0F, true)).start();
        }
    }

    @Override
    public List<Component> getTooltipLines() {
        if (this.hasFocus()) {
            return Screen.hasShiftDown() ? this.extendedTooltip : this.tooltip;
        } else {
            return super.getTooltipLines();
        }
    }
}