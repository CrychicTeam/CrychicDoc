package dev.xkmc.l2archery.content.feature.bow;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.types.OnPullFeature;
import dev.xkmc.l2archery.content.feature.types.OnShootFeature;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public record EnderShootFeature(int range) implements OnShootFeature, OnPullFeature, IGlowFeature {

    @Override
    public boolean onShoot(@Nullable LivingEntity player, Consumer<Consumer<GenericArrowEntity>> consumer) {
        if (player == null) {
            return false;
        } else {
            Entity target = null;
            if (player instanceof Player pl) {
                target = RayTraceUtil.serverGetTarget(pl);
            } else if (player instanceof Mob mob) {
                target = mob.getTarget();
            }
            if (target == null) {
                return false;
            } else {
                Entity finalTarget = target;
                consumer.accept((Consumer) entity -> {
                    float w = finalTarget.getBbWidth();
                    float h = finalTarget.getBbHeight();
                    Vec3 dst = finalTarget.position().add(0.0, (double) (h / 2.0F), 0.0);
                    double r = Math.sqrt((double) (w * w * 2.0F + h * h)) / 2.0;
                    Vec3 src = dst.add(entity.m_20184_().normalize().scale(-r - 1.0));
                    entity.m_146884_(src);
                });
                return true;
            }
        }
    }

    @Override
    public void tickAim(LivingEntity player, GenericItemStack<GenericBowItem> bow) {
        if (player instanceof Player pl) {
            RayTraceUtil.clientUpdateTarget(pl, (double) this.range);
        }
    }

    @Override
    public void stopAim(LivingEntity player, GenericItemStack<GenericBowItem> bow) {
        if (player.m_9236_().isClientSide() && player instanceof LocalPlayer) {
            RayTraceUtil.TARGET.updateTarget(null);
        }
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
        list.add(LangData.FEATURE_ENDER_SHOOT.get());
    }
}