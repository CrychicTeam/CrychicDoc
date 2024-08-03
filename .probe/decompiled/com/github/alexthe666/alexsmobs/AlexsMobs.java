package com.github.alexthe666.alexsmobs;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.client.model.layered.AMModelLayers;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.config.BiomeConfig;
import com.github.alexthe666.alexsmobs.config.ConfigHolder;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.enchantment.AMEnchantmentRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.event.ServerEvents;
import com.github.alexthe666.alexsmobs.inventory.AMMenuRegistry;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.message.MessageCrowDismount;
import com.github.alexthe666.alexsmobs.message.MessageCrowMountPlayer;
import com.github.alexthe666.alexsmobs.message.MessageHurtMultipart;
import com.github.alexthe666.alexsmobs.message.MessageInteractMultipart;
import com.github.alexthe666.alexsmobs.message.MessageKangarooEat;
import com.github.alexthe666.alexsmobs.message.MessageKangarooInventorySync;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoDismount;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoMountPlayer;
import com.github.alexthe666.alexsmobs.message.MessageMungusBiomeChange;
import com.github.alexthe666.alexsmobs.message.MessageSendVisualFlagFromServer;
import com.github.alexthe666.alexsmobs.message.MessageSetPupfishChunkOnClient;
import com.github.alexthe666.alexsmobs.message.MessageStartDancing;
import com.github.alexthe666.alexsmobs.message.MessageSwingArm;
import com.github.alexthe666.alexsmobs.message.MessageSyncEntityPos;
import com.github.alexthe666.alexsmobs.message.MessageTarantulaHawkSting;
import com.github.alexthe666.alexsmobs.message.MessageTransmuteFromMenu;
import com.github.alexthe666.alexsmobs.message.MessageUpdateCapsid;
import com.github.alexthe666.alexsmobs.message.MessageUpdateEagleControls;
import com.github.alexthe666.alexsmobs.message.MessageUpdateTransmutablesToDisplay;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBannerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMCreativeTabRegistry;
import com.github.alexthe666.alexsmobs.misc.AMLootRegistry;
import com.github.alexthe666.alexsmobs.misc.AMPaintingRegistry;
import com.github.alexthe666.alexsmobs.misc.AMPointOfInterestRegistry;
import com.github.alexthe666.alexsmobs.misc.AMRecipeRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.world.AMFeatureRegistry;
import com.github.alexthe666.alexsmobs.world.AMLeafcutterAntBiomeModifier;
import com.github.alexthe666.alexsmobs.world.AMMobSpawnBiomeModifier;
import com.github.alexthe666.alexsmobs.world.AMMobSpawnStructureModifier;
import com.mojang.serialization.Codec;
import java.util.Calendar;
import java.util.Date;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("alexsmobs")
@EventBusSubscriber(modid = "alexsmobs")
public class AlexsMobs {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "alexsmobs";

    public static final SimpleChannel NETWORK_WRAPPER;

    private static final String PROTOCOL_VERSION = Integer.toString(1);

    public static final CommonProxy PROXY = (CommonProxy) DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    private static int packetsRegistered;

    private static boolean isAprilFools = false;

    private static boolean isHalloween = false;

