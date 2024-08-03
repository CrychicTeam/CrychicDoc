package dev.xkmc.l2complements.content.item.wand;

import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2library.base.effects.EffectUtil;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WinterStormWand extends Item {

    public static final int RANGE = 64;

    public static final int CHARGE = 100;

    public static final int SIZE_0 = 3;

    public static final int SIZE_1 = 7;

    public WinterStormWand(Item.Properties properties) {
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
            Vec3 center = player.m_20182_();
            int time = Math.min(100, this.getUseDuration(stack) - remain);
            if (remain % 20 == 0) {
                stack.hurtAndBreak(1, user, ex -> ex.broadcastBreakEvent(ex.getUsedItemHand()));
            }
            double radius = 3.0 + (double) time * 1.0 * 7.0 / 100.0;
            if (level.isClientSide()) {
                for (int i = 0; i < 5; i++) {
                    float tpi = (float) (Math.PI * 2);
                    Vec3 v = new Vec3(0.0, 1.0, 0.0);
                    v = v.xRot(tpi / 4.0F).yRot(level.getRandom().nextFloat() * tpi);
                    Vec3 v0 = v.scale(radius);
                    Vec3 v1 = v.yRot(tpi * 0.375F);
                    level.addAlwaysVisibleParticle(ParticleTypes.SNOWFLAKE, center.x + v0.x, center.y + v0.y + 0.5, center.z + v0.z, v1.x, v1.y, v1.z);
                }
            } else {
                for (LivingEntity e : player.m_9236_().getEntities(EntityTypeTest.forClass(LivingEntity.class), player.m_20191_().inflate(radius), ex -> ex instanceof Mob)) {
                    double dist = (double) player.m_20270_(e) / radius;
                    if (!(dist > 1.0)) {
                        Vec3 vec = e.m_20182_().subtract(player.m_20182_()).normalize().scale((1.0 - dist) * 0.2);
                        e.m_5997_(vec.x, vec.y, vec.z);
                        if (e.m_146888_() < 140) {
                            e.m_146917_(140);
                        }
                        EffectUtil.refreshEffect(e, new MobEffectInstance((MobEffect) LCEffects.ICE.get(), 40), EffectUtil.AddReason.NONE, player);
                    }
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remain) {
        if (user instanceof Player) {
            stack.hurtAndBreak(1, user, e -> e.broadcastBreakEvent(e.getUsedItemHand()));
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
        list.add(LangData.IDS.WINTERSTORM_WAND.get().withStyle(ChatFormatting.GRAY));
    }
}