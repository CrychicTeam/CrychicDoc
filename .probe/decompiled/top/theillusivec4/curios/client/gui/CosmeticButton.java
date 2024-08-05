package top.theillusivec4.curios.client.gui;

import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketToggleCosmetics;

public class CosmeticButton extends ImageButton {

    private static final ResourceLocation CURIO_INVENTORY = new ResourceLocation("curios", "textures/gui/inventory_revamp.png");

    private final CuriosScreenV2 parentGui;

    CosmeticButton(CuriosScreenV2 parentGui, int xIn, int yIn, int widthIn, int heightIn) {
        super(xIn, yIn, widthIn, heightIn, 0, 0, CURIO_INVENTORY, button -> {
            ((CuriosContainerV2) parentGui.m_6262_()).toggleCosmetics();
            NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketToggleCosmetics(((CuriosContainerV2) parentGui.m_6262_()).f_38840_));
        });
        this.parentGui = parentGui;
        this.m_257544_(Tooltip.create(Component.translatable("gui.curios.toggle.cosmetics")));
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int x, int y, float partialTicks) {
        int yTex = 0;
        int xTex;
        if (((CuriosContainerV2) this.parentGui.m_6262_()).isViewingCosmetics) {
            xTex = 143;
        } else {
            xTex = 123;
        }
        if (this.m_198029_()) {
            yTex = 17;
        }
        this.m_252865_(this.parentGui.getGuiLeft() - 27);
        this.m_253211_(this.parentGui.getGuiTop() - 18);
        guiGraphics.blit(this.f_94223_, this.m_252754_(), this.m_252907_(), xTex, yTex, this.f_93618_, this.f_93619_);
    }
}