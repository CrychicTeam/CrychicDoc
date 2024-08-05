package dev.xkmc.l2complements.content.item.wand;

import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2library.util.raytrace.IGlowingTarget;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SonicShooter extends WandItem implements IGlowingTarget {

    private static final int RANGE = 17;

    public SonicShooter(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide() && selected && entity instanceof Player player) {
            RayTraceUtil.clientUpdateTarget(player, 17.0);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        player.m_6672_(hand);
        player.playSound(SoundEvents.WARDEN_SONIC_CHARGE, 3.0F, 1.0F);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (level instanceof ServerLevel sl) {
            int size = 1;
            Vec3 src = user.m_146892_();
            Vec3 dst = RayTraceUtil.getRayTerm(src, user.m_146909_(), user.m_146908_(), 17.0);
            Vec3 dir = dst.subtract(src).normalize();
            for (int i = 1; i < 17; i++) {
                Vec3 vec33 = src.add(dir.scale((double) i));
                sl.sendParticles(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
            List<LivingEntity> target = new ArrayList();
            AABB aabb = new AABB(src, src.add(dir.scale(17.0)));
            for (Entity e : level.m_45933_(user, aabb)) {
                if (e instanceof LivingEntity) {
                    LivingEntity x = (LivingEntity) e;
                    AABB box = x.m_20191_().inflate((double) size);
                    for (int i = 0; i <= 17; i++) {
                        if (box.contains(src.add(dir.scale((double) i)))) {
                            target.add(x);
                            break;
                        }
                    }
                }
            }
            for (LivingEntity ex : target) {
                ex.hurt(sl.m_269111_().sonicBoom(user), (float) LCConfig.COMMON.sonicShooterDamage.get().intValue());
                double d1 = 0.5 * (1.0 - ex.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                double d0 = 2.5 * (1.0 - ex.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                ex.m_5997_(dir.x() * d0, dir.y() * d1, dir.z() * d0);
            }
        }
        user.m_5496_(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);
        stack.hurtAndBreak(1, user, ex -> ex.broadcastBreakEvent(ex.getUsedItemHand()));
        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 34;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getDistance(ItemStack itemStack) {
        return 17;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.IDS.SONIC_SHOOTER.get().withStyle(ChatFormatting.GRAY));
    }
}