package net.minecraftforge.event;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import java.io.File;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingPackSizeEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PermissionsChangedEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.AlterGroundEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkTicketLevelUpdatedEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.level.PistonEvent;
import net.minecraftforge.event.level.SaplingGrowTreeEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ForgeEventFactory {

    public static boolean onMultiBlockPlace(@Nullable Entity entity, List<BlockSnapshot> blockSnapshots, Direction direction) {
        BlockSnapshot snap = (BlockSnapshot) blockSnapshots.get(0);
        BlockState placedAgainst = snap.getLevel().m_8055_(snap.getPos().relative(direction.getOpposite()));
        BlockEvent.EntityMultiPlaceEvent event = new BlockEvent.EntityMultiPlaceEvent(blockSnapshots, placedAgainst, entity);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    public static boolean onBlockPlace(@Nullable Entity entity, @NotNull BlockSnapshot blockSnapshot, @NotNull Direction direction) {
        BlockState placedAgainst = blockSnapshot.getLevel().m_8055_(blockSnapshot.getPos().relative(direction.getOpposite()));
        BlockEvent.EntityPlaceEvent event = new BlockEvent.EntityPlaceEvent(blockSnapshot, placedAgainst, entity);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    public static BlockEvent.NeighborNotifyEvent onNeighborNotify(Level level, BlockPos pos, BlockState state, EnumSet<Direction> notifiedSides, boolean forceRedstoneUpdate) {
        BlockEvent.NeighborNotifyEvent event = new BlockEvent.NeighborNotifyEvent(level, pos, state, notifiedSides, forceRedstoneUpdate);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static boolean doPlayerHarvestCheck(Player player, BlockState state, boolean success) {
        PlayerEvent.HarvestCheck event = new PlayerEvent.HarvestCheck(player, state, success);
        MinecraftForge.EVENT_BUS.post(event);
        return event.canHarvest();
    }

    public static float getBreakSpeed(Player player, BlockState state, float original, BlockPos pos) {
        PlayerEvent.BreakSpeed event = new PlayerEvent.BreakSpeed(player, state, original, pos);
        return MinecraftForge.EVENT_BUS.post(event) ? -1.0F : event.getNewSpeed();
    }

    public static void onPlayerDestroyItem(Player player, @NotNull ItemStack stack, @Nullable InteractionHand hand) {
        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, stack, hand));
    }

    @Internal
    public static boolean checkSpawnPlacements(EntityType<?> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random, boolean defaultResult) {
        MobSpawnEvent.SpawnPlacementCheck event = new MobSpawnEvent.SpawnPlacementCheck(entityType, level, spawnType, pos, random, defaultResult);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult() == Result.DEFAULT ? defaultResult : event.getResult() == Result.ALLOW;
    }

    public static boolean checkSpawnPosition(Mob mob, ServerLevelAccessor level, MobSpawnType spawnType) {
        MobSpawnEvent.PositionCheck event = new MobSpawnEvent.PositionCheck(mob, level, spawnType, null);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult() != Result.DEFAULT ? event.getResult() == Result.ALLOW : mob.checkSpawnRules(level, spawnType) && mob.checkSpawnObstruction(level);
    }

    public static boolean checkSpawnPositionSpawner(Mob mob, ServerLevelAccessor level, MobSpawnType spawnType, SpawnData spawnData, BaseSpawner spawner) {
        MobSpawnEvent.PositionCheck event = new MobSpawnEvent.PositionCheck(mob, level, spawnType, null);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult() != Result.DEFAULT ? event.getResult() == Result.ALLOW : (spawnData.getCustomSpawnRules().isPresent() || mob.checkSpawnRules(level, spawnType)) && mob.checkSpawnObstruction(level);
    }

    @Nullable
    public static SpawnGroupData onFinalizeSpawn(Mob mob, ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag spawnTag) {
        MobSpawnEvent.FinalizeSpawn event = new MobSpawnEvent.FinalizeSpawn(mob, level, mob.m_20185_(), mob.m_20186_(), mob.m_20189_(), difficulty, spawnType, spawnData, spawnTag, null);
        boolean cancel = MinecraftForge.EVENT_BUS.post(event);
        if (!cancel) {
            mob.finalizeSpawn(level, event.getDifficulty(), event.getSpawnType(), event.getSpawnData(), event.getSpawnTag());
        }
        return cancel ? null : event.getSpawnData();
    }

    @Nullable
    public static MobSpawnEvent.FinalizeSpawn onFinalizeSpawnSpawner(Mob mob, ServerLevelAccessor level, DifficultyInstance difficulty, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag spawnTag, BaseSpawner spawner) {
        MobSpawnEvent.FinalizeSpawn event = new MobSpawnEvent.FinalizeSpawn(mob, level, mob.m_20185_(), mob.m_20186_(), mob.m_20189_(), difficulty, MobSpawnType.SPAWNER, spawnData, spawnTag, spawner);
        boolean cancel = MinecraftForge.EVENT_BUS.post(event);
        return cancel ? null : event;
    }

    public static Result canEntityDespawn(Mob entity, ServerLevelAccessor level) {
        MobSpawnEvent.AllowDespawn event = new MobSpawnEvent.AllowDespawn(entity, level);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult();
    }

    public static int getItemBurnTime(@NotNull ItemStack itemStack, int burnTime, @Nullable RecipeType<?> recipeType) {
        FurnaceFuelBurnTimeEvent event = new FurnaceFuelBurnTimeEvent(itemStack, burnTime, recipeType);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getBurnTime();
    }

    public static int getExperienceDrop(LivingEntity entity, Player attackingPlayer, int originalExperience) {
        LivingExperienceDropEvent event = new LivingExperienceDropEvent(entity, attackingPlayer, originalExperience);
        return MinecraftForge.EVENT_BUS.post(event) ? 0 : event.getDroppedExperience();
    }

    public static int getMaxSpawnPackSize(Mob entity) {
        LivingPackSizeEvent maxCanSpawnEvent = new LivingPackSizeEvent(entity);
        MinecraftForge.EVENT_BUS.post(maxCanSpawnEvent);
        return maxCanSpawnEvent.getResult() == Result.ALLOW ? maxCanSpawnEvent.getMaxPackSize() : entity.getMaxSpawnClusterSize();
    }

    public static Component getPlayerDisplayName(Player player, Component username) {
        PlayerEvent.NameFormat event = new PlayerEvent.NameFormat(player, username);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getDisplayname();
    }

    public static Component getPlayerTabListDisplayName(Player player) {
        PlayerEvent.TabListNameFormat event = new PlayerEvent.TabListNameFormat(player);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getDisplayName();
    }

    public static BlockState fireFluidPlaceBlockEvent(LevelAccessor level, BlockPos pos, BlockPos liquidPos, BlockState state) {
        BlockEvent.FluidPlaceBlockEvent event = new BlockEvent.FluidPlaceBlockEvent(level, pos, liquidPos, state);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getNewState();
    }

    public static ItemTooltipEvent onItemTooltip(ItemStack itemStack, @Nullable Player entityPlayer, List<Component> list, TooltipFlag flags) {
        ItemTooltipEvent event = new ItemTooltipEvent(itemStack, entityPlayer, list, flags);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static ZombieEvent.SummonAidEvent fireZombieSummonAid(Zombie zombie, Level level, int x, int y, int z, LivingEntity attacker, double summonChance) {
        ZombieEvent.SummonAidEvent summonEvent = new ZombieEvent.SummonAidEvent(zombie, level, x, y, z, attacker, summonChance);
        MinecraftForge.EVENT_BUS.post(summonEvent);
        return summonEvent;
    }

    public static boolean onEntityStruckByLightning(Entity entity, LightningBolt bolt) {
        return MinecraftForge.EVENT_BUS.post(new EntityStruckByLightningEvent(entity, bolt));
    }

    public static int onItemUseStart(LivingEntity entity, ItemStack item, int duration) {
        LivingEntityUseItemEvent event = new LivingEntityUseItemEvent.Start(entity, item, duration);
        return MinecraftForge.EVENT_BUS.post(event) ? -1 : event.getDuration();
    }

    public static int onItemUseTick(LivingEntity entity, ItemStack item, int duration) {
        LivingEntityUseItemEvent event = new LivingEntityUseItemEvent.Tick(entity, item, duration);
        return MinecraftForge.EVENT_BUS.post(event) ? -1 : event.getDuration();
    }

    public static boolean onUseItemStop(LivingEntity entity, ItemStack item, int duration) {
        return MinecraftForge.EVENT_BUS.post(new LivingEntityUseItemEvent.Stop(entity, item, duration));
    }

    public static ItemStack onItemUseFinish(LivingEntity entity, ItemStack item, int duration, ItemStack result) {
        LivingEntityUseItemEvent.Finish event = new LivingEntityUseItemEvent.Finish(entity, item, duration, result);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResultStack();
    }

    public static void onStartEntityTracking(Entity entity, Player player) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.StartTracking(player, entity));
    }

    public static void onStopEntityTracking(Entity entity, Player player) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.StopTracking(player, entity));
    }

    public static void firePlayerLoadingEvent(Player player, File playerDirectory, String uuidString) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.LoadFromFile(player, playerDirectory, uuidString));
    }

    public static void firePlayerSavingEvent(Player player, File playerDirectory, String uuidString) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.SaveToFile(player, playerDirectory, uuidString));
    }

    public static void firePlayerLoadingEvent(Player player, PlayerDataStorage playerFileData, String uuidString) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.LoadFromFile(player, playerFileData.getPlayerDataFolder(), uuidString));
    }

    @Nullable
    public static BlockState onToolUse(BlockState originalState, UseOnContext context, ToolAction toolAction, boolean simulate) {
        BlockEvent.BlockToolModificationEvent event = new BlockEvent.BlockToolModificationEvent(originalState, context, toolAction, simulate);
        return MinecraftForge.EVENT_BUS.post(event) ? null : event.getFinalState();
    }

    public static int onApplyBonemeal(@NotNull Player player, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ItemStack stack) {
        BonemealEvent event = new BonemealEvent(player, level, pos, state, stack);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return -1;
        } else if (event.getResult() == Result.ALLOW) {
            if (!level.isClientSide) {
                stack.shrink(1);
            }
            return 1;
        } else {
            return 0;
        }
    }

    @Nullable
    public static InteractionResultHolder<ItemStack> onBucketUse(@NotNull Player player, @NotNull Level level, @NotNull ItemStack stack, @Nullable HitResult target) {
        FillBucketEvent event = new FillBucketEvent(player, stack, level, target);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        } else if (event.getResult() == Result.ALLOW) {
            if (player.getAbilities().instabuild) {
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            } else {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, event.getFilledBucket());
                } else {
                    if (!player.getInventory().add(event.getFilledBucket())) {
                        player.drop(event.getFilledBucket(), false);
                    }
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
                }
            }
        } else {
            return null;
        }
    }

    public static PlayLevelSoundEvent.AtEntity onPlaySoundAtEntity(Entity entity, Holder<SoundEvent> name, SoundSource category, float volume, float pitch) {
        PlayLevelSoundEvent.AtEntity event = new PlayLevelSoundEvent.AtEntity(entity, name, category, volume, pitch);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static PlayLevelSoundEvent.AtPosition onPlaySoundAtPosition(Level level, double x, double y, double z, Holder<SoundEvent> name, SoundSource category, float volume, float pitch) {
        PlayLevelSoundEvent.AtPosition event = new PlayLevelSoundEvent.AtPosition(level, new Vec3(x, y, z), name, category, volume, pitch);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static int onItemExpire(ItemEntity entity, @NotNull ItemStack item) {
        if (item.isEmpty()) {
            return -1;
        } else {
            ItemExpireEvent event = new ItemExpireEvent(entity, item.isEmpty() ? 6000 : item.getItem().getEntityLifespan(item, entity.m_9236_()));
            return !MinecraftForge.EVENT_BUS.post(event) ? -1 : event.getExtraLife();
        }
    }

    public static int onItemPickup(ItemEntity entityItem, Player player) {
        Event event = new EntityItemPickupEvent(player, entityItem);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return -1;
        } else {
            return event.getResult() == Result.ALLOW ? 1 : 0;
        }
    }

    public static boolean canMountEntity(Entity entityMounting, Entity entityBeingMounted, boolean isMounting) {
        boolean isCanceled = MinecraftForge.EVENT_BUS.post(new EntityMountEvent(entityMounting, entityBeingMounted, entityMounting.level(), isMounting));
        if (isCanceled) {
            entityMounting.absMoveTo(entityMounting.getX(), entityMounting.getY(), entityMounting.getZ(), entityMounting.yRotO, entityMounting.xRotO);
            return false;
        } else {
            return true;
        }
    }

    public static boolean onAnimalTame(Animal animal, Player tamer) {
        return MinecraftForge.EVENT_BUS.post(new AnimalTameEvent(animal, tamer));
    }

    public static Player.BedSleepingProblem onPlayerSleepInBed(Player player, Optional<BlockPos> pos) {
        PlayerSleepInBedEvent event = new PlayerSleepInBedEvent(player, pos);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResultStatus();
    }

    public static void onPlayerWakeup(Player player, boolean wakeImmediately, boolean updateLevel) {
        MinecraftForge.EVENT_BUS.post(new PlayerWakeUpEvent(player, wakeImmediately, updateLevel));
    }

    public static void onPlayerFall(Player player, float distance, float multiplier) {
        MinecraftForge.EVENT_BUS.post(new PlayerFlyableFallEvent(player, distance, multiplier));
    }

    public static boolean onPlayerSpawnSet(Player player, ResourceKey<Level> levelKey, BlockPos pos, boolean forced) {
        return MinecraftForge.EVENT_BUS.post(new PlayerSetSpawnEvent(player, levelKey, pos, forced));
    }

    public static void onPlayerClone(Player player, Player oldPlayer, boolean wasDeath) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.Clone(player, oldPlayer, wasDeath));
    }

    public static boolean onExplosionStart(Level level, Explosion explosion) {
        return MinecraftForge.EVENT_BUS.post(new ExplosionEvent.Start(level, explosion));
    }

    public static void onExplosionDetonate(Level level, Explosion explosion, List<Entity> list, double diameter) {
        MinecraftForge.EVENT_BUS.post(new ExplosionEvent.Detonate(level, explosion, list));
    }

    public static boolean onCreateWorldSpawn(Level level, ServerLevelData settings) {
        return MinecraftForge.EVENT_BUS.post(new LevelEvent.CreateSpawnPosition(level, settings));
    }

    public static float onLivingHeal(LivingEntity entity, float amount) {
        LivingHealEvent event = new LivingHealEvent(entity, amount);
        return MinecraftForge.EVENT_BUS.post(event) ? 0.0F : event.getAmount();
    }

    public static boolean onPotionAttemptBrew(NonNullList<ItemStack> stacks) {
        NonNullList<ItemStack> tmp = NonNullList.withSize(stacks.size(), ItemStack.EMPTY);
        for (int x = 0; x < tmp.size(); x++) {
            tmp.set(x, stacks.get(x).copy());
        }
        PotionBrewEvent.Pre event = new PotionBrewEvent.Pre(tmp);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        } else {
            boolean changed = false;
            for (int x = 0; x < stacks.size(); x++) {
                changed |= ItemStack.matches(tmp.get(x), stacks.get(x));
                stacks.set(x, event.getItem(x));
            }
            if (changed) {
                onPotionBrewed(stacks);
            }
            return true;
        }
    }

    public static void onPotionBrewed(NonNullList<ItemStack> brewingItemStacks) {
        MinecraftForge.EVENT_BUS.post(new PotionBrewEvent.Post(brewingItemStacks));
    }

    public static void onPlayerBrewedPotion(Player player, ItemStack stack) {
        MinecraftForge.EVENT_BUS.post(new PlayerBrewedPotionEvent(player, stack));
    }

    @Nullable
    public static <T extends ICapabilityProvider> CapabilityDispatcher gatherCapabilities(Class<? extends T> type, T provider) {
        return gatherCapabilities(type, provider, null);
    }

    @Nullable
    public static <T extends ICapabilityProvider> CapabilityDispatcher gatherCapabilities(Class<? extends T> type, T provider, @Nullable ICapabilityProvider parent) {
        return gatherCapabilities(new AttachCapabilitiesEvent((Class<T>) type, provider), parent);
    }

    @Nullable
    private static CapabilityDispatcher gatherCapabilities(AttachCapabilitiesEvent<?> event, @Nullable ICapabilityProvider parent) {
        MinecraftForge.EVENT_BUS.post(event);
        return event.getCapabilities().size() <= 0 && parent == null ? null : new CapabilityDispatcher(event.getCapabilities(), event.getListeners(), parent);
    }

    public static boolean fireSleepingLocationCheck(LivingEntity player, BlockPos sleepingLocation) {
        SleepingLocationCheckEvent evt = new SleepingLocationCheckEvent(player, sleepingLocation);
        MinecraftForge.EVENT_BUS.post(evt);
        Result canContinueSleep = evt.getResult();
        return canContinueSleep == Result.DEFAULT ? (Boolean) player.getSleepingPos().map(pos -> {
            BlockState state = player.m_9236_().getBlockState(pos);
            return state.m_60734_().isBed(state, player.m_9236_(), pos, player);
        }).orElse(false) : canContinueSleep == Result.ALLOW;
    }

    public static boolean fireSleepingTimeCheck(Player player, Optional<BlockPos> sleepingLocation) {
        SleepingTimeCheckEvent evt = new SleepingTimeCheckEvent(player, sleepingLocation);
        MinecraftForge.EVENT_BUS.post(evt);
        Result canContinueSleep = evt.getResult();
        return canContinueSleep == Result.DEFAULT ? !player.m_9236_().isDay() : canContinueSleep == Result.ALLOW;
    }

    public static InteractionResultHolder<ItemStack> onArrowNock(ItemStack item, Level level, Player player, InteractionHand hand, boolean hasAmmo) {
        ArrowNockEvent event = new ArrowNockEvent(player, item, hand, level, hasAmmo);
        return MinecraftForge.EVENT_BUS.post(event) ? new InteractionResultHolder<>(InteractionResult.FAIL, item) : event.getAction();
    }

    public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
        ArrowLooseEvent event = new ArrowLooseEvent(player, stack, level, charge, hasAmmo);
        return MinecraftForge.EVENT_BUS.post(event) ? -1 : event.getCharge();
    }

    public static ProjectileImpactEvent.ImpactResult onProjectileImpactResult(Projectile projectile, HitResult ray) {
        ProjectileImpactEvent event = new ProjectileImpactEvent(projectile, ray);
        return MinecraftForge.EVENT_BUS.post(event) ? ProjectileImpactEvent.ImpactResult.SKIP_ENTITY : event.getImpactResult();
    }

    public static boolean onProjectileImpact(Projectile projectile, HitResult ray) {
        return onProjectileImpactResult(projectile, ray) != ProjectileImpactEvent.ImpactResult.DEFAULT;
    }

    public static LootTable loadLootTable(ResourceLocation name, LootTable table) {
        LootTableLoadEvent event = new LootTableLoadEvent(name, table);
        return MinecraftForge.EVENT_BUS.post(event) ? LootTable.EMPTY : event.getTable();
    }

    public static boolean canCreateFluidSource(Level level, BlockPos pos, BlockState state, boolean def) {
        BlockEvent.CreateFluidSourceEvent evt = new BlockEvent.CreateFluidSourceEvent(level, pos, state);
        MinecraftForge.EVENT_BUS.post(evt);
        Result result = evt.getResult();
        return result == Result.DEFAULT ? def : result == Result.ALLOW;
    }

    public static Optional<PortalShape> onTrySpawnPortal(LevelAccessor level, BlockPos pos, Optional<PortalShape> size) {
        if (!size.isPresent()) {
            return size;
        } else {
            return !MinecraftForge.EVENT_BUS.post(new BlockEvent.PortalSpawnEvent(level, pos, level.m_8055_(pos), (PortalShape) size.get())) ? size : Optional.empty();
        }
    }

    public static int onEnchantmentLevelSet(Level level, BlockPos pos, int enchantRow, int power, ItemStack itemStack, int enchantmentLevel) {
        EnchantmentLevelSetEvent e = new EnchantmentLevelSetEvent(level, pos, enchantRow, power, itemStack, enchantmentLevel);
        MinecraftForge.EVENT_BUS.post(e);
        return e.getEnchantLevel();
    }

    public static boolean onEntityDestroyBlock(LivingEntity entity, BlockPos pos, BlockState state) {
        return !MinecraftForge.EVENT_BUS.post(new LivingDestroyBlockEvent(entity, pos, state));
    }

    public static boolean getMobGriefingEvent(Level level, @Nullable Entity entity) {
        if (entity == null) {
            return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        } else {
            EntityMobGriefingEvent event = new EntityMobGriefingEvent(entity);
            MinecraftForge.EVENT_BUS.post(event);
            Result result = event.getResult();
            return result == Result.DEFAULT ? level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) : result == Result.ALLOW;
        }
    }

    public static SaplingGrowTreeEvent blockGrowFeature(LevelAccessor level, RandomSource randomSource, BlockPos pos, @Nullable Holder<ConfiguredFeature<?, ?>> holder) {
        SaplingGrowTreeEvent event = new SaplingGrowTreeEvent(level, randomSource, pos, holder);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static BlockState alterGround(LevelSimulatedReader level, RandomSource random, BlockPos pos, BlockState altered) {
        AlterGroundEvent event = new AlterGroundEvent(level, random, pos, altered);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getNewAlteredState();
    }

    public static void fireChunkTicketLevelUpdated(ServerLevel level, long chunkPos, int oldTicketLevel, int newTicketLevel, @Nullable ChunkHolder chunkHolder) {
        if (oldTicketLevel != newTicketLevel) {
            MinecraftForge.EVENT_BUS.post(new ChunkTicketLevelUpdatedEvent(level, chunkPos, oldTicketLevel, newTicketLevel, chunkHolder));
        }
    }

    public static void fireChunkWatch(ServerPlayer entity, LevelChunk chunk, ServerLevel level) {
        MinecraftForge.EVENT_BUS.post(new ChunkWatchEvent.Watch(entity, chunk, level));
    }

    public static void fireChunkUnWatch(ServerPlayer entity, ChunkPos chunkpos, ServerLevel level) {
        MinecraftForge.EVENT_BUS.post(new ChunkWatchEvent.UnWatch(entity, chunkpos, level));
    }

    public static boolean onPistonMovePre(Level level, BlockPos pos, Direction direction, boolean extending) {
        return MinecraftForge.EVENT_BUS.post(new PistonEvent.Pre(level, pos, direction, extending ? PistonEvent.PistonMoveType.EXTEND : PistonEvent.PistonMoveType.RETRACT));
    }

    public static boolean onPistonMovePost(Level level, BlockPos pos, Direction direction, boolean extending) {
        return MinecraftForge.EVENT_BUS.post(new PistonEvent.Post(level, pos, direction, extending ? PistonEvent.PistonMoveType.EXTEND : PistonEvent.PistonMoveType.RETRACT));
    }

    public static long onSleepFinished(ServerLevel level, long newTime, long minTime) {
        SleepFinishedTimeEvent event = new SleepFinishedTimeEvent(level, newTime, minTime);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getNewTime();
    }

    public static List<PreparableReloadListener> onResourceReload(ReloadableServerResources serverResources, RegistryAccess registryAccess) {
        AddReloadListenerEvent event = new AddReloadListenerEvent(serverResources, registryAccess);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getListeners();
    }

    public static void onCommandRegister(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection environment, CommandBuildContext context) {
        RegisterCommandsEvent event = new RegisterCommandsEvent(dispatcher, environment, context);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Deprecated(forRemoval = true, since = "1.20.1")
    public static EntityEvent.Size getEntitySizeForge(Entity entity, Pose pose, EntityDimensions size, float eyeHeight) {
        EntityEvent.Size evt = new EntityEvent.Size(entity, pose, size, eyeHeight);
        MinecraftForge.EVENT_BUS.post(evt);
        return evt;
    }

    @Deprecated(forRemoval = true, since = "1.20.1")
    public static EntityEvent.Size getEntitySizeForge(Entity entity, Pose pose, EntityDimensions oldSize, EntityDimensions newSize, float newEyeHeight) {
        EntityEvent.Size evt = new EntityEvent.Size(entity, pose, oldSize, newSize, entity.getEyeHeight(), newEyeHeight);
        MinecraftForge.EVENT_BUS.post(evt);
        return evt;
    }

    public static boolean canLivingConvert(LivingEntity entity, EntityType<? extends LivingEntity> outcome, Consumer<Integer> timer) {
        return !MinecraftForge.EVENT_BUS.post(new LivingConversionEvent.Pre(entity, outcome, timer));
    }

    public static void onLivingConvert(LivingEntity entity, LivingEntity outcome) {
        MinecraftForge.EVENT_BUS.post(new LivingConversionEvent.Post(entity, outcome));
    }

    public static EntityTeleportEvent.TeleportCommand onEntityTeleportCommand(Entity entity, double targetX, double targetY, double targetZ) {
        EntityTeleportEvent.TeleportCommand event = new EntityTeleportEvent.TeleportCommand(entity, targetX, targetY, targetZ);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static EntityTeleportEvent.SpreadPlayersCommand onEntityTeleportSpreadPlayersCommand(Entity entity, double targetX, double targetY, double targetZ) {
        EntityTeleportEvent.SpreadPlayersCommand event = new EntityTeleportEvent.SpreadPlayersCommand(entity, targetX, targetY, targetZ);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static EntityTeleportEvent.EnderEntity onEnderTeleport(LivingEntity entity, double targetX, double targetY, double targetZ) {
        EntityTeleportEvent.EnderEntity event = new EntityTeleportEvent.EnderEntity(entity, targetX, targetY, targetZ);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    @Internal
    public static EntityTeleportEvent.EnderPearl onEnderPearlLand(ServerPlayer entity, double targetX, double targetY, double targetZ, ThrownEnderpearl pearlEntity, float attackDamage, HitResult hitResult) {
        EntityTeleportEvent.EnderPearl event = new EntityTeleportEvent.EnderPearl(entity, targetX, targetY, targetZ, pearlEntity, attackDamage, hitResult);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static EntityTeleportEvent.ChorusFruit onChorusFruitTeleport(LivingEntity entity, double targetX, double targetY, double targetZ) {
        EntityTeleportEvent.ChorusFruit event = new EntityTeleportEvent.ChorusFruit(entity, targetX, targetY, targetZ);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    public static boolean onPermissionChanged(GameProfile gameProfile, int newLevel, PlayerList playerList) {
        int oldLevel = playerList.getServer().getProfilePermissions(gameProfile);
        ServerPlayer player = playerList.getPlayer(gameProfile.getId());
        return newLevel != oldLevel && player != null ? MinecraftForge.EVENT_BUS.post(new PermissionsChangedEvent(player, newLevel, oldLevel)) : false;
    }

    public static void firePlayerChangedDimensionEvent(Player player, ResourceKey<Level> fromDim, ResourceKey<Level> toDim) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerChangedDimensionEvent(player, fromDim, toDim));
    }

    public static void firePlayerLoggedIn(Player player) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedInEvent(player));
    }

    public static void firePlayerLoggedOut(Player player) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedOutEvent(player));
    }

    public static void firePlayerRespawnEvent(Player player, boolean endConquered) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.PlayerRespawnEvent(player, endConquered));
    }

    public static void firePlayerItemPickupEvent(Player player, ItemEntity item, ItemStack clone) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemPickupEvent(player, item, clone));
    }

    public static void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(player, crafted, craftMatrix));
    }

    public static void firePlayerSmeltedEvent(Player player, ItemStack smelted) {
        MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemSmeltedEvent(player, smelted));
    }

    public static void onRenderTickStart(float timer) {
        MinecraftForge.EVENT_BUS.post(new TickEvent.RenderTickEvent(TickEvent.Phase.START, timer));
    }

    public static void onRenderTickEnd(float timer) {
        MinecraftForge.EVENT_BUS.post(new TickEvent.RenderTickEvent(TickEvent.Phase.END, timer));
    }

    public static void onPlayerPreTick(Player player) {
        MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.START, player));
    }

    public static void onPlayerPostTick(Player player) {
        MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
    }

    public static void onPreLevelTick(Level level, BooleanSupplier haveTime) {
        MinecraftForge.EVENT_BUS.post(new TickEvent.LevelTickEvent(level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER, TickEvent.Phase.START, level, haveTime));
    }

    public static void onPostLevelTick(Level level, BooleanSupplier haveTime) {
        MinecraftForge.EVENT_BUS.post(new TickEvent.LevelTickEvent(level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER, TickEvent.Phase.END, level, haveTime));
    }

    public static void onPreClientTick() {
        MinecraftForge.EVENT_BUS.post(new TickEvent.ClientTickEvent(TickEvent.Phase.START));
    }

    public static void onPostClientTick() {
        MinecraftForge.EVENT_BUS.post(new TickEvent.ClientTickEvent(TickEvent.Phase.END));
    }

    public static void onPreServerTick(BooleanSupplier haveTime, MinecraftServer server) {
        MinecraftForge.EVENT_BUS.post(new TickEvent.ServerTickEvent(TickEvent.Phase.START, haveTime, server));
    }

    public static void onPostServerTick(BooleanSupplier haveTime, MinecraftServer server) {
        MinecraftForge.EVENT_BUS.post(new TickEvent.ServerTickEvent(TickEvent.Phase.END, haveTime, server));
    }

    public static WeightedRandomList<MobSpawnSettings.SpawnerData> getPotentialSpawns(LevelAccessor level, MobCategory category, BlockPos pos, WeightedRandomList<MobSpawnSettings.SpawnerData> oldList) {
        LevelEvent.PotentialSpawns event = new LevelEvent.PotentialSpawns(level, category, pos, oldList);
        return MinecraftForge.EVENT_BUS.post(event) ? WeightedRandomList.create() : WeightedRandomList.create(event.getSpawnerDataList());
    }

    @Internal
    public static void onAdvancementEarnedEvent(Player player, Advancement earned) {
        MinecraftForge.EVENT_BUS.post(new AdvancementEvent.AdvancementEarnEvent(player, earned));
    }

    @Internal
    public static void onAdvancementProgressedEvent(Player player, Advancement progressed, AdvancementProgress advancementProgress, String criterion, AdvancementEvent.AdvancementProgressEvent.ProgressType progressType) {
        MinecraftForge.EVENT_BUS.post(new AdvancementEvent.AdvancementProgressEvent(player, progressed, advancementProgress, criterion, progressType));
    }
}