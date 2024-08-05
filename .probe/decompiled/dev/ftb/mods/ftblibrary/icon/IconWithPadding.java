package dev.ftb.mods.ftblibrary.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IconWithPadding extends IconWithParent {

    public int padding;

    IconWithPadding(Icon p, int b) {
        super(p);
        this.padding = b;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        x += this.padding;
        y += this.padding;
        w -= this.padding * 2;
        h -= this.padding * 2;
        this.parent.draw(graphics, x, y, w, h);
    }

    @Override
    public JsonElement getJson() {
        if (this.padding == 0) {
            return this.parent.getJson();
        } else {
            JsonObject json = new JsonObject();
            json.addProperty("id", "padding");
            json.addProperty("padding", this.padding);
            json.add("parent", this.parent.getJson());
            return json;
        }
    }

    public IconWithPadding copy() {
        return new IconWithPadding(this.parent.copy(), this.padding);
    }

    public IconWithPadding withTint(Color4I color) {
        return new IconWithPadding(this.parent.withTint(color), this.padding);
    }

    public IconWithPadding withColor(Color4I color) {
        return new IconWithPadding(this.parent.withColor(color), this.padding);
    }
}