package dev.architectury.mixin.forge;

import dev.architectury.event.forge.EventHandlerImplCommon;
import java.lang.ref.WeakReference;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ LevelEvent.class })
public class MixinLevelEvent implements EventHandlerImplCommon.LevelEventAttachment {

    @Unique
    private WeakReference<LevelAccessor> level;

    @Override
    public LevelAccessor architectury$getAttachedLevel() {
        return this.level == null ? null : (LevelAccessor) this.level.get();
    }

    @Override
    public void architectury$attachLevel(LevelAccessor level) {
        this.level = new WeakReference(level);
    }
}