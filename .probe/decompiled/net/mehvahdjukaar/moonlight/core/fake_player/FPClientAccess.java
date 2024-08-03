package net.mehvahdjukaar.moonlight.core.fake_player;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FPClientAccess {

    @OnlyIn(Dist.CLIENT)
    public static Player get(Level level, GameProfile id) {
        return FakeLocalPlayer.get(level, id);
    }

    @OnlyIn(Dist.CLIENT)
    public static void unloadLevel(LevelAccessor level) {
        FakeLocalPlayer.unloadLevel(level);
    }
}