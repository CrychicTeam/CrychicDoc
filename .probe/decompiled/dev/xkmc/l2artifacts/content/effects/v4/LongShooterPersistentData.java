package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetData;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;

@SerialClass
public class LongShooterPersistentData extends AttributeSetData {

    @SerialField
    public boolean old;
}