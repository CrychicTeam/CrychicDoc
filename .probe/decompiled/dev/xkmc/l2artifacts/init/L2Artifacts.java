package dev.xkmc.l2artifacts.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2artifacts.events.ArtifactAttackListener;
import dev.xkmc.l2artifacts.events.ArtifactSel;
import dev.xkmc.l2artifacts.events.ArtifactSlotClickListener;
import dev.xkmc.l2artifacts.init.data.ArtifactConfig;
import dev.xkmc.l2artifacts.init.data.ArtifactTagGen;
import dev.xkmc.l2artifacts.init.data.ConfigGen;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.data.RecipeGen;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactGLMProvider;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactLootGen;
import dev.xkmc.l2artifacts.init.data.slot.SlotGen;
import dev.xkmc.l2artifacts.init.registrate.ArtifactEffects;
import dev.xkmc.l2artifacts.init.registrate.ArtifactMenuRegistry;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.ArtifactRegistrate;
import dev.xkmc.l2artifacts.init.registrate.items.ArtifactItems;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2itemselector.select.SelectionRegistry;
import dev.xkmc.l2library.init.events.EffectSyncEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("l2artifacts")
@EventBusSubscriber(modid = "l2artifacts", bus = Bus.MOD)
public class L2Artifacts {

    public static final String MODID = "l2artifacts";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final ArtifactRegistrate REGISTRATE = new ArtifactRegistrate();

    public static final ArtifactSlotClickListener CLICK = new ArtifactSlotClickListener();

    public L2Artifacts() {
        ArtifactTypeRegistry.register();
        ArtifactItems.register();
        ArtifactMenuRegistry.register();
        ArtifactEffects.register();
        ArtifactConfig.init();
        NetworkManager.register();
        ConfigGen.register();
        REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
        REGISTRATE.addDataGenerator(ProviderType.LOOT, ArtifactLootGen::onLootGen);
        SelectionRegistry.register(-5000, ArtifactSel.INSTANCE);
        AttackEventHandler.register(3000, new ArtifactAttackListener());
    }

    @SubscribeEvent
    public static void commonInit(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            EffectSyncEvents.TRACKED.add((MobEffect) ArtifactEffects.FLESH_OVERGROWTH.get());
            EffectSyncEvents.TRACKED.add((MobEffect) ArtifactEffects.FUNGUS.get());
        });
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if (ModList.get().isLoaded("l2complements")) {
            REGISTRATE.addDataGenerator(TagGen.EFF_TAGS, ArtifactTagGen::onEffectTagGen);
        }
        event.getGenerator().addProvider(event.includeServer(), new ConfigGen(event.getGenerator()));
        event.getGenerator().addProvider(event.includeServer(), new SlotGen(event.getGenerator()));
        event.getGenerator().addProvider(event.includeServer(), new ArtifactGLMProvider(event.getGenerator()));
    }
}