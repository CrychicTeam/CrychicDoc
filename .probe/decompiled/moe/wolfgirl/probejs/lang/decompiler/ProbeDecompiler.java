package moe.wolfgirl.probejs.lang.decompiler;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.decompiler.remapper.ProbeRemapper;
import net.minecraftforge.fml.ModList;
import org.jetbrains.java.decompiler.main.Fernflower;

public class ProbeDecompiler {

    public final Fernflower engine;

    public final ProbeFileSaver resultSaver = new ProbeFileSaver();

    public static List<File> findModFiles() {
        ModList modList = ModList.get();
        return (List<File>) modList.getModFiles().stream().map(fileInfo -> fileInfo.getFile().getFilePath()).map(Path::toFile).collect(Collectors.toList());
    }

    public ProbeDecompiler() {
        this.engine = new Fernflower(this.resultSaver, Map.of("rename-members", "1", "user-renamer-class", ProbeRemapper.class.getName()), new ProbeDecompilerLogger());
    }

    public void addSource(File source) {
        this.engine.addSource(source);
    }

    public void fromMods() {
        for (File modFile : findModFiles()) {
            this.addSource(modFile);
        }
    }

    public void decompileContext() {
        this.resultSaver.classCount = 0;
        try {
            this.engine.decompileContext();
        } finally {
            this.engine.clearContext();
        }
    }
}