package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

@SerialClass
public class MinionData {

    @SerialField(toClient = true)
    public UUID uuid;

    @SerialField(toClient = true)
    public int id;

    @SerialField(toClient = true)
    public double linkDistance;

    @SerialField(toClient = true)
    public boolean protectMaster;

    @SerialField(toClient = true)
    public boolean discardOnUnlink;

    public Mob master;

    public boolean tick(LivingEntity mob) {
        if (mob.m_9236_() instanceof ServerLevel sl) {
            if (this.master == null && sl.getEntity(this.uuid) instanceof Mob mas) {
                this.master = mas;
            }
            if (this.discardOnUnlink && (this.master == null || (double) this.master.m_20270_(mob) > this.linkDistance)) {
                mob.m_146870_();
            }
        } else {
            if (this.master == null && mob.m_9236_().getEntity(this.id) instanceof Mob mas && mas.m_20148_().equals(this.uuid)) {
                this.master = mas;
            }
            if (this.master != null && this.id != this.master.m_19879_()) {
                this.id = this.master.m_19879_();
                return true;
            }
        }
        return false;
    }

    public MinionData init(Mob mob, EntityConfig.Minion config) {
        this.uuid = mob.m_20148_();
        this.id = mob.m_19879_();
        this.linkDistance = config.linkDistance();
        this.protectMaster = config.protectMaster();
        this.discardOnUnlink = config.discardOnUnlink();
        this.master = mob;
        return this;
    }
}