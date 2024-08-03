package club.iananderson.seasonhud.client.gui.components.sliders;

import club.iananderson.seasonhud.client.gui.components.boxes.ColorEditBox;
import club.iananderson.seasonhud.util.Rgb;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;

public class RedSlider extends RgbSlider {

    public RedSlider(int x, int y, ColorEditBox seasonBox) {
        super(x, y, seasonBox);
        this.f_93618_ = seasonBox.m_5711_() + 2;
        this.seasonBox = seasonBox;
        this.maxValue = 255;
        this.initial = (double) Rgb.getRed(this.season);
        this.r = Rgb.rgbColor(Integer.parseInt(seasonBox.m_94155_())).getRed();
        this.f_93577_ = this.snapToNearest((double) this.r);
        this.textColor = ChatFormatting.RED;
        this.m_5695_();
    }

    @Override
    public void setSliderValue(int newValue) {
        int oldValue = (int) this.f_93577_;
        this.f_93577_ = this.snapToNearest((double) Rgb.rgbColor(newValue).getRed());
        if (!Mth.equal((double) oldValue, this.f_93577_)) {
            this.r = Rgb.rgbColor(newValue).getRed();
        }
        this.m_5695_();
    }

    @Override
    protected void applyValue() {
        this.g = Rgb.getGreen(this.season);
        this.b = Rgb.getBlue(this.season);
        this.rgb = Rgb.rgbInt(this.getValueInt(), this.g, this.b);
        Rgb.setRgb(this.season, this.rgb);
        this.seasonBox.m_94144_(String.valueOf(this.rgb));
    }
}