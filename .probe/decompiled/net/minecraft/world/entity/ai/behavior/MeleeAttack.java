package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;

public class MeleeAttack {

    public static OneShot<Mob> create(int int0) {
        return BehaviorBuilder.create(p_258526_ -> p_258526_.group(p_258526_.registered(MemoryModuleType.LOOK_TARGET), p_258526_.present(MemoryModuleType.ATTACK_TARGET), p_258526_.absent(MemoryModuleType.ATTACK_COOLING_DOWN), p_258526_.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply(p_258526_, (p_258529_, p_258530_, p_258531_, p_258532_) -> (p_258539_, p_258540_, p_258541_) -> {
            LivingEntity $$9 = p_258526_.get(p_258530_);
            if (!isHoldingUsableProjectileWeapon(p_258540_) && p_258540_.isWithinMeleeAttackRange($$9) && p_258526_.<NearestVisibleLivingEntities>get(p_258532_).contains($$9)) {
                p_258529_.set(new EntityTracker($$9, true));
                p_258540_.m_6674_(InteractionHand.MAIN_HAND);
                p_258540_.doHurtTarget($$9);
                p_258531_.setWithExpiry(true, (long) int0);
                return true;
            } else {
                return false;
            }
        }));
    }

    private static boolean isHoldingUsableProjectileWeapon(Mob mob0) {
        return mob0.m_21093_(p_147697_ -> {
            Item $$2 = p_147697_.getItem();
            return $$2 instanceof ProjectileWeaponItem && mob0.canFireProjectileWeapon((ProjectileWeaponItem) $$2);
        });
    }
}