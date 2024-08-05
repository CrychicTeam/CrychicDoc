package dev.xkmc.l2complements.events;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2complements.content.feature.EntityFeature;
import dev.xkmc.l2complements.events.event.EnderPickupEvent;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2damagetracker.contents.materials.generic.GenericArmorItem;
import dev.xkmc.l2damagetracker.contents.materials.generic.GenericTieredItem;
import java.util.Optional;
import java.util.Stack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "l2complements", bus = Bus.FORGE)
public class SpecialEquipmentEvents {

    public static ThreadLocal<Stack<Pair<ServerPlayer, BlockState>>> PLAYER = ThreadLocal.withInitial(Stack::new);

    public static boolean isVisible(LivingEntity entity, ItemStack stack) {
        if (entity.m_20145_()) {
            if (stack.getItem() instanceof GenericTieredItem item && item.getExtraConfig().hideWithEffect()) {
                return false;
            }
            if (stack.getItem() instanceof GenericArmorItem item && item.getConfig().hideWithEffect()) {
                return false;
            }
            return stack.getEnchantmentLevel((Enchantment) LCEnchantments.SHULKER_ARMOR.get()) == 0;
        } else {
            return true;
        }
    }

    public static int blockSound(ItemStack stack) {
        if (stack.getItem() instanceof GenericArmorItem item && item.getConfig().dampenVibration()) {
            return 1;
        }
        return stack.getEnchantmentLevel((Enchantment) LCEnchantments.DAMPENED.get());
    }

    private static ItemStack process(Level level, ItemStack stack) {
        ItemStack input = stack.copy();
        SimpleContainer cont = new SimpleContainer(input);
        Optional<SmeltingRecipe> opt = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, cont, level);
        if (opt.isPresent()) {
            ItemStack ans = ((SmeltingRecipe) opt.get()).m_5874_(cont, level.registryAccess());
            int count = ans.getCount() * input.getCount();
            ans.setCount(count);
            return ans;
        } else {
            return stack;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityDrop(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity player && player.getMainHandItem().getEnchantmentLevel((Enchantment) LCEnchantments.SMELT.get()) > 0) {
            for (ItemEntity e : event.getDrops()) {
                ItemStack result = process(player.m_9236_(), e.getItem());
                e.setItem(result);
            }
        }
        if (event.getSource().getEntity() instanceof ServerPlayer player && player.m_21205_().getEnchantmentLevel((Enchantment) LCEnchantments.ENDER.get()) > 0) {
            for (ItemEntity e : event.getDrops()) {
                EnderPickupEvent ender = new EnderPickupEvent(player, e.getItem().copy());
                MinecraftForge.EVENT_BUS.post(ender);
                ItemStack stack = ender.getStack();
                if (!stack.isEmpty() && !player.m_150109_().add(stack)) {
                    e.setItem(stack);
                    e.m_6021_(player.m_20185_(), player.m_20186_(), player.m_20189_());
                } else {
                    e.setItem(ItemStack.EMPTY);
                }
            }
            event.getDrops().removeIf(ex -> ex.getItem().isEmpty());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Stack<Pair<ServerPlayer, BlockState>> players = (Stack<Pair<ServerPlayer, BlockState>>) PLAYER.get();
        if (!players.isEmpty()) {
            ServerPlayer player = (ServerPlayer) ((Pair) players.peek()).getFirst();
            if (event.getEntity() instanceof ItemEntity e) {
                if (player.m_21205_().getEnchantmentLevel((Enchantment) LCEnchantments.SMELT.get()) > 0) {
                    ItemStack result = process(event.getLevel(), e.getItem());
                    e.setItem(result);
                }
                if (player.m_21205_().getEnchantmentLevel((Enchantment) LCEnchantments.ENDER.get()) > 0) {
                    EnderPickupEvent ender = new EnderPickupEvent(player, e.getItem().copy());
                    MinecraftForge.EVENT_BUS.post(ender);
                    ItemStack stack = ender.getStack();
                    if (stack.isEmpty() || player.m_150109_().add(stack)) {
                        event.setCanceled(true);
                        return;
                    }
                    e.setItem(stack);
                    e.m_6021_(player.m_20185_(), player.m_20186_(), player.m_20189_());
                }
                if (player.m_213854_()) {
                    e.getPersistentData().putBoolean("dampensVibrations", true);
                }
            }
        }
    }

    public static void pushPlayer(ServerPlayer player, BlockPos pos) {
        ((Stack) PLAYER.get()).push(Pair.of(player, player.m_9236_().getBlockState(pos)));
    }

    public static void popPlayer(ServerPlayer player) {
        if (((Pair) ((Stack) PLAYER.get()).peek()).getFirst() == player) {
            ((Stack) PLAYER.get()).pop();
        }
    }

    public static boolean canWalkOn(FluidState state, LivingEntity self) {
        if (state.getType() == Fluids.LAVA) {
            double dy = self.m_20186_();
            double vy = self.m_20184_().y;
            return vy > 0.0 && dy - Math.floor(dy) < 0.5 ? false : EntityFeature.LAVA_WALKER.test(self);
        } else {
            return false;
        }
    }

    public static boolean canSee(Entity instance, Operation<Boolean> original) {
        boolean ans = (Boolean) original.call(new Object[] { instance });
        if (ans) {
            return true;
        } else {
            if (instance instanceof LivingEntity le) {
                if (le.m_20077_() && (EntityFeature.FIRE_REJECT.test(le) || EntityFeature.ENVIRONMENTAL_REJECT.test(le) || EntityFeature.LAVA_WALKER.test(le))) {
                    return true;
                }
                if (le.f_146808_ && (PowderSnowBlock.canEntityWalkOnPowderSnow(instance) || EntityFeature.ENVIRONMENTAL_REJECT.test(le))) {
                    return true;
                }
            }
            return false;
        }
    }
}