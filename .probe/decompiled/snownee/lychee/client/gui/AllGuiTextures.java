package snownee.lychee.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import snownee.lychee.util.Color;

public enum AllGuiTextures implements ScreenElement {

    JEI_SLOT("jei/widgets", 18, 18),
    JEI_CHANCE_SLOT("jei/widgets", 20, 156, 18, 18),
    JEI_CATALYST_SLOT("jei/widgets", 0, 156, 18, 18),
    JEI_ARROW("jei/widgets", 19, 10, 42, 10),
    JEI_LONG_ARROW("jei/widgets", 19, 0, 71, 10),
    JEI_DOWN_ARROW("jei/widgets", 0, 21, 18, 14),
    JEI_LIGHT("jei/widgets", 0, 42, 52, 11),
    JEI_QUESTION_MARK("jei/widgets", 0, 178, 12, 16),
    JEI_SHADOW("jei/widgets", 0, 56, 52, 11),
    BLOCKZAPPER_UPGRADE_RECIPE("jei/widgets", 0, 75, 144, 66),
    INFO("jei/widgets", 240, 0, 16, 16),
    LEFT_CLICK("jei/widgets", 192, 0, 16, 16),
    RIGHT_CLICK("jei/widgets", 224, 0, 16, 16);

    public final ResourceLocation location;

    public int width;

    public int height;

    public int startX;

    public int startY;

    private AllGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    private AllGuiTextures(int startX, int startY) {
        this("icons", startX * 16, startY * 16, 16, 16);
    }

    private AllGuiTextures(String location, int startX, int startY, int width, int height) {
        this("lychee", location, startX, startY, width, height);
    }

    private AllGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    public void bind() {
        RenderSystem.setShaderTexture(0, this.location);
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(this.location, x, y, this.startX, this.startY, this.width, this.height);
    }

    public void render(GuiGraphics graphics, int x, int y, Color c) {
        this.bind();
        UIRenderHelper.drawColoredTexture(graphics, c, x, y, this.startX, this.startY, this.width, this.height);
    }
}