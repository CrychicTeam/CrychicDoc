package fuzs.puzzleslib.impl.capability.data;

import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import fuzs.puzzleslib.api.capability.v2.data.PlayerCapabilityKey;
import fuzs.puzzleslib.api.capability.v2.data.PlayerRespawnCopyStrategy;
import fuzs.puzzleslib.api.capability.v2.data.SyncStrategy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ForgePlayerCapabilityKey<C extends CapabilityComponent> extends ForgeCapabilityKey<C> implements PlayerCapabilityKey<C> {

    private boolean respawnStrategy;

    private SyncStrategy syncStrategy = SyncStrategy.MANUAL;

    public ForgePlayerCapabilityKey(ResourceLocation id, Class<C> componentClass, ForgeCapabilityKey.CapabilityTokenFactory<C> factory) {
        super(id, componentClass, factory);
    }

    public ForgePlayerCapabilityKey<C> setRespawnStrategy(PlayerRespawnCopyStrategy respawnStrategy) {
        if (this.respawnStrategy) {
            throw new IllegalStateException("Attempting to set new player respawn strategy when it has already been set");
        } else {
            this.respawnStrategy = true;
            MinecraftForge.EVENT_BUS.addListener(evt -> this.onPlayerClone(evt, respawnStrategy));
            return this;
        }
    }

    @Override
    void validateCapability() {
        super.validateCapability();
        if (!this.respawnStrategy) {
            throw new IllegalStateException("Player respawn strategy missing from capability %s".formatted(this.getId()));
        }
    }

    public ForgePlayerCapabilityKey<C> setSyncStrategy(SyncStrategy syncStrategy) {
        if (this.syncStrategy != SyncStrategy.MANUAL) {
            throw new IllegalStateException("Attempting to set new sync behaviour when it has already been set");
        } else {
            this.syncStrategy = syncStrategy;
            MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
            MinecraftForge.EVENT_BUS.addListener(this::onPlayerChangedDimension);
            if (syncStrategy == SyncStrategy.SELF_AND_TRACKING) {
                MinecraftForge.EVENT_BUS.addListener(this::onStartTracking);
            }
            return this;
        }
    }

    @Override
    public void syncToRemote(ServerPlayer player) {
        PlayerCapabilityKey.syncCapabilityToRemote(player, player, this.syncStrategy, this.orThrow(player), this.getId(), false);
    }

    private void onPlayerClone(PlayerEvent.Clone evt, PlayerRespawnCopyStrategy respawnStrategy) {
        evt.getOriginal().reviveCaps();
        this.maybeGet(evt.getOriginal()).ifPresent(oldCapability -> this.maybeGet(evt.getEntity()).ifPresent(newCapability -> respawnStrategy.copy(oldCapability, newCapability, !evt.isWasDeath(), evt.getEntity().m_9236_().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY))));
        evt.getOriginal().invalidateCaps();
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {
        Player player = evt.getEntity();
        this.maybeGet(player).ifPresent(capability -> PlayerCapabilityKey.syncCapabilityToRemote(player, (ServerPlayer) player, this.syncStrategy, capability, this.getId(), true));
    }

    private void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent evt) {
        Player player = evt.getEntity();
        this.maybeGet(player).ifPresent(capability -> PlayerCapabilityKey.syncCapabilityToRemote(player, (ServerPlayer) player, this.syncStrategy, capability, this.getId(), true));
    }

    private void onStartTracking(PlayerEvent.StartTracking evt) {
        this.maybeGet(evt.getTarget()).ifPresent(capability -> PlayerCapabilityKey.syncCapabilityToRemote(evt.getTarget(), (ServerPlayer) evt.getEntity(), SyncStrategy.SELF, capability, this.getId(), true));
    }
}