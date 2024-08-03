package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiAlignment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.blocks.workbench.action.WorkbenchAction;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class GuiActionButton extends GuiElement {

    private final WorkbenchAction action;

    private final ItemStack targetStack;

    private final ToolRequirementGui toolIndicator;

    private final GuiClickable iconClickable;

    private final GuiClickable labelClickable;

    private final GuiTexture borderLeft;

    private final GuiTexture borderRight;

    private final GuiRect borderTop;

    private final GuiRect borderBottom;

    public GuiActionButton(int x, int y, WorkbenchAction action, ItemStack targetStack, Consumer<WorkbenchAction> clickHandler) {
        this(x, y, action, targetStack, GuiAlignment.left, clickHandler);
    }

    public GuiActionButton(int x, int y, WorkbenchAction action, ItemStack targetStack, GuiAlignment alignment, Consumer<WorkbenchAction> clickHandler) {
        super(x, y, 0, 11);
        this.action = action;
        this.targetStack = targetStack;
        String label = I18n.get(String.format("%s.%s.label", "tetra", action.getKey()));
        this.width = Minecraft.getInstance().font.width(label) + 42;
        this.labelClickable = new GuiClickable(0, 0, this.width, this.height, () -> clickHandler.accept(action)) {

            @Override
            protected void onFocus() {
                GuiActionButton.this.setBorderColors(9408367);
            }

            @Override
            protected void onBlur() {
                if (!GuiActionButton.this.iconClickable.hasFocus()) {
                    GuiActionButton.this.setBorderColors(8355711);
                }
            }
        };
        this.labelClickable.addChild(new GuiRect(9, 0, this.width - 18, 11, 0));
        this.borderLeft = new GuiTexture(0, 0, 9, 11, 79, 0, GuiTextures.workbench).setColor(8355711);
        this.labelClickable.addChild(this.borderLeft);
        this.borderRight = new GuiTexture(this.width - 9, 0, 9, 11, 88, 0, GuiTextures.workbench).setColor(8355711);
        this.labelClickable.addChild(this.borderRight);
        this.borderTop = new GuiRect(9, 1, this.width - 18, 1, 8355711);
        this.labelClickable.addChild(this.borderTop);
        this.borderBottom = new GuiRect(9, 9, this.width - 18, 1, 8355711);
        this.labelClickable.addChild(this.borderBottom);
        GuiString labelString = new GuiStringOutline(7, 1, label);
        labelString.setAttachment(alignment.flip().toAttachment());
        if (GuiAlignment.left.equals(alignment)) {
            labelString.setX(-labelString.getX());
        }
        this.labelClickable.addChild(labelString);
        this.addChild(this.labelClickable);
        this.iconClickable = new GuiClickable(6, -9, 29, 29, () -> clickHandler.accept(action)) {

            @Override
            protected void onFocus() {
                GuiActionButton.this.setBorderColors(9408367);
            }

            @Override
            protected void onBlur() {
                if (!GuiActionButton.this.labelClickable.hasFocus()) {
                    GuiActionButton.this.setBorderColors(8355711);
                }
            }
        };
        this.iconClickable.setAttachment(alignment.toAttachment());
        if (GuiAlignment.right.equals(alignment)) {
            this.iconClickable.setX(-this.iconClickable.getX());
        }
        this.iconClickable.addChild(new GuiTexture(0, 0, 29, 29, 97, 0, GuiTextures.workbench));
        this.addChild(this.iconClickable);
        ToolAction requiredTool = (ToolAction) action.getRequiredToolActions(targetStack).stream().findFirst().orElse(TetraToolActions.hammer);
        this.toolIndicator = new ToolRequirementGui(6, 7, requiredTool);
        this.iconClickable.addChild(this.toolIndicator);
    }

    private void setBorderColors(int color) {
        this.borderLeft.setColor(color);
        this.borderRight.setColor(color);
        this.borderTop.setColor(color);
        this.borderBottom.setColor(color);
    }

    public void update(Map<ToolAction, Integer> availableTools) {
        Map<ToolAction, Integer> requiredTools = this.action.getRequiredTools(this.targetStack);
        if (!requiredTools.isEmpty()) {
            this.toolIndicator.setTooltipVisibility(true);
            requiredTools.entrySet().stream().findFirst().ifPresent(entry -> this.toolIndicator.updateRequirement((Integer) entry.getValue(), (Integer) availableTools.getOrDefault(entry.getKey(), 0)));
        } else {
            this.toolIndicator.setTooltipVisibility(false);
        }
    }
}