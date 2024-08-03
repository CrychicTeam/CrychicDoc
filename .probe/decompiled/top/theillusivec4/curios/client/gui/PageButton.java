package top.theillusivec4.curios.client.gui;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketPage;

public class PageButton extends Button {

    private final CuriosScreenV2 parentGui;

    private final PageButton.Type type;

    private static final ResourceLocation CURIO_INVENTORY = new ResourceLocation("curios", "textures/gui/inventory_revamp.png");

    public PageButton(CuriosScreenV2 parentGui, int xIn, int yIn, int widthIn, int heightIn, PageButton.Type type) {
        super(xIn, yIn, widthIn, heightIn, CommonComponents.EMPTY, button -> NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketPage(((CuriosContainerV2) parentGui.m_6262_()).f_38840_, type == PageButton.Type.NEXT)), f_252438_);
        this.parentGui = parentGui;
        this.type = type;
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int x, int y, float partialTicks) {
        int xText = this.type == PageButton.Type.NEXT ? 43 : 32;
        int yText = 25;
        if (this.type == PageButton.Type.NEXT) {
            this.m_252865_(this.parentGui.getGuiLeft() - 17);
            this.f_93623_ = ((CuriosContainerV2) this.parentGui.m_6262_()).currentPage + 1 < ((CuriosContainerV2) this.parentGui.m_6262_()).totalPages;
        } else {
            this.m_252865_(this.parentGui.getGuiLeft() - 28);
            this.f_93623_ = ((CuriosContainerV2) this.parentGui.m_6262_()).currentPage > 0;
        }
        if (!this.m_142518_()) {
            yText += 12;
        } else if (this.m_198029_()) {
            xText += 22;
        }
        if (this.m_274382_()) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("gui.curios.page", ((CuriosContainerV2) this.parentGui.m_6262_()).currentPage + 1, ((CuriosContainerV2) this.parentGui.m_6262_()).totalPages), x, y);
        }
        guiGraphics.blit(CURIO_INVENTORY, this.m_252754_(), this.m_252907_(), xText, yText, this.f_93618_, this.f_93619_);
    }

    public static enum Type {

        NEXT, PREVIOUS
    }
}