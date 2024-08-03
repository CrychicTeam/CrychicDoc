package mezz.jei.gui.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.common.gui.elements.DrawableNineSliceTexture;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.input.IInternalKeyMappings;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.input.IUserInputHandler;
import mezz.jei.gui.input.UserInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;

public class GuiIconButton extends Button {

    private final IDrawable icon;

    private final Textures textures;

    public GuiIconButton(IDrawable icon, Button.OnPress pressable, Textures textures) {
        super(0, 0, 0, 0, CommonComponents.EMPTY, pressable, Button.DEFAULT_NARRATION);
        this.icon = icon;
        this.textures = textures;
    }

    public void updateBounds(ImmutableRect2i area) {
        this.m_252865_(area.getX());
        this.m_253211_(area.getY());
        this.f_93618_ = area.getWidth();
        this.f_93619_ = area.getHeight();
    }

    public void setHeight(int value) {
        this.f_93619_ = value;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            boolean hovered = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            DrawableNineSliceTexture texture = this.textures.getButtonForState(this.f_93623_, hovered);
            texture.draw(guiGraphics, this.m_252754_(), this.m_252907_(), this.f_93618_, this.f_93619_);
            int color = -2039584;
            if (!this.f_93623_) {
                color = -6250336;
            } else if (hovered) {
                color = -1;
            }
            float red = (float) (color >> 16 & 0xFF) / 255.0F;
            float blue = (float) (color >> 8 & 0xFF) / 255.0F;
            float green = (float) (color & 0xFF) / 255.0F;
            float alpha = (float) (color >> 24 & 0xFF) / 255.0F;
            RenderSystem.setShaderColor(red, blue, green, alpha);
            double xOffset = (double) this.m_252754_() + (double) (this.f_93618_ - this.icon.getWidth()) / 2.0;
            double yOffset = (double) this.m_252907_() + (double) (this.f_93619_ - this.icon.getHeight()) / 2.0;
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(xOffset, yOffset, 0.0);
            this.icon.draw(guiGraphics);
            poseStack.popPose();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public IUserInputHandler createInputHandler() {
        return new GuiIconButton.UserInputHandler(this);
    }

    private class UserInputHandler implements IUserInputHandler {

        private final GuiIconButton button;

        public UserInputHandler(GuiIconButton button) {
            this.button = button;
        }

        @Override
        public Optional<IUserInputHandler> handleUserInput(Screen screen, UserInput input, IInternalKeyMappings keyBindings) {
            if (!input.isMouse()) {
                return Optional.empty();
            } else {
                double mouseX = input.getMouseX();
                double mouseY = input.getMouseY();
                if (this.button.f_93623_ && this.button.f_93624_ && GuiIconButton.this.m_5953_(mouseX, mouseY)) {
                    if (!this.button.m_7972_(input.getKey().getValue())) {
                        return Optional.empty();
                    } else {
                        boolean flag = this.button.m_93680_(mouseX, mouseY);
                        if (!flag) {
                            return Optional.empty();
                        } else {
                            if (!input.isSimulate()) {
                                this.button.m_7435_(Minecraft.getInstance().getSoundManager());
                                this.button.m_5716_(mouseX, mouseY);
                            }
                            return Optional.of(this);
                        }
                    }
                } else {
                    return Optional.empty();
                }
            }
        }
    }
}