package harmonised.pmmo.registry;

import com.google.common.base.Preconditions;
import harmonised.pmmo.util.MsLoggy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class LevelRegistry {

    private final List<BiFunction<String, Integer, Integer>> providers = new ArrayList();

    public void registerLevelProvider(BiFunction<String, Integer, Integer> provider) {
        Preconditions.checkNotNull(provider);
        this.providers.add(provider);
        MsLoggy.INFO.log(MsLoggy.LOG_CODE.API, "Level Provider Registered");
    }

    public int process(String skill, int nativeLevel) {
        int outLevel = nativeLevel;
        for (BiFunction<String, Integer, Integer> provider : this.providers) {
            outLevel = (Integer) provider.apply(skill, outLevel);
        }
        return outLevel;
    }
}