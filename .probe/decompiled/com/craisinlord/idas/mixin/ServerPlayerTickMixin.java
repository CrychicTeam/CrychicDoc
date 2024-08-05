package com.craisinlord.idas.mixin;

import com.craisinlord.idas.IDAS;
import com.craisinlord.idas.IDASTags;
import com.craisinlord.idas.state.IStateCacheProvider;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ServerPlayer.class })
public abstract class ServerPlayerTickMixin extends Player {

    @Shadow
    public ServerGamePacketListenerImpl connection;

    @Shadow
    @Final
    public ServerPlayerGameMode gameMode;

    public ServerPlayerTickMixin(Level $$0, BlockPos $$1, float $$2, GameProfile $$3) {
        super($$0, $$1, $$2, $$3);
    }

    @Shadow
    public abstract ServerLevel serverLevel();

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    private void idas_playerTick(CallbackInfo info) {
        if (this.gameMode.isSurvival() && this.f_19797_ % 100 == 0) {
            BlockPos playerPos = this.m_20183_();
            StructureStart structureStart = this.serverLevel().structureManager().getStructureWithPieceAt(playerPos, IDASTags.APPLIES_MINING_FATIGUE);
            boolean isInLabyrinth = this.serverLevel().m_46749_(playerPos) && structureStart.isValid();
            if (!isInLabyrinth || !IDAS.CONFIG.general.applyMiningFatigue) {
                return;
            }
            boolean isCleared = ((IStateCacheProvider) this.serverLevel()).getStateCache().isCleared(structureStart.getChunkPos().getWorldPosition());
            if (isCleared) {
                return;
            }
            if (!this.m_21023_(MobEffects.DIG_SLOWDOWN) || this.m_21124_(MobEffects.DIG_SLOWDOWN).getAmplifier() < 2 || this.m_21124_(MobEffects.DIG_SLOWDOWN).getDuration() < 120) {
                if (!this.m_21023_(MobEffects.DIG_SLOWDOWN)) {
                    this.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ELDER_GUARDIAN_CURSE), SoundSource.HOSTILE, this.m_20185_(), this.m_20186_(), this.m_20189_(), 1.0F, 1.0F, this.serverLevel().getSeed()));
                }
                this.m_147207_(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 2), this);
            }
        }
    }
}