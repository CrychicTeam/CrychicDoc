package org.embeddedt.modernfix.common.mixin.perf.ticking_chunk_alloc;

import java.time.LocalDate;
import net.minecraft.world.entity.ambient.Bat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { Bat.class }, priority = 1200)
public class BatMixin {

    private static long mfix$lastQueriedTime = -1L;

    private static LocalDate mfix$lastQueriedDate = null;

    @Redirect(method = { "isHalloween" }, at = @At(value = "INVOKE", target = "Ljava/time/LocalDate;now()Ljava/time/LocalDate;"), require = 0)
    private static LocalDate useCachedLocalDate() {
        LocalDate date = mfix$lastQueriedDate;
        if (date == null || Math.abs(System.currentTimeMillis() - mfix$lastQueriedTime) > 30000L) {
            mfix$lastQueriedDate = date = LocalDate.now();
            mfix$lastQueriedTime = System.currentTimeMillis();
        }
        return date;
    }
}