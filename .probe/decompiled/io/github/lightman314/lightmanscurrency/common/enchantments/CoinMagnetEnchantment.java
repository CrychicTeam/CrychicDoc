package io.github.lightman314.lightmanscurrency.common.enchantments;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.core.ModEnchantments;
import io.github.lightman314.lightmanscurrency.common.core.ModSounds;
import io.github.lightman314.lightmanscurrency.common.items.WalletItem;
import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletMenuBase;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class CoinMagnetEnchantment extends WalletEnchantment {

    public CoinMagnetEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, LCEnchantmentCategories.WALLET_PICKUP_CATEGORY, slots);
    }

    @Override
    public int getMinCost(int level) {
        return 5 + (level - 1) * 8;
    }

    @Override
    public int getMaxCost(int level) {
        return super.m_6183_(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public static void runEntityTick(@Nonnull IWalletHandler walletHandler, @Nonnull LivingEntity entity) {
        if (!entity.m_5833_()) {
            ItemStack wallet = walletHandler.getWallet();
            if (WalletItem.isWallet(wallet) && WalletItem.CanPickup((WalletItem) wallet.getItem())) {
                int enchantLevel = wallet.getEnchantmentLevel(ModEnchantments.COIN_MAGNET.get());
                if (enchantLevel > 0) {
                    float range = getCollectionRange(enchantLevel);
                    Level level = entity.m_9236_();
                    AABB searchBox = new AABB(entity.f_19854_ - (double) range, entity.f_19855_ - (double) range, entity.f_19856_ - (double) range, entity.f_19854_ + (double) range, entity.f_19855_ + (double) range, entity.f_19856_ + (double) range);
                    boolean updateWallet = false;
                    for (Entity e : level.getEntities(entity, searchBox, CoinMagnetEnchantment::coinMagnetEntityFilter)) {
                        ItemEntity ie = (ItemEntity) e;
                        ItemStack coinStack = ie.getItem();
                        ItemStack leftovers = WalletItem.PickupCoin(wallet, coinStack);
                        if (!InventoryUtil.ItemsFullyMatch(leftovers, coinStack)) {
                            updateWallet = true;
                            if (leftovers.isEmpty()) {
                                ie.m_146870_();
                            } else {
                                ie.setItem(leftovers);
                            }
                            level.playSound(null, entity, ModSounds.COINS_CLINKING.get(), SoundSource.PLAYERS, 0.4F, 1.0F);
                        }
                    }
                    if (updateWallet) {
                        walletHandler.setWallet(wallet);
                        WalletMenuBase.OnWalletUpdated(entity);
                    }
                }
            }
        }
    }

    public static boolean coinMagnetEntityFilter(Entity entity) {
        return entity instanceof ItemEntity item ? CoinAPI.API.IsCoin(item.getItem(), false) : false;
    }

    public static float getCollectionRange(int enchantLevel) {
        enchantLevel--;
        return enchantLevel < 0 ? 0.0F : (float) (LCConfig.SERVER.coinMagnetBaseRange.get() + LCConfig.SERVER.coinMagnetLeveledRange.get() * Math.min(enchantLevel, LCConfig.SERVER.coinMagnetCalculationCap.get()));
    }

    public static Component getCollectionRangeDisplay(int enchantLevel) {
        float range = getCollectionRange(enchantLevel);
        String display = range % 1.0F > 0.0F ? String.valueOf(range) : String.valueOf(Math.round(range));
        return Component.literal(display).withStyle(ChatFormatting.GREEN);
    }

    @Override
    public void addWalletTooltips(List<Component> tooltips, int enchantLevel, ItemStack wallet) {
        if (wallet.getItem() instanceof WalletItem && enchantLevel > 0 && WalletItem.CanPickup((WalletItem) wallet.getItem())) {
            tooltips.add(LCText.TOOLTIP_WALLET_PICKUP_MAGNET.get(getCollectionRangeDisplay(enchantLevel)).withStyle(ChatFormatting.YELLOW));
        }
    }
}