package net.minecraft.client.gui.components;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class Button extends AbstractButton {

    public static final int SMALL_WIDTH = 120;

    public static final int DEFAULT_WIDTH = 150;

    public static final int DEFAULT_HEIGHT = 20;

    protected static final Button.CreateNarration DEFAULT_NARRATION = p_253298_ -> (MutableComponent) p_253298_.get();

    protected final Button.OnPress onPress;

    protected final Button.CreateNarration createNarration;

    public static Button.Builder builder(Component component0, Button.OnPress buttonOnPress1) {
        return new Button.Builder(component0, buttonOnPress1);
    }

    protected Button(int int0, int int1, int int2, int int3, Component component4, Button.OnPress buttonOnPress5, Button.CreateNarration buttonCreateNarration6) {
        super(int0, int1, int2, int3, component4);
        this.onPress = buttonOnPress5;
        this.createNarration = buttonCreateNarration6;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return this.createNarration.createNarrationMessage(() -> super.m_5646_());
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        this.m_168802_(narrationElementOutput0);
    }

    public static class Builder {

        private final Component message;

        private final Button.OnPress onPress;

        @Nullable
        private Tooltip tooltip;

        private int x;

        private int y;

        private int width = 150;

        private int height = 20;

        private Button.CreateNarration createNarration = Button.DEFAULT_NARRATION;

        public Builder(Component component0, Button.OnPress buttonOnPress1) {
            this.message = component0;
            this.onPress = buttonOnPress1;
        }

        public Button.Builder pos(int int0, int int1) {
            this.x = int0;
            this.y = int1;
            return this;
        }

        public Button.Builder width(int int0) {
            this.width = int0;
            return this;
        }

        public Button.Builder size(int int0, int int1) {
            this.width = int0;
            this.height = int1;
            return this;
        }

        public Button.Builder bounds(int int0, int int1, int int2, int int3) {
            return this.pos(int0, int1).size(int2, int3);
        }

        public Button.Builder tooltip(@Nullable Tooltip tooltip0) {
            this.tooltip = tooltip0;
            return this;
        }

        public Button.Builder createNarration(Button.CreateNarration buttonCreateNarration0) {
            this.createNarration = buttonCreateNarration0;
            return this;
        }

        public Button build() {
            Button $$0 = new Button(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration);
            $$0.m_257544_(this.tooltip);
            return $$0;
        }
    }

    public interface CreateNarration {

        MutableComponent createNarrationMessage(Supplier<MutableComponent> var1);
    }

    public interface OnPress {

        void onPress(Button var1);
    }
}