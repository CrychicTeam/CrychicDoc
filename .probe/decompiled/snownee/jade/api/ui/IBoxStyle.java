package snownee.jade.api.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public interface IBoxStyle {

    float borderWidth();

    void render(GuiGraphics var1, float var2, float var3, float var4, float var5);

    default void tag(ResourceLocation tag) {
    }

    public static enum Empty implements IBoxStyle {

        INSTANCE;

        @Override
        public float borderWidth() {
            return 0.0F;
        }

        @Override
        public void render(GuiGraphics guiGraphics, float x, float y, float w, float h) {
        }
    }
}