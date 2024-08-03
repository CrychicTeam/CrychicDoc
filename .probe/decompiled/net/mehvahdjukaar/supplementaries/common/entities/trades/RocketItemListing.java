package net.mehvahdjukaar.supplementaries.common.entities.trades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.misc.StrOpt;
import net.mehvahdjukaar.moonlight.api.trades.ModItemListing;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public record RocketItemListing(ItemStack emeralds, ItemStack priceSecondary, int rockets, int maxTrades, int xp, float priceMult, int level) implements ModItemListing {

    public static final Codec<RocketItemListing> CODEC = RecordCodecBuilder.create(instance -> instance.group(ItemStack.CODEC.fieldOf("price").forGetter(RocketItemListing::emeralds), StrOpt.of(ItemStack.CODEC, "price_secondary", ItemStack.EMPTY).forGetter(RocketItemListing::priceSecondary), Codec.INT.fieldOf("amount").forGetter(RocketItemListing::rockets), StrOpt.of(ExtraCodecs.POSITIVE_INT, "max_trades", 16).forGetter(RocketItemListing::maxTrades), StrOpt.of(ExtraCodecs.POSITIVE_INT, "xp").forGetter(s -> Optional.of(s.xp)), StrOpt.of(ExtraCodecs.POSITIVE_FLOAT, "price_multiplier", 0.05F).forGetter(RocketItemListing::priceMult), StrOpt.of(Codec.intRange(1, 5), "level", 1).forGetter(RocketItemListing::level)).apply(instance, RocketItemListing::createDefault));

    public static RocketItemListing createDefault(ItemStack price, ItemStack price2, int rockets, int maxTrades, Optional<Integer> xp, float priceMult, int level) {
        return new RocketItemListing(price, price2, rockets, maxTrades, (Integer) xp.orElse(ModItemListing.defaultXp(false, level)), priceMult, level);
    }

    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource random) {
        ItemStack itemstack = new ItemStack(Items.FIREWORK_ROCKET, this.rockets);
        CompoundTag tag = itemstack.getOrCreateTagElement("Fireworks");
        ListTag listTag = new ListTag();
        int stars = 0;
        List<FireworkRocketItem.Shape> usedShapes = new ArrayList();
        do {
            listTag.add(StarItemListing.createRandomFireworkStar(random, usedShapes));
        } while (random.nextFloat() < 0.42F && ++stars < 7);
        tag.putByte("Flight", (byte) (random.nextInt(3) + 1));
        tag.put("Explosions", listTag);
        return new MerchantOffer(this.emeralds, this.priceSecondary, itemstack, this.maxTrades, ModItemListing.defaultXp(true, this.level), this.priceMult);
    }

    @Override
    public Codec<? extends ModItemListing> getCodec() {
        return CODEC;
    }

    @Override
    public int getLevel() {
        return this.level;
    }
}