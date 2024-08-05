package com.simibubi.create.content.redstone.diodes;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BrassDiodeScrollValueBehaviour extends ScrollValueBehaviour {

    public BrassDiodeScrollValueBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
        super(label, be, slot);
    }

    @Override
    public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
        return new ValueSettingsBoard(this.label, 60, 10, Lang.translatedOptions("generic.unit", "ticks", "seconds", "minutes"), new ValueSettingsFormatter(this::formatSettings));
    }

    @Override
    public void onShortInteract(Player player, InteractionHand hand, Direction side) {
        BlockState blockState = this.blockEntity.m_58900_();
        if (blockState.m_60734_() instanceof BrassDiodeBlock bdb) {
            bdb.toggle(this.getWorld(), this.getPos(), blockState, player, hand);
        }
    }

    @Override
    public void setValueSettings(Player player, ValueSettingsBehaviour.ValueSettings valueSetting, boolean ctrlHeld) {
        int value = valueSetting.value();
        int multiplier = switch(valueSetting.row()) {
            case 0 ->
                1;
            case 1 ->
                20;
            default ->
                1200;
        };
        if (!valueSetting.equals(this.getValueSettings())) {
            this.playFeedbackSound(this);
        }
        this.setValue(Math.max(2, Math.max(1, value) * multiplier));
    }

    @Override
    public ValueSettingsBehaviour.ValueSettings getValueSettings() {
        int row = 0;
        int value = this.value;
        if (value > 1200) {
            value /= 1200;
            row = 2;
        } else if (value > 60) {
            value /= 20;
            row = 1;
        }
        return new ValueSettingsBehaviour.ValueSettings(row, value);
    }

    public MutableComponent formatSettings(ValueSettingsBehaviour.ValueSettings settings) {
        int value = Math.max(1, settings.value());
        return Components.literal(switch(settings.row()) {
            case 0 ->
                Math.max(2, value) + "t";
            case 1 ->
                "0:" + (value < 10 ? "0" : "") + value;
            default ->
                value + ":00";
        });
    }

    @Override
    public String getClipboardKey() {
        return "Timings";
    }
}