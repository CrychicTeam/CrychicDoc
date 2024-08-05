package io.github.lightman314.lightmanscurrency.common.crafting.condition;

import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.LCConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class LCCraftingConditions {

    public static void register() {
        try {
            CraftingHelper.register(LCCraftingConditions.NetworkTrader.SERIALIZER);
            CraftingHelper.register(LCCraftingConditions.TraderInterface.SERIALIZER);
            CraftingHelper.register(LCCraftingConditions.AuctionStand.SERIALIZER);
            CraftingHelper.register(LCCraftingConditions.CoinChest.SERIALIZER);
            CraftingHelper.register(LCCraftingConditions.CoinChestUpgradeExchange.SERIALIZER);
            CraftingHelper.register(LCCraftingConditions.CoinChestUpgradeMagnet.SERIALIZER);
            CraftingHelper.register(LCCraftingConditions.CoinChestUpgradeSecurity.SERIALIZER);
            CraftingHelper.register(LCCraftingConditions.TaxCollector.SERIALIZER);
        } catch (IllegalStateException var1) {
        }
    }

    public static class AuctionStand extends SimpleCraftingCondition {

        public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "auction_stand_craftable");

        public static final LCCraftingConditions.AuctionStand INSTANCE = new LCCraftingConditions.AuctionStand();

        public static final IConditionSerializer<LCCraftingConditions.AuctionStand> SERIALIZER = new LCCraftingConditions.AuctionStand.Serializer();

        private AuctionStand() {
            super(TYPE, LCConfig.COMMON.canCraftAuctionStands);
        }

        private static class Serializer implements IConditionSerializer<LCCraftingConditions.AuctionStand> {

            public void write(JsonObject json, LCCraftingConditions.AuctionStand value) {
            }

            public LCCraftingConditions.AuctionStand read(JsonObject json) {
                return LCCraftingConditions.AuctionStand.INSTANCE;
            }

            @Override
            public ResourceLocation getID() {
                return LCCraftingConditions.AuctionStand.TYPE;
            }
        }
    }

    public static class CoinChest extends SimpleCraftingCondition {

        public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "coin_chest_craftable");

        public static final LCCraftingConditions.CoinChest INSTANCE = new LCCraftingConditions.CoinChest();

        public static final IConditionSerializer<LCCraftingConditions.CoinChest> SERIALIZER = new LCCraftingConditions.CoinChest.Serializer();

        private CoinChest() {
            super(TYPE, LCConfig.COMMON.canCraftCoinChest);
        }

        private static class Serializer implements IConditionSerializer<LCCraftingConditions.CoinChest> {

            public void write(JsonObject json, LCCraftingConditions.CoinChest value) {
            }

            public LCCraftingConditions.CoinChest read(JsonObject json) {
                return LCCraftingConditions.CoinChest.INSTANCE;
            }

            @Override
            public ResourceLocation getID() {
                return LCCraftingConditions.CoinChest.TYPE;
            }
        }
    }

    public static class CoinChestUpgradeExchange extends SimpleCraftingCondition {

        public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "coin_chest_exchange_craftable");

        public static final LCCraftingConditions.CoinChestUpgradeExchange INSTANCE = new LCCraftingConditions.CoinChestUpgradeExchange();

        public static final IConditionSerializer<LCCraftingConditions.CoinChestUpgradeExchange> SERIALIZER = new LCCraftingConditions.CoinChestUpgradeExchange.Serializer();

        private CoinChestUpgradeExchange() {
            super(TYPE, LCConfig.COMMON.canCraftCoinChestUpgradeExchange);
        }

        private static class Serializer implements IConditionSerializer<LCCraftingConditions.CoinChestUpgradeExchange> {

            public void write(JsonObject json, LCCraftingConditions.CoinChestUpgradeExchange value) {
            }

            public LCCraftingConditions.CoinChestUpgradeExchange read(JsonObject json) {
                return LCCraftingConditions.CoinChestUpgradeExchange.INSTANCE;
            }

            @Override
            public ResourceLocation getID() {
                return LCCraftingConditions.CoinChestUpgradeExchange.TYPE;
            }
        }
    }

    public static class CoinChestUpgradeMagnet extends SimpleCraftingCondition {

        public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "coin_chest_magnet_craftable");

        public static final LCCraftingConditions.CoinChestUpgradeMagnet INSTANCE = new LCCraftingConditions.CoinChestUpgradeMagnet();

        public static final IConditionSerializer<LCCraftingConditions.CoinChestUpgradeMagnet> SERIALIZER = new LCCraftingConditions.CoinChestUpgradeMagnet.Serializer();

        private CoinChestUpgradeMagnet() {
            super(TYPE, LCConfig.COMMON.canCraftCoinChestUpgradeMagnet);
        }

        private static class Serializer implements IConditionSerializer<LCCraftingConditions.CoinChestUpgradeMagnet> {

            public void write(JsonObject json, LCCraftingConditions.CoinChestUpgradeMagnet value) {
            }

            public LCCraftingConditions.CoinChestUpgradeMagnet read(JsonObject json) {
                return LCCraftingConditions.CoinChestUpgradeMagnet.INSTANCE;
            }

            @Override
            public ResourceLocation getID() {
                return LCCraftingConditions.CoinChestUpgradeMagnet.TYPE;
            }
        }
    }

    public static class CoinChestUpgradeSecurity extends SimpleCraftingCondition {

        public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "coin_chest_security_craftable");

        public static final LCCraftingConditions.CoinChestUpgradeSecurity INSTANCE = new LCCraftingConditions.CoinChestUpgradeSecurity();

        public static final IConditionSerializer<LCCraftingConditions.CoinChestUpgradeSecurity> SERIALIZER = new LCCraftingConditions.CoinChestUpgradeSecurity.Serializer();

        private CoinChestUpgradeSecurity() {
            super(TYPE, LCConfig.COMMON.canCraftCoinChestUpgradeSecurity);
        }

        private static class Serializer implements IConditionSerializer<LCCraftingConditions.CoinChestUpgradeSecurity> {

            public void write(JsonObject json, LCCraftingConditions.CoinChestUpgradeSecurity value) {
            }

            public LCCraftingConditions.CoinChestUpgradeSecurity read(JsonObject json) {
                return LCCraftingConditions.CoinChestUpgradeSecurity.INSTANCE;
            }

            @Override
            public ResourceLocation getID() {
                return LCCraftingConditions.CoinChestUpgradeSecurity.TYPE;
            }
        }
    }

    public static class NetworkTrader extends SimpleCraftingCondition {

        public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "network_trader_craftable");

        public static final LCCraftingConditions.NetworkTrader INSTANCE = new LCCraftingConditions.NetworkTrader();

        public static final IConditionSerializer<LCCraftingConditions.NetworkTrader> SERIALIZER = new LCCraftingConditions.NetworkTrader.Serializer();

        private NetworkTrader() {
            super(TYPE, LCConfig.COMMON.canCraftNetworkTraders);
        }

        private static class Serializer implements IConditionSerializer<LCCraftingConditions.NetworkTrader> {

            public void write(JsonObject json, LCCraftingConditions.NetworkTrader value) {
            }

            public LCCraftingConditions.NetworkTrader read(JsonObject json) {
                return LCCraftingConditions.NetworkTrader.INSTANCE;
            }

            @Override
            public ResourceLocation getID() {
                return LCCraftingConditions.NetworkTrader.TYPE;
            }
        }
    }

    public static class TaxCollector extends SimpleCraftingCondition {

        public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "tax_collector_craftable");

        public static final LCCraftingConditions.TaxCollector INSTANCE = new LCCraftingConditions.TaxCollector();

        public static final IConditionSerializer<LCCraftingConditions.TaxCollector> SERIALIZER = new LCCraftingConditions.TaxCollector.Serializer();

        private TaxCollector() {
            super(TYPE, LCConfig.COMMON.canCraftTaxBlock);
        }

        private static class Serializer implements IConditionSerializer<LCCraftingConditions.TaxCollector> {

            public void write(JsonObject json, LCCraftingConditions.TaxCollector value) {
            }

            public LCCraftingConditions.TaxCollector read(JsonObject json) {
                return LCCraftingConditions.TaxCollector.INSTANCE;
            }

            @Override
            public ResourceLocation getID() {
                return LCCraftingConditions.TaxCollector.TYPE;
            }
        }
    }

    public static class TraderInterface extends SimpleCraftingCondition {

        public static final ResourceLocation TYPE = new ResourceLocation("lightmanscurrency", "trader_interface_craftable");

        public static final LCCraftingConditions.TraderInterface INSTANCE = new LCCraftingConditions.TraderInterface();

        public static final IConditionSerializer<LCCraftingConditions.TraderInterface> SERIALIZER = new LCCraftingConditions.TraderInterface.Serializer();

        private TraderInterface() {
            super(TYPE, LCConfig.COMMON.canCraftTraderInterfaces);
        }

        private static class Serializer implements IConditionSerializer<LCCraftingConditions.TraderInterface> {

            public void write(JsonObject json, LCCraftingConditions.TraderInterface value) {
            }

            public LCCraftingConditions.TraderInterface read(JsonObject json) {
                return LCCraftingConditions.TraderInterface.INSTANCE;
            }

            @Override
            public ResourceLocation getID() {
                return LCCraftingConditions.TraderInterface.TYPE;
            }
        }
    }
}