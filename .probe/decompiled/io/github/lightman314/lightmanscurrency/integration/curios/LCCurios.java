package io.github.lightman314.lightmanscurrency.integration.curios;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.core.ModEnchantments;
import io.github.lightman314.lightmanscurrency.common.gamerule.ModGameRules;
import io.github.lightman314.lightmanscurrency.common.items.PortableATMItem;
import io.github.lightman314.lightmanscurrency.common.items.PortableTerminalItem;
import io.github.lightman314.lightmanscurrency.common.menus.wallet.WalletMenuBase;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.capability.CurioItemCapability;

public class LCCurios {

    public static final String WALLET_SLOT = "wallet";

    private static ICuriosItemHandler lazyGetCuriosHelper(LivingEntity entity) {
        LazyOptional<ICuriosItemHandler> optional = CuriosApi.getCuriosHelper().getCuriosHandler(entity);
        return optional.isPresent() ? optional.orElseGet(() -> {
            throw new RuntimeException("Unexpected error occurred!");
        }) : null;
    }

    public static boolean hasWalletSlot(LivingEntity entity) {
        if (entity == null) {
            return false;
        } else {
            try {
                ICuriosItemHandler curiosHelper = lazyGetCuriosHelper(entity);
                if (curiosHelper != null) {
                    ICurioStacksHandler stacksHandler = (ICurioStacksHandler) curiosHelper.getStacksHandler("wallet").orElse(null);
                    return stacksHandler != null && stacksHandler.getSlots() > 0;
                }
            } catch (Throwable var3) {
                LightmansCurrency.LogError("Error checking curios wallet slot validity.", var3);
            }
            return false;
        }
    }

    public static ItemStack getCuriosWalletContents(LivingEntity entity) {
        try {
            ICuriosItemHandler curiosHelper = lazyGetCuriosHelper(entity);
            if (curiosHelper != null) {
                ICurioStacksHandler stacksHandler = (ICurioStacksHandler) curiosHelper.getStacksHandler("wallet").orElse(null);
                if (stacksHandler != null && stacksHandler.getSlots() > 0) {
                    return stacksHandler.getStacks().getStackInSlot(0);
                }
            }
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error getting wallet from curios wallet slot.", var3);
        }
        return ItemStack.EMPTY;
    }

    public static void setCuriosWalletContents(LivingEntity entity, ItemStack wallet) {
        try {
            ICuriosItemHandler curiosHelper = lazyGetCuriosHelper(entity);
            if (curiosHelper != null) {
                ICurioStacksHandler stacksHandler = (ICurioStacksHandler) curiosHelper.getStacksHandler("wallet").orElse(null);
                if (stacksHandler != null && stacksHandler.getSlots() > 0) {
                    stacksHandler.getStacks().setStackInSlot(0, wallet);
                }
            }
        } catch (Throwable var4) {
            LightmansCurrency.LogError("Error placing wallet into the curios wallet slot.", var4);
        }
    }

    public static boolean getCuriosWalletVisibility(LivingEntity entity) {
        try {
            ICuriosItemHandler curiosHelper = lazyGetCuriosHelper(entity);
            if (curiosHelper != null) {
                ICurioStacksHandler stacksHandler = (ICurioStacksHandler) curiosHelper.getStacksHandler("wallet").orElse(null);
                if (stacksHandler != null && stacksHandler.getSlots() > 0) {
                    return stacksHandler.getRenders().get(0);
                }
            }
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error getting wallet slot visibility from curios.", var3);
        }
        return false;
    }

    public static boolean hasPortableTerminal(LivingEntity entity) {
        try {
            ICuriosItemHandler curiosHelper = lazyGetCuriosHelper(entity);
            if (curiosHelper != null) {
                for (Entry<String, ICurioStacksHandler> entry : curiosHelper.getCurios().entrySet()) {
                    ICurioStacksHandler stacksHandler = (ICurioStacksHandler) entry.getValue();
                    if (stacksHandler != null) {
                        IDynamicStackHandler sh = stacksHandler.getStacks();
                        for (int i = 0; i < sh.getSlots(); i++) {
                            if (sh.getStackInSlot(i).getItem() instanceof PortableTerminalItem) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Throwable var7) {
            LightmansCurrency.LogError("Error checking for Portable Terminal from curios.", var7);
        }
        return false;
    }

    public static boolean hasPortableATM(LivingEntity entity) {
        try {
            ICuriosItemHandler curiosHelper = lazyGetCuriosHelper(entity);
            if (curiosHelper != null) {
                for (Entry<String, ICurioStacksHandler> entry : curiosHelper.getCurios().entrySet()) {
                    ICurioStacksHandler stacksHandler = (ICurioStacksHandler) entry.getValue();
                    if (stacksHandler != null) {
                        IDynamicStackHandler sh = stacksHandler.getStacks();
                        for (int i = 0; i < sh.getSlots(); i++) {
                            if (sh.getStackInSlot(i).getItem() instanceof PortableATMItem) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Throwable var7) {
            LightmansCurrency.LogError("Error checking for Portable Terminal from curios.", var7);
        }
        return false;
    }

    @Nullable
    public static ItemStack getMoneyMendingItem(LivingEntity entity) {
        try {
            ICuriosItemHandler curiosHelper = lazyGetCuriosHelper(entity);
            if (curiosHelper != null) {
                for (Entry<String, ICurioStacksHandler> entry : curiosHelper.getCurios().entrySet()) {
                    ICurioStacksHandler stacksHandler = (ICurioStacksHandler) entry.getValue();
                    if (stacksHandler != null) {
                        IDynamicStackHandler sh = stacksHandler.getStacks();
                        for (int i = 0; i < sh.getSlots(); i++) {
                            ItemStack item = sh.getStackInSlot(i);
                            if (EnchantmentHelper.getEnchantments(item).containsKey(ModEnchantments.MONEY_MENDING.get())) {
                                return item;
                            }
                        }
                    }
                }
            }
        } catch (Throwable var8) {
            LightmansCurrency.LogError("Error checking for Money Mending item from curios.", var8);
        }
        return null;
    }

    public static ICapabilityProvider createWalletProvider(final ItemStack stack) {
        try {
            return CurioItemCapability.createProvider(new ICurio() {

                @Override
                public ItemStack getStack() {
                    return stack;
                }

                @Nonnull
                @Override
                public ICurio.SoundInfo getEquipSound(SlotContext context) {
                    return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
                }

                @Override
                public boolean canEquipFromUse(SlotContext context) {
                    return false;
                }

                @Override
                public boolean canSync(SlotContext context) {
                    return true;
                }

                @Override
                public boolean canEquip(SlotContext context) {
                    return true;
                }

                @Override
                public boolean canUnequip(SlotContext context) {
                    if (context.entity() instanceof Player player && player.containerMenu instanceof WalletMenuBase menu) {
                        return !menu.isEquippedWallet();
                    }
                    return true;
                }

                @Nonnull
                @Override
                public ICurio.DropRule getDropRule(SlotContext context, DamageSource source, int lootingLevel, boolean recentlyHit) {
                    return ModGameRules.safeGetCustomBool(context.entity().m_9236_(), ModGameRules.KEEP_WALLET, false) ? ICurio.DropRule.ALWAYS_KEEP : ICurio.DropRule.DEFAULT;
                }
            });
        } catch (Throwable var2) {
            return null;
        }
    }
}