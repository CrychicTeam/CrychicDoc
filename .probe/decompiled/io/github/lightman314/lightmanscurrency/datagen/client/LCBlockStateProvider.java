package io.github.lightman314.lightmanscurrency.datagen.client;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.ITallBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IWideBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.PaygateBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.common.core.variants.Color;
import io.github.lightman314.lightmanscurrency.common.core.variants.WoodType;
import io.github.lightman314.lightmanscurrency.datagen.util.ColorHelper;
import io.github.lightman314.lightmanscurrency.datagen.util.WoodData;
import java.util.Objects;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LCBlockStateProvider extends BlockStateProvider {

    public LCBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, "lightmanscurrency", exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.registerBasicItem(ModItems.COIN_COPPER);
        this.registerBasicItem(ModItems.COIN_IRON);
        this.registerBasicItem(ModItems.COIN_GOLD);
        this.registerBasicItem(ModItems.COIN_EMERALD);
        this.registerBasicItem(ModItems.COIN_DIAMOND);
        this.registerBasicItem(ModItems.COIN_NETHERITE);
        this.registerBasicItem(ModItems.COIN_CHOCOLATE_COPPER);
        this.registerBasicItem(ModItems.COIN_CHOCOLATE_IRON);
        this.registerBasicItem(ModItems.COIN_CHOCOLATE_GOLD);
        this.registerBasicItem(ModItems.COIN_CHOCOLATE_EMERALD);
        this.registerBasicItem(ModItems.COIN_CHOCOLATE_DIAMOND);
        this.registerBasicItem(ModItems.COIN_CHOCOLATE_NETHERITE);
        this.registerCoinPile(ModBlocks.COINPILE_COPPER);
        this.registerCoinPile(ModBlocks.COINPILE_IRON);
        this.registerCoinPile(ModBlocks.COINPILE_GOLD);
        this.registerCoinPile(ModBlocks.COINPILE_EMERALD);
        this.registerCoinPile(ModBlocks.COINPILE_DIAMOND);
        this.registerCoinPile(ModBlocks.COINPILE_NETHERITE);
        this.registerCoinPile(ModBlocks.COINPILE_CHOCOLATE_COPPER);
        this.registerCoinPile(ModBlocks.COINPILE_CHOCOLATE_IRON);
        this.registerCoinPile(ModBlocks.COINPILE_CHOCOLATE_GOLD);
        this.registerCoinPile(ModBlocks.COINPILE_CHOCOLATE_EMERALD);
        this.registerCoinPile(ModBlocks.COINPILE_CHOCOLATE_DIAMOND);
        this.registerCoinPile(ModBlocks.COINPILE_CHOCOLATE_NETHERITE);
        this.registerCoinBlock(ModBlocks.COINBLOCK_COPPER);
        this.registerCoinBlock(ModBlocks.COINBLOCK_IRON);
        this.registerCoinBlock(ModBlocks.COINBLOCK_GOLD);
        this.registerCoinBlock(ModBlocks.COINBLOCK_EMERALD);
        this.registerCoinBlock(ModBlocks.COINBLOCK_DIAMOND);
        this.registerCoinBlock(ModBlocks.COINBLOCK_NETHERITE);
        this.registerCoinBlock(ModBlocks.COINBLOCK_CHOCOLATE_COPPER);
        this.registerCoinBlock(ModBlocks.COINBLOCK_CHOCOLATE_IRON);
        this.registerCoinBlock(ModBlocks.COINBLOCK_CHOCOLATE_GOLD);
        this.registerCoinBlock(ModBlocks.COINBLOCK_CHOCOLATE_EMERALD);
        this.registerCoinBlock(ModBlocks.COINBLOCK_CHOCOLATE_DIAMOND);
        this.registerCoinBlock(ModBlocks.COINBLOCK_CHOCOLATE_NETHERITE);
        this.registerBasicItem(ModItems.TRADING_CORE);
        this.registerBasicItem(ModItems.WALLET_COPPER);
        this.registerBasicItem(ModItems.WALLET_IRON);
        this.registerBasicItem(ModItems.WALLET_GOLD);
        this.registerBasicItem(ModItems.WALLET_EMERALD);
        this.registerBasicItem(ModItems.WALLET_DIAMOND);
        this.registerBasicItem(ModItems.WALLET_NETHERITE);
        this.registerTallRotatable(ModBlocks.ATM, "atm_top", "atm_bottom", "atm", true);
        this.registerBasicItem(ModItems.PORTABLE_ATM);
        this.registerRotatable(ModBlocks.COIN_MINT);
        this.registerRotatable(ModBlocks.CASH_REGISTER, "cash_register_modern", true);
        this.registerSimpleState(ModBlocks.DISPLAY_CASE);
        ModBlocks.VENDING_MACHINE.forEach((color, block) -> {
            ResourceLocation interiorTexture = new ResourceLocation("lightmanscurrency", this.lazyColoredID("block/vending_machine/", color, "_interior"));
            ResourceLocation exteriorTexture = new ResourceLocation("lightmanscurrency", this.lazyColoredID("block/vending_machine/", color, "_exterior"));
            String topID = this.lazyColoredID("block/vending_machine/", color, "_top");
            String bottomID = this.lazyColoredID("block/vending_machine/", color, "_bottom");
            String itemID = this.lazyColoredID("block/vending_machine/", color, "_item");
            this.models().getBuilder(topID).parent(this.lazyBlockModel("vending_machine/base_top", true)).texture("exterior", exteriorTexture).texture("interior", interiorTexture);
            this.models().getBuilder(bottomID).parent(this.lazyBlockModel("vending_machine/base_bottom", true)).texture("exterior", exteriorTexture).texture("interior", interiorTexture);
            this.models().getBuilder(itemID).parent(this.lazyBlockModel("vending_machine/base_item", true)).texture("exterior", exteriorTexture).texture("interior", interiorTexture);
            this.registerTallRotatable(block, topID, bottomID, itemID, false);
        });
        ModBlocks.VENDING_MACHINE_LARGE.forEach((color, block) -> {
            ResourceLocation interiorTexture = new ResourceLocation("lightmanscurrency", this.lazyColoredID("block/large_vending_machine/", color, "_interior"));
            ResourceLocation exteriorTexture = new ResourceLocation("lightmanscurrency", this.lazyColoredID("block/large_vending_machine/", color, "_exterior"));
            String topLeftID = this.lazyColoredID("block/large_vending_machine/", color, "_top_left");
            String topRightID = this.lazyColoredID("block/large_vending_machine/", color, "_top_right");
            String bottomLeftID = this.lazyColoredID("block/large_vending_machine/", color, "_bottom_left");
            String bottomRightID = this.lazyColoredID("block/large_vending_machine/", color, "_bottom_right");
            String itemID = this.lazyColoredID("block/large_vending_machine/", color, "_item");
            this.models().getBuilder(topLeftID).parent(this.lazyBlockModel("large_vending_machine/base_top_left", true)).texture("exterior", exteriorTexture).texture("interior", interiorTexture);
            this.models().getBuilder(topRightID).parent(this.lazyBlockModel("large_vending_machine/base_top_right", true)).texture("exterior", exteriorTexture).texture("interior", interiorTexture);
            this.models().getBuilder(bottomLeftID).parent(this.lazyBlockModel("large_vending_machine/base_bottom_left", true)).texture("exterior", exteriorTexture).texture("interior", interiorTexture);
            this.models().getBuilder(bottomRightID).parent(this.lazyBlockModel("large_vending_machine/base_bottom_right", true)).texture("exterior", exteriorTexture).texture("interior", interiorTexture);
            this.models().getBuilder(itemID).parent(this.lazyBlockModel("large_vending_machine/base_item", true)).texture("exterior", exteriorTexture).texture("interior", interiorTexture);
            this.registerTallWideRotatable(block, topLeftID, topRightID, bottomLeftID, bottomRightID, itemID, false);
        });
        ModBlocks.SHELF.forEach((type, block) -> {
            String modelID = this.lazyWoodenID("block/shelf/", type);
            WoodData data = type.getData();
            if (data != null) {
                this.models().getBuilder(modelID).parent(this.lazyBlockModel("shelf/base", true)).texture("main", data.plankTexture);
            } else {
                LightmansCurrency.LogWarning("Could not generate models for wood type '" + type.id + "' as it has no wood data!");
            }
            this.registerRotatable(block, modelID, false);
        });
        ModBlocks.SHELF_2x2.forEach((type, block) -> {
            String modelID = this.lazyWoodenID("block/shelf2/", type);
            WoodData data = type.getData();
            if (data != null) {
                this.models().getBuilder(modelID).parent(this.lazyBlockModel("shelf2/base", true)).texture("main", data.plankTexture);
            } else {
                LightmansCurrency.LogWarning("Could not generate models for wood type '" + type.id + "' as it has no wood data!");
            }
            this.registerRotatable(block, modelID, false);
        });
        ModBlocks.CARD_DISPLAY.forEach((type, color, block) -> {
            String modelID = this.lazyWoodenID("block/card_display/", type, "/" + color.getResourceSafeName());
            WoodData data = type.getData();
            if (data != null) {
                this.models().getBuilder(modelID).parent(this.lazyBlockModel("card_display/base", true)).texture("log", data.logSideTexture).texture("logtop", data.logTopTexture).texture("plank", data.plankTexture).texture("wool", ColorHelper.GetWoolTextureOfColor(color));
            }
            this.registerRotatable(block, modelID, false);
        });
        ModBlocks.FREEZER.forEach((color, block) -> {
            ResourceLocation concreteTexture = new ResourceLocation(this.lazyColoredID("block/", color, "_concrete_powder"));
            String topModelID = this.lazyColoredID("block/freezer/", color, "_top");
            String bottomModelID = this.lazyColoredID("block/freezer/", color, "_bottom");
            String itemModelID = this.lazyColoredID("block/freezer/", color, "_item");
            String doorModelID = this.lazyColoredID("block/freezer/doors/", color);
            this.models().getBuilder(topModelID).parent(this.lazyBlockModel("freezer/base_top", true)).texture("concrete", concreteTexture);
            this.models().getBuilder(bottomModelID).parent(this.lazyBlockModel("freezer/base_bottom", true)).texture("concrete", concreteTexture);
            this.models().getBuilder(itemModelID).parent(this.lazyBlockModel("freezer/base_item", true)).texture("concrete", concreteTexture);
            this.models().getBuilder(doorModelID).parent(this.lazyBlockModel("freezer/doors/base", true)).texture("concrete", concreteTexture);
            this.registerTallRotatable(block, topModelID, bottomModelID, itemModelID, false);
        });
        this.registerTallRotatable(ModBlocks.ARMOR_DISPLAY, "armor_display_top", "armor_display_bottom", "armor_display", true);
        this.registerTallRotatable(ModBlocks.TICKET_KIOSK, "ticket_kiosk_top", "ticket_kiosk_bottom", "ticket_kiosk", true);
        ModBlocks.BOOKSHELF_TRADER.forEach((type, block) -> {
            String modelID = this.lazyWoodenID("block/bookshelf_trader/", type);
            WoodData data = type.getData();
            if (data != null) {
                this.models().getBuilder(modelID).parent(this.lazyBlockModel("bookshelf_trader/base", true)).texture("main", data.plankTexture);
            }
            this.registerRotatable(block, modelID, false);
        });
        this.registerTallRotatableInv(ModBlocks.SLOT_MACHINE, "slot_machine/top", "slot_machine/bottom", "slot_machine/item", true);
        this.registerPaygate(ModBlocks.PAYGATE, "paygate_powered", "paygate_unpowered");
        this.registerRotatable(ModBlocks.ITEM_NETWORK_TRADER_1, "item_network_trader_1", true);
        this.registerRotatable(ModBlocks.ITEM_NETWORK_TRADER_2, "item_network_trader_2", true);
        this.registerRotatable(ModBlocks.ITEM_NETWORK_TRADER_3, "item_network_trader_3", true);
        this.registerRotatable(ModBlocks.ITEM_NETWORK_TRADER_4, "item_network_trader_4", true);
        this.registerRotatable(ModBlocks.ITEM_TRADER_INTERFACE);
        this.registerRotatable(ModBlocks.TERMINAL);
        this.registerBasicItem(ModItems.PORTABLE_TERMINAL);
        this.registerRotatable(ModBlocks.GEM_TERMINAL);
        this.registerBasicItem(ModItems.PORTABLE_GEM_TERMINAL);
        this.registerRotatable(ModBlocks.TICKET_STATION);
        this.registerBasicItem(ModItems.TICKET);
        this.registerLayeredItem(ModItems.TICKET_MASTER);
        this.registerLayeredItem(ModItems.TICKET_PASS);
        this.registerBasicItem(ModItems.TICKET_STUB);
        this.registerBasicItem(ModItems.GOLDEN_TICKET);
        this.registerLayeredItem(ModItems.GOLDEN_TICKET_MASTER);
        this.registerLayeredItem(ModItems.GOLDEN_TICKET_PASS);
        this.registerBasicItem(ModItems.GOLDEN_TICKET_STUB);
        ModBlocks.AUCTION_STAND.forEach((type, block) -> {
            String modelID = this.lazyWoodenID("block/auction_stand/", type);
            WoodData data = type.getData();
            if (data != null) {
                this.models().getBuilder(modelID).parent(this.lazyBlockModel("auction_stand/base", true)).texture("log", data.logSideTexture).texture("log_top", data.logTopTexture);
            }
            this.registerSimpleState(block, modelID);
        });
        this.getVariantBuilder(ModBlocks.COIN_CHEST.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(new ModelFile.ExistingModelFile(new ResourceLocation("block/chest"), this.models().existingFileHelper)).build());
        this.registerBlockItemModel(ModBlocks.COIN_CHEST, new ModelFile.ExistingModelFile(new ResourceLocation("item/chest"), this.models().existingFileHelper));
        this.registerRotatable(ModBlocks.PIGGY_BANK, "jars/piggy_bank", true);
        this.registerRotatable(ModBlocks.COINJAR_BLUE, "jars/coinjar_blue", true);
        this.registerBasicItem(ModItems.ITEM_CAPACITY_UPGRADE_1);
        this.registerBasicItem(ModItems.ITEM_CAPACITY_UPGRADE_2);
        this.registerBasicItem(ModItems.ITEM_CAPACITY_UPGRADE_3);
        this.registerBasicItem(ModItems.ITEM_CAPACITY_UPGRADE_4);
        this.registerBasicItem(ModItems.SPEED_UPGRADE_1);
        this.registerBasicItem(ModItems.SPEED_UPGRADE_2);
        this.registerBasicItem(ModItems.SPEED_UPGRADE_3);
        this.registerBasicItem(ModItems.SPEED_UPGRADE_4);
        this.registerBasicItem(ModItems.SPEED_UPGRADE_5);
        this.registerBasicItem(ModItems.NETWORK_UPGRADE);
        this.registerBasicItem(ModItems.HOPPER_UPGRADE);
        this.registerBasicItem(ModItems.COIN_CHEST_EXCHANGE_UPGRADE);
        this.registerBasicItem(ModItems.COIN_CHEST_MAGNET_UPGRADE_1);
        this.registerBasicItem(ModItems.COIN_CHEST_MAGNET_UPGRADE_2);
        this.registerBasicItem(ModItems.COIN_CHEST_MAGNET_UPGRADE_3);
        this.registerBasicItem(ModItems.COIN_CHEST_MAGNET_UPGRADE_4);
        this.registerBasicItem(ModItems.COIN_CHEST_SECURITY_UPGRADE);
        this.registerBasicItem(ModItems.UPGRADE_SMITHING_TEMPLATE);
        this.registerRotatable(ModBlocks.TAX_COLLECTOR);
        this.registerRotatableInv(ModBlocks.SUS_JAR, "jars/sus_jar", true);
    }

    private void registerBasicItem(RegistryObject<? extends ItemLike> item) {
        this.itemModels().basicItem(item.get().asItem());
    }

    private void registerLayeredItem(RegistryObject<? extends ItemLike> item) {
        ResourceLocation itemID = (ResourceLocation) Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.get().asItem()));
        this.itemModels().basicItem(itemID).texture("layer1", new ResourceLocation(itemID.getNamespace(), "item/" + itemID.getPath() + "_overlay"));
    }

    private void registerBlockItemModel(RegistryObject<? extends Block> block, String itemModel, boolean check) {
        this.registerBlockItemModel(block, this.lazyBlockModel(itemModel, check));
    }

    private void registerBlockItemModel(RegistryObject<? extends Block> block, ModelFile itemModel) {
        this.itemModels().getBuilder(ForgeRegistries.ITEMS.getKey(block.get().asItem()).toString()).parent(itemModel);
    }

    private void registerSimpleState(RegistryObject<? extends Block> block) {
        this.registerSimpleState(block, this.lazyModelID(block));
    }

    private void registerSimpleState(RegistryObject<? extends Block> block, String modelID) {
        ModelFile model = this.lazyBlockModel(modelID, true);
        this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
        this.registerBlockItemModel(block, model);
    }

    private void registerCoinPile(RegistryObject<? extends Block> block) {
        String modelID = this.lazyModelID(block);
        ResourceLocation texture = ForgeRegistries.BLOCKS.getKey(block.get()).withPrefix("block/");
        this.models().getBuilder(modelID).parent(this.lazyBlockModel("coin_pile", true)).texture("main", texture);
        ModelFile model = this.lazyBlockModel(modelID, false);
        this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(model).rotationY(this.getRotationY(state)).build());
        this.registerBasicItem(block);
    }

    private void registerCoinBlock(RegistryObject<? extends Block> block) {
        String modelID = this.lazyModelID(block);
        ResourceLocation texture = ForgeRegistries.BLOCKS.getKey(block.get()).withPrefix("block/");
        this.models().getBuilder(modelID).parent(this.lazyBlockModel("coin_block", true)).texture("main", texture);
        this.registerSimpleState(block, modelID);
    }

    private void registerPaygate(RegistryObject<? extends Block> block, String poweredModelID, String unpoweredModelID) {
        ModelFile powered = this.lazyBlockModel(poweredModelID, true);
        ModelFile unpowered = this.lazyBlockModel(unpoweredModelID, true);
        this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.m_61143_(PaygateBlock.POWERED) ? powered : unpowered).rotationY(this.getRotationY(state)).build());
        this.registerBlockItemModel(block, powered);
    }

    private void registerRotatable(RegistryObject<? extends Block> block) {
        this.registerRotatable(block, this.lazyModelID(block), true);
    }

    private void registerRotatable(RegistryObject<? extends Block> block, String modelID, boolean check) {
        ModelFile model = this.lazyBlockModel(modelID, check);
        this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(model).rotationY(this.getRotationY(state)).build());
        this.registerBlockItemModel(block, model);
    }

    private void registerRotatableInv(RegistryObject<? extends Block> block, String modelID, boolean check) {
        ModelFile model = this.lazyBlockModel(modelID, check);
        this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(model).rotationY(this.getRotationYInv(state)).build());
        this.registerBlockItemModel(block, model);
    }

    private void registerTallRotatableInv(RegistryObject<? extends Block> block, String topModelID, String bottomModelID, String itemModelID, boolean check) {
        ModelFile top = this.lazyBlockModel(topModelID, check);
        ModelFile bottom = this.lazyBlockModel(bottomModelID, check);
        this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(this.getTopBottomModel(state, top, bottom)).rotationY(this.getRotationYInv(state)).build());
        this.registerBlockItemModel(block, itemModelID, check);
    }

    private void registerTallRotatable(RegistryObject<? extends Block> block, String topModelID, String bottomModelID, String itemModelID, boolean check) {
        ModelFile top = this.lazyBlockModel(topModelID, check);
        ModelFile bottom = this.lazyBlockModel(bottomModelID, check);
        this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(this.getTopBottomModel(state, top, bottom)).rotationY(this.getRotationY(state)).build());
        this.registerBlockItemModel(block, itemModelID, check);
    }

    private void registerTallWideRotatable(RegistryObject<? extends Block> block, String topLeftModelID, String topRightModelID, String bottomLeftModelID, String bottomRightModelID, String itemModelID, boolean check) {
        ModelFile topLeft = this.lazyBlockModel(topLeftModelID, check);
        ModelFile topRight = this.lazyBlockModel(topRightModelID, check);
        ModelFile bottomLeft = this.lazyBlockModel(bottomLeftModelID, check);
        ModelFile bottomRight = this.lazyBlockModel(bottomRightModelID, check);
        this.getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(this.getTopBottomLeftRightModel(state, topLeft, topRight, bottomLeft, bottomRight)).rotationY(this.getRotationY(state)).build());
        this.registerBlockItemModel(block, itemModelID, check);
    }

    private String lazyColoredID(String prefix, Color color) {
        return this.lazyColoredID(prefix, color, "");
    }

    private String lazyColoredID(String prefix, Color color, String postFix) {
        return prefix + color.getResourceSafeName() + postFix;
    }

    private String lazyWoodenID(String prefix, WoodType type) {
        return type.generateResourceLocation(prefix);
    }

    private String lazyWoodenID(String prefix, WoodType type, String postFix) {
        return type.generateResourceLocation(prefix, postFix);
    }

    private String lazyModelID(RegistryObject<? extends Block> block) {
        return ForgeRegistries.BLOCKS.getKey(block.get()).getPath();
    }

    private ResourceLocation lazyBlockModelID(String modelID) {
        return new ResourceLocation("lightmanscurrency", modelID.startsWith("block/") ? modelID : "block/" + modelID);
    }

    private ModelFile lazyBlockModel(String modelID, boolean check) {
        return (ModelFile) (check ? new ModelFile.ExistingModelFile(this.lazyBlockModelID(modelID), this.models().existingFileHelper) : new ModelFile.UncheckedModelFile(this.lazyBlockModelID(modelID)));
    }

    private int getRotationYInv(BlockState state) {
        return switch((Direction) state.m_61143_(IRotatableBlock.FACING)) {
            case WEST ->
                90;
            case NORTH ->
                180;
            case EAST ->
                270;
            default ->
                0;
        };
    }

    private int getRotationY(BlockState state) {
        return switch((Direction) state.m_61143_(IRotatableBlock.FACING)) {
            case WEST ->
                270;
            default ->
                0;
            case EAST ->
                90;
            case SOUTH ->
                180;
        };
    }

    private ModelFile getTopBottomModel(BlockState state, ModelFile top, ModelFile bottom) {
        return state.m_61143_(ITallBlock.ISBOTTOM) ? bottom : top;
    }

    private ModelFile getTopBottomLeftRightModel(BlockState state, ModelFile topLeft, ModelFile topRight, ModelFile bottomLeft, ModelFile bottomRight) {
        if ((Boolean) state.m_61143_(ITallBlock.ISBOTTOM)) {
            return state.m_61143_(IWideBlock.ISLEFT) ? bottomLeft : bottomRight;
        } else {
            return state.m_61143_(IWideBlock.ISLEFT) ? topLeft : topRight;
        }
    }
}