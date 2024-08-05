package software.bernie.geckolib.loading;

import com.google.gson.JsonObject;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.apache.commons.io.IOUtils;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.loading.json.raw.Model;
import software.bernie.geckolib.loading.object.BakedAnimations;
import software.bernie.geckolib.util.JsonUtil;

public final class FileLoader {

    public static BakedAnimations loadAnimationsFile(ResourceLocation location, ResourceManager manager) {
        return (BakedAnimations) JsonUtil.GEO_GSON.fromJson(GsonHelper.getAsJsonObject(loadFile(location, manager), "animations"), BakedAnimations.class);
    }

    public static Model loadModelFile(ResourceLocation location, ResourceManager manager) {
        return (Model) JsonUtil.GEO_GSON.fromJson(loadFile(location, manager), Model.class);
    }

    public static JsonObject loadFile(ResourceLocation location, ResourceManager manager) {
        return GsonHelper.fromJson(JsonUtil.GEO_GSON, getFileContents(location, manager), JsonObject.class);
    }

    public static String getFileContents(ResourceLocation location, ResourceManager manager) {
        try {
            InputStream inputStream = manager.m_215593_(location).open();
            String var3;
            try {
                var3 = IOUtils.toString(inputStream, Charset.defaultCharset());
            } catch (Throwable var6) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return var3;
        } catch (Exception var7) {
            GeckoLib.LOGGER.error("Couldn't load " + location, var7);
            throw new RuntimeException(new FileNotFoundException(location.toString()));
        }
    }
}