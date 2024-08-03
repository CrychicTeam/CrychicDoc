package com.simibubi.create.content.equipment.armor;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class BacktankUtil {

    private static final List<Function<LivingEntity, List<ItemStack>>> BACKTANK_SUPPLIERS = new ArrayList();

    public static List<ItemStack> getAllWithAir(LivingEntity entity) {
        List<ItemStack> all = new ArrayList();
        for (Function<LivingEntity, List<ItemStack>> supplier : BACKTANK_SUPPLIERS) {
            for (ItemStack stack : (List) supplier.apply(entity)) {
                if (hasAirRemaining(stack)) {
                    all.add(stack);
                }
            }
        }
        all.sort((a, b) -> Float.compare(getAir(a), getAir(b)));
        return all;
    }

    public static boolean hasAirRemaining(ItemStack backtank) {
        return getAir(backtank) > 0.0F;
    }

    public static float getAir(ItemStack backtank) {
        CompoundTag tag = backtank.getOrCreateTag();
        return Math.min(tag.getFloat("Air"), (float) maxAir(backtank));
    }

    public static void consumeAir(LivingEntity entity, ItemStack backtank, float i) {
        CompoundTag tag = backtank.getOrCreateTag();
        int maxAir = maxAir(backtank);
        float air = getAir(backtank);
        float newAir = Math.max(air - i, 0.0F);
        tag.putFloat("Air", Math.min(newAir, (float) maxAir));
        backtank.setTag(tag);
        if (entity instanceof ServerPlayer player) {
            sendWarning(player, air, newAir, (float) maxAir / 10.0F);
            sendWarning(player, air, newAir, 1.0F);
        }
    }

    private static void sendWarning(ServerPlayer player, float air, float newAir, float threshold) {
        if (!(newAir > threshold)) {
            if (!(air <= threshold)) {
                boolean depleted = threshold == 1.0F;
                MutableComponent component = Lang.translateDirect(depleted ? "backtank.depleted" : "backtank.low");
                AllSoundEvents.DENY.play(player.m_9236_(), null, player.m_20183_(), 1.0F, 1.25F);
                AllSoundEvents.STEAM.play(player.m_9236_(), null, player.m_20183_(), 0.5F, 0.5F);
                player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 40, 10));
                player.connection.send(new ClientboundSetSubtitleTextPacket(Components.literal("⚠ ").withStyle(depleted ? ChatFormatting.RED : ChatFormatting.GOLD).append(component.withStyle(ChatFormatting.GRAY))));
                player.connection.send(new ClientboundSetTitleTextPacket(Components.immutableEmpty()));
            }
        }
    }

    public static int maxAir(ItemStack backtank) {
        return maxAir(backtank.getEnchantmentLevel((Enchantment) AllEnchantments.CAPACITY.get()));
    }

    public static int maxAir(int enchantLevel) {
        return AllConfigs.server().equipment.airInBacktank.get() + AllConfigs.server().equipment.enchantedBacktankCapacity.get() * enchantLevel;
    }

    public static int maxAirWithoutEnchants() {
        return AllConfigs.server().equipment.airInBacktank.get();
    }

    public static boolean canAbsorbDamage(LivingEntity entity, int usesPerTank) {
        if (usesPerTank == 0) {
            return true;
        } else if (entity instanceof Player && ((Player) entity).isCreative()) {
            return true;
        } else {
            List<ItemStack> backtanks = getAllWithAir(entity);
            if (backtanks.isEmpty()) {
                return false;
            } else {
                float cost = (float) maxAirWithoutEnchants() / (float) usesPerTank;
                consumeAir(entity, (ItemStack) backtanks.get(0), cost);
                return true;
            }
        }
    }

    public static boolean isBarVisible(ItemStack stack, int usesPerTank) {
        if (usesPerTank == 0) {
            return false;
        } else {
            Player player = (Player) DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player);
            if (player == null) {
                return false;
            } else {
                List<ItemStack> backtanks = getAllWithAir(player);
                return backtanks.isEmpty() ? stack.isDamaged() : true;
            }
        }
    }

    public static int getBarWidth(ItemStack stack, int usesPerTank) {
        if (usesPerTank == 0) {
            return 13;
        } else {
            Player player = (Player) DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player);
            if (player == null) {
                return 13;
            } else {
                List<ItemStack> backtanks = getAllWithAir(player);
                if (backtanks.isEmpty()) {
                    return Math.round(13.0F - (float) stack.getDamageValue() / (float) stack.getMaxDamage() * 13.0F);
                } else if (backtanks.size() == 1) {
                    return ((ItemStack) backtanks.get(0)).getItem().getBarWidth((ItemStack) backtanks.get(0));
                } else {
                    int sumBarWidth = (Integer) backtanks.stream().map(backtank -> backtank.getItem().getBarWidth(backtank)).reduce(0, Integer::sum);
                    return Math.round((float) sumBarWidth / (float) backtanks.size());
                }
            }
        }
    }

    public static int getBarColor(ItemStack stack, int usesPerTank) {
        if (usesPerTank == 0) {
            return 0;
        } else {
            Player player = (Player) DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player);
            if (player == null) {
                return 0;
            } else {
                List<ItemStack> backtanks = getAllWithAir(player);
                return backtanks.isEmpty() ? Mth.hsvToRgb(Math.max(0.0F, 1.0F - (float) stack.getDamageValue() / (float) stack.getMaxDamage()) / 3.0F, 1.0F, 1.0F) : ((ItemStack) backtanks.get(0)).getItem().getBarColor((ItemStack) backtanks.get(0));
            }
        }
    }

    public static void addBacktankSupplier(Function<LivingEntity, List<ItemStack>> supplier) {
        BACKTANK_SUPPLIERS.add(supplier);
    }

    static {
        addBacktankSupplier(entity -> {
            List<ItemStack> stacks = new ArrayList();
            for (ItemStack itemStack : entity.getArmorSlots()) {
                if (AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES.matches(itemStack)) {
                    stacks.add(itemStack);
                }
            }
            return stacks;
        });
    }
}