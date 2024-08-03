package dev.ftb.mods.ftblibrary.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import dev.ftb.mods.ftblibrary.math.PixelBuffer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class MutableColor4I extends Color4I {

    public static final Color4I TEMP = new MutableColor4I(255, 255, 255, 255);

    MutableColor4I(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public MutableColor4I copy() {
        return new MutableColor4I(this.red, this.green, this.blue, this.alpha);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public MutableColor4I mutable() {
        return this;
    }

    @Override
    public JsonElement getJson() {
        if (this.isEmpty()) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject json = new JsonObject();
            json.addProperty("red", this.red);
            json.addProperty("green", this.green);
            json.addProperty("blue", this.blue);
            if (this.alpha < 255) {
                json.addProperty("alpha", this.alpha);
            }
            json.addProperty("mutable", true);
            return json;
        }
    }

    public Color4I set(int r, int g, int b, int a) {
        this.red = r & 0xFF;
        this.green = g & 0xFF;
        this.blue = b & 0xFF;
        this.alpha = a & 0xFF;
        return this;
    }

    public Color4I set(Color4I col, int a) {
        return this.set(col.red, col.green, col.blue, a);
    }

    public Color4I set(Color4I col) {
        return this.set(col, col.alpha);
    }

    public Color4I set(int col, int a) {
        return this.set(col >> 16, col >> 8, col, a);
    }

    public Color4I set(int col) {
        return this.set(col, col >> 24);
    }

    public Color4I setAlpha(int a) {
        this.alpha = a;
        return this;
    }

    public Color4I addBrightness(int b) {
        return this.set(Mth.clamp(this.red + b, 0, 255), Mth.clamp(this.green + b, 0, 255), Mth.clamp(this.blue + b, 0, 255), this.alpha);
    }

    private int toint(float f) {
        return (int) (f * 255.0F + 0.5F);
    }

    public Color4I setFromHSB(float h, float s, float b) {
        this.red = this.green = this.blue = 0;
        if (!(s <= 0.0F) && !(b <= 0.0F)) {
            if (s > 1.0F) {
                s = 1.0F;
            }
            if (b > 1.0F) {
                b = 1.0F;
            }
            float h6 = (h - (float) Mth.floor(h)) * 6.0F;
            float f = h6 - (float) Mth.floor(h6);
            float p = b * (1.0F - s);
            float q = b * (1.0F - s * f);
            float t = b * (1.0F - s * (1.0F - f));
            switch((int) h6) {
                case 0:
                    this.red = this.toint(b);
                    this.green = this.toint(t);
                    this.blue = this.toint(p);
                    return this;
                case 1:
                    this.red = this.toint(q);
                    this.green = this.toint(b);
                    this.blue = this.toint(p);
                    return this;
                case 2:
                    this.red = this.toint(p);
                    this.green = this.toint(b);
                    this.blue = this.toint(t);
                    return this;
                case 3:
                    this.red = this.toint(p);
                    this.green = this.toint(q);
                    this.blue = this.toint(b);
                    return this;
                case 4:
                    this.red = this.toint(t);
                    this.green = this.toint(p);
                    this.blue = this.toint(b);
                    return this;
                default:
                    this.red = this.toint(b);
                    this.green = this.toint(p);
                    this.blue = this.toint(q);
                    return this;
            }
        } else {
            this.red = this.green = this.blue = this.toint(b);
            return this;
        }
    }

    static class None extends MutableColor4I {

        private boolean hasColor = false;

        None() {
            super(255, 255, 255, 255);
        }

        @Override
        public Color4I set(int r, int g, int b, int a) {
            this.hasColor = true;
            return super.set(r, g, b, a);
        }

        @Override
        public boolean isEmpty() {
            return !this.hasColor;
        }

        @Nullable
        @Override
        public PixelBuffer createPixelBuffer() {
            return null;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            return o == this;
        }
    }
}