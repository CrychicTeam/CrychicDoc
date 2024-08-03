package noppes.npcs.api.wrapper.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.function.gui.GuiComponentUpdate;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.gui.ISlider;

public class CustomGuiSliderWrapper extends CustomGuiComponentWrapper implements ISlider {

    private String format = "%s%%";

    private float min = 0.0F;

    private float max = 100.0F;

    private int decimals = 0;

    private float value = 100.0F;

    private GuiComponentUpdate<ISlider> onChange = null;

    public CustomGuiSliderWrapper() {
    }

    public CustomGuiSliderWrapper(int id, String format, int x, int y, int width, int height) {
        this.setID(id);
        if (!format.isEmpty()) {
            this.setFormat(format);
        }
        this.setPos(x, y);
        this.setSize(width, height);
    }

    @Override
    public float getValue() {
        return this.value;
    }

    public CustomGuiSliderWrapper setValue(float value) {
        BigDecimal bd = new BigDecimal((double) value);
        this.value = bd.setScale(this.decimals, RoundingMode.FLOOR).floatValue();
        return this;
    }

    @Override
    public String getFormat() {
        return this.format;
    }

    public CustomGuiSliderWrapper setFormat(String format) {
        this.format = format;
        return this;
    }

    @Override
    public float getMin() {
        return this.min;
    }

    public CustomGuiSliderWrapper setMin(float min) {
        this.min = min;
        return this;
    }

    @Override
    public float getMax() {
        return this.max;
    }

    public CustomGuiSliderWrapper setMax(float max) {
        this.max = max;
        return this;
    }

    @Override
    public int getDecimals() {
        return this.decimals;
    }

    public CustomGuiSliderWrapper setDecimals(int decimals) {
        if (decimals < 0) {
            throw new CustomNPCsException("Decimals cant be lower then 0");
        } else {
            this.decimals = decimals;
            return this;
        }
    }

    @Override
    public int getType() {
        return 8;
    }

    @Override
    public CompoundTag toNBT(CompoundTag compound) {
        super.toNBT(compound);
        compound.putString("format", this.format);
        compound.putInt("decimals", this.decimals);
        compound.putFloat("min", this.min);
        compound.putFloat("max", this.max);
        compound.putFloat("value", this.value);
        return compound;
    }

    @Override
    public CustomGuiComponentWrapper fromNBT(CompoundTag compound) {
        super.fromNBT(compound);
        this.setFormat(compound.getString("format"));
        this.setDecimals(compound.getInt("decimals"));
        this.setMin(compound.getFloat("min"));
        this.setMax(compound.getFloat("max"));
        this.setValue(compound.getFloat("value"));
        return this;
    }

    public CustomGuiSliderWrapper setOnChange(GuiComponentUpdate<ISlider> onChange) {
        this.onChange = onChange;
        return this;
    }

    public final void onChange(ICustomGui gui) {
        if (this.onChange != null) {
            this.onChange.onChange(gui, this);
        }
    }
}