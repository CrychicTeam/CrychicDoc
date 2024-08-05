package dev.xkmc.l2archery.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2archery.compat.GolemCompat;
import dev.xkmc.l2archery.compat.JeedHelper;
import dev.xkmc.l2archery.content.config.BowArrowStatConfig;
import dev.xkmc.l2archery.events.ArrowAttackListener;
import dev.xkmc.l2archery.init.data.AdvGen;
import dev.xkmc.l2archery.init.data.ArcheryConfig;
import dev.xkmc.l2archery.init.data.ArcheryConfigGen;
import dev.xkmc.l2archery.init.data.ArcheryDamageMultiplex;
import dev.xkmc.l2archery.init.data.ArcheryTagGen;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2archery.init.data.RecipeGen;
import dev.xkmc.l2archery.init.registrate.ArcheryEffects;
import dev.xkmc.l2archery.init.registrate.ArcheryEnchantments;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.ConfigTypeEntry;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("l2archery")
@EventBusSubscriber(modid = "l2archery", bus = Bus.MOD)
public class L2Archery {

    public static final String MODID = "l2archery";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final L2Registrate REGISTRATE = new L2Registrate("l2archery");

    public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(new ResourceLocation("l2archery", "main"), 2);

    public static final ConfigTypeEntry<BowArrowStatConfig> STATS = new ConfigTypeEntry<>(HANDLER, "stats", BowArrowStatConfig.class);

    private static void registerRegistrates(IEventBus bus) {
        ArcheryRegister.register();
        ArcheryItems.register();
        ArcheryEffects.register();
        ArcheryEnchantments.register();
        ArcheryDamageMultiplex.register();
        AttackEventHandler.register(2000, new ArrowAttackListener());
        ArcheryConfig.init();
        if (ModList.get().isLoaded("modulargolems")) {
            GolemCompat.register();
        }
        if (ModList.get().isLoaded("jeed")) {
            JeedHelper.register();
        }
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
        REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
        REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, AdvGen::genAdvancements);
        REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, ArcheryTagGen::onEntityTagGen);
        REGISTRATE.addDataGenerator(TagGen.EFF_TAGS, ArcheryTagGen::onEffectTagGen);
    }

    private static void registerForgeEvents() {
    }

    public L2Archery() {
        FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
        IEventBus bus = ctx.getModEventBus();
        registerRegistrates(bus);
        registerForgeEvents();
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> ArcheryEffects.registerBrewingRecipe());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        boolean gen = event.includeServer();
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        event.getGenerator().addProvider(gen, new ArcheryConfigGen(event.getGenerator()));
        new ArcheryDamageMultiplex(output, lookup, helper).generate(gen, event.getGenerator());
    }

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
    }
}