package dev.xkmc.l2backpack.content.render;

public interface ItemOnBackItem {

    default boolean shouldRender() {
        return true;
    }
}