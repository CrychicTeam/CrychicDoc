package se.mickelus.tetra.data;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.mutil.data.DataDistributor;
import se.mickelus.mutil.data.DataStore;
import se.mickelus.tetra.module.data.ImprovementData;
import se.mickelus.tetra.module.data.MaterialImprovementData;

@ParametersAreNonnullByDefault
public class ImprovementStore extends DataStore<ImprovementData[]> {

    private final MaterialStore materialStore;

    public ImprovementStore(Gson gson, String namespace, String directory, MaterialStore materialStore, DataDistributor distributor) {
        super(gson, namespace, directory, ImprovementData[].class, distributor);
        this.materialStore = materialStore;
    }

    @Override
    protected void processData() {
        this.dataMap = (Map<ResourceLocation, ImprovementData[]>) this.dataMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> this.processData((ImprovementData[]) entry.getValue())));
    }

    private ImprovementData[] processData(ImprovementData[] data) {
        return (ImprovementData[]) Arrays.stream(data).flatMap(improvement -> improvement instanceof MaterialImprovementData ? this.expandMaterialImprovement((MaterialImprovementData) improvement) : Stream.of(improvement)).toArray(ImprovementData[]::new);
    }

    private Stream<ImprovementData> expandMaterialImprovement(MaterialImprovementData data) {
        return Arrays.stream(data.materials).map(rl -> rl.getPath().endsWith("/") ? this.materialStore.getDataIn(rl) : (Collection) Optional.ofNullable(this.materialStore.getData(rl)).map(Collections::singletonList).orElseGet(Collections::emptyList)).flatMap(Collection::stream).map(data::combine);
    }
}