package noppes.npcs.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.EntityIMixin;
import noppes.npcs.mixin.EntityLivingIMixin;
import noppes.npcs.mixin.WalkAnimationStateMixin;
import noppes.npcs.shared.common.util.LogWriter;

public class EntityUtil {

    private static HashMap<EntityType<? extends Entity>, Class> entityClasses = new HashMap();

    public static void Copy(LivingEntity copied, LivingEntity entity) {
        ((EntityIMixin) entity).setLevel(copied.m_9236_());
        entity.deathTime = copied.deathTime;
        entity.f_19787_ = copied.f_19787_;
        entity.f_19867_ = copied.f_19787_;
        entity.f_19788_ = copied.f_19788_;
        entity.zza = copied.zza;
        entity.xxa = copied.xxa;
        entity.m_6853_(copied.m_20096_());
        entity.f_19789_ = copied.f_19789_;
        entity.setJumping(((EntityLivingIMixin) copied).jumping());
        List<SynchedEntityData.DataItem<?>> copiedData = ((ISynchedEntityData) copied.m_20088_()).getAll();
        List<SynchedEntityData.DataItem<?>> data = ((ISynchedEntityData) entity.m_20088_()).getAll();
        for (SynchedEntityData.DataItem<?> entry : copiedData) {
            if (data.stream().anyMatch(e -> e.getAccessor() == entry.getAccessor()) && entry.getValue() instanceof SynchedEntityData.DataValue) {
                entity.m_20088_().set(entry.getAccessor(), ((SynchedEntityData.DataValue) entry.getValue()).value());
            }
        }
        entity.f_19854_ = copied.f_19854_;
        entity.f_19855_ = copied.f_19855_;
        entity.f_19856_ = copied.f_19856_;
        entity.m_6034_(copied.m_20185_(), copied.m_20186_(), copied.m_20189_());
        entity.f_19790_ = copied.f_19790_;
        entity.f_19791_ = copied.f_19791_;
        entity.f_19792_ = copied.f_19792_;
        entity.m_20256_(copied.m_20184_());
        entity.m_146926_(copied.m_146909_());
        entity.m_146922_(copied.m_146908_());
        entity.f_19860_ = copied.f_19860_;
        entity.f_19859_ = copied.f_19859_;
        entity.yHeadRot = copied.yHeadRot;
        entity.yHeadRotO = copied.yHeadRotO;
        entity.yBodyRot = copied.yBodyRot;
        entity.yBodyRotO = copied.yBodyRotO;
        ((EntityLivingIMixin) entity).useItemRemaining(copied.getUseItemRemainingTicks());
        ((WalkAnimationStateMixin) entity.walkAnimation).setPosition(copied.walkAnimation.position());
        ((EntityLivingIMixin) entity).animStep(((EntityLivingIMixin) copied).animStep());
        ((EntityLivingIMixin) entity).animStepO(((EntityLivingIMixin) copied).animStepO());
        ((EntityLivingIMixin) entity).swimAmount(((EntityLivingIMixin) copied).swimAmount());
        ((EntityLivingIMixin) entity).swimAmountO(((EntityLivingIMixin) copied).swimAmountO());
        entity.swinging = copied.swinging;
        entity.swingTime = copied.swingTime;
        entity.walkAnimation.setSpeed(copied.walkAnimation.speed());
        ((WalkAnimationStateMixin) entity.walkAnimation).setSpeedOld(((WalkAnimationStateMixin) copied.walkAnimation).getSpeedOld());
        entity.attackAnim = copied.attackAnim;
        entity.oAttackAnim = copied.oAttackAnim;
        entity.f_19797_ = copied.f_19797_;
        entity.setHealth(Math.min(copied.getHealth(), entity.getMaxHealth()));
        entity.getPersistentData().merge(copied.getPersistentData());
        if (entity instanceof Player && copied instanceof Player) {
            Player ePlayer = (Player) entity;
            Player cPlayer = (Player) copied;
            ePlayer.bob = cPlayer.bob;
            ePlayer.oBob = cPlayer.oBob;
            ePlayer.xCloakO = cPlayer.xCloakO;
            ePlayer.yCloakO = cPlayer.yCloakO;
            ePlayer.zCloakO = cPlayer.zCloakO;
            ePlayer.xCloak = cPlayer.xCloak;
            ePlayer.yCloak = cPlayer.yCloak;
            ePlayer.zCloak = cPlayer.zCloak;
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            entity.setItemSlot(slot, copied.getItemBySlot(slot));
        }
        if (entity instanceof EnderDragon) {
            entity.m_146926_(entity.m_146909_() + 180.0F);
        }
        ((EntityIMixin) entity).removal(((EntityIMixin) copied).removal());
        entity.deathTime = copied.deathTime;
        entity.f_19797_ = copied.f_19797_;
        if (entity instanceof EnderDragon) {
            entity.m_146922_(entity.m_146908_() + 180.0F);
        }
        if (entity instanceof Chicken) {
            ((Chicken) entity).flap = copied.m_20096_() ? 0.0F : 1.0F;
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            entity.setItemSlot(slot, copied.getItemBySlot(slot));
        }
        if (copied instanceof EntityNPCInterface && entity instanceof EntityNPCInterface) {
            EntityNPCInterface npc = (EntityNPCInterface) copied;
            EntityNPCInterface target = (EntityNPCInterface) entity;
            target.textureLocation = npc.textureLocation;
            target.textureGlowLocation = npc.textureGlowLocation;
            target.textureCloakLocation = npc.textureCloakLocation;
            target.display = npc.display;
            target.inventory = npc.inventory;
            if (npc.job.getType() == 9) {
                target.job = npc.job;
            }
            if (target.currentAnimation != npc.currentAnimation) {
                target.currentAnimation = npc.currentAnimation;
                npc.m_6210_();
            }
            target.setDataWatcher(npc.m_20088_());
        }
        if (entity instanceof EntityCustomNpc && copied instanceof EntityCustomNpc npcx) {
            EntityCustomNpc targetx = (EntityCustomNpc) entity;
            targetx.modelData = npcx.modelData.copy();
            targetx.modelData.setEntity(null);
        }
    }

