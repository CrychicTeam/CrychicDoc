package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.effects.core.SetEffectData;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class AttributeSetData extends SetEffectData {

    @SerialField
    public ArrayList<AttributeSetData.AttributePair> list = new ArrayList();

    @Override
    protected void remove(Player player) {
        for (AttributeSetData.AttributePair attr : this.list) {
            AttributeInstance ins = player.m_21051_(attr.attr());
            if (ins != null) {
                ins.removeModifier(attr.id());
            }
        }
    }

    public static record AttributePair(Attribute attr, UUID id) {
    }
}