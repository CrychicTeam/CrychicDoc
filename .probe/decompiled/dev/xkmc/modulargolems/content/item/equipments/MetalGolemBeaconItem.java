package dev.xkmc.modulargolems.content.item.equipments;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class MetalGolemBeaconItem extends GolemEquipmentItem implements TickEquipmentItem {

    private final int beaconLevel;

    public MetalGolemBeaconItem(Item.Properties properties, int beaconLevel) {
        super(properties, EquipmentSlot.FEET, GolemTypes.ENTITY_GOLEM::get, builder -> {
        });
        this.beaconLevel = beaconLevel;
    }

    public int getBeaconLevel() {
        return this.beaconLevel;
    }

    @Override
    public void tick(ItemStack stack, Level level, Entity entity) {
        if ((double) level.getGameTime() % 80.0 == 0.0) {
            if (stack.getItem() instanceof MetalGolemBeaconItem beacon && entity instanceof AbstractGolemEntity<?, ?> golem) {
                double range = (double) this.getBeaconLevel() * 10.0 + 10.0;
                int time = (9 + this.getBeaconLevel() * 2) * 20;
                AABB aabb = golem.m_20191_().inflate(range).expandTowards(0.0, (double) level.m_141928_(), 0.0);
                for (LivingEntity entity1 : level.m_45976_(LivingEntity.class, aabb)) {
                    if (entity1 == golem.getOwner()) {
                        entity1.addEffect(new MobEffectInstance(MobEffects.REGENERATION, time, this.getBeaconLevel() - 1, true, true));
                    } else if (entity1 instanceof OwnableEntity) {
                        OwnableEntity ownable = (OwnableEntity) entity1;
                        if (golem.getOwner() == ownable.getOwner()) {
                            entity1.addEffect(new MobEffectInstance(MobEffects.REGENERATION, time, this.getBeaconLevel() - 1, true, true));
                        }
                    }
                }
            }
        }
    }
}