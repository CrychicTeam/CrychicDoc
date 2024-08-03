package icyllis.modernui.mc.testforge.trash;

import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.common.util.LazyOptional;

@Deprecated
public class LanguageData {

    private static LanguageData sInstance;

    private Object2ObjectMap<String, String> mData;

    private boolean mDefaultRTL;

    public LanguageData() {
        LazyOptional<Object> lazyOptional = LazyOptional.empty();
        Object o = lazyOptional.orElse(null);
        if (o != null) {
        }
    }

    public LanguageData(Map<String, String> data, boolean defaultRTL) {
        this.mData = new Object2ObjectAVLTreeMap(data);
        this.mDefaultRTL = defaultRTL;
    }

    @Nonnull
    public static LanguageData getInstance() {
        return sInstance;
    }

    public static void setInstance(@Nonnull LanguageData instance) {
        sInstance = instance;
    }

    @Nullable
    public String get(@Nonnull String key) {
        return (String) this.mData.get(key);
    }

    @Nonnull
    public String getOrDefault(@Nonnull String key) {
        String ret = (String) this.mData.get(key);
        return ret == null ? key : ret;
    }

    public boolean isDefaultRTL() {
        return this.mDefaultRTL;
    }
}