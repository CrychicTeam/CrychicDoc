package noppes.npcs.client.gui.player;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerCarpentryBench;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;

public class GuiNpcCarpentryBench extends GuiContainerNPCInterface<ContainerCarpentryBench> {

    private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/carpentry.png");

    private ContainerCarpentryBench container;

    private GuiButtonNop button;

    public GuiNpcCarpentryBench(ContainerCarpentryBench container, Inventory inv, Component titleIn) {
        super(null, container, inv, titleIn);
        this.container = container;
        this.title = "";
        this.f_97727_ = 180;
    }

    @Override
    public void init() {
        super.m_7856_();
        this.addButton(this.button = new GuiButtonNop(this, 0, this.guiLeft + 158, this.guiTop + 4, 12, 20, "..."));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        this.setScreen(new GuiRecipes());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        this.button.f_93623_ = RecipeController.instance != null && !RecipeController.instance.anvilRecipes.isEmpty();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.resource);
        int l = (this.f_96543_ - this.f_97726_) / 2;
        int i1 = (this.f_96544_ - this.f_97727_) / 2;
        String title = I18n.get("block.customnpcs.npccarpentybench");
        graphics.blit(this.resource, l, i1, 0, 0, this.f_97726_, this.f_97727_);
        graphics.drawString(this.f_96547_, title, this.guiLeft + 4, this.guiTop + 4, CustomNpcResourceListener.DefaultTextColor, false);
        graphics.drawString(this.f_96547_, I18n.get("container.inventory"), this.guiLeft + 4, this.guiTop + 87, CustomNpcResourceListener.DefaultTextColor, false);
    }

    @Override
    public void save() {
    }
}