package com.simibubi.create.content.processing.recipe;

import com.simibubi.create.Create;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.utility.Lang;

public enum HeatCondition {

    NONE(16777215), HEATED(15237888), SUPERHEATED(6067176);

    private int color;

    private HeatCondition(int color) {
        this.color = color;
    }

    public boolean testBlazeBurner(BlazeBurnerBlock.HeatLevel level) {
        if (this == SUPERHEATED) {
            return level == BlazeBurnerBlock.HeatLevel.SEETHING;
        } else {
            return this != HEATED ? true : level != BlazeBurnerBlock.HeatLevel.NONE && level != BlazeBurnerBlock.HeatLevel.SMOULDERING;
        }
    }

    public BlazeBurnerBlock.HeatLevel visualizeAsBlazeBurner() {
        if (this == SUPERHEATED) {
            return BlazeBurnerBlock.HeatLevel.SEETHING;
        } else {
            return this == HEATED ? BlazeBurnerBlock.HeatLevel.KINDLED : BlazeBurnerBlock.HeatLevel.NONE;
        }
    }

    public String serialize() {
        return Lang.asId(this.name());
    }

    public String getTranslationKey() {
        return "recipe.heat_requirement." + this.serialize();
    }

    public static HeatCondition deserialize(String name) {
        for (HeatCondition heatCondition : values()) {
            if (heatCondition.serialize().equals(name)) {
                return heatCondition;
            }
        }
        Create.LOGGER.warn("Tried to deserialize invalid heat condition: \"" + name + "\"");
        return NONE;
    }

    public int getColor() {
        return this.color;
    }
}