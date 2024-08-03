package noppes.npcs.client.gui.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class GuiContainerNPCInterface2<T extends AbstractContainerMenu> extends GuiContainerNPCInterface<T> {

    private ResourceLocation background = new ResourceLocation("customnpcs", "textures/gui/menubg.png");

    private final ResourceLocation defaultBackground = new ResourceLocation("customnpcs", "textures/gui/menubg.png");

    private GuiNpcMenu menu;

    public int menuYOffset = 0;

    public GuiContainerNPCInterface2(EntityNPCInterface npc, T cont, Inventory inv, Component titleIn) {
        this(npc, cont, inv, titleIn, -1);
    }

    public GuiContainerNPCInterface2(EntityNPCInterface npc, T cont, Inventory inv, Component titleIn, int activeMenu) {
        super(npc, cont, inv, titleIn);
        this.f_97726_ = 420;
        this.menu = new GuiNpcMenu(this, activeMenu, npc);
        this.title = "";
    }

    public void setBackground(String texture) {
        this.background = new ResourceLocation("customnpcs", "textures/gui/" + texture);
    }

    @Override
    public ResourceLocation getResource(String texture) {
        return new ResourceLocation("customnpcs", "textures/gui/" + texture);
    }

    @Override
    public void init() {
        super.m_7856_();
        this.menu.initGui(this.guiLeft, this.guiTop + this.menuYOffset, this.f_97726_);
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        if (!this.hasSubGui()) {
            this.menu.mouseClicked(i, j, k);
        }
        return super.m_6375_(i, j, k);
    }

    public void delete() {
        this.npc.delete();
        this.setScreen(null);
        this.f_96541_.mouseHandler.grabMouse();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        PoseStack matrixStack = graphics.pose();
        this.m_280273_(graphics);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.background);
        graphics.blit(this.background, this.guiLeft, this.guiTop, 0, 0, 256, 256);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.defaultBackground);
        graphics.blit(this.defaultBackground, this.guiLeft + this.f_97726_ - 200, this.guiTop, 26, 0, 200, 220);
        this.menu.drawElements(graphics, this.f_96547_, x, y, this.f_96541_, partialTicks);
        super.m_7286_(graphics, partialTicks, x, y);
    }
}