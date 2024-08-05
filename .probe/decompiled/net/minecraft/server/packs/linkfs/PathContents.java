package net.minecraft.server.packs.linkfs;

import java.nio.file.Path;
import java.util.Map;

interface PathContents {

    PathContents MISSING = new PathContents() {

        public String toString() {
            return "empty";
        }
    };

    PathContents RELATIVE = new PathContents() {

        public String toString() {
            return "relative";
        }
    };

    public static record DirectoryContents(Map<String, LinkFSPath> f_243989_) implements PathContents {

        private final Map<String, LinkFSPath> children;

        public DirectoryContents(Map<String, LinkFSPath> f_243989_) {
            this.children = f_243989_;
        }
    }

    public static record FileContents(Path f_244421_) implements PathContents {

        private final Path contents;

        public FileContents(Path f_244421_) {
            this.contents = f_244421_;
        }
    }
}