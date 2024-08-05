package com.mna.tools.proxy;

import com.mna.entities.constructs.animated.Construct;
import com.mna.tools.DidYouKnowHelper;
import com.mna.tools.ISidedProxy;
import java.util.function.Predicate;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ServerProxy implements ISidedProxy {

    @Override
    public Level getClientWorld() {
        return null;
    }

    @Override
    public Player getClientPlayer() {
        return null;
    }

    @Override
    public long getGameTicks() {
        return 0L;
    }

    @Override
    public void incrementTick() {
    }

    @Override
    public void openConstructDiagnostics(Construct target) {
    }

    @Override
    public boolean isUIModifierKeyDown() {
        return false;
    }

    @Override
    public Construct getDummyConstructForRender() {
        return null;
    }

    @Override
    public void setRenderViewEntity(Entity e) {
    }

    @Override
    public void resetRenderViewEntity() {
    }

    @Override
    public void sendCastingResourceGuiEvents() {
    }

    @Override
    public boolean isGamePaused() {
        return false;
    }

    @Override
    public void setFlySpeed(Player player, float speed) {
    }

    @Override
    public void setFlightEnabled(Player player, boolean enabled) {
        if (player.getAbilities().mayfly != enabled) {
            if (enabled) {
                player.getAbilities().mayfly = true;
            } else {
                boolean creative = player.isCreative() || player.isSpectator();
                player.getAbilities().mayfly = creative;
                if (!creative) {
                    player.getAbilities().flying = false;
                }
            }
            if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).onUpdateAbilities();
            }
        }
    }

    @Override
    public Vec3 getClientLastTickPosition() {
        return Vec3.ZERO;
    }

    @Override
    public boolean checkConstructDanceMixPlaying() {
        return false;
    }

    @Override
    public Vec3 getCameraPosition() {
        return Vec3.ZERO;
    }

    @Override
    public void playLoopingSound(SoundEvent sound, String identifier, Predicate<String> predicate) {
    }

    @Override
    public void playLoopingSound(SoundEvent sound, String identifier, Predicate<String> predicate, float volume) {
    }

    @Override
    public void playLoopingSound(SoundEvent sound, String identifier, Predicate<String> predicate, float volume, BlockPos position) {
    }

    @Override
    public void playEntityAliveLoopingSound(SoundEvent sound, Entity entity) {
    }

    @Override
    public void showDidYouKnow(Player player, String msg) {
        DidYouKnowHelper.CheckAndShowDidYouKnow(player, msg);
    }

    @Override
    public boolean playerHasAdvancement(Player player, ResourceLocation advancementID) {
        if (player instanceof ServerPlayer sp) {
            Advancement advancement = sp.m_9236_().getServer().getAdvancements().getAdvancement(advancementID);
            if (advancement != null) {
                PlayerAdvancements advancements = sp.getAdvancements();
                AdvancementProgress progress = advancements.getOrStartProgress(advancement);
                return progress.isDone();
            }
        }
        return false;
    }
}