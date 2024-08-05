package mezz.jei.api.runtime.config;

import java.util.Collection;
import org.jetbrains.annotations.Unmodifiable;

public interface IJeiConfigCategory {

    String getName();

    @Unmodifiable
    Collection<? extends IJeiConfigValue<?>> getConfigValues();
}