package noobanidus.mods.lootr.util;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import noobanidus.mods.lootr.client.impl.ClientGetter;
import noobanidus.mods.lootr.impl.ServerGetter;

public class Getter {

    @Nullable
    public static Player getPlayer() {
        return FMLEnvironment.dist == Dist.CLIENT ? ClientGetter.getPlayer() : ServerGetter.getPlayer();
    }
}