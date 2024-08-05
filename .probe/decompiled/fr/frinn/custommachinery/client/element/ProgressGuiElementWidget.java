package fr.frinn.custommachinery.client.element;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.common.crafting.machine.MachineProcessor;
import fr.frinn.custommachinery.common.guielement.ProgressBarGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ProgressGuiElementWidget extends AbstractGuiElementWidget<ProgressBarGuiElement> {

    public ProgressGuiElementWidget(ProgressBarGuiElement element, IMachineScreen screen) {
        super(element, screen, Component.literal("Progress Bar"));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int filledWidth = (int) ((double) this.f_93618_ * Mth.clamp(Mth.map(this.getRecipeProgressPercent(), (double) this.getElement().getStart(), (double) this.getElement().getEnd(), 0.0, 1.0), 0.0, 1.0));
        int filledHeight = (int) ((double) this.f_93619_ * Mth.clamp(Mth.map(this.getRecipeProgressPercent(), (double) this.getElement().getStart(), (double) this.getElement().getEnd(), 0.0, 1.0), 0.0, 1.0));
        if (this.getElement().getEmptyTexture().equals(ProgressBarGuiElement.BASE_EMPTY_TEXTURE) && this.getElement().getFilledTexture().equals(ProgressBarGuiElement.BASE_FILLED_TEXTURE)) {
            graphics.pose().pushPose();
            rotate(graphics.pose(), this.getElement().getDirection(), this.m_252754_(), this.m_252907_(), this.f_93618_, this.f_93619_);
            graphics.blit(this.getElement().getEmptyTexture(), 0, 0, 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
            graphics.blit(this.getElement().getFilledTexture(), 0, 0, 0.0F, 0.0F, filledWidth, this.f_93619_, this.f_93618_, this.f_93619_);
            graphics.pose().popPose();
        } else {
            graphics.blit(this.getElement().getEmptyTexture(), this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
            ResourceLocation filled = this.getElement().getFilledTexture();
            switch(this.getElement().getDirection()) {
                case RIGHT:
                    graphics.blit(filled, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, filledWidth, this.f_93619_, this.f_93618_, this.f_93619_);
                    break;
                case LEFT:
                    graphics.blit(filled, this.m_252754_() + this.f_93618_ - filledWidth, this.m_252907_(), (float) (this.f_93618_ - filledWidth), 0.0F, filledWidth, this.f_93619_, this.f_93618_, this.f_93619_);
                    break;
                case TOP:
                    graphics.blit(filled, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, filledHeight, this.f_93618_, this.f_93619_);
                    break;
                case BOTTOM:
                    graphics.blit(filled, this.m_252754_(), this.m_252907_() + this.f_93619_ - filledHeight, 0.0F, (float) (this.f_93619_ - filledHeight), this.f_93618_, filledHeight, this.f_93618_, this.f_93619_);
            }
        }
    }

    public double getRecipeProgressPercent() {
        if (this.getScreen().getTile().getProcessor() instanceof MachineProcessor machineProcessor && machineProcessor.getRecipeTotalTime() > 0) {
            return machineProcessor.getRecipeProgressTime() / (double) machineProcessor.getRecipeTotalTime();
        }
        return this.getScreen().getTile().getMachine().isDummy() ? (double) (System.currentTimeMillis() % 2000L) / 2000.0 : 0.0;
    }

    public static void rotate(PoseStack matrix, ProgressBarGuiElement.Orientation orientation, int posX, int posY, int width, int height) {
        switch(orientation) {
            case RIGHT:
                matrix.translate((float) posX, (float) posY, 0.0F);
                break;
            case LEFT:
                matrix.mulPose(new Quaternionf().fromAxisAngleDeg(new Vector3f(0.0F, 0.0F, 1.0F), 180.0F));
                matrix.translate((float) (-width - posX), (float) (-height - posY), 0.0F);
                break;
            case TOP:
                matrix.mulPose(new Quaternionf().fromAxisAngleDeg(new Vector3f(0.0F, 0.0F, 1.0F), 270.0F));
                matrix.translate((float) (-width - posY), (float) posX, 0.0F);
                break;
            case BOTTOM:
                matrix.mulPose(new Quaternionf().fromAxisAngleDeg(new Vector3f(0.0F, 0.0F, 1.0F), 90.0F));
                matrix.translate((float) posY, (float) (-height - posX), 0.0F);
        }
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return false;
    }
}