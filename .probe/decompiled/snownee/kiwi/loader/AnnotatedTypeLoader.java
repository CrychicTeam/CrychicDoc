package snownee.kiwi.loader;

import com.google.common.io.Closeables;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

public class AnnotatedTypeLoader implements Supplier<KiwiConfiguration> {

    public final String modId;

    public AnnotatedTypeLoader(String modId) {
        this.modId = modId;
    }

    public KiwiConfiguration get() {
        Map<String, Object> properties = (Map<String, Object>) ModList.get().getModContainerById(this.modId).map(ModContainer::getModInfo).map(IModInfo::getModProperties).orElse(Collections.EMPTY_MAP);
        boolean useJson = (Boolean) properties.getOrDefault("kiwiJsonMap", Platform.isProduction());
        if (!useJson) {
            return new DevEnvAnnotatedTypeLoader(this.modId).get();
        } else {
            String name = "/%s.kiwi.json".formatted(this.modId);
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            if (is == null) {
                return null;
            } else {
                InputStreamReader isr = new InputStreamReader(is);
                KiwiConfiguration var6;
                try {
                    var6 = (KiwiConfiguration) new Gson().fromJson(isr, KiwiConfiguration.class);
                } finally {
                    Closeables.closeQuietly(isr);
                }
                return var6;
            }
        }
    }
}