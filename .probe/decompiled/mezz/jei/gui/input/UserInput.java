package mezz.jei.gui.input;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Optional;
import mezz.jei.api.runtime.IJeiKeyMapping;
import mezz.jei.common.platform.IPlatformInputHelper;
import mezz.jei.common.platform.Services;
import net.minecraft.SharedConstants;
import net.minecraft.client.KeyMapping;

public class UserInput {

    private final InputConstants.Key key;

    private final double mouseX;

    private final double mouseY;

    private final int modifiers;

    private final InputType clickState;

    public static UserInput fromVanilla(int keyCode, int scanCode, int modifiers, InputType inputType) {
        InputConstants.Key input = InputConstants.getKey(keyCode, scanCode);
        return new UserInput(input, MouseUtil.getX(), MouseUtil.getY(), modifiers, inputType);
    }

    public static Optional<UserInput> fromVanilla(double mouseX, double mouseY, int mouseButton, InputType inputType) {
        if (mouseButton < 0) {
            return Optional.empty();
        } else {
            InputConstants.Key input = InputConstants.Type.MOUSE.getOrCreate(mouseButton);
            UserInput userInput = new UserInput(input, mouseX, mouseY, 0, inputType);
            return Optional.of(userInput);
        }
    }

    public UserInput(InputConstants.Key key, double mouseX, double mouseY, int modifiers, InputType clickState) {
        this.key = key;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.modifiers = modifiers;
        this.clickState = clickState;
    }

    public InputConstants.Key getKey() {
        return this.key;
    }

    public double getMouseX() {
        return this.mouseX;
    }

    public double getMouseY() {
        return this.mouseY;
    }

    public InputType getClickState() {
        return this.clickState;
    }

    public boolean isSimulate() {
        return this.clickState == InputType.SIMULATE;
    }

    public boolean isMouse() {
        return this.key.getType() == InputConstants.Type.MOUSE;
    }

    public boolean isKeyboard() {
        return this.key.getType() == InputConstants.Type.KEYSYM;
    }

    public boolean isAllowedChatCharacter() {
        return this.isKeyboard() && SharedConstants.isAllowedChatCharacter((char) this.key.getValue());
    }

    public boolean is(IJeiKeyMapping keyMapping) {
        return keyMapping.isActiveAndMatches(this.key);
    }

    public boolean is(KeyMapping keyMapping) {
        IPlatformInputHelper inputHelper = Services.PLATFORM.getInputHelper();
        return inputHelper.isActiveAndMatches(keyMapping, this.key);
    }

    public boolean callVanilla(UserInput.MouseOverable mouseOverable, UserInput.MouseClickable mouseClickable) {
        if (this.key.getType() != InputConstants.Type.MOUSE || !mouseOverable.isMouseOver(this.mouseX, this.mouseY)) {
            return false;
        } else {
            return this.isSimulate() ? true : mouseClickable.mouseClicked(this.mouseX, this.mouseY, this.key.getValue());
        }
    }

    public boolean callVanilla(UserInput.KeyPressable keyPressable) {
        if (this.key.getType() == InputConstants.Type.KEYSYM) {
            return this.isSimulate() ? true : keyPressable.keyPressed(this.key.getValue(), 0, this.modifiers);
        } else {
            return false;
        }
    }

    public boolean callVanilla(UserInput.MouseOverable mouseOverable, UserInput.MouseClickable mouseClickable, UserInput.KeyPressable keyPressable) {
        return switch(this.key.getType()) {
            case KEYSYM ->
                this.callVanilla(keyPressable);
            case MOUSE ->
                this.callVanilla(mouseOverable, mouseClickable);
            default ->
                false;
        };
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("clickState", this.clickState).add("key", this.key.getDisplayName().getString()).add("modifiers", this.modifiers).add("mouse", String.format("%s, %s", this.mouseX, this.mouseY)).toString();
    }

    @FunctionalInterface
    public interface KeyPressable {

        boolean keyPressed(int var1, int var2, int var3);
    }

    @FunctionalInterface
    public interface MouseClickable {

        boolean mouseClicked(double var1, double var3, int var5);
    }

    @FunctionalInterface
    public interface MouseOverable {

        boolean isMouseOver(double var1, double var3);
    }
}