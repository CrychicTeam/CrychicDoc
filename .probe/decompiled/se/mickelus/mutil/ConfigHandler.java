package se.mickelus.mutil;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.tuple.Pair;

@ParametersAreNonnullByDefault
@EventBusSubscriber(bus = Bus.MOD)
class ConfigHandler {

    public static ConfigHandler.Client client;

    static ForgeConfigSpec clientSpec;

    public static void setup() {
        if (FMLEnvironment.dist.isClient()) {
            setupClient();
            ModLoadingContext.get().registerConfig(Type.CLIENT, clientSpec);
            FMLJavaModLoadingContext.get().getModEventBus().register(client);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void setupClient() {
        Pair<ConfigHandler.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigHandler.Client::new);
        clientSpec = (ForgeConfigSpec) specPair.getRight();
        client = (ConfigHandler.Client) specPair.getLeft();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Client {

        public ForgeConfigSpec.BooleanValue queryPerks;

        Client(ForgeConfigSpec.Builder builder) {
            this.queryPerks = builder.comment("Controls if perks data should be queried on startup").define("query_perks", true);
        }
    }
}