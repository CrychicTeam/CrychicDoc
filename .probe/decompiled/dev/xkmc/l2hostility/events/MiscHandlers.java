package dev.xkmc.l2hostility.events;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.item.consumable.BookCopy;
import dev.xkmc.l2hostility.content.item.wand.IMobClickItem;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

@EventBusSubscriber(modid = "l2hostility", bus = Bus.FORGE)
public class MiscHandlers {

    public static void copyCap(LivingEntity self, LivingEntity sub) {
        if (MobTraitCap.HOLDER.isProper(self) && MobTraitCap.HOLDER.isProper(sub)) {
            MobTraitCap selfCap = (MobTraitCap) MobTraitCap.HOLDER.get(self);
            MobTraitCap subCap = (MobTraitCap) MobTraitCap.HOLDER.get(sub);
            subCap.copyFrom(self, sub, selfCap);
        }
    }

    @SubscribeEvent
    public static void onTargetCardClick(PlayerInteractEvent.EntityInteract event) {
        if (event.getItemStack().getItem() instanceof IMobClickItem && event.getTarget() instanceof LivingEntity le) {
            event.setCancellationResult(event.getItemStack().interactLivingEntity(event.getEntity(), le, event.getHand()));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ItemEntity ie && ie.getItem().getEnchantmentLevel((Enchantment) LHEnchantments.VANISH.get()) > 0) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAnvilCraft(AnvilUpdateEvent event) {
        ItemStack copy = event.getLeft();
        ItemStack book = event.getRight();
        if (copy.getItem() instanceof BookCopy && book.getItem() instanceof EnchantedBookItem) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(book);
            int cost = 0;
            for (Entry<Enchantment, Integer> e : map.entrySet()) {
                cost += BookCopy.cost((Enchantment) e.getKey(), (Integer) e.getValue());
            }
            ItemStack result = book.copy();
            result.setCount(book.getCount() + copy.getCount());
            event.setOutput(result);
            event.setMaterialCost(book.getCount());
            event.setCost(cost);
        }
    }

    public static boolean useOnSkip(UseOnContext ctx, ItemStack stack) {
        Player player = ctx.getPlayer();
        if (player == null) {
            return false;
        } else {
            return !ctx.getPlayer().m_21023_((MobEffect) LHEffects.ANTIBUILD.get()) ? false : stack.getItem() instanceof BlockItem || stack.is(LHTagGen.ANTIBUILD_BAN);
        }
    }

    public static boolean predicateSlotValid(SlotContext slotContext, ItemStack stack) {
        if (stack.hasTag() && stack.getTagElement("sealedItem") != null) {
            CompoundTag ctag = stack.getOrCreateTag().getCompound("sealedItem");
            ItemStack content = ItemStack.of(ctag);
            return CuriosApi.isStackValid(slotContext, content);
        } else {
            return false;
        }
    }
}