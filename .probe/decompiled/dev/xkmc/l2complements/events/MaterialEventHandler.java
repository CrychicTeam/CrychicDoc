package dev.xkmc.l2complements.events;

import dev.xkmc.l2complements.content.item.misc.WarpStone;
import dev.xkmc.l2complements.content.recipe.BurntRecipe;
import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2complements.init.registrate.LCRecipes;
import java.util.Optional;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "l2complements", bus = Bus.FORGE)
public class MaterialEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof EnderMan ender && !ender.m_9236_().isClientSide() && event.getSource().getEntity() instanceof Player player && ender.isCreepy() && ender.m_20097_().m_123342_() <= ender.m_9236_().m_141937_() - LCConfig.COMMON.belowVoid.get()) {
            ender.m_19983_(LCItems.VOID_EYE.asStack());
        }
        if (event.getEntity() instanceof Phantom phantom) {
            Level level = phantom.m_9236_();
            if (!level.isClientSide()) {
                if (phantom.m_20097_().m_123342_() >= level.m_151558_() + LCConfig.COMMON.phantomHeight.get() && level.isDay() && level.m_45527_(phantom.m_20097_()) && phantom.m_6060_()) {
                    phantom.m_19983_(LCItems.SUN_MEMBRANE.asStack());
                }
                if (event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
                    phantom.m_19983_(LCItems.STORM_CORE.asStack());
                }
            }
        }
        if (event.getEntity() instanceof Drowned drowned) {
            Level level = drowned.m_9236_();
            if (!level.isClientSide() && event.getSource().is(DamageTypeTags.IS_FREEZING)) {
                drowned.m_19983_(LCItems.HARD_ICE.asStack());
            }
        }
        if (event.getEntity() instanceof PiglinBrute brute && !brute.m_9236_().isClientSide() && brute.m_21023_((MobEffect) LCEffects.STONE_CAGE.get())) {
            brute.m_19983_(LCItems.BLACKSTONE_CORE.asStack());
        }
        if (event.getEntity() instanceof ElderGuardian guardian && !guardian.m_9236_().isClientSide() && event.getSource().is(DamageTypeTags.IS_LIGHTNING)) {
            guardian.m_19983_(LCItems.GUARDIAN_EYE.asStack());
        }
        if (event.getEntity() instanceof WitherBoss wither && !wither.m_9236_().isClientSide() && event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
            wither.m_19983_(LCItems.FORCE_FIELD.asStack());
        }
        if (event.getEntity() instanceof Warden warden && !warden.m_9236_().isClientSide() && event.getSource().getEntity() instanceof Player) {
            warden.m_19983_(LCItems.WARDEN_BONE_SHARD.asStack());
        }
        if (event.getEntity() instanceof Ghast ghast) {
            Level level = ghast.m_9236_();
            if (!level.isClientSide() && ghast.m_21023_((MobEffect) LCEffects.FLAME.get())) {
                ghast.m_19983_(LCItems.SOUL_FLAME.asStack());
            }
        }
    }

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getItemStack().is((Item) LCItems.WIND_BOTTLE.get()) && event.getTarget() instanceof ShulkerBullet bullet) {
                bullet.hurt(event.getLevel().damageSources().playerAttack(event.getEntity()), 1.0F);
                event.getItemStack().shrink(1);
                event.getEntity().getInventory().placeItemBackInInventory(LCItems.CAPTURED_BULLET.asStack());
            }
        }
    }

    @SubscribeEvent
    public static void onGrind(GrindstoneEvent.OnPlaceItem event) {
        if (event.getTopItem().getItem() instanceof WarpStone) {
            ItemStack copy = event.getTopItem().copy();
            if (WarpStone.getPos(copy).isPresent()) {
                copy.getOrCreateTag().remove("pos");
                event.setOutput(copy);
                event.setXp(0);
            }
        }
    }

    public static void onItemKill(Level level, Entity entity, ItemStack stack) {
        BurntRecipe.Inv inv = new BurntRecipe.Inv();
        inv.m_6836_(0, stack);
        Optional<BurntRecipe> opt = level.getRecipeManager().getRecipeFor((RecipeType<BurntRecipe>) LCRecipes.RT_BURNT.get(), inv, level);
        if (opt.isPresent()) {
            BurntRecipe r = (BurntRecipe) opt.get();
            ItemStack result = r.assemble(inv, level.registryAccess());
            int chance = r.chance;
            int trial = stack.getCount();
            int det = trial / chance;
            trial %= chance;
            if (level.random.nextInt(chance) < trial) {
                det++;
            }
            det *= result.getCount();
            while (det > 0) {
                int sup = Math.min(det, result.getMaxStackSize());
                det -= sup;
                ItemStack copy = result.copy();
                copy.setCount(sup);
                level.m_7967_(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), copy, 0.0, 0.5, 0.0));
            }
        }
    }
}