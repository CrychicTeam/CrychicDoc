package se.mickelus.tetra.blocks.salvage;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.blocks.workbench.gui.GuiTool;
import se.mickelus.tetra.properties.PropertyHelper;

@ParametersAreNonnullByDefault
public class InteractiveToolGui extends GuiElement {

    private final GuiTool toolIcon;

    private final KeyframeAnimation hide;

    private final ToolAction toolAction;

    private final int toolLevel;

    private final Player player;

    private KeyframeAnimation show;

    private int currentSlot;

    public InteractiveToolGui(int x, int y, ToolAction toolAction, int toolLevel, Player player) {
        super(x, y, 16, 16);
        this.opacity = 0.0F;
        this.toolAction = toolAction;
        this.toolLevel = toolLevel;
        this.player = player;
        this.toolIcon = new GuiTool(-1, 0, toolAction);
        this.addChild(this.toolIcon);
        this.show = new KeyframeAnimation(100, this).applyTo(new Applier.Opacity(0.0F, 1.0F)).withDelay(650);
        this.hide = new KeyframeAnimation(100, this).applyTo(new Applier.Opacity(1.0F, 0.0F));
        this.updateTint();
        this.currentSlot = player.getInventory().selected;
    }

    public void updateFadeTime() {
        this.show = this.show.withDelay(0);
    }

    private void updateTint() {
        int mainHandLevel = PropertyHelper.getItemToolLevel(this.player.m_21205_(), this.toolAction);
        int offHandLevel = PropertyHelper.getItemToolLevel(this.player.m_21206_(), this.toolAction);
        if (mainHandLevel < this.toolLevel && offHandLevel < this.toolLevel) {
            if (PropertyHelper.getPlayerToolLevel(this.player, this.toolAction) >= this.toolLevel) {
                this.toolIcon.update(this.toolLevel, 16776960);
            } else {
                this.toolIcon.update(this.toolLevel, 16733525);
            }
        } else {
            this.toolIcon.update(this.toolLevel, 16777215);
        }
    }

    public void show() {
        this.updateTint();
        this.hide.stop();
        this.show.start();
    }

    public void hide() {
        this.show.stop();
        this.hide.start();
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (this.player.getInventory().selected != this.currentSlot) {
            this.updateTint();
            this.currentSlot = this.player.getInventory().selected;
        }
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }
}