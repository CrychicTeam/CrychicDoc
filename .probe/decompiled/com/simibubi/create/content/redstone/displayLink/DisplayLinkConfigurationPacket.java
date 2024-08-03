package com.simibubi.create.content.redstone.displayLink;

import com.simibubi.create.content.redstone.displayLink.source.DisplaySource;
import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class DisplayLinkConfigurationPacket extends BlockEntityConfigurationPacket<DisplayLinkBlockEntity> {

    private CompoundTag configData;

    private int targetLine;

    public DisplayLinkConfigurationPacket(BlockPos pos, CompoundTag configData, int targetLine) {
        super(pos);
        this.configData = configData;
        this.targetLine = targetLine;
    }

    public DisplayLinkConfigurationPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void writeSettings(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.configData);
        buffer.writeInt(this.targetLine);
    }

    @Override
    protected void readSettings(FriendlyByteBuf buffer) {
        this.configData = buffer.readNbt();
        this.targetLine = buffer.readInt();
    }

    protected void applySettings(DisplayLinkBlockEntity be) {
        be.targetLine = this.targetLine;
        if (!this.configData.contains("Id")) {
            be.notifyUpdate();
        } else {
            ResourceLocation id = new ResourceLocation(this.configData.getString("Id"));
            DisplaySource source = AllDisplayBehaviours.getSource(id);
            if (source == null) {
                be.notifyUpdate();
            } else {
                if (be.activeSource != null && be.activeSource == source) {
                    be.getSourceConfig().merge(this.configData);
                } else {
                    be.activeSource = source;
                    be.setSourceConfig(this.configData.copy());
                }
                be.updateGatheredData();
                be.notifyUpdate();
            }
        }
    }
}