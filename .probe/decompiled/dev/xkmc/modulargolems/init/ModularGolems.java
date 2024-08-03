package dev.xkmc.modulargolems.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.ConfigTypeEntry;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.modulargolems.compat.curio.CurioCompatRegistry;
import dev.xkmc.modulargolems.compat.materials.common.CompatManager;
import dev.xkmc.modulargolems.content.capability.ConfigHeartBeatToServer;
import dev.xkmc.modulargolems.content.capability.ConfigSyncToClient;
import dev.xkmc.modulargolems.content.capability.ConfigUpdateToServer;
import dev.xkmc.modulargolems.content.capability.GolemConfigStorage;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.config.GolemPartConfig;
import dev.xkmc.modulargolems.content.entity.mode.GolemModes;
import dev.xkmc.modulargolems.content.menu.ghost.SetItemFilterToServer;
import dev.xkmc.modulargolems.content.menu.registry.OpenConfigMenuToServer;
import dev.xkmc.modulargolems.content.menu.registry.OpenEquipmentMenuToServer;
import dev.xkmc.modulargolems.events.GolemAttackListener;
import dev.xkmc.modulargolems.events.GolemDispenserBehaviors;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import dev.xkmc.modulargolems.init.data.MGAdvGen;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.data.MGConfigGen;
import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.init.data.RecipeGen;
import dev.xkmc.modulargolems.init.data.SlotGen;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemMiscs;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("modulargolems")
@EventBusSubscriber(modid = "modulargolems", bus = Bus.MOD)
public class ModularGolems {

    public static final String MODID = "modulargolems";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final L2Registrate REGISTRATE = new L2Registrate("modulargolems");

    public static final IEventBus MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(new ResourceLocation("modulargolems", "main"), 1, e -> e.create(ConfigSyncToClient.class, NetworkDirection.PLAY_TO_CLIENT), e -> e.create(ConfigUpdateToServer.class, NetworkDirection.PLAY_TO_SERVER), e -> e.create(ConfigHeartBeatToServer.class, NetworkDirection.PLAY_TO_SERVER), e -> e.create(SetItemFilterToServer.class, NetworkDirection.PLAY_TO_SERVER), e -> e.create(OpenConfigMenuToServer.class, NetworkDirection.PLAY_TO_SERVER), e -> e.create(OpenEquipmentMenuToServer.class, NetworkDirection.PLAY_TO_SERVER));

    public static final ConfigTypeEntry<GolemPartConfig> PARTS = new ConfigTypeEntry<>(HANDLER, "parts", GolemPartConfig.class);

    public static final ConfigTypeEntry<GolemMaterialConfig> MATERIALS = new ConfigTypeEntry<>(HANDLER, "materials", GolemMaterialConfig.class);

    private static void registerRegistrates() {
        GolemItems.register();
        GolemTypes.register();
        GolemMiscs.register();
        GolemModifiers.register();
        MGConfig.init();
        GolemTriggers.register();
        GolemModes.register();
        GolemConfigStorage.register();
        CurioCompatRegistry.register();
        REGISTRATE.addDataGenerator(ProviderType.LANG, MGLangData::genLang);
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, MGTagGen::onBlockTagGen);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, MGTagGen::onItemTagGen);
        REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, MGTagGen::onEntityTagGen);
        REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, MGAdvGen::genAdvancements);
        AttackEventHandler.register(3500, new GolemAttackListener());
    }

    public ModularGolems() {
        registerRegistrates();
    }

    @SubscribeEvent
    public static void modifyAttributes(EntityAttributeModificationEvent event) {
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> GolemDispenserBehaviors.registerDispenseBehaviors());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeServer(), new MGConfigGen(event.getGenerator()));
        CompatManager.gatherData(event);
        event.getGenerator().addProvider(event.includeServer(), new SlotGen(event.getGenerator()));
        if (ModList.get().isLoaded("l2complements")) {
            REGISTRATE.addDataGenerator(TagGen.EFF_TAGS, MGTagGen::onEffTagGen);
        }
    }

    @SubscribeEvent
    public static void sendMessage(InterModEnqueueEvent event) {
    }
}