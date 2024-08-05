package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.QuarrySmasherEntity;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class QuarrySmasherItem extends Item {

    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::m_6087_);

    public QuarrySmasherItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        HitResult hitresult = m_41435_(level, player, ClipContext.Fluid.ANY);
        if (hitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            Vec3 vec3 = player.m_20252_(1.0F);
            double d0 = 5.0;
            List<Entity> list = level.getEntities(player, player.m_20191_().expandTowards(vec3.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
            if (!list.isEmpty()) {
                Vec3 vec31 = player.m_146892_();
                for (Entity entity : list) {
                    AABB aabb = entity.getBoundingBox().inflate((double) entity.getPickRadius());
                    if (aabb.contains(vec31)) {
                        return InteractionResultHolder.pass(itemstack);
                    }
                }
            }
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                Vec3 vec31 = hitresult.getLocation();
                QuarrySmasherEntity smasherEntity = ACEntityRegistry.QUARRY_SMASHER.get().create(level);
                smasherEntity.m_6034_(vec31.x, vec31.y, vec31.z);
                smasherEntity.m_146922_(player.m_146908_());
                smasherEntity.setInactive(true);
                if (!level.m_45756_(smasherEntity, smasherEntity.m_20191_())) {
                    return InteractionResultHolder.fail(itemstack);
                } else {
                    if (!level.isClientSide) {
                        level.m_7967_(smasherEntity);
                        level.m_220400_(player, GameEvent.ENTITY_PLACE, vec31);
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                }
            } else {
                return InteractionResultHolder.pass(itemstack);
            }
        }
    }
}