package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.client.ToolActionIconStore;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.data.GlyphData;

@ParametersAreNonnullByDefault
public class GuiTool extends GuiElement {

    public static final int width = 16;

    private final GuiString levelIndicator;

    protected GuiElement iconContainer;

    protected ToolAction toolAction;

    private GlyphData fallback = new GlyphData(GuiTextures.toolActions, 240, 0);

    public GuiTool(int x, int y, ToolAction toolAction) {
        super(x, y, 16, 16);
        this.toolAction = toolAction;
        this.iconContainer = new GuiElement(0, 0, 16, 16);
        this.addChild(this.iconContainer);
        this.updateIcon();
        this.levelIndicator = new GuiStringOutline(10, 8, "");
        this.addChild(this.levelIndicator);
    }

    public void update(int level, int color) {
        this.levelIndicator.setVisible(level >= 0);
        this.levelIndicator.setString(level + "");
        this.levelIndicator.setColor(color);
        this.updateIcon();
    }

    protected void updateIcon() {
        this.iconContainer.clearChildren();
        GlyphData glyph = (GlyphData) Optional.ofNullable(ToolActionIconStore.instance.getIcon(this.toolAction)).orElse(this.fallback);
        this.iconContainer.addChild(new GuiTexture(0, 0, 16, 16, glyph.textureX, glyph.textureY, glyph.textureLocation));
    }

    public ToolAction getToolAction() {
        return this.toolAction;
    }

    protected int getOffset(ToolAction tool) {
        if (TetraToolActions.hammer.equals(tool)) {
            return 0;
        } else if (ToolActions.AXE_DIG.equals(tool)) {
            return 1;
        } else if (ToolActions.PICKAXE_DIG.equals(tool)) {
            return 2;
        } else if (ToolActions.SHOVEL_DIG.equals(tool)) {
            return 3;
        } else if (TetraToolActions.cut.equals(tool)) {
            return 4;
        } else if (TetraToolActions.pry.equals(tool)) {
            return 5;
        } else {
            return ToolActions.HOE_DIG.equals(tool) ? 6 : 14;
        }
    }
}