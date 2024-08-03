package dev.ftb.mods.ftbteams.api;

import dev.ftb.mods.ftbteams.api.client.ClientTeamManager;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class FTBTeamsAPI {

    public static final String MOD_ID = "ftbteams";

    public static final String MOD_NAME = "FTB Teams";

    private static FTBTeamsAPI.API instance;

    public static FTBTeamsAPI.API api() {
        return instance;
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation("ftbteams", path);
    }

    @Internal
    public static void _init(FTBTeamsAPI.API instance) {
        if (FTBTeamsAPI.instance != null) {
            throw new IllegalStateException("can't init more than once!");
        } else {
            FTBTeamsAPI.instance = instance;
        }
    }

    public interface API {

        boolean isManagerLoaded();

        TeamManager getManager();

        boolean isClientManagerLoaded();

        ClientTeamManager getClientManager();

        @Deprecated
        @Nullable
        CustomPartyCreationHandler setCustomPartyCreationHandler(@Nullable CustomPartyCreationHandler var1);

        @Deprecated
        @Nullable
        CustomPartyCreationHandler getCustomPartyCreationHandler();

        void setPartyCreationFromAPIOnly(boolean var1);

        TeamMessage createMessage(UUID var1, Component var2);
    }
}