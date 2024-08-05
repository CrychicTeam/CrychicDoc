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
import net.zanckor.questapi.api.file.npc.entity_type_tag.codec.EntityTypeTagDialog;
import net.zanckor.questapi.mod.common.datapack.CompoundTagDialogJSONListener;
import net.zanckor.questapi.util.GsonManager;

public class LoadTagDialogList {

    static EntityTypeTagDialog entityTypeTagDialog;

    public static void registerNPCTagDialogList(MinecraftServer server, String modid) {
        ResourceManager resourceManager = server.getResourceManager();
        if (CommonMain.compoundTag_List == null) {
            FolderManager.createAPIFolder(server.getWorldPath(LevelResource.ROOT).toAbsolutePath());
        }
        resourceManager.listResources("npc/compound_tag_list", file -> {
            if (file.getPath().length() > 22) {
                String fileName = file.getPath().substring(22);
                ResourceLocation resourceLocation = new ResourceLocation(modid, file.getPath());
                if (!modid.equals(file.getNamespace())) {
                    return false;
                }
                if (!file.getPath().endsWith(".json")) {
                    throw new RuntimeException("File " + fileName + " in " + file.getPath() + " is not .json");
                }
                read(resourceLocation, server);
                write(entityTypeTagDialog, modid, fileName);
            }
            return false;
        });
        QuestDialogManager.registerDialogByCompoundTag();
    }

    public static void registerDatapackTagDialogList(MinecraftServer server) throws IOException {
        if (CommonMain.compoundTag_List == null) {
            FolderManager.createAPIFolder(server.getWorldPath(LevelResource.ROOT).toAbsolutePath());
        }
        for (Entry<String, JsonObject> entry : CompoundTagDialogJSONListener.datapackDialogPerCompoundTagList.entrySet()) {
            FileWriter writer = new FileWriter(String.valueOf(Path.of(CommonMain.compoundTag_List + "/" + (String) entry.getKey())));
            writer.write(((JsonObject) entry.getValue()).toString());
            writer.close();
        }
    }

    private static void read(ResourceLocation resourceLocation, MinecraftServer server) {
        try {
            if (!server.getResourceManager().m_213713_(resourceLocation).isEmpty()) {
                InputStream inputStream = ((Resource) server.getResourceManager().m_213713_(resourceLocation).get()).open();
                entityTypeTagDialog = (EntityTypeTagDialog) GsonManager.gson.fromJson(new InputStreamReader(inputStream), EntityTypeTagDialog.class);
            }
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    private static void write(EntityTypeTagDialog entityTypeDialog, String modid, String fileName) {
        try {
            if (entityTypeDialog != null) {
                File file = new File(CommonMain.compoundTag_List.toFile(), modid + "." + fileName);
                GsonManager.writeJson(file, entityTypeDialog);
            }
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }
}