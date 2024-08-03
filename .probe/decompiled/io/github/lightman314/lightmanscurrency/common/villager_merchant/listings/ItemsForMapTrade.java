package io.github.lightman314.lightmanscurrency.common.villager_merchant.listings;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.common.villager_merchant.ItemListingSerializer;
import io.github.lightman314.lightmanscurrency.util.EnumUtil;
import io.github.lightman314.lightmanscurrency.util.FileUtil;
import javax.annotation.Nonnull;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class ItemsForMapTrade implements VillagerTrades.ItemListing {

    public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "items_for_map");

    public static final ItemsForMapTrade.Serializer SERIALIZER = new ItemsForMapTrade.Serializer();

    protected final ItemStack price1;

    protected final ItemStack price2;

    protected final TagKey<Structure> destination;

    protected final String displayName;

    protected final MapDecoration.Type mapDecorationType;

    protected final int maxTrades;

    protected final int xp;

    protected final float priceMult;

    public ItemsForMapTrade(ItemStack price, TagKey<Structure> destination, String displayName, MapDecoration.Type mapDecorationType, int maxUses, int xpValue) {
        this(price, ItemStack.EMPTY, destination, displayName, mapDecorationType, maxUses, xpValue, 0.05F);
    }

    public ItemsForMapTrade(ItemStack price1, ItemStack price2, TagKey<Structure> destination, String displayName, MapDecoration.Type mapDecorationType, int maxUses, int xpValue, float priceMult) {
        this.price1 = price1;
        this.price2 = price2;
        this.destination = destination;
        this.displayName = displayName;
        this.mapDecorationType = mapDecorationType;
        this.maxTrades = maxUses;
        this.xp = xpValue;
        this.priceMult = priceMult;
    }

    @Override
    public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull RandomSource rand) {
        if (trader != null && trader.level() instanceof ServerLevel serverworld) {
            BlockPos blockPos = serverworld.findNearestMapStructure(this.destination, trader.blockPosition(), 100, true);
            if (blockPos != null) {
                ItemStack itemstack = MapItem.create(serverworld, blockPos.m_123341_(), blockPos.m_123343_(), (byte) 2, true, true);
                MapItem.lockMap(serverworld, itemstack);
                MapItemSavedData.addTargetDecoration(itemstack, blockPos, "+", this.mapDecorationType);
                itemstack.setHoverName(Component.translatable(this.displayName));
                return new MerchantOffer(this.price1, this.price2, itemstack, this.maxTrades, this.xp, this.priceMult);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static class Serializer implements ItemListingSerializer.IItemListingSerializer, ItemListingSerializer.IItemListingDeserializer {

        private Serializer() {
        }

        @Override
        public ResourceLocation getType() {
            return ItemsForMapTrade.TYPE;
        }

        @Override
        public JsonObject serializeInternal(JsonObject json, VillagerTrades.ItemListing trade) {
            if (trade instanceof ItemsForMapTrade t) {
                json.add("Price", FileUtil.convertItemStack(t.price1));
                if (!t.price2.isEmpty()) {
                    json.add("Price2", FileUtil.convertItemStack(t.price2));
                }
                json.addProperty("Destination", t.destination.location().toString());
                json.addProperty("MapName", t.displayName);
                json.addProperty("Decoration", t.mapDecorationType.toString());
                json.addProperty("MaxTrades", t.maxTrades);
                json.addProperty("XP", t.xp);
                json.addProperty("PriceMult", t.priceMult);
                return json;
            } else {
                return null;
            }
        }

        @Override
        public VillagerTrades.ItemListing deserialize(JsonObject json) throws JsonSyntaxException, ResourceLocationException {
            ItemStack price1 = FileUtil.parseItemStack(GsonHelper.getAsJsonObject(json, "Price"));
            ItemStack price2 = json.has("Price2") ? FileUtil.parseItemStack(GsonHelper.getAsJsonObject(json, "Price2")) : ItemStack.EMPTY;
            TagKey<Structure> destination = TagKey.create(Registries.STRUCTURE, new ResourceLocation(GsonHelper.getAsString(json, "Destination")));
            String displayName = GsonHelper.getAsString(json, "MapName");
            MapDecoration.Type mapDecorationType = EnumUtil.enumFromString(GsonHelper.getAsString(json, "Decoration"), MapDecoration.Type.values(), MapDecoration.Type.FRAME);
            int maxTrades = GsonHelper.getAsInt(json, "MaxTrades");
            int xp = GsonHelper.getAsInt(json, "XP");
            float priceMult = GsonHelper.getAsFloat(json, "PriceMult");
            return new ItemsForMapTrade(price1, price2, destination, displayName, mapDecorationType, maxTrades, xp, priceMult);
        }
    }
}