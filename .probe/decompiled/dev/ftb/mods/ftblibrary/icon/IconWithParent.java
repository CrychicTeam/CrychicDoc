package dev.ftb.mods.ftblibrary.icon;

public abstract class IconWithParent extends Icon {

    public final Icon parent;

    public IconWithParent(Icon i) {
        this.parent = i;
    }
}