package net.minecraft.world.level.validation;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class DirectoryValidator {

    private final PathAllowList symlinkTargetAllowList;

    public DirectoryValidator(PathAllowList pathAllowList0) {
        this.symlinkTargetAllowList = pathAllowList0;
    }

    public void validateSymlink(Path path0, List<ForbiddenSymlinkInfo> listForbiddenSymlinkInfo1) throws IOException {
        Path $$2 = Files.readSymbolicLink(path0);
        if (!this.symlinkTargetAllowList.matches($$2)) {
            listForbiddenSymlinkInfo1.add(new ForbiddenSymlinkInfo(path0, $$2));
        }
    }

    public List<ForbiddenSymlinkInfo> validateSave(Path path0, boolean boolean1) throws IOException {
        final List<ForbiddenSymlinkInfo> $$2 = new ArrayList();
        BasicFileAttributes $$3;
        try {
            $$3 = Files.readAttributes(path0, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        } catch (NoSuchFileException var6) {
            return $$2;
        }
        if (!$$3.isRegularFile() && !$$3.isOther()) {
            if ($$3.isSymbolicLink()) {
                if (!boolean1) {
                    this.validateSymlink(path0, $$2);
                    return $$2;
                }
                path0 = Files.readSymbolicLink(path0);
            }
            Files.walkFileTree(path0, new SimpleFileVisitor<Path>() {

                private void validateSymlink(Path p_289935_, BasicFileAttributes p_289941_) throws IOException {
                    if (p_289941_.isSymbolicLink()) {
                        DirectoryValidator.this.validateSymlink(p_289935_, $$2);
                    }
                }

                public FileVisitResult preVisitDirectory(Path p_289946_, BasicFileAttributes p_289950_) throws IOException {
                    this.validateSymlink(p_289946_, p_289950_);
                    return super.preVisitDirectory(p_289946_, p_289950_);
                }

                public FileVisitResult visitFile(Path p_289986_, BasicFileAttributes p_289991_) throws IOException {
                    this.validateSymlink(p_289986_, p_289991_);
                    return super.visitFile(p_289986_, p_289991_);
                }
            });
            return $$2;
        } else {
            throw new IOException("Path " + path0 + " is not a directory");
        }
    }
}