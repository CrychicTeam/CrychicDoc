package team.lodestar.lodestone.systems.fireeffect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import team.lodestar.lodestone.capability.LodestoneEntityDataCapability;
import team.lodestar.lodestone.helpers.NBTHelper;
import team.lodestar.lodestone.registry.common.LodestoneFireEffectRegistry;

public class FireEffectInstance {

    public int duration;

    public final FireEffectType type;

    public FireEffectInstance(FireEffectType type) {
        this.type = type;
    }

    public FireEffectInstance extendDuration(int increase) {
        this.duration += increase;
        return this;
    }

    public FireEffectInstance setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public FireEffectInstance sync(Entity target) {
        LodestoneEntityDataCapability.syncTrackingAndSelf(target, NBTHelper.create("fireEffect", "type", "duration").setWhitelist());
        return this;
    }

    public FireEffectInstance syncDuration(Entity target) {
        LodestoneEntityDataCapability.syncTrackingAndSelf(target, NBTHelper.create("fireEffect", "duration").setWhitelist());
        return this;
    }

    public void tick(Entity target) {
        if (target.isInPowderSnow || target.isInWaterRainOrBubble()) {
            this.type.extinguish(this, target);
        }
        if (this.canDamageTarget(target)) {
            this.duration--;
            if (this.type.isValid(this) && this.duration % this.type.getTickInterval(this) == 0) {
                this.type.tick(this, target);
            }
        } else {
            this.duration -= 4;
        }
    }

    public void entityAttack() {
    }

    public boolean canDamageTarget(Entity target) {
        return !target.fireImmune();
    }

    public boolean isValid() {
        return this.type.isValid(this);
    }

    public void serializeNBT(CompoundTag tag) {
        CompoundTag fireTag = new CompoundTag();
        fireTag.putString("type", this.type.id);
        fireTag.putInt("duration", this.duration);
        tag.put("fireEffect", fireTag);
    }

    public static FireEffectInstance deserializeNBT(CompoundTag tag) {
        if (!tag.contains("fireEffect")) {
            return null;
        } else {
            CompoundTag fireTag = tag.getCompound("fireEffect");
            FireEffectInstance instance = new FireEffectInstance((FireEffectType) LodestoneFireEffectRegistry.FIRE_TYPES.get(fireTag.getString("type")));
            instance.setDuration(fireTag.getInt("duration"));
            return instance;
        }
    }
}