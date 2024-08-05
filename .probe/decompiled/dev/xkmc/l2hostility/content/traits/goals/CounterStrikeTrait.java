package dev.xkmc.l2hostility.content.traits.goals;

import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class CounterStrikeTrait extends MobTrait {

    public CounterStrikeTrait(ChatFormatting format) {
        super(format);
    }

    @Override
    public void tick(LivingEntity le, int level) {
        if (!le.m_9236_().isClientSide()) {
            CounterStrikeTrait.Data data = ((MobTraitCap) MobTraitCap.HOLDER.get(le)).getOrCreateData(this.getRegistryName(), CounterStrikeTrait.Data::new);
            if (data.cooldown > 0) {
                data.cooldown--;
            } else if (le instanceof Mob mob) {
                if (le.m_20096_()) {
                    LivingEntity target = mob.getTarget();
                    if (target != null && target.isAlive()) {
                        if (data.strikeId != null && data.strikeId.equals(target.m_20148_())) {
                            Vec3 diff = target.m_20182_().subtract(le.m_20182_());
                            diff = diff.normalize().scale(3.0);
                            if (diff.y <= 0.2) {
                                diff = diff.add(0.0, 0.2, 0.0);
                            }
                            le.m_20256_(diff);
                            le.f_19812_ = true;
                            data.duration = LHConfig.COMMON.counterStrikeDuration.get();
                            data.strikeId = null;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onHurtByOthers(int level, LivingEntity le, LivingHurtEvent event) {
        if (!le.m_9236_().isClientSide()) {
            Entity target = event.getSource().getEntity();
            CounterStrikeTrait.Data data = ((MobTraitCap) MobTraitCap.HOLDER.get(le)).getOrCreateData(this.getRegistryName(), CounterStrikeTrait.Data::new);
            if (target instanceof LivingEntity && le instanceof Mob mob && mob.getTarget() == target) {
                data.strikeId = target.getUUID();
            }
        }
    }

    @SerialClass
    public static class Data extends CapStorageData {

        @SerialField
        public int cooldown;

        @SerialField
        public int duration;

        @SerialField
        public UUID strikeId;
    }
}