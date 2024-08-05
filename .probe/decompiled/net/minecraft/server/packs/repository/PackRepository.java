package net.minecraft.server.packs.repository;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.server.packs.PackResources;
import net.minecraft.world.flag.FeatureFlagSet;

public class PackRepository {

    private final Set<RepositorySource> sources;

    private Map<String, Pack> available = ImmutableMap.of();

    private List<Pack> selected = ImmutableList.of();

    public PackRepository(RepositorySource... repositorySource0) {
        this.sources = ImmutableSet.copyOf(repositorySource0);
    }

    public void reload() {
        List<String> $$0 = (List<String>) this.selected.stream().map(Pack::m_10446_).collect(ImmutableList.toImmutableList());
        this.available = this.discoverAvailable();
        this.selected = this.rebuildSelected($$0);
    }

    private Map<String, Pack> discoverAvailable() {
        Map<String, Pack> $$0 = Maps.newTreeMap();
        for (RepositorySource $$1 : this.sources) {
            $$1.loadPacks(p_143903_ -> $$0.put(p_143903_.getId(), p_143903_));
        }
        return ImmutableMap.copyOf($$0);
    }

    public void setSelected(Collection<String> collectionString0) {
        this.selected = this.rebuildSelected(collectionString0);
    }

    public boolean addPack(String string0) {
        Pack $$1 = (Pack) this.available.get(string0);
        if ($$1 != null && !this.selected.contains($$1)) {
            List<Pack> $$2 = Lists.newArrayList(this.selected);
            $$2.add($$1);
            this.selected = $$2;
            return true;
        } else {
            return false;
        }
    }

    public boolean removePack(String string0) {
        Pack $$1 = (Pack) this.available.get(string0);
        if ($$1 != null && this.selected.contains($$1)) {
            List<Pack> $$2 = Lists.newArrayList(this.selected);
            $$2.remove($$1);
            this.selected = $$2;
            return true;
        } else {
            return false;
        }
    }

    private List<Pack> rebuildSelected(Collection<String> collectionString0) {
        List<Pack> $$1 = (List<Pack>) this.getAvailablePacks(collectionString0).collect(Collectors.toList());
        for (Pack $$2 : this.available.values()) {
            if ($$2.isRequired() && !$$1.contains($$2)) {
                $$2.getDefaultPosition().insert($$1, $$2, Functions.identity(), false);
            }
        }
        return ImmutableList.copyOf($$1);
    }

    private Stream<Pack> getAvailablePacks(Collection<String> collectionString0) {
        return collectionString0.stream().map(this.available::get).filter(Objects::nonNull);
    }

    public Collection<String> getAvailableIds() {
        return this.available.keySet();
    }

    public Collection<Pack> getAvailablePacks() {
        return this.available.values();
    }

    public Collection<String> getSelectedIds() {
        return (Collection<String>) this.selected.stream().map(Pack::m_10446_).collect(ImmutableSet.toImmutableSet());
    }

    public FeatureFlagSet getRequestedFeatureFlags() {
        return (FeatureFlagSet) this.getSelectedPacks().stream().map(Pack::m_245532_).reduce(FeatureFlagSet::m_246699_).orElse(FeatureFlagSet.of());
    }

    public Collection<Pack> getSelectedPacks() {
        return this.selected;
    }

    @Nullable
    public Pack getPack(String string0) {
        return (Pack) this.available.get(string0);
    }

    public boolean isAvailable(String string0) {
        return this.available.containsKey(string0);
    }

    public List<PackResources> openAllSelected() {
        return (List<PackResources>) this.selected.stream().map(Pack::m_10445_).collect(ImmutableList.toImmutableList());
    }
}