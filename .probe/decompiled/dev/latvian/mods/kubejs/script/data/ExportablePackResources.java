package dev.latvian.mods.kubejs.script.data;

import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.server.packs.PackResources;

public interface ExportablePackResources extends PackResources {

    void export(Path var1) throws IOException;
}