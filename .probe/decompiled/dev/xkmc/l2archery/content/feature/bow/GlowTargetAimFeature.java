package dev.xkmc.l2archery.content.feature.bow;

import dev.xkmc.l2archery.content.feature.types.OnPullFeature;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2library.util.raytrace.EntityTarget;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public record GlowTargetAimFeature(int range) implements OnPullFeature, IGlowFeature {

    public static final EntityTarget TARGET = new EntityTarget(3.0, 0.08726646259971647, 2);

    @Override
    public void tickAim(LivingEntity player, GenericItemStack<GenericBowItem> bow) {
        if (player.m_9236_().isClientSide()) {
            Vec3 vec3 = player.m_146892_();
            Vec3 vec31 = player.m_20252_(1.0F).scale((double) this.range);
            Vec3 vec32 = vec3.add(vec31);
            AABB aabb = player.m_20191_().expandTowards(vec31).inflate(1.0);
            int sq = this.range * this.range;
            Predicate<Entity> predicate = e -> e instanceof LivingEntity && !e.isSpectator();
            EntityHitResult result = ProjectileUtil.getEntityHitResult(player, vec3, vec32, aabb, predicate, (double) sq);
            if (result != null && vec3.distanceToSqr(result.m_82450_()) < (double) sq) {
                TARGET.updateTarget(result.getEntity());
            }
        }
    }

    @Override
    public void stopAim(LivingEntity player, GenericItemStack<GenericBowItem> bow) {
        TARGET.updateTarget(null);
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
        list.add(LangData.FEATURE_AIM_GLOW.get(this.range));
    }
}