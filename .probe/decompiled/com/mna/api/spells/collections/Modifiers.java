package com.mna.api.spells.collections;

import com.mna.api.spells.parts.Modifier;
import net.minecraftforge.registries.ObjectHolder;

public class Modifiers {

    @ObjectHolder(registryName = "mna:modifiers", value = "mna:modifiers/damage")
    public static final Modifier DAMAGE = null;

    @ObjectHolder(registryName = "mna:modifiers", value = "mna:modifiers/range")
    public static final Modifier RANGE = null;

    @ObjectHolder(registryName = "mna:modifiers", value = "mna:modifiers/duration")
    public static final Modifier DURATION = null;

    @ObjectHolder(registryName = "mna:modifiers", value = "mna:modifiers/speed")
    public static final Modifier SPEED = null;

    @ObjectHolder(registryName = "mna:modifiers", value = "mna:modifiers/radius")
    public static final Modifier RADIUS = null;

    @ObjectHolder(registryName = "mna:modifiers", value = "mna:modifiers/magnitude")
    public static final Modifier MAGNITUDE = null;

    @ObjectHolder(registryName = "mna:modifiers", value = "mna:modifiers/delay")
    public static final Modifier DELAY = null;

    @ObjectHolder(registryName = "mna:modifiers", value = "mna:modifiers/precision")
    public static final Modifier PRECISION = null;
}