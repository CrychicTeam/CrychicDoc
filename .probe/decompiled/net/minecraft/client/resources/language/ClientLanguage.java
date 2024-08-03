package net.minecraft.client.resources.language;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FormattedCharSequence;
import org.slf4j.Logger;

public class ClientLanguage extends Language {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<String, String> storage;

    private final boolean defaultRightToLeft;

    private ClientLanguage(Map<String, String> mapStringString0, boolean boolean1) {
        this.storage = mapStringString0;
        this.defaultRightToLeft = boolean1;
    }

    public static ClientLanguage loadFrom(ResourceManager resourceManager0, List<String> listString1, boolean boolean2) {
        Map<String, String> $$3 = Maps.newHashMap();
        for (String $$4 : listString1) {
            String $$5 = String.format(Locale.ROOT, "lang/%s.json", $$4);
            for (String $$6 : resourceManager0.getNamespaces()) {
                try {
                    ResourceLocation $$7 = new ResourceLocation($$6, $$5);
                    appendFrom($$4, resourceManager0.getResourceStack($$7), $$3);
                } catch (Exception var10) {
                    LOGGER.warn("Skipped language file: {}:{} ({})", new Object[] { $$6, $$5, var10.toString() });
                }
            }
        }
        return new ClientLanguage(ImmutableMap.copyOf($$3), boolean2);
    }

    private static void appendFrom(String string0, List<Resource> listResource1, Map<String, String> mapStringString2) {
        for (Resource $$3 : listResource1) {
            try {
                InputStream $$4 = $$3.open();
                try {
                    Language.loadFromJson($$4, mapStringString2::put);
                } catch (Throwable var9) {
                    if ($$4 != null) {
                        try {
                            $$4.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }
                    throw var9;
                }
                if ($$4 != null) {
                    $$4.close();
                }
            } catch (IOException var10) {
                LOGGER.warn("Failed to load translations for {} from pack {}", new Object[] { string0, $$3.sourcePackId(), var10 });
            }
        }
    }

    @Override
    public String getOrDefault(String string0, String string1) {
        return (String) this.storage.getOrDefault(string0, string1);
    }

    @Override
    public boolean has(String string0) {
        return this.storage.containsKey(string0);
    }

    @Override
    public boolean isDefaultRightToLeft() {
        return this.defaultRightToLeft;
    }

    @Override
    public FormattedCharSequence getVisualOrder(FormattedText formattedText0) {
        return FormattedBidiReorder.reorder(formattedText0, this.defaultRightToLeft);
    }
}