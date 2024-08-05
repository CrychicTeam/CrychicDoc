package net.raphimc.immediatelyfast.feature.sign_text_buffering;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.block.entity.SignText;

public class SignTextCache implements ResourceManagerReloadListener {

    public final SignAtlasFramebuffer signAtlasFramebuffer = new SignAtlasFramebuffer();

    public final Cache<SignText, SignAtlasFramebuffer.Slot> slotCache = CacheBuilder.newBuilder().expireAfterAccess(5L, TimeUnit.SECONDS).removalListener(notification -> {
        if (!notification.getCause().equals(RemovalCause.EXPLICIT)) {
            SignAtlasFramebuffer.Slot slot = (SignAtlasFramebuffer.Slot) notification.getValue();
            if (slot != null) {
                slot.markFree();
            }
        }
    }).build();

    public final RenderType renderLayer = RenderType.text(this.signAtlasFramebuffer.getTextureId());

    public void clearCache() {
        this.slotCache.invalidateAll();
        this.signAtlasFramebuffer.clear();
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        this.clearCache();
    }
}