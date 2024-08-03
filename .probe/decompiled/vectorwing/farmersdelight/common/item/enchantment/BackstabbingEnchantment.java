package vectorwing.farmersdelight.common.item.enchantment;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vectorwing.farmersdelight.common.registry.ModEnchantments;

public class BackstabbingEnchantment extends Enchantment {

    public BackstabbingEnchantment(Enchantment.Rarity rarity, EnchantmentCategory category, EquipmentSlot... applicableSlots) {
        super(rarity, category, applicableSlots);
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 15 + (enchantmentLevel - 1) * 9;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return super.getMinCost(enchantmentLevel) + 50;
    }

    public static boolean isLookingBehindTarget(LivingEntity target, Vec3 attackerLocation) {
        if (attackerLocation != null) {
            Vec3 lookingVector = target.m_20252_(1.0F);
            Vec3 attackAngleVector = attackerLocation.subtract(target.m_20182_()).normalize();
            attackAngleVector = new Vec3(attackAngleVector.x, 0.0, attackAngleVector.z);
            return attackAngleVector.dot(lookingVector) < -0.5;
        } else {
            return false;
        }
    }

    public static float getBackstabbingDamagePerLevel(float amount, int level) {
        float multiplier = (float) level * 0.2F + 1.2F;
        return amount * multiplier;
    }

    @EventBusSubscriber(modid = "farmersdelight", bus = Bus.FORGE)
    public static class BackstabbingEvent {

        @SubscribeEvent
        public static void onKnifeBackstab(LivingHurtEvent event) {
            Entity attacker = event.getSource().getEntity();
            if (attacker instanceof Player) {
                ItemStack weapon = ((Player) attacker).m_21205_();
                int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.BACKSTABBING.get(), weapon);
                if (enchantmentLevel > 0 && BackstabbingEnchantment.isLookingBehindTarget(event.getEntity(), event.getSource().getSourcePosition())) {
                    Level level = event.getEntity().m_20193_();
                    if (!level.isClientSide) {
                        event.setAmount(BackstabbingEnchantment.getBackstabbingDamagePerLevel(event.getAmount(), enchantmentLevel));
                        level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }
}