package dev.xkmc.l2weaponry.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2complements.events.ItemUseEventHandler;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2library.serial.config.PacketHandler;
import dev.xkmc.l2weaponry.compat.GolemCompat;
import dev.xkmc.l2weaponry.compat.XEnchCompat;
import dev.xkmc.l2weaponry.content.capability.LWPlayerData;
import dev.xkmc.l2weaponry.events.LWAttackEventListener;
import dev.xkmc.l2weaponry.events.LWClickListener;
import dev.xkmc.l2weaponry.init.data.LWAttributeConfigGen;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.data.LWDamageTypeGen;
import dev.xkmc.l2weaponry.init.data.LangData;
import dev.xkmc.l2weaponry.init.data.RecipeGen;
import dev.xkmc.l2weaponry.init.data.TagGen;
import dev.xkmc.l2weaponry.init.registrate.LWEnchantments;
import dev.xkmc.l2weaponry.init.registrate.LWEntities;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import dev.xkmc.l2weaponry.init.registrate.LWRegistrate;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
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

@Mod("l2weaponry")
@EventBusSubscriber(modid = "l2weaponry", bus = Bus.MOD)
public class L2Weaponry {

    public static final String MODID = "l2weaponry";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final LWRegistrate REGISTRATE = new LWRegistrate("l2weaponry");

    public static final PacketHandler HANDLER = new PacketHandler(new ResourceLocation("l2weaponry", "main"), 1);

    public L2Weaponry() {
        FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
        IEventBus bus = ctx.getModEventBus();
        LWItems.register();
        LWEntities.register();
        LWDamageTypeGen.register();
        LWConfig.init();
        LWPlayerData.register();
        LWEnchantments.register();
        ItemUseEventHandler.LIST.add(new LWClickListener());
        if (ModList.get().isLoaded("modulargolems")) {
            GolemCompat.register(bus);
        }
        REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::addTranslations);
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagGen::onBlockTagGen);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagGen::onItemTagGen);
        REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, TagGen::onEntityTagGen);
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            AttackEventHandler.register(4000, new LWAttackEventListener());
            if (ModList.get().isLoaded("x_enchantment")) {
                XEnchCompat.onInit();
            }
        });
    }

    @SubscribeEvent
    public static void modifyAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, (Attribute) LWItems.SHIELD_DEFENSE.get());
        event.add(EntityType.PLAYER, (Attribute) LWItems.REFLECT_TIME.get());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        boolean gen = event.includeServer();
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        new LWDamageTypeGen(output, lookup, helper).generate(gen, event.getGenerator());
        event.getGenerator().addProvider(event.includeServer(), new LWAttributeConfigGen(event.getGenerator()));
    }
}