package net.minecraft.world.item;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BoatItem extends Item {

    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::m_6087_);

    private final Boat.Type type;

    private final boolean hasChest;

    public BoatItem(boolean boolean0, Boat.Type boatType1, Item.Properties itemProperties2) {
        super(itemProperties2);
        this.hasChest = boolean0;
        this.type = boatType1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        HitResult $$4 = m_41435_(level0, player1, ClipContext.Fluid.ANY);
        if ($$4.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass($$3);
        } else {
            Vec3 $$5 = player1.m_20252_(1.0F);
            double $$6 = 5.0;
            List<Entity> $$7 = level0.getEntities(player1, player1.m_20191_().expandTowards($$5.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
            if (!$$7.isEmpty()) {
                Vec3 $$8 = player1.m_146892_();
                for (Entity $$9 : $$7) {
                    AABB $$10 = $$9.getBoundingBox().inflate((double) $$9.getPickRadius());
                    if ($$10.contains($$8)) {
                        return InteractionResultHolder.pass($$3);
                    }
                }
            }
            if ($$4.getType() == HitResult.Type.BLOCK) {
                Boat $$11 = this.getBoat(level0, $$4);
                $$11.setVariant(this.type);
                $$11.m_146922_(player1.m_146908_());
                if (!level0.m_45756_($$11, $$11.m_20191_())) {
                    return InteractionResultHolder.fail($$3);
                } else {
                    if (!level0.isClientSide) {
                        level0.m_7967_($$11);
                        level0.m_220400_(player1, GameEvent.ENTITY_PLACE, $$4.getLocation());
                        if (!player1.getAbilities().instabuild) {
                            $$3.shrink(1);
                        }
                    }
                    player1.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess($$3, level0.isClientSide());
                }
            } else {
                return InteractionResultHolder.pass($$3);
            }
        }
    }

    private Boat getBoat(Level level0, HitResult hitResult1) {
        return (Boat) (this.hasChest ? new ChestBoat(level0, hitResult1.getLocation().x, hitResult1.getLocation().y, hitResult1.getLocation().z) : new Boat(level0, hitResult1.getLocation().x, hitResult1.getLocation().y, hitResult1.getLocation().z));
    }
}