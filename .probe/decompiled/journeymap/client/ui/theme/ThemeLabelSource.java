package journeymap.client.ui.theme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import journeymap.client.Constants;
import journeymap.client.data.WorldData;
import journeymap.client.ui.UIManager;
import journeymap.common.properties.config.StringField;
import net.minecraft.client.Minecraft;

public class ThemeLabelSource implements StringField.ValuesProvider {

    public static final Map<String, ThemeLabelSource.InfoSlot> values = new HashMap();

    public static ThemeLabelSource.InfoSlot FPS = create("jm.theme.labelsource.fps", 10L, 1L, ThemeLabelSource::getFps);

    public static ThemeLabelSource.InfoSlot GameTime = create("jm.theme.labelsource.gametime", 0L, 500L, ThemeLabelSource::getGameTime);

    public static ThemeLabelSource.InfoSlot GameTimeReal = create("jm.theme.labelsource.gametime.real", 0L, 500L, ThemeLabelSource::getGameTimeReal);

    public static ThemeLabelSource.InfoSlot RealTime = create("jm.theme.labelsource.realtime", 0L, 500L, ThemeLabelSource::getRealTime);

    public static ThemeLabelSource.InfoSlot Location = create("jm.theme.labelsource.location", 100L, 1L, ThemeLabelSource::getLocation);

    public static ThemeLabelSource.InfoSlot Biome = create("jm.theme.labelsource.biome", 1000L, 1L, ThemeLabelSource::getBiome);

    public static ThemeLabelSource.InfoSlot Dimension = create("jm.theme.labelsource.dimension", 1000L, 1L, ThemeLabelSource::getDimension);

    public static ThemeLabelSource.InfoSlot Region = create("jm.theme.labelsource.region", 1000L, 1L, ThemeLabelSource::getRegion);

    public static ThemeLabelSource.InfoSlot LightLevel = create("jm.theme.labelsource.lightlevel", 100L, 100L, ThemeLabelSource::getLightLevel);

    public static ThemeLabelSource.InfoSlot MoonPhase = create("jm.theme.labelsource.moonphase", 100L, 100L, ThemeLabelSource::getMoonPhase);

    public static ThemeLabelSource.InfoSlot Blank = create("jm.theme.labelsource.blank", 0L, 1L, () -> "");

    public static ThemeLabelSource.InfoSlot create(String key, long cacheMillis, long granularityMillis, Supplier<String> supplier) {
        ThemeLabelSource.InfoSlot slot = new ThemeLabelSource.InfoSlot(key, cacheMillis, granularityMillis, supplier);
        values.put(key, slot);
        return slot;
    }

    public static ThemeLabelSource.InfoSlot create(String modId, String key, long cacheMillis, long granularityMillis, Supplier<String> supplier) {
        ThemeLabelSource.InfoSlot slot = new ThemeLabelSource.InfoSlot(modId, key, cacheMillis, granularityMillis, supplier);
        values.put(key, slot);
        return slot;
    }

    public static void resetCaches() {
        for (ThemeLabelSource.InfoSlot source : values.values()) {
            source.lastCallTime = 0L;
            source.lastValue = "";
        }
    }

    @Override
    public List<String> getStrings() {
        return (List<String>) values.values().stream().map(infoSlot -> infoSlot.key).collect(Collectors.toList());
    }

    @Override
    public String getDefaultString() {
        return null;
    }

    private static String getFps() {
        return Minecraft.fps + " fps";
    }

    private static String getGameTime() {
        return WorldData.getGameTime();
    }

    private static String getGameTimeReal() {
        return WorldData.getRealGameTime();
    }

    private static String getRealTime() {
        return WorldData.getSystemTime();
    }

    private static String getLocation() {
        return UIManager.INSTANCE.getMiniMap().getLocation();
    }

    private static String getBiome() {
        return UIManager.INSTANCE.getMiniMap().getBiome();
    }

    private static String getDimension() {
        return WorldData.getDimension();
    }

    private static String getLightLevel() {
        return WorldData.getLightLevel();
    }

    private static String getRegion() {
        return WorldData.getRegion();
    }

    private static String getMoonPhase() {
        return WorldData.getMoonPhase();
    }

    public static class InfoSlot {

        private final String key;

        private final String modId;

        private final Supplier<String> supplier;

        private final long cacheMillis;

        private final long granularityMillis;

        private long lastCallTime;

        private String lastValue = "";

        public InfoSlot(String key, long cacheMillis, long granularityMillis, Supplier<String> supplier) {
            this.key = key;
            this.modId = "";
            this.supplier = supplier;
            this.cacheMillis = cacheMillis;
            this.granularityMillis = granularityMillis;
        }

        public InfoSlot(String modId, String key, long cacheMillis, long granularityMillis, Supplier<String> supplier) {
            this.key = key;
            this.modId = modId;
            this.supplier = supplier;
            this.cacheMillis = cacheMillis;
            this.granularityMillis = granularityMillis;
        }

        public String getLabelText(long currentTimeMillis) {
            try {
                long now = this.granularityMillis * (currentTimeMillis / this.granularityMillis);
                if (now - this.lastCallTime <= this.cacheMillis) {
                    return this.lastValue;
                } else {
                    this.lastCallTime = now;
                    this.lastValue = (String) this.supplier.get();
                    return this.lastValue;
                }
            } catch (Exception var5) {
                return "?";
            }
        }

        public boolean isShown() {
            return Minecraft.getInstance().level == null ? false : this != ThemeLabelSource.Blank && this.supplier != null && this.supplier.get() != null && !((String) this.supplier.get()).isEmpty() && !"".equals(this.supplier.get());
        }

        public String getTooltip() {
            if (!Constants.getString(this.key + ".tooltip").equals(this.key + ".tooltip")) {
                String prefix = this.modId + ":";
                return "".equals(this.modId) ? Constants.getString(this.key + ".tooltip") : prefix + Constants.getString(this.key + ".tooltip");
            } else {
                return null;
            }
        }

        public String getKey() {
            return this.key;
        }
    }
}