package se.mickelus.tetra.client;

import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.ToolAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.data.GlyphData;

public class ToolActionIconStore implements ResourceManagerReloadListener {

    protected static final String jsonExtension = ".json";

    private static final String directory = "tool_actions";

    private static final Logger logger = LogManager.getLogger();

    public static ToolActionIconStore instance;

    private Map<ToolAction, GlyphData> icons = Collections.emptyMap();

    public ToolActionIconStore() {
        instance = this;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.icons = this.prepareIcons();
        logger.info("Loaded {} tool action icons", this.icons.size());
    }

    public GlyphData getIcon(ToolAction action) {
        return (GlyphData) this.icons.get(action);
    }

    private Map<ToolAction, GlyphData> prepareIcons() {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        return (Map<ToolAction, GlyphData>) resourceManager.listResources("tool_actions", rl -> rl.getPath().endsWith(".json")).entrySet().stream().filter(entry -> "tetra".equals(((ResourceLocation) entry.getKey()).getNamespace())).collect(HashMap::new, (map, entry) -> map.put(this.getAction((ResourceLocation) entry.getKey()), this.getGlyph((ResourceLocation) entry.getKey(), (Resource) entry.getValue())), HashMap::putAll);
    }

    private ToolAction getAction(ResourceLocation resourceLocation) {
        String path = resourceLocation.getPath();
        return ToolAction.get(path.substring("tool_actions".length() + 1, path.length() - ".json".length()));
    }

    private GlyphData getGlyph(ResourceLocation resourceLocation, Resource resource) {
        try {
            BufferedReader reader = resource.openAsReader();
            GlyphData var4;
            try {
                var4 = (GlyphData) Optional.of((GlyphData) GsonHelper.fromJson(DataManager.gson, reader, GlyphData.class)).orElse(null);
            } catch (Throwable var7) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if (reader != null) {
                reader.close();
            }
            return var4;
        } catch (JsonParseException | IOException var8) {
            logger.warn("Failed to parse tool action icon from '{}': {}", resourceLocation, var8);
            return null;
        }
    }
}