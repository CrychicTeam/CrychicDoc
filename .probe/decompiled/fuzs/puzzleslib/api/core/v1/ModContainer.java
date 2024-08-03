package fuzs.puzzleslib.api.core.v1;

import com.google.common.collect.ImmutableMap;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface ModContainer {

    String getModId();

    String getDisplayName();

    String getDescription();

    String getVersion();

    Collection<String> getLicenses();

    Collection<String> getAuthors();

    Collection<String> getCredits();

    Map<String, String> getContactTypes();

    Optional<Path> findResource(String... var1);

    Collection<ModContainer> getChildren();

    default Stream<ModContainer> getAllChildren() {
        return Stream.concat(Stream.of(this), this.getChildren().stream().flatMap(ModContainer::getAllChildren));
    }

    @Nullable
    ModContainer getParent();

    @Internal
    static Map<String, ModContainer> toModList(Supplier<Stream<? extends ModContainer>> modContainers) {
        try {
            return (Map<String, ModContainer>) ((Stream) modContainers.get()).sorted(Comparator.comparing(ModContainer::getModId)).collect(ImmutableMap.toImmutableMap(ModContainer::getModId, Function.identity()));
        } catch (Throwable var2) {
            PuzzlesLib.LOGGER.warn("Failed to generate mod list", var2);
            return Collections.emptyMap();
        }
    }
}