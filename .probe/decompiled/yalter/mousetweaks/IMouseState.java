package yalter.mousetweaks;

public interface IMouseState {

    boolean isButtonPressed(MouseButton var1);

    int consumeScrollAmount();

    default void update() {
    }

    default void clear() {
    }
}