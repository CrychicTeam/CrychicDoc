package com.rekindled.embers.api.event;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DialInformationEvent extends UpgradeEvent {

    List<Component> information;

    String dialType;

    public DialInformationEvent(BlockEntity tile, List<Component> information, String dialType) {
        super(tile);
        this.information = information;
        this.dialType = dialType;
    }

    public List<Component> getInformation() {
        return this.information;
    }

    public String getDialType() {
        return this.dialType;
    }
}