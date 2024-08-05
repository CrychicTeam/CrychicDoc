package org.embeddedt.modernfix;

import com.google.common.cache.CacheLoader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;

public class FileWalker extends CacheLoader<Pair<Path, Integer>, List<Path>> {

    public static final FileWalker INSTANCE = new FileWalker();

    public List<Path> load(Pair<Path, Integer> key) throws Exception {
        Stream<Path> stream = Files.walk((Path) key.getLeft(), (Integer) key.getRight(), new FileVisitOption[0]);
        List var3;
        try {
            var3 = (List) stream.collect(Collectors.toList());
        } catch (Throwable var6) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }
            throw var6;
        }
        if (stream != null) {
            stream.close();
        }
        return var3;
    }
}