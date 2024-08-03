package noppes.npcs.api.wrapper;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLiving;
import noppes.npcs.api.entity.data.IMark;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.data.MarkData;

public class EntityLivingBaseWrapper<T extends LivingEntity> extends EntityWrapper<T> implements IEntityLiving {

    public EntityLivingBaseWrapper(T entity) {
        super(entity);
    }

    @Override
    public float getHealth() {
        return this.entity.getHealth();
    }

    @Override
    public void setHealth(float health) {
        this.entity.setHealth(health);
    }

    @Override
    public float getMaxHealth() {
        return this.entity.getMaxHealth();
    }

    @Override
    public void setMaxHealth(float health) {
        if (!(health < 0.0F)) {
            this.entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double) health);
        }
    }

    @Override
    public boolean isAttacking() {
        return this.entity.getLastHurtByMob() != null;
    }

    @Override
    public void setAttackTarget(IEntityLiving living) {
        if (living == null) {
            this.entity.setLastHurtByMob(null);
        } else {
            this.entity.setLastHurtByMob(living.getMCEntity());
        }
    }

    @Override
    public IEntityLiving getAttackTarget() {
        return (IEntityLiving) NpcAPI.Instance().getIEntity(this.entity.getLastHurtByMob());
    }

    @Override
    public IEntityLiving getLastAttacked() {
        return (IEntityLiving) NpcAPI.Instance().getIEntity(this.entity.getLastHurtMob());
    }

    @Override
    public int getLastAttackedTime() {
        return this.entity.getLastHurtMobTimestamp();
    }

    @Override
    public boolean canSeeEntity(IEntity entity) {
        return this.entity.hasLineOfSight(entity.getMCEntity());
    }

    @Override
    public void swingMainhand() {
        this.entity.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public void swingOffhand() {
        this.entity.swing(InteractionHand.OFF_HAND);
    }

    @Override
    public void addPotionEffect(int effect, int duration, int strength, boolean hideParticles) {
        MobEffect p = MobEffect.byId(effect);
        if (p != null) {
            if (strength < 0) {
                strength = 0;
            } else if (strength > 255) {
                strength = 255;
            }
            if (duration < 0) {
                duration = 0;
            } else if (duration > 1000000) {
                duration = 1000000;
            }
            if (!p.isInstantenous()) {
                duration *= 20;
            }
            if (duration == 0) {
                this.entity.removeEffect(p);
            } else {
                this.entity.addEffect(new MobEffectInstance(p, duration, strength, false, hideParticles));
            }
        }
    }

    @Override
    public void clearPotionEffects() {
        this.entity.removeAllEffects();
    }

    @Override
    public int getPotionEffect(int effect) {
        MobEffectInstance pf = this.entity.getEffect(MobEffect.byId(effect));
        return pf == null ? -1 : pf.getAmplifier();
    }

    @Override
    public IItemStack getMainhandItem() {
        return NpcAPI.Instance().getIItemStack(this.entity.getMainHandItem());
    }

    @Override
    public void setMainhandItem(IItemStack item) {
        this.entity.setItemInHand(InteractionHand.MAIN_HAND, item == null ? ItemStack.EMPTY : item.getMCItemStack());
    }

    @Override
    public IItemStack getOffhandItem() {
        return NpcAPI.Instance().getIItemStack(this.entity.getOffhandItem());
    }

    @Override
    public void setOffhandItem(IItemStack item) {
        this.entity.setItemInHand(InteractionHand.OFF_HAND, item == null ? ItemStack.EMPTY : item.getMCItemStack());
    }

    @Override
    public IItemStack getArmor(int slot) {
        if (slot >= 0 && slot <= 3) {
            return NpcAPI.Instance().getIItemStack(this.entity.getItemBySlot(this.getSlot(slot)));
        } else {
            throw new CustomNPCsException("Wrong slot id:" + slot);
        }
    }

    @Override
    public void setArmor(int slot, IItemStack item) {
        if (slot >= 0 && slot <= 3) {
            this.entity.setItemSlot(this.getSlot(slot), item == null ? ItemStack.EMPTY : item.getMCItemStack());
        } else {
            throw new CustomNPCsException("Wrong slot id:" + slot);
        }
    }

    private EquipmentSlot getSlot(int slot) {
        if (slot == 3) {
            return EquipmentSlot.HEAD;
        } else if (slot == 2) {
            return EquipmentSlot.CHEST;
        } else if (slot == 1) {
            return EquipmentSlot.LEGS;
        } else {
            return slot == 0 ? EquipmentSlot.FEET : null;
        }
    }

    @Override
    public float getRotation() {
        return this.entity.yBodyRot;
    }

    @Override
    public void setRotation(float rotation) {
        this.entity.yBodyRot = rotation;
    }

    @Override
    public int getType() {
        return 5;
    }

    @Override
    public boolean typeOf(int type) {
        return type == 5 ? true : super.typeOf(type);
    }

    @Override
    public boolean isChild() {
        return this.entity.isBaby();
    }

    @Override
    public IMark addMark(int type) {
        MarkData data = MarkData.get(this.entity);
        return data.addMark(type);
    }

    @Override
    public void removeMark(IMark mark) {
        MarkData data = MarkData.get(this.entity);
        data.marks.remove(mark);
        data.syncClients();
    }

    @Override
    public IMark[] getMarks() {
        MarkData data = MarkData.get(this.entity);
        return (IMark[]) data.marks.toArray(new IMark[data.marks.size()]);
    }

    @Override
    public float getMoveForward() {
        return this.entity.zza;
    }

    @Override
    public void setMoveForward(float move) {
        this.entity.zza = move;
    }

    @Override
    public float getMoveStrafing() {
        return this.entity.xxa;
    }

    @Override
    public void setMoveStrafing(float move) {
        this.entity.xxa = move;
    }

    @Override
    public float getMoveVertical() {
        return this.entity.yya;
    }

    @Override
    public void setMoveVertical(float move) {
        this.entity.yya = move;
    }
}