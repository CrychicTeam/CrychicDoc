package dev.xkmc.l2complements.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2complements.content.enchantment.special.SoulBoundPlayerData;
import dev.xkmc.l2complements.content.item.misc.FireChargeItem;
import dev.xkmc.l2complements.events.L2ComplementsClick;
import dev.xkmc.l2complements.events.MaterialDamageListener;
import dev.xkmc.l2complements.init.data.DamageTypeGen;
import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.data.LCConfigGen;
import dev.xkmc.l2complements.init.data.LCDatapackRegistriesGen;
import dev.xkmc.l2complements.init.data.LCSpriteSourceProvider;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2complements.init.data.RecipeGen;
import dev.xkmc.l2complements.init.data.TagGen;
import dev.xkmc.l2complements.init.registrate.LCBlocks;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2complements.init.registrate.LCEntities;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2complements.init.registrate.LCParticle;
import dev.xkmc.l2complements.init.registrate.LCRecipes;
import dev.xkmc.l2complements.network.EmptyRightClickToServer;
import dev.xkmc.l2complements.network.RotateDiggerToServer;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2damagetracker.contents.materials.vanilla.GenItemVanillaType;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2screentracker.click.quickaccess.DefaultQuickAccessActions;
import dev.xkmc.l2screentracker.compat.arclight.AnvilMenuArclight;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("l2complements")
@EventBusSubscriber(modid = "l2complements", bus = Bus.MOD)
public class L2Complements {

    public static final String MODID = "l2complements";

    public static final PacketHandlerWithConfig HANDLER = new PacketHandlerWithConfig(new ResourceLocation("l2complements", "main"), 3, e -> e.create(EmptyRightClickToServer.class, NetworkDirection.PLAY_TO_SERVER), e -> e.create(RotateDiggerToServer.class, NetworkDirection.PLAY_TO_SERVER));

    public static final Logger LOGGER = LogManager.getLogger();

    public static final L2Registrate REGISTRATE = new L2Registrate("l2complements");

    public static final GenItemVanillaType MATS = new GenItemVanillaType("l2complements", REGISTRATE);

    public L2Complements() {
        ForgeMod.enableMilkFluid();
        LCItems.register();
        LCBlocks.register();
        LCEffects.register();
        LCParticle.register();
        LCEnchantments.register();
        LCEntities.register();
        LCRecipes.register();
        LCConfig.init();
        SoulBoundPlayerData.register();
        new L2ComplementsClick(new ResourceLocation("l2complements", "main"));
        AttackEventHandler.register(5000, new MaterialDamageListener());
        REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::addTranslations);
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagGen::onBlockTagGen);
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagGen::onItemTagGen);
        REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, TagGen::onEntityTagGen);
        REGISTRATE.addDataGenerator(TagGen.EFF_TAGS, TagGen::onEffectTagGen);
        REGISTRATE.addDataGenerator(TagGen.ENCH_TAGS, TagGen::onEnchTagGen);
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LCEffects.registerBrewingRecipe();
            DispenserBlock.registerBehavior((ItemLike) LCItems.SOUL_CHARGE.get(), (FireChargeItem) LCItems.SOUL_CHARGE.get().new FireChargeBehavior());
            DispenserBlock.registerBehavior((ItemLike) LCItems.STRONG_CHARGE.get(), (FireChargeItem) LCItems.STRONG_CHARGE.get().new FireChargeBehavior());
            DispenserBlock.registerBehavior((ItemLike) LCItems.BLACK_CHARGE.get(), (FireChargeItem) LCItems.BLACK_CHARGE.get().new FireChargeBehavior());
            DefaultQuickAccessActions.quickAccess(MenuType.ANVIL, LCBlocks.ETERNAL_ANVIL.m_5456_(), AnvilMenuArclight::new, "container.repair");
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void gatherData(GatherDataEvent event) {
        boolean run = event.includeServer();
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> pvd = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        new DamageTypeGen(output, pvd, helper).generate(run, gen);
        gen.addProvider(run, new LCConfigGen(gen));
        gen.addProvider(run, new LCDatapackRegistriesGen(output, pvd));
        gen.addProvider(run, new LCSpriteSourceProvider(output, helper));
    }
}