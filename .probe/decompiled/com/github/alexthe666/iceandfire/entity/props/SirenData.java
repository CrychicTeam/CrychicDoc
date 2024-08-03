package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.util.IHearsSiren;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SirenData {

    @Nullable
    public EntitySiren charmedBy;

    public int charmTime;

    public boolean isCharmed;

    @Nullable
    private UUID charmedByUUID;

    private int charmedById;

    private boolean isInitialized;

    private boolean triggerClientUpdate;

    public void tickCharmed(LivingEntity holder) {
        if (holder instanceof Player || holder instanceof AbstractVillager || holder instanceof IHearsSiren) {
            if (!this.isInitialized) {
                this.initialize(holder.m_9236_());
            }
            if (this.charmedBy != null) {
                if (this.charmedBy.isActuallySinging()) {
                    if (EntitySiren.isWearingEarplugs(holder) || this.charmTime > IafConfig.sirenMaxSingTime) {
                        this.charmedBy.singCooldown = IafConfig.sirenTimeBetweenSongs;
                        this.clearCharm();
                        return;
                    }
                    if (!this.charmedBy.m_6084_() || holder.m_20270_(this.charmedBy) > 64.0F || holder instanceof Player player && (player.isCreative() || player.isSpectator())) {
                        this.clearCharm();
                        return;
                    }
                    if (holder.m_20270_(this.charmedBy) < 5.0F) {
                        this.charmedBy.singCooldown = IafConfig.sirenTimeBetweenSongs;
                        this.charmedBy.setSinging(false);
                        this.charmedBy.m_6710_(holder);
                        this.charmedBy.setAggressive(true);
                        this.charmedBy.triggerOtherSirens(holder);
                        this.clearCharm();
                        return;
                    }
                    this.isCharmed = true;
                    this.charmTime++;
                    if (holder.getRandom().nextInt(7) == 0) {
                        for (int i = 0; i < 5; i++) {
                            holder.m_9236_().addParticle(ParticleTypes.HEART, holder.m_20185_() + (holder.getRandom().nextDouble() - 0.5) * 3.0, holder.m_20186_() + (holder.getRandom().nextDouble() - 0.5) * 3.0, holder.m_20189_() + (holder.getRandom().nextDouble() - 0.5) * 3.0, 0.0, 0.0, 0.0);
                        }
                    }
                    if (holder.f_19862_) {
                        holder.setJumping(true);
                    }
                    double motionXAdd = (Math.signum(this.charmedBy.m_20185_() - holder.m_20185_()) * 0.5 - holder.m_20184_().x) * 0.100000000372529;
                    double motionYAdd = (Math.signum(this.charmedBy.m_20186_() - holder.m_20186_() + 1.0) * 0.5 - holder.m_20184_().y) * 0.100000000372529;
                    double motionZAdd = (Math.signum(this.charmedBy.m_20189_() - holder.m_20189_()) * 0.5 - holder.m_20184_().z) * 0.100000000372529;
                    holder.m_20256_(holder.m_20184_().add(motionXAdd, motionYAdd, motionZAdd));
                    if (holder.m_20159_()) {
                        holder.stopRiding();
                    }
                    if (!(holder instanceof Player)) {
                        double x = this.charmedBy.m_20185_() - holder.m_20185_();
                        double y = this.charmedBy.m_20186_() - 1.0 - holder.m_20186_();
                        double z = this.charmedBy.m_20189_() - holder.m_20189_();
                        double radius = Math.sqrt(x * x + z * z);
                        float xRot = (float) (-(Mth.atan2(y, radius) * (180.0 / Math.PI)));
                        float yRot = (float) (Mth.atan2(z, x) * (180.0 / Math.PI)) - 90.0F;
                        holder.m_146926_(this.updateRotation(holder.m_146909_(), xRot, 30.0F));
                        holder.m_146922_(this.updateRotation(holder.m_146908_(), yRot, 30.0F));
                    }
                }
            }
        }
    }

    public void setCharmed(Entity entity) {
        if (entity instanceof EntitySiren siren) {
            this.charmedBy = siren;
            this.isCharmed = true;
            this.triggerClientUpdate = true;
        }
    }

    public void clearCharm() {
        this.charmTime = 0;
        this.isCharmed = false;
        this.charmedBy = null;
        this.triggerClientUpdate = true;
    }

    public void serialize(CompoundTag tag) {
        CompoundTag sirenData = new CompoundTag();
        if (this.charmedBy != null) {
            sirenData.put("charmedByUUID", NbtUtils.createUUID(this.charmedBy.m_20148_()));
            sirenData.putInt("charmedById", this.charmedBy.m_19879_());
        } else {
            sirenData.putInt("charmedById", -1);
        }
        sirenData.putInt("charmTime", this.charmTime);
        sirenData.putBoolean("isCharmed", this.isCharmed);
        tag.put("sirenData", sirenData);
    }

    public void deserialize(CompoundTag tag) {
        CompoundTag sirenData = tag.getCompound("sirenData");
        Tag uuidTag = sirenData.get("charmedByUUID");
        if (uuidTag != null) {
            this.charmedByUUID = NbtUtils.loadUUID(uuidTag);
        }
        this.charmedById = sirenData.getInt("charmedById");
        this.charmTime = sirenData.getInt("charmTime");
        this.isCharmed = sirenData.getBoolean("isCharmed");
        this.isInitialized = false;
    }

    public boolean doesClientNeedUpdate() {
        if (this.triggerClientUpdate) {
            this.triggerClientUpdate = false;
            return true;
        } else {
            return false;
        }
    }

    private float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = Mth.wrapDegrees(targetAngle - angle);
        if (f > maxIncrease) {
            f = maxIncrease;
        }
        if (f < -maxIncrease) {
            f = -maxIncrease;
        }
        return angle + f;
    }

    private void initialize(Level level) {
        this.charmedBy = null;
        if (this.charmedByUUID != null && level instanceof ServerLevel serverLevel) {
            if (serverLevel.getEntity(this.charmedByUUID) instanceof EntitySiren siren) {
                this.triggerClientUpdate = true;
                this.charmedByUUID = null;
                this.charmedBy = siren;
            }
        } else if (this.charmedById != -1 && level.getEntity(this.charmedById) instanceof EntitySiren siren) {
            this.charmedBy = siren;
        }
        this.isInitialized = true;
    }
}