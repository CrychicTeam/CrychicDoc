package journeymap.common.config.forge;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import journeymap.common.Journeymap;
import journeymap.common.config.AdminConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = "journeymap", bus = Bus.MOD)
public class ForgeConfig implements AdminConfig {

    public static final ForgeConfig.Server SERVER;

    public static final ForgeConfigSpec SERVER_SPEC;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> serverAdmins;

    public static ForgeConfigSpec.ConfigValue<Boolean> opAccess;

    @Override
    public boolean getOpAccess() {
        return opAccess.get();
    }

    @Override
    public List<String> getAdmins() {
        return new ArrayList((Collection) serverAdmins.get());
    }

    @Override
    public void load() {
        Journeymap.getLogger().info("Loading JourneyMap Forge Configs");
        if (serverAdmins == null) {
            if (SERVER.serverAdmins.get().contains("12341234132")) {
                SERVER.serverAdmins.get().remove("12341234132");
                SERVER.serverAdmins.save();
            }
            serverAdmins = SERVER.serverAdmins;
        }
        if (opAccess == null) {
            opAccess = SERVER.opAccess;
        }
    }

    static {
        Pair<ForgeConfig.Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ForgeConfig.Server::new);
        SERVER_SPEC = (ForgeConfigSpec) specPair.getRight();
        SERVER = (ForgeConfig.Server) specPair.getLeft();
    }

    public static class Server {

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> serverAdmins;

        public final ForgeConfigSpec.ConfigValue<Boolean> opAccess;

        Server(ForgeConfigSpec.Builder builder) {
            builder.push("admins");
            this.opAccess = builder.comment("Default, all Ops have access to Server Admin UI in the Options screen.", "If set to false, only users in the Admin List will have access.", "If set to true, all ops and users in the Admin List will have access.").define("opAccess", true);
            this.serverAdmins = builder.comment("Players in this list have access to the Journeymap's Server Admin Panel", "Add users by name or UUID, Prefer UUID as it is more secure!", "Each value on a new line with the example format provided. (please delete the default values)").defineList("serverAdmins", Lists.newArrayList(new String[] { "mysticdrew", "79f597fe-2877-4ecb-acdf-8c58cc1854ca" }), e -> e instanceof String);
            builder.pop();
        }
    }
}