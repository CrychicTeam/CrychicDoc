package noppes.npcs.api.wrapper;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IProjectile;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.entity.EntityProjectile;

public class ProjectileWrapper<T extends EntityProjectile> extends ThrowableWrapper<T> implements IProjectile {

    public ProjectileWrapper(T entity) {
        super(entity);
    }

    @Override
    public IItemStack getItem() {
        return NpcAPI.Instance().getIItemStack(this.entity.getItemDisplay());
    }

    @Override
    public void setItem(IItemStack item) {
        if (item == null) {
            this.entity.setThrownItem(ItemStack.EMPTY);
        } else {
            this.entity.setThrownItem(item.getMCItemStack());
        }
    }

    @Override
    public boolean getHasGravity() {
        return this.entity.hasGravity();
    }

    @Override
    public void setHasGravity(boolean bo) {
        this.entity.setHasGravity(bo);
    }

    @Override
    public int getAccuracy() {
        return this.entity.accuracy;
    }

    @Override
    public void setAccuracy(int accuracy) {
        this.entity.accuracy = accuracy;
    }

    @Override
    public void setHeading(IEntity entity) {
        this.setHeading(entity.getX(), entity.getMCEntity().getBoundingBox().minY + (double) (entity.getHeight() / 2.0F), entity.getZ());
    }

    @Override
    public void setHeading(double x, double y, double z) {
        x -= this.entity.m_20185_();
        y -= this.entity.m_20186_();
        z -= this.entity.m_20189_();
        float varF = this.entity.hasGravity() ? (float) Math.sqrt(x * x + z * z) : 0.0F;
        float angle = this.entity.getAngleForXYZ(x, y, z, varF, false);
        float acc = 20.0F - (float) Mth.floor((float) this.entity.accuracy / 5.0F);
        this.entity.shoot(x, y, z, angle, acc);
    }

    @Override
    public void setHeading(float yaw, float pitch) {
        this.entity.f_19859_ = yaw;
        this.entity.f_19860_ = pitch;
        this.entity.m_146922_(yaw);
        this.entity.m_146926_(pitch);
        double varX = (double) (-Mth.sin(yaw / 180.0F * (float) Math.PI) * Mth.cos(pitch / 180.0F * (float) Math.PI));
        double varZ = (double) (Mth.cos(yaw / 180.0F * (float) Math.PI) * Mth.cos(pitch / 180.0F * (float) Math.PI));
        double varY = (double) (-Mth.sin(pitch / 180.0F * (float) Math.PI));
        float acc = 20.0F - (float) Mth.floor((float) this.entity.accuracy / 5.0F);
        this.entity.shoot(varX, varY, varZ, -pitch, acc);
    }

    @Override
    public int getType() {
        return 7;
    }

    @Override
    public boolean typeOf(int type) {
        return type == 7 ? true : super.typeOf(type);
    }

    @Override
    public void enableEvents() {
        if (ScriptContainer.Current == null) {
            throw new CustomNPCsException("Can only be called during scripts");
        } else {
            if (!this.entity.scripts.contains(ScriptContainer.Current)) {
                this.entity.scripts.add(ScriptContainer.Current);
            }
        }
    }
}