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

public class CompoundTagDialogJSONListener extends SimpleJsonResourceReloadListener {

    public static HashMap<String, JsonObject> datapackDialogPerCompoundTagList = new HashMap();

    public CompoundTagDialogJSONListener(Gson gson, String name) {
        super(gson, name);
    }

    public static void register(AddReloadListenerEvent e) {
        e.addListener(new CompoundTagDialogJSONListener(GsonManager.gson, "questapi/npc/compound_tag_list"));
    }

    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        CommonMain.Constants.LOG.info("Loaded list of dialogs loaded via entity type datapack");
        jsonElementMap.forEach((rl, jsonElement) -> {
            JsonObject obj = jsonElement.getAsJsonObject();
            if (obj.get("id") != null && obj.get("entity_type") != null) {
                String questId = "." + obj.get("id").toString().substring(1, obj.get("id").toString().length() - 1);
                Path path = Path.of(rl.getNamespace() + questId + ".json");
                datapackDialogPerCompoundTagList.put(path.toString(), obj);
            }
        });
    }
}