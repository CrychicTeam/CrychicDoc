package vazkii.patchouli.mixin.client;

import java.util.Map;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ KeyMapping.class })
public interface AccessorKeyMapping {

    @Accessor("ALL")
    static Map<String, KeyMapping> getAllKeyMappings() {
        throw new IllegalStateException();
    }
}