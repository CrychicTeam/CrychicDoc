package dev.xkmc.modulargolems.content.entity.ranged;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.events.event.GolemThrowableEvent;
import java.util.HashSet;
import java.util.Map;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class GolemShooterHelper {

    private static final HashSet<Class<?>> BLACKLIST = new HashSet();

    public static GolemThrowableEvent isValidThrowableWeapon(HumanoidGolemEntity golem, ItemStack stack, InteractionHand hand) {
        if (stack.getEnchantmentLevel(Enchantments.LOYALTY) > 0) {
            stack = stack.copy();
            Map<Enchantment, Integer> map = stack.getAllEnchantments();
            map.remove(Enchantments.LOYALTY);
            EnchantmentHelper.setEnchantments(map, stack);
        }
        GolemThrowableEvent event = new GolemThrowableEvent(golem, stack, hand);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static boolean arrowIsInfinite(ItemStack arrow, ItemStack bow) {
        if (arrow.getItem() instanceof ArrowItem item) {
            if (BLACKLIST.contains(item.getClass())) {
                return false;
            } else {
                try {
                    return item.isInfinite(arrow, bow, null);
                } catch (NullPointerException var4) {
                    BLACKLIST.add(item.getClass());
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public static void shootAimHelper(LivingEntity target, Projectile arrow) {
        shootAimHelper(target, arrow, 3.0, 0.05);
    }

    public static void shootAimHelper(LivingEntity target, Projectile arrow, double v, double g) {
        double dx = target.m_20185_() - arrow.m_20185_();
        double dy = target.m_20227_(0.5) - arrow.m_20186_();
        double dz = target.m_20189_() - arrow.m_20189_();
        double c = dx * dx + dz * dz + dy * dy;
        boolean completed = false;
        if (target instanceof Slime) {
            BlockHitResult clip = target.m_9236_().m_45547_(new ClipContext(target.m_20182_(), target.m_20182_().add(0.0, -3.0, 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, target));
            if (clip.getType() == HitResult.Type.BLOCK) {
                dy += clip.m_82450_().y() - target.m_20186_();
                completed = true;
            }
        }
        if (!completed) {
            double rt = Math.sqrt(c) / v;
            Vec3 tv = target.m_20184_();
            dx += tv.x * rt;
            dy += tv.y * rt;
            dz += tv.z * rt;
        }
        c = dx * dx + dz * dz + dy * dy;
        if (g > 0.0 && c > v * v * 4.0) {
            double a = g * g / 4.0;
            double b = dy * g - v * v;
            double delta = b * b - 4.0 * a * c;
            if (delta > 0.0) {
                double t21 = (-b + Math.sqrt(delta)) / (2.0 * a);
                double t22 = (-b - Math.sqrt(delta)) / (2.0 * a);
                if (t21 > 0.0 || t22 > 0.0) {
                    double t2 = t21 > 0.0 ? (t22 > 0.0 ? Math.min(t21, t22) : t21) : t22;
                    arrow.shoot(dx, dy + g * t2 / 2.0, dz, (float) v, 0.0F);
                    return;
                }
            }
        }
        arrow.shoot(dx, dy, dz, (float) v, 0.0F);
    }
}