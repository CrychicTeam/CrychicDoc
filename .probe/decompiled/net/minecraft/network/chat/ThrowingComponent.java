package net.minecraft.network.chat;

public class ThrowingComponent extends Exception {

    private final Component component;

    public ThrowingComponent(Component component0) {
        super(component0.getString());
        this.component = component0;
    }

    public ThrowingComponent(Component component0, Throwable throwable1) {
        super(component0.getString(), throwable1);
        this.component = component0;
    }

    public Component getComponent() {
        return this.component;
    }
}