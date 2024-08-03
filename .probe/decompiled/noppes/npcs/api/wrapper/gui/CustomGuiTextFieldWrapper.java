package noppes.npcs.api.wrapper.gui;

import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.function.gui.GuiComponentUpdate;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.gui.ITextField;

public class CustomGuiTextFieldWrapper extends CustomGuiComponentWrapper implements ITextField {

    private int color = 14737632;

    private int type = 0;

    private String text = "";

    private boolean focused = false;

    private GuiComponentUpdate<ITextField> onChange = null;

    private GuiComponentUpdate<ITextField> onFocusLost = null;

    private int min = Integer.MIN_VALUE;

    private int max = Integer.MAX_VALUE;

    public CustomGuiTextFieldWrapper() {
    }

    public CustomGuiTextFieldWrapper(int id, int x, int y, int width, int height) {
        this.setID(id);
        this.setPos(x, y);
        this.setSize(width, height);
    }

    @Override
    public String getText() {
        return this.text;
    }

    public CustomGuiTextFieldWrapper setText(String text) {
        this.text = (String) Objects.requireNonNull(text, "");
        if (!this.text.isEmpty() && (this.getCharacterType() == 1 || this.getCharacterType() == 2)) {
            this.setInteger(this.getInteger());
        }
        return this;
    }

    @Override
    public int getInteger() {
        if (this.type == 0) {
            throw new CustomNPCsException("Character Type 0 doesnt convert to integer");
        } else if (this.text.isEmpty()) {
            return Math.max(this.min, 0);
        } else {
            return this.type == 1 ? Integer.parseInt(this.text) : Integer.parseInt(this.text, 16);
        }
    }

    public CustomGuiTextFieldWrapper setInteger(int i) {
        if (this.type == 0) {
            throw new CustomNPCsException("Character Type 0 doesnt support setInteger");
        } else {
            i = Math.max(this.min, i);
            i = Math.min(this.max, i);
            if (this.type == 1 || this.type == 3) {
                this.text = i + "";
            }
            if (this.type == 2) {
                this.text = String.format("%01x", i);
            }
            return this;
        }
    }

    @Override
    public float getFloat() {
        if (this.type == 0) {
            throw new CustomNPCsException("Character Type 0 doesnt convert to float");
        } else if (this.text.isEmpty()) {
            return (float) Math.max(this.min, 0);
        } else if (this.type == 1) {
            return (float) Integer.parseInt(this.text);
        } else {
            return this.type == 2 ? (float) Integer.parseInt(this.text, 16) : Float.parseFloat(this.text);
        }
    }

    public CustomGuiTextFieldWrapper setFloat(float f) {
        if (this.type != 0 && this.type != 2) {
            f = Math.max((float) this.min, f);
            f = Math.min((float) this.max, f);
            if (this.type == 1) {
                this.text = f + "";
            }
            return this;
        } else {
            throw new CustomNPCsException("Character Type 0 doesnt support setFloat");
        }
    }

    @Override
    public int getColor() {
        return this.color;
    }

    public CustomGuiTextFieldWrapper setColor(int color) {
        this.color = color;
        return this;
    }

    public CustomGuiTextFieldWrapper setFocused(boolean bo) {
        this.focused = bo;
        return this;
    }

    @Override
    public boolean getFocused() {
        return this.focused;
    }

    public CustomGuiTextFieldWrapper setCharacterType(int type) {
        this.type = type;
        return this;
    }

    @Override
    public int getCharacterType() {
        return this.type;
    }

    public CustomGuiTextFieldWrapper setMinMax(int min, int max) {
        if (this.type == 0) {
            throw new CustomNPCsException("Character Type 0 doesnt support setInteger");
        } else {
            this.min = min;
            this.max = max;
            return this;
        }
    }

    @Override
    public int getType() {
        return 3;
    }

    @Override
    public CompoundTag toNBT(CompoundTag nbt) {
        super.toNBT(nbt);
        nbt.putString("default", this.text);
        nbt.putBoolean("focused", this.focused);
        nbt.putInt("color", this.color);
        nbt.putInt("character_type", this.type);
        nbt.putInt("min", this.min);
        nbt.putInt("max", this.max);
        return nbt;
    }

    @Override
    public CustomGuiComponentWrapper fromNBT(CompoundTag nbt) {
        super.fromNBT(nbt);
        this.setText(nbt.getString("default"));
        this.setFocused(nbt.getBoolean("focused"));
        this.setColor(nbt.getInt("color"));
        this.setCharacterType(nbt.getInt("character_type"));
        this.min = nbt.getInt("min");
        this.max = nbt.getInt("max");
        return this;
    }

    public CustomGuiTextFieldWrapper setOnChange(GuiComponentUpdate<ITextField> onChange) {
        this.onChange = onChange;
        return this;
    }

    public CustomGuiTextFieldWrapper setOnFocusLost(GuiComponentUpdate<ITextField> onFocusChange) {
        this.onFocusLost = onFocusChange;
        return this;
    }

    public final void onChange(ICustomGui gui) {
        if (this.onChange != null) {
            this.onChange.onChange(gui, this);
        }
    }

    public final void onFocusLost(ICustomGui gui) {
        if (this.onFocusLost != null) {
            this.onFocusLost.onChange(gui, this);
        }
    }
}