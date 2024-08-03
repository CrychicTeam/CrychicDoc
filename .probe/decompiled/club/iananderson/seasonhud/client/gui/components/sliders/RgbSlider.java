package club.iananderson.seasonhud.client.gui.components.sliders;

import club.iananderson.seasonhud.client.SeasonHUDClient;
import club.iananderson.seasonhud.client.gui.components.boxes.ColorEditBox;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.seasons.SeasonList;
import club.iananderson.seasonhud.util.DrawUtil;
import club.iananderson.seasonhud.util.Rgb;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class RgbSlider extends AbstractSliderButton {

    private static final ResourceLocation SLIDER_LOCATION = new ResourceLocation("textures/gui/slider.png");

    public static int SLIDER_PADDING = 2;

    public final boolean drawString;

    public boolean enableColor = Config.enableSeasonNameColor.get();

    public SeasonList season;

    public ColorEditBox seasonBox;

    public int minValue;

    public int maxValue;

    public int r;

    public int g;

    public int b;

    public int rgb;

    public double initial;

    public ChatFormatting textColor;

    private boolean canChangeValue;

    private RgbSlider(int x, int y, int width, int height, double initial) {
        super(x, y, width, height, CommonComponents.EMPTY, initial);
        this.initial = initial;
        this.drawString = true;
    }

    public RgbSlider(int x, int y, ColorEditBox seasonBox) {
        this(x, y, seasonBox.m_5711_() + 2, seasonBox.m_93694_() - 6, (double) Integer.parseInt(seasonBox.m_94155_()));
        this.minValue = 0;
        this.maxValue = 16777215;
        this.season = seasonBox.getSeason();
        this.rgb = Integer.parseInt(seasonBox.m_94155_());
        this.r = Rgb.rgbColor(this.rgb).getRed();
        this.g = Rgb.rgbColor(this.rgb).getGreen();
        this.b = Rgb.rgbColor(this.rgb).getBlue();
        this.f_93577_ = this.snapToNearest((double) this.rgb);
        this.textColor = ChatFormatting.WHITE;
        this.updateMessage();
    }

    @Override
    public int getTextureY() {
        int i = this.m_93696_() && !this.canChangeValue ? 1 : 0;
        return i * 20;
    }

    @Override
    public int getHandleTextureY() {
        int i = !this.f_93622_ && !this.canChangeValue ? 2 : 3;
        return i * 20;
    }

    @Override
    protected void updateMessage() {
        Component colorString = Component.literal(this.getValueString());
        if (this.drawString) {
            this.m_93666_(colorString.copy().withStyle(this.textColor));
            if (!this.enableColor) {
                this.m_93666_(colorString.copy().withStyle(ChatFormatting.GRAY));
            }
        } else {
            this.m_93666_(Component.empty());
        }
    }

    public double snapToNearest(double value) {
        return (double) ((Mth.clamp((float) value, (float) this.minValue, (float) this.maxValue) - (float) this.minValue) / (float) (this.maxValue - this.minValue));
    }

    public void setSliderValue(int newValue) {
    }

    public double getValue() {
        return this.f_93577_ * (double) (this.maxValue - this.minValue) + (double) this.minValue;
    }

    public long getValueLong() {
        return Math.round(this.getValue());
    }

    public int getValueInt() {
        return (int) this.getValueLong();
    }

    public String getValueString() {
        return String.valueOf(this.getValueInt());
    }

    @Override
    protected void applyValue() {
    }

    public int getFGColor() {
        return this.f_93623_ ? 16777215 : 10526880;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        DrawUtil.blitWithBorder(graphics, SLIDER_LOCATION, this.m_252754_(), this.m_252907_(), 0, this.getTextureY(), this.f_93618_, this.f_93619_, 200, 20, 2, 3, 2, 2);
        DrawUtil.blitWithBorder(graphics, SLIDER_LOCATION, this.m_252754_() + (int) (this.f_93577_ * (double) (this.f_93618_ - 8)), this.m_252907_(), 0, this.getHandleTextureY(), 8, this.f_93619_, 200, 20, 2, 3, 2, 2);
        this.m_280372_(graphics, SeasonHUDClient.mc.font, 2, this.getFGColor() | Mth.ceil(this.f_93625_ * 255.0F) << 24);
    }
}