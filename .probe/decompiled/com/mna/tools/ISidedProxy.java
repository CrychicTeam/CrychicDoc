package com.mna.tools;

import com.mna.entities.constructs.animated.Construct;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface ISidedProxy {

    @Nullable
    Player getClientPlayer();

    Vec3 getClientLastTickPosition();

    @Nullable
    Level getClientWorld();

    @Nullable
    Construct getDummyConstructForRender();

    void setRenderViewEntity(Entity var1);

    void resetRenderViewEntity();

    long getGameTicks();

    void incrementTick();

    void openConstructDiagnostics(Construct var1);

    boolean isUIModifierKeyDown();

    void sendCastingResourceGuiEvents();

    boolean isGamePaused();

    void setFlySpeed(Player var1, float var2);

    void setFlightEnabled(Player var1, boolean var2);

    boolean checkConstructDanceMixPlaying();

    boolean playerHasAdvancement(Player var1, ResourceLocation var2);

    Vec3 getCameraPosition();

    void playLoopingSound(SoundEvent var1, String var2, Predicate<String> var3);

    void playLoopingSound(SoundEvent var1, String var2, Predicate<String> var3, float var4);

    void playLoopingSound(SoundEvent var1, String var2, Predicate<String> var3, float var4, BlockPos var5);

    void playEntityAliveLoopingSound(SoundEvent var1, Entity var2);

    void showDidYouKnow(Player var1, String var2);
}