package io.github.lightman314.lightmanscurrency.datagen.common.tags;

import io.github.lightman314.lightmanscurrency.LCTags;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBiBundle;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBundle;
import io.github.lightman314.lightmanscurrency.common.core.variants.IOptionalKey;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class LCItemTagProvider extends ItemTagsProvider {

    public LCItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, "lightmanscurrency", existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider lookup) {
        this.cTag(LCTags.Items.COINS).addTag(LCTags.Items.EVENT_COINS).add(ModItems.COIN_COPPER).add(ModItems.COIN_IRON).add(ModItems.COIN_GOLD).add(ModItems.COIN_EMERALD).add(ModItems.COIN_DIAMOND).add(ModItems.COIN_NETHERITE);
        this.cTag(LCTags.Items.EVENT_COINS).addTag(LCTags.Items.EVENT_COIN_CHOCOLATE);
        this.cTag(LCTags.Items.EVENT_COIN_CHOCOLATE).add(ModItems.COIN_CHOCOLATE_COPPER).add(ModItems.COIN_CHOCOLATE_IRON).add(ModItems.COIN_CHOCOLATE_GOLD).add(ModItems.COIN_CHOCOLATE_EMERALD).add(ModItems.COIN_CHOCOLATE_DIAMOND).add(ModItems.COIN_CHOCOLATE_NETHERITE);
        this.cTag(LCTags.Items.COIN_MINTING_MATERIAL).addTag(Tags.Items.INGOTS_COPPER).addTag(Tags.Items.INGOTS_IRON).addTag(Tags.Items.INGOTS_GOLD).addTag(Tags.Items.GEMS_EMERALD).addTag(Tags.Items.GEMS_DIAMOND).addTag(Tags.Items.INGOTS_NETHERITE);
        this.cTag(LCTags.Items.WALLET).add(ModItems.WALLET_COPPER).add(ModItems.WALLET_IRON).add(ModItems.WALLET_GOLD).add(ModItems.WALLET_EMERALD).add(ModItems.WALLET_DIAMOND).add(ModItems.WALLET_NETHERITE);
        this.cTag(LCTags.Items.TRADER).addTag(LCTags.Items.TRADER_NORMAL).addTag(LCTags.Items.TRADER_SPECIALTY).addTag(LCTags.Items.TRADER_NETWORK);
        this.cTag(LCTags.Items.TRADER_NORMAL).addTag(LCTags.Items.TRADER_DISPLAY_CASE).addTag(LCTags.Items.TRADER_VENDING_MACHINE).addTag(LCTags.Items.TRADER_LARGE_VENDING_MACHINE).addTag(LCTags.Items.TRADER_SHELF).addTag(LCTags.Items.TRADER_SHELF_2x2).addTag(LCTags.Items.TRADER_CARD_DISPLAY).addTag(LCTags.Items.TRADER_FREEZER);
        this.cTag(LCTags.Items.TRADER_DISPLAY_CASE).add(ModBlocks.DISPLAY_CASE);
        this.cTag(LCTags.Items.TRADER_VENDING_MACHINE).add(ModBlocks.VENDING_MACHINE);
        this.cTag(LCTags.Items.TRADER_LARGE_VENDING_MACHINE).add(ModBlocks.VENDING_MACHINE_LARGE);
        this.cTag(LCTags.Items.TRADER_SHELF).add(ModBlocks.SHELF);
        this.cTag(LCTags.Items.TRADER_SHELF_2x2).add(ModBlocks.SHELF_2x2);
        this.cTag(LCTags.Items.TRADER_CARD_DISPLAY).add(ModBlocks.CARD_DISPLAY);
        this.cTag(LCTags.Items.TRADER_FREEZER).add(ModBlocks.FREEZER);
        this.cTag(LCTags.Items.TRADER_SPECIALTY).addTag(LCTags.Items.TRADER_SPECIALTY_ARMOR_DISPLAY).addTag(LCTags.Items.TRADER_SPECIALTY_PAYGATE).addTag(LCTags.Items.TRADER_SPECIALTY_TICKET_KIOSK).addTag(LCTags.Items.TRADER_SPECIALTY_BOOKSHELF).addTag(LCTags.Items.TRADER_SPECIALTY_SLOT_MACHINE);
        this.cTag(LCTags.Items.TRADER_SPECIALTY_ARMOR_DISPLAY).add(ModBlocks.ARMOR_DISPLAY);
        this.cTag(LCTags.Items.TRADER_SPECIALTY_PAYGATE).add(ModBlocks.PAYGATE);
        this.cTag(LCTags.Items.TRADER_SPECIALTY_TICKET_KIOSK).add(ModBlocks.TICKET_KIOSK);
        this.cTag(LCTags.Items.TRADER_SPECIALTY_BOOKSHELF).add(ModBlocks.BOOKSHELF_TRADER);
        this.cTag(LCTags.Items.TRADER_SPECIALTY_SLOT_MACHINE).add(ModBlocks.SLOT_MACHINE);
        this.cTag(LCTags.Items.TRADER_NETWORK).addTag(LCTags.Items.TRADER_NETWORK_ITEM);
        this.cTag(LCTags.Items.TRADER_NETWORK_ITEM).add(ModBlocks.ITEM_NETWORK_TRADER_1).add(ModBlocks.ITEM_NETWORK_TRADER_2).add(ModBlocks.ITEM_NETWORK_TRADER_3).add(ModBlocks.ITEM_NETWORK_TRADER_4);
        this.cTag(LCTags.Items.NETWORK_TERMINAL).add(ModBlocks.TERMINAL).add(ModItems.PORTABLE_TERMINAL).add(ModBlocks.GEM_TERMINAL).add(ModItems.PORTABLE_GEM_TERMINAL);
        this.cTag(LCTags.Items.TRADING_TERMINAL).addTag(LCTags.Items.NETWORK_TERMINAL);
        this.cTag(LCTags.Items.ATM).add(ModBlocks.ATM).add(ModItems.PORTABLE_ATM);
        this.cTag(LCTags.Items.TRADER_INTERFACE).add(ModBlocks.ITEM_TRADER_INTERFACE);
        this.cTag(LCTags.Items.COIN_JAR_NORMAL).add(ModBlocks.PIGGY_BANK).add(ModBlocks.COINJAR_BLUE);
        this.cTag(LCTags.Items.COIN_JAR_ALL).addTag(LCTags.Items.COIN_JAR_NORMAL).add(ModBlocks.SUS_JAR);
        this.cTag(LCTags.Items.TRADABLE_BOOK).add(Items.BOOK).add(Items.ENCHANTED_BOOK).add(Items.WRITABLE_BOOK).add(Items.WRITTEN_BOOK);
        this.cTag(LCTags.Items.TICKETS).addTag(LCTags.Items.TICKETS_TICKET).addTag(LCTags.Items.TICKETS_PASS).addTag(LCTags.Items.TICKETS_MASTER);
        this.cTag(LCTags.Items.TICKETS_TICKET).add(ModItems.TICKET).add(ModItems.GOLDEN_TICKET);
        this.cTag(LCTags.Items.TICKETS_PASS).add(ModItems.TICKET_PASS).add(ModItems.GOLDEN_TICKET_PASS);
        this.cTag(LCTags.Items.TICKETS_MASTER).add(ModItems.TICKET_MASTER).add(ModItems.GOLDEN_TICKET_MASTER);
        this.cTag(LCTags.Items.TICKET_MATERIAL).addTag(LCTags.Items.TICKET_MATERIAL_PAPER).addTag(LCTags.Items.TICKET_MATERIAL_GOLD);
        this.cTag(LCTags.Items.TICKET_MATERIAL_PAPER).add(Items.PAPER).add(ModItems.TICKET_STUB).add(ModItems.TICKET).add(ModItems.TICKET_PASS).add(ModItems.TICKET_MASTER);
        this.cTag(LCTags.Items.TICKET_MATERIAL_GOLD).addTag(Tags.Items.INGOTS_GOLD).add(ModItems.GOLDEN_TICKET_STUB).add(ModItems.GOLDEN_TICKET).add(ModItems.GOLDEN_TICKET_PASS).add(ModItems.GOLDEN_TICKET_MASTER);
        this.cTag(ItemTags.BEACON_PAYMENT_ITEMS).add(ModItems.COIN_IRON).add(ModItems.COIN_GOLD).add(ModItems.COIN_EMERALD).add(ModItems.COIN_DIAMOND).add(ModItems.COIN_NETHERITE);
        this.cTag(ItemTags.PIGLIN_LOVED).add(ModItems.COIN_GOLD).add(ModBlocks.COINPILE_GOLD).add(ModBlocks.COINBLOCK_GOLD);
        this.cTag(new ResourceLocation("curios", "wallet")).addTag(LCTags.Items.WALLET);
        this.cTag(new ResourceLocation("curios", "charm")).add(ModItems.PORTABLE_TERMINAL).add(ModItems.PORTABLE_GEM_TERMINAL).add(ModItems.PORTABLE_ATM);
    }

    private LCItemTagProvider.CustomTagAppender cTag(TagKey<Item> tag) {
        return new LCItemTagProvider.CustomTagAppender(this.m_206424_(tag));
    }

    private LCItemTagProvider.CustomTagAppender cTag(ResourceLocation tag) {
        return new LCItemTagProvider.CustomTagAppender(this.m_206424_(ItemTags.create(tag)));
    }

    private static record CustomTagAppender(IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> appender) {

        public LCItemTagProvider.CustomTagAppender add(ItemLike item) {
            this.appender.add(item.asItem());
            return this;
        }

        public LCItemTagProvider.CustomTagAppender add(RegistryObject<? extends ItemLike> item) {
            this.add(item.get());
            return this;
        }

        public LCItemTagProvider.CustomTagAppender addOptional(RegistryObject<? extends ItemLike> item) {
            this.appender.m_176839_(item.getId());
            return this;
        }

        public LCItemTagProvider.CustomTagAppender add(RegistryObjectBundle<? extends ItemLike, ?> bundle) {
            bundle.forEach((key, item) -> {
                if (key instanceof IOptionalKey ok) {
                    if (ok.isModded()) {
                        this.addOptional(item);
                    } else {
                        this.add(item);
                    }
                } else {
                    this.add(item);
                }
            });
            return this;
        }

        public <T> LCItemTagProvider.CustomTagAppender add(RegistryObjectBiBundle<? extends ItemLike, T, ?> bundle, @Nonnull T key) {
            bundle.forEach((key1, key2, item) -> {
                if (key1 == key) {
                    if (key1 instanceof IOptionalKey ok1) {
                        if (ok1.isModded()) {
                            this.addOptional(item);
                        } else if (key2 instanceof IOptionalKey ok2) {
                            if (ok2.isModded()) {
                                this.addOptional(item);
                            } else {
                                this.add(item);
                            }
                        }
                    } else if (key2 instanceof IOptionalKey ok2x) {
                        if (ok2x.isModded()) {
                            this.addOptional(item);
                        } else {
                            this.add(item);
                        }
                    } else {
                        this.add(item);
                    }
                }
            });
            return this;
        }

        public LCItemTagProvider.CustomTagAppender add(RegistryObjectBiBundle<? extends ItemLike, ?, ?> bundle) {
            bundle.forEach((key1, key2, item) -> {
                if (key1 instanceof IOptionalKey ok1) {
                    if (ok1.isModded()) {
                        this.addOptional(item);
                    } else if (key2 instanceof IOptionalKey ok2) {
                        if (ok2.isModded()) {
                            this.addOptional(item);
                        } else {
                            this.add(item);
                        }
                    } else {
                        this.add(item);
                    }
                } else if (key2 instanceof IOptionalKey ok2x) {
                    if (ok2x.isModded()) {
                        this.addOptional(item);
                    } else {
                        this.add(item);
                    }
                } else {
                    this.add(item);
                }
            });
            return this;
        }

        public LCItemTagProvider.CustomTagAppender addTag(TagKey<Item> tag) {
            this.appender.addTag(tag);
            return this;
        }

        public LCItemTagProvider.CustomTagAppender addTags(List<TagKey<Item>> tags) {
            for (TagKey<Item> tag : tags) {
                this.addTag(tag);
            }
            return this;
        }
    }
}