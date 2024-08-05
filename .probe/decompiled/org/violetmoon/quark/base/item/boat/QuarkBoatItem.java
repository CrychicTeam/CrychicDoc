package org.violetmoon.quark.base.item.boat;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.handler.WoodSetHandler;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;

public class QuarkBoatItem extends ZetaItem {

    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::m_6087_);

    public final String type;

    private final boolean chest;

    public QuarkBoatItem(String type, ZetaModule module, boolean chest) {
        super(type + (chest ? "_chest" : "") + "_boat", module, new Item.Properties().stacksTo(1));
        this.type = type;
        this.chest = chest;
        CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.TOOLS_AND_UTILITIES, this, Blocks.RAIL, true);
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        HitResult hitresult = m_41435_(world, player, ClipContext.Fluid.ANY);
        if (hitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            Vec3 view = player.m_20252_(1.0F);
            List<Entity> list = world.getEntities(player, player.m_20191_().expandTowards(view.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
            if (!list.isEmpty()) {
                Vec3 eyes = player.m_146892_();
                for (Entity entity : list) {
                    AABB aabb = entity.getBoundingBox().inflate((double) entity.getPickRadius());
                    if (aabb.contains(eyes)) {
                        return InteractionResultHolder.pass(itemstack);
                    }
                }
            }
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                Boat boat = (Boat) (this.chest ? new QuarkChestBoat(world, hitresult.getLocation().x, hitresult.getLocation().y, hitresult.getLocation().z) : new QuarkBoat(world, hitresult.getLocation().x, hitresult.getLocation().y, hitresult.getLocation().z));
                ((IQuarkBoat) boat).setQuarkBoatTypeObj(WoodSetHandler.getQuarkBoatType(this.type));
                boat.m_146922_(player.m_146908_());
                if (!world.m_45756_(boat, boat.m_20191_())) {
                    return InteractionResultHolder.fail(itemstack);
                } else {
                    if (!world.isClientSide) {
                        world.m_7967_(boat);
                        world.m_142346_(player, GameEvent.ENTITY_PLACE, BlockPos.containing(hitresult.getLocation()));
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide());
                }
            } else {
                return InteractionResultHolder.pass(itemstack);
            }
        }
    }
}