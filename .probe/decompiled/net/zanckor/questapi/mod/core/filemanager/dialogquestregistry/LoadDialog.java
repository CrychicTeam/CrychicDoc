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
import net.zanckor.questapi.api.file.dialog.codec.Conversation;
import net.zanckor.questapi.mod.common.datapack.DialogJSONListener;
import net.zanckor.questapi.util.GsonManager;

public class LoadDialog {

    static Conversation dialogTemplate;

    public static void registerDialog(MinecraftServer server, String modid) {
        ResourceManager resourceManager = server.getResourceManager();
        if (CommonMain.serverDialogs == null) {
            FolderManager.createAPIFolder(server.getWorldPath(LevelResource.ROOT).toAbsolutePath());
        }
        resourceManager.listResources("dialog", file -> {
            if (file.getPath().length() > 7) {
                String fileName = file.getPath().substring(7);
                ResourceLocation resourceLocation = new ResourceLocation(modid, file.getPath());
                if (!modid.equals(file.getNamespace())) {
                    return false;
                }
                if (!file.getPath().endsWith(".json")) {
                    throw new RuntimeException("File " + fileName + " in " + file.getPath() + " is not .json");
                }
                read(resourceLocation, server);
                write(dialogTemplate, modid, fileName);
            }
            return false;
        });
    }

    public static void registerDatapackDialog(MinecraftServer server) throws IOException {
        if (CommonMain.serverDialogs == null) {
            FolderManager.createAPIFolder(server.getWorldPath(LevelResource.ROOT).toAbsolutePath());
        }
        for (Entry<String, JsonObject> entry : DialogJSONListener.datapackDialogList.entrySet()) {
            FileWriter writer = new FileWriter(String.valueOf(Path.of(CommonMain.serverDialogs + "/" + (String) entry.getKey())));
            writer.write(((JsonObject) entry.getValue()).toString());
            writer.close();
        }
    }

    private static void read(ResourceLocation resourceLocation, MinecraftServer server) {
        try {
            if (!server.getResourceManager().m_213713_(resourceLocation).isEmpty()) {
                InputStream inputStream = ((Resource) server.getResourceManager().m_213713_(resourceLocation).get()).open();
                dialogTemplate = (Conversation) GsonManager.gson.fromJson(new InputStreamReader(inputStream), Conversation.class);
            }
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    private static void write(Conversation conversation, String identifier, String fileName) {
        try {
            if (conversation != null) {
                File file = new File(CommonMain.serverDialogs.toFile(), identifier + "." + fileName);
                if (conversation.getIdentifier() == null || conversation.getIdentifier().isEmpty()) {
                    conversation.setIdentifier(identifier);
                }
                conversation.setConversationID(identifier + "." + fileName.substring(0, fileName.length() - 5));
                GsonManager.writeJson(file, conversation);
            }
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }
}