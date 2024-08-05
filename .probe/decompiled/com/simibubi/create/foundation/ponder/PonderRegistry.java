package com.simibubi.create.foundation.ponder;

import com.simibubi.create.Create;
import com.simibubi.create.infrastructure.ponder.PonderIndex;
import com.simibubi.create.infrastructure.ponder.SharedText;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class PonderRegistry {

    public static final PonderTagRegistry TAGS = new PonderTagRegistry();

    public static final PonderChapterRegistry CHAPTERS = new PonderChapterRegistry();

    public static final Map<ResourceLocation, List<PonderStoryBoardEntry>> ALL = new HashMap();

    public static void addStoryBoard(PonderStoryBoardEntry entry) {
        synchronized (ALL) {
            List<PonderStoryBoardEntry> list = (List<PonderStoryBoardEntry>) ALL.computeIfAbsent(entry.getComponent(), $ -> new ArrayList());
            synchronized (list) {
                list.add(entry);
            }
        }
    }

    public static List<PonderScene> compile(ResourceLocation id) {
        List<PonderStoryBoardEntry> list = (List<PonderStoryBoardEntry>) ALL.get(id);
        return list == null ? Collections.emptyList() : compile(list);
    }

    public static List<PonderScene> compile(PonderChapter chapter) {
        List<PonderStoryBoardEntry> list = CHAPTERS.getStories(chapter);
        return list == null ? Collections.emptyList() : compile(list);
    }

    public static List<PonderScene> compile(List<PonderStoryBoardEntry> entries) {
        if (PonderIndex.editingModeActive()) {
            PonderLocalization.SHARED.clear();
            SharedText.gatherText();
        }
        List<PonderScene> scenes = new ArrayList();
        for (int i = 0; i < entries.size(); i++) {
            PonderStoryBoardEntry sb = (PonderStoryBoardEntry) entries.get(i);
            StructureTemplate activeTemplate = loadSchematic(sb.getSchematicLocation());
            PonderWorld world = new PonderWorld(BlockPos.ZERO, Minecraft.getInstance().level);
            activeTemplate.placeInWorld(world, BlockPos.ZERO, BlockPos.ZERO, new StructurePlaceSettings(), world.f_46441_, 2);
            world.createBackup();
            PonderScene scene = compileScene(i, sb, world);
            scene.begin();
            scenes.add(scene);
        }
        return scenes;
    }

    public static PonderScene compileScene(int i, PonderStoryBoardEntry sb, PonderWorld world) {
        PonderScene scene = new PonderScene(world, sb.getNamespace(), sb.getComponent(), sb.getTags());
        SceneBuilder builder = scene.builder();
        sb.getBoard().program(builder, scene.getSceneBuildingUtil());
        return scene;
    }

    public static StructureTemplate loadSchematic(ResourceLocation location) {
        return loadSchematic(Minecraft.getInstance().getResourceManager(), location);
    }

    public static StructureTemplate loadSchematic(ResourceManager resourceManager, ResourceLocation location) {
        String namespace = location.getNamespace();
        String path = "ponder/" + location.getPath() + ".nbt";
        ResourceLocation location1 = new ResourceLocation(namespace, path);
        Optional<Resource> optionalResource = resourceManager.m_213713_(location1);
        if (optionalResource.isPresent()) {
            Resource resource = (Resource) optionalResource.get();
            try {
                InputStream inputStream = resource.open();
                StructureTemplate var8;
                try {
                    var8 = loadSchematic(inputStream);
                } catch (Throwable var11) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }
                    throw var11;
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                return var8;
            } catch (IOException var12) {
                Create.LOGGER.error("Failed to read ponder schematic: " + location1, var12);
            }
        } else {
            Create.LOGGER.error("Ponder schematic missing: " + location1);
        }
        return new StructureTemplate();
    }

    public static StructureTemplate loadSchematic(InputStream resourceStream) throws IOException {
        StructureTemplate t = new StructureTemplate();
        DataInputStream stream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(resourceStream)));
        CompoundTag nbt = NbtIo.read(stream, new NbtAccounter(536870912L));
        t.load(Minecraft.getInstance().level.m_246945_(Registries.BLOCK), nbt);
        return t;
    }
}