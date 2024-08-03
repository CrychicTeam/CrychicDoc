package noppes.npcs.client.gui.script;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;

public class GuiScriptGlobal extends GuiNPCInterface {

    private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/smallbg.png");

    public GuiScriptGlobal() {
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.drawDefaultBackground = false;
        this.title = "";
    }

    @Override
    public void init() {
        super.m_7856_();
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 38, this.guiTop + 20, 100, 20, "Players"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 38, this.guiTop + 50, 100, 20, "Forge"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        this.m_280273_(graphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.resource);
        graphics.blit(this.resource, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.setScreen(new GuiScriptPlayers());
        }
        if (guibutton.id == 1) {
            this.setScreen(new GuiScriptForge());
        }
    }

    @Override
    public void save() {
    }
}