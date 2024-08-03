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
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.npc.entity_type.codec.EntityTypeDialog;
import net.zanckor.questapi.mod.common.datapack.EntityTypeDialogJSONListener;
import net.zanckor.questapi.util.GsonManager;

public class LoadDialogList {

    static EntityTypeDialog entityTypeDialog;

    public static void registerNPCDialogList(MinecraftServer server, String modid) throws IOException {
        ResourceManager resourceManager = server.getResourceManager();
        if (CommonMain.entity_type_list == null) {
            FolderManager.createAPIFolder(server.getWorldPath(LevelResource.ROOT).toAbsolutePath());
        }
        resourceManager.listResources("npc/entity_type_list", file -> {
            if (file.getPath().length() > 22) {
                String fileName = file.getPath().substring(21);
                ResourceLocation resourceLocation = new ResourceLocation(modid, file.getPath());
                if (!modid.equals(file.getNamespace())) {
                    return false;
                }
                if (!file.getPath().endsWith(".json")) {
                    throw new RuntimeException("File " + fileName + " in " + file.getPath() + " is not .json");
                }
                read(resourceLocation, server);
                write(entityTypeDialog, modid, fileName);
            }
            return false;
        });
        QuestDialogManager.registerDialogByEntityType();
    }

    public static void registerDatapackDialogList(MinecraftServer server) throws IOException {
        if (CommonMain.entity_type_list == null) {
            FolderManager.createAPIFolder(server.getWorldPath(LevelResource.ROOT).toAbsolutePath());
        }
        for (Entry<String, JsonObject> entry : EntityTypeDialogJSONListener.datapackDialogPerEntityTypeList.entrySet()) {
            FileWriter writer = new FileWriter(String.valueOf(Path.of(CommonMain.entity_type_list + "/" + (String) entry.getKey())));
            writer.write(((JsonObject) entry.getValue()).toString());
            writer.close();
        }
    }

    private static void read(ResourceLocation resourceLocation, MinecraftServer server) {
        try {
            if (!server.getResourceManager().m_213713_(resourceLocation).isEmpty()) {
                InputStream inputStream = ((Resource) server.getResourceManager().m_213713_(resourceLocation).get()).open();
                entityTypeDialog = (EntityTypeDialog) GsonManager.gson.fromJson(new InputStreamReader(inputStream), EntityTypeDialog.class);
            }
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    private static void write(EntityTypeDialog entityTypeDialog, String modid, String fileName) {
        try {
            if (entityTypeDialog != null) {
                File file = new File(CommonMain.entity_type_list.toFile(), modid + "." + fileName);
                GsonManager.writeJson(file, entityTypeDialog);
            }
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }
}