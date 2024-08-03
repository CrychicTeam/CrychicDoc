package team.lodestar.lodestone.systems.item;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.systems.entity.LodestoneBoatEntity;

public class LodestoneBoatItem extends Item {

    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::m_6087_);

    private final RegistryObject<EntityType<LodestoneBoatEntity>> boat;

    public LodestoneBoatItem(Item.Properties properties, RegistryObject<EntityType<LodestoneBoatEntity>> boat) {
        super(properties);
        this.boat = boat;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        HitResult raytraceresult = m_41435_(level, playerIn, ClipContext.Fluid.ANY);
        if (raytraceresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            Vec3 vector3d = playerIn.m_20252_(1.0F);
            List<Entity> list = level.getEntities(playerIn, playerIn.m_20191_().expandTowards(vector3d.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
            if (!list.isEmpty()) {
                Vec3 vector3d1 = playerIn.m_20299_(1.0F);
                for (Entity entity : list) {
                    AABB AABB = entity.getBoundingBox().inflate((double) entity.getPickRadius());
                    if (AABB.contains(vector3d1)) {
                        return InteractionResultHolder.pass(itemstack);
                    }
                }
            }
            if (raytraceresult.getType() == HitResult.Type.BLOCK) {
                LodestoneBoatEntity boatEntity = this.boat.get().create(level);
                boatEntity.m_6034_(raytraceresult.getLocation().x, raytraceresult.getLocation().y, raytraceresult.getLocation().z);
                boatEntity.m_146922_(playerIn.m_146908_());
                if (!level.m_45756_(boatEntity, boatEntity.m_20191_().inflate(-0.1))) {
                    return InteractionResultHolder.fail(itemstack);
                } else {
                    if (!level.isClientSide) {
                        level.m_7967_(boatEntity);
                        if (!playerIn.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                    }
                    playerIn.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                }
            } else {
                return InteractionResultHolder.pass(itemstack);
            }
        }
    }
}