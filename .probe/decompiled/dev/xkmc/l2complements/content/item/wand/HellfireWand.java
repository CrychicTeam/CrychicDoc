package dev.xkmc.l2complements.content.item.wand;

import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HellfireWand extends WandItem {

    public static final int RANGE = 64;

    public static final int CHARGE = 200;

    public static final int SIZE = 10;

    public HellfireWand(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        player.m_6672_(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remain) {
        if (user instanceof Player player) {
            BlockHitResult result = RayTraceUtil.rayTraceBlock(level, player, 64.0);
            Vec3 center = result.m_82450_();
            int time = Math.min(200, this.getUseDuration(stack) - remain);
            double radius = (double) time * 1.0 * 10.0 / 200.0;
            if (level.isClientSide()) {
                for (int i = 0; i < 5; i++) {
                    float tpi = (float) (Math.PI * 2);
                    Vec3 v0 = new Vec3(0.0, radius, 0.0);
                    v0 = v0.xRot(tpi / 4.0F).yRot(level.getRandom().nextFloat() * tpi);
                    level.addAlwaysVisibleParticle(ParticleTypes.FLAME, center.x + v0.x, center.y + v0.y + 0.5, center.z + v0.z, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remain) {
        if (user instanceof Player player) {
            stack.hurtAndBreak(1, user, ex -> ex.broadcastBreakEvent(ex.getUsedItemHand()));
            BlockHitResult result = RayTraceUtil.rayTraceBlock(level, player, 64.0);
            Vec3 center = result.m_82450_();
            level.playSound(player, center.x, center.y, center.z, SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 3.0F, 1.0F);
            int time = Math.min(200, this.getUseDuration(stack) - remain);
            double radius = (double) time * 1.0 * 10.0 / 200.0;
            if (level.isClientSide()) {
                double side = 1.644;
                double perimeter = Math.PI * 2;
                double total = side * 5.0 + perimeter;
                int size = time * 2;
                for (int i = 0; i < size; i++) {
                    double perc = (double) i * 1.0 / (double) size * total;
                    if (perc < side * 5.0) {
                        int start = (int) Math.floor(perc / side);
                        Vec3 tip = new Vec3(0.0, radius, 0.0);
                        tip = tip.xRot((float) (Math.PI / 2)).yRot((float) ((Math.PI * 4.0 / 5.0) * (double) start));
                        Vec3 next = tip.yRot((float) (Math.PI * 4.0 / 5.0));
                        Vec3 v0 = tip.add(next.subtract(tip).scale(perc / side - (double) start));
                        level.addAlwaysVisibleParticle(ParticleTypes.SOUL, center.x + v0.x, center.y + v0.y + 0.5, center.z + v0.z, 0.0, 1.0, 0.0);
                    } else {
                        Vec3 v0 = new Vec3(0.0, radius, 0.0);
                        v0 = v0.xRot((float) (Math.PI / 2)).yRot((float) (perc - side * 5.0));
                        level.addAlwaysVisibleParticle(ParticleTypes.SOUL_FIRE_FLAME, center.x + v0.x, center.y + v0.y + 0.5, center.z + v0.z, 0.0, 1.0, 0.0);
                    }
                }
            }
            if (level instanceof ServerLevel sl) {
                for (Entity e : level.m_45933_(user, AABB.ofSize(center.add(0.0, radius, 0.0), radius * 2.0, radius * 2.0, radius * 2.0))) {
                    if (e instanceof LivingEntity x) {
                        x.hurt(sl.m_269111_().indirectMagic(null, user), (float) LCConfig.COMMON.hellfireWandDamage.get().intValue());
                    }
                }
            }
            player.getCooldowns().addCooldown(this, 40);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.IDS.HELLFIRE_WAND.get().withStyle(ChatFormatting.GRAY));
    }
}