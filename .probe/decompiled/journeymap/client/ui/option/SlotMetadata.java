package journeymap.client.ui.option;

import java.util.ArrayList;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.IConfigFieldHolder;
import journeymap.client.ui.component.IntSliderButton;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.config.ConfigField;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

public class SlotMetadata<T> implements Comparable<SlotMetadata> {

    protected final Button button;

    protected final String range;

    protected final T defaultValue;

    protected final SlotMetadata.ValueType valueType;

    protected String name;

    protected String tooltip;

    protected boolean advanced;

    protected List<FormattedCharSequence> tooltipLines;

    protected List valueList;

    protected boolean master;

    protected int order;

    public SlotMetadata(Button button) {
        this(button, false);
    }

    public SlotMetadata(Button button, int order) {
        this(button, false);
        this.order = order;
    }

    public SlotMetadata(Button button, boolean advanced) {
        this(button, button.getDisplayString(), button.getUnformattedTooltip(), null, null, advanced);
    }

    public SlotMetadata(Button button, String name, String tooltip, boolean advanced) {
        this(button, name, tooltip, null, null, advanced);
    }

    public SlotMetadata(Button button, String name, String tooltip) {
        this(button, name, tooltip, null, null, false);
    }

    public SlotMetadata(Button button, String name, String tooltip, int order) {
        this(button, name, tooltip, null, null, false);
        this.order = order;
    }

    public SlotMetadata(Button button, String name, String tooltip, String range, T defaultValue, boolean advanced) {
        this.button = button;
        this.name = name;
        this.tooltip = tooltip;
        this.range = range;
        this.defaultValue = defaultValue;
        this.advanced = advanced;
        if (defaultValue == null && range == null && !advanced) {
            this.valueType = SlotMetadata.ValueType.Toolbar;
        } else if (defaultValue instanceof Boolean) {
            this.valueType = SlotMetadata.ValueType.Boolean;
        } else if (defaultValue instanceof Integer) {
            this.valueType = SlotMetadata.ValueType.Integer;
        } else {
            this.valueType = SlotMetadata.ValueType.Set;
        }
    }

    public boolean isMasterPropertyForCategory() {
        return this.master;
    }

    public void setMasterPropertyForCategory(boolean master) {
        this.master = master;
    }

    public Button getButton() {
        return this.button;
    }

    public String getName() {
        return this.name;
    }

    public String getRange() {
        return this.range;
    }

    public boolean isAdvanced() {
        return this.advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public SlotMetadata.ValueType getValueType() {
        return this.valueType;
    }

    public List<FormattedCharSequence> getTooltipLines() {
        return this.tooltipLines;
    }

    public boolean isMaster() {
        return this.master;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public boolean isToolbar() {
        return this.valueType == SlotMetadata.ValueType.Toolbar;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List getValueList() {
        return this.valueList;
    }

    public void setValueList(List valueList) {
        this.valueList = valueList;
    }

    public void updateFromButton() {
        if (this.button != null) {
            this.name = this.button.getDisplayString();
            this.tooltip = this.button.getUnformattedTooltip();
            this.tooltipLines = null;
        }
    }

    public List<FormattedCharSequence> getTooltip() {
        if (this.tooltipLines == null) {
            ArrayList<FormattedCharSequence> lines = new ArrayList(4);
            if (this.tooltip != null || this.range != null || this.defaultValue != null || this.advanced) {
                ChatFormatting nameColor = this.isToolbar() ? ChatFormatting.GREEN : (this.advanced ? ChatFormatting.RED : ChatFormatting.AQUA);
                String formattedName = Component.translatable("jm.config.tooltip_format", this.name).getString();
                lines.add(FormattedCharSequence.forward(formattedName, Style.EMPTY.applyFormat(nameColor)));
                if (this.tooltip != null) {
                    lines.addAll(this.getWordWrappedLines(this.tooltip, Style.EMPTY.applyFormat(ChatFormatting.YELLOW)));
                }
                if (this.button != null && this.button instanceof IntSliderButton) {
                    Style style = Style.EMPTY.applyFormats(ChatFormatting.GRAY, ChatFormatting.ITALIC);
                    lines.addAll(this.getWordWrappedLines(Constants.getString("jm.config.control_arrowkeys"), style));
                }
                if (this.range != null) {
                    String formattedRange = Component.translatable("jm.config.tooltip_format", this.range).getString();
                    lines.add(FormattedCharSequence.forward(formattedRange, Style.EMPTY.applyFormat(ChatFormatting.WHITE)));
                }
            }
            if (!lines.isEmpty()) {
                return lines;
            }
        }
        return this.tooltipLines;
    }

    protected List<FormattedCharSequence> getWordWrappedLines(String original, Style style) {
        Font fontRenderer = Minecraft.getInstance().font;
        List<FormattedCharSequence> list = new ArrayList();
        int max = fontRenderer.isBidirectional() ? 170 : 250;
        for (FormattedText line : fontRenderer.getSplitter().splitLines(Constants.getStringTextComponent(original), max, style)) {
            list.add(FormattedCharSequence.forward(Component.translatable("jm.config.tooltip_format", line.getString()).getString(), style));
        }
        return list;
    }

    public void resetToDefaultValue() {
        if (this.button != null) {
            if (this.button instanceof IConfigFieldHolder) {
                try {
                    ConfigField configField = ((IConfigFieldHolder) this.button).getConfigField();
                    if (configField != null) {
                        configField.setToDefault();
                    }
                } catch (Exception var2) {
                    var2.printStackTrace();
                }
            }
            this.button.refresh();
        }
    }

    public boolean hasConfigField() {
        return this.button != null && this.button instanceof IConfigFieldHolder && ((IConfigFieldHolder) this.button).getConfigField() != null;
    }

    public PropertiesBase getProperties() {
        return this.hasConfigField() ? ((IConfigFieldHolder) this.button).getConfigField().getOwner() : null;
    }

    public int compareTo(SlotMetadata other) {
        int result = Boolean.compare(this.isToolbar(), other.isToolbar());
        if (result == 0) {
            result = Integer.compare(this.order, other.order);
        }
        if (result == 0) {
            result = Boolean.compare(other.master, this.master);
        }
        if (result == 0) {
            result = this.valueType.compareTo(other.valueType);
        }
        if (result == 0) {
            result = this.name.compareTo(other.name);
        }
        return result;
    }

    public static enum ValueType {

        Boolean, Set, Integer, Toolbar
    }
}