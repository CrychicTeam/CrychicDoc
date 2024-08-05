package dev.ftb.mods.ftblibrary.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HollowRectangleIcon extends Icon {

    public Color4I color;

    public boolean roundEdges;

    public HollowRectangleIcon(Color4I c, boolean r) {
        this.color = c;
        this.roundEdges = r;
    }

    public HollowRectangleIcon copy() {
        return new HollowRectangleIcon(this.color, this.roundEdges);
    }

    public HollowRectangleIcon withColor(Color4I color) {
        return new HollowRectangleIcon(color, this.roundEdges);
    }

    public HollowRectangleIcon withTint(Color4I c) {
        return this.withColor(this.color.withTint(c));
    }

    @Override
    protected void setProperties(IconProperties properties) {
        super.setProperties(properties);
        this.roundEdges = properties.getBoolean("round_edges", this.roundEdges);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        GuiHelper.drawHollowRect(graphics, x, y, w, h, this.color, this.roundEdges);
    }

    @Override
    public JsonElement getJson() {
        JsonObject o = new JsonObject();
        o.addProperty("id", "hollow_rectangle");
        o.add("color", this.color.getJson());
        if (this.roundEdges) {
            o.addProperty("round_edges", true);
        }
        return o;
    }
}