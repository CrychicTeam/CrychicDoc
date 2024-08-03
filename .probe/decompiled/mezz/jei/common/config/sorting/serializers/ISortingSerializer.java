package mezz.jei.common.config.sorting.serializers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ISortingSerializer<T> {

    List<T> read(Path var1) throws IOException;

    void write(Path var1, List<T> var2) throws IOException;
}