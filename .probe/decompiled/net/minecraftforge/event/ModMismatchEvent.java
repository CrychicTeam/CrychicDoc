package net.minecraftforge.event;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ModMismatchEvent extends Event implements IModBusEvent {

    private final LevelStorageSource.LevelDirectory levelDirectory;

    private final HashMap<String, ModMismatchEvent.MismatchedVersionInfo> versionDifferences;

    private final HashMap<String, ModContainer> resolved;

    @Internal
    public ModMismatchEvent(LevelStorageSource.LevelDirectory levelDirectory, Map<String, ArtifactVersion> previousVersions, Map<String, ArtifactVersion> missingVersions) {
        this.levelDirectory = levelDirectory;
        this.resolved = new HashMap(previousVersions.size());
        this.versionDifferences = new HashMap();
        previousVersions.forEach((modId, version) -> this.versionDifferences.put(modId, new ModMismatchEvent.MismatchedVersionInfo(version, (ArtifactVersion) ModList.get().getModContainerById(modId).map(ModContainer::getModInfo).map(IModInfo::getVersion).orElse(null))));
        missingVersions.forEach((modId, version) -> this.versionDifferences.put(modId, new ModMismatchEvent.MismatchedVersionInfo(version, null)));
    }

    public LevelStorageSource.LevelDirectory getLevelDirectory() {
        return this.levelDirectory;
    }

    @Nullable
    public ArtifactVersion getPreviousVersion(String modId) {
        return this.versionDifferences.containsKey(modId) ? ((ModMismatchEvent.MismatchedVersionInfo) this.versionDifferences.get(modId)).oldVersion() : null;
    }

    @Nullable
    public ArtifactVersion getCurrentVersion(String modid) {
        return this.versionDifferences.containsKey(modid) ? ((ModMismatchEvent.MismatchedVersionInfo) this.versionDifferences.get(modid)).newVersion() : null;
    }

    public void markResolved(String modId) {
        ModContainer resolvedBy = ModLoadingContext.get().getActiveContainer();
        this.resolved.putIfAbsent(modId, resolvedBy);
    }

    public boolean wasResolved(String modId) {
        return this.resolved.containsKey(modId);
    }

    public Optional<ModMismatchEvent.MismatchedVersionInfo> getVersionDifference(String modid) {
        return Optional.ofNullable((ModMismatchEvent.MismatchedVersionInfo) this.versionDifferences.get(modid));
    }

    public Optional<ModContainer> getResolver(String modid) {
        return Optional.ofNullable((ModContainer) this.resolved.get(modid));
    }

    public boolean anyUnresolved() {
        return this.resolved.size() < this.versionDifferences.size();
    }

    public Stream<ModMismatchEvent.MismatchResolutionResult> getUnresolved() {
        return this.versionDifferences.keySet().stream().filter(modid -> !this.resolved.containsKey(modid)).map(unresolved -> new ModMismatchEvent.MismatchResolutionResult(unresolved, (ModMismatchEvent.MismatchedVersionInfo) this.versionDifferences.get(unresolved), null)).sorted(Comparator.comparing(ModMismatchEvent.MismatchResolutionResult::modid));
    }

    public boolean anyResolved() {
        return !this.resolved.isEmpty();
    }

    public Stream<ModMismatchEvent.MismatchResolutionResult> getResolved() {
        return this.resolved.keySet().stream().map(modid -> new ModMismatchEvent.MismatchResolutionResult(modid, (ModMismatchEvent.MismatchedVersionInfo) this.versionDifferences.get(modid), (ModContainer) this.resolved.get(modid))).sorted(Comparator.comparing(ModMismatchEvent.MismatchResolutionResult::modid));
    }

    public static record MismatchResolutionResult(String modid, ModMismatchEvent.MismatchedVersionInfo versionDifference, @Nullable ModContainer resolver) {

        public boolean wasSelfResolved() {
            return this.resolver != null && this.resolver.getModId().equals(this.modid);
        }
    }

    public static record MismatchedVersionInfo(ArtifactVersion oldVersion, @Nullable ArtifactVersion newVersion) {

        public boolean isMissing() {
            return this.newVersion == null;
        }

        public boolean wasUpgrade() {
            return this.newVersion == null ? false : this.newVersion.compareTo(this.oldVersion) > 0;
        }
    }
}