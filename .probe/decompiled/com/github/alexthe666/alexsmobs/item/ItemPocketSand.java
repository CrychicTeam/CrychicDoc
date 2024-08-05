package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.entity.EntitySandShot;
import java.util.function.Predicate;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class ItemPocketSand extends Item {

    public static final Predicate<ItemStack> IS_SAND = stack -> stack.is(ItemTags.SAND);

    public ItemPocketSand(Item.Properties properties) {
        super(properties);
    }

    public ItemStack findAmmo(Player entity) {
        if (entity.isCreative()) {
            return ItemStack.EMPTY;
        } else {
            for (int i = 0; i < entity.getInventory().getContainerSize(); i++) {
                ItemStack itemstack1 = entity.getInventory().getItem(i);
                if (IS_SAND.test(itemstack1)) {
                    return itemstack1;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player livingEntityIn, InteractionHand handIn) {
        ItemStack itemstack = livingEntityIn.m_21120_(handIn);
        ItemStack ammo = this.findAmmo(livingEntityIn);
        if (livingEntityIn.isCreative()) {
            ammo = new ItemStack(Items.SAND);
        }
        if (!worldIn.isClientSide && !ammo.isEmpty()) {
            livingEntityIn.m_146850_(GameEvent.ITEM_INTERACT_START);
            worldIn.playSound((Player) null, livingEntityIn.m_20185_(), livingEntityIn.m_20186_(), livingEntityIn.m_20189_(), SoundEvents.SAND_BREAK, SoundSource.PLAYERS, 0.5F, 0.4F + livingEntityIn.m_217043_().nextFloat() * 0.4F + 0.8F);
            boolean left = false;
            if (livingEntityIn.m_7655_() == InteractionHand.OFF_HAND && livingEntityIn.getMainArm() == HumanoidArm.RIGHT || livingEntityIn.m_7655_() == InteractionHand.MAIN_HAND && livingEntityIn.getMainArm() == HumanoidArm.LEFT) {
                left = true;
            }
            EntitySandShot blood = new EntitySandShot(worldIn, livingEntityIn, !left);
            Vec3 vector3d = livingEntityIn.m_20252_(1.0F);
            blood.shoot(vector3d.x(), vector3d.y(), vector3d.z(), 1.2F, 11.0F);
            if (!worldIn.isClientSide) {
                worldIn.m_7967_(blood);
            }
            livingEntityIn.getCooldowns().addCooldown(this, 2);
            ammo.shrink(1);
            itemstack.hurtAndBreak(1, livingEntityIn, player -> player.m_21190_(livingEntityIn.m_7655_()));
        }
        livingEntityIn.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
    }
}