package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.GlUtil;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

public class GpuWarnlistManager extends SimplePreparableReloadListener<GpuWarnlistManager.Preparations> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation GPU_WARNLIST_LOCATION = new ResourceLocation("gpu_warnlist.json");

    private ImmutableMap<String, String> warnings = ImmutableMap.of();

    private boolean showWarning;

    private boolean warningDismissed;

    private boolean skipFabulous;

    public boolean hasWarnings() {
        return !this.warnings.isEmpty();
    }

    public boolean willShowWarning() {
        return this.hasWarnings() && !this.warningDismissed;
    }

    public void showWarning() {
        this.showWarning = true;
    }

    public void dismissWarning() {
        this.warningDismissed = true;
    }

    public void dismissWarningAndSkipFabulous() {
        this.warningDismissed = true;
        this.skipFabulous = true;
    }

    public boolean isShowingWarning() {
        return this.showWarning && !this.warningDismissed;
    }

    public boolean isSkippingFabulous() {
        return this.skipFabulous;
    }

    public void resetWarnings() {
        this.showWarning = false;
        this.warningDismissed = false;
        this.skipFabulous = false;
    }

    @Nullable
    public String getRendererWarnings() {
        return (String) this.warnings.get("renderer");
    }

    @Nullable
    public String getVersionWarnings() {
        return (String) this.warnings.get("version");
    }

    @Nullable
    public String getVendorWarnings() {
        return (String) this.warnings.get("vendor");
    }

    @Nullable
    public String getAllWarnings() {
        StringBuilder $$0 = new StringBuilder();
        this.warnings.forEach((p_109235_, p_109236_) -> $$0.append(p_109235_).append(": ").append(p_109236_));
        return $$0.length() == 0 ? null : $$0.toString();
    }

    protected GpuWarnlistManager.Preparations prepare(ResourceManager resourceManager0, ProfilerFiller profilerFiller1) {
        List<Pattern> $$2 = Lists.newArrayList();
        List<Pattern> $$3 = Lists.newArrayList();
        List<Pattern> $$4 = Lists.newArrayList();
        profilerFiller1.startTick();
        JsonObject $$5 = parseJson(resourceManager0, profilerFiller1);
        if ($$5 != null) {
            profilerFiller1.push("compile_regex");
            compilePatterns($$5.getAsJsonArray("renderer"), $$2);
            compilePatterns($$5.getAsJsonArray("version"), $$3);
            compilePatterns($$5.getAsJsonArray("vendor"), $$4);
            profilerFiller1.pop();
        }
        profilerFiller1.endTick();
        return new GpuWarnlistManager.Preparations($$2, $$3, $$4);
    }

    protected void apply(GpuWarnlistManager.Preparations gpuWarnlistManagerPreparations0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2) {
        this.warnings = gpuWarnlistManagerPreparations0.apply();
    }

    private static void compilePatterns(JsonArray jsonArray0, List<Pattern> listPattern1) {
        jsonArray0.forEach(p_109239_ -> listPattern1.add(Pattern.compile(p_109239_.getAsString(), 2)));
    }

    @Nullable
    private static JsonObject parseJson(ResourceManager resourceManager0, ProfilerFiller profilerFiller1) {
        profilerFiller1.push("parse_json");
        JsonObject $$2 = null;
        try {
            Reader $$3 = resourceManager0.m_215597_(GPU_WARNLIST_LOCATION);
            try {
                $$2 = JsonParser.parseReader($$3).getAsJsonObject();
            } catch (Throwable var7) {
                if ($$3 != null) {
                    try {
                        $$3.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if ($$3 != null) {
                $$3.close();
            }
        } catch (JsonSyntaxException | IOException var8) {
            LOGGER.warn("Failed to load GPU warnlist");
        }
        profilerFiller1.pop();
        return $$2;
    }

    protected static final class Preparations {

        private final List<Pattern> rendererPatterns;

        private final List<Pattern> versionPatterns;

        private final List<Pattern> vendorPatterns;

        Preparations(List<Pattern> listPattern0, List<Pattern> listPattern1, List<Pattern> listPattern2) {
            this.rendererPatterns = listPattern0;
            this.versionPatterns = listPattern1;
            this.vendorPatterns = listPattern2;
        }

        private static String matchAny(List<Pattern> listPattern0, String string1) {
            List<String> $$2 = Lists.newArrayList();
            for (Pattern $$3 : listPattern0) {
                Matcher $$4 = $$3.matcher(string1);
                while ($$4.find()) {
                    $$2.add($$4.group());
                }
            }
            return String.join(", ", $$2);
        }

        ImmutableMap<String, String> apply() {
            Builder<String, String> $$0 = new Builder();
            String $$1 = matchAny(this.rendererPatterns, GlUtil.getRenderer());
            if (!$$1.isEmpty()) {
                $$0.put("renderer", $$1);
            }
            String $$2 = matchAny(this.versionPatterns, GlUtil.getOpenGLVersion());
            if (!$$2.isEmpty()) {
                $$0.put("version", $$2);
            }
            String $$3 = matchAny(this.vendorPatterns, GlUtil.getVendor());
            if (!$$3.isEmpty()) {
                $$0.put("vendor", $$3);
            }
            return $$0.build();
        }
    }
}