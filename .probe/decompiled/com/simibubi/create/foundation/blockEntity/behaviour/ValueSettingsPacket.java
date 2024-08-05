package com.simibubi.create.foundation.blockEntity.behaviour;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;

public class ValueSettingsPacket extends BlockEntityConfigurationPacket<SmartBlockEntity> {

    private int row;

    private int value;

    private InteractionHand interactHand;

    private Direction side;

    private boolean ctrlDown;

    public ValueSettingsPacket(BlockPos pos, int row, int value, @Nullable InteractionHand interactHand, Direction side, boolean ctrlDown) {
        super(pos);
        this.row = row;
        this.value = value;
        this.interactHand = interactHand;
        this.side = side;
        this.ctrlDown = ctrlDown;
    }

    public ValueSettingsPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void writeSettings(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.value);
        buffer.writeVarInt(this.row);
        buffer.writeBoolean(this.interactHand != null);
        if (this.interactHand != null) {
            buffer.writeBoolean(this.interactHand == InteractionHand.MAIN_HAND);
        }
        buffer.writeVarInt(this.side.ordinal());
        buffer.writeBoolean(this.ctrlDown);
    }

    @Override
    protected void readSettings(FriendlyByteBuf buffer) {
        this.value = buffer.readVarInt();
        this.row = buffer.readVarInt();
        if (buffer.readBoolean()) {
            this.interactHand = buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        }
        this.side = Direction.values()[buffer.readVarInt()];
        this.ctrlDown = buffer.readBoolean();
    }

    protected void applySettings(ServerPlayer player, SmartBlockEntity be) {
        for (BlockEntityBehaviour behaviour : be.getAllBehaviours()) {
            if (behaviour instanceof ValueSettingsBehaviour valueSettingsBehaviour && valueSettingsBehaviour.acceptsValueSettings()) {
                if (this.interactHand != null) {
                    valueSettingsBehaviour.onShortInteract(player, this.interactHand, this.side);
                    return;
                }
                valueSettingsBehaviour.setValueSettings(player, new ValueSettingsBehaviour.ValueSettings(this.row, this.value), this.ctrlDown);
                return;
            }
        }
    }

    protected void applySettings(SmartBlockEntity be) {
    }
}