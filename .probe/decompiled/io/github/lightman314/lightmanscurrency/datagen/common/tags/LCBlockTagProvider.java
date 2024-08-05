package io.github.lightman314.lightmanscurrency.datagen.common.tags;

import io.github.lightman314.lightmanscurrency.LCTags;
import io.github.lightman314.lightmanscurrency.common.core.ModBlocks;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBiBundle;
import io.github.lightman314.lightmanscurrency.common.core.groups.RegistryObjectBundle;
import io.github.lightman314.lightmanscurrency.common.core.variants.IOptionalKey;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class LCBlockTagProvider extends BlockTagsProvider {

    public LCBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "lightmanscurrency", existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        this.cTag(LCTags.Blocks.MULTI_BLOCK).add(ModBlocks.ATM).add(ModBlocks.VENDING_MACHINE).add(ModBlocks.VENDING_MACHINE_LARGE).add(ModBlocks.ARMOR_DISPLAY).add(ModBlocks.FREEZER).add(ModBlocks.TICKET_KIOSK).add(ModBlocks.SLOT_MACHINE);
        this.cTag(LCTags.Blocks.OWNER_PROTECTED).add(ModBlocks.DISPLAY_CASE).add(ModBlocks.VENDING_MACHINE).add(ModBlocks.VENDING_MACHINE_LARGE).add(ModBlocks.SHELF).add(ModBlocks.SHELF_2x2).add(ModBlocks.CARD_DISPLAY).add(ModBlocks.ARMOR_DISPLAY).add(ModBlocks.FREEZER).add(ModBlocks.ITEM_NETWORK_TRADER_1).add(ModBlocks.ITEM_NETWORK_TRADER_2).add(ModBlocks.ITEM_NETWORK_TRADER_3).add(ModBlocks.ITEM_NETWORK_TRADER_4).add(ModBlocks.ITEM_TRADER_INTERFACE).add(ModBlocks.PAYGATE).add(ModBlocks.TICKET_KIOSK).add(ModBlocks.BOOKSHELF_TRADER).add(ModBlocks.SLOT_MACHINE).add(ModBlocks.COIN_CHEST).add(ModBlocks.TAX_COLLECTOR);
        this.cTag(LCTags.Blocks.SAFE_INTERACTABLE).addTag(LCTags.Blocks.OWNER_PROTECTED).add(ModBlocks.ATM).add(ModBlocks.CASH_REGISTER).add(ModBlocks.AUCTION_STAND);
        this.cTag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.COINPILE_COPPER).add(ModBlocks.COINPILE_IRON).add(ModBlocks.COINPILE_GOLD).add(ModBlocks.COINPILE_DIAMOND).add(ModBlocks.COINPILE_EMERALD).add(ModBlocks.COINPILE_NETHERITE).add(ModBlocks.COINBLOCK_COPPER).add(ModBlocks.COINBLOCK_IRON).add(ModBlocks.COINBLOCK_GOLD).add(ModBlocks.COINBLOCK_EMERALD).add(ModBlocks.COINBLOCK_DIAMOND).add(ModBlocks.COINBLOCK_NETHERITE).add(ModBlocks.ATM).add(ModBlocks.COIN_MINT).add(ModBlocks.DISPLAY_CASE).add(ModBlocks.VENDING_MACHINE).add(ModBlocks.VENDING_MACHINE_LARGE).add(ModBlocks.ARMOR_DISPLAY).add(ModBlocks.FREEZER).add(ModBlocks.ITEM_NETWORK_TRADER_1).add(ModBlocks.ITEM_NETWORK_TRADER_2).add(ModBlocks.ITEM_NETWORK_TRADER_3).add(ModBlocks.ITEM_NETWORK_TRADER_4).add(ModBlocks.ITEM_TRADER_INTERFACE).add(ModBlocks.CASH_REGISTER).add(ModBlocks.TERMINAL).add(ModBlocks.GEM_TERMINAL).add(ModBlocks.PAYGATE).add(ModBlocks.TICKET_KIOSK).add(ModBlocks.SLOT_MACHINE).add(ModBlocks.TICKET_STATION).add(ModBlocks.PIGGY_BANK).add(ModBlocks.COINJAR_BLUE).add(ModBlocks.SUS_JAR).add(ModBlocks.TAX_COLLECTOR);
        this.cTag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.SHELF).add(ModBlocks.SHELF_2x2).add(ModBlocks.CARD_DISPLAY).add(ModBlocks.BOOKSHELF_TRADER).add(ModBlocks.COIN_CHEST).add(ModBlocks.AUCTION_STAND);
        this.cTag(BlockTags.DRAGON_IMMUNE).addTag(LCTags.Blocks.OWNER_PROTECTED);
        this.cTag(BlockTags.WITHER_IMMUNE).addTag(LCTags.Blocks.OWNER_PROTECTED);
        this.cTag(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.COINBLOCK_COPPER).add(ModBlocks.COINBLOCK_IRON).add(ModBlocks.COINBLOCK_GOLD).add(ModBlocks.COINBLOCK_EMERALD).add(ModBlocks.COINBLOCK_DIAMOND).add(ModBlocks.COINBLOCK_NETHERITE);
        this.cTag(new ResourceLocation("forge", "immovable")).addTag(LCTags.Blocks.MULTI_BLOCK);
        this.cTag(new ResourceLocation("create", "non_movable")).addTag(LCTags.Blocks.MULTI_BLOCK);
        this.cTag(new ResourceLocation("ftbchunks", "interact_whitelist")).addTag(LCTags.Blocks.SAFE_INTERACTABLE);
    }

    private LCBlockTagProvider.CustomTagAppender cTag(TagKey<Block> tag) {
        return new LCBlockTagProvider.CustomTagAppender(this.m_206424_(tag));
    }

    private LCBlockTagProvider.CustomTagAppender cTag(ResourceLocation tag) {
        return new LCBlockTagProvider.CustomTagAppender(this.m_206424_(BlockTags.create(tag)));
    }

    private static record CustomTagAppender(IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block> appender) {

        public LCBlockTagProvider.CustomTagAppender add(Block block) {
            this.appender.add(block);
            return this;
        }

        public LCBlockTagProvider.CustomTagAppender add(RegistryObject<? extends Block> block) {
            this.appender.add(block.get());
            return this;
        }

        public LCBlockTagProvider.CustomTagAppender addOptional(RegistryObject<? extends Block> block) {
            this.appender.m_176839_(block.getId());
            return this;
        }

        public LCBlockTagProvider.CustomTagAppender add(RegistryObjectBundle<? extends Block, ?> bundle) {
            bundle.forEach((key, block) -> {
                if (key instanceof IOptionalKey ok) {
                    if (ok.isModded()) {
                        this.addOptional(block);
                    } else {
                        this.add(block);
                    }
                } else {
                    this.add(block);
                }
            });
            return this;
        }

        public LCBlockTagProvider.CustomTagAppender add(RegistryObjectBiBundle<? extends Block, ?, ?> bundle) {
            bundle.forEach((key1, key2, block) -> {
                if (key1 instanceof IOptionalKey ok1) {
                    if (ok1.isModded()) {
                        this.addOptional(block);
                    } else if (key2 instanceof IOptionalKey ok2) {
                        if (ok2.isModded()) {
                            this.addOptional(block);
                        } else {
                            this.add(block);
                        }
                    } else {
                        this.add(block);
                    }
                } else if (key2 instanceof IOptionalKey ok2x) {
                    if (ok2x.isModded()) {
                        this.addOptional(block);
                    } else {
                        this.add(block);
                    }
                } else {
                    this.add(block);
                }
            });
            return this;
        }

        public LCBlockTagProvider.CustomTagAppender addTag(TagKey<Block> tag) {
            this.appender.addTag(tag);
            return this;
        }
    }
}