package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityTendonSegment;
import com.github.alexthe666.alexsmobs.entity.util.TendonWhipUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ItemTendonWhip extends SwordItem implements ILeftClick {

    private final ImmutableMultimap<Attribute, AttributeModifier> tendonModifiers;

    public ItemTendonWhip(Item.Properties props) {
        super(Tiers.IRON, 3, 0.0F, props);
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", 4.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", -3.0, AttributeModifier.Operation.ADDITION));
        this.tendonModifiers = builder.build();
    }

    public static boolean isActive(ItemStack stack, LivingEntity holder) {
        return holder != null && (holder.getMainHandItem() == stack || holder.getOffhandItem() == stack) ? !TendonWhipUtil.canLaunchTendons(holder.m_9236_(), holder) : false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return (Multimap<Attribute, AttributeModifier>) (slot == EquipmentSlot.MAINHAND ? this.tendonModifiers : super.getDefaultAttributeModifiers(slot));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity player) {
        this.launchTendonsAt(stack, player, entity);
        return super.hurtEnemy(stack, entity, player);
    }

    private boolean isCharged(Player player, ItemStack stack) {
        return player.getAttackStrengthScale(0.5F) > 0.9F;
    }

    @Override
    public boolean onLeftClick(ItemStack stack, LivingEntity playerIn) {
        if (stack.is(AMItemRegistry.TENDON_WHIP.get()) && (!(playerIn instanceof Player) || this.isCharged((Player) playerIn, stack))) {
            Level worldIn = playerIn.m_9236_();
            Entity closestValid = null;
            Vec3 playerEyes = playerIn.m_20299_(1.0F);
            HitResult hitresult = worldIn.m_45547_(new ClipContext(playerEyes, playerEyes.add(playerIn.m_20154_().scale(12.0)), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, playerIn));
            if (hitresult instanceof EntityHitResult) {
                Entity entity = ((EntityHitResult) hitresult).getEntity();
                if (!entity.equals(playerIn) && !playerIn.m_7307_(entity) && !entity.isAlliedTo(playerIn) && entity instanceof Mob && playerIn.hasLineOfSight(entity)) {
                    closestValid = entity;
                }
            } else {
                for (Entity entity : worldIn.m_45976_(LivingEntity.class, playerIn.m_20191_().inflate(12.0))) {
                    if (!entity.equals(playerIn) && !playerIn.m_7307_(entity) && !entity.isAlliedTo(playerIn) && entity instanceof Mob && playerIn.hasLineOfSight(entity) && (closestValid == null || playerIn.m_20270_(entity) < playerIn.m_20270_(closestValid))) {
                        closestValid = entity;
                    }
                }
            }
            if (closestValid != null) {
                stack.hurtAndBreak(1, playerIn, player -> player.broadcastBreakEvent(playerIn.getUsedItemHand()));
            }
            return this.launchTendonsAt(stack, playerIn, closestValid);
        } else {
            return false;
        }
    }

    public boolean launchTendonsAt(ItemStack stack, LivingEntity playerIn, Entity closestValid) {
        Level worldIn = playerIn.m_9236_();
        if (TendonWhipUtil.canLaunchTendons(worldIn, playerIn)) {
            TendonWhipUtil.retractFarTendons(worldIn, playerIn);
            if (!worldIn.isClientSide && closestValid != null) {
                EntityTendonSegment segment = AMEntityRegistry.TENDON_SEGMENT.get().create(worldIn);
                segment.m_20359_(playerIn);
                worldIn.m_7967_(segment);
                segment.setCreatorEntityUUID(playerIn.m_20148_());
                segment.setFromEntityID(playerIn.m_19879_());
                segment.setToEntityID(closestValid.getId());
                segment.m_20359_(playerIn);
                segment.setProgress(0.0F);
                segment.setHasGlint(stack.hasFoil());
                TendonWhipUtil.setLastTendon(playerIn, segment);
                return true;
            }
        }
        return false;
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return toolAction != ToolActions.SWORD_SWEEP && super.canPerformAction(stack, toolAction);
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }

    public int getMaxDamage(ItemStack stack) {
        return 450;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pickaxe, ItemStack stack) {
        return stack.is(AMItemRegistry.ELASTIC_TENDON.get());
    }
}