package journeymap.client.ui.component;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.api.option.KeyedEnum;
import journeymap.common.properties.config.ConfigField;
import net.minecraft.client.gui.Font;

public class PropertyDropdownButton<T> extends DropDownButton implements IConfigFieldHolder<ConfigField<T>> {

    protected final ConfigField<T> field;

    protected final String baseLabel;

    protected final String glyph = "⇕";

    protected final String labelPattern = "%1$s : %2$s %3$s %2$s";

    private final Collection<T> values;

    protected net.minecraft.client.gui.components.Button.OnPress pressable;

    public PropertyDropdownButton(Collection<T> values, String label, ConfigField<T> field, net.minecraft.client.gui.components.Button.OnPress pressable) {
        super("", pressable);
        this.pressable = pressable;
        this.field = field;
        this.baseLabel = label;
        this.values = values;
        this.setValue(field.get());
        this.disabledLabelColor = 4210752;
        List<DropDownItem> items = this.setItems(values);
        super.setItems(items);
        this.setRenderDecorations(true);
        this.setRenderSolidBackground(true);
    }

    public PropertyDropdownButton(Collection<T> values, String label, ConfigField<T> field) {
        this(values, label, field, emptyPressable());
    }

    protected List<DropDownItem> setItems(Collection<T> values) {
        List<DropDownItem> items = Lists.newArrayList();
        values.forEach(value -> {
            String label;
            if (value instanceof KeyedEnum) {
                label = Constants.getString(((KeyedEnum) value).getKey());
            } else {
                label = Constants.getString(value.toString());
            }
            items.add(new DropDownItem(this, value, label));
        });
        return items;
    }

    @Override
    public void setSelected(DropDownItem selected) {
        this.selected = selected;
        this.setValue((T) selected.getId());
        this.onPress.onPress(selected);
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

    @Override
    public int getFitWidth(Font fr) {
        int max = fr.width(this.m_6035_().getString());
        for (DropDownItem item : this.items) {
            max = Math.max(max, fr.width(this.getFormattedLabel(item.getLabel())));
        }
        return max + this.buttonBuffer;
    }

    @Override
    public int getWidth() {
        return this.f_93618_;
    }

    @Override
    public void setWidth(int width) {
        if (this.paneScreen != null) {
            this.paneScreen.setPaneWidth(width);
        }
        super.m_93674_(width);
    }

    @Override
    protected String getLabel(DropDownItem item) {
        return this.getFormattedLabel(item.getLabel());
    }

    protected String getFormattedLabel(String value) {
        return String.format("%1$s : %2$s %3$s %2$s", this.baseLabel, "⇕", value);
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