package io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces;

public interface IMouseListener {

    boolean onMouseClicked(double var1, double var3, int var5);

    default boolean onMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }
}