package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.effects.core.SetEffectData;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;

@SerialClass
public class ImmobileData extends SetEffectData {

    @SerialField
    public double x;

    @SerialField
    public double y;

    @SerialField
    public double z;

    @SerialField
    public int time;
}