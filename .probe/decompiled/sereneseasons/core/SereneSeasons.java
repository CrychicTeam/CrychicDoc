package sereneseasons.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sereneseasons.handler.season.SeasonColorHandlers;
import sereneseasons.init.ModBlocks;
import sereneseasons.init.ModConfig;
import sereneseasons.init.ModCreativeTab;
import sereneseasons.init.ModGameRules;
import sereneseasons.init.ModHandlers;
import sereneseasons.init.ModItems;
import sereneseasons.init.ModTags;

@Mod("sereneseasons")
public class SereneSeasons {

    public static final String MOD_ID = "sereneseasons";

    public static CommonProxy proxy = (CommonProxy) DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final Logger LOGGER = LogManager.getLogger("sereneseasons");

    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(Registries.BLOCK, "sereneseasons");

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "sereneseasons");

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "sereneseasons");

    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(Registries.ITEM, "sereneseasons");

    public SereneSeasons() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::loadComplete);
        BLOCK_REGISTER.register(bus);
        BLOCK_ENTITY_REGISTER.register(bus);
        CREATIVE_TAB_REGISTER.register(bus);
        ITEM_REGISTER.register(bus);
        ModBlocks.setup();
        ModItems.setup();
        ModHandlers.init();
        ModConfig.init();
        ModGameRules.init();
        ModTags.setup();
        ModCreativeTab.setup();
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            SeasonColorHandlers.setup();
            proxy.registerItemModelsProperties();
        });
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void loadComplete(FMLLoadCompleteEvent event) {
    }
}