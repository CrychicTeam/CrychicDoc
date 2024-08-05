package de.keksuccino.konkrete.rendering.animation;

import net.minecraft.client.gui.GuiGraphics;

public interface IAnimationRenderer {

    void render(GuiGraphics var1);

    void setStretchImageToScreensize(boolean var1);

    boolean isStretchedToStreensize();

    void setHideAfterLastFrame(boolean var1);

    boolean isFinished();

    void setWidth(int var1);

    int getWidth();

    void setHeight(int var1);

    int getHeight();

    void setPosX(int var1);

    int getPosX();

    void setPosY(int var1);

    int getPosY();

    int currentFrame();

    int animationFrames();

    void resetAnimation();

    boolean isReady();

    void prepareAnimation();

    String getPath();

    void setFPS(int var1);

    boolean isGettingLooped();

    void setLooped(boolean var1);

    int getFPS();

    void setOpacity(float var1);
}