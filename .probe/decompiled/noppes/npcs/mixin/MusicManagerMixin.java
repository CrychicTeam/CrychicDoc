package noppes.npcs.mixin;

import net.minecraft.client.sounds.MusicManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ MusicManager.class })
public interface MusicManagerMixin {

    @Accessor("nextSongDelay")
    void nextSongDelay(int var1);
}