package net.minecraft.world.level.validation;

import java.nio.file.Path;

public record ForbiddenSymlinkInfo(Path f_289826_, Path f_289840_) {

    private final Path link;

    private final Path target;

    public ForbiddenSymlinkInfo(Path f_289826_, Path f_289840_) {
        this.link = f_289826_;
        this.target = f_289840_;
    }
}