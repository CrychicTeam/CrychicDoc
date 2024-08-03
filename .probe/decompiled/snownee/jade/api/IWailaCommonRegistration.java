package snownee.jade.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import snownee.jade.api.view.IServerExtensionProvider;

@NonExtendable
public interface IWailaCommonRegistration {

    void registerBlockDataProvider(IServerDataProvider<BlockAccessor> var1, Class<? extends BlockEntity> var2);

    void registerEntityDataProvider(IServerDataProvider<EntityAccessor> var1, Class<? extends Entity> var2);

    <T> void registerItemStorage(IServerExtensionProvider<T, ItemStack> var1, Class<? extends T> var2);

    <T> void registerFluidStorage(IServerExtensionProvider<T, CompoundTag> var1, Class<? extends T> var2);

    <T> void registerEnergyStorage(IServerExtensionProvider<T, CompoundTag> var1, Class<? extends T> var2);

    <T> void registerProgress(IServerExtensionProvider<T, CompoundTag> var1, Class<? extends T> var2);
}