package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerMagicProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<MagicData> PLAYER_MAGIC = CapabilityManager.get(new CapabilityToken<MagicData>() {
    });

    private MagicData playerMagicData = null;

    private final LazyOptional<MagicData> opt = LazyOptional.of(this::createPlayerMagicData);

    private ServerPlayer serverPlayer;

    public PlayerMagicProvider(ServerPlayer serverPlayer) {
        this.serverPlayer = serverPlayer;
    }

    @Nonnull
    private MagicData createPlayerMagicData() {
        if (this.playerMagicData == null) {
            this.playerMagicData = new MagicData(this.serverPlayer);
        }
        return this.playerMagicData;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == PLAYER_MAGIC ? this.opt.cast() : LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return this.getCapability(cap);
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        this.createPlayerMagicData().saveNBTData(nbt);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.createPlayerMagicData().loadNBTData(nbt);
    }
}