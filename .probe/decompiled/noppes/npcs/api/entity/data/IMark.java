package noppes.npcs.api.entity.data;

import noppes.npcs.api.handler.data.IAvailability;

public interface IMark {

    IAvailability getAvailability();

    int getColor();

    void setColor(int var1);

    int getType();

    void setType(int var1);

    void update();
}