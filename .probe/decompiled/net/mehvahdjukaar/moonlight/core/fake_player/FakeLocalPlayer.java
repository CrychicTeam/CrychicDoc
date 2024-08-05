package net.mehvahdjukaar.moonlight.core.fake_player;

import com.mojang.authlib.GameProfile;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class FakeLocalPlayer extends AbstractClientPlayer {

    private static final boolean HAS_CACHE = PlatHelper.getPlatform().isForge();

    private static final WeakHashMap<ClientLevel, Map<GameProfile, FakeLocalPlayer>> FAKE_PLAYERS = new WeakHashMap();

    private final EntityDimensions dimensions = EntityDimensions.fixed(0.0F, 0.0F);

    static FakeLocalPlayer get(Level level, GameProfile username) {
        return !HAS_CACHE ? new FakeLocalPlayer((ClientLevel) level, username) : (FakeLocalPlayer) ((Map) FAKE_PLAYERS.computeIfAbsent((ClientLevel) level, l -> new HashMap())).computeIfAbsent(username, u -> new FakeLocalPlayer((ClientLevel) level, username));
    }

    static void unloadLevel(LevelAccessor level) {
        FAKE_PLAYERS.entrySet().removeIf(e -> e.getKey() == level);
    }

    public FakeLocalPlayer(ClientLevel pClientLevel, GameProfile pGameProfile) {
        super(pClientLevel, pGameProfile);
        this.f_19794_ = true;
    }

    @Override
    public void playSound(SoundEvent pSound, float pVolume, float pPitch) {
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.dimensions;
    }

    @Override
    public void tick() {
    }

    @Override
    public Vec3 position() {
        return new Vec3(this.m_20185_(), this.m_20186_(), this.m_20189_());
    }

    @Override
    public BlockPos blockPosition() {
        return new BlockPos((int) this.m_20185_(), (int) this.m_20186_(), (int) this.m_20189_());
    }

    @Override
    public void setXRot(float pXRot) {
        super.m_146926_(pXRot);
        this.f_19860_ = pXRot;
    }

    @Override
    public void setYRot(float pYRot) {
        super.m_146922_(pYRot);
        this.f_19859_ = pYRot;
    }
}