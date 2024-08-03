package malte0811.ferritecore.mixin.dedupmultipart;

import java.util.Map;
import net.minecraft.client.resources.model.MultiPartBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { MultiPartBakedModel.class }, priority = 1100)
public class MixinMultipartModel {

    @Redirect(method = { "getQuads", "method_4707", "emitBlockQuads", "getSelectors" }, at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
    public <K, V> V redirectCacheGet(Map<K, V> map, K key) {
        synchronized (map) {
            return (V) map.get(key);
        }
    }

    @Redirect(method = { "getQuads", "method_4707", "emitBlockQuads", "getSelectors" }, at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
    public <K, V> V redirectCachePut(Map<K, V> map, K key, V value) {
        synchronized (map) {
            return (V) map.put(key, value);
        }
    }
}