package com.rekindled.embers.api.power;

import com.rekindled.embers.entity.EmberPacketEntity;

public interface IEmberPacketReceiver {

    boolean hasRoomFor(double var1);

    boolean onReceive(EmberPacketEntity var1);
}