package mezz.jei.gui.input;

public interface ICharTypedHandler {

    boolean hasKeyboardFocus();

    boolean onCharTyped(char var1, int var2);
}