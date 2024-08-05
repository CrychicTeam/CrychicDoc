package io.github.lightman314.lightmanscurrency.common.items;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.capability.money.CapabilityMoneyViewer;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyViewer;
import io.github.lightman314.lightmanscurrency.common.capability.MixedCapabilityProvider;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.WalletCapability;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.WalletMoneyViewer;
import io.github.lightman314.lightmanscurrency.common.core.ModSounds;
import io.github.lightman314.lightmanscurrency.common.enchantments.WalletEnchantment;
import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletMenuBase;
import io.github.lightman314.lightmanscurrency.integration.curios.LCCurios;
import io.github.lightman314.lightmanscurrency.network.message.walletslot.SPacketSyncWallet;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class WalletItem extends Item {

    private static final SoundEvent emptyOpenSound = SoundEvents.ARMOR_EQUIP_LEATHER;

    private final ResourceLocation MODEL_TEXTURE;

    private final int level;

    private final int storageSize;

    public WalletItem(int level, int storageSize, String modelName, Item.Properties properties) {
        super(properties.stacksTo(1));
        this.level = level;
        this.storageSize = storageSize;
        WalletMenuBase.updateMaxWalletSlots(this.storageSize);
        this.MODEL_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/entity/" + modelName + ".png");
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        List<ICapabilityProvider> providers = new ArrayList();
        providers.add(CapabilityMoneyViewer.createProvider(new WalletMoneyViewer(stack)));
        if (LightmansCurrency.isCuriosLoaded()) {
            ICapabilityProvider temp = LCCurios.createWalletProvider(stack);
            if (temp != null) {
                providers.add(temp);
            }
        }
        return new MixedCapabilityProvider(providers);
    }

    public int getEnchantmentValue(ItemStack stack) {
        return 10;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return true;
    }

    public static boolean validWalletStack(ItemStack walletStack) {
        return walletStack.isEmpty() ? true : isWallet(walletStack.getItem());
    }

    public static boolean isWallet(ItemStack item) {
        return !item.isEmpty() && isWallet(item.getItem());
    }

    public static boolean isWallet(Item item) {
        return item instanceof WalletItem;
    }

    public static boolean CanExchange(WalletItem wallet) {
        return wallet == null ? false : wallet.level >= LCConfig.SERVER.walletExchangeLevel.get();
    }

    public static boolean CanPickup(WalletItem wallet) {
        return wallet == null ? false : wallet.level >= LCConfig.SERVER.walletPickupLevel.get();
    }

    public static boolean HasBankAccess(WalletItem wallet) {
        return wallet == null ? false : wallet.level >= LCConfig.SERVER.walletBankLevel.get();
    }

    public static int InventorySize(WalletItem wallet) {
        return wallet == null ? 0 : wallet.storageSize;
    }

    public static int InventorySize(ItemStack wallet) {
        return wallet.getItem() instanceof WalletItem ? InventorySize((WalletItem) wallet.getItem()) : 0;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        if (CanPickup(this)) {
            tooltip.add(LCText.TOOLTIP_WALLET_PICKUP.get().withStyle(ChatFormatting.YELLOW));
        }
        if (CanExchange(this)) {
            if (CanPickup(this)) {
                Component onOffText = getAutoExchange(stack) ? LCText.TOOLTIP_WALLET_EXCHANGE_AUTO_ON.get().withStyle(ChatFormatting.GREEN) : LCText.TOOLTIP_WALLET_EXCHANGE_AUTO_OFF.get().withStyle(ChatFormatting.RED);
                tooltip.add(LCText.TOOLTIP_WALLET_EXCHANGE_AUTO.get(onOffText).withStyle(ChatFormatting.YELLOW));
            } else {
                tooltip.add(LCText.TOOLTIP_WALLET_EXCHANGE_MANUAL.get().withStyle(ChatFormatting.YELLOW));
            }
        }
        if (HasBankAccess(this)) {
            tooltip.add(LCText.TOOLTIP_WALLET_BANK_ACCOUNT.get().withStyle(ChatFormatting.YELLOW));
        }
        WalletEnchantment.addWalletEnchantmentTooltips(tooltip, stack);
        if (!CoinAPI.API.NoDataAvailable()) {
            IMoneyViewer handler = CapabilityMoneyViewer.getCapability(stack);
            if (handler != null) {
                MoneyView contents = handler.getStoredMoney();
                tooltip.add(LCText.TOOLTIP_WALLET_STORED_MONEY.get());
                for (MoneyValue val : contents.allValues()) {
                    tooltip.add(val.getText().withStyle(ChatFormatting.DARK_GREEN));
                }
            }
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack wallet = player.m_21120_(hand);
        if (!world.isClientSide) {
            int walletSlot = GetWalletSlot(player.getInventory(), wallet);
            if (walletSlot >= 0) {
                if (player.m_6047_() && !LightmansCurrency.isCuriosValid(player)) {
                    boolean equippedWallet = false;
                    IWalletHandler walletHandler = WalletCapability.lazyGetWalletHandler(player);
                    if (walletHandler != null && walletHandler.getWallet().isEmpty()) {
                        walletHandler.setWallet(wallet);
                        player.m_21008_(hand, ItemStack.EMPTY);
                        new SPacketSyncWallet(player.m_19879_(), walletHandler.getWallet(), walletHandler.visible()).sendTo(player);
                        walletHandler.clean();
                        equippedWallet = true;
                    }
                    if (equippedWallet) {
                        walletSlot = -1;
                    }
                }
                WalletMenuBase.SafeOpenWalletMenu((ServerPlayer) player, walletSlot);
            } else {
                LightmansCurrency.LogError("Could not find the wallet in the players inventory!");
            }
        } else {
            player.m_9236_().playSound(player, player.m_20183_(), emptyOpenSound, SoundSource.PLAYERS, 0.75F, 1.25F + player.m_9236_().random.nextFloat() * 0.5F);
            if (!isEmpty(wallet)) {
                player.m_9236_().playSound(player, player.m_20183_(), ModSounds.COINS_CLINKING.get(), SoundSource.PLAYERS, 0.4F, 1.0F);
            }
        }
        return InteractionResultHolder.success(wallet);
    }

    public static boolean isEmpty(@Nonnull ItemStack wallet) {
        return getWalletInventory(wallet).isEmpty();
    }

    private static int GetWalletSlot(Inventory inventory, ItemStack wallet) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i) == wallet) {
                return i;
            }
        }
        return -1;
    }

    public static ItemStack PickupCoin(ItemStack wallet, ItemStack coins) {
        Container inventory = getWalletInventory(wallet);
        ItemStack returnValue = InventoryUtil.TryPutItemStack(inventory, coins);
        if (getAutoExchange(wallet)) {
            CoinAPI.API.CoinExchangeAllUp(inventory);
        }
        CoinAPI.API.SortCoinsByValue(inventory);
        putWalletInventory(wallet, inventory);
        return returnValue;
    }

    public static void putWalletInventory(@Nonnull ItemStack wallet, @Nonnull Container inventory) {
        if (wallet.getItem() instanceof WalletItem) {
            InventoryUtil.saveAllItems("Items", wallet.getOrCreateTag(), inventory);
        }
    }

    @Nonnull
    public static SimpleContainer getWalletInventory(@Nonnull ItemStack wallet) {
        if (!(wallet.getItem() instanceof WalletItem)) {
            return new SimpleContainer();
        } else {
            CompoundTag compound = wallet.getOrCreateTag();
            int inventorySize = InventorySize(wallet);
            return !compound.contains("Items") ? new SimpleContainer(inventorySize) : InventoryUtil.loadAllItems("Items", wallet.getOrCreateTag(), inventorySize);
        }
    }

    public static boolean getAutoExchange(ItemStack wallet) {
        if (!(wallet.getItem() instanceof WalletItem)) {
            return false;
        } else if (CanExchange((WalletItem) wallet.getItem()) && CanPickup((WalletItem) wallet.getItem())) {
            CompoundTag tag = wallet.getOrCreateTag();
            if (!tag.contains("AutoConvert")) {
                tag.putBoolean("AutoConvert", true);
                return true;
            } else {
                return tag.getBoolean("AutoConvert");
            }
        } else {
            return false;
        }
    }

    public static void toggleAutoExchange(ItemStack wallet) {
        if (wallet.getItem() instanceof WalletItem) {
            if (CanExchange((WalletItem) wallet.getItem())) {
                CompoundTag tag = wallet.getOrCreateTag();
                boolean oldValue = getAutoExchange(wallet);
                tag.putBoolean("AutoConvert", !oldValue);
            }
        }
    }

    public static void QuickCollect(Player player, Container container, boolean allowSideChain) {
        ItemStack wallet = CoinAPI.API.getEquippedWallet(player);
        if (isWallet(wallet)) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (CoinAPI.API.IsCoin(stack, allowSideChain)) {
                    stack = PickupCoin(wallet, stack);
                    container.setItem(i, stack);
                }
            }
        }
    }

    public ResourceLocation getModelTexture() {
        return this.MODEL_TEXTURE;
    }
}