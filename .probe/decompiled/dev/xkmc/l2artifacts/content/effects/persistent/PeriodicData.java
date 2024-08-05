package dev.xkmc.l2artifacts.content.effects.persistent;

import dev.xkmc.l2artifacts.content.effects.core.SetEffectData;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;

@SerialClass
public class PeriodicData extends SetEffectData {

    @SerialField
    public int tick_count;
}