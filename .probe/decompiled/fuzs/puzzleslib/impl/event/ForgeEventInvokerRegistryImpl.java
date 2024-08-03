package fuzs.puzzleslib.impl.event;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.core.v1.ModContainerHelper;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.event.v1.LoadCompleteCallback;
import fuzs.puzzleslib.api.event.v1.RegistryEntryAddedCallback;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.event.v1.core.ForgeEventInvokerRegistry;
import fuzs.puzzleslib.api.event.v1.data.DefaultedDouble;
import fuzs.puzzleslib.api.event.v1.data.DefaultedFloat;
import fuzs.puzzleslib.api.event.v1.data.DefaultedInt;
import fuzs.puzzleslib.api.event.v1.data.DefaultedValue;
import fuzs.puzzleslib.api.event.v1.data.MutableDouble;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import fuzs.puzzleslib.api.event.v1.entity.EntityRidingEvents;
import fuzs.puzzleslib.api.event.v1.entity.ProjectileImpactCallback;
import fuzs.puzzleslib.api.event.v1.entity.ServerEntityLevelEvents;
import fuzs.puzzleslib.api.event.v1.entity.living.AnimalTameCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.BabyEntitySpawnCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.CheckMobDespawnCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.ItemAttributeModifiersCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingAttackCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingChangeTargetCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDeathCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingDropsCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingEquipmentChangeCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingEvents;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingExperienceDropCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingFallCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingHurtCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LivingKnockBackCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.LootingLevelCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.MobEffectEvents;
import fuzs.puzzleslib.api.event.v1.entity.living.ShieldBlockCallback;
import fuzs.puzzleslib.api.event.v1.entity.living.UseItemEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.AnvilRepairCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.AnvilUpdateCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.ArrowLooseCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.BonemealCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.GrindstoneEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.ItemTossCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.ItemTouchCallback;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerTickEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerXpEvents;
import fuzs.puzzleslib.api.event.v1.level.BlockEvents;
import fuzs.puzzleslib.api.event.v1.level.ExplosionEvents;
import fuzs.puzzleslib.api.event.v1.level.GatherPotentialSpawnsCallback;
import fuzs.puzzleslib.api.event.v1.level.PlayLevelSoundEvents;
import fuzs.puzzleslib.api.event.v1.level.ServerChunkEvents;
import fuzs.puzzleslib.api.event.v1.level.ServerLevelEvents;
import fuzs.puzzleslib.api.event.v1.level.ServerLevelTickEvents;
import fuzs.puzzleslib.api.event.v1.server.LootTableLoadEvents;
import fuzs.puzzleslib.api.event.v1.server.RegisterCommandsCallback;
import fuzs.puzzleslib.api.event.v1.server.ServerLifecycleEvents;
import fuzs.puzzleslib.api.event.v1.server.ServerTickEvents;
import fuzs.puzzleslib.api.event.v1.server.SyncDataPackContentsCallback;
import fuzs.puzzleslib.api.event.v1.server.TagsUpdatedCallback;
import fuzs.puzzleslib.api.init.v3.RegistryHelper;
import fuzs.puzzleslib.impl.PuzzlesLib;
import fuzs.puzzleslib.impl.client.event.ForgeClientEventInvokers;
import fuzs.puzzleslib.impl.event.core.EventInvokerImpl;
import fuzs.puzzleslib.mixin.accessor.ForgeRegistryForgeAccessor;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.PlayLevelSoundEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.Nullable;

public final class ForgeEventInvokerRegistryImpl implements ForgeEventInvokerRegistry {

    public static void registerLoadingHandlers() {
        INSTANCE.register(LoadCompleteCallback.class, FMLLoadCompleteEvent.class, (callback, evt) -> callback.onLoadComplete());
        INSTANCE.register(RegistryEntryAddedCallback.class, ForgeEventInvokerRegistryImpl::onRegistryEntryAdded);
        if (ModLoaderEnvironment.INSTANCE.isClient()) {
            ForgeClientEventInvokers.registerLoadingHandlers();
        }
    }

