package noppes.npcs.mixin;

import java.io.File;
import net.minecraft.client.resources.SkinManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ SkinManager.class })
public interface SkinManagerMixin {

    @Accessor("skinsDirectory")
    File getDir();
}