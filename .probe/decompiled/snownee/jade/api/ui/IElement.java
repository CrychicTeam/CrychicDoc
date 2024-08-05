package snownee.jade.api.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

public interface IElement {

    IElement size(@Nullable Vec2 var1);

    Vec2 getSize();

    Vec2 getCachedSize();

    void render(GuiGraphics var1, float var2, float var3, float var4, float var5);

    IElement align(IElement.Align var1);

    IElement.Align getAlignment();

    IElement translate(Vec2 var1);

    Vec2 getTranslation();

    IElement tag(ResourceLocation var1);

    ResourceLocation getTag();

    @Nullable
    default String getMessage() {
        return null;
    }

    @Nullable
    String getCachedMessage();

    IElement clearCachedMessage();

    IElement message(@Nullable String var1);

    public static enum Align {

        LEFT, RIGHT
    }
}