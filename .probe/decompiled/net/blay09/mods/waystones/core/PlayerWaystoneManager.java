package net.blay09.mods.waystones.core;

import com.mojang.datafixers.util.Either;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.waystones.api.IMutableWaystone;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.IWaystoneTeleportContext;
import net.blay09.mods.waystones.api.TeleportDestination;
import net.blay09.mods.waystones.api.WaystoneActivatedEvent;
import net.blay09.mods.waystones.api.WaystoneTeleportError;
import net.blay09.mods.waystones.api.WaystoneTeleportEvent;
import net.blay09.mods.waystones.api.WaystonesAPI;
import net.blay09.mods.waystones.block.entity.WarpPlateBlockEntity;
import net.blay09.mods.waystones.config.DimensionalWarp;
import net.blay09.mods.waystones.config.InventoryButtonMode;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.network.message.TeleportEffectMessage;
import net.blay09.mods.waystones.tag.ModItemTags;
import net.blay09.mods.waystones.worldgen.namegen.NameGenerationMode;
import net.blay09.mods.waystones.worldgen.namegen.NameGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class PlayerWaystoneManager {

    private static final Logger logger = LogManager.getLogger();

    private static final IPlayerWaystoneData persistentPlayerWaystoneData = new PersistentPlayerWaystoneData();

    private static final IPlayerWaystoneData inMemoryPlayerWaystoneData = new InMemoryPlayerWaystoneData();

    public static boolean mayBreakWaystone(Player player, BlockGetter world, BlockPos pos) {
        return WaystonesConfig.getActive().restrictions.restrictToCreative && !player.getAbilities().instabuild ? false : (Boolean) WaystoneManager.get(player.m_20194_()).getWaystoneAt(world, pos).map(waystone -> {
            if (player.getAbilities().instabuild) {
                return true;
            } else if (waystone.wasGenerated() && WaystonesConfig.getActive().restrictions.generatedWaystonesUnbreakable) {
                return false;
            } else {
                boolean isGlobal = waystone.isGlobal();
                boolean mayBreakGlobalWaystones = !WaystonesConfig.getActive().restrictions.globalWaystoneSetupRequiresCreativeMode;
                return !isGlobal || mayBreakGlobalWaystones;
            }
        }).orElse(true);
    }

    public static boolean mayPlaceWaystone(@Nullable Player player) {
        return !WaystonesConfig.getActive().restrictions.restrictToCreative || player != null && player.getAbilities().instabuild;
    }

    public static WaystoneEditPermissions mayEditWaystone(Player player, Level world, IWaystone waystone) {
        if (WaystonesConfig.getActive().restrictions.restrictToCreative && !player.getAbilities().instabuild) {
            return WaystoneEditPermissions.NOT_CREATIVE;
        } else if (WaystonesConfig.getActive().restrictions.restrictRenameToOwner && !waystone.isOwner(player)) {
            return WaystoneEditPermissions.NOT_THE_OWNER;
        } else {
            return waystone.isGlobal() && !player.getAbilities().instabuild && WaystonesConfig.getActive().restrictions.globalWaystoneSetupRequiresCreativeMode ? WaystoneEditPermissions.GET_CREATIVE : WaystoneEditPermissions.ALLOW;
        }
    }

    public static boolean isWaystoneActivated(Player player, IWaystone waystone) {
        return getPlayerWaystoneData(player.m_9236_()).isWaystoneActivated(player, waystone);
    }

    public static void activateWaystone(Player player, IWaystone waystone) {
        if (!waystone.hasName() && waystone instanceof IMutableWaystone && waystone.wasGenerated()) {
            NameGenerationMode nameGenerationMode = WaystonesConfig.getActive().worldGen.nameGenerationMode;
            String name = NameGenerator.get(player.m_20194_()).getName(waystone, player.m_9236_().random, nameGenerationMode);
            ((IMutableWaystone) waystone).setName(name);
        }
        if (!waystone.hasOwner() && waystone instanceof IMutableWaystone) {
            ((IMutableWaystone) waystone).setOwnerUid(player.m_20148_());
        }
        if (player.m_20194_() != null) {
            WaystoneManager.get(player.m_20194_()).m_77762_();
        }
        if (!isWaystoneActivated(player, waystone) && waystone.getWaystoneType().equals(WaystoneTypes.WAYSTONE)) {
            getPlayerWaystoneData(player.m_9236_()).activateWaystone(player, waystone);
            Balm.getEvents().fireEvent(new WaystoneActivatedEvent(player, waystone));
        }
    }

    public static int predictExperienceLevelCost(Entity player, IWaystone waystone, WarpMode warpMode, @Nullable IWaystone fromWaystone) {
        WaystoneTeleportContext context = new WaystoneTeleportContext(player, waystone, null);
        context.getLeashedEntities().addAll(findLeashedAnimals(player));
        context.setFromWaystone(fromWaystone);
        return getExperienceLevelCost(player, waystone, warpMode, context);
    }

    public static int getExperienceLevelCost(Entity entity, IWaystone waystone, WarpMode warpMode, IWaystoneTeleportContext context) {
        if (entity instanceof Player player) {
            if (context.getFromWaystone() != null && waystone.getWaystoneUid().equals(context.getFromWaystone().getWaystoneUid())) {
                return 0;
            } else {
                boolean enableXPCost = !player.getAbilities().instabuild;
                int xpForLeashed = WaystonesConfig.getActive().xpCost.xpCostPerLeashed * context.getLeashedEntities().size();
                double xpCostMultiplier = warpMode.getXpCostMultiplier();
                if (waystone.isGlobal()) {
                    xpCostMultiplier *= WaystonesConfig.getActive().xpCost.globalWaystoneXpCostMultiplier;
                }
                BlockPos pos = waystone.getPos();
                double dist = Math.sqrt(player.m_20275_((double) pos.m_123341_(), player.m_20186_(), (double) pos.m_123343_()));
                double minimumXpCost = WaystonesConfig.getActive().xpCost.minimumBaseXpCost;
                double maximumXpCost = WaystonesConfig.getActive().xpCost.maximumBaseXpCost;
                double xpLevelCost;
                if (waystone.getDimension() != player.m_9236_().dimension()) {
                    int dimensionalWarpXpCost = WaystonesConfig.getActive().xpCost.dimensionalWarpXpCost;
                    xpLevelCost = Mth.clamp((double) dimensionalWarpXpCost, minimumXpCost, (double) dimensionalWarpXpCost);
                } else if (WaystonesConfig.getActive().xpCost.blocksPerXpLevel > 0) {
                    xpLevelCost = Mth.clamp(Math.floor(dist / (double) ((float) WaystonesConfig.getActive().xpCost.blocksPerXpLevel)), minimumXpCost, maximumXpCost);
                    if (WaystonesConfig.getActive().xpCost.inverseXpCost) {
                        xpLevelCost = maximumXpCost - xpLevelCost;
                    }
                } else {
                    xpLevelCost = minimumXpCost;
                }
                return enableXPCost ? (int) Math.round((xpLevelCost + (double) xpForLeashed) * xpCostMultiplier) : 0;
            }
        } else {
            return 0;
        }
    }

    @Nullable
    public static IWaystone getInventoryButtonWaystone(Player player) {
        InventoryButtonMode inventoryButtonMode = WaystonesConfig.getActive().getInventoryButtonMode();
        if (inventoryButtonMode.isReturnToNearest()) {
            return getNearestWaystone(player);
        } else {
            return inventoryButtonMode.hasNamedTarget() ? (IWaystone) WaystoneManager.get(player.m_20194_()).findWaystoneByName(inventoryButtonMode.getNamedTarget()).orElse(null) : null;
        }
    }

    public static boolean canUseInventoryButton(Player player) {
        IWaystone waystone = getInventoryButtonWaystone(player);
        int xpLevelCost = waystone != null ? predictExperienceLevelCost(player, waystone, WarpMode.INVENTORY_BUTTON, null) : 0;
        return getInventoryButtonCooldownLeft(player) <= 0L && (xpLevelCost <= 0 || player.experienceLevel >= xpLevelCost);
    }

    public static boolean canUseWarpStone(Player player, ItemStack heldItem) {
        return getWarpStoneCooldownLeft(player) <= 0L;
    }

    public static double getCooldownMultiplier(IWaystone waystone) {
        return waystone.isGlobal() ? WaystonesConfig.getActive().cooldowns.globalWaystoneCooldownMultiplier : 1.0;
    }

    private static void informPlayer(Entity entity, String translationKey) {
        if (entity instanceof Player) {
            MutableComponent chatComponent = Component.translatable(translationKey);
            chatComponent.withStyle(ChatFormatting.RED);
            ((Player) entity).displayClientMessage(chatComponent, false);
        }
    }

    public static Consumer<WaystoneTeleportError> informRejectedTeleport(Entity entityToInform) {
        return error -> {
            logger.info("Rejected teleport: " + error.getClass().getSimpleName());
            if (error.getTranslationKey() != null) {
                informPlayer(entityToInform, error.getTranslationKey());
            }
        };
    }

    public static Either<List<Entity>, WaystoneTeleportError> tryTeleportToWaystone(Entity entity, IWaystone waystone, WarpMode warpMode, @Nullable IWaystone fromWaystone) {
        return WaystonesAPI.createDefaultTeleportContext(entity, waystone, warpMode, fromWaystone).flatMap(PlayerWaystoneManager::tryTeleport).ifRight(informRejectedTeleport(entity));
    }

    public static Either<List<Entity>, WaystoneTeleportError> tryTeleport(IWaystoneTeleportContext context) {
        WaystoneTeleportEvent.Pre event = new WaystoneTeleportEvent.Pre(context);
        Balm.getEvents().fireEvent(event);
        if (event.isCanceled()) {
            return Either.right(new WaystoneTeleportError.CancelledByEvent());
        } else {
            IWaystone waystone = context.getTargetWaystone();
            Entity entity = context.getEntity();
            WarpMode warpMode = context.getWarpMode();
            if (!canUseWarpMode(entity, warpMode, context.getWarpItem(), context.getFromWaystone())) {
                return Either.right(new WaystoneTeleportError.WarpModeRejected());
            } else if (!warpMode.getAllowTeleportPredicate().test(entity, waystone)) {
                return Either.right(new WaystoneTeleportError.WarpModeRejected());
            } else if (context.isDimensionalTeleport() && !event.getDimensionalTeleportResult().withDefault(() -> canDimensionalWarpBetween(entity, waystone))) {
                return Either.right(new WaystoneTeleportError.DimensionalWarpDenied());
            } else {
                if (!context.getLeashedEntities().isEmpty()) {
                    if (!WaystonesConfig.getActive().restrictions.transportLeashed) {
                        return Either.right(new WaystoneTeleportError.LeashedWarpDenied());
                    }
                    List<ResourceLocation> forbidden = WaystonesConfig.getActive().restrictions.leashedDenyList.stream().map(ResourceLocation::new).toList();
                    if (context.getLeashedEntities().stream().anyMatch(e -> forbidden.contains(BuiltInRegistries.ENTITY_TYPE.getKey(e.m_6095_())))) {
                        return Either.right(new WaystoneTeleportError.SpecificLeashedWarpDenied());
                    }
                    if (context.isDimensionalTeleport() && !WaystonesConfig.getActive().restrictions.transportLeashedDimensional) {
                        return Either.right(new WaystoneTeleportError.LeashedDimensionalWarpDenied());
                    }
                }
                if (entity instanceof Player && ((Player) entity).experienceLevel < context.getXpCost()) {
                    return Either.right(new WaystoneTeleportError.NotEnoughXp());
                } else {
                    boolean isCreativeMode = entity instanceof Player && ((Player) entity).getAbilities().instabuild;
                    if (!context.getWarpItem().isEmpty() && event.getConsumeItemResult().withDefault(() -> !isCreativeMode && context.consumesWarpItem())) {
                        context.getWarpItem().shrink(1);
                    }
                    if (entity instanceof Player player) {
                        applyCooldown(warpMode, player, context.getCooldown());
                        applyXpCost(player, context.getXpCost());
                    }
                    List<Entity> teleportedEntities = doTeleport(context);
                    Balm.getEvents().fireEvent(new WaystoneTeleportEvent.Post(context, teleportedEntities));
                    return Either.left(teleportedEntities);
                }
            }
        }
    }

    private static void sendHackySyncPacketsAfterTeleport(Entity entity) {
        if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetExperiencePacket(player.f_36080_, player.f_36079_, player.f_36078_));
        }
    }

    private static void applyXpCost(Player player, int xpLevelCost) {
        if (xpLevelCost > 0) {
            player.giveExperienceLevels(-xpLevelCost);
        }
    }

    private static void applyCooldown(WarpMode warpMode, Player player, int cooldown) {
        if (cooldown > 0) {
            Level level = player.m_9236_();
            switch(warpMode) {
                case INVENTORY_BUTTON:
                    getPlayerWaystoneData(level).setInventoryButtonCooldownUntil(player, System.currentTimeMillis() + (long) cooldown * 1000L);
                    break;
                case WARP_STONE:
                    getPlayerWaystoneData(level).setWarpStoneCooldownUntil(player, System.currentTimeMillis() + (long) cooldown * 1000L);
            }
            WaystoneSyncManager.sendWaystoneCooldowns(player);
        }
    }

    public static int getCooldownPeriod(WarpMode warpMode, IWaystone waystone) {
        return (int) ((double) getCooldownPeriod(warpMode) * getCooldownMultiplier(waystone));
    }

    private static int getCooldownPeriod(WarpMode warpMode) {
        return switch(warpMode) {
            case INVENTORY_BUTTON ->
                WaystonesConfig.getActive().cooldowns.inventoryButtonCooldown;
            case WARP_STONE ->
                WaystonesConfig.getActive().cooldowns.warpStoneCooldown;
            default ->
                0;
        };
    }

    private static boolean canDimensionalWarpBetween(Entity player, IWaystone waystone) {
        ResourceLocation fromDimension = player.level().dimension().location();
        ResourceLocation toDimension = waystone.getDimension().location();
        Collection<String> dimensionAllowList = WaystonesConfig.getActive().restrictions.dimensionalWarpAllowList;
        Collection<String> dimensionDenyList = WaystonesConfig.getActive().restrictions.dimensionalWarpDenyList;
        if (dimensionAllowList.isEmpty() || dimensionAllowList.contains(toDimension.toString()) && dimensionAllowList.contains(fromDimension.toString())) {
            if (dimensionDenyList.isEmpty() || !dimensionDenyList.contains(toDimension.toString()) && !dimensionDenyList.contains(fromDimension.toString())) {
                DimensionalWarp dimensionalWarpMode = WaystonesConfig.getActive().restrictions.dimensionalWarp;
                return dimensionalWarpMode == DimensionalWarp.ALLOW || dimensionalWarpMode == DimensionalWarp.GLOBAL_ONLY && waystone.isGlobal();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static ItemStack findWarpItem(Entity entity, WarpMode warpMode) {
        return switch(warpMode) {
            case WARP_STONE ->
                findWarpItem(entity, ModItemTags.WARP_STONES);
            case WARP_SCROLL ->
                findWarpItem(entity, ModItemTags.WARP_SCROLLS);
            case RETURN_SCROLL ->
                findWarpItem(entity, ModItemTags.RETURN_SCROLLS);
            case BOUND_SCROLL ->
                findWarpItem(entity, ModItemTags.BOUND_SCROLLS);
            default ->
                ItemStack.EMPTY;
        };
    }

    private static ItemStack findWarpItem(Entity entity, TagKey<Item> warpItemTag) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getMainHandItem().is(warpItemTag)) {
                return livingEntity.getMainHandItem();
            }
            if (livingEntity.getOffhandItem().is(warpItemTag)) {
                return livingEntity.getOffhandItem();
            }
        }
        return ItemStack.EMPTY;
    }

    public static List<Mob> findLeashedAnimals(Entity player) {
        return player.level().m_6443_(Mob.class, new AABB(player.blockPosition()).inflate(10.0), e -> player.equals(e.getLeashHolder()));
    }

    public static List<Entity> doTeleport(IWaystoneTeleportContext context) {
        List<Entity> teleportedEntities = teleportEntityAndAttached(context.getEntity(), context);
        context.getAdditionalEntities().forEach(additionalEntity -> teleportedEntities.addAll(teleportEntityAndAttached(additionalEntity, context)));
        ServerLevel sourceWorld = (ServerLevel) context.getEntity().level();
        BlockPos sourcePos = context.getEntity().blockPosition();
        TeleportDestination destination = context.getDestination();
        ServerLevel targetLevel = destination.getLevel();
        BlockPos targetPos = BlockPos.containing(destination.getLocation());
        if (targetLevel.m_7702_(targetPos) instanceof WarpPlateBlockEntity warpPlate) {
            teleportedEntities.forEach(warpPlate::markEntityForCooldown);
        }
        if (context.playsSound()) {
            sourceWorld.m_5594_(null, sourcePos, SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.1F, 1.0F);
            targetLevel.m_5594_(null, targetPos, SoundEvents.PORTAL_TRAVEL, SoundSource.PLAYERS, 0.1F, 1.0F);
        }
        if (context.playsEffect()) {
            teleportedEntities.forEach(additionalEntity -> Balm.getNetworking().sendToTracking(sourceWorld, sourcePos, new TeleportEffectMessage(sourcePos)));
            Balm.getNetworking().sendToTracking(targetLevel, targetPos, new TeleportEffectMessage(targetPos));
        }
        return teleportedEntities;
    }

    private static List<Entity> teleportEntityAndAttached(Entity entity, IWaystoneTeleportContext context) {
        ArrayList<Entity> teleportedEntities = new ArrayList();
        TeleportDestination destination = context.getDestination();
        ServerLevel targetLevel = destination.getLevel();
        Vec3 targetLocation = destination.getLocation();
        Direction targetDirection = destination.getDirection();
        Entity mount = entity.getVehicle();
        Entity teleportedMount = null;
        if (mount != null) {
            teleportedMount = teleportEntity(mount, targetLevel, targetLocation, targetDirection);
            teleportedEntities.add(teleportedMount);
        }
        List<Mob> leashedEntities = context.getLeashedEntities();
        List<Entity> teleportedLeashedEntities = new ArrayList();
        leashedEntities.forEach(leashedEntity -> {
            Entity teleportedLeashedEntity = teleportEntity(leashedEntity, targetLevel, targetLocation, targetDirection);
            teleportedEntities.add(teleportedLeashedEntity);
            teleportedLeashedEntities.add(teleportedLeashedEntity);
        });
        Entity teleportedEntity = teleportEntity(entity, targetLevel, targetLocation, targetDirection);
        teleportedEntities.add(teleportedEntity);
        teleportedLeashedEntities.forEach(teleportedLeashedEntity -> {
            if (teleportedLeashedEntity instanceof Mob teleportedLeashedMob) {
                teleportedLeashedMob.setLeashedTo(teleportedEntity, true);
            }
        });
        if (teleportedMount != null) {
        }
        return teleportedEntities;
    }

    private static Entity teleportEntity(Entity entity, ServerLevel targetWorld, Vec3 targetPos3d, Direction direction) {
        float yaw = direction.toYRot();
        double x = targetPos3d.x;
        double y = targetPos3d.y;
        double z = targetPos3d.z;
        if (entity instanceof ServerPlayer) {
            ChunkPos chunkPos = new ChunkPos(BlockPos.containing(x, y, z));
            targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkPos, 1, entity.getId());
            entity.stopRiding();
            if (((ServerPlayer) entity).m_5803_()) {
                ((ServerPlayer) entity).stopSleepInBed(true, true);
            }
            if (targetWorld == entity.level()) {
                ((ServerPlayer) entity).connection.teleport(x, y, z, yaw, entity.getXRot(), Collections.emptySet());
            } else {
                ((ServerPlayer) entity).teleportTo(targetWorld, x, y, z, yaw, entity.getXRot());
            }
            entity.setYHeadRot(yaw);
        } else {
            float pitch = Mth.clamp(entity.getXRot(), -90.0F, 90.0F);
            if (targetWorld == entity.level()) {
                entity.moveTo(x, y, z, yaw, pitch);
                entity.setYHeadRot(yaw);
            } else {
                entity.unRide();
                Entity oldEntity = entity;
                entity = entity.getType().create(targetWorld);
                if (entity == null) {
                    return oldEntity;
                }
                entity.restoreFrom(oldEntity);
                entity.moveTo(x, y, z, yaw, pitch);
                entity.setYHeadRot(yaw);
                oldEntity.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
                targetWorld.addDuringTeleport(entity);
            }
        }
        if (!(entity instanceof LivingEntity) || !((LivingEntity) entity).isFallFlying()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
            entity.setOnGround(true);
        }
        if (entity instanceof PathfinderMob) {
            ((PathfinderMob) entity).m_21573_().stop();
        }
        sendHackySyncPacketsAfterTeleport(entity);
        return entity;
    }

    public static void deactivateWaystone(Player player, IWaystone waystone) {
        getPlayerWaystoneData(player.m_9236_()).deactivateWaystone(player, waystone);
    }

    private static boolean canUseWarpMode(Entity entity, WarpMode warpMode, ItemStack heldItem, @Nullable IWaystone fromWaystone) {
        return switch(warpMode) {
            case INVENTORY_BUTTON ->
                entity instanceof Player && canUseInventoryButton((Player) entity);
            case WARP_STONE ->
                !heldItem.isEmpty() && heldItem.is(ModItemTags.WARP_STONES) && entity instanceof Player && canUseWarpStone((Player) entity, heldItem);
            case WARP_SCROLL ->
                !heldItem.isEmpty() && heldItem.is(ModItemTags.WARP_SCROLLS);
            case RETURN_SCROLL ->
                !heldItem.isEmpty() && heldItem.is(ModItemTags.RETURN_SCROLLS);
            case BOUND_SCROLL ->
                !heldItem.isEmpty() && heldItem.is(ModItemTags.BOUND_SCROLLS);
            case WAYSTONE_TO_WAYSTONE ->
                WaystonesConfig.getActive().restrictions.allowWaystoneToWaystoneTeleport && fromWaystone != null && fromWaystone.isValid() && fromWaystone.getWaystoneType().equals(WaystoneTypes.WAYSTONE);
            case SHARESTONE_TO_SHARESTONE ->
                fromWaystone != null && fromWaystone.isValid() && WaystoneTypes.isSharestone(fromWaystone.getWaystoneType());
            case WARP_PLATE ->
                fromWaystone != null && fromWaystone.isValid() && fromWaystone.getWaystoneType().equals(WaystoneTypes.WARP_PLATE);
            case PORTSTONE_TO_WAYSTONE ->
                fromWaystone != null && fromWaystone.isValid() && fromWaystone.getWaystoneType().equals(WaystoneTypes.PORTSTONE);
            case CUSTOM ->
                true;
        };
    }

    public static long getWarpStoneCooldownUntil(Player player) {
        return getPlayerWaystoneData(player.m_9236_()).getWarpStoneCooldownUntil(player);
    }

    public static long getWarpStoneCooldownLeft(Player player) {
        long cooldownUntil = getWarpStoneCooldownUntil(player);
        return Math.max(0L, cooldownUntil - System.currentTimeMillis());
    }

    public static void setWarpStoneCooldownUntil(Player player, long timeStamp) {
        getPlayerWaystoneData(player.m_9236_()).setWarpStoneCooldownUntil(player, timeStamp);
    }

    public static long getInventoryButtonCooldownUntil(Player player) {
        return getPlayerWaystoneData(player.m_9236_()).getInventoryButtonCooldownUntil(player);
    }

    public static long getInventoryButtonCooldownLeft(Player player) {
        long cooldownUntil = getInventoryButtonCooldownUntil(player);
        return Math.max(0L, cooldownUntil - System.currentTimeMillis());
    }

    public static void setInventoryButtonCooldownUntil(Player player, long timeStamp) {
        getPlayerWaystoneData(player.m_9236_()).setInventoryButtonCooldownUntil(player, timeStamp);
    }

    @Nullable
    public static IWaystone getNearestWaystone(Player player) {
        return (IWaystone) getPlayerWaystoneData(player.m_9236_()).getWaystones(player).stream().filter(it -> it.getDimension() == player.m_9236_().dimension()).min((first, second) -> {
            double firstDist = first.getPos().m_203198_(player.m_20185_(), player.m_20186_(), player.m_20189_());
            double secondDist = second.getPos().m_203198_(player.m_20185_(), player.m_20186_(), player.m_20189_());
            return (int) Math.round(firstDist) - (int) Math.round(secondDist);
        }).orElse(null);
    }

    public static List<IWaystone> getWaystones(Player player) {
        return getPlayerWaystoneData(player.m_9236_()).getWaystones(player);
    }

    public static IPlayerWaystoneData getPlayerWaystoneData(@Nullable Level world) {
        return world != null && !world.isClientSide ? persistentPlayerWaystoneData : inMemoryPlayerWaystoneData;
    }

    public static IPlayerWaystoneData getPlayerWaystoneData(BalmEnvironment side) {
        return side.isClient() ? inMemoryPlayerWaystoneData : persistentPlayerWaystoneData;
    }

    public static boolean mayTeleportToWaystone(Player player, IWaystone waystone) {
        return true;
    }

    public static void swapWaystoneSorting(Player player, int index, int otherIndex) {
        getPlayerWaystoneData(player.m_9236_()).swapWaystoneSorting(player, index, otherIndex);
    }

    public static boolean mayEditGlobalWaystones(Player player) {
        return player.getAbilities().instabuild || !WaystonesConfig.getActive().restrictions.globalWaystoneSetupRequiresCreativeMode;
    }

    public static void activeWaystoneForEveryone(@Nullable MinecraftServer server, IWaystone waystone) {
        if (server != null) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (!isWaystoneActivated(player, waystone)) {
                    activateWaystone(player, waystone);
                }
            }
        }
    }

    public static void removeKnownWaystone(@Nullable MinecraftServer server, IWaystone waystone) {
        if (server != null) {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                deactivateWaystone(player, waystone);
                WaystoneSyncManager.sendActivatedWaystones(player);
            }
        }
    }
}