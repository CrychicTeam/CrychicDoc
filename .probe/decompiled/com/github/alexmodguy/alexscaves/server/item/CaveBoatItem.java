package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.server.entity.item.AlexsCavesBoatEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.AlexsCavesChestBoatEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.AlexsCavesBoat;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CaveBoatItem extends Item {

    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::m_6087_);

    private final AlexsCavesBoat.Type type;

    private final boolean hasChest;

    public CaveBoatItem(boolean hasChest, AlexsCavesBoat.Type type, Item.Properties properties) {
        super(properties);
        this.type = type;
        this.hasChest = hasChest;
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
                Boat boat = (Boat) (this.hasChest ? new AlexsCavesChestBoatEntity(level, hitresult.getLocation(), this.type) : new AlexsCavesBoatEntity(level, hitresult.getLocation(), this.type));
                boat.m_146922_(player.m_146908_());
                if (!level.m_45756_(boat, boat.m_20191_())) {
                    return InteractionResultHolder.fail(itemstack);
                } else {
                    if (!level.isClientSide) {
                        level.m_7967_(boat);
                        level.m_220400_(player, GameEvent.ENTITY_PLACE, hitresult.getLocation());
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