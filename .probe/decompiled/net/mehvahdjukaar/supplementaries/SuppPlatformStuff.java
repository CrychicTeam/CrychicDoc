package net.mehvahdjukaar.supplementaries;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.List;
import net.mehvahdjukaar.supplementaries.forge.SuppPlatformStuffImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SuppPlatformStuff {

    @ExpectPlatform
    @Transformed
    public static EntityType<?> getFishType(MobBucketItem bucketItem) {
        return SuppPlatformStuffImpl.getFishType(bucketItem);
    }

    @ExpectPlatform
    @Nullable
    @Contract
    @Transformed
    public static <T> T getForgeCap(@NotNull Object object, Class<T> capClass) {
        return SuppPlatformStuffImpl.getForgeCap(object, capClass);
    }

    @Nullable
    @Contract
    @ExpectPlatform
    @Transformed
    public static BlockState getUnoxidised(Level level, BlockPos pos, BlockState state) {
        return SuppPlatformStuffImpl.getUnoxidised(level, pos, state);
    }

    @ExpectPlatform
    @Transformed
    public static boolean isEndermanMask(EnderMan enderMan, Player player, ItemStack itemstack) {
        return SuppPlatformStuffImpl.isEndermanMask(enderMan, player, itemstack);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static int getItemLifeSpawn(ItemEntity itemEntity) {
        return SuppPlatformStuffImpl.getItemLifeSpawn(itemEntity);
    }

    @ExpectPlatform
    @Transformed
    public static void onItemPickup(Player player, ItemEntity itemEntity, ItemStack copy) {
        SuppPlatformStuffImpl.onItemPickup(player, itemEntity, copy);
    }

    @ExpectPlatform
    @Transformed
    public static CreativeModeTab.Builder searchBar(CreativeModeTab.Builder c) {
        return SuppPlatformStuffImpl.searchBar(c);
    }

    @ExpectPlatform
    @Transformed
    public static float getDownfall(Biome biome) {
        return SuppPlatformStuffImpl.getDownfall(biome);
    }

    @ExpectPlatform
    @Transformed
    public static VillagerTrades.ItemListing[] fireRedMerchantTradesEvent(List<VillagerTrades.ItemListing> listings) {
        return SuppPlatformStuffImpl.fireRedMerchantTradesEvent(listings);
    }

    @ExpectPlatform
    @Transformed
    public static void disableAMWarn() {
        SuppPlatformStuffImpl.disableAMWarn();
    }

    @ExpectPlatform
    @Transformed
    public static void disableOFWarn(boolean on) {
        SuppPlatformStuffImpl.disableOFWarn(on);
    }
}