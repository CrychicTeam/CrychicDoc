package net.minecraft.client.resources.language;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.resources.metadata.language.LanguageMetadataSection;
import net.minecraft.locale.Language;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.slf4j.Logger;

public class LanguageManager implements ResourceManagerReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String DEFAULT_LANGUAGE_CODE = "en_us";

    private static final LanguageInfo DEFAULT_LANGUAGE = new LanguageInfo("US", "English", false);

    private Map<String, LanguageInfo> languages = ImmutableMap.of("en_us", DEFAULT_LANGUAGE);

    private String currentCode;

    public LanguageManager(String string0) {
        this.currentCode = string0;
    }

    private static Map<String, LanguageInfo> extractLanguages(Stream<PackResources> streamPackResources0) {
        Map<String, LanguageInfo> $$1 = Maps.newHashMap();
        streamPackResources0.forEach(p_264712_ -> {
            try {
                LanguageMetadataSection $$2 = p_264712_.getMetadataSection(LanguageMetadataSection.TYPE);
                if ($$2 != null) {
                    $$2.languages().forEach($$1::putIfAbsent);
                }
            } catch (IOException | RuntimeException var3) {
                LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", p_264712_.packId(), var3);
            }
        });
        return ImmutableMap.copyOf($$1);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager0) {
        this.languages = extractLanguages(resourceManager0.listPacks());
        List<String> $$1 = new ArrayList(2);
        boolean $$2 = DEFAULT_LANGUAGE.bidirectional();
        $$1.add("en_us");
        if (!this.currentCode.equals("en_us")) {
            LanguageInfo $$3 = (LanguageInfo) this.languages.get(this.currentCode);
            if ($$3 != null) {
                $$1.add(this.currentCode);
                $$2 = $$3.bidirectional();
            }
        }
        ClientLanguage $$4 = ClientLanguage.loadFrom(resourceManager0, $$1, $$2);
        I18n.setLanguage($$4);
        Language.inject($$4);
    }

    public void setSelected(String string0) {
        this.currentCode = string0;
    }

    public String getSelected() {
        return this.currentCode;
    }

    public SortedMap<String, LanguageInfo> getLanguages() {
        return new TreeMap(this.languages);
    }

    @Nullable
    public LanguageInfo getLanguage(String string0) {
        return (LanguageInfo) this.languages.get(string0);
    }
}