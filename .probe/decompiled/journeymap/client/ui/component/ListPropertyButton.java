package journeymap.client.ui.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.api.option.KeyedEnum;
import journeymap.common.properties.config.ConfigField;
import net.minecraft.client.gui.Font;

public class ListPropertyButton<T> extends Button implements IConfigFieldHolder<ConfigField<T>> {

    protected final ConfigField<T> field;

    protected final List<T> values;

    protected final String baseLabel;

    protected final String glyph = "⇕";

    protected final String labelPattern = "%1$s : %2$s %3$s %2$s";

    public ListPropertyButton(Collection<T> values, String label, ConfigField<T> field, net.minecraft.client.gui.components.Button.OnPress pressable) {
        super("", pressable);
        this.field = field;
        this.values = new ArrayList(values);
        this.baseLabel = label;
        this.setValue(field.get());
        this.disabledLabelColor = 4210752;
    }

    public ListPropertyButton(Collection<T> values, String label, ConfigField<T> field) {
        this(values, label, field, emptyPressable());
    }

    public void setValue(T value) {
        if (!this.field.get().equals(value)) {
            this.field.set(value);
            this.field.save();
        }
        String label;
        if (value instanceof KeyedEnum) {
            label = Constants.getString(((KeyedEnum) value).getKey());
        } else {
            label = Constants.getString(value.toString());
        }
        this.m_93666_(Constants.getStringTextComponent(this.getFormattedLabel(label)));
    }

    public ConfigField<T> getField() {
        return this.field;
    }

    public void nextOption() {
        int index = this.values.indexOf(this.field.get()) + 1;
        if (index == this.values.size()) {
            index = 0;
        }
        this.setValue((T) this.values.get(index));
    }

    public void prevOption() {
        int index = this.values.indexOf(this.field.get()) - 1;
        if (index == -1) {
            index = this.values.size() - 1;
        }
        this.setValue((T) this.values.get(index));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.mouseOver(mouseX, mouseY)) {
            if (button == 0) {
                this.nextOption();
            } else if (button == 1) {
                this.prevOption();
                super.f_93717_.onPress(this);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected String getFormattedLabel(String value) {
        return String.format("%1$s : %2$s %3$s %2$s", this.baseLabel, "⇕", value);
    }

    @Override
    public int getFitWidth(Font fr) {
        int max = fr.width(this.m_6035_().getString());
        for (T value : this.values) {
            max = Math.max(max, fr.width(this.getFormattedLabel(value.toString())));
        }
        return max + this.WIDTH_PAD;
    }

    public boolean keyTyped(char c, int i) {
        if (this.isMouseOver()) {
            if (i == 263 || i == 264 || i == 45) {
                this.prevOption();
                return true;
            }
            if (i == 262 || i == 265 || i == 61) {
                this.nextOption();
                return true;
            }
        }
        return false;
    }

    @Override
    public void refresh() {
        this.setValue(this.field.get());
    }

    @Override
    public ConfigField<T> getConfigField() {
        return this.field;
    }
}