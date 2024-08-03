package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.util.Proxy;
import javax.annotation.Nullable;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientGlowingHandler {

    private static int cacheTick;

    private static boolean cacheGlass;

    public static boolean isGlowing(Entity entity) {
        if (!(entity instanceof LivingEntity le)) {
            return false;
        } else {
            if (le instanceof Mob mob && MobTraitCap.HOLDER.isProper(mob)) {
                MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
                if (cap.isSummoned() || cap.isMasterProtected()) {
                    return true;
                }
            }
            return le.m_9236_().isClientSide() ? isGlowingImpl(le) : false;
        }
    }

    private static boolean playerHasGlass(Player player) {
        if (player.f_19797_ == cacheTick) {
            return cacheGlass;
        } else {
            cacheGlass = CurioCompat.hasItemInCurioOrSlot(player, (Item) LHItems.DETECTOR_GLASSES.get());
            cacheTick = player.f_19797_;
            return cacheGlass;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean isGlowingImpl(LivingEntity entity) {
        LocalPlayer player = Proxy.getClientPlayer();
        if (player != null && playerHasGlass(player)) {
            boolean glow = entity.m_20145_() || entity.m_20177_(player);
            glow |= player.m_21023_(MobEffects.BLINDNESS);
            glow |= player.m_21023_(MobEffects.DARKNESS);
            float hidden = (float) LHConfig.CLIENT.glowingRangeHidden.get().intValue() + entity.m_20205_() * 2.0F;
            float near = (float) LHConfig.CLIENT.glowingRangeNear.get().intValue() + entity.m_20205_() * 2.0F;
            double distSqr = entity.m_20280_(player);
            return distSqr <= (double) (near * near) || glow && distSqr <= (double) (hidden * hidden);
        } else {
            return false;
        }
    }

    @Nullable
    public static Integer getColor(Entity entity) {
        if (entity instanceof Mob mob && MobTraitCap.HOLDER.isProper(mob)) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(mob);
            if (cap.isSummoned()) {
                return 16711680;
            }
            if (cap.isMasterProtected()) {
                return 16755200;
            }
        }
        return null;
    }
}