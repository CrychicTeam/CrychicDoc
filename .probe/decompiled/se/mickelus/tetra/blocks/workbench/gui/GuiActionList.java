package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiAlignment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.blocks.workbench.action.WorkbenchAction;

@ParametersAreNonnullByDefault
public class GuiActionList extends GuiElement {

    private GuiActionButton[] actionButtons = new GuiActionButton[0];

    public GuiActionList(int x, int y) {
        super(x, y, 0, 0);
    }

    public void updateActions(ItemStack targetStack, WorkbenchAction[] actions, Player player, Consumer<WorkbenchAction> clickHandler, WorkbenchTile tile) {
        WorkbenchAction[] availableActions = (WorkbenchAction[]) Arrays.stream(actions).filter(action -> action.canPerformOn(player, tile, targetStack)).toArray(WorkbenchAction[]::new);
        this.actionButtons = new GuiActionButton[availableActions.length];
        this.clearChildren();
        int count = availableActions.length;
        this.setHeight(count * 2 + 20);
        for (int i = 0; i < count; i++) {
            GuiAlignment alignment = i % 2 == 0 ? GuiAlignment.left : GuiAlignment.right;
            this.actionButtons[i] = new GuiActionButton(count > 1 ? -9 : -20, i * 14, actions[i], targetStack, alignment, clickHandler);
            this.actionButtons[i].setAttachmentPoint(alignment.toAttachment());
            if (GuiAlignment.right.equals(alignment)) {
                this.actionButtons[i].setX(-this.actionButtons[i].getX());
            }
            this.addChild(this.actionButtons[i]);
        }
    }

    public void updateTools(Map<ToolAction, Integer> availableTools) {
        Arrays.stream(this.actionButtons).forEach(button -> button.update(availableTools));
    }

    public void showAnimation() {
        if (this.isVisible()) {
            for (int i = 0; i < this.getNumChildren(); i++) {
                new KeyframeAnimation(100, this.getChild(i)).withDelay(i * 100 + 300).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX(i % 2 == 0 ? -2.0F : 2.0F, 0.0F, true)).start();
            }
        }
    }
}