package club.iananderson.seasonhud.mixin.ftbchunks;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.impl.seasons.CurrentSeason;
import dev.ftb.mods.ftbchunks.client.FTBChunksClient;
import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import dev.ftb.mods.ftbchunks.client.map.MapDimension;
import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ FTBChunksClient.class })
public class FTBChunksClientMixin {

    @Unique
    private static final BooleanValue MINIMAP_SEASON = (BooleanValue) FTBChunksClientConfig.MINIMAP.addBoolean("season", true).comment(new String[] { "Show season under minimap" });

    @Inject(method = { "buildMinimapTextData" }, at = { @At("RETURN") }, remap = false, cancellable = true)
    private void buildMinimapTextData(Minecraft mc, double playerX, double playerY, double playerZ, MapDimension dim, CallbackInfoReturnable<List<Component>> cir) {
        MutableComponent seasonCombined = Component.translatable("desc.seasonhud.combined", ((Component) CurrentSeason.getSeasonHudName().get(0)).copy().withStyle(Common.SEASON_STYLE), ((Component) CurrentSeason.getSeasonHudName().get(1)).copy());
        List<Component> res = (List<Component>) cir.getReturnValue();
        Config.enableMod.set((Boolean) MINIMAP_SEASON.get());
        if (Config.enableMod.get() && Config.enableMinimapIntegration.get()) {
            res.add(seasonCombined);
        }
        cir.setReturnValue(res);
    }
}