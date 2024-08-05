package journeymap.client.service.webmap.kotlin.routes;

import com.mojang.blaze3d.platform.NativeImage;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.collections.CollectionsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.text.StringsKt;
import info.journeymap.shaded.kotlin.spark.kotlin.RouteHandler;
import info.journeymap.shaded.org.eclipse.jetty.io.EofException;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.io.FileNotFoundException;
import java.nio.channels.Channels;
import java.util.List;
import javax.imageio.IIOException;
import journeymap.client.JourneymapClient;
import journeymap.client.service.webmap.Webmap;
import journeymap.client.texture.TextureCache;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

@Metadata(mv = { 1, 8, 0 }, k = 2, xi = 48, d1 = { "\u0000\u001e\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0000\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t" }, d2 = { "ALLOWED_EXTENSIONS", "", "", "logger", "Lorg/apache/logging/log4j/Logger;", "resourcesGet", "", "handler", "Linfo/journeymap/shaded/kotlin/spark/kotlin/RouteHandler;", "journeymap" })
public final class ResourcesKt {

    @NotNull
    private static final Logger logger;

    @NotNull
    private static final List<String> ALLOWED_EXTENSIONS = CollectionsKt.listOf("png");

    @NotNull
    public static final Object resourcesGet(@NotNull RouteHandler handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        NativeImage img = null;
        String resource = handler.getRequest().queryParams("resource");
        ResourceLocation resourceLocation = new ResourceLocation(resource);
        Intrinsics.checkNotNullExpressionValue(resource, "resource");
        CharSequence var10000 = (CharSequence) resource;
        char[] var5 = new char[] { '.' };
        String extension = CollectionsKt.last(StringsKt.split$default(var10000, var5, false, 0, 6, null));
        if (Minecraft.getInstance().level != null && JourneymapClient.getInstance().isMapping()) {
            if (StringsKt.contains$default((CharSequence) extension, (CharSequence) ":", false, 2, null)) {
                var10000 = (CharSequence) extension;
                String[] var12 = new String[] { ":" };
                extension = CollectionsKt.first(StringsKt.split$default(var10000, var12, false, 0, 6, null));
            }
            var10000 = (CharSequence) resource;
            String[] var13 = new String[] { ":" };
            if (StringsKt.contains$default(CollectionsKt.first(StringsKt.split$default(var10000, var13, false, 0, 6, null)), (CharSequence) "fake", false, 2, null)) {
                NativeImage var18 = TextureCache.getTexture(resourceLocation).getNativeImage();
                Intrinsics.checkNotNullExpressionValue(var18, "getTexture(resourceLocation).nativeImage");
                img = var18;
            } else {
                try {
                    NativeImage var21 = NativeImage.read(journeymap.common.kotlin.extensions.ResourcesKt.getResourceAsStream(resourceLocation));
                    Intrinsics.checkNotNullExpressionValue(var21, "{\n            NativeImag…urceAsStream())\n        }");
                    var14 = var21;
                } catch (FileNotFoundException var7) {
                    logger.warn("File at resource location not found: " + resource);
                    handler.status(404);
                    NativeImage var20 = NativeImage.read(Webmap.INSTANCE.getClass().getResource("/assets/journeymap/ui/img/marker-dot-32.png").openStream());
                    Intrinsics.checkNotNullExpressionValue(var20, "{\n            logger.war…).openStream())\n        }");
                    var14 = var20;
                } catch (EofException var8) {
                    logger.info("Connection closed while writing image response. Webmap probably reloaded.");
                    return "";
                } catch (IIOException var9) {
                    logger.info("Connection closed while writing image response. Webmap probably reloaded.");
                    return "";
                } catch (Exception var10) {
                    logger.error("Exception thrown while retrieving resource at location: " + resource, (Throwable) var10);
                    handler.status(500);
                    NativeImage var19 = NativeImage.read(Webmap.INSTANCE.getClass().getResource("/assets/journeymap/ui/img/marker-dot-32.png").openStream());
                    Intrinsics.checkNotNullExpressionValue(var19, "{\n            logger.err…).openStream())\n        }");
                    var14 = var19;
                }
                img = var14;
            }
            handler.getResponse().raw().setContentType("image/" + extension);
            img.writeToChannel(Channels.newChannel(handler.getResponse().raw().getOutputStream()));
            handler.getResponse().raw().getOutputStream().flush();
            var10000 = (CharSequence) resource;
            String[] var15 = new String[] { ":" };
            if (!StringsKt.contains$default(CollectionsKt.first(StringsKt.split$default(var10000, var15, false, 0, 6, null)), (CharSequence) "fake", false, 2, null)) {
                img.close();
            }
            return handler.getResponse();
        } else {
            return "";
        }
    }

    static {
        Logger var10000 = Journeymap.getLogger("webmap/routes/resources");
        Intrinsics.checkNotNullExpressionValue(var10000, "getLogger(\"webmap/routes/resources\")");
        logger = var10000;
    }
}