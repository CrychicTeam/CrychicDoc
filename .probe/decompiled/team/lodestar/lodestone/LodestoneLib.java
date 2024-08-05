package team.lodestar.lodestone;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.lodestar.lodestone.compability.CuriosCompat;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.data.LodestoneBlockTagDatagen;
import team.lodestar.lodestone.data.LodestoneDamageTypeDatagen;
import team.lodestar.lodestone.data.LodestoneItemTagDatagen;
import team.lodestar.lodestone.data.LodestoneLangDatagen;
import team.lodestar.lodestone.registry.common.LodestoneArgumentTypeRegistry;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;
import team.lodestar.lodestone.registry.common.LodestoneBlockEntityRegistry;
import team.lodestar.lodestone.registry.common.LodestonePaintingRegistry;
import team.lodestar.lodestone.registry.common.LodestoneRecipeSerializerRegistry;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.item.LodestoneItemProperties;

@Mod("lodestone")
public class LodestoneLib {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String LODESTONE = "lodestone";

    public static final RandomSource RANDOM = RandomSource.create();

    public LodestoneLib() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.SPEC);
        LodestoneBlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modBus);
        LodestoneParticleRegistry.PARTICLES.register(modBus);
        LodestoneAttributeRegistry.ATTRIBUTES.register(modBus);
        LodestoneRecipeSerializerRegistry.RECIPE_SERIALIZERS.register(modBus);
        LodestonePaintingRegistry.register(modBus);
        LodestoneArgumentTypeRegistry.register(modBus);
        CuriosCompat.init();
        modBus.addListener(this::gatherData);
        modBus.addListener(LodestoneItemProperties::populateItemGroups);
        modBus.addListener(LodestoneWorldEventTypeRegistry::postRegistryEvent);
    }

    public static ResourceLocation lodestonePath(String path) {
        return new ResourceLocation("lodestone", path);
    }

    public void gatherData(GatherDataEvent event) {
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        LodestoneBlockTagDatagen blockTagDatagen = new LodestoneBlockTagDatagen(packOutput, lookupProvider, existingFileHelper);
        event.getGenerator().addProvider(true, new LodestoneLangDatagen(packOutput));
        event.getGenerator().addProvider(true, blockTagDatagen);
        event.getGenerator().addProvider(true, new LodestoneItemTagDatagen(packOutput, lookupProvider, blockTagDatagen.m_274426_(), existingFileHelper));
        event.getGenerator().addProvider(true, new LodestoneDamageTypeDatagen(packOutput, lookupProvider, existingFileHelper));
    }
}