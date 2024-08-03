package net.mehvahdjukaar.moonlight.api.util;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.core.fake_player.FPClientAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FakePlayerManager {

    private static final GameProfile DEFAULT = new GameProfile(UUID.fromString("61e22C44-14d5-1f22-ed27-13D2C95CA355"), "[ML_Fake_Player]");

    public static Player get(GameProfile id, Entity entity) {
        return get(id, entity.level());
    }

    public static Player get(GameProfile id, Level level) {
        try {
            Player fakePlayer;
            if (level instanceof ServerLevel sl) {
                fakePlayer = PlatHelper.getFakeServerPlayer(id, sl);
            } else {
                fakePlayer = FPClientAccess.get(level, id);
            }
            return fakePlayer;
        } catch (Exception var4) {
            throw new IllegalArgumentException("Level must be either ServerLevel or ClientLevel", var4);
        }
    }

    public static Player get(GameProfile id, Entity copyPosFrom, Entity copyRotFrom) {
        Player p = get(id, copyPosFrom.level());
        p.m_6034_(copyPosFrom.getX(), copyPosFrom.getY(), copyPosFrom.getZ());
        p.m_5616_(copyRotFrom.getYHeadRot());
        p.m_146926_(copyRotFrom.getXRot());
        p.m_146922_(copyRotFrom.getYRot());
        p.m_146867_();
        return p;
    }

    public static Player getDefault(Entity copyPosFrom, Entity copyRotFrom) {
        return get(DEFAULT, copyPosFrom, copyRotFrom);
    }

    public static Player getDefault(Level level) {
        return get(DEFAULT, level);
    }

    public static Player getDefault(Entity entity) {
        return get(DEFAULT, entity);
    }
}