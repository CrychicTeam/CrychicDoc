package se.mickelus.tetra.effect;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;

public class FocusEffect {

    private static final Cache<Integer, Integer> cache = CacheBuilder.newBuilder().maximumSize(20L).expireAfterWrite(30L, TimeUnit.SECONDS).build();

    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.m_9236_().getGameTime() % 2L == 0L) {
            if (hasApplicableItem(event.player) && event.player.m_6047_()) {
                Player player = event.player;
                int id = getIdentifier(player);
                Integer duration = (Integer) cache.getIfPresent(id);
                boolean isDrawing = isDrawing(event.player);
                int change = isDrawing ? 1 : 2;
                cache.put(id, duration != null ? duration + change : change);
                if (!player.m_9236_().isClientSide) {
                    int respiration = EnchantmentHelper.getRespiration(player);
                    int amount = isDrawing ? 6 : 2;
                    int reduction = 0;
                    if (respiration > 0) {
                        for (int i = 0; i < amount - 1; i++) {
                            if (player.m_217043_().nextInt(respiration + 2) > 1) {
                                reduction++;
                            }
                        }
                    }
                    player.m_20301_(player.m_20146_() - (amount - reduction));
                    if (player.m_20146_() <= -20) {
                        player.m_20301_(0);
                        player.hurt(player.m_9236_().damageSources().drown(), 2.0F);
                    }
                }
            } else {
                cache.invalidate(getIdentifier(event.player));
            }
        }
    }

    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getAmount() > 0.0F && !event.getSource().is(DamageTypes.DROWN) && event.getEntity() instanceof Player player) {
            cache.invalidate(getIdentifier(player));
        }
    }

    public static void onFireArrow(Player player, ItemStack itemStack) {
        boolean hasEcho = CastOptional.cast(itemStack.getItem(), ModularItem.class).filter(item -> item.getEffectLevel(itemStack, ItemEffect.focusEcho) > 0).isPresent();
        if (!hasEcho) {
            cache.invalidate(getIdentifier(player));
        }
    }

    public static boolean isDrawing(Player player) {
        ItemStack itemStack = player.m_21211_();
        return (Boolean) CastOptional.cast(itemStack.getItem(), ModularBowItem.class).map(item -> item.getProgress(itemStack, player) < 1.0F).orElse(false);
    }

    public static float getSpreadReduction(Player player, ItemStack itemStack) {
        Integer duration = (Integer) cache.getIfPresent(getIdentifier(player));
        return duration != null ? (Float) CastOptional.cast(itemStack.getItem(), ModularItem.class).map(item -> item.getEffectEfficiency(itemStack, ItemEffect.focus) * (float) duration.intValue() / 20.0F).orElse(0.0F) : 0.0F;
    }

    public static boolean hasApplicableItem(LivingEntity player) {
        return isApplicableItem(player.getMainHandItem()) || isApplicableItem(player.getOffhandItem());
    }

    public static boolean isApplicableItem(ItemStack itemStack) {
        return CastOptional.cast(itemStack.getItem(), ModularItem.class).filter(item -> item.getEffectEfficiency(itemStack, ItemEffect.focus) > 0.0F).isPresent();
    }

    private static int getIdentifier(Player entity) {
        return entity.m_9236_().isClientSide ? -entity.m_19879_() : entity.m_19879_();
    }
}