package se.mickelus.tetra.blocks.workbench.gui;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ToolAction;

@ParametersAreNonnullByDefault
public class ToolRequirementGui extends GuiTool {

    String requirementTooltip;

    private int requiredLevel;

    private int availableLevel;

    private boolean showTooltip = true;

    private boolean showTooltipRequirement = true;

    public ToolRequirementGui(int x, int y, ToolAction toolAction) {
        this(x, y, toolAction, "tetra.tool." + toolAction.name() + ".requirement");
    }

    public ToolRequirementGui(int x, int y, ToolAction toolAction, String requirementTooltip) {
        super(x, y, toolAction);
        this.requirementTooltip = requirementTooltip;
    }

    public ToolRequirementGui setTooltipVisibility(boolean shouldShow) {
        this.showTooltip = shouldShow;
        return this;
    }

    public ToolRequirementGui setTooltipRequirementVisibility(boolean shouldShow) {
        this.showTooltipRequirement = shouldShow;
        return this;
    }

    public ToolRequirementGui updateRequirement(int requiredLevel, int availableLevel) {
        this.setVisible(requiredLevel != 0);
        this.requiredLevel = requiredLevel;
        this.availableLevel = availableLevel;
        if (this.isVisible()) {
            if (requiredLevel > availableLevel) {
                this.update(requiredLevel, 16755370);
            } else {
                this.update(requiredLevel, 11206570);
            }
        }
        return this;
    }

    @Override
    public List<Component> getTooltipLines() {
        if (!this.hasFocus() || !this.showTooltip) {
            return super.getTooltipLines();
        } else {
            return (List<Component>) (this.showTooltipRequirement ? ImmutableList.of(Component.translatable(this.requirementTooltip, this.requiredLevel), Component.literal(""), Component.translatable("tetra.tool.available", this.availableLevel).withStyle(this.requiredLevel > this.availableLevel ? ChatFormatting.RED : ChatFormatting.GREEN)) : Collections.singletonList(Component.translatable(this.requirementTooltip, this.requiredLevel)));
        }
    }
}