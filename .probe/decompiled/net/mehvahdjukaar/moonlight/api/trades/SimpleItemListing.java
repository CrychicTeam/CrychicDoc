package net.mehvahdjukaar.moonlight.api.trades;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import net.mehvahdjukaar.moonlight.api.misc.StrOpt;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record SimpleItemListing(ItemStack price, ItemStack price2, ItemStack offer, int maxTrades, int xp, float priceMult, int level, LootItemFunction func) implements ModItemListing {

    public static final Codec<SimpleItemListing> CODEC = RecordCodecBuilder.create(instance -> instance.group(ItemStack.CODEC.fieldOf("price").forGetter(SimpleItemListing::price), StrOpt.of(ItemStack.CODEC, "price_secondary", ItemStack.EMPTY).forGetter(SimpleItemListing::price2), ItemStack.CODEC.fieldOf("offer").forGetter(SimpleItemListing::offer), StrOpt.of(ExtraCodecs.POSITIVE_INT, "max_trades", 16).forGetter(SimpleItemListing::maxTrades), StrOpt.of(ExtraCodecs.POSITIVE_INT, "xp").forGetter(s -> Optional.of(s.xp)), StrOpt.of(ExtraCodecs.POSITIVE_FLOAT, "price_multiplier", 0.05F).forGetter(SimpleItemListing::priceMult), StrOpt.of(Codec.intRange(1, 5), "level", 1).forGetter(SimpleItemListing::level)).apply(instance, SimpleItemListing::createDefault));

    public SimpleItemListing(ItemStack price, ItemStack forSale, int maxTrades, int xp, float priceMult) {
        this(price, ItemStack.EMPTY, forSale, maxTrades, xp, priceMult, 1, null);
    }

    public SimpleItemListing(int emeralds, ItemStack forSale, int maxTrades, int xp, float mult) {
        this(new ItemStack(Items.EMERALD, emeralds), forSale, maxTrades, xp, mult);
    }

    public SimpleItemListing(int emeralds, ItemStack forSale, int maxTrades, int xp) {
        this(new ItemStack(Items.EMERALD, emeralds), forSale, maxTrades, xp, 0.05F);
    }

    public static SimpleItemListing createDefault(ItemStack price, ItemStack price2, ItemStack offer, int maxTrades, Optional<Integer> xp, float priceMult, int level) {
        boolean buying = offer.is(Items.EMERALD);
        return new SimpleItemListing(price, price2, offer, maxTrades, (Integer) xp.orElse(ModItemListing.defaultXp(buying, level)), priceMult, level, null);
    }

    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
        AtomicReference<ItemStack> stack = new AtomicReference();
        stack.set(this.offer);
        if (this.func != null && entity.level() instanceof ServerLevel serverLevel) {
            LootParams lootParams = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.THIS_ENTITY, entity).create(LootContextParamSets.GIFT);
            LootContext context = new LootContext.Builder(lootParams).create(Moonlight.res("trading_sequence"));
            LootItemFunction.decorate(this.func, stack::set, context).accept(this.offer.copy());
        }
        return new MerchantOffer(this.price, this.price2, (ItemStack) stack.get(), this.maxTrades, this.xp, this.priceMult);
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public Codec<SimpleItemListing> getCodec() {
        return CODEC;
    }
}