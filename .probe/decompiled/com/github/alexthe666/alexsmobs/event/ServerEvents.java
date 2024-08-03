package com.github.alexthe666.alexsmobs.event;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.effect.EffectClinging;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityBunfungus;
import com.github.alexthe666.alexsmobs.entity.EntityElephant;
import com.github.alexthe666.alexsmobs.entity.EntityEmu;
import com.github.alexthe666.alexsmobs.entity.EntityEndergrade;
import com.github.alexthe666.alexsmobs.entity.EntityFly;
import com.github.alexthe666.alexsmobs.entity.EntityFlyingFish;
import com.github.alexthe666.alexsmobs.entity.EntityGiantSquid;
import com.github.alexthe666.alexsmobs.entity.EntityJerboa;
import com.github.alexthe666.alexsmobs.entity.EntityMimicOctopus;
import com.github.alexthe666.alexsmobs.entity.EntityMoose;
import com.github.alexthe666.alexsmobs.entity.EntitySeaBear;
import com.github.alexthe666.alexsmobs.entity.EntitySeal;
import com.github.alexthe666.alexsmobs.entity.EntitySnowLeopard;
import com.github.alexthe666.alexsmobs.entity.EntityTiger;
import com.github.alexthe666.alexsmobs.entity.util.FlyingFishBootsUtil;
import com.github.alexthe666.alexsmobs.entity.util.RainbowUtil;
import com.github.alexthe666.alexsmobs.entity.util.RockyChestplateUtil;
import com.github.alexthe666.alexsmobs.entity.util.VineLassoUtil;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.item.ILeftClick;
import com.github.alexthe666.alexsmobs.item.ItemGhostlyPickaxe;
import com.github.alexthe666.alexsmobs.message.MessageSwingArm;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.alexsmobs.misc.EmeraldsForItemsTrade;
import com.github.alexthe666.alexsmobs.misc.ItemsForEmeraldsTrade;
import com.github.alexthe666.alexsmobs.world.AMWorldData;
import com.github.alexthe666.alexsmobs.world.BeachedCachalotWhaleSpawner;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.items.ItemHandlerHelper;
import org.antlr.v4.runtime.misc.Triple;

@EventBusSubscriber(modid = "alexsmobs", bus = Bus.FORGE)
public class ServerEvents {

    public static final UUID ALEX_UUID = UUID.fromString("71363abe-fd03-49c9-940d-aae8b8209b7c");

    public static final UUID CARRO_UUID = UUID.fromString("98905d4a-1cbc-41a4-9ded-2300404e2290");

    private static final UUID SAND_SPEED_MODIFIER = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF28E");

    private static final UUID SNEAK_SPEED_MODIFIER = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF28F");

    private static final AttributeModifier SAND_SPEED_BONUS = new AttributeModifier(SAND_SPEED_MODIFIER, "roadrunner speed bonus", 0.1F, AttributeModifier.Operation.ADDITION);

    private static final AttributeModifier SNEAK_SPEED_BONUS = new AttributeModifier(SNEAK_SPEED_MODIFIER, "frontier cap speed bonus", 0.1F, AttributeModifier.Operation.ADDITION);

    private static final Map<ServerLevel, BeachedCachalotWhaleSpawner> BEACHED_CACHALOT_WHALE_SPAWNER_MAP = new HashMap();

    public static final ObjectList<Triple<ServerPlayer, ServerLevel, BlockPos>> teleportPlayers = new ObjectArrayList();

    private static final Random RAND = new Random();

