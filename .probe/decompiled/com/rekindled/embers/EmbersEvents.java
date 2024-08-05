package com.rekindled.embers;

import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.event.EmberProjectileEvent;
import com.rekindled.embers.api.item.IInflictorGem;
import com.rekindled.embers.api.item.IInflictorGemHolder;
import com.rekindled.embers.api.item.ITyrfingWeapon;
import com.rekindled.embers.augment.ShiftingScalesAugment;
import com.rekindled.embers.blockentity.ExplosionPedestalBlockEntity;
import com.rekindled.embers.datagen.EmbersDamageTypes;
import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.network.PacketHandler;
import com.rekindled.embers.network.message.MessageEmberGenOffset;
import com.rekindled.embers.network.message.MessageWorldSeed;
import com.rekindled.embers.research.ResearchManager;
import com.rekindled.embers.util.EmberGenUtil;
import com.rekindled.embers.util.EmberWorldData;
import com.rekindled.embers.util.ExplosionCharmWorldInfo;
import com.rekindled.embers.util.Misc;
import java.util.HashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public class EmbersEvents {

    public static HashMap<ResourceLocation, ExplosionCharmWorldInfo> explosionCharmData = new HashMap();

    public static void fixMappings(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Block> mapping : event.getMappings(ForgeRegistries.Keys.BLOCKS, "embersrekindled")) {
            mapping.remap(BuiltInRegistries.BLOCK.get(new ResourceLocation("embers", mapping.getKey().getPath())));
        }
        for (MissingMappingsEvent.Mapping<Item> mapping : event.getMappings(ForgeRegistries.Keys.ITEMS, "embersrekindled")) {
            mapping.remap(BuiltInRegistries.ITEM.get(new ResourceLocation("embers", mapping.getKey().getPath())));
        }
        for (MissingMappingsEvent.Mapping<Fluid> mapping : event.getMappings(ForgeRegistries.Keys.FLUIDS, "embersrekindled")) {
            mapping.remap(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("embers", mapping.getKey().getPath())));
        }
        for (MissingMappingsEvent.Mapping<EntityType<?>> mapping : event.getMappings(ForgeRegistries.Keys.ENTITY_TYPES, "embersrekindled")) {
            mapping.remap(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation("embers", mapping.getKey().getPath())));
        }
    }

    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer && !event.getLevel().isClientSide()) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            ResearchManager.sendResearchData(player);
            ShiftingScalesAugment.sendScalesData(player);
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MessageWorldSeed(((ServerLevel) event.getLevel()).getSeed()));
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MessageEmberGenOffset(EmberGenUtil.offX, EmberGenUtil.offZ));
        }
    }

    public static void onEntityDamaged(LivingHurtEvent event) {
        if (event.getSource().type().equals(((Registry) event.getEntity().m_9236_().registryAccess().registry(Registries.DAMAGE_TYPE).get()).getHolderOrThrow(EmbersDamageTypes.EMBER_KEY).get()) && (event.getEntity().m_5825_() || event.getEntity().hasEffect(MobEffects.FIRE_RESISTANCE))) {
            event.setAmount(event.getAmount() * 0.5F);
        }
        attuneInflictorGem(event.getEntity(), event.getSource(), event.getEntity().getMainHandItem());
        attuneInflictorGem(event.getEntity(), event.getSource(), event.getEntity().getOffhandItem());
        float mult = 1.0F;
        for (ItemStack armor : event.getEntity().getArmorSlots()) {
            mult -= getInflictorGemResistance(event, armor);
            addHeat(event.getEntity(), armor, 5.0F);
        }
        if (mult <= 0.0F) {
            event.setCanceled(true);
        }
        event.setAmount(event.getAmount() * mult);
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity livingSource) {
            ItemStack heldStack = livingSource.getMainHandItem();
            if (heldStack.getItem() instanceof ITyrfingWeapon tyrfing) {
                tyrfing.attack(event, event.getEntity().getAttribute(Attributes.ARMOR).getValue());
            }
            addHeat(source, heldStack, 1.0F);
        }
    }

    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player != null && event.getState().m_60800_(event.getLevel(), event.getPos()) > 0.0F) {
            addHeat(player, player.m_21205_(), 1.0F);
        }
    }

    public static void onProjectileFired(EmberProjectileEvent event) {
        addHeat(event.getShooter(), event.getStack(), (float) event.getProjectiles().size() * (float) Mth.clampedLerp(0.5, 3.0, event.getCharge()));
    }

    public static void onArrowLoose(ArrowLooseEvent event) {
        addHeat(event.getEntity(), event.getBow(), 1.0F);
    }

    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.getLeft().isRepairable() && !event.getLeft().is(EmbersItemTags.MATERIA_BLACKLIST) && event.getRight().getItem() == RegistryManager.ISOLATED_MATERIA.get()) {
            int i = 0;
            int j = 0;
            int k = 0;
            int l2 = Math.min(event.getLeft().getDamageValue(), event.getLeft().getMaxDamage() / 4);
            if (l2 <= 0) {
                event.setOutput(ItemStack.EMPTY);
                event.setCost(0);
                return;
            }
            ItemStack copy = event.getLeft().copy();
            int i3;
            for (i3 = 0; l2 > 0 && i3 < event.getRight().getCount(); i3++) {
                int j3 = copy.getDamageValue() - l2;
                copy.setDamageValue(j3);
                i++;
                l2 = Math.min(event.getLeft().getDamageValue(), event.getLeft().getMaxDamage() / 4);
            }
            event.setMaterialCost(i3);
            if (event.getName() != null && !Util.isBlank(event.getName())) {
                if (!event.getName().equals(event.getLeft().getHoverName().getString())) {
                    k = 1;
                    i += k;
                    copy.setHoverName(Component.literal(event.getName()));
                }
            } else if (event.getLeft().hasCustomHoverName()) {
                k = 1;
                i += k;
                copy.resetHoverName();
            }
            event.setCost(j + i);
            if (i <= 0) {
                copy = ItemStack.EMPTY;
            }
            if (k == i && k > 0 && event.getCost() >= 40) {
                event.setCost(39);
            }
            if (event.getCost() >= 40 && !event.getPlayer().getAbilities().instabuild) {
                copy = ItemStack.EMPTY;
            }
            if (!copy.isEmpty()) {
                int k2 = copy.getBaseRepairCost();
                if (k != i || k == 0) {
                    k2 = k2 * 2 + 1;
                }
                copy.setRepairCost(k2);
            }
            event.setOutput(copy);
        }
    }

    public static void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel) {
            EmberWorldData.get(ServerLifecycleHooks.getCurrentServer().overworld());
        }
    }

    public static void onServerTick(TickEvent.LevelTickEvent event) {
        if (event.side.isServer() && event.level.dimension() == Level.OVERWORLD) {
            boolean changed = false;
            if (Misc.random.nextInt(400) == 0) {
                EmberGenUtil.offX++;
                changed = true;
            }
            if (Misc.random.nextInt(400) == 0) {
                EmberGenUtil.offZ++;
                changed = true;
            }
            if (changed) {
                PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new MessageEmberGenOffset(EmberGenUtil.offX, EmberGenUtil.offZ));
                EmberWorldData.get(ServerLifecycleHooks.getCurrentServer().overworld()).m_77762_();
            }
        }
    }

    public static void putExplosionCharm(Level world, BlockPos pos) {
        if (!explosionCharmData.containsKey(world.dimension().location())) {
            explosionCharmData.put(world.dimension().location(), new ExplosionCharmWorldInfo());
        }
        ExplosionCharmWorldInfo data = (ExplosionCharmWorldInfo) explosionCharmData.get(world.dimension().location());
        data.put(pos);
    }

    public static void onExplosion(ExplosionEvent.Start event) {
        Level world = event.getLevel();
        Explosion explosion = event.getExplosion();
        ExplosionCharmWorldInfo data = (ExplosionCharmWorldInfo) explosionCharmData.get(world.dimension().location());
        if (data != null) {
            BlockPos charmPos = data.getClosestExplosionCharm(world, BlockPos.containing(explosion.getPosition()), 8);
            if (charmPos != null) {
                BlockEntity tile = world.getBlockEntity(charmPos);
                if (tile instanceof ExplosionPedestalBlockEntity) {
                    ((ExplosionPedestalBlockEntity) tile).absorb(explosion);
                    event.setCanceled(true);
                }
            }
        }
    }

    public static void addHeat(Entity entity, ItemStack stack, float added) {
        if (AugmentUtil.hasHeat(stack)) {
            double maxHeat = (double) AugmentUtil.getMaxHeat(stack);
            double heat = (double) AugmentUtil.getHeat(stack);
            if (heat < maxHeat) {
                AugmentUtil.addHeat(stack, added);
                if (heat + (double) added >= maxHeat) {
                    entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), EmbersSounds.HEATED_ITEM_LEVELUP.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    public static void attuneInflictorGem(LivingEntity entityLiving, DamageSource source, ItemStack stack) {
        if (stack.getItem() instanceof IInflictorGem) {
            ((IInflictorGem) stack.getItem()).attuneSource(stack, entityLiving, source);
        }
    }

    public static float getInflictorGemResistance(LivingHurtEvent event, ItemStack stack) {
        return stack.getItem() instanceof IInflictorGemHolder inflictorGemHolder ? inflictorGemHolder.getTotalDamageResistance(event.getEntity(), event.getSource(), stack) : 0.0F;
    }
}