package dev.xkmc.l2backpack.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2backpack.LCCompat;
import dev.xkmc.l2backpack.compat.GolemCompat;
import dev.xkmc.l2backpack.compat.PatchouliCompat;
import dev.xkmc.l2backpack.content.capability.PickupModeCap;
import dev.xkmc.l2backpack.content.remote.common.WorldStorage;
import dev.xkmc.l2backpack.content.remote.player.EnderSyncCap;
import dev.xkmc.l2backpack.content.remote.player.EnderSyncPacket;
import dev.xkmc.l2backpack.events.BackpackSel;
import dev.xkmc.l2backpack.events.BackpackSlotClickListener;
import dev.xkmc.l2backpack.events.PatchouliClickListener;
import dev.xkmc.l2backpack.init.advancement.BackpackTriggers;
import dev.xkmc.l2backpack.init.data.AdvGen;
import dev.xkmc.l2backpack.init.data.BackpackConfig;
import dev.xkmc.l2backpack.init.data.LangData;
import dev.xkmc.l2backpack.init.data.RecipeGen;
import dev.xkmc.l2backpack.init.data.SlotGen;
import dev.xkmc.l2backpack.init.data.TagGen;
import dev.xkmc.l2backpack.init.loot.LootGen;
import dev.xkmc.l2backpack.init.registrate.BackpackBlocks;
import dev.xkmc.l2backpack.init.registrate.BackpackItems;
import dev.xkmc.l2backpack.init.registrate.BackpackMenus;
import dev.xkmc.l2backpack.init.registrate.BackpackMisc;
import dev.xkmc.l2backpack.network.CreativeSetCarryToClient;
import dev.xkmc.l2backpack.network.CreativeSetCarryToServer;
import dev.xkmc.l2backpack.network.DrawerInteractToServer;
import dev.xkmc.l2backpack.network.RequestTooltipUpdateEvent;
import dev.xkmc.l2backpack.network.RespondTooltipUpdateEvent;
import dev.xkmc.l2itemselector.select.SelectionRegistry;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandler;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("l2backpack")
@EventBusSubscriber(modid = "l2backpack", bus = Bus.MOD)
public class L2Backpack {

    public static final String MODID = "l2backpack";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final L2Registrate REGISTRATE = new L2Registrate("l2backpack");

    public static final BackpackSlotClickListener SLOT_CLICK = new BackpackSlotClickListener();

    public static final PacketHandler HANDLER = new PacketHandler(new ResourceLocation("l2backpack", "main"), 3, e -> e.create(DrawerInteractToServer.class, NetworkDirection.PLAY_TO_SERVER), e -> e.create(CreativeSetCarryToClient.class, NetworkDirection.PLAY_TO_CLIENT), e -> e.create(CreativeSetCarryToServer.class, NetworkDirection.PLAY_TO_SERVER), e -> e.create(RequestTooltipUpdateEvent.class, NetworkDirection.PLAY_TO_SERVER), e -> e.create(RespondTooltipUpdateEvent.class, NetworkDirection.PLAY_TO_CLIENT), e -> e.create(EnderSyncPacket.class, NetworkDirection.PLAY_TO_CLIENT));

    private static void registerRegistrates(IEventBus bus) {
        ForgeMod.enableMilkFluid();
        BackpackBlocks.register();
        BackpackItems.register();
        BackpackMenus.register();
        BackpackMisc.register(bus);
        Handlers.register();
        BackpackTriggers.register();
        BackpackConfig.init();
        PickupModeCap.register();
        EnderSyncCap.register();
        if (ModList.get().isLoaded("modulargolems")) {
            GolemCompat.register();
        }
        if (ModList.get().isLoaded("l2complements")) {
            MinecraftForge.EVENT_BUS.register(LCCompat.class);
        }
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
        REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, AdvGen::genAdvancements);
        REGISTRATE.addDataGenerator(ProviderType.LOOT, LootGen::genLoot);
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagGen::onBlockTagGen);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagGen::onItemTagGen);
        if (ModList.get().isLoaded("patchouli")) {
            PatchouliCompat.gen();
            new PatchouliClickListener();
            MinecraftForge.EVENT_BUS.register(PatchouliClickListener.class);
        }
    }

    public L2Backpack() {
        FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
        IEventBus bus = ctx.getModEventBus();
        registerRegistrates(bus);
        SelectionRegistry.register(-1000, BackpackSel.INSTANCE);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(BackpackMisc::commonSetup);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        LangData.addTranslations(REGISTRATE::addRawLang);
        DataGenerator gen = event.getGenerator();
        boolean server = event.includeServer();
        gen.addProvider(server, new SlotGen(gen));
    }

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(WorldStorage.class);
    }
}