    @SubscribeEvent
    public static void onServerTick(TickEvent.LevelTickEvent tick) {
        if (!tick.level.isClientSide && tick.level instanceof ServerLevel serverWorld) {
            BEACHED_CACHALOT_WHALE_SPAWNER_MAP.computeIfAbsent(serverWorld, k -> new BeachedCachalotWhaleSpawner(serverWorld));
            BeachedCachalotWhaleSpawner spawner = (BeachedCachalotWhaleSpawner) BEACHED_CACHALOT_WHALE_SPAWNER_MAP.get(serverWorld);
            spawner.tick();
            if (!teleportPlayers.isEmpty()) {
                ObjectListIterator var3 = teleportPlayers.iterator();
                while (var3.hasNext()) {
                    Triple<ServerPlayer, ServerLevel, BlockPos> triple = (Triple<ServerPlayer, ServerLevel, BlockPos>) var3.next();
                    ServerPlayer player = (ServerPlayer) triple.a;
                    ServerLevel endpointWorld = (ServerLevel) triple.b;
                    BlockPos endpoint = (BlockPos) triple.c;
                    int heightFromMap = endpointWorld.m_6924_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, endpoint.m_123341_(), endpoint.m_123343_());
                    endpoint = new BlockPos(endpoint.m_123341_(), Math.max(heightFromMap, endpoint.m_123342_()), endpoint.m_123343_());
                    player.teleportTo(endpointWorld, (double) endpoint.m_123341_() + 0.5, (double) endpoint.m_123342_() + 0.5, (double) endpoint.m_123343_() + 0.5, player.m_146908_(), player.m_146909_());
                    ChunkPos chunkpos = new ChunkPos(endpoint);
                    endpointWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 1, player.m_19879_());
                    player.connection.send(new ClientboundSetExperiencePacket(player.f_36080_, player.f_36079_, player.f_36078_));
                }
                teleportPlayers.clear();
            }
        }
        AMWorldData data = AMWorldData.get(tick.level);
        if (data != null) {
            data.tickPupfish();
        }
    }

    protected static BlockHitResult rayTrace(Level worldIn, Player player, ClipContext.Fluid fluidMode) {
        float x = player.m_146909_();
        float y = player.m_146908_();
        Vec3 vector3d = player.m_20299_(1.0F);
        float f0 = -y * (float) (Math.PI / 180.0) - (float) Math.PI;
        float f1 = -x * (float) (Math.PI / 180.0);
        float f2 = Mth.cos(f0);
        float f3 = Mth.sin(f0);
        float f4 = -Mth.cos(f1);
        float f5 = Mth.sin(f1);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.m_21051_(ForgeMod.BLOCK_REACH.get()).getValue();
        Vec3 vector3d1 = vector3d.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
        return worldIn.m_45547_(new ClipContext(vector3d, vector3d1, ClipContext.Block.OUTLINE, fluidMode, player));
    }

    @SubscribeEvent
    public static void onItemUseLast(LivingEntityUseItemEvent.Finish event) {
        if (event.getItem().getItem() == Items.CHORUS_FRUIT && RAND.nextInt(3) == 0 && event.getEntity().hasEffect(AMEffectRegistry.ENDER_FLU.get())) {
            event.getEntity().removeEffect(AMEffectRegistry.ENDER_FLU.get());
        }
    }

    @SubscribeEvent
    public static void onEntityResize(EntityEvent.Size event) {
        if (event.getEntity() instanceof Player entity) {
            Map<MobEffect, MobEffectInstance> potions = entity.m_21221_();
            if (event.getEntity().level() != null && potions != null && !potions.isEmpty() && potions.containsKey(AMEffectRegistry.CLINGING) && EffectClinging.isUpsideDown(entity)) {
                float minus = event.getOldSize().height - event.getOldEyeHeight();
                event.setNewEyeHeight(minus);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (AMConfig.giveBookOnStartup) {
            CompoundTag playerData = event.getEntity().getPersistentData();
            CompoundTag data = playerData.getCompound("PlayerPersisted");
            if (data != null && !data.getBoolean("alexsmobs_has_book")) {
                ItemHandlerHelper.giveItemToPlayer(event.getEntity(), new ItemStack(AMItemRegistry.ANIMAL_DICTIONARY.get()));
                boolean isAlex = Objects.equals(event.getEntity().m_20148_(), ALEX_UUID);
                if (isAlex || Objects.equals(event.getEntity().m_20148_(), CARRO_UUID)) {
                    ItemHandlerHelper.giveItemToPlayer(event.getEntity(), new ItemStack(AMItemRegistry.BEAR_DUST.get()));
                }
                if (isAlex) {
                    ItemHandlerHelper.giveItemToPlayer(event.getEntity(), new ItemStack(AMItemRegistry.NOVELTY_HAT.get()));
                }
                data.putBoolean("alexsmobs_has_book", true);
                playerData.put("PlayerPersisted", data);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        boolean flag = false;
        ItemStack leftItem = event.getEntity().m_21206_();
        ItemStack rightItem = event.getEntity().m_21205_();
        if (leftItem.getItem() instanceof ILeftClick iLeftClick) {
            iLeftClick.onLeftClick(leftItem, event.getEntity());
            flag = true;
        }
        if (rightItem.getItem() instanceof ILeftClick iLeftClick) {
            iLeftClick.onLeftClick(rightItem, event.getEntity());
            flag = true;
        }
        if (flag && event.getLevel().isClientSide) {
            AlexsMobs.sendMSGToServer(MessageSwingArm.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void onStruckByLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity().getType() == EntityType.SQUID && !event.getEntity().level().isClientSide) {
            ServerLevel level = (ServerLevel) event.getEntity().level();
            event.setCanceled(true);
            EntityGiantSquid squid = AMEntityRegistry.GIANT_SQUID.get().create(level);
            squid.m_7678_(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity().getYRot(), event.getEntity().getXRot());
            squid.finalizeSpawn(level, level.m_6436_(squid.m_20183_()), MobSpawnType.CONVERSION, null, null);
            if (event.getEntity().hasCustomName()) {
                squid.m_6593_(event.getEntity().getCustomName());
                squid.m_20340_(event.getEntity().isCustomNameVisible());
            }
            squid.setBlue(true);
            squid.m_21530_();
            level.m_47205_(squid);
            event.getEntity().discard();
        }
    }

    @SubscribeEvent
    public void onProjectileHit(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult hitResult && hitResult.getEntity() instanceof EntityEmu emu && !event.getEntity().level().isClientSide) {
            if (event.getEntity() instanceof AbstractArrow arrow) {
                arrow.setPierceLevel((byte) 0);
            }
            if ((emu.getAnimation() == EntityEmu.ANIMATION_DODGE_RIGHT || emu.getAnimation() == EntityEmu.ANIMATION_DODGE_LEFT) && emu.getAnimationTick() < 7) {
                event.setCanceled(true);
            }
            if (emu.getAnimation() != EntityEmu.ANIMATION_DODGE_RIGHT && emu.getAnimation() != EntityEmu.ANIMATION_DODGE_LEFT) {
                boolean left = true;
                Vec3 arrowPos = event.getEntity().position();
                Vec3 rightVector = emu.m_20154_().yRot((float) (Math.PI / 2)).add(emu.m_20182_());
                Vec3 leftVector = emu.m_20154_().yRot((float) (-Math.PI / 2)).add(emu.m_20182_());
                if (arrowPos.distanceTo(rightVector) < arrowPos.distanceTo(leftVector)) {
                    left = false;
                } else if (arrowPos.distanceTo(rightVector) > arrowPos.distanceTo(leftVector)) {
                    left = true;
                } else {
                    left = emu.m_217043_().nextBoolean();
                }
                Vec3 vector3d2 = event.getEntity().getDeltaMovement().yRot((float) ((double) (left ? -0.5F : 0.5F) * Math.PI)).normalize();
                emu.setAnimation(left ? EntityEmu.ANIMATION_DODGE_LEFT : EntityEmu.ANIMATION_DODGE_RIGHT);
                emu.f_19812_ = true;
                if (!emu.f_19862_) {
                    emu.m_6478_(MoverType.SELF, new Vec3(vector3d2.x() * 0.25, 0.1F, vector3d2.z() * 0.25));
                }
                if (!event.getEntity().level().isClientSide && event.getEntity() instanceof Projectile projectile && projectile.getOwner() instanceof ServerPlayer serverPlayer) {
                    AMAdvancementTriggerRegistry.EMU_DODGE.trigger(serverPlayer);
                }
                emu.m_20256_(emu.m_20184_().add(vector3d2.x() * 0.5, 0.32F, vector3d2.z() * 0.5));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onEntityDespawnAttempt(MobSpawnEvent.AllowDespawn event) {
        if (event.getEntity().m_21023_(AMEffectRegistry.DEBILITATING_STING.get()) && event.getEntity().m_21124_(AMEffectRegistry.DEBILITATING_STING.get()) != null && event.getEntity().m_21124_(AMEffectRegistry.DEBILITATING_STING.get()).getAmplifier() > 0) {
            event.setResult(Result.DENY);
        }
    }

    @SubscribeEvent
    public void onTradeSetup(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.FISHERMAN) {
            VillagerTrades.ItemListing ambergrisTrade = new EmeraldsForItemsTrade(AMItemRegistry.AMBERGRIS.get(), 20, 3, 4);
            List<VillagerTrades.ItemListing> list = (List<VillagerTrades.ItemListing>) event.getTrades().get(2);
            list.add(ambergrisTrade);
            event.getTrades().put(2, list);
        }
    }

    @SubscribeEvent
    public void onWanderingTradeSetup(WandererTradesEvent event) {
        if (AMConfig.wanderingTraderOffers) {
            List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
            List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.ANIMAL_DICTIONARY.get(), 4, 1, 2, 1));
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.ACACIA_BLOSSOM.get(), 3, 2, 2, 1));
            if (AMConfig.cockroachSpawnWeight > 0) {
                genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.COCKROACH_OOTHECA.get(), 2, 1, 2, 1));
            }
            if (AMConfig.blobfishSpawnWeight > 0) {
                genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.BLOBFISH_BUCKET.get(), 4, 1, 3, 1));
            }
            if (AMConfig.crocodileSpawnWeight > 0) {
                genericTrades.add(new ItemsForEmeraldsTrade(AMBlockRegistry.CROCODILE_EGG.get().asItem(), 6, 1, 2, 1));
            }
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.BEAR_FUR.get(), 1, 1, 2, 1));
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.CROCODILE_SCUTE.get(), 5, 1, 2, 1));
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.ROADRUNNER_FEATHER.get(), 1, 2, 2, 2));
            genericTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.MOSQUITO_LARVA.get(), 1, 3, 5, 1));
            rareTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.SOMBRERO.get(), 20, 1, 1, 1));
            rareTrades.add(new ItemsForEmeraldsTrade(AMBlockRegistry.BANANA_PEEL.get(), 1, 2, 1, 1));
            rareTrades.add(new ItemsForEmeraldsTrade(AMItemRegistry.BLOOD_SAC.get(), 5, 2, 3, 1));
        }
    }

    @SubscribeEvent
    public void onLootLevelEvent(LootingLevelEvent event) {
        DamageSource src = event.getDamageSource();
        if (src != null && src.getEntity() instanceof EntitySnowLeopard) {
            event.setLootingLevel(event.getLootingLevel() + 2);
        }
    }

    @SubscribeEvent
    public void onUseItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (event.getItemStack().getItem() == Items.WHEAT && player.m_20202_() instanceof EntityElephant elephant && elephant.triggerCharge(event.getItemStack())) {
            player.m_6674_(event.getHand());
            if (!player.isCreative()) {
                event.getItemStack().shrink(1);
            }
        }
        if (event.getItemStack().getItem() == Items.GLASS_BOTTLE && AMConfig.lavaBottleEnabled) {
            HitResult raytraceresult = rayTrace(event.getLevel(), player, ClipContext.Fluid.SOURCE_ONLY);
            if (raytraceresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = ((BlockHitResult) raytraceresult).getBlockPos();
                if (event.getLevel().mayInteract(player, blockpos) && event.getLevel().getFluidState(blockpos).is(FluidTags.LAVA)) {
                    player.m_146850_(GameEvent.ITEM_INTERACT_START);
                    event.getLevel().playSound(player, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    player.awardStat(Stats.ITEM_USED.get(Items.GLASS_BOTTLE));
                    player.m_20254_(6);
                    if (!player.addItem(new ItemStack(AMItemRegistry.LAVA_BOTTLE.get()))) {
                        player.m_19983_(new ItemStack(AMItemRegistry.LAVA_BOTTLE.get()));
                    }
                    player.m_6674_(event.getHand());
                    if (!player.isCreative()) {
                        event.getItemStack().shrink(1);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onInteractWithEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof LivingEntity living) {
            if (!event.getEntity().m_6144_() && VineLassoUtil.hasLassoData(living)) {
                if (!event.getEntity().m_9236_().isClientSide) {
                    event.getTarget().spawnAtLocation(new ItemStack(AMItemRegistry.VINE_LASSO.get()));
                }
                VineLassoUtil.lassoTo(null, living);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
            if (!(event.getTarget() instanceof Player) && !(event.getTarget() instanceof EntityEndergrade) && living.hasEffect(AMEffectRegistry.ENDER_FLU.get()) && event.getItemStack().getItem() == Items.CHORUS_FRUIT) {
                if (!event.getEntity().isCreative()) {
                    event.getItemStack().shrink(1);
                }
                event.getTarget().gameEvent(GameEvent.EAT);
                event.getTarget().playSound(SoundEvents.GENERIC_EAT, 1.0F, 0.5F + event.getEntity().m_217043_().nextFloat());
                if (event.getEntity().m_217043_().nextFloat() < 0.4F) {
                    living.removeEffect(AMEffectRegistry.ENDER_FLU.get());
                    Items.CHORUS_FRUIT.finishUsingItem(event.getItemStack().copy(), event.getLevel(), (LivingEntity) event.getTarget());
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
            if (RainbowUtil.getRainbowType(living) > 0 && event.getItemStack().getItem() == Items.SPONGE) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
                RainbowUtil.setRainbowType(living, 0);
                if (!event.getEntity().isCreative()) {
                    event.getItemStack().shrink(1);
                }
                ItemStack wetSponge = new ItemStack(Items.WET_SPONGE);
                if (!event.getEntity().addItem(wetSponge)) {
                    event.getEntity().drop(wetSponge, true);
                }
            }
            if (living instanceof Rabbit rabbit && event.getItemStack().getItem() == AMItemRegistry.MUNGAL_SPORES.get() && AMConfig.bunfungusTransformation) {
                ThreadLocalRandom random = ThreadLocalRandom.current();
                if (!event.getEntity().m_9236_().isClientSide && random.nextFloat() < 0.15F) {
                    EntityBunfungus bunfungus = (EntityBunfungus) rabbit.m_21406_(AMEntityRegistry.BUNFUNGUS.get(), true);
                    if (bunfungus != null) {
                        event.getEntity().m_9236_().m_7967_(bunfungus);
                        bunfungus.setTransformsIn(50);
                    }
                } else {
                    for (int i = 0; i < 2 + random.nextInt(2); i++) {
                        double d0 = random.nextGaussian() * 0.02;
                        double d1 = 0.05F + random.nextGaussian() * 0.02;
                        double d2 = random.nextGaussian() * 0.02;
                        event.getTarget().level().addParticle(AMParticleRegistry.BUNFUNGUS_TRANSFORMATION.get(), event.getTarget().getRandomX(0.7F), event.getTarget().getY(0.6F), event.getTarget().getRandomZ(0.7F), d0, d1, d2);
                    }
                }
                if (!event.getEntity().isCreative()) {
                    event.getItemStack().shrink(1);
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public void onUseItemAir(PlayerInteractEvent.RightClickEmpty event) {
        ItemStack stack = event.getEntity().m_21120_(event.getHand());
        if (stack.isEmpty()) {
            stack = event.getEntity().getItemBySlot(EquipmentSlot.MAINHAND);
        }
        if (RainbowUtil.getRainbowType(event.getEntity()) > 0 && stack.is(Items.SPONGE)) {
            event.getEntity().m_6674_(InteractionHand.MAIN_HAND);
            RainbowUtil.setRainbowType(event.getEntity(), 0);
            if (!event.getEntity().isCreative()) {
                stack.shrink(1);
            }
            ItemStack wetSponge = new ItemStack(Items.WET_SPONGE);
            if (!event.getEntity().addItem(wetSponge)) {
                event.getEntity().drop(wetSponge, true);
            }
        }
    }

    @SubscribeEvent
    public void onUseItemOnBlock(PlayerInteractEvent.RightClickBlock event) {
        if (AlexsMobs.isAprilFools() && event.getItemStack().is(Items.STICK) && !event.getEntity().getCooldowns().isOnCooldown(Items.STICK)) {
            BlockState state = event.getEntity().m_9236_().getBlockState(event.getPos());
            boolean flag = false;
            if (state.m_60713_(Blocks.SAND)) {
                flag = true;
                event.getEntity().m_9236_().setBlockAndUpdate(event.getPos(), AMBlockRegistry.SAND_CIRCLE.get().defaultBlockState());
            } else if (state.m_60713_(Blocks.RED_SAND)) {
                flag = true;
                event.getEntity().m_9236_().setBlockAndUpdate(event.getPos(), AMBlockRegistry.RED_SAND_CIRCLE.get().defaultBlockState());
            }
            if (flag) {
                event.setCanceled(true);
                event.getEntity().m_146850_(GameEvent.BLOCK_PLACE);
                event.getEntity().playSound(SoundEvents.SAND_BREAK, 1.0F, 1.0F);
                event.getEntity().getCooldowns().addCooldown(Items.STICK, 30);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public void onEntityDrops(LivingDropsEvent event) {
        if (VineLassoUtil.hasLassoData(event.getEntity())) {
            VineLassoUtil.lassoTo(null, event.getEntity());
            event.getDrops().add(new ItemEntity(event.getEntity().m_9236_(), event.getEntity().m_20185_(), event.getEntity().m_20186_(), event.getEntity().m_20189_(), new ItemStack(AMItemRegistry.VINE_LASSO.get())));
        }
    }

    @SubscribeEvent
    public void onEntityFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob entity = event.getEntity();
        if (entity instanceof WanderingTrader trader && AMConfig.elephantTraderSpawnChance > 0.0) {
            Biome biome = (Biome) event.getLevel().m_204166_(entity.m_20183_()).value();
            if ((double) RAND.nextFloat() <= AMConfig.elephantTraderSpawnChance && (!AMConfig.limitElephantTraderBiomes || biome.getBaseTemperature() >= 1.0F)) {
                ChunkPos chunkPos = new ChunkPos(trader.m_20183_());
                if (event.getLevel().m_7726_().getChunkNow(chunkPos.x, chunkPos.z) != null) {
                    EntityElephant elephant = AMEntityRegistry.ELEPHANT.get().create(trader.m_9236_());
                    elephant.m_20359_(trader);
                    if (elephant.canSpawnWithTraderHere()) {
                        elephant.setTrader(true);
                        elephant.setChested(true);
                        if (!event.getLevel().m_5776_()) {
                            trader.m_9236_().m_7967_(elephant);
                            trader.m_7998_(elephant, true);
                        }
                        elephant.addElephantLoot(null, RAND.nextInt());
                    }
                }
            }
        }
        try {
            if (AMConfig.spidersAttackFlies && entity instanceof Spider spider) {
                spider.f_21346_.addGoal(4, new NearestAttackableTargetGoal(spider, EntityFly.class, 1, true, false, null));
            } else if (AMConfig.wolvesAttackMoose && entity instanceof Wolf wolf) {
                wolf.f_21346_.addGoal(6, new NonTameRandomTargetGoal(wolf, EntityMoose.class, false, null));
            } else if (AMConfig.polarBearsAttackSeals && entity instanceof PolarBear bear) {
                bear.f_21346_.addGoal(6, new NearestAttackableTargetGoal(bear, EntitySeal.class, 15, true, true, null));
            } else if (entity instanceof Creeper creeper) {
                creeper.f_21346_.addGoal(3, new AvoidEntityGoal(creeper, EntitySnowLeopard.class, 6.0F, 1.0, 1.2));
                creeper.f_21346_.addGoal(3, new AvoidEntityGoal(creeper, EntityTiger.class, 6.0F, 1.0, 1.2));
            } else if (!AMConfig.catsAndFoxesAttackJerboas || !(entity instanceof Fox) && !(entity instanceof Cat) && !(entity instanceof Ocelot)) {
                if (AMConfig.bunfungusTransformation && entity instanceof Rabbit rabbit) {
                    rabbit.f_21345_.addGoal(3, new TemptGoal(rabbit, 1.0, Ingredient.of(AMItemRegistry.MUNGAL_SPORES.get()), false));
                } else if (AMConfig.dolphinsAttackFlyingFish && entity instanceof Dolphin dolphin) {
                    dolphin.f_21346_.addGoal(2, new NearestAttackableTargetGoal(dolphin, EntityFlyingFish.class, 70, true, true, null));
                }
            } else {
                entity.targetSelector.addGoal(6, new NearestAttackableTargetGoal(entity, EntityJerboa.class, 45, true, true, null));
            }
        } catch (Exception var10) {
            AlexsMobs.LOGGER.warn("Tried to add unique behaviors to vanilla mobs and encountered an error");
        }
    }

    @SubscribeEvent
    public void onPlayerAttackEntityEvent(AttackEntityEvent event) {
        if (event.getTarget() instanceof LivingEntity living) {
            if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.MOOSE_HEADGEAR.get()) {
                living.knockback(1.0, (double) Mth.sin(event.getEntity().m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(event.getEntity().m_146908_() * (float) (Math.PI / 180.0))));
            }
            if (event.getEntity().m_21023_(AMEffectRegistry.TIGERS_BLESSING.get()) && !event.getTarget().isAlliedTo(event.getEntity()) && !(event.getTarget() instanceof EntityTiger)) {
                AABB bb = new AABB(event.getEntity().m_20185_() - 32.0, event.getEntity().m_20186_() - 32.0, event.getEntity().m_20189_() - 32.0, event.getEntity().m_20189_() + 32.0, event.getEntity().m_20186_() + 32.0, event.getEntity().m_20189_() + 32.0);
                for (EntityTiger tiger : event.getEntity().m_9236_().m_6443_(EntityTiger.class, bb, EntitySelector.ENTITY_STILL_ALIVE)) {
                    if (!tiger.m_6162_()) {
                        tiger.m_6710_(living);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingDamageEvent(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (event.getAmount() > 0.0F && attacker.hasEffect(AMEffectRegistry.SOULSTEAL.get()) && attacker.getEffect(AMEffectRegistry.SOULSTEAL.get()) != null) {
                int level = attacker.getEffect(AMEffectRegistry.SOULSTEAL.get()).getAmplifier() + 1;
                if (attacker.getHealth() < attacker.getMaxHealth() && ThreadLocalRandom.current().nextFloat() < 0.25F + (float) level * 0.25F) {
                    attacker.heal(Math.min(event.getAmount() / 2.0F * (float) level, (float) (2 + 2 * level)));
                }
            }
            if (event.getEntity() instanceof Player player) {
                if (attacker instanceof EntityMimicOctopus octupus && octupus.m_21830_(player)) {
                    event.setCanceled(true);
                    return;
                }
                if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.SPIKED_TURTLE_SHELL.get() && attacker.m_20270_(player) < attacker.m_20205_() + player.m_20205_() + 0.5F) {
                    attacker.hurt(attacker.m_269291_().thorns(player), 1.0F);
                    attacker.knockback(0.5, (double) Mth.sin((attacker.m_146908_() + 180.0F) * (float) (Math.PI / 180.0)), (double) (-Mth.cos((attacker.m_146908_() + 180.0F) * (float) (Math.PI / 180.0))));
                }
            }
        }
        if (!event.getEntity().getItemBySlot(EquipmentSlot.LEGS).isEmpty() && event.getEntity().getItemBySlot(EquipmentSlot.LEGS).getItem() == AMItemRegistry.EMU_LEGGINGS.get() && event.getSource().is(DamageTypeTags.IS_PROJECTILE) && (double) event.getEntity().getRandom().nextFloat() < AMConfig.emuPantsDodgeChance) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingSetTargetEvent(LivingChangeTargetEvent event) {
        if (event.getNewTarget() != null && event.getEntity() instanceof Mob mob) {
            if (mob.m_6336_() == MobType.ARTHROPOD && event.getNewTarget().hasEffect(AMEffectRegistry.BUG_PHEROMONES.get()) && event.getEntity().getLastHurtByMob() != event.getNewTarget()) {
                event.setCanceled(true);
                return;
            }
            if (mob.m_6336_() == MobType.UNDEAD && !mob.m_6095_().is(AMTagRegistry.IGNORES_KIMONO) && event.getNewTarget().getItemBySlot(EquipmentSlot.CHEST).is(AMItemRegistry.UNSETTLING_KIMONO.get()) && event.getEntity().getLastHurtByMob() != event.getNewTarget()) {
                event.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if ((double) player.m_20192_() < (double) player.m_20206_() * 0.5) {
                player.m_6210_();
            }
            if (entity.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED)) {
                AttributeInstance attributes = entity.getAttribute(Attributes.MOVEMENT_SPEED);
                if (player.getItemBySlot(EquipmentSlot.FEET).getItem() == AMItemRegistry.ROADDRUNNER_BOOTS.get() || attributes.hasModifier(SAND_SPEED_BONUS)) {
                    boolean sand = player.m_9236_().getBlockState(this.getDownPos(player.m_20183_(), player.m_9236_())).m_204336_(BlockTags.SAND);
                    if (sand && !attributes.hasModifier(SAND_SPEED_BONUS)) {
                        attributes.addPermanentModifier(SAND_SPEED_BONUS);
                    }
                    if (player.f_19797_ % 25 == 0 && (player.getItemBySlot(EquipmentSlot.FEET).getItem() != AMItemRegistry.ROADDRUNNER_BOOTS.get() || !sand) && attributes.hasModifier(SAND_SPEED_BONUS)) {
                        attributes.removeModifier(SAND_SPEED_BONUS);
                    }
                }
                if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.FRONTIER_CAP.get() || attributes.hasModifier(SNEAK_SPEED_BONUS)) {
                    boolean shift = player.m_6144_();
                    if (shift && !attributes.hasModifier(SNEAK_SPEED_BONUS)) {
                        attributes.addPermanentModifier(SNEAK_SPEED_BONUS);
                    }
                    if ((!shift || player.getItemBySlot(EquipmentSlot.HEAD).getItem() != AMItemRegistry.FRONTIER_CAP.get()) && attributes.hasModifier(SNEAK_SPEED_BONUS)) {
                        attributes.removeModifier(SNEAK_SPEED_BONUS);
                    }
                }
            }
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.SPIKED_TURTLE_SHELL.get() && !player.m_204029_(FluidTags.WATER)) {
                player.m_7292_(new MobEffectInstance(MobEffects.WATER_BREATHING, 310, 0, false, false, true));
            }
        }
        ItemStack boots = entity.getItemBySlot(EquipmentSlot.FEET);
        if (!boots.isEmpty() && boots.hasTag() && boots.getOrCreateTag().contains("BisonFur") && boots.getOrCreateTag().getBoolean("BisonFur")) {
            BlockPos posBelow = new BlockPos((int) event.getEntity().m_20185_(), (int) (entity.m_20191_().minY - 0.1F), (int) entity.m_20189_());
            if (entity.m_9236_().getBlockState(posBelow).m_60713_(Blocks.POWDER_SNOW)) {
                entity.m_6853_(true);
                entity.m_146917_(0);
                entity.m_6034_(entity.m_20185_(), Math.max(entity.m_20186_(), (double) ((float) posBelow.m_123342_() + 1.0F)), entity.m_20189_());
            }
            if (entity.f_146808_) {
                entity.m_6853_(true);
                entity.m_20256_(entity.m_20184_().add(0.0, 0.1F, 0.0));
            }
        }
        if (entity.getItemBySlot(EquipmentSlot.LEGS).getItem() == AMItemRegistry.CENTIPEDE_LEGGINGS.get() && entity.f_19862_ && !entity.m_20069_()) {
            entity.f_19789_ = 0.0F;
            Vec3 motion = entity.m_20184_();
            double d2 = 0.1;
            if (entity.m_6144_() || !entity.m_146900_().isScaffolding(entity) && entity.isSuppressingSlidingDownLadder()) {
                d2 = 0.0;
            }
            motion = new Vec3(Mth.clamp(motion.x, -0.15F, 0.15F), d2, Mth.clamp(motion.z, -0.15F, 0.15F));
            entity.m_20256_(motion);
        }
        if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() == AMItemRegistry.SOMBRERO.get() && !entity.m_9236_().isClientSide && AlexsMobs.isAprilFools() && entity.m_20072_()) {
            RandomSource random = entity.getRandom();
            if (random.nextInt(245) == 0 && !EntitySeaBear.isMobSafe(entity)) {
                int dist = 32;
                List<EntitySeaBear> nearbySeabears = entity.m_9236_().m_45976_(EntitySeaBear.class, entity.m_20191_().inflate(32.0, 32.0, 32.0));
                if (nearbySeabears.isEmpty()) {
                    EntitySeaBear bear = AMEntityRegistry.SEA_BEAR.get().create(entity.m_9236_());
                    BlockPos at = entity.m_20183_();
                    BlockPos farOff = null;
                    for (int i = 0; i < 15; i++) {
                        int f1 = (int) Math.signum((float) random.nextInt() - 0.5F);
                        int f2 = (int) Math.signum((float) random.nextInt() - 0.5F);
                        BlockPos pos1 = at.offset(f1 * (10 + random.nextInt(22)), random.nextInt(1), f2 * (10 + random.nextInt(22)));
                        if (entity.m_9236_().m_46801_(pos1)) {
                            farOff = pos1;
                        }
                    }
                    if (farOff != null) {
                        bear.m_6034_((double) ((float) farOff.m_123341_() + 0.5F), (double) ((float) farOff.m_123342_() + 0.5F), (double) ((float) farOff.m_123343_() + 0.5F));
                        bear.m_146922_(random.nextFloat() * 360.0F);
                        bear.setTarget(entity);
                        entity.m_9236_().m_7967_(bear);
                    }
                } else {
                    for (EntitySeaBear bear : nearbySeabears) {
                        bear.setTarget(entity);
                    }
                }
            }
        }
        if (VineLassoUtil.hasLassoData(entity)) {
            VineLassoUtil.tickLasso(entity);
        }
        if (RockyChestplateUtil.isWearing(entity)) {
            RockyChestplateUtil.tickRockyRolling(entity);
        }
        if (FlyingFishBootsUtil.isWearing(entity)) {
            FlyingFishBootsUtil.tickFlyingFishBoots(entity);
        }
    }

    private BlockPos getDownPos(BlockPos entered, LevelAccessor world) {
        for (int i = 0; world.m_46859_(entered) && i < 3; i++) {
            entered = entered.below();
        }
        return entered;
    }

    @SubscribeEvent
    public void onFOVUpdate(ComputeFovModifierEvent event) {
        if (event.getPlayer().m_21023_(AMEffectRegistry.FEAR.get()) || event.getPlayer().m_21023_(AMEffectRegistry.POWER_DOWN.get())) {
            event.setNewFovModifier(1.0F);
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        if (!event.getEntity().getUseItem().isEmpty() && event.getSource() != null && event.getSource().getEntity() != null && event.getEntity().getUseItem().getItem() == AMItemRegistry.SHIELD_OF_THE_DEEP.get() && event.getSource().getEntity() instanceof LivingEntity living) {
            boolean flag = false;
            if (living.m_20270_(event.getEntity()) <= 4.0F && !living.hasEffect(AMEffectRegistry.EXSANGUINATION.get())) {
                living.addEffect(new MobEffectInstance(AMEffectRegistry.EXSANGUINATION.get(), 60, 2));
                flag = true;
            }
            if (event.getEntity().m_20072_()) {
                event.getEntity().m_20301_(Math.min(event.getEntity().m_6062_(), event.getEntity().m_20146_() + 150));
                flag = true;
            }
            if (flag) {
                event.getEntity().getUseItem().hurtAndBreak(1, event.getEntity(), player -> player.broadcastBreakEvent(event.getEntity().getUsedItemHand()));
            }
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        CompoundTag tag = event.getItemStack().getTag();
        if (tag != null && tag.contains("BisonFur") && tag.getBoolean("BisonFur")) {
            event.getToolTip().add(Component.translatable("item.alexsmobs.insulated_with_fur").withStyle(ChatFormatting.AQUA));
        }
    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        AlexsMobs.LOGGER.info("Adding datapack listener capsid_recipes");
        event.addListener(AlexsMobs.PROXY.getCapsidRecipeManager());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        if (event.getEntity() != null && event.getEntity().m_21055_(AMItemRegistry.GHOSTLY_PICKAXE.get()) && ItemGhostlyPickaxe.shouldStoreInGhost(event.getEntity(), event.getEntity().m_21205_())) {
            event.setCanHarvest(false);
        }
    }
}