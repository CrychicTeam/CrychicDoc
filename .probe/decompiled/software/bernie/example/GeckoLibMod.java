package software.bernie.example;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.example.registry.BlockEntityRegistry;
import software.bernie.example.registry.BlockRegistry;
import software.bernie.example.registry.EntityRegistry;
import software.bernie.example.registry.ItemRegistry;
import software.bernie.example.registry.SoundRegistry;
import software.bernie.geckolib.GeckoLib;

@EventBusSubscriber
@Mod("geckolib")
public final class GeckoLibMod {

    public static final String DISABLE_EXAMPLES_PROPERTY_KEY = "geckolib.disable_examples";

    public GeckoLibMod() {
        GeckoLib.initialize();
        if (shouldRegisterExamples()) {
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            EntityRegistry.ENTITIES.register(bus);
            ItemRegistry.ITEMS.register(bus);
            ItemRegistry.TABS.register(bus);
            BlockEntityRegistry.TILES.register(bus);
            BlockRegistry.BLOCKS.register(bus);
            SoundRegistry.SOUNDS.register(bus);
        }
    }

    static boolean shouldRegisterExamples() {
        return !FMLEnvironment.production && !Boolean.getBoolean("geckolib.disable_examples");
    }
}