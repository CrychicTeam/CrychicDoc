package com.simibubi.create.content.schematics;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.simibubi.create.foundation.utility.Lang;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.loading.FMLPaths;

public class SchematicExport {

    public static final Path SCHEMATICS = FMLPaths.GAMEDIR.get().resolve("schematics");

    @Nullable
    public static SchematicExport.SchematicExportResult saveSchematic(Path dir, String fileName, boolean overwrite, Level level, BlockPos first, BlockPos second) {
        BoundingBox bb = BoundingBox.fromCorners(first, second);
        BlockPos origin = new BlockPos(bb.minX(), bb.minY(), bb.minZ());
        BlockPos bounds = new BlockPos(bb.getXSpan(), bb.getYSpan(), bb.getZSpan());
        StructureTemplate structure = new StructureTemplate();
        structure.fillFromWorld(level, origin, bounds, true, Blocks.AIR);
        CompoundTag data = structure.save(new CompoundTag());
        SchematicAndQuillItem.replaceStructureVoidWithAir(data);
        SchematicAndQuillItem.clampGlueBoxes(level, new AABB(origin, origin.offset(bounds)), data);
        if (fileName.isEmpty()) {
            fileName = Lang.translateDirect("schematicAndQuill.fallbackName").getString();
        }
        if (!overwrite) {
            fileName = FilesHelper.findFirstValidFilename(fileName, dir, "nbt");
        }
        if (!fileName.endsWith(".nbt")) {
            fileName = fileName + ".nbt";
        }
        Path file = dir.resolve(fileName).toAbsolutePath();
        try {
            Files.createDirectories(dir);
            boolean overwritten = Files.deleteIfExists(file);
            OutputStream out = Files.newOutputStream(file, StandardOpenOption.CREATE);
            try {
                NbtIo.writeCompressed(data, out);
            } catch (Throwable var17) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Throwable var16) {
                        var17.addSuppressed(var16);
                    }
                }
                throw var17;
            }
            if (out != null) {
                out.close();
            }
            return new SchematicExport.SchematicExportResult(file, dir, fileName, overwritten, origin, bounds);
        } catch (IOException var18) {
            Create.LOGGER.error("An error occurred while saving schematic [" + fileName + "]", var18);
            return null;
        }
    }

    public static record SchematicExportResult(Path file, Path dir, String fileName, boolean overwritten, BlockPos origin, BlockPos bounds) {
    }
}