    private static <T> void onRegistryEntryAdded(RegistryEntryAddedCallback<T> callback, @Nullable Object context) {
        Objects.requireNonNull(context, "context is null");
        ResourceKey<? extends Registry<T>> resourceKey = (ResourceKey<? extends Registry<T>>) context;
        Registry<T> registry = RegistryHelper.findBuiltInRegistry(resourceKey);
        IForgeRegistry<T> forgeRegistry = RegistryManager.ACTIVE.getRegistry(resourceKey);
        boolean[] loadComplete = new boolean[1];
        synchronized (forgeRegistry) {
            IForgeRegistry.AddCallback<T> originalAddCallback = ((ForgeRegistryForgeAccessor) forgeRegistry).puzzleslib$getAdd();
            IForgeRegistry.AddCallback<T> newAddCallback = (owner, stage, id, key, obj, oldObj) -> {
                if (!loadComplete[0] && oldObj == null) {
                    try {
                        callback.onRegistryEntryAdded(registry, key.location(), obj, (resourceLocation, supplier) -> {
                            try {
                                T t = (T) supplier.get();
                                Objects.requireNonNull(t, "entry is null");
                                owner.register(resourceLocation, t);
                            } catch (Exception var4x) {
                                PuzzlesLib.LOGGER.error("Failed to register new entry", var4x);
                            }
                        });
                    } catch (Exception var10) {
                        PuzzlesLib.LOGGER.error("Failed to run registry entry added callback", var10);
                    }
                }
            };
            ((ForgeRegistryForgeAccessor) forgeRegistry).puzzleslib$setAdd(originalAddCallback != null ? (owner, stage, id, key, obj, oldObj) -> {
                originalAddCallback.onAdd(owner, stage, id, key, obj, oldObj);
                newAddCallback.onAdd(owner, stage, id, key, obj, oldObj);
            } : newAddCallback);
        }
        IEventBus eventBus = ModContainerHelper.getActiveModEventBus();
        eventBus.addListener(evt -> loadComplete[0] = true);
        Set<Consumer<BiConsumer<ResourceLocation, Supplier<T>>>> callbacks = Sets.newLinkedHashSet();
        for (Entry<ResourceKey<T>, T> entry : forgeRegistry.getEntries()) {
            callbacks.add((Consumer) consumer -> {
                try {
                    callback.onRegistryEntryAdded(registry, ((ResourceKey) entry.getKey()).location(), (T) entry.getValue(), consumer);
                } catch (Exception var5x) {
                    PuzzlesLib.LOGGER.error("Failed to run registry entry added callback", var5x);
                }
            });
        }
        eventBus.addListener(evt -> {
            if (evt.getRegistryKey() == resourceKey) {
                callbacks.forEach(consumer -> consumer.accept((BiConsumer) (resourceLocation, supplier) -> evt.register(resourceKey, resourceLocation, supplier)));
                callbacks.clear();
            }
        });
    }

