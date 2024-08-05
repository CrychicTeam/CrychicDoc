package noppes.npcs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.client.parts.MpmPart;
import noppes.npcs.client.parts.MpmPartData;
import noppes.npcs.constants.BodyPart;
import noppes.npcs.constants.EnumParts;

public abstract class ModelDataShared {

    public ModelPartConfig arm1 = new ModelPartConfig();

    public ModelPartConfig arm2 = new ModelPartConfig();

    public ModelPartConfig body = new ModelPartConfig();

    public ModelPartConfig leg1 = new ModelPartConfig();

    public ModelPartConfig leg2 = new ModelPartConfig();

    public ModelPartConfig head = new ModelPartConfig();

    protected ResourceLocation entityName = null;

    protected LivingEntity entity;

    public CompoundTag extra = new CompoundTag();

    public ListTag oldPartData = new ListTag();

    public List<MpmPartData> mpmParts = new ArrayList();

    public List<BodyPart> hiddenParts = new ArrayList();

    public int wingMode = 0;

    public String url = "";

    public String displayName = "";

    public long lastEdited = System.currentTimeMillis();

    public int inLove = 0;

    public int animationTime = -1;

    public int modelType = 0;

    public int moveAnimation = 16;

    public int prevMoveAnimation = 16;

    public boolean startMoveAnimation = false;

    public int animation = 0;

    public int prevAnimation = 0;

    public boolean startAnimation = false;

    public int animationStart = 0;

