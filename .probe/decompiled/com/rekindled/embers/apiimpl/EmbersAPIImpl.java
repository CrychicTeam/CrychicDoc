package com.rekindled.embers.apiimpl;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.EmbersAPI;
import com.rekindled.embers.api.IEmbersAPI;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.augment.ShiftingScalesAugment;
import com.rekindled.embers.network.PacketHandler;
import com.rekindled.embers.network.message.MessageScalesData;
import com.rekindled.embers.util.EmberGenUtil;
import com.rekindled.embers.util.EmberInventoryUtil;
import com.rekindled.embers.util.Misc;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;

public class EmbersAPIImpl implements IEmbersAPI {

    public static void init() {
        EmbersAPI.IMPL = new EmbersAPIImpl();
        AugmentUtil.IMPL = new AugmentUtilImpl();
        UpgradeUtil.IMPL = new UpgradeUtilImpl();
    }

    @Override
    public float getEmberDensity(long seed, int x, int z) {
        return EmberGenUtil.getEmberDensity(seed, x, z);
    }

    @Override
    public float getEmberStability(long seed, int x, int z) {
        return EmberGenUtil.getEmberStability(seed, x, z);
    }

    @Override
    public void registerLinkingHammer(Item item) {
        Misc.IS_HOLDING_HAMMER.add((BiPredicate) (player, hand) -> player.m_21120_(hand).getItem() == item);
    }

    @Override
    public void registerLinkingHammer(BiPredicate<Player, InteractionHand> predicate) {
        Misc.IS_HOLDING_HAMMER.add(predicate);
    }

    @Override
    public void registerHammerTargetGetter(Item item) {
        Misc.GET_HAMMER_TARGET.add((Function) player -> {
            ItemStack stack = player.m_21205_();
            if (stack.getItem() != item) {
                stack = player.m_21206_();
            }
            if (stack.getItem() == item && stack.hasTag()) {
                CompoundTag nbt = stack.getTag();
                if (stack.hasTag() && nbt.contains("targetWorld") && player.m_9236_().dimension().location().toString().equals(nbt.getString("targetWorld"))) {
                    return Pair.of(new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ")), Direction.byName(nbt.getString("targetFace")));
                }
            }
            return null;
        });
    }

    @Override
    public void registerHammerTargetGetter(Function<Player, Pair<BlockPos, Direction>> predicate) {
        Misc.GET_HAMMER_TARGET.add(predicate);
    }

    @Override
    public boolean isHoldingHammer(Player player, InteractionHand hand) {
        return Misc.isHoldingHammer(player, hand);
    }

    @Override
    public Pair<BlockPos, Direction> getHammerTarget(Player player) {
        return Misc.getHammerTarget(player);
    }

    @Override
    public void registerLens(Ingredient ingredient) {
        Misc.IS_WEARING_LENS.add((Predicate) player -> ingredient.test(player.m_21205_()) || ingredient.test(player.m_21206_()));
    }

    @Override
    public void registerWearableLens(Ingredient ingredient) {
        Misc.IS_WEARING_LENS.add((Predicate) player -> ingredient.test(player.getInventory().armor.get(EquipmentSlot.HEAD.getIndex())) ? AugmentUtil.getAugmentLevel(player.getInventory().armor.get(EquipmentSlot.HEAD.getIndex()), RegistryManager.SMOKY_LENS_AUGMENT) < 1 : false);
    }

    @Override
    public void registerLens(Predicate<Player> predicate) {
        Misc.IS_WEARING_LENS.add(predicate);
    }

    @Override
    public boolean isWearingLens(Player player) {
        return Misc.isWearingLens(player);
    }

    @Override
    public void registerEmberResonance(Ingredient ingredient, double resonance) {
        Misc.GET_EMBER_RESONANCE.add((Function) stack -> ingredient.test(stack) ? resonance : -1.0);
    }

    @Override
    public double getEmberResonance(ItemStack stack) {
        return Misc.getEmberResonance(stack);
    }

    @Override
    public double getEmberTotal(Player player) {
        return EmberInventoryUtil.getEmberTotal(player);
    }

    @Override
    public double getEmberCapacityTotal(Player player) {
        return EmberInventoryUtil.getEmberCapacityTotal(player);
    }

    @Override
    public void removeEmber(Player player, double amount) {
        EmberInventoryUtil.removeEmber(player, amount);
    }

    @Override
    public Item getTaggedItem(TagKey<Item> tag) {
        return Misc.getTaggedItem(tag);
    }

    @Override
    public double getScales(LivingEntity entity) {
        ShiftingScalesAugment.IScalesCapability cap = (ShiftingScalesAugment.IScalesCapability) entity.getCapability(ShiftingScalesAugment.ScalesCapabilityProvider.scalesCapability).orElse(null);
        return cap != null ? cap.getScales() : 0.0;
    }

    @Override
    public void setScales(LivingEntity entity, double scales) {
        ShiftingScalesAugment.IScalesCapability cap = (ShiftingScalesAugment.IScalesCapability) entity.getCapability(ShiftingScalesAugment.ScalesCapabilityProvider.scalesCapability).orElse(null);
        if (cap != null) {
            if (entity instanceof ServerPlayer player && cap.getScales() != scales) {
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MessageScalesData(scales));
            }
            cap.setScales(scales);
        }
    }
}