package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

public abstract class PushPullTrait extends LegendaryTrait {

    public PushPullTrait(ChatFormatting style) {
        super(style);
    }

    protected abstract int getRange();

    protected abstract double getStrength(double var1);

    @Override
    public void tick(LivingEntity mob, int level) {
        int r = this.getRange();
        List<? extends LivingEntity> list;
        if (mob.m_9236_().isClientSide()) {
            list = mob.m_9236_().getEntities(EntityTypeTest.forClass(Player.class), mob.m_20191_().inflate((double) r), ex -> ex.isLocalPlayer() && !ex.getAbilities().instabuild && !ex.isSpectator());
        } else {
            list = mob.m_9236_().getEntities(EntityTypeTest.forClass(LivingEntity.class), mob.m_20191_().inflate((double) r), ex -> {
                if (ex instanceof Player pl && !pl.getAbilities().instabuild && !ex.m_5833_() || ex instanceof Mob m && m.getTarget() == mob) {
                    return true;
                }
                return false;
            });
        }
        for (LivingEntity e : list) {
            double dist = (double) (mob.m_20270_(e) / (float) r);
            if (dist > 1.0) {
                return;
            }
            if (!CurioCompat.hasItemInCurio(e, (Item) LHItems.ABRAHADABRA.get())) {
                double strength = this.getStrength(dist);
                int lv = 0;
                for (ItemStack armor : e.getArmorSlots()) {
                    lv += armor.getEnchantmentLevel((Enchantment) LHEnchantments.INSULATOR.get());
                }
                if (lv > 0) {
                    strength *= Math.pow(LHConfig.COMMON.insulatorFactor.get(), (double) lv);
                }
                Vec3 vec = e.m_20182_().subtract(mob.m_20182_()).normalize().scale(strength);
                e.m_5997_(vec.x, vec.y, vec.z);
            }
        }
    }
}