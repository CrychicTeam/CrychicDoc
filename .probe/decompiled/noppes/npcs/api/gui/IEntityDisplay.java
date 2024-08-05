package noppes.npcs.api.gui;

import noppes.npcs.api.entity.IEntity;

public interface IEntityDisplay extends ICustomGuiComponent {

    IEntity getEntity();

    IEntityDisplay setEntity(IEntity var1);

    int getRotation();

    IEntityDisplay setRotation(int var1);

    float getScale();

    IEntityDisplay setScale(float var1);

    boolean getBackground();

    IEntityDisplay setBackground(boolean var1);
}