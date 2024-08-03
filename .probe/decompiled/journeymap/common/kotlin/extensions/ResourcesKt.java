package journeymap.common.kotlin.extensions;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

@Metadata(mv = { 1, 8, 0 }, k = 2, xi = 48, d1 = { "\u0000\f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0003" }, d2 = { "getResourceAsStream", "Ljava/io/InputStream;", "Lnet/minecraft/resources/ResourceLocation;", "journeymap" })
public final class ResourcesKt {

    @NotNull
    public static final InputStream getResourceAsStream(@NotNull ResourceLocation $this$getResourceAsStream) {
        Intrinsics.checkNotNullParameter($this$getResourceAsStream, "<this>");
        InputStream var10000 = Minecraft.getInstance().getResourceManager().m_215595_($this$getResourceAsStream);
        Intrinsics.checkNotNullExpressionValue(var10000, "getInstance().resourceManager.open(this)");
        return var10000;
    }
}