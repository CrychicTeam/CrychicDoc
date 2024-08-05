package se.mickelus.tetra.effect;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;
import se.mickelus.tetra.ServerScheduler;

public class JankEffect {

    public static void jankItemsDelayed(ServerLevel level, BlockPos target, int effectLevel, float efficiency, Entity entity) {
        ServerScheduler.schedule(0, () -> jankItems(level, target, effectLevel, efficiency, entity));
    }

    public static void jankItems(ServerLevel level, BlockPos target, int effectLevel, float efficiency, Entity entity) {
        List<ItemEntity> items = level.m_142425_(EntityType.ITEM, new AABB(target).inflate((double) effectLevel * 0.5), Entity::m_6084_);
        if (!items.isEmpty() && level.f_46441_.nextFloat() < efficiency && level.m_46469_().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            Endermite endermite = EntityType.ENDERMITE.create(level);
            endermite.m_20035_(target, 0.0F, entity.getXRot() + 180.0F);
            level.addFreshEntity(endermite);
        }
        items.forEach(item -> {
            level.sendParticles(ParticleTypes.REVERSE_PORTAL, item.m_20185_() + (double) (item.m_20205_() / 2.0F), item.m_20186_() + (double) (item.m_20206_() / 2.0F), item.m_20189_() + (double) (item.m_20205_() / 2.0F), 1, 0.0, 0.0, 0.0, 0.0);
            item.m_20219_(entity.getPosition(0.0F));
            item.setPickUpDelay(0);
        });
        level.m_142425_(EntityType.EXPERIENCE_ORB, new AABB(target).inflate((double) effectLevel * 0.5), Entity::m_6084_).forEach(orb -> orb.m_20219_(entity.getPosition(0.0F)));
    }
}