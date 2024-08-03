package net.zanckor.questapi.mod.core.filemanager.dialogquestregistry;

import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.LevelResource;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.mod.common.datapack.QuestJSONListener;
import net.zanckor.questapi.util.GsonManager;

public class QuestRegistry {

    static ServerQuest playerQuest;

    public static void registerQuest(MinecraftServer server, String modid) {
        ResourceManager resourceManager = server.getResourceManager();
        if (CommonMain.serverQuests == null) {
            FolderManager.createAPIFolder(server.getWorldPath(LevelResource.ROOT).toAbsolutePath());
        }
        resourceManager.listResources("quest", file -> {
            if (file.getPath().length() > 7) {
                String fileName = file.getPath().substring(6);
                ResourceLocation resourceLocation = new ResourceLocation(modid, file.getPath());
                if (!modid.equals(file.getNamespace())) {
                    return false;
                }
                if (!file.getPath().endsWith(".json")) {
                    throw new RuntimeException("File " + fileName + " in " + file.getPath() + " is not .json");
                }
                read(resourceLocation, resourceManager);
                write(playerQuest, fileName, modid);
            }
            return false;
        });
    }

    public static void registerDatapackQuest(MinecraftServer server) throws IOException {
        if (CommonMain.serverQuests == null) {
            FolderManager.createAPIFolder(server.getWorldPath(LevelResource.ROOT).toAbsolutePath());
        }
        for (Entry<String, JsonObject> entry : QuestJSONListener.datapackQuestList.entrySet()) {
            FileWriter writer = new FileWriter(String.valueOf(Path.of(CommonMain.serverQuests + "/" + (String) entry.getKey())));
            writer.write(((JsonObject) entry.getValue()).toString());
            writer.close();
        }
    }

    private static void read(ResourceLocation resourceLocation, ResourceManager resourceManager) {
        try {
            if (!resourceManager.m_213713_(resourceLocation).isEmpty()) {
                InputStream inputStream = ((Resource) resourceManager.m_213713_(resourceLocation).get()).open();
                playerQuest = (ServerQuest) GsonManager.gson.fromJson(new InputStreamReader(inputStream), ServerQuest.class);
            }
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    private static void write(ServerQuest serverQuest, String fileName, String identifier) {
        try {
            if (serverQuest != null) {
                File file = new File(CommonMain.serverQuests.toFile(), identifier + "." + fileName);
                serverQuest.setId(identifier + "." + fileName.substring(0, fileName.length() - 5));
                GsonManager.writeJson(file, serverQuest);
            }
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }
}