    public AlexsMobs() {
        IEventBus modBusEvent = FMLJavaModLoadingContext.get().getModEventBus();
        modBusEvent.addListener(this::setup);
        modBusEvent.addListener(this::setupClient);
        modBusEvent.addListener(this::onModConfigEvent);
        modBusEvent.addListener(this::setupEntityModelLayers);
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        AMBlockRegistry.DEF_REG.register(modBusEvent);
        AMEntityRegistry.DEF_REG.register(modBusEvent);
        AMItemRegistry.DEF_REG.register(modBusEvent);
        AMTileEntityRegistry.DEF_REG.register(modBusEvent);
        AMPointOfInterestRegistry.DEF_REG.register(modBusEvent);
        AMFeatureRegistry.DEF_REG.register(modBusEvent);
        AMSoundRegistry.DEF_REG.register(modBusEvent);
        AMParticleRegistry.DEF_REG.register(modBusEvent);
        AMPaintingRegistry.DEF_REG.register(modBusEvent);
        AMEffectRegistry.EFFECT_DEF_REG.register(modBusEvent);
        AMEffectRegistry.POTION_DEF_REG.register(modBusEvent);
        AMEnchantmentRegistry.DEF_REG.register(modBusEvent);
        AMMenuRegistry.DEF_REG.register(modBusEvent);
        AMRecipeRegistry.DEF_REG.register(modBusEvent);
        AMLootRegistry.DEF_REG.register(modBusEvent);
        AMBannerRegistry.DEF_REG.register(modBusEvent);
        AMCreativeTabRegistry.DEF_REG.register(modBusEvent);
        DeferredRegister<Codec<? extends BiomeModifier>> biomeModifiers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "alexsmobs");
        biomeModifiers.register(modBusEvent);
        biomeModifiers.register("am_mob_spawns", AMMobSpawnBiomeModifier::makeCodec);
        biomeModifiers.register("am_leafcutter_ant_spawns", AMLeafcutterAntBiomeModifier::makeCodec);
        DeferredRegister<Codec<? extends StructureModifier>> structureModifiers = DeferredRegister.create(ForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, "alexsmobs");
        structureModifiers.register(modBusEvent);
        structureModifiers.register("am_structure_spawns", AMMobSpawnStructureModifier::makeCodec);
        modLoadingContext.registerConfig(Type.COMMON, ConfigHolder.COMMON_SPEC, "alexsmobs.toml");
        PROXY.init();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        isAprilFools = calendar.get(2) + 1 == 4 && calendar.get(5) == 1;
        isHalloween = calendar.get(2) + 1 == 10 && calendar.get(5) >= 29 && calendar.get(5) <= 31;
    }

    public static boolean isAprilFools() {
        return isAprilFools || AMConfig.superSecretSettings;
    }

    public static boolean isHalloween() {
        return isHalloween || AMConfig.superSecretSettings;
    }

    private void setupEntityModelLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        AMModelLayers.register(event);
    }

    @SubscribeEvent
    public void onModConfigEvent(ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ConfigHolder.COMMON_SPEC) {
            AMConfig.bake(config);
        }
        BiomeConfig.init();
    }

    public static <MSG> void sendMSGToServer(MSG message) {
        NETWORK_WRAPPER.sendToServer(message);
    }

    public static <MSG> void sendMSGToAll(MSG message) {
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendNonLocal(message, player);
        }
    }

    public static <MSG> void sendNonLocal(MSG msg, ServerPlayer player) {
        NETWORK_WRAPPER.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    private void setup(FMLCommonSetupEvent event) {
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageMosquitoMountPlayer.class, MessageMosquitoMountPlayer::write, MessageMosquitoMountPlayer::read, MessageMosquitoMountPlayer.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageMosquitoDismount.class, MessageMosquitoDismount::write, MessageMosquitoDismount::read, MessageMosquitoDismount.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageHurtMultipart.class, MessageHurtMultipart::write, MessageHurtMultipart::read, MessageHurtMultipart.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageCrowMountPlayer.class, MessageCrowMountPlayer::write, MessageCrowMountPlayer::read, MessageCrowMountPlayer.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageCrowDismount.class, MessageCrowDismount::write, MessageCrowDismount::read, MessageCrowDismount.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageMungusBiomeChange.class, MessageMungusBiomeChange::write, MessageMungusBiomeChange::read, MessageMungusBiomeChange.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageKangarooInventorySync.class, MessageKangarooInventorySync::write, MessageKangarooInventorySync::read, MessageKangarooInventorySync.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageKangarooEat.class, MessageKangarooEat::write, MessageKangarooEat::read, MessageKangarooEat.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdateCapsid.class, MessageUpdateCapsid::write, MessageUpdateCapsid::read, MessageUpdateCapsid.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSwingArm.class, MessageSwingArm::write, MessageSwingArm::read, MessageSwingArm.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdateEagleControls.class, MessageUpdateEagleControls::write, MessageUpdateEagleControls::read, MessageUpdateEagleControls.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSyncEntityPos.class, MessageSyncEntityPos::write, MessageSyncEntityPos::read, MessageSyncEntityPos.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageTarantulaHawkSting.class, MessageTarantulaHawkSting::write, MessageTarantulaHawkSting::read, MessageTarantulaHawkSting.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageStartDancing.class, MessageStartDancing::write, MessageStartDancing::read, MessageStartDancing.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageInteractMultipart.class, MessageInteractMultipart::write, MessageInteractMultipart::read, MessageInteractMultipart.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSendVisualFlagFromServer.class, MessageSendVisualFlagFromServer::write, MessageSendVisualFlagFromServer::read, MessageSendVisualFlagFromServer.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSetPupfishChunkOnClient.class, MessageSetPupfishChunkOnClient::write, MessageSetPupfishChunkOnClient::read, MessageSetPupfishChunkOnClient.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdateTransmutablesToDisplay.class, MessageUpdateTransmutablesToDisplay::write, MessageUpdateTransmutablesToDisplay::read, MessageUpdateTransmutablesToDisplay.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageTransmuteFromMenu.class, MessageTransmuteFromMenu::write, MessageTransmuteFromMenu::read, MessageTransmuteFromMenu.Handler::handle);
        event.enqueueWork(AMItemRegistry::init);
        event.enqueueWork(AMItemRegistry::initDispenser);
        AMAdvancementTriggerRegistry.init();
        AMEffectRegistry.init();
        AMRecipeRegistry.init();
    }

    private void setupClient(FMLClientSetupEvent event) {
        PROXY.clientInit();
    }

    static {
        NetworkRegistry.ChannelBuilder channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("alexsmobs", "main_channel"));
        String version = PROTOCOL_VERSION;
        channel = channel.clientAcceptedVersions(version::equals);
        version = PROTOCOL_VERSION;
        NETWORK_WRAPPER = channel.serverAcceptedVersions(version::equals).networkProtocolVersion(() -> AlexsMobs.PROTOCOL_VERSION).simpleChannel();
    }
}