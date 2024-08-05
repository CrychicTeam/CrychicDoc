package se.mickelus.tetra.gui;

import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.gui.stats.bar.GuiBar;
import se.mickelus.tetra.module.ItemModuleMajor;

@ParametersAreNonnullByDefault
public class GuiSettleProgress extends GuiElement {

    protected GuiString labelString = new GuiStringSmall(0, 0, I18n.get("item.tetra.modular.settle_progress.label"));

    protected GuiString valueString;

    protected GuiBar bar;

    protected List<Component> tooltip;

    public GuiSettleProgress(int x, int y, int barLength) {
        super(x, y, barLength, 12);
        this.addChild(this.labelString);
        this.valueString = new GuiStringSmall(0, 0, "");
        this.valueString.setAttachment(GuiAttachment.topRight);
        this.addChild(this.valueString);
        this.bar = new GuiBar(0, 0, barLength, 0.0, 1.0);
        this.addChild(this.bar);
    }

    public void update(ItemStack itemStack, ItemModuleMajor module) {
        int value = module.getSettleProgress(itemStack);
        int limit = module.getSettleLimit(itemStack);
        float progress = (1.0F * (float) limit - (float) value) / (float) limit;
        int settleMaxCount = module.getSettleMaxCount(itemStack);
        boolean fullySettled = settleMaxCount <= module.getImprovementLevel(itemStack, "settled");
        boolean isArrested = module.getImprovementLevel(itemStack, "arrested") != -1;
        boolean isGain = module.getIntegrityGain(itemStack) > 0;
        if (isArrested) {
            this.labelString.setString(ChatFormatting.RED + I18n.get("tetra.improvement.arrested.name"));
            this.labelString.setAttachment(GuiAttachment.topCenter);
            this.tooltip = Collections.singletonList(Component.translatable("tetra.improvement.arrested.description"));
            this.valueString.setString("");
            this.bar.setValue(0.0, 0.0);
        } else if (fullySettled) {
            this.labelString.setString(ChatFormatting.GREEN + I18n.get("item.tetra.modular.settle_full.label"));
            this.labelString.setAttachment(GuiAttachment.topCenter);
            if (isGain) {
                this.tooltip = Collections.singletonList(Component.translatable("item.tetra.modular.settle_full_gain.description"));
            } else {
                this.tooltip = Collections.singletonList(Component.translatable("item.tetra.modular.settle_full_cost.description"));
            }
            this.valueString.setString("");
            this.bar.setValue(1.0, 1.0);
        } else if (settleMaxCount == 0) {
            this.labelString.setString(I18n.get("item.tetra.modular.settle_full_null.label"));
            this.labelString.setAttachment(GuiAttachment.topCenter);
            this.tooltip = Collections.singletonList(Component.translatable("item.tetra.modular.settle_full_null.description"));
            this.valueString.setString("");
            this.bar.setValue(1.0, 1.0);
        } else {
            double durabilityPenalty = Math.max((double) module.getImprovementLevel(itemStack, "settled") * ConfigHandler.settleLimitLevelMultiplier.get(), 1.0) * (double) module.getDurability(itemStack) * ConfigHandler.settleLimitDurabilityMultiplier.get();
            this.labelString.setString(I18n.get("item.tetra.modular.settle_progress.label"));
            this.labelString.setAttachment(GuiAttachment.topLeft);
            this.tooltip = Collections.singletonList(Component.translatable(isGain ? "item.tetra.modular.settle_progress_gain.description" : "item.tetra.modular.settle_progress_cost.description", limit - value, limit, ConfigHandler.settleLimitBase.get(), (int) durabilityPenalty));
            this.valueString.setString(String.format("%.0f%%", 100.0F * progress));
            this.bar.setValue((double) progress, (double) progress);
        }
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : super.getTooltipLines();
    }
}