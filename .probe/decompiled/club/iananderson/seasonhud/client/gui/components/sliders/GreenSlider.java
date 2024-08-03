package club.iananderson.seasonhud.client.gui.components.sliders;

import club.iananderson.seasonhud.client.gui.components.boxes.ColorEditBox;
import club.iananderson.seasonhud.util.Rgb;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;

public class GreenSlider extends RgbSlider {

    public GreenSlider(int x, int y, ColorEditBox seasonBox) {
        super(x, y, seasonBox);
        this.f_93618_ = seasonBox.m_5711_() + 2;
        this.seasonBox = seasonBox;
        this.maxValue = 255;
        this.initial = (double) Rgb.getGreen(this.season);
        this.g = Rgb.rgbColor(Integer.parseInt(seasonBox.m_94155_())).getGreen();
        this.f_93577_ = this.snapToNearest((double) this.g);
        this.textColor = ChatFormatting.GREEN;
        this.m_5695_();
    }

    @Override
    public void setSliderValue(int newValue) {
        int oldValue = (int) this.f_93577_;
        this.f_93577_ = this.snapToNearest((double) Rgb.rgbColor(newValue).getGreen());
        if (!Mth.equal((double) oldValue, this.f_93577_)) {
            this.g = Rgb.rgbColor(newValue).getGreen();
        }
        this.m_5695_();
    }

    @Override
    protected void applyValue() {
        this.r = Rgb.getRed(this.season);
        this.b = Rgb.getBlue(this.season);
        this.rgb = Rgb.rgbInt(this.r, this.getValueInt(), this.b);
        Rgb.setRgb(this.season, this.rgb);
        this.seasonBox.m_94144_(String.valueOf(this.rgb));
    }
}