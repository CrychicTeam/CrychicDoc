package dev.ftb.mods.ftblibrary.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IconWithBorder extends IconWithParent {

    public static final Icon BUTTON_GRAY = Color4I.rgb(2171169).withBorder(Color4I.rgb(1315860), false);

    public static final Icon BUTTON_RED = Color4I.rgb(1409462).withBorder(Color4I.rgb(12531494), false);

    public static final Icon BUTTON_GREEN = Color4I.rgb(10012160).withBorder(Color4I.rgb(4425472), false);

    public static final Icon BUTTON_BLUE = Color4I.rgb(8439794).withBorder(Color4I.rgb(1409462), false);

    public static final Icon BUTTON_ROUND_GRAY = Color4I.rgb(2171169).withBorder(Color4I.rgb(1315860), true);

    public static final Icon BUTTON_ROUND_RED = Color4I.rgb(1409462).withBorder(Color4I.rgb(12531494), true);

    public static final Icon BUTTON_ROUND_GREEN = Color4I.rgb(10012160).withBorder(Color4I.rgb(4425472), true);

    public static final Icon BUTTON_ROUND_BLUE = Color4I.rgb(8439794).withBorder(Color4I.rgb(1409462), true);

    public Color4I color;

    public boolean roundEdges;

    IconWithBorder(Icon i, Color4I c, boolean r) {
        super(i);
        this.color = c;
        this.roundEdges = r;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        this.parent.draw(graphics, x + 1, y + 1, w - 2, h - 2);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GuiHelper.drawHollowRect(graphics, x, y, w, h, this.color, this.roundEdges);
    }

    @Override
    public JsonElement getJson() {
        JsonObject o = new JsonObject();
        o.addProperty("id", "border");
        o.add("icon", this.parent.getJson());
        o.add("color", this.color.getJson());
        if (this.roundEdges) {
            o.addProperty("round_edges", true);
        }
        return o;
    }

    public IconWithBorder copy() {
        return new IconWithBorder(this.parent.copy(), this.color.copy(), this.roundEdges);
    }

    public IconWithBorder withTint(Color4I c) {
        return new IconWithBorder(this.parent, this.color.withTint(c), this.roundEdges);
    }

    public IconWithBorder withColor(Color4I c) {
        return new IconWithBorder(this.parent, c, this.roundEdges);
    }
}