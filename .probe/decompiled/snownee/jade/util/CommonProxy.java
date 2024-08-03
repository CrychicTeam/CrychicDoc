package snownee.jade.util;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import snownee.jade.Jade;
import snownee.jade.JadeCommonConfig;
import snownee.jade.addon.universal.ItemCollector;
import snownee.jade.addon.universal.ItemIterator;
import snownee.jade.addon.universal.ItemStorageProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.IJadeProvider;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.view.EnergyView;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.command.JadeServerCommand;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.WailaCommonRegistration;
import snownee.jade.impl.config.PluginConfig;
import snownee.jade.network.ReceiveDataPacket;
import snownee.jade.network.RequestEntityPacket;
import snownee.jade.network.RequestTilePacket;
import snownee.jade.network.ServerPingPacket;
import snownee.jade.network.ShowOverlayPacket;

@Mod("jade")
public final class CommonProxy {

    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("jade", "networking")).clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).networkProtocolVersion(() -> "2").simpleChannel();

    public static final Set<ResourceLocation> BLOCKED_UIDS = Sets.newHashSet(new ResourceLocation[] { new ResourceLocation("bluepower:machines"), new ResourceLocation("bluepower:parts") });

    public CommonProxy() {
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (a, b) -> true));
        FMLJavaModLoadingContext.get().getModEventBus().register(JadeCommonConfig.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
        MinecraftForge.EVENT_BUS.addListener(CommonProxy::playerJoin);
        MinecraftForge.EVENT_BUS.addListener(CommonProxy::registerServerCommand);
        if (isPhysicallyClient()) {
            ClientProxy.init();
        }
    }

    public static int showOrHideFromServer(Collection<ServerPlayer> players, boolean show) {
        ShowOverlayPacket msg = new ShowOverlayPacket(show);
        for (ServerPlayer player : players) {
            NETWORK.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
        return players.size();
    }

    public static boolean isBlockedUid(IJadeProvider provider) {
        try {
            return BLOCKED_UIDS.contains(provider.getUid());
        } catch (Throwable var2) {
            return true;
        }
    }

    public static boolean isMultipartEntity(Entity target) {
        return target.isMultipartEntity();
    }

    public static Entity wrapPartEntityParent(Entity target) {
        return target instanceof PartEntity<?> part ? part.getParent() : target;
    }

    public static int getPartEntityIndex(Entity entity) {
        if (entity instanceof PartEntity<?> part) {
            Entity parent = wrapPartEntityParent(entity);
            PartEntity<?>[] parts = parent.getParts();
            return parts == null ? -1 : List.of(parts).indexOf(part);
        } else {
            return -1;
        }
    }

    public static Entity getPartEntity(Entity parent, int index) {
        if (parent == null) {
            return null;
        } else if (index < 0) {
            return parent;
        } else {
            PartEntity<?>[] parts = parent.getParts();
            return (Entity) (parts != null && index < parts.length ? parts[index] : parent);
        }
    }

    private void setup(FMLCommonSetupEvent event) {
        NETWORK.registerMessage(0, ReceiveDataPacket.class, ReceiveDataPacket::write, ReceiveDataPacket::read, ReceiveDataPacket.Handler::onMessage, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        NETWORK.registerMessage(1, ServerPingPacket.class, ServerPingPacket::write, ServerPingPacket::read, ServerPingPacket.Handler::onMessage, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        NETWORK.registerMessage(2, RequestEntityPacket.class, RequestEntityPacket::write, RequestEntityPacket::read, RequestEntityPacket.Handler::onMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        NETWORK.registerMessage(3, RequestTilePacket.class, RequestTilePacket::write, RequestTilePacket::read, RequestTilePacket.Handler::onMessage, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        NETWORK.registerMessage(4, ShowOverlayPacket.class, ShowOverlayPacket::write, ShowOverlayPacket::read, ShowOverlayPacket.Handler::onMessage, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    private void loadComplete(FMLLoadCompleteEvent event) {
        for (String className : ModList.get().getAllScanData().stream().flatMap($ -> $.getAnnotations().stream()).filter($ -> {
            if (!$.annotationType().getClassName().equals(WailaPlugin.class.getName())) {
                return false;
            } else {
                String required = (String) $.annotationData().getOrDefault("value", "");
                return required.isEmpty() || ModList.get().isLoaded(required);
            }
        }).map(AnnotationData::memberName).toList()) {
            Jade.LOGGER.info("Start loading plugin at {}", className);
            try {
                Class<?> clazz = Class.forName(className);
                if (IWailaPlugin.class.isAssignableFrom(clazz)) {
                    IWailaPlugin plugin = (IWailaPlugin) clazz.getDeclaredConstructor().newInstance();
                    plugin.register(WailaCommonRegistration.INSTANCE);
                    if (isPhysicallyClient()) {
                        plugin.registerClient(WailaClientRegistration.INSTANCE);
                    }
                }
            } catch (Throwable var7) {
                Jade.LOGGER.error("Error loading plugin at %s".formatted(className), var7);
                Throwables.throwIfInstanceOf(var7, IllegalStateException.class);
                if (className.startsWith("snownee.jade.")) {
                    throw new RuntimeException(var7);
                }
            }
        }
        Jade.loadComplete();
    }

    private static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Jade.LOGGER.info("Syncing config to {} ({})", event.getEntity().getGameProfile().getName(), event.getEntity().getGameProfile().getId());
        NETWORK.sendTo(new ServerPingPacket(PluginConfig.INSTANCE), ((ServerPlayer) event.getEntity()).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Nullable
    public static String getLastKnownUsername(UUID uuid) {
        return UsernameCache.getLastKnownUsername(uuid);
    }

    public static File getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get().toFile();
    }

    public static boolean isShears(ItemStack tool) {
        return tool.is(Tags.Items.SHEARS);
    }

    public static boolean isShearable(BlockState state) {
        return state.m_60734_() instanceof IForgeShearable;
    }

    public static boolean isCorrectToolForDrops(BlockState state, Player player) {
        return ForgeHooks.isCorrectToolForDrops(state, player);
    }

    public static String getModIdFromItem(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("id")) {
            String s = stack.getTag().getString("id");
            if (s.contains(":")) {
                ResourceLocation id = ResourceLocation.tryParse(s);
                if (id != null) {
                    return id.getNamespace();
                }
            }
        }
        return stack.getItem().getCreatorModId(stack);
    }

    public static boolean isPhysicallyClient() {
        return FMLEnvironment.dist.isClient();
    }

    private static void registerServerCommand(RegisterCommandsEvent event) {
        JadeServerCommand.register(event.getDispatcher());
    }

    public static ItemCollector<?> createItemCollector(Object target, Cache<Object, ItemCollector<?>> containerCache) {
        if (target instanceof CapabilityProvider<?> capProvider && (!(target instanceof Entity) || target instanceof AbstractChestedHorse)) {
            try {
                IItemHandler itemHandler = capProvider.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
                if (itemHandler != null) {
                    return (ItemCollector<?>) containerCache.get(itemHandler, () -> new ItemCollector<>(JadeForgeUtils.fromItemHandler(itemHandler, target instanceof AbstractChestedHorse ? 2 : 0)));
                }
            } catch (Throwable var4) {
                WailaExceptionHandler.handleErr(var4, null, null, null);
            }
        }
        if (target instanceof Container) {
            return target instanceof ChestBlockEntity ? new ItemCollector<>(new ItemIterator.ContainerItemIterator(o -> {
                if (o instanceof ChestBlockEntity be) {
                    if (be.m_58900_().m_60734_() instanceof ChestBlock chestBlock) {
                        Container compound = ChestBlock.getContainer(chestBlock, be.m_58900_(), be.m_58904_(), be.m_58899_(), false);
                        if (compound != null) {
                            return compound;
                        }
                    }
                    return be;
                } else {
                    return null;
                }
            }, 0)) : new ItemCollector<>(new ItemIterator.ContainerItemIterator(0));
        } else {
            return ItemCollector.EMPTY;
        }
    }

    @Nullable
    public static List<ViewGroup<ItemStack>> containerGroup(Container container, Accessor<?> accessor) {
        try {
            return ((ItemCollector) ItemStorageProvider.INSTANCE.containerCache.get(container, () -> new ItemCollector<>(new ItemIterator.ContainerItemIterator(0)))).update(container, accessor.getLevel().getGameTime());
        } catch (ExecutionException var3) {
            return null;
        }
    }

    @Nullable
    public static List<ViewGroup<ItemStack>> storageGroup(Object storage, Accessor<?> accessor) {
        try {
            return ((ItemCollector) ItemStorageProvider.INSTANCE.containerCache.get(storage, () -> new ItemCollector<>(JadeForgeUtils.fromItemHandler((IItemHandler) storage, 0)))).update(storage, accessor.getLevel().getGameTime());
        } catch (ExecutionException var3) {
            return null;
        }
    }

    public static List<ViewGroup<CompoundTag>> wrapFluidStorage(Object target, @Nullable Player player) {
        if (target instanceof CapabilityProvider<?> capProvider) {
            IFluidHandler fluidHandler = capProvider.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
            if (fluidHandler != null) {
                return JadeForgeUtils.fromFluidHandler(fluidHandler);
            }
        }
        return null;
    }

    public static List<ViewGroup<CompoundTag>> wrapEnergyStorage(Object target, @Nullable Player player) {
        if (target instanceof CapabilityProvider<?> capProvider) {
            IEnergyStorage storage = capProvider.getCapability(ForgeCapabilities.ENERGY).orElse(null);
            if (storage != null && storage.getMaxEnergyStored() > 0) {
                ViewGroup<CompoundTag> group = new ViewGroup<>(List.of(EnergyView.of((long) storage.getEnergyStored(), (long) storage.getMaxEnergyStored())));
                group.getExtraData().putString("Unit", "FE");
                return List.of(group);
            }
        }
        return null;
    }

    public static boolean isDevEnv() {
        return !FMLEnvironment.production;
    }

    public static float getEnchantPowerBonus(BlockState state, Level world, BlockPos pos) {
        return state.getEnchantPowerBonus(world, pos);
    }

    public static ResourceLocation getId(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static ResourceLocation getId(EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
    }

    public static ResourceLocation getId(BlockEntityType<?> blockEntityType) {
        return BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntityType);
    }

    public static ResourceLocation getId(PaintingVariant motive) {
        return BuiltInRegistries.PAINTING_VARIANT.getKey(motive);
    }

    public static String getPlatformIdentifier() {
        return "forge";
    }

    public static MutableComponent getProfressionName(VillagerProfession profession) {
        ResourceLocation profName = ForgeRegistries.VILLAGER_PROFESSIONS.getKey(profession);
        return Component.translatable(EntityType.VILLAGER.getDescriptionId() + "." + (!"minecraft".equals(profName.getNamespace()) ? profName.getNamespace() + "." : "") + profName.getPath());
    }

    public static boolean isBoss(Entity entity) {
        EntityType<?> entityType = entity.getType();
        return entityType.is(Tags.EntityTypes.BOSSES) || entityType == EntityType.ENDER_DRAGON || entityType == EntityType.WITHER;
    }

    public static boolean isModLoaded(String modid) {
        try {
            return ModList.get().isLoaded(modid);
        } catch (Throwable var2) {
            return false;
        }
    }

    public static ItemStack getBlockPickedResult(BlockState state, Player player, BlockHitResult hitResult) {
        return state.getCloneItemStack(hitResult, player.m_9236_(), hitResult.getBlockPos(), player);
    }

    public static ItemStack getEntityPickedResult(Entity entity, Player player, EntityHitResult hitResult) {
        return entity.getPickedResult(hitResult);
    }

    public static Component getFluidName(JadeFluidObject fluid) {
        return toFluidStack(fluid).getDisplayName();
    }

    public static FluidStack toFluidStack(JadeFluidObject fluid) {
        return new FluidStack(fluid.getType(), (int) fluid.getAmount(), fluid.getTag());
    }

    public static Void handleNetworkingError(Throwable e) {
        Jade.LOGGER.error("Error receiving packet", e);
        return null;
    }
}