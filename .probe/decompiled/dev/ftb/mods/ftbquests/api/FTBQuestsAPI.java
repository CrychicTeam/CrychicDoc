package dev.ftb.mods.ftbquests.api;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.Internal;

public class FTBQuestsAPI {

    public static final String MOD_ID = "ftbquests";

    public static final String MOD_NAME = "FTB Quests";

    private static FTBQuestsAPI.API instance;

    public static FTBQuestsAPI.API api() {
        return (FTBQuestsAPI.API) Objects.requireNonNull(instance);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation("ftbquests", path);
    }

    @Internal
    public static void _init(FTBQuestsAPI.API instance) {
        if (FTBQuestsAPI.instance != null) {
            throw new IllegalStateException("can't init more than once!");
        } else {
            FTBQuestsAPI.instance = instance;
        }
    }

    public interface API {

        BaseQuestFile getQuestFile(boolean var1);

        void registerFilterAdapter(ItemFilterAdapter var1);
    }
}