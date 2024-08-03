package net.minecraft.data;

import java.nio.file.Path;
import net.minecraft.resources.ResourceLocation;

public class PackOutput {

    private final Path outputFolder;

    public PackOutput(Path path0) {
        this.outputFolder = path0;
    }

    public Path getOutputFolder() {
        return this.outputFolder;
    }

    public Path getOutputFolder(PackOutput.Target packOutputTarget0) {
        return this.getOutputFolder().resolve(packOutputTarget0.directory);
    }

    public PackOutput.PathProvider createPathProvider(PackOutput.Target packOutputTarget0, String string1) {
        return new PackOutput.PathProvider(this, packOutputTarget0, string1);
    }

    public static class PathProvider {

        private final Path root;

        private final String kind;

        PathProvider(PackOutput packOutput0, PackOutput.Target packOutputTarget1, String string2) {
            this.root = packOutput0.getOutputFolder(packOutputTarget1);
            this.kind = string2;
        }

        public Path file(ResourceLocation resourceLocation0, String string1) {
            return this.root.resolve(resourceLocation0.getNamespace()).resolve(this.kind).resolve(resourceLocation0.getPath() + "." + string1);
        }

        public Path json(ResourceLocation resourceLocation0) {
            return this.root.resolve(resourceLocation0.getNamespace()).resolve(this.kind).resolve(resourceLocation0.getPath() + ".json");
        }
    }

    public static enum Target {

        DATA_PACK("data"), RESOURCE_PACK("assets"), REPORTS("reports");

        final String directory;

        private Target(String p_251326_) {
            this.directory = p_251326_;
        }
    }
}