package noppes.npcs.api.wrapper.gui;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.api.INbt;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.gui.IEntityDisplay;
import noppes.npcs.api.wrapper.NBTWrapper;

public class CustomGuiEntityDisplayWrapper extends CustomGuiComponentWrapper implements IEntityDisplay {

    private IEntity entity;

    private INbt entityData = new NBTWrapper(new CompoundTag());

    private int rotation;

    private float scale = 1.0F;

    private boolean showBackground = true;

    private float offsetX = 0.0F;

    private float offsetY = 0.0F;

    public CustomGuiEntityDisplayWrapper() {
    }

    public CustomGuiEntityDisplayWrapper(int id, IEntity entity, int x, int y) {
        this.setID(id);
        this.setEntity(entity);
        this.setPos(x, y);
    }

    @Override
    public IEntity getEntity() {
        return this.entity;
    }

    public INbt getEntityData() {
        return this.entityData;
    }

    @Override
    public IEntityDisplay setEntity(IEntity entity) {
        this.entity = entity;
        if (entity == null) {
            this.entityData = new NBTWrapper(new CompoundTag());
        } else {
            this.entityData = entity.getEntityNbt();
        }
        return this;
    }

    @Override
    public int getRotation() {
        return this.rotation;
    }

    @Override
    public IEntityDisplay setRotation(int rotation) {
        this.rotation = rotation;
        return this;
    }

    @Override
    public float getScale() {
        return this.scale;
    }

    @Override
    public IEntityDisplay setScale(float scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public boolean getBackground() {
        return this.showBackground;
    }

    @Override
    public IEntityDisplay setBackground(boolean bo) {
        this.showBackground = bo;
        return this;
    }

    @Override
    public int getType() {
        return 9;
    }

    @Override
    public CompoundTag toNBT(CompoundTag compound) {
        super.toNBT(compound);
        compound.put("entity", this.entityData.getMCNBT());
        compound.putInt("rotation", this.rotation);
        compound.putFloat("scale", this.scale);
        compound.putBoolean("background", this.showBackground);
        return compound;
    }

    @Override
    public CustomGuiComponentWrapper fromNBT(CompoundTag compound) {
        super.fromNBT(compound);
        this.entityData = NpcAPI.Instance().getINbt(compound.getCompound("entity"));
        this.setRotation(compound.getInt("rotation"));
        this.setScale(compound.getFloat("scale"));
        this.setBackground(compound.getBoolean("background"));
        return this;
    }
}