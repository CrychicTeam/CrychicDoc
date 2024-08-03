package dev.xkmc.l2library.init;

import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.base.effects.ClientEffectCap;
import dev.xkmc.l2library.base.effects.EffectToClient;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import dev.xkmc.l2library.capability.conditionals.TokenToClient;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapToClient;
import dev.xkmc.l2library.init.data.L2TagGen;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2library.serial.conditions.BooleanValueCondition;
import dev.xkmc.l2library.serial.conditions.DoubleValueCondition;
import dev.xkmc.l2library.serial.conditions.IntValueCondition;
import dev.xkmc.l2library.serial.conditions.L2ConditionSerializer;
import dev.xkmc.l2library.serial.conditions.ListStringValueCondition;
import dev.xkmc.l2library.serial.conditions.StringValueCondition;
import dev.xkmc.l2library.serial.config.ConfigTypeEntry;
import dev.xkmc.l2library.serial.config.PacketHandler;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2library.serial.config.SyncPacket;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import dev.xkmc.l2library.serial.ingredients.MobEffectIngredient;
import dev.xkmc.l2library.serial.ingredients.PotionIngredient;
import dev.xkmc.l2library.util.raytrace.TargetSetPacket;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("l2library")
@EventBusSubscriber(modid = "l2library", bus = Bus.MOD)
public class L2Library {

    public static final String MODID = "l2library";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final L2Registrate REGISTRATE = new L2Registrate("l2library");

    public static final PacketHandlerWithConfig PACKET_HANDLER = new PacketHandlerWithConfig(new ResourceLocation("l2library", "main"), 1, e -> e.create(SyncPacket.class, NetworkDirection.PLAY_TO_CLIENT), e -> e.create(EffectToClient.class, NetworkDirection.PLAY_TO_CLIENT), e -> e.create(PlayerCapToClient.class, NetworkDirection.PLAY_TO_CLIENT), e -> e.create(TargetSetPacket.class, NetworkDirection.PLAY_TO_SERVER), e -> e.create(TokenToClient.class, NetworkDirection.PLAY_TO_CLIENT));

    public static final ConfigTypeEntry<MenuLayoutConfig> MENU_LAYOUT = new ConfigTypeEntry<>(PACKET_HANDLER, "menu_layout", MenuLayoutConfig.class);

    public L2Library() {
        Handlers.register();
        FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
        IEventBus bus = ctx.getModEventBus();
        MinecraftForge.EVENT_BUS.register(GeneralEventHandler.class);
        bus.register(L2Library.class);
        bus.addListener(PacketHandler::setup);
        L2LibraryConfig.init();
        ConditionalData.register();
        ClientEffectCap.register();
        REGISTRATE.addDataGenerator(L2TagGen.EFF_TAGS, L2TagGen::onEffectTagGen);
    }

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        for (GeneralCapabilityHolder<?, ?> holder : GeneralCapabilityHolder.INTERNAL_MAP.values()) {
            event.register(holder.holder_class);
        }
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(EnchantmentIngredient.INSTANCE.id(), EnchantmentIngredient.INSTANCE);
            CraftingHelper.register(PotionIngredient.INSTANCE.id(), PotionIngredient.INSTANCE);
            CraftingHelper.register(MobEffectIngredient.INSTANCE.id(), MobEffectIngredient.INSTANCE);
            CraftingHelper.register(new L2ConditionSerializer(BooleanValueCondition.ID, BooleanValueCondition.class));
            CraftingHelper.register(new L2ConditionSerializer(IntValueCondition.ID, IntValueCondition.class));
            CraftingHelper.register(new L2ConditionSerializer(DoubleValueCondition.ID, DoubleValueCondition.class));
            CraftingHelper.register(new L2ConditionSerializer(StringValueCondition.ID, StringValueCondition.class));
            CraftingHelper.register(new L2ConditionSerializer(ListStringValueCondition.ID, ListStringValueCondition.class));
        }
    }
}