    public float sleepRotation;

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        if (this.entityName != null) {
            compound.putString("EntityName", this.entityName.toString());
        }
        compound.put("ArmsConfig", this.arm1.writeToNBT());
        compound.put("Arms2Config", this.arm2.writeToNBT());
        compound.put("BodyConfig", this.body.writeToNBT());
        compound.put("LegsConfig", this.leg1.writeToNBT());
        compound.put("Legs2Config", this.leg2.writeToNBT());
        compound.put("HeadConfig", this.head.writeToNBT());
        compound.put("ExtraData", this.extra);
        compound.putInt("WingMode", this.wingMode);
        compound.putString("CustomSkinUrl", this.url);
        compound.putString("DisplayName", this.displayName);
        compound.putInt("Animation", this.animation);
        compound.putInt("MoveAnimation", this.moveAnimation);
        compound.putInt("ModelType", this.modelType);
        compound.putLong("LastEdited", this.lastEdited);
        compound.put("Parts", this.oldPartData);
        ListTag list = new ListTag();
        for (MpmPartData e : this.mpmParts) {
            list.add(e.getNbt());
        }
        compound.put("NewParts", list);
        return compound;
    }

    public void load(CompoundTag compound) {
        String rl = compound.getString("EntityName");
        this.setEntity(rl.isEmpty() ? null : new ResourceLocation(rl));
        this.arm1.readFromNBT(compound.getCompound("ArmsConfig"));
        this.arm2.readFromNBT(compound.getCompound("Arms2Config"));
        this.body.readFromNBT(compound.getCompound("BodyConfig"));
        this.leg1.readFromNBT(compound.getCompound("LegsConfig"));
        this.leg2.readFromNBT(compound.getCompound("Legs2Config"));
        this.head.readFromNBT(compound.getCompound("HeadConfig"));
        this.extra = compound.getCompound("ExtraData");
        this.wingMode = compound.getInt("WingMode");
        this.url = compound.getString("CustomSkinUrl");
        this.displayName = compound.getString("DisplayName");
        this.animation = compound.getInt("Animation");
        this.moveAnimation = compound.getInt("MoveAnimation");
        this.modelType = compound.getInt("ModelType");
        this.lastEdited = compound.getLong("LastEdited");
        List<MpmPartData> mpmParts = new ArrayList();
        ListTag list = compound.getList("NewParts", 10);
        for (int i = 0; i < list.size(); i++) {
            MpmPartData part = new MpmPartData();
            part.setNbt(list.getCompound(i));
            if (part.partId.equals(ModelEyeData.RESOURCE) || part.partId.equals(ModelEyeData.RESOURCE_RIGHT) || part.partId.equals(ModelEyeData.RESOURCE_LEFT)) {
                part = new ModelEyeData();
                part.setNbt(list.getCompound(i));
            }
            mpmParts.add(part);
        }
        this.mpmParts = mpmParts;
        this.oldPartData = compound.getList("Parts", 10);
        if (this.mpmParts.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                this.mpmParts.add(EnumParts.convertOldPart(list.getCompound(i)));
            }
        }
        this.refreshParts();
        this.updateTransate();
    }

    public void setMoveAnimation(int ani) {
        this.startMoveAnimation = this.moveAnimation != ani;
        this.moveAnimation = ani;
    }

    public int getMoveAnimtion(LivingEntity player) {
        if (player.m_20159_()) {
            return 1;
        } else if (player.isSleeping()) {
            return 2;
        } else {
            return this.moveAnimation == 16 && player.m_6047_() ? 4 : this.moveAnimation;
        }
    }

    public boolean isMovementAnimation(int ani) {
        return ani == 2 || ani == 7 || ani == 4 || ani == 1 || ani == 14 || ani == 15 || ani == 16 || ani == 18 || ani == 17;
    }

    public void setAnimation(int ani) {
        if (this.isMovementAnimation(ani)) {
            this.setMoveAnimation(ani);
        } else {
            this.animationTime = -1;
            this.animation = ani;
            this.lastEdited = System.currentTimeMillis();
            this.startAnimation = this.animation != ani;
            if (this.animation == 10) {
                this.animationTime = 80;
            }
            if (this.animation == 13 || this.animation == 12) {
                this.animationTime = 60;
            }
            if (this.getOwner() != null && ani != 0) {
                this.animationStart = this.getOwner().f_19797_;
            } else {
                this.animationStart = -1;
            }
        }
    }

    public void updateTransate() {
        for (EnumParts part : EnumParts.values()) {
            ModelPartConfig config = this.getPartConfig(part);
            if (config != null) {
                if (part == EnumParts.HEAD) {
                    config.setTranslate(0.0F, this.getBodyY(), 0.0F);
                } else if (part == EnumParts.ARM_LEFT) {
                    ModelPartConfig body = this.getPartConfig(EnumParts.BODY);
                    float x = (1.0F - body.scaleX) * 0.25F + (1.0F - config.scaleX) * 0.0625F;
                    float y = this.getBodyY() + (1.0F - config.scaleY) * -0.125F;
                    config.setTranslate(-x, y, 0.0F);
                    if (!config.notShared) {
                        ModelPartConfig arm = this.getPartConfig(EnumParts.ARM_RIGHT);
                        arm.copyValues(config);
                    }
                } else if (part == EnumParts.ARM_RIGHT) {
                    ModelPartConfig body = this.getPartConfig(EnumParts.BODY);
                    float x = (1.0F - body.scaleX) * 0.25F + (1.0F - config.scaleX) * 0.0625F;
                    float y = this.getBodyY() + (1.0F - config.scaleY) * -0.125F;
                    config.setTranslate(x, y, 0.0F);
                } else if (part == EnumParts.LEG_LEFT) {
                    config.setTranslate(-(1.0F - config.scaleX) * 0.118F, this.getLegsY(), -(1.0F - config.scaleZ) * 0.00625F);
                    if (!config.notShared) {
                        ModelPartConfig leg = this.getPartConfig(EnumParts.LEG_RIGHT);
                        leg.copyValues(config);
                    }
                } else if (part == EnumParts.LEG_RIGHT) {
                    config.setTranslate((1.0F - config.scaleX) * 0.118F, this.getLegsY(), -(1.0F - config.scaleZ) * 0.00625F);
                } else if (part == EnumParts.BODY) {
                    config.setTranslate(0.0F, this.getBodyY(), 0.0F);
                }
            }
        }
    }

    public void setEntity(ResourceLocation resourceLocation) {
        this.entityName = resourceLocation;
        this.clearEntity();
        this.extra = new CompoundTag();
    }

    public ResourceLocation getEntityName() {
        return this.entityName;
    }

    public boolean hasEntity() {
        return this.entityName != null;
    }

    public float offsetY() {
        return this.entity == null ? -this.getBodyY() : this.entity.m_20206_() - 1.8F;
    }

    public void clearEntity() {
        this.entity = null;
    }

    public ModelPartConfig getPartConfig(EnumParts type) {
        if (type == EnumParts.BODY) {
            return this.body;
        } else if (type == EnumParts.ARM_LEFT) {
            return this.arm1;
        } else if (type == EnumParts.ARM_RIGHT) {
            return this.arm2;
        } else if (type == EnumParts.LEG_LEFT) {
            return this.leg1;
        } else {
            return type == EnumParts.LEG_RIGHT ? this.leg2 : this.head;
        }
    }

    public abstract LivingEntity getOwner();

    public float getBodyY() {
        return this.entity != null ? this.entity.m_20206_() : (1.0F - this.body.scaleY) * 0.75F + this.getLegsY();
    }

    public float getLegsY() {
        ModelPartConfig legs = this.leg1;
        if (this.leg1.notShared && this.leg2.scaleY > this.leg1.scaleY) {
            legs = this.leg2;
        }
        return (1.0F - legs.scaleY) * 0.75F;
    }

    public void refreshParts() {
        this.hiddenParts = (List<BodyPart>) this.mpmParts.stream().flatMap(part -> {
            MpmPart p = part.getPart();
            return p != null ? p.hiddenParts.stream() : Stream.empty();
        }).distinct().collect(Collectors.toList());
    }
}