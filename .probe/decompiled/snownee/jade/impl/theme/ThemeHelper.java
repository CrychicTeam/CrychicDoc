package snownee.jade.impl.theme;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import snownee.jade.Jade;
import snownee.jade.JadeClient;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.theme.Theme;
import snownee.jade.impl.config.WailaConfig;
import snownee.jade.overlay.DisplayHelper;
import snownee.jade.overlay.OverlayRenderer;
import snownee.jade.util.JsonConfig;

public class ThemeHelper extends SimpleJsonResourceReloadListener implements IThemeHelper {

    public static final ThemeHelper INSTANCE = new ThemeHelper();

    public static final ResourceLocation ID = new ResourceLocation("jade", "themes");

    private static final Int2ObjectMap<Style> styleCache = new Int2ObjectOpenHashMap(6);

    private final Map<ResourceLocation, Theme> themes = Maps.newTreeMap(Comparator.comparing(ResourceLocation::toString));

    public ThemeHelper() {
        super(JsonConfig.GSON, "jade_themes");
    }

    public static Style colorStyle(int color) {
        return (Style) styleCache.computeIfAbsent(color, Style.EMPTY::m_178520_);
    }

    @Override
    public Theme theme() {
        return (Theme) OverlayRenderer.theme.getValue();
    }

    @Override
    public Collection<Theme> getThemes() {
        return this.themes.values();
    }

    @NotNull
    @Override
    public Theme getTheme(ResourceLocation id) {
        return (Theme) this.themes.getOrDefault(id, Theme.DARK);
    }

    @Override
    public MutableComponent info(Object componentOrString) {
        return this.color(componentOrString, this.theme().infoColor);
    }

    @Override
    public MutableComponent success(Object componentOrString) {
        return this.color(componentOrString, this.theme().successColor);
    }

    @Override
    public MutableComponent warning(Object componentOrString) {
        return this.color(componentOrString, this.theme().warningColor);
    }

    @Override
    public MutableComponent danger(Object componentOrString) {
        return this.color(componentOrString, this.theme().dangerColor);
    }

    @Override
    public MutableComponent failure(Object componentOrString) {
        return this.color(componentOrString, this.theme().failureColor);
    }

    @Override
    public MutableComponent title(Object componentOrString) {
        Component component;
        if (componentOrString instanceof MutableComponent) {
            component = (MutableComponent) componentOrString;
        } else {
            component = Component.literal(Objects.toString(componentOrString));
        }
        return this.color(DisplayHelper.INSTANCE.stripColor(component), this.theme().titleColor);
    }

    @Override
    public MutableComponent seconds(int ticks) {
        return this.info(JadeClient.format("jade.seconds", ticks / 20));
    }

    protected MutableComponent color(Object componentOrString, int color) {
        if (componentOrString instanceof MutableComponent component) {
            return component.getStyle().isEmpty() ? component.setStyle(colorStyle(color)) : component.setStyle(component.getStyle().withColor(color));
        } else {
            return Component.literal(Objects.toString(componentOrString)).setStyle(colorStyle(color));
        }
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Set<ResourceLocation> existingKeys = Set.copyOf(this.themes.keySet());
        MutableObject<Theme> enable = new MutableObject();
        WailaConfig.ConfigOverlay config = Jade.CONFIG.get().getOverlay();
        this.themes.clear();
        map.forEach((idx, json) -> {
            try {
                JsonObject o = json.getAsJsonObject();
                Theme theme = (Theme) JsonConfig.GSON.fromJson(o, Theme.class);
                theme.id = idx;
                this.themes.put(idx, theme);
                if (enable.getValue() == null && GsonHelper.getAsBoolean(o, "autoEnable", false) && !existingKeys.contains(idx)) {
                    enable.setValue(theme);
                }
            } catch (Exception var7x) {
                Jade.LOGGER.error("Failed to load theme {}", idx, var7x);
            }
        });
        int hash = 0;
        for (ResourceLocation id : this.themes.keySet()) {
            hash = 31 * hash + id.hashCode();
        }
        if (hash != config.themesHash) {
            if (hash != 0 && enable.getValue() != null) {
                Theme theme = (Theme) enable.getValue();
                config.activeTheme = theme.id;
                Jade.LOGGER.info("Auto enabled theme {}", theme.id);
                if (theme.squareBorder != null) {
                    config.setSquare(theme.squareBorder);
                }
                if (theme.opacity != 0.0F) {
                    config.setAlpha(theme.opacity);
                }
            }
            config.themesHash = hash;
            Jade.CONFIG.save();
        }
        if (this.themes.isEmpty()) {
            this.themes.put(Theme.DARK.id, Theme.DARK);
        }
        config.applyTheme(config.activeTheme);
    }
}