    private <T> void setData(LivingEntity entity, List<SynchedEntityData.DataItem<T>> copiedData, List<SynchedEntityData.DataItem<T>> data) {
        for (SynchedEntityData.DataItem<? extends Object> entry : copiedData) {
            if (data.stream().anyMatch(e -> e.getAccessor() == entry.getAccessor())) {
                entity.m_20088_().set(entry.getAccessor(), entry.getValue());
            }
        }
    }

    public static void setRecentlyHit(LivingEntity entity) {
        ((EntityLivingIMixin) entity).lastHurtByPlayerTime(100);
    }

    public static HashMap<EntityType<? extends Entity>, Class> getAllEntitiesClasses(Level level) {
        if (!entityClasses.isEmpty()) {
            return entityClasses;
        } else {
            HashMap<EntityType<? extends Entity>, Class> data = new HashMap();
            for (EntityType<? extends Entity> ent : ForgeRegistries.ENTITY_TYPES.getValues()) {
                try {
                    Entity e = ent.create(level);
                    if (e != null) {
                        if (LivingEntity.class.isAssignableFrom(e.getClass())) {
                            data.put(ent, e.getClass());
                        }
                        e.discard();
                    }
                } catch (Exception var5) {
                }
            }
            entityClasses = data;
            return data;
        }
    }

    public static HashMap<EntityType<? extends Entity>, Class> getAllEntitiesClassesNoNpcs(Level level) {
        HashMap<EntityType<? extends Entity>, Class> data = new HashMap(getAllEntitiesClasses(level));
        Iterator<Entry<EntityType<? extends Entity>, Class>> ita = data.entrySet().iterator();
        while (ita.hasNext()) {
            Entry<EntityType<? extends Entity>, Class> entry = (Entry<EntityType<? extends Entity>, Class>) ita.next();
            if (EntityNPCInterface.class.isAssignableFrom((Class) entry.getValue()) || !LivingEntity.class.isAssignableFrom((Class) entry.getValue())) {
                ita.remove();
            }
        }
        return data;
    }

    public static HashMap<String, ResourceLocation> getAllEntities(Level level, boolean withNpcs) {
        HashMap<String, ResourceLocation> data = new HashMap();
        for (EntityType<? extends Entity> ent : ForgeRegistries.ENTITY_TYPES.getValues()) {
            try {
                Entity e = ent.create(level);
                if (e != null) {
                    if (LivingEntity.class.isAssignableFrom(e.getClass()) && (withNpcs || !EntityNPCInterface.class.isAssignableFrom(e.getClass()))) {
                        data.put(ent.getDescriptionId(), ForgeRegistries.ENTITY_TYPES.getKey(ent));
                    }
                    e.discard();
                }
            } catch (Throwable var6) {
                LogWriter.except(var6);
            }
        }
        return data;
    }
}