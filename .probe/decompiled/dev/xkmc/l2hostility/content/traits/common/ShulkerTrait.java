package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2hostility.content.capability.mob.CapStorageData;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.entity.BulletType;
import dev.xkmc.l2hostility.content.entity.HostilityBullet;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.List;
import java.util.UUID;
import java.util.function.IntSupplier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class ShulkerTrait extends MobTrait {

    private final IntSupplier interval;

    private final BulletType type;

    private final int offset;

    public ShulkerTrait(ChatFormatting format, IntSupplier interval, BulletType type, int offset) {
        super(format);
        this.interval = interval;
        this.type = type;
        this.offset = offset;
    }

    @Override
    public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
        this.type.onAttackedByOthers(level, entity, event);
    }

    @Override
    public void tick(LivingEntity e, int level) {
        if (!e.m_9236_().isClientSide()) {
            if (e instanceof Mob mob && MobTraitCap.HOLDER.isProper(mob)) {
                MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
                ShulkerTrait.Data data = cap.getOrCreateData(this.getRegistryName(), ShulkerTrait.Data::new);
                if (data.uuid != null && mob.m_9236_() instanceof ServerLevel sl && sl.getEntity(data.uuid) instanceof ShulkerBullet) {
                    return;
                }
                data.tickCount++;
                if (data.tickCount < this.interval.getAsInt()) {
                    return;
                }
                if ((mob.f_19797_ + this.offset) % this.interval.getAsInt() != 0) {
                    return;
                }
                if (mob.getTarget() != null && mob.getTarget().isAlive()) {
                    HostilityBullet bullet = new HostilityBullet(mob.m_9236_(), mob, mob.getTarget(), Direction.Axis.Y, this.type, level);
                    data.tickCount = 0;
                    if (this.type.limit()) {
                        data.uuid = bullet.m_20148_();
                    }
                    mob.m_9236_().m_7967_(bullet);
                    mob.m_5496_(SoundEvents.SHULKER_SHOOT, 2.0F, (mob.m_217043_().nextFloat() - mob.m_217043_().nextFloat()) * 0.2F + 1.0F);
                }
            }
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc", Component.literal((double) this.interval.getAsInt() / 20.0 + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
    }

    @SerialClass
    public static class Data extends CapStorageData {

        @SerialField
        public int tickCount;

        @SerialField
        public UUID uuid;
    }
}