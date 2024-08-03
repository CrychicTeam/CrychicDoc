package com.simibubi.create.foundation.blockEntity.behaviour.scrollValue;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;

public class ScrollValueBehaviour extends BlockEntityBehaviour implements ValueSettingsBehaviour {

    public static final BehaviourType<ScrollValueBehaviour> TYPE = new BehaviourType<>();

    ValueBoxTransform slotPositioning;

    Vec3 textShift;

    int min = 0;

    protected int max = 1;

    public int value;

    public Component label;

    Consumer<Integer> callback;

    Consumer<Integer> clientCallback;

    Function<Integer, String> formatter;

    private Supplier<Boolean> isActive;

    boolean needsWrench;

    public ScrollValueBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
        super(be);
        this.setLabel(label);
        this.slotPositioning = slot;
        this.callback = i -> {
        };
        this.clientCallback = i -> {
        };
        this.formatter = i -> Integer.toString(i);
        this.value = 0;
        this.isActive = () -> true;
    }

    @Override
    public boolean isSafeNBT() {
        return true;
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        nbt.putInt("ScrollValue", this.value);
        super.write(nbt, clientPacket);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        this.value = nbt.getInt("ScrollValue");
        super.read(nbt, clientPacket);
    }

    public ScrollValueBehaviour withClientCallback(Consumer<Integer> valueCallback) {
        this.clientCallback = valueCallback;
        return this;
    }

    public ScrollValueBehaviour withCallback(Consumer<Integer> valueCallback) {
        this.callback = valueCallback;
        return this;
    }

    public ScrollValueBehaviour between(int min, int max) {
        this.min = min;
        this.max = max;
        return this;
    }

    public ScrollValueBehaviour requiresWrench() {
        this.needsWrench = true;
        return this;
    }

    public ScrollValueBehaviour withFormatter(Function<Integer, String> formatter) {
        this.formatter = formatter;
        return this;
    }

    public ScrollValueBehaviour onlyActiveWhen(Supplier<Boolean> condition) {
        this.isActive = condition;
        return this;
    }

    public void setValue(int value) {
        value = Mth.clamp(value, this.min, this.max);
        if (value != this.value) {
            this.value = value;
            this.callback.accept(value);
            this.blockEntity.m_6596_();
            this.blockEntity.sendData();
        }
    }

    public int getValue() {
        return this.value;
    }

    public String formatValue() {
        return (String) this.formatter.apply(this.value);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean isActive() {
        return (Boolean) this.isActive.get();
    }

    @Override
    public boolean testHit(Vec3 hit) {
        BlockState state = this.blockEntity.m_58900_();
        Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(this.blockEntity.m_58899_()));
        return this.slotPositioning.testHit(state, localHit);
    }

    public void setLabel(Component label) {
        this.label = label;
    }

    @Override
    public ValueBoxTransform getSlotPositioning() {
        return this.slotPositioning;
    }

    @Override
    public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
        return new ValueSettingsBoard(this.label, this.max, 10, ImmutableList.of(Components.literal("Value")), new ValueSettingsFormatter(ValueSettingsBehaviour.ValueSettings::format));
    }

    @Override
    public void setValueSettings(Player player, ValueSettingsBehaviour.ValueSettings valueSetting, boolean ctrlDown) {
        if (!valueSetting.equals(this.getValueSettings())) {
            this.setValue(valueSetting.value());
            this.playFeedbackSound(this);
        }
    }

    @Override
    public ValueSettingsBehaviour.ValueSettings getValueSettings() {
        return new ValueSettingsBehaviour.ValueSettings(0, this.value);
    }

    @Override
    public boolean onlyVisibleWithWrench() {
        return this.needsWrench;
    }

    @Override
    public void onShortInteract(Player player, InteractionHand hand, Direction side) {
        if (player instanceof FakePlayer) {
            this.blockEntity.m_58900_().m_60664_(this.getWorld(), player, hand, new BlockHitResult(VecHelper.getCenterOf(this.getPos()), side, this.getPos(), true));
        }
    }

    public static class StepContext {

        public int currentValue;

        public boolean forward;

        public boolean shift;

        public boolean control;
    }
}