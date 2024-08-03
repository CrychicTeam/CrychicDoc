package net.zanckor.questapi.mod.common.datapack;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.util.GsonManager;
import org.jetbrains.annotations.NotNull;

public class DialogJSONListener extends SimpleJsonResourceReloadListener {

    public static HashMap<String, JsonObject> datapackDialogList = new HashMap();

    public DialogJSONListener(Gson gson, String name) {
        super(gson, name);
    }

    public static void register(AddReloadListenerEvent e) {
        e.addListener(new DialogJSONListener(GsonManager.gson, "questapi/dialog"));
    }

    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        CommonMain.Constants.LOG.info("Loaded dialog datapack");
        jsonElementMap.forEach((rl, jsonElement) -> {
            JsonObject obj = jsonElement.getAsJsonObject();
            if (obj.get("dialog") != null) {
                String dialogID = "." + rl.getPath();
                Path path = Path.of(rl.getNamespace() + dialogID + ".json");
                datapackDialogList.put(path.toString(), obj);
            }
        });
    }
}