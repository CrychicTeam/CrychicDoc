package noppes.npcs.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import noppes.npcs.ModelData;
import noppes.npcs.ModelEyeData;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.client.parts.MpmPartData;
import noppes.npcs.constants.EnumParts;

public class EntityCustomNpc extends EntityNPCFlying {

    public ModelData modelData = new ModelData(this);

    public EntityCustomNpc(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("NpcModelData")) {
            this.modelData.load(compound.getCompound("NpcModelData"));
        }
        super.m_7378_(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.put("NpcModelData", this.modelData.save());
    }

    @Override
    public boolean saveAsPassenger(CompoundTag compound) {
        boolean bo = super.m_20086_(compound);
        if (bo) {
            String s = this.m_20078_();
            if (s.equals("minecraft:customnpcs.customnpc")) {
                compound.putString("id", "customnpcs:customnpc");
            }
        }
        return bo;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.isClientSide()) {
            LivingEntity entity = this.modelData.getEntity(this);
            if (entity != null) {
                try {
                    entity.tick();
                } catch (Exception var3) {
                }
                EntityUtil.Copy(this, entity);
            }
        }
        for (MpmPartData pd : this.modelData.mpmParts) {
            if (pd instanceof ModelEyeData) {
                ((ModelEyeData) pd).update(this);
            }
        }
    }

    @Override
    public boolean startRiding(Entity par1Entity, boolean force) {
        boolean b = super.m_7998_(par1Entity, force);
        this.refreshDimensions();
        return b;
    }

    @Override
    public void refreshDimensions() {
        Entity entity = this.modelData.getEntity(this);
        if (entity != null) {
            entity.refreshDimensions();
        }
        super.m_6210_();
    }

    @Override
    public EntityDimensions getDimensions(Pose pos) {
        Entity entity = this.modelData.getEntity(this);
        if (entity == null) {
            float height = 1.9F - this.modelData.getBodyY() + (this.modelData.getPartConfig(EnumParts.HEAD).scaleY - 1.0F) / 2.0F;
            if (this.baseSize.height != height) {
                this.baseSize = new EntityDimensions(this.baseSize.width, height, false);
            }
            return super.m_6972_(pos);
        } else {
            EntityDimensions size = entity.getDimensions(pos);
            if (entity instanceof EntityNPCInterface) {
                return size;
            } else {
                float width = size.width / 5.0F * (float) this.display.getSize();
                float height = size.height / 5.0F * (float) this.display.getSize();
                if (width < 0.1F) {
                    width = 0.1F;
                }
                if (height < 0.1F) {
                    height = 0.1F;
                }
                if (this.display.getHitboxState() == 1 || this.isKilled() && this.stats.hideKilledBody) {
                    width = 1.0E-5F;
                }
                if ((double) (width / 2.0F) > this.m_9236_().getMaxEntityRadius()) {
                    this.m_9236_().increaseMaxEntityRadius((double) (width / 2.0F));
                }
                return new EntityDimensions(width, height, false);
            }
        }
    }
}