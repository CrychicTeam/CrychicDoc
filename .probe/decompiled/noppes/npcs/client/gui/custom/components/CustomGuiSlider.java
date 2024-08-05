package noppes.npcs.client.gui.custom.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiSliderWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCustomGuiSliderUpdate;

public class CustomGuiSlider extends AbstractWidget implements IGuiComponent {

    private GuiCustom parent;

    private CustomGuiSliderWrapper component;

    private CustomGuiTextFieldWrapper tfcomponent;

    private CustomGuiTextField textfield;

    private float sliderValue;

    private float startValue;

    private long lastClickedTime = 0L;

    private float total;

    public int id;

    private boolean disablePackets = false;

    public CustomGuiSlider(GuiCustom parent, CustomGuiSliderWrapper component) {
        super(component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight(), Component.translatable(component.getFormat(), component.getValue()));
        this.component = component;
        this.parent = parent;
        this.tfcomponent = new CustomGuiTextFieldWrapper(this.id, this.m_252754_(), this.m_252907_(), this.f_93618_, this.f_93619_).setCharacterType(3);
        this.init();
    }

    @Override
    public void init() {
        this.id = this.component.getID();
        this.m_252865_(this.component.getPosX());
        this.m_253211_(this.component.getPosY());
        this.m_93674_(this.component.getWidth());
        this.setHeight(this.component.getHeight());
        this.total = this.component.getMax() - this.component.getMin();
        this.startValue = this.sliderValue = (this.component.getValue() - this.component.getMin()) / this.total;
        this.tfcomponent.setID(this.id);
        this.tfcomponent.setPos(this.component.getPosX(), this.component.getPosY());
        this.tfcomponent.setSize(this.component.getWidth(), this.component.getHeight());
        this.f_93623_ = this.component.getEnabled() && this.component.getVisible();
        this.f_93624_ = this.component.getVisible();
        this.m_93666_(Component.translatable(this.component.getFormat(), this.component.getValue()));
    }

    public CustomGuiSlider disablePackets() {
        this.disablePackets = true;
        return this;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack matrixStack = graphics.pose();
        if (this.f_93624_) {
            super.render(graphics, mouseX, mouseY, partialTicks);
            if (this.textfield != null) {
                this.textfield.onRender(graphics, mouseX, mouseY, partialTicks);
                if (!this.textfield.m_93696_()) {
                    this.closeTextfield();
                }
            }
            boolean hovered = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            if (hovered && this.component.hasHoverText()) {
                this.parent.hoverText = this.component.getHoverTextList();
            }
        }
    }

    private void setSliderValue(float value) {
        value = Mth.clamp(value, 0.0F, 1.0F);
        if (value != this.sliderValue) {
            this.sliderValue = value;
            this.component.setValue(value * this.total + this.component.getMin());
            this.m_93666_(Component.translatable(this.component.getFormat(), this.component.getValue()));
            if (!this.disablePackets) {
                Packets.sendServer(new SPacketCustomGuiSliderUpdate(this.component.getUniqueID(), this.component.getValue()));
            } else {
                this.component.onChange(null);
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.textfield != null && (keyCode == 257 || keyCode == 335)) {
            this.closeTextfield();
        }
        return this.textfield != null ? this.textfield.keyPressed(keyCode, scanCode, modifiers) : super.m_7933_(keyCode, scanCode, modifiers);
    }

    private void closeTextfield() {
        this.setSliderValue((this.tfcomponent.getFloat() + this.component.getMin()) / this.total);
        this.textfield = null;
    }

    @Override
    public boolean charTyped(char c, int i) {
        return this.textfield != null ? this.textfield.charTyped(c, i) : super.m_5534_(c, i);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, f_93617_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        int i = !this.f_93623_ ? 0 : (this.m_198029_() ? 2 : 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        guiGraphics0.blit(f_93617_, this.m_252754_(), this.m_252907_(), 0, 46 + i * 20, this.f_93618_ / 2, this.f_93619_);
        guiGraphics0.blit(f_93617_, this.m_252754_() + this.f_93618_ / 2, this.m_252907_(), 200 - this.f_93618_ / 2, 46 + i * 20, this.f_93618_ / 2, this.f_93619_);
        this.renderBg(guiGraphics0, minecraft, int1, int2);
        int j = this.getFGColor();
        guiGraphics0.drawCenteredString(font, this.m_6035_(), this.m_252754_() + this.f_93618_ / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, j | Mth.ceil(this.f_93625_ * 255.0F) << 24);
    }

    @Override
    public void onClick(double x, double y) {
        if (this.f_93624_ && this.f_93623_) {
            long time = System.currentTimeMillis();
            if (time - this.lastClickedTime < 500L) {
                this.tfcomponent.setText(this.component.getValue() + "");
                this.textfield = new CustomGuiTextField(this.parent, this.tfcomponent);
                this.textfield.setFocused(true);
            } else if (this.textfield != null) {
                this.textfield.m_6375_(x, y, 0);
                return;
            }
            this.lastClickedTime = time;
            this.setSliderValue((float) (x - (double) (this.m_252754_() + 4)) / (float) (this.f_93618_ - 8));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return this.textfield != null ? this.textfield.m_6375_(mouseX, mouseY, mouseButton) : super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dx, double dy) {
        this.setSliderValue((float) (mouseX - (double) (this.m_252754_() + 4)) / (float) (this.f_93618_ - 8));
        return true;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    public void tick() {
        if (this.textfield != null) {
            this.textfield.m_94120_();
        }
    }

    @Override
    public void onRelease(double x, double y) {
        if (this.sliderValue != this.startValue) {
            super.playDownSound(Minecraft.getInstance().getSoundManager());
            this.startValue = this.sliderValue;
        }
    }

    public void renderBg(GuiGraphics graphics, Minecraft mc, int p_146119_2_, int p_146119_3_) {
        if (this.f_93624_) {
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, f_93617_);
            int lvt_4_1_ = (this.f_93622_ ? 2 : 1) * 20;
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 10.0F);
            graphics.blit(f_93617_, this.m_252754_() + (int) ((double) this.sliderValue * (double) (this.f_93618_ - 8)), this.m_252907_(), 0, 46 + lvt_4_1_, 4, this.f_93619_ / 2);
            graphics.blit(f_93617_, this.m_252754_() + (int) ((double) this.sliderValue * (double) (this.f_93618_ - 8)), this.m_252907_() + this.f_93619_ / 2, 0, 46 + lvt_4_1_ + 20 - this.f_93619_ / 2, 4, this.f_93619_ / 2);
            graphics.blit(f_93617_, this.m_252754_() + (int) ((double) this.sliderValue * (double) (this.f_93618_ - 8)) + 4, this.m_252907_(), 196, 46 + lvt_4_1_, 4, this.f_93619_ / 2);
            graphics.blit(f_93617_, this.m_252754_() + (int) ((double) this.sliderValue * (double) (this.f_93618_ - 8)) + 4, this.m_252907_() + this.f_93619_ / 2, 196, 46 + lvt_4_1_ + 20 - this.f_93619_ / 2, 4, this.f_93619_ / 2);
            graphics.pose().popPose();
        }
    }

    @Override
    public ICustomGuiComponent component() {
        return this.component;
    }
}