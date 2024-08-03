package vazkii.patchouli.mixin.client;

import java.util.Map;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.multiplayer.ClientAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ClientAdvancements.class })
public interface AccessorClientAdvancements {

    @Accessor("progress")
    Map<Advancement, AdvancementProgress> getProgress();
}