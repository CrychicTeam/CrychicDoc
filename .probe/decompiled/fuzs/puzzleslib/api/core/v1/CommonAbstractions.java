package fuzs.puzzleslib.api.core.v1;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface CommonAbstractions {

    CommonAbstractions INSTANCE = ServiceProviderHelper.load(CommonAbstractions.class);

    MinecraftServer getMinecraftServer();

    default void openMenu(ServerPlayer player, MenuProvider menuProvider) {
        this.openMenu(player, menuProvider, (serverPlayer, buf) -> {
        });
    }

    void openMenu(ServerPlayer var1, MenuProvider var2, BiConsumer<ServerPlayer, FriendlyByteBuf> var3);

    boolean isBossMob(EntityType<?> var1);

    float getEnchantPowerBonus(BlockState var1, Level var2, BlockPos var3);

    boolean canEquip(ItemStack var1, EquipmentSlot var2, Entity var3);

    int getMobLootingLevel(Entity var1, @Nullable Entity var2, @Nullable DamageSource var3);

    boolean getMobGriefingRule(Level var1, @Nullable Entity var2);

    void onPlayerDestroyItem(Player var1, ItemStack var2, @Nullable InteractionHand var3);

    @Nullable
    MobSpawnType getMobSpawnType(Mob var1);

    Pack.Info createPackInfo(ResourceLocation var1, Component var2, int var3, FeatureFlagSet var4, boolean var5);

    boolean canApplyAtEnchantingTable(Enchantment var1, ItemStack var2);

    boolean isAllowedOnBooks(Enchantment var1);
}