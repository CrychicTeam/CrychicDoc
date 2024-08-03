package net.mehvahdjukaar.amendments.events;

import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.events.behaviors.InteractEvents;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.SoulFiredCompat;
import net.mehvahdjukaar.amendments.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.block.ILightable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModEvents {

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return !player.isSpectator() ? InteractEvents.onItemUsedOnBlock(player, level, player.m_21120_(hand), hand, hitResult) : InteractionResult.PASS;
    }

    public static InteractionResult onRightClickBlockHP(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return !player.isSpectator() ? InteractEvents.onItemUsedOnBlockHP(player, level, player.m_21120_(hand), hand, hitResult) : InteractionResult.PASS;
    }

    public static InteractionResultHolder<ItemStack> onUseItem(Player player, Level level, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        return !player.isSpectator() ? InteractEvents.onItemUse(player, level, hand, stack) : InteractionResultHolder.pass(stack);
    }

    public static InteractionResult onAttackEntity(Player player, Level level, InteractionHand hand, Entity target, @Nullable EntityHitResult entityHitResult) {
        ItemStack stack = player.m_21120_(hand);
        if ((Boolean) CommonConfigs.TORCH_FIRE_OFFHAND.get() && stack.getItem().getDefaultAttributeModifiers(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND).containsKey(Attributes.ATTACK_DAMAGE)) {
            ItemStack offHand = hand == InteractionHand.MAIN_HAND ? player.m_21206_() : player.m_21205_();
            InteractionResult ret = torchEntity(player, level, target, offHand);
            if (ret.consumesAction()) {
                return ret;
            }
        }
        return torchEntity(player, level, target, stack);
    }

    @NotNull
    private static InteractionResult torchEntity(Player player, Level level, Entity target, ItemStack stack) {
        if (stack.is(ModTags.SET_ENTITY_ON_FIRE) && target.isAttackable() && !target.skipAttackInteraction(player) && target instanceof LivingEntity && !target.isOnFire()) {
            int duration = (Integer) CommonConfigs.TORCH_FIRE_DURATION.get();
            if (CompatHandler.SOUL_FIRED) {
                SoulFiredCompat.setSecondsOnFire(target, duration, stack);
            } else {
                target.setSecondsOnFire(duration);
            }
            if (stack.is(ILightable.FLINT_AND_STEELS)) {
                target.playSound(SoundEvents.FLINTANDSTEEL_USE, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            } else {
                target.playSound(SoundEvents.FIRECHARGE_USE, 0.5F, 1.3F + level.getRandom().nextFloat() * 0.2F);
            }
        }
        return InteractionResult.PASS;
    }
}