    public static void registerEventHandlers() {
        INSTANCE.register(PlayerInteractEvents.UseBlock.class, PlayerInteractEvent.RightClickBlock.class, (callback, evt) -> {
            EventResultHolder<InteractionResult> result = callback.onUseBlock(evt.getEntity(), evt.getLevel(), evt.getHand(), evt.getHitVec());
            Optional<InteractionResult> optional = result.getInterrupt().filter(t -> t != InteractionResult.PASS);
            if (optional.isPresent()) {
                evt.setCancellationResult((InteractionResult) optional.get());
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(PlayerInteractEvents.AttackBlock.class, PlayerInteractEvent.LeftClickBlock.class, (callback, evt) -> {
            EventResultHolder<InteractionResult> result = callback.onAttackBlock(evt.getEntity(), evt.getLevel(), evt.getHand(), evt.getPos(), evt.getFace());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(PlayerInteractEvents.AttackBlockV2.class, PlayerInteractEvent.LeftClickBlock.class, (callback, evt) -> {
            EventResult result = callback.onAttackBlock(evt.getEntity(), evt.getLevel(), evt.getHand(), evt.getPos(), evt.getFace());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(PlayerInteractEvents.UseItem.class, PlayerInteractEvent.RightClickItem.class, (callback, evt) -> {
            EventResultHolder<InteractionResultHolder<ItemStack>> result = callback.onUseItem(evt.getEntity(), evt.getLevel(), evt.getHand());
            if (result.isInterrupt()) {
                InteractionResultHolder<ItemStack> holder = (InteractionResultHolder<ItemStack>) result.getInterrupt().get();
                if (holder.getResult() != InteractionResult.PASS) {
                    evt.setCancellationResult(holder.getResult());
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(PlayerInteractEvents.UseItemV2.class, PlayerInteractEvent.RightClickItem.class, (callback, evt) -> {
            EventResultHolder<InteractionResult> result = callback.onUseItem(evt.getEntity(), evt.getLevel(), evt.getHand());
            if (result.isInterrupt()) {
                InteractionResult interactionResult = (InteractionResult) result.getInterrupt().get();
                if (interactionResult != InteractionResult.PASS) {
                    evt.setCancellationResult(interactionResult);
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(PlayerInteractEvents.UseEntity.class, PlayerInteractEvent.EntityInteract.class, (callback, evt) -> {
            EventResultHolder<InteractionResult> result = callback.onUseEntity(evt.getEntity(), evt.getLevel(), evt.getHand(), evt.getTarget());
            if (result.isInterrupt()) {
                InteractionResult interactionResult = (InteractionResult) result.getInterrupt().get();
                if (interactionResult != InteractionResult.PASS) {
                    evt.setCancellationResult(interactionResult);
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(PlayerInteractEvents.UseEntityAt.class, PlayerInteractEvent.EntityInteractSpecific.class, (callback, evt) -> {
            EventResultHolder<InteractionResult> result = callback.onUseEntityAt(evt.getEntity(), evt.getLevel(), evt.getHand(), evt.getTarget(), evt.getLocalPos());
            if (result.isInterrupt()) {
                InteractionResult interactionResult = (InteractionResult) result.getInterrupt().get();
                if (interactionResult != InteractionResult.PASS) {
                    evt.setCancellationResult(interactionResult);
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(PlayerInteractEvents.AttackEntity.class, AttackEntityEvent.class, (callback, evt) -> {
            if (callback.onAttackEntity(evt.getEntity(), evt.getEntity().m_9236_(), InteractionHand.MAIN_HAND, evt.getTarget()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(PlayerXpEvents.PickupXp.class, PlayerXpEvent.PickupXp.class, (callback, evt) -> {
            if (callback.onPickupXp(evt.getEntity(), evt.getOrb()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(BonemealCallback.class, BonemealEvent.class, (callback, evt) -> {
            EventResult result = callback.onBonemeal(evt.getLevel(), evt.getPos(), evt.getBlock(), evt.getStack());
            if (result.isInterrupt()) {
                if (result.getAsBoolean()) {
                    evt.setResult(Result.ALLOW);
                } else {
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(LivingExperienceDropCallback.class, LivingExperienceDropEvent.class, (callback, evt) -> {
            DefaultedInt droppedExperience = DefaultedInt.fromEvent(evt::setDroppedExperience, evt::getDroppedExperience, evt::getOriginalExperience);
            if (callback.onLivingExperienceDrop(evt.getEntity(), evt.getAttackingPlayer(), droppedExperience).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(BlockEvents.Break.class, BlockEvent.BreakEvent.class, (callback, evt) -> {
            if (!(evt.getState().m_60734_() instanceof GameMasterBlock) || evt.getPlayer().canUseGameMasterBlocks()) {
                GameType gameType = ((ServerPlayer) evt.getPlayer()).gameMode.getGameModeForPlayer();
                if (!evt.getPlayer().blockActionRestricted((Level) evt.getLevel(), evt.getPos(), gameType)) {
                    if (!evt.getPlayer().m_21205_().onBlockStartBreak(evt.getPos(), evt.getPlayer())) {
                        EventResult result = callback.onBreakBlock((ServerLevel) evt.getLevel(), evt.getPos(), evt.getState(), evt.getPlayer(), evt.getPlayer().m_21205_());
                        if (result.isInterrupt()) {
                            evt.setCanceled(true);
                        }
                    }
                }
            }
        });
        INSTANCE.register(BlockEvents.DropExperience.class, BlockEvent.BreakEvent.class, (callback, evt) -> {
            MutableInt experienceToDrop = MutableInt.fromEvent(evt::setExpToDrop, evt::getExpToDrop);
            callback.onDropExperience((ServerLevel) evt.getLevel(), evt.getPos(), evt.getState(), evt.getPlayer(), evt.getPlayer().m_21205_(), experienceToDrop);
        });
        INSTANCE.register(BlockEvents.FarmlandTrample.class, BlockEvent.FarmlandTrampleEvent.class, (callback, evt) -> {
            if (callback.onFarmlandTrample((Level) evt.getLevel(), evt.getPos(), evt.getState(), evt.getFallDistance(), evt.getEntity()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(PlayerTickEvents.Start.class, TickEvent.PlayerTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.START) {
                callback.onStartPlayerTick(evt.player);
            }
        });
        INSTANCE.register(PlayerTickEvents.End.class, TickEvent.PlayerTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.END) {
                callback.onEndPlayerTick(evt.player);
            }
        });
        INSTANCE.register(LivingFallCallback.class, LivingFallEvent.class, (callback, evt) -> {
            MutableFloat fallDistance = MutableFloat.fromEvent(evt::setDistance, evt::getDistance);
            MutableFloat damageMultiplier = MutableFloat.fromEvent(evt::setDamageMultiplier, evt::getDamageMultiplier);
            if (callback.onLivingFall(evt.getEntity(), fallDistance, damageMultiplier).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(RegisterCommandsCallback.class, RegisterCommandsEvent.class, (callback, evt) -> callback.onRegisterCommands(evt.getDispatcher(), evt.getBuildContext(), evt.getCommandSelection()));
        INSTANCE.register(LootTableLoadEvents.Replace.class, LootTableLoadEvent.class, (callback, evt) -> {
            MutableValue<LootTable> table = MutableValue.fromEvent(evt::setTable, evt::getTable);
            callback.onReplaceLootTable(evt.getName(), table);
        });
        INSTANCE.register(LootTableLoadEvents.Modify.class, LootTableModifyEvent.class, (callback, evt) -> callback.onModifyLootTable(evt.getLootDataManager(), evt.getIdentifier(), evt::addPool, evt::removePool));
        INSTANCE.register(AnvilRepairCallback.class, AnvilRepairEvent.class, (callback, evt) -> {
            MutableFloat breakChance = MutableFloat.fromEvent(evt::setBreakChance, evt::getBreakChance);
            callback.onAnvilRepair(evt.getEntity(), evt.getLeft(), evt.getRight(), evt.getOutput(), breakChance);
        });
        INSTANCE.register(ItemTouchCallback.class, EntityItemPickupEvent.class, (callback, evt) -> {
            EventResult result = callback.onItemTouch(evt.getEntity(), evt.getItem());
            if (result.isInterrupt()) {
                if (result.getAsBoolean()) {
                    evt.setResult(Result.ALLOW);
                } else {
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(PlayerEvents.ItemPickup.class, PlayerEvent.ItemPickupEvent.class, (callback, evt) -> callback.onItemPickup(evt.getEntity(), evt.getOriginalEntity(), evt.getStack()));
        INSTANCE.register(LootingLevelCallback.class, LootingLevelEvent.class, (callback, evt) -> {
            MutableInt lootingLevel = MutableInt.fromEvent(evt::setLootingLevel, evt::getLootingLevel);
            callback.onLootingLevel(evt.getEntity(), evt.getDamageSource(), lootingLevel);
        });
        INSTANCE.register(AnvilUpdateCallback.class, AnvilUpdateEvent.class, (callback, evt) -> {
            ItemStack originalOutput = evt.getOutput();
            int originalEnchantmentCost = evt.getCost();
            int originalMaterialCost = evt.getMaterialCost();
            MutableValue<ItemStack> output = MutableValue.fromEvent(evt::setOutput, evt::getOutput);
            MutableInt enchantmentCost = MutableInt.fromEvent(evt::setCost, evt::getCost);
            MutableInt materialCost = MutableInt.fromEvent(evt::setMaterialCost, evt::getMaterialCost);
            EventResult result = callback.onAnvilUpdate(evt.getLeft(), evt.getRight(), output, evt.getName(), enchantmentCost, materialCost, evt.getPlayer());
            if (result.isInterrupt()) {
                if (!result.getAsBoolean()) {
                    evt.setCanceled(true);
                }
            } else {
                evt.setOutput(originalOutput);
                evt.setCost(originalEnchantmentCost);
                evt.setMaterialCost(originalMaterialCost);
            }
        });
        INSTANCE.register(LivingDropsCallback.class, LivingDropsEvent.class, (callback, evt) -> {
            if (callback.onLivingDrops(evt.getEntity(), evt.getSource(), evt.getDrops(), evt.getLootingLevel(), evt.isRecentlyHit()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(LivingEvents.Tick.class, LivingEvent.LivingTickEvent.class, (callback, evt) -> {
            if (callback.onLivingTick(evt.getEntity()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(ArrowLooseCallback.class, ArrowLooseEvent.class, (callback, evt) -> {
            MutableInt charge = MutableInt.fromEvent(evt::setCharge, evt::getCharge);
            if (callback.onArrowLoose(evt.getEntity(), evt.getBow(), evt.getLevel(), charge, evt.hasAmmo()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(LivingHurtCallback.class, LivingHurtEvent.class, (callback, evt) -> {
            MutableFloat amount = MutableFloat.fromEvent(evt::setAmount, evt::getAmount);
            if (callback.onLivingHurt(evt.getEntity(), evt.getSource(), amount).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(UseItemEvents.Start.class, LivingEntityUseItemEvent.Start.class, (callback, evt) -> {
            MutableInt useDuration = MutableInt.fromEvent(evt::setDuration, evt::getDuration);
            if (callback.onUseItemStart(evt.getEntity(), evt.getItem(), useDuration).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(UseItemEvents.Tick.class, LivingEntityUseItemEvent.Tick.class, (callback, evt) -> {
            MutableInt useItemRemaining = MutableInt.fromEvent(evt::setDuration, evt::getDuration);
            if (callback.onUseItemTick(evt.getEntity(), evt.getItem(), useItemRemaining).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(UseItemEvents.Stop.class, LivingEntityUseItemEvent.Stop.class, (callback, evt) -> {
            if (callback.onUseItemStop(evt.getEntity(), evt.getItem(), evt.getDuration()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(UseItemEvents.Finish.class, LivingEntityUseItemEvent.Finish.class, (callback, evt) -> {
            MutableValue<ItemStack> stack = MutableValue.fromEvent(evt::setResultStack, evt::getResultStack);
            callback.onUseItemFinish(evt.getEntity(), stack, evt.getDuration(), evt.getItem());
        });
        INSTANCE.register(ShieldBlockCallback.class, ShieldBlockEvent.class, (callback, evt) -> {
            DefaultedFloat blockedDamage = DefaultedFloat.fromEvent(evt::setBlockedDamage, evt::getBlockedDamage, evt::getOriginalBlockedDamage);
            if (callback.onShieldBlock(evt.getEntity(), evt.getDamageSource(), blockedDamage).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(TagsUpdatedCallback.class, TagsUpdatedEvent.class, (callback, evt) -> callback.onTagsUpdated(evt.getRegistryAccess(), evt.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED));
        INSTANCE.register(ExplosionEvents.Start.class, ExplosionEvent.Start.class, (callback, evt) -> {
            if (callback.onExplosionStart(evt.getLevel(), evt.getExplosion()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(ExplosionEvents.Detonate.class, ExplosionEvent.Detonate.class, (callback, evt) -> callback.onExplosionDetonate(evt.getLevel(), evt.getExplosion(), evt.getAffectedBlocks(), evt.getAffectedEntities()));
        INSTANCE.register(SyncDataPackContentsCallback.class, OnDatapackSyncEvent.class, (callback, evt) -> {
            if (evt.getPlayer() != null) {
                callback.onSyncDataPackContents(evt.getPlayer(), true);
            } else {
                for (ServerPlayer player : evt.getPlayerList().getPlayers()) {
                    callback.onSyncDataPackContents(player, false);
                }
            }
        });
        INSTANCE.register(ServerLifecycleEvents.ServerStarting.class, ServerAboutToStartEvent.class, (callback, evt) -> callback.onServerStarting(evt.getServer()));
        INSTANCE.register(ServerLifecycleEvents.ServerStarted.class, ServerStartedEvent.class, (callback, evt) -> callback.onServerStarted(evt.getServer()));
        INSTANCE.register(ServerLifecycleEvents.ServerStopping.class, ServerStoppingEvent.class, (callback, evt) -> callback.onServerStopping(evt.getServer()));
        INSTANCE.register(ServerLifecycleEvents.ServerStopped.class, ServerStoppedEvent.class, (callback, evt) -> callback.onServerStopped(evt.getServer()));
        INSTANCE.register(PlayLevelSoundEvents.AtPosition.class, PlayLevelSoundEvent.AtPosition.class, (callback, evt) -> {
            MutableValue<Holder<SoundEvent>> sound = MutableValue.fromEvent(evt::setSound, evt::getSound);
            MutableValue<SoundSource> source = MutableValue.fromEvent(evt::setSource, evt::getSource);
            DefaultedFloat volume = DefaultedFloat.fromEvent(evt::setNewVolume, evt::getNewVolume, evt::getOriginalVolume);
            DefaultedFloat pitch = DefaultedFloat.fromEvent(evt::setNewPitch, evt::getNewPitch, evt::getOriginalPitch);
            if (callback.onPlaySoundAtPosition(evt.getLevel(), evt.getPosition(), sound, source, volume, pitch).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(PlayLevelSoundEvents.AtEntity.class, PlayLevelSoundEvent.AtEntity.class, (callback, evt) -> {
            MutableValue<Holder<SoundEvent>> sound = MutableValue.fromEvent(evt::setSound, evt::getSound);
            MutableValue<SoundSource> source = MutableValue.fromEvent(evt::setSource, evt::getSource);
            DefaultedFloat volume = DefaultedFloat.fromEvent(evt::setNewVolume, evt::getNewVolume, evt::getOriginalVolume);
            DefaultedFloat pitch = DefaultedFloat.fromEvent(evt::setNewPitch, evt::getNewPitch, evt::getOriginalPitch);
            if (callback.onPlaySoundAtEntity(evt.getLevel(), evt.getEntity(), sound, source, volume, pitch).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(ServerEntityLevelEvents.Load.class, EntityJoinLevelEvent.class, (callback, evt) -> {
            if (!evt.getLevel().isClientSide) {
                if (callback.onEntityLoad(evt.getEntity(), (ServerLevel) evt.getLevel(), !evt.loadedFromDisk() && evt.getEntity() instanceof Mob mob ? mob.getSpawnType() : null).isInterrupt()) {
                    if (evt.getEntity() instanceof Player) {
                        throw new UnsupportedOperationException("Cannot prevent player from spawning in!");
                    }
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(ServerEntityLevelEvents.LoadV2.class, EntityJoinLevelEvent.class, (callback, evt) -> {
            if (!evt.getLevel().isClientSide) {
                if (callback.onEntityLoad(evt.getEntity(), (ServerLevel) evt.getLevel()).isInterrupt()) {
                    if (evt.getEntity() instanceof Player) {
                        throw new UnsupportedOperationException("Cannot prevent player from loading in!");
                    }
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(ServerEntityLevelEvents.Spawn.class, EntityJoinLevelEvent.class, (callback, evt) -> {
            if (!evt.getLevel().isClientSide && !evt.loadedFromDisk()) {
                if (callback.onEntitySpawn(evt.getEntity(), (ServerLevel) evt.getLevel(), evt.getEntity() instanceof Mob mob ? mob.getSpawnType() : null).isInterrupt()) {
                    if (evt.getEntity() instanceof Player) {
                        throw new UnsupportedOperationException("Cannot prevent player from spawning in!");
                    }
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(ServerEntityLevelEvents.Remove.class, EntityLeaveLevelEvent.class, (callback, evt) -> {
            if (!evt.getLevel().isClientSide) {
                callback.onEntityRemove(evt.getEntity(), (ServerLevel) evt.getLevel());
            }
        });
        INSTANCE.register(LivingDeathCallback.class, LivingDeathEvent.class, (callback, evt) -> {
            EventResult result = callback.onLivingDeath(evt.getEntity(), evt.getSource());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(PlayerEvents.StartTracking.class, PlayerEvent.StartTracking.class, (callback, evt) -> callback.onStartTracking(evt.getTarget(), (ServerPlayer) evt.getEntity()));
        INSTANCE.register(PlayerEvents.StopTracking.class, PlayerEvent.StopTracking.class, (callback, evt) -> callback.onStopTracking(evt.getTarget(), (ServerPlayer) evt.getEntity()));
        INSTANCE.register(PlayerEvents.LoggedIn.class, PlayerEvent.PlayerLoggedInEvent.class, (callback, evt) -> callback.onLoggedIn((ServerPlayer) evt.getEntity()));
        INSTANCE.register(PlayerEvents.LoggedOut.class, PlayerEvent.PlayerLoggedOutEvent.class, (callback, evt) -> callback.onLoggedOut((ServerPlayer) evt.getEntity()));
        INSTANCE.register(PlayerEvents.AfterChangeDimension.class, PlayerEvent.PlayerChangedDimensionEvent.class, (callback, evt) -> {
            MinecraftServer server = CommonAbstractions.INSTANCE.getMinecraftServer();
            ServerLevel from = server.getLevel(evt.getFrom());
            ServerLevel to = server.getLevel(evt.getTo());
            Objects.requireNonNull(from, "level origin is null");
            Objects.requireNonNull(to, "level destination is null");
            callback.onAfterChangeDimension((ServerPlayer) evt.getEntity(), from, to);
        });
        INSTANCE.register(BabyEntitySpawnCallback.class, BabyEntitySpawnEvent.class, (callback, evt) -> {
            MutableValue<AgeableMob> child = MutableValue.fromEvent(evt::setChild, evt::getChild);
            if (callback.onBabyEntitySpawn(evt.getParentA(), evt.getParentB(), child).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(AnimalTameCallback.class, AnimalTameEvent.class, (callback, evt) -> {
            if (callback.onAnimalTame(evt.getAnimal(), evt.getTamer()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(LivingAttackCallback.class, LivingAttackEvent.class, (callback, evt) -> {
            if (callback.onLivingAttack(evt.getEntity(), evt.getSource(), evt.getAmount()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(PlayerEvents.Copy.class, PlayerEvent.Clone.class, (callback, evt) -> {
            evt.getOriginal().reviveCaps();
            callback.onCopy((ServerPlayer) evt.getOriginal(), (ServerPlayer) evt.getEntity(), !evt.isWasDeath());
            evt.getOriginal().invalidateCaps();
        });
        INSTANCE.register(PlayerEvents.Respawn.class, PlayerEvent.PlayerRespawnEvent.class, (callback, evt) -> callback.onRespawn((ServerPlayer) evt.getEntity(), evt.isEndConquered()));
        INSTANCE.register(ServerTickEvents.Start.class, TickEvent.ServerTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.START) {
                callback.onStartServerTick(evt.getServer());
            }
        });
        INSTANCE.register(ServerTickEvents.End.class, TickEvent.ServerTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.END) {
                callback.onEndServerTick(evt.getServer());
            }
        });
        INSTANCE.register(ServerLevelTickEvents.Start.class, TickEvent.LevelTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.START && evt.level instanceof ServerLevel level) {
                callback.onStartLevelTick(level.getServer(), level);
            }
        });
        INSTANCE.register(ServerLevelTickEvents.End.class, TickEvent.LevelTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.END && evt.level instanceof ServerLevel level) {
                callback.onEndLevelTick(level.getServer(), level);
            }
        });
        INSTANCE.register(ServerLevelEvents.Load.class, LevelEvent.Load.class, (callback, evt) -> {
            if (evt.getLevel() instanceof ServerLevel level) {
                callback.onLevelLoad(level.getServer(), level);
            }
        });
        INSTANCE.register(ServerLevelEvents.Unload.class, LevelEvent.Unload.class, (callback, evt) -> {
            if (evt.getLevel() instanceof ServerLevel level) {
                callback.onLevelUnload(level.getServer(), level);
            }
        });
        INSTANCE.register(ServerChunkEvents.Load.class, ChunkEvent.Load.class, (callback, evt) -> {
            if (evt.getLevel() instanceof ServerLevel level) {
                callback.onChunkLoad(level, (LevelChunk) evt.getChunk());
            }
        });
        INSTANCE.register(ServerChunkEvents.Unload.class, ChunkEvent.Unload.class, (callback, evt) -> {
            if (evt.getLevel() instanceof ServerLevel level) {
                callback.onChunkUnload(level, (LevelChunk) evt.getChunk());
            }
        });
        INSTANCE.register(ItemTossCallback.class, ItemTossEvent.class, (callback, evt) -> {
            if (callback.onItemToss(evt.getEntity(), evt.getPlayer()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(LivingKnockBackCallback.class, LivingKnockBackEvent.class, (callback, evt) -> {
            DefaultedDouble strength = DefaultedDouble.fromEvent(v -> evt.setStrength((float) v), evt::getStrength, evt::getOriginalStrength);
            DefaultedDouble ratioX = DefaultedDouble.fromEvent(evt::setRatioX, evt::getRatioX, evt::getOriginalRatioX);
            DefaultedDouble ratioZ = DefaultedDouble.fromEvent(evt::setRatioZ, evt::getRatioZ, evt::getOriginalRatioZ);
            if (callback.onLivingKnockBack(evt.getEntity(), strength, ratioX, ratioZ).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(ItemAttributeModifiersCallback.class, ItemAttributeModifierEvent.class, (callback, evt) -> {
            Multimap<Attribute, AttributeModifier> attributeModifiers = new ForgeAttributeModifiersMultimap(evt::getModifiers, evt::addModifier, evt::removeModifier, evt::removeAttribute, evt::clearModifiers);
            callback.onItemAttributeModifiers(evt.getItemStack(), evt.getSlotType(), attributeModifiers);
        });
        INSTANCE.register(ProjectileImpactCallback.class, ProjectileImpactEvent.class, (callback, evt) -> {
            if (callback.onProjectileImpact(evt.getProjectile(), evt.getRayTraceResult()).isInterrupt()) {
                evt.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
            }
        });
        INSTANCE.register(PlayerEvents.BreakSpeed.class, PlayerEvent.BreakSpeed.class, (callback, evt) -> {
            DefaultedFloat breakSpeed = DefaultedFloat.fromEvent(evt::setNewSpeed, evt::getNewSpeed, evt::getOriginalSpeed);
            if (callback.onBreakSpeed(evt.getEntity(), evt.getState(), breakSpeed).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(MobEffectEvents.Affects.class, MobEffectEvent.Applicable.class, (callback, evt) -> {
            EventResult result = callback.onMobEffectAffects(evt.getEntity(), evt.getEffectInstance());
            if (result.isInterrupt()) {
                evt.setResult(result.getAsBoolean() ? Result.ALLOW : Result.DENY);
            }
        });
        INSTANCE.register(MobEffectEvents.Apply.class, MobEffectEvent.Added.class, (callback, evt) -> callback.onMobEffectApply(evt.getEntity(), evt.getEffectInstance(), evt.getOldEffectInstance(), evt.getEffectSource()));
        INSTANCE.register(MobEffectEvents.Remove.class, MobEffectEvent.Remove.class, (callback, evt) -> {
            if (callback.onMobEffectRemove(evt.getEntity(), evt.getEffectInstance()).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(MobEffectEvents.Expire.class, MobEffectEvent.Expired.class, (callback, evt) -> callback.onMobEffectExpire(evt.getEntity(), evt.getEffectInstance()));
        INSTANCE.register(LivingEvents.Jump.class, LivingEvent.LivingJumpEvent.class, (callback, evt) -> EventImplHelper.onLivingJump(callback, evt.getEntity()));
        INSTANCE.register(LivingEvents.Visibility.class, LivingEvent.LivingVisibilityEvent.class, (callback, evt) -> callback.onLivingVisibility(evt.getEntity(), evt.getLookingEntity(), MutableDouble.fromEvent(visibilityModifier -> evt.modifyVisibility(visibilityModifier / evt.getVisibilityModifier()), evt::getVisibilityModifier)));
        INSTANCE.register(LivingChangeTargetCallback.class, LivingChangeTargetEvent.class, (callback, evt) -> {
            DefaultedValue<LivingEntity> target = DefaultedValue.fromEvent(evt::setNewTarget, evt::getNewTarget, evt::getOriginalTarget);
            if (callback.onLivingChangeTarget(evt.getEntity(), target).isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        INSTANCE.register(CheckMobDespawnCallback.class, MobSpawnEvent.AllowDespawn.class, (callback, evt) -> {
            EventResult result = callback.onCheckMobDespawn(evt.getEntity(), (ServerLevel) evt.getLevel());
            if (result.isInterrupt()) {
                evt.setResult(result.getAsBoolean() ? Result.ALLOW : Result.DENY);
            }
        });
        INSTANCE.register(GatherPotentialSpawnsCallback.class, LevelEvent.PotentialSpawns.class, (callback, evt) -> {
            ServerLevel level = (ServerLevel) evt.getLevel();
            List<MobSpawnSettings.SpawnerData> mobsAt = new PotentialSpawnsList<MobSpawnSettings.SpawnerData>(evt::getSpawnerDataList, spawnerData -> {
                evt.addSpawnerData(spawnerData);
                return true;
            }, evt::removeSpawnerData);
            callback.onGatherPotentialSpawns(level, level.structureManager(), level.getChunkSource().getGenerator(), evt.getMobCategory(), evt.getPos(), mobsAt);
        });
        INSTANCE.register(EntityRidingEvents.Start.class, EntityMountEvent.class, (callback, evt) -> {
            if (!evt.isDismounting()) {
                if (callback.onStartRiding(evt.getLevel(), evt.getEntity(), evt.getEntityBeingMounted()).isInterrupt()) {
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(EntityRidingEvents.Stop.class, EntityMountEvent.class, (callback, evt) -> {
            if (!evt.isMounting()) {
                if (callback.onStopRiding(evt.getLevel(), evt.getEntity(), evt.getEntityBeingMounted()).isInterrupt()) {
                    evt.setCanceled(true);
                }
            }
        });
        INSTANCE.register(GrindstoneEvents.Update.class, GrindstoneEvent.OnPlaceItem.class, (callback, evt) -> {
            ItemStack originalOutput = evt.getOutput();
            int originalExperienceReward = evt.getXp();
            MutableValue<ItemStack> output = MutableValue.fromEvent(evt::setOutput, evt::getOutput);
            MutableInt experienceReward = MutableInt.fromEvent(evt::setXp, evt::getXp);
            Player player = (Player) EventImplHelper.getGrindstoneUsingPlayer(evt.getTopItem(), evt.getBottomItem()).orElseThrow(NullPointerException::new);
            EventResult result = callback.onGrindstoneUpdate(evt.getTopItem(), evt.getBottomItem(), output, experienceReward, player);
            if (result.isInterrupt()) {
                if (!result.getAsBoolean()) {
                    evt.setCanceled(true);
                }
            } else {
                evt.setOutput(originalOutput);
                evt.setXp(originalExperienceReward);
            }
        });
        INSTANCE.register(GrindstoneEvents.Use.class, GrindstoneEvent.OnTakeItem.class, (callback, evt) -> {
            DefaultedValue<ItemStack> topInput = DefaultedValue.fromValue(evt.getTopItem());
            if (!evt.getNewTopItem().isEmpty()) {
                topInput.accept(evt.getNewTopItem());
            }
            DefaultedValue<ItemStack> bottomInput = DefaultedValue.fromValue(evt.getBottomItem());
            if (!evt.getNewBottomItem().isEmpty()) {
                bottomInput.accept(evt.getNewBottomItem());
            }
            Player player = (Player) EventImplHelper.getGrindstoneUsingPlayer(evt.getTopItem(), evt.getBottomItem()).orElseThrow(NullPointerException::new);
            callback.onGrindstoneUse(topInput, bottomInput, player);
            topInput.getAsOptional().ifPresent(evt::setNewTopItem);
            bottomInput.getAsOptional().ifPresent(evt::setNewBottomItem);
        });
        INSTANCE.register(ServerChunkEvents.Watch.class, ChunkWatchEvent.Watch.class, (callback, evt) -> callback.onChunkWatch(evt.getPlayer(), evt.getChunk(), evt.getLevel()));
        INSTANCE.register(ServerChunkEvents.Unwatch.class, ChunkWatchEvent.UnWatch.class, (callback, evt) -> callback.onChunkUnwatch(evt.getPlayer(), evt.getPos(), evt.getLevel()));
        INSTANCE.register(LivingEquipmentChangeCallback.class, LivingEquipmentChangeEvent.class, (callback, evt) -> callback.onLivingEquipmentChange(evt.getEntity(), evt.getSlot(), evt.getFrom(), evt.getTo()));
        if (ModLoaderEnvironment.INSTANCE.isClient()) {
            ForgeClientEventInvokers.register();
        }
    }

    @Override
    public <T, E extends Event> void register(Class<T> clazz, Class<E> event, ForgeEventInvokerRegistry.ForgeEventContextConsumer<T, E> converter, boolean joinInvokers) {
        Objects.requireNonNull(clazz, "type is null");
        Objects.requireNonNull(converter, "converter is null");
        IEventBus eventBus;
        if (IModBusEvent.class.isAssignableFrom(event)) {
            FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
            eventBus = context != null ? context.getModEventBus() : null;
        } else {
            eventBus = MinecraftForge.EVENT_BUS;
        }
        EventInvokerImpl.register(clazz, new ForgeEventInvokerRegistryImpl.ForgeEventInvoker<>(eventBus, event, converter), joinInvokers);
    }

    private static record ForgeEventInvoker<T, E extends Event>(@Nullable IEventBus eventBus, Class<E> event, ForgeEventInvokerRegistry.ForgeEventContextConsumer<T, E> converter) implements EventInvoker<T>, EventInvokerImpl.EventInvokerLike<T> {

        private static final Map<EventPhase, EventPriority> PHASE_TO_PRIORITY = Map.of(EventPhase.FIRST, EventPriority.HIGHEST, EventPhase.BEFORE, EventPriority.HIGH, EventPhase.DEFAULT, EventPriority.NORMAL, EventPhase.AFTER, EventPriority.LOW, EventPhase.LAST, EventPriority.LOWEST);

        @Override
        public EventInvoker<T> asEventInvoker(@Nullable Object context) {
            return (EventInvoker<T>) (context != null ? (phase, callback) -> this.register(phase, callback, context) : this);
        }

        @Override
        public void register(EventPhase phase, T callback) {
            this.register(phase, callback, null);
        }

        private void register(EventPhase phase, T callback, @Nullable Object context) {
            Objects.requireNonNull(phase, "phase is null");
            Objects.requireNonNull(callback, "callback is null");
            EventPriority eventPriority = (EventPriority) PHASE_TO_PRIORITY.getOrDefault(phase, EventPriority.NORMAL);
            IEventBus eventBus = this.eventBus;
            if (eventBus == null) {
                Objects.requireNonNull(context, "mod id context is null");
                eventBus = ModContainerHelper.getModEventBus((String) context);
            }
            if (eventBus != MinecraftForge.EVENT_BUS && eventPriority != EventPriority.NORMAL) {
                throw new IllegalStateException("mod event bus does not support event phases");
            } else {
                eventBus.addListener(eventPriority, false, this.event, evt -> this.converter.accept(callback, (E) evt, context));
            }
        }
    }
}