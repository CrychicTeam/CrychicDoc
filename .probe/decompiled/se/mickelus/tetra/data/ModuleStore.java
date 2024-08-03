package se.mickelus.tetra.data;

import com.google.gson.Gson;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.data.DataDistributor;
import se.mickelus.mutil.data.MergingDataStore;
import se.mickelus.tetra.module.data.ModuleData;

@ParametersAreNonnullByDefault
public class ModuleStore extends MergingDataStore<ModuleData, ModuleData[]> {

    public ModuleStore(Gson gson, String namespace, String directory, DataDistributor distributor) {
        super(gson, namespace, directory, ModuleData.class, ModuleData[].class, distributor);
    }

    protected ModuleData mergeData(ModuleData[] data) {
        if (data.length > 0) {
            ModuleData result = data[0];
            for (int i = 1; i < data.length; i++) {
                if (data[i].replace) {
                    result = data[i];
                } else {
                    ModuleData.copyFields(data[i], result);
                }
            }
            return result;
        } else {
            return null;
        }
    }
}