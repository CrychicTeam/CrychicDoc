package noobanidus.mods.lootr.api;

import java.util.UUID;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import noobanidus.mods.lootr.api.inventory.ILootrInventory;

public interface ILootrAPI {

    boolean isFakePlayer(Player var1);

    default boolean clearPlayerLoot(ServerPlayer entity) {
        return this.clearPlayerLoot(entity.m_20148_());
    }

    boolean clearPlayerLoot(UUID var1);

    @Nullable
    ILootrInventory getInventory(Level var1, UUID var2, BlockPos var3, ServerPlayer var4, BaseContainerBlockEntity var5, LootFiller var6, Supplier<ResourceLocation> var7, LongSupplier var8);

    @Nullable
    ILootrInventory getInventory(Level var1, UUID var2, BlockPos var3, ServerPlayer var4, BaseContainerBlockEntity var5, LootFiller var6, Supplier<ResourceLocation> var7, LongSupplier var8, MenuBuilder var9);

    @Deprecated
    @Nullable
    default MenuProvider getModdedMenu(Level level, UUID id, BlockPos pos, ServerPlayer player, BaseContainerBlockEntity blockEntity, LootFiller filler, Supplier<ResourceLocation> tableSupplier, LongSupplier seedSupplier) {
        return this.getInventory(level, id, pos, player, blockEntity, filler, tableSupplier, seedSupplier);
    }

    @Deprecated
    @Nullable
    default MenuProvider getModdedMenu(Level level, UUID id, BlockPos pos, ServerPlayer player, BaseContainerBlockEntity blockEntity, LootFiller filler, Supplier<ResourceLocation> tableSupplier, LongSupplier seedSupplier, MenuBuilder builder) {
        return this.getInventory(level, id, pos, player, blockEntity, filler, tableSupplier, seedSupplier, builder);
    }

    @Nullable
    ILootrInventory getInventory(Level var1, UUID var2, BlockPos var3, ServerPlayer var4, IntSupplier var5, Supplier<Component> var6, LootFiller var7, Supplier<ResourceLocation> var8, LongSupplier var9);

    @Nullable
    ILootrInventory getInventory(Level var1, UUID var2, BlockPos var3, ServerPlayer var4, IntSupplier var5, Supplier<Component> var6, LootFiller var7, Supplier<ResourceLocation> var8, LongSupplier var9, MenuBuilder var10);

    @Deprecated
    @Nullable
    default MenuProvider getModdedMenu(Level level, UUID id, BlockPos pos, ServerPlayer player, IntSupplier sizeSupplier, Supplier<Component> displaySupplier, LootFiller filler, Supplier<ResourceLocation> tableSupplier, LongSupplier seedSupplier) {
        return this.getInventory(level, id, pos, player, sizeSupplier, displaySupplier, filler, tableSupplier, seedSupplier);
    }

    @Deprecated
    @Nullable
    default MenuProvider getModdedMenu(Level level, UUID id, BlockPos pos, ServerPlayer player, IntSupplier sizeSupplier, Supplier<Component> displaySupplier, LootFiller filler, Supplier<ResourceLocation> tableSupplier, LongSupplier seedSupplier, MenuBuilder builder) {
        return this.getInventory(level, id, pos, player, sizeSupplier, displaySupplier, filler, tableSupplier, seedSupplier, builder);
    }

    long getLootSeed(long var1);

    default boolean isSavingStructure() {
        return this.shouldDiscard();
    }

    boolean shouldDiscard();

    float getExplosionResistance(Block var1, float var2);

    float getDestroyProgress(BlockState var1, Player var2, BlockGetter var3, BlockPos var4, float var5);

    int getAnalogOutputSignal(BlockState var1, Level var2, BlockPos var3, int var4);

    default boolean hasCapacity(String capacity) {
        return switch(capacity) {
            case "structure_saving" ->
                true;
            case "should_discard_id_and_openers" ->
                true;
            case "capacities" ->
                true;
            case "explosion_resistance" ->
                true;
            case "destruction_progress" ->
                true;
            default ->
                false;
        };
    }
}