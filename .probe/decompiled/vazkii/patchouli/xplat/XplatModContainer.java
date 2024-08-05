package vazkii.patchouli.xplat;

import java.nio.file.Path;
import java.util.List;

public interface XplatModContainer {

    String getId();

    String getName();

    Path getPath(String var1);

    List<Path> getRootPaths();
}