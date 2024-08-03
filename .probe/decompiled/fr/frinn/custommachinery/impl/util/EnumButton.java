package fr.frinn.custommachinery.impl.util;

import java.util.List;
import java.util.function.Function;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class EnumButton<E> extends Button {

    private final Function<E, Component> messageFunction;

    private final List<E> values;

    private E value;

    public EnumButton(int x, int y, int width, int height, Button.OnPress pressedAction, Function<E, Component> messageFunction, List<E> values, E defaultValue) {
        super(x, y, width, height, Component.empty(), pressedAction, s -> Component.empty());
        this.messageFunction = messageFunction;
        this.values = values;
        if (this.values.contains(defaultValue)) {
            this.value = defaultValue;
        } else {
            this.value = (E) this.values.get(0);
        }
    }

    @Override
    public void onPress() {
        this.nextValue();
        super.onPress();
    }

    @Override
    public Component getMessage() {
        return (Component) this.messageFunction.apply(this.value);
    }

    public E getValue() {
        return this.value;
    }

    private void nextValue() {
        int index = this.values.indexOf(this.value);
        if (index < this.values.size() - 1) {
            index++;
        } else {
            index = 0;
        }
        this.value = (E) this.values.get(index);
    }
}