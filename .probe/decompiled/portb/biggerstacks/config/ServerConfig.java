package portb.biggerstacks.config;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ServerConfig {

    public static final ServerConfig SERVER_INSTANCE = new ServerConfig(true);

    public static final ServerConfig LOCAL_INSTANCE = new ServerConfig(false);

    public final ForgeConfigSpec SPEC;

    public final ForgeConfigSpec.BooleanValue increaseTransferRate;

    ServerConfig(boolean isOnlyForDedicatedServer) {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        if (isOnlyForDedicatedServer && FMLEnvironment.dist.isClient()) {
            builder.comment("IGNORE THIS CONFIG FILE!!!!", "IGNORE THIS CONFIG FILE!!!!", "IGNORE THIS CONFIG FILE!!!!", "IT IS ONLY USED WHEN HOSTING ON LAN");
        }
        builder.push("biggerstacks");
        this.increaseTransferRate = builder.comment("Whether to increase max transfer rate of some mods to the new stack limit/t.", "E.g. if max stack limit is 1000, it will become 1000 items per tick (where applicable).", "How this is done will vary for each mod", "- Modular routers will require more stack upgrades", "- Pipez does not need this option, it has a config for transfer rate, which you can set to anything", "- Mekanism also has its own config value, though the logistical sorter has its extract rate increased", "- Pretty pipes has its extract rate scaled up", "- XNet can already extract a variable amount, but you will be able to go past 64 to the new maximum stack limit", "- Cyclic still extracts 1 stack (more than 64 items) per tick, but the size of the stack is adjusted").define("Increase transfer rate", true);
        builder.pop();
        this.SPEC = builder.build();
    }

    public static ServerConfig get() {
        return !FMLEnvironment.dist.isDedicatedServer() && Minecraft.getInstance().hasSingleplayerServer() ? LOCAL_INSTANCE : SERVER_INSTANCE;
    }
}