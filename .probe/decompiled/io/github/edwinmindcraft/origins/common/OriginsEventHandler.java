package io.github.edwinmindcraft.origins.common;

import com.google.common.collect.ImmutableList;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.mixin.EntityAccessor;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.badge.BadgeManager;
import io.github.apace100.origins.command.OriginCommand;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.apace100.origins.power.OriginsPowerTypes;
import io.github.apace100.origins.registry.ModDamageSources;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.common.ApoliEventHandler;
import io.github.edwinmindcraft.calio.api.event.DynamicRegistrationEvent;
import io.github.edwinmindcraft.calio.api.event.CalioDynamicRegistryEvent.LoadComplete;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import io.github.edwinmindcraft.origins.common.capabilities.OriginContainer;
import io.github.edwinmindcraft.origins.common.network.S2COpenOriginScreen;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = "origins")
public class OriginsEventHandler {

    private static final Method DECREASE_AIR_SUPPLY = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "m_7302_", new Class[] { int.class });

    private static final Method INCREASE_AIR_SUPPLY = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "m_7305_", new Class[] { int.class });

    private static int increaseAirSupply(LivingEntity living, int value) {
        try {
            return (Integer) INCREASE_AIR_SUPPLY.invoke(living, value);
        } catch (InvocationTargetException | IllegalAccessException var3) {
            throw new RuntimeException(var3);
        }
    }

    private static int decreaseAirSupply(LivingEntity living, int value) {
        try {
            return (Integer) DECREASE_AIR_SUPPLY.invoke(living, value);
        } catch (InvocationTargetException | IllegalAccessException var3) {
            throw new RuntimeException(var3);
        }
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        OriginCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            event.addCapability(OriginContainer.ID, new OriginContainer(player));
        }
    }

    @SubscribeEvent
    public static void onDataSync(OnDatapackSyncEvent event) {
        PacketDistributor.PacketTarget target = event.getPlayer() == null ? PacketDistributor.ALL.noArg() : PacketDistributor.PLAYER.with(event::getPlayer);
        OriginsCommon.CHANNEL.send(target, BadgeManager.createPacket());
        if (event.getPlayer() != null) {
            IOriginContainer.get(event.getPlayer()).map(IOriginContainer::getSynchronizationPacket).ifPresent(packet -> OriginsCommon.CHANNEL.send(target, packet));
        }
    }

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        Advancement advancement = event.getAdvancement();
        Registry<Origin> originsRegistry = OriginsAPI.getOriginsRegistry();
        IOriginContainer.get(event.getEntity()).ifPresent(container -> container.getOrigins().forEach((layer, origin) -> originsRegistry.getHolder(origin).stream().flatMap(x -> ((Origin) x.get()).getUpgrades().stream()).filter(x -> Objects.equals(x.advancement(), advancement.getId())).findFirst().ifPresent(upgrade -> {
            try {
                Holder<Origin> target = upgrade.origin();
                if (target.isBound() && target.unwrapKey().isPresent()) {
                    container.setOrigin(layer, (ResourceKey<Origin>) target.unwrapKey().get());
                    container.synchronize();
                    if (!upgrade.announcement().isBlank()) {
                        event.getEntity().displayClientMessage(Component.translatable(upgrade.announcement()).withStyle(ChatFormatting.GOLD), false);
                    }
                }
            } catch (IllegalArgumentException var6) {
                Origins.LOGGER.error("Could not perform Origins upgrade from {} to {}, as the upgrade origin did not exist!", origin.location(), upgrade.origin().unwrapKey().orElse(null));
            }
        })));
    }

    @SubscribeEvent
    public static void reloadComplete(LoadComplete event) {
        OriginRegistry.clear();
        OriginLayers.clear();
        MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
        if (currentServer != null) {
            for (ServerPlayer player : currentServer.getPlayerList().getPlayers()) {
                IOriginContainer.get(player).ifPresent(container -> container.onReload(event.getRegistryManager()));
            }
        }
        if (OriginsConfigs.COMMON.updateOriginList(event.getRegistryManager(), event.getRegistryManager().get(OriginsDynamicRegistries.ORIGINS_REGISTRY)) && OriginsConfigs.COMMON_SPECS.isLoaded()) {
            OriginsConfigs.COMMON_SPECS.save();
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer sp && !event.getEntity().m_9236_().isClientSide()) {
            ((MinecraftServer) Objects.requireNonNull(sp.m_20194_())).m_18689_(() -> IOriginContainer.get(sp).ifPresent(container -> {
                if (!container.hasAllOrigins()) {
                    container.checkAutoChoosingLayers(true);
                    OriginsCommon.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> sp), container.getSynchronizationPacket());
                    container.synchronize();
                    if (container.hasAllOrigins()) {
                        container.onChosen(false);
                    } else {
                        OriginsCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new S2COpenOriginScreen(true));
                    }
                }
            }));
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player target && event.getEntity() instanceof ServerPlayer sp && !event.getEntity().m_9236_().isClientSide()) {
            ((MinecraftServer) Objects.requireNonNull(sp.m_20194_())).m_18689_(() -> IOriginContainer.get(target).ifPresent(x -> OriginsCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), x.getSynchronizationPacket())));
        }
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        LazyOptional<IOriginContainer> original = IOriginContainer.get(event.getOriginal());
        LazyOptional<IOriginContainer> player = IOriginContainer.get(event.getEntity());
        if (original.isPresent() != player.isPresent()) {
            Apoli.LOGGER.info("Capability mismatch: original:{}, new:{}", original.isPresent(), player.isPresent());
        }
        player.ifPresent(p -> original.ifPresent(o -> p.deserializeNBT(o.serializeNBT())));
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void playerChangedDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            IOriginContainer.get(event.getEntity()).ifPresent(IOriginContainer::synchronize);
        }
    }

    @SubscribeEvent
    public static void onPlayerTickEnd(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            IOriginContainer.get(event.player).ifPresent(IOriginContainer::tick);
            if (IPowerContainer.hasPower(player, OriginsPowerTypes.WATER_BREATHING.get())) {
                if (player.isEyeInFluidType(ForgeMod.WATER_TYPE.get()) || player.m_21023_(MobEffects.WATER_BREATHING) || player.m_21023_(MobEffects.CONDUIT_POWER)) {
                    if (player.m_20146_() < player.m_6062_()) {
                        player.m_20301_(increaseAirSupply(player, player.m_20146_()));
                    }
                } else if (!((EntityAccessor) player).callIsBeingRainedOn()) {
                    int landGain = increaseAirSupply(player, 0);
                    player.m_20301_(decreaseAirSupply(player, player.m_20146_()) - landGain);
                    if (player.m_20146_() == -20) {
                        player.m_20301_(0);
                        for (int i = 0; i < 8; i++) {
                            double f = player.m_217043_().nextDouble() - player.m_217043_().nextDouble();
                            double g = player.m_217043_().nextDouble() - player.m_217043_().nextDouble();
                            double h = player.m_217043_().nextDouble() - player.m_217043_().nextDouble();
                            player.m_9236_().addParticle(ParticleTypes.BUBBLE, player.m_20208_(0.5), player.m_20188_() + player.m_217043_().nextGaussian() * 0.08, player.m_20262_(0.5), f * 0.5, g * 0.5 + 0.25, h * 0.5);
                        }
                        player.hurt(player.m_269291_().source(ModDamageSources.NO_WATER_FOR_GILLS), 2.0F);
                    }
                } else {
                    int landGain = increaseAirSupply(player, 0);
                    player.m_20301_(player.m_20146_() - landGain);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onOriginLoad(DynamicRegistrationEvent<Origin> event) {
        if (!((Origin) event.getOriginal()).isSpecial()) {
            if (!OriginsConfigs.COMMON.isOriginEnabled(event.getRegistryName())) {
                event.withCancellationReason("Disabled by config").setCanceled(true);
            } else {
                Origin original = (Origin) event.getOriginal();
                Set<Holder<ConfiguredPower<?, ?>>> originalPowers = (Set<Holder<ConfiguredPower<?, ?>>>) original.getPowers().stream().flatMap(HolderSet::m_203614_).collect(Collectors.toUnmodifiableSet());
                Set<Holder<ConfiguredPower<?, ?>>> powers = new HashSet(originalPowers);
                powers.removeIf(x -> {
                    Optional<ResourceKey<ConfiguredPower<?, ?>>> key = x.unwrapKey();
                    return key.isEmpty() || !OriginsConfigs.COMMON.isPowerEnabled(event.getRegistryName(), ((ResourceKey) key.get()).location());
                });
                if (powers.size() != originalPowers.size()) {
                    Origins.LOGGER.info("Powers [{}] were disabled by config for origin: {}", originalPowers.stream().filter(x -> !powers.contains(x)).map(x -> ((ResourceKey) x.unwrapKey().orElseThrow()).location().toString()).collect(Collectors.joining(",")), event.getRegistryName());
                }
                powers.removeIf(x -> {
                    Optional<ResourceKey<ConfiguredPower<?, ?>>> key = x.unwrapKey();
                    return key.isEmpty() || ApoliEventHandler.isPowerDisabled(((ResourceKey) key.get()).location());
                });
                if (powers.size() != originalPowers.size()) {
                    event.setNewEntry(new Origin(ImmutableList.of(HolderSet.direct(ImmutableList.copyOf(powers))), original.getIcon(), original.isUnchoosable(), original.getOrder(), original.getImpact(), original.getName(), original.getDescription(), original.getUpgrades(), original.isSpecial()));
                }
            }
        }
    }
}