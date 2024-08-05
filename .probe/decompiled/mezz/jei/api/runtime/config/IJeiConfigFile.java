package mezz.jei.api.runtime.config;

import java.nio.file.Path;
import java.util.List;
import org.jetbrains.annotations.Unmodifiable;

public interface IJeiConfigFile {

    Path getPath();

    @Unmodifiable
    List<? extends IJeiConfigCategory> getCategories();
}