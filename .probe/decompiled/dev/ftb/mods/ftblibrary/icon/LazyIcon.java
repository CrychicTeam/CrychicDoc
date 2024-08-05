package dev.ftb.mods.ftblibrary.icon;

import com.google.gson.JsonElement;
import dev.ftb.mods.ftblibrary.math.PixelBuffer;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class LazyIcon extends Icon {

    public final Supplier<Icon> iconSupplier;

    private Icon cachedIcon;

    public LazyIcon(Supplier<Icon> s) {
        this.iconSupplier = s;
    }

    public Icon getIcon() {
        if (this.cachedIcon == null) {
            this.cachedIcon = (Icon) this.iconSupplier.get();
            if (this.cachedIcon == null || this.cachedIcon.isEmpty()) {
                this.cachedIcon = Icon.empty();
            }
        }
        return this.cachedIcon;
    }

    @Override
    public boolean isEmpty() {
        return this.getIcon().isEmpty();
    }

    @Override
    public Icon copy() {
        return new LazyIcon(() -> this.getIcon().copy());
    }

    @Override
    public JsonElement getJson() {
        return this.getIcon().getJson();
    }

    @Override
    public Icon withColor(Color4I color) {
        return this.getIcon().withColor(color);
    }

    @Override
    public Icon withTint(Color4I color) {
        return this.getIcon().withTint(color);
    }

    @Override
    public Icon withUV(float u0, float v0, float u1, float v1) {
        return this.getIcon().withUV(u0, v0, u1, v1);
    }

    @Override
    public int hashCode() {
        return this.getJson().hashCode();
    }

    @Override
    public boolean hasPixelBuffer() {
        return this.getIcon().hasPixelBuffer();
    }

    @Nullable
    @Override
    public PixelBuffer createPixelBuffer() {
        return this.getIcon().createPixelBuffer();
    }

    @Nullable
    @Override
    public Object getIngredient() {
        return this.getIcon().getIngredient();
    }

    @Override
    protected void setProperties(IconProperties properties) {
        this.getIcon().setProperties(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        this.getIcon().draw(graphics, x, y, w, h);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawStatic(GuiGraphics graphics, int x, int y, int w, int h) {
        this.getIcon().drawStatic(graphics, x, y, w, h);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw3D(GuiGraphics graphics) {
        this.getIcon().draw3D(graphics);
    }

    public String toString() {
        return this.getIcon().toString();
    }
}