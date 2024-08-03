package com.mna.tools.proxy;

import com.mna.KeybindInit;
import com.mna.api.events.CastingResourceGuiRegistrationEvent;
import com.mna.api.sound.Music;
import com.mna.capabilities.playerdata.magic.resources.CastingResourceGuiRegistry;
import com.mna.entities.constructs.animated.Construct;
import com.mna.gui.constructs.GuiConstructDiagnostics;
import com.mna.sound.EntityAliveLoopingSound;
import com.mna.sound.PredicateLoopingSound;
import com.mna.tools.DidYouKnowHelper;
import com.mna.tools.ISidedProxy;
import java.util.function.Predicate;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy implements ISidedProxy, SoundEventListener, ClientAdvancements.Listener {

    private Minecraft mc;

    private long clientTickCounter;

    private Entity queuedRenderViewEntity;

    private Vec3 lastTickPosition;

    private SoundInstance activeDanceMix;

    private boolean initialized = false;

    private Advancement queryAdvancement = null;

    private AdvancementProgress progress = null;

    private Construct __dummyconstruct;

    public ClientProxy() {
        this.mc = Minecraft.getInstance();
    }

    @Override
    public Player getClientPlayer() {
        return this.mc.player;
    }

    @Override
    public Level getClientWorld() {
        return this.mc.level;
    }

    @Override
    public long getGameTicks() {
        return this.clientTickCounter;
    }

    @Override
    public void incrementTick() {
        if (!this.initialized) {
            this.mc.getSoundManager().addListener(this);
            this.initialized = true;
        }
        this.clientTickCounter++;
        if (this.queuedRenderViewEntity != null) {
            this.mc.cameraEntity = this.queuedRenderViewEntity;
            this.mc.gameRenderer.checkEntityPostEffect(this.queuedRenderViewEntity);
            this.queuedRenderViewEntity = null;
        }
        this.lastTickPosition = this.mc.player != null ? this.mc.player.m_20182_() : Vec3.ZERO;
    }

    @Override
    public Vec3 getClientLastTickPosition() {
        return this.lastTickPosition;
    }

    @Override
    public void openConstructDiagnostics(Construct target) {
        Minecraft.getInstance().setScreen(new GuiConstructDiagnostics(target));
    }

    @Override
    public boolean isUIModifierKeyDown() {
        return ((KeyMapping) KeybindInit.InventoryItemOpen.get()).isDown();
    }

    @Override
    public Construct getDummyConstructForRender() {
        if (this.__dummyconstruct == null && this.getClientWorld() != null) {
            this.__dummyconstruct = new Construct(this.getClientWorld());
        }
        return this.__dummyconstruct;
    }

    @Override
    public void resetRenderViewEntity() {
        this.setRenderViewEntity(this.mc.player);
    }

    @Override
    public void setRenderViewEntity(Entity e) {
        this.queuedRenderViewEntity = e;
    }

    @Override
    public void sendCastingResourceGuiEvents() {
        CastingResourceGuiRegistry.Instance.registerDefaults();
        MinecraftForge.EVENT_BUS.post(new CastingResourceGuiRegistrationEvent(CastingResourceGuiRegistry.Instance));
    }

    @Override
    public boolean isGamePaused() {
        return Minecraft.getInstance().isPaused();
    }

    @Override
    public void setFlySpeed(Player player, float speed) {
        player.getAbilities().setFlyingSpeed(speed);
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
                } else {
                    player.getAbilities().setFlyingSpeed(0.05F);
                }
            }
            if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).onUpdateAbilities();
            }
        }
    }

    @Override
    public boolean checkConstructDanceMixPlaying() {
        if (this.activeDanceMix != null) {
            if (!Minecraft.getInstance().getSoundManager().isActive(this.activeDanceMix)) {
                this.activeDanceMix = null;
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onPlaySound(SoundInstance inst, WeighedSoundEvents weight) {
        if (inst.getLocation().equals(Music.Construct.DANCE_MIX.getLocation())) {
            this.activeDanceMix = inst;
        }
    }

    @Override
    public Vec3 getCameraPosition() {
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        return camera.getPosition();
    }

    @Override
    public void playLoopingSound(SoundEvent sound, String identifier, Predicate<String> predicate) {
        Minecraft.getInstance().getSoundManager().play(new PredicateLoopingSound(sound, "active_loop", predicate));
    }

    @Override
    public void playLoopingSound(SoundEvent sound, String identifier, Predicate<String> predicate, float volume) {
        Minecraft.getInstance().getSoundManager().play(new PredicateLoopingSound(sound, "active_loop", predicate).setVolume(volume));
    }

    @Override
    public void playLoopingSound(SoundEvent sound, String identifier, Predicate<String> predicate, float volume, BlockPos position) {
        Minecraft.getInstance().getSoundManager().play(new PredicateLoopingSound(sound, "active_loop", predicate).setVolume(volume).setPosition(position));
    }

    @Override
    public void playEntityAliveLoopingSound(SoundEvent sound, Entity entity) {
        Minecraft.getInstance().getSoundManager().play(new EntityAliveLoopingSound(sound, entity));
    }

    @Override
    public void showDidYouKnow(Player player, String msg) {
        DidYouKnowHelper.CheckAndShowDidYouKnow(player, msg);
    }

    @Override
    public boolean playerHasAdvancement(Player player, ResourceLocation advancementID) {
        Minecraft mc = Minecraft.getInstance();
        ClientAdvancements advancements = mc.player.connection.getAdvancements();
        this.queryAdvancement = advancements.getAdvancements().get(advancementID);
        if (this.queryAdvancement == null) {
            return false;
        } else {
            this.progress = null;
            advancements.setListener(this);
            if (this.progress != null) {
                advancements.setListener(null);
                return this.progress.isDone();
            } else {
                return false;
            }
        }
    }

    @Override
    public void onAddAdvancementRoot(Advancement pAdvancement) {
    }

    @Override
    public void onAddAdvancementTask(Advancement pAdvancement) {
    }

    @Override
    public void onAdvancementsCleared() {
    }

    @Override
    public void onRemoveAdvancementRoot(Advancement pAdvancement) {
    }

    @Override
    public void onRemoveAdvancementTask(Advancement pAdvancement) {
    }

    @Override
    public void onSelectedTabChanged(Advancement pAdvancement) {
    }

    @Override
    public void onUpdateAdvancementProgress(Advancement pAdvancement, AdvancementProgress pProgress) {
        if (this.queryAdvancement == pAdvancement) {
            this.progress = pProgress;
        }
    }
}