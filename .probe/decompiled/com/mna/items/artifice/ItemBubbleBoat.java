package com.mna.items.artifice;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.items.TieredItem;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.entities.constructs.BubbleBoat;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ItemBubbleBoat extends TieredItem {

    public static final int UPGRADE_NONE = 0;

    public static final int UPGRADE_LAVA = 1;

    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::m_6087_);

    private int upgrade;

    public ItemBubbleBoat(int upgrade) {
        super(new Item.Properties().stacksTo(1));
        this.upgrade = upgrade;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFireResistant() {
        return this.upgrade == 1;
    }

    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return stack.getItem() == this && armorType == EquipmentSlot.HEAD;
    }

    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (player.m_20069_() && player.m_21124_(MobEffects.WATER_BREATHING) == null) {
            player.m_7292_(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false));
        }
        if (this.upgrade == 1 && (player.m_20077_() || player.m_6060_()) && player.m_21124_(MobEffects.FIRE_RESISTANCE) == null) {
            player.m_7292_(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0, false, false));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        IPlayerMagic magic = (IPlayerMagic) playerIn.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if (!magic.isMagicUnlocked()) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            HitResult raytraceresult = m_41435_(worldIn, playerIn, ClipContext.Fluid.ANY);
            if (raytraceresult.getType() == HitResult.Type.MISS) {
                return InteractionResultHolder.pass(itemstack);
            } else {
                Vec3 vec3d = playerIn.m_20252_(1.0F);
                List<Entity> list = worldIn.getEntities(playerIn, playerIn.m_20191_().expandTowards(vec3d.scale(5.0)).inflate(1.0), ENTITY_PREDICATE);
                if (!list.isEmpty()) {
                    Vec3 vec3d1 = playerIn.m_20299_(1.0F);
                    for (Entity entity : list) {
                        AABB axisalignedbb = entity.getBoundingBox().inflate((double) entity.getPickRadius());
                        if (axisalignedbb.contains(vec3d1)) {
                            return InteractionResultHolder.pass(itemstack);
                        }
                    }
                }
                if (raytraceresult.getType() == HitResult.Type.BLOCK) {
                    BubbleBoat boatentity = new BubbleBoat(worldIn, raytraceresult.getLocation().x, raytraceresult.getLocation().y + 0.5, raytraceresult.getLocation().z);
                    if (this.upgrade == 1) {
                        boatentity = boatentity.setBrimstone();
                    }
                    boatentity.m_146922_(playerIn.m_146908_());
                    if (!worldIn.m_45756_(boatentity, boatentity.m_20191_().inflate(-0.1))) {
                        return InteractionResultHolder.fail(itemstack);
                    } else {
                        if (!worldIn.isClientSide) {
                            worldIn.m_7967_(boatentity);
                            if (!playerIn.getAbilities().instabuild) {
                                itemstack.shrink(1);
                            }
                        }
                        playerIn.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.success(itemstack);
                    }
                } else {
                    return InteractionResultHolder.pass(itemstack);
                }
            }
        }
    }
}