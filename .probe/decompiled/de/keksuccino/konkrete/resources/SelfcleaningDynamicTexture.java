package de.keksuccino.konkrete.resources;

import com.mojang.blaze3d.platform.NativeImage;
import de.keksuccino.konkrete.annotations.OptifineFix;
import de.keksuccino.konkrete.reflection.ReflectionHelper;
import java.lang.reflect.Field;
import net.minecraft.client.renderer.texture.DynamicTexture;

@OptifineFix
public class SelfcleaningDynamicTexture extends DynamicTexture {

    public SelfcleaningDynamicTexture(NativeImage nativeImageIn) {
        super(nativeImageIn);
    }

    @Override
    public void upload() {
        super.upload();
        clearTextureData(this);
    }

    private static void clearTextureData(DynamicTexture texture) {
        try {
            Field f = ReflectionHelper.findField(DynamicTexture.class, "f_117977_");
            ((NativeImage) f.get(texture)).close();
            f.set(texture, new NativeImage(1, 1, true));
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }
}