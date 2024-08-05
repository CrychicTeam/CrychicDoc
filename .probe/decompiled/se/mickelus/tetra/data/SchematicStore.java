package se.mickelus.tetra.data;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.data.DataDistributor;
import se.mickelus.mutil.data.MergingDataStore;
import se.mickelus.tetra.module.schematic.SchematicDefinition;

@ParametersAreNonnullByDefault
public class SchematicStore extends MergingDataStore<SchematicDefinition, SchematicDefinition[]> {

    public SchematicStore(Gson gson, String namespace, String directory, DataDistributor distributor) {
        super(gson, namespace, directory, SchematicDefinition.class, SchematicDefinition[].class, distributor);
    }

    protected SchematicDefinition mergeData(SchematicDefinition[] collection) {
        if (collection.length > 0) {
            SchematicDefinition result = collection[0];
            for (int i = 1; i < collection.length; i++) {
                if (collection[i].replace) {
                    String[] sources = (String[]) Stream.concat(Arrays.stream(collection[i].sources), Arrays.stream(result.sources)).distinct().toArray(String[]::new);
                    result = collection[i];
                    result.sources = sources;
                } else {
                    SchematicDefinition.copyFields(collection[i], result);
                }
            }
            return result;
        } else {
            return null;
        }
    }

    @Override
    protected void processData() {
        this.dataMap.forEach((key, value) -> value.key = key.getPath());
    }
}