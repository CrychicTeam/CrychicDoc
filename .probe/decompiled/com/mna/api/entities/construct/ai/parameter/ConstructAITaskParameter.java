package com.mna.api.entities.construct.ai.parameter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public abstract class ConstructAITaskParameter {

    private final ConstructParameterTypes _type;

    private final Component _tooltip;

    private String _id;

    public ConstructAITaskParameter(String id, ConstructParameterTypes typeID) {
        this._type = typeID;
        this._tooltip = Component.translatable("gui.mna.parameter." + id);
        this._id = id;
    }

    public void loadData(CompoundTag nbt) {
        this._id = nbt.getString("parameter_id");
    }

    public CompoundTag saveData() {
        CompoundTag tag = new CompoundTag();
        tag.putString("parameter_id", this._id);
        return tag;
    }

    public final ConstructParameterTypes getTypeID() {
        return this._type;
    }

    public final Component getTooltip() {
        return this._tooltip;
    }

    public final String getId() {
        return this._id;
    }
}