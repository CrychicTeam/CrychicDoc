package net.blay09.mods.balm.common;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntityBase;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.provider.BalmProvider;
import net.blay09.mods.balm.api.provider.BalmProviderHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BalmBlockEntity extends BalmBlockEntityBase implements BalmProviderHolder {

    private final Map<Class<?>, BalmProvider<?>> providers = new HashMap();

    private final Map<Pair<Direction, Class<?>>, BalmProvider<?>> sidedProviders = new HashMap();

    private boolean providersInitialized;

    public BalmBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.createUpdateTag();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return this.createUpdatePacket();
    }

    @Override
    public <T> T getProvider(Class<T> clazz) {
        if (!this.providersInitialized) {
            List<Object> providers = new ArrayList();
            this.buildProviders(providers);
            for (Object holder : providers) {
                BalmProviderHolder providerHolder = (BalmProviderHolder) holder;
                for (BalmProvider<?> provider : providerHolder.getProviders()) {
                    this.providers.put(provider.getProviderClass(), provider);
                }
                for (Pair<Direction, BalmProvider<?>> pair : providerHolder.getSidedProviders()) {
                    Direction direction = (Direction) pair.getFirst();
                    BalmProvider<?> provider = (BalmProvider<?>) pair.getSecond();
                    this.sidedProviders.put(Pair.of(direction, provider.getProviderClass()), provider);
                }
            }
            this.providersInitialized = true;
        }
        BalmProvider<?> found = (BalmProvider<?>) this.providers.get(clazz);
        return (T) (found != null ? found.getInstance() : null);
    }

    @Override
    public void buildProviders(List<Object> providers) {
        providers.add(this);
        if (this instanceof BalmContainerProvider containerProvider) {
            providers.add(new BalmProviderHolder() {

                @Override
                public List<BalmProvider<?>> getProviders() {
                    Container container = containerProvider.getContainer();
                    return (List<BalmProvider<?>>) (container != null ? Lists.newArrayList(new BalmProvider[] { new BalmProvider<>(Container.class, container) }) : Collections.emptyList());
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    List<Pair<Direction, BalmProvider<?>>> providers = new ArrayList();
                    for (Direction direction : Direction.values()) {
                        Container container = containerProvider.getContainer(direction);
                        if (container != null) {
                            providers.add(Pair.of(direction, new BalmProvider<>(Container.class, container)));
                        }
                    }
                    return providers;
                }
            });
        }
        if (this instanceof BalmFluidTankProvider fluidTankProvider) {
            providers.add(new BalmProviderHolder() {

                @Override
                public List<BalmProvider<?>> getProviders() {
                    FluidTank fluidTank = fluidTankProvider.getFluidTank();
                    return (List<BalmProvider<?>>) (fluidTank != null ? Lists.newArrayList(new BalmProvider[] { new BalmProvider<>(FluidTank.class, fluidTank) }) : Collections.emptyList());
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    List<Pair<Direction, BalmProvider<?>>> providers = new ArrayList();
                    for (Direction direction : Direction.values()) {
                        FluidTank fluidTank = fluidTankProvider.getFluidTank(direction);
                        if (fluidTank != null) {
                            providers.add(Pair.of(direction, new BalmProvider<>(FluidTank.class, fluidTank)));
                        }
                    }
                    return providers;
                }
            });
        }
        if (this instanceof BalmEnergyStorageProvider energyStorageProvider) {
            providers.add(new BalmProviderHolder() {

                @Override
                public List<BalmProvider<?>> getProviders() {
                    EnergyStorage energyStorage = energyStorageProvider.getEnergyStorage();
                    return (List<BalmProvider<?>>) (energyStorage != null ? Lists.newArrayList(new BalmProvider[] { new BalmProvider<>(EnergyStorage.class, energyStorage) }) : Collections.emptyList());
                }

                @Override
                public List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
                    List<Pair<Direction, BalmProvider<?>>> providers = new ArrayList();
                    for (Direction direction : Direction.values()) {
                        EnergyStorage energyStorage = energyStorageProvider.getEnergyStorage(direction);
                        if (energyStorage != null) {
                            providers.add(Pair.of(direction, new BalmProvider<>(EnergyStorage.class, energyStorage)));
                        }
                    }
                    return providers;
                }
            });
        }
    }

    public void sync() {
        if (this.m_58904_() != null && !this.m_58904_().isClientSide) {
            ((ServerLevel) this.m_58904_()).getChunkSource().blockChanged(this.m_58899_());
        }
    }

    public Packet<ClientGamePacketListener> createUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, this::createUpdateTag);
    }

    public CompoundTag createUpdateTag() {
        return this.createUpdateTag(this);
    }

    private CompoundTag createUpdateTag(BlockEntity blockEntity) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BalmBlockEntity balmBlockEntity) {
            balmBlockEntity.writeUpdateTag(tag);
        }
        return tag;
    }

    protected void writeUpdateTag(CompoundTag tag) {
    }
}