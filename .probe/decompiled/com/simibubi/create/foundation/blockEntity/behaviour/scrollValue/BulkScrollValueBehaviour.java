package com.simibubi.create.foundation.blockEntity.behaviour.scrollValue;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import java.util.List;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class BulkScrollValueBehaviour extends ScrollValueBehaviour {

    Function<SmartBlockEntity, List<? extends SmartBlockEntity>> groupGetter;

    public BulkScrollValueBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot, Function<SmartBlockEntity, List<? extends SmartBlockEntity>> groupGetter) {
        super(label, be, slot);
        this.groupGetter = groupGetter;
    }

    @Override
    public void setValueSettings(Player player, ValueSettingsBehaviour.ValueSettings valueSetting, boolean ctrlDown) {
        if (!ctrlDown) {
            super.setValueSettings(player, valueSetting, ctrlDown);
        } else {
            if (!valueSetting.equals(this.getValueSettings())) {
                this.playFeedbackSound(this);
            }
            for (SmartBlockEntity be : this.getBulk()) {
                ScrollValueBehaviour other = be.getBehaviour(ScrollValueBehaviour.TYPE);
                if (other != null) {
                    other.setValue(valueSetting.value());
                }
            }
        }
    }

    public List<? extends SmartBlockEntity> getBulk() {
        return (List<? extends SmartBlockEntity>) this.groupGetter.apply(this.blockEntity);
    }
}