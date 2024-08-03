package net.minecraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.stats.Stats;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WrittenBookItem extends Item {

    public static final int TITLE_LENGTH = 16;

    public static final int TITLE_MAX_LENGTH = 32;

    public static final int PAGE_EDIT_LENGTH = 1024;

    public static final int PAGE_LENGTH = 32767;

    public static final int MAX_PAGES = 100;

    public static final int MAX_GENERATION = 2;

    public static final String TAG_TITLE = "title";

    public static final String TAG_FILTERED_TITLE = "filtered_title";

    public static final String TAG_AUTHOR = "author";

    public static final String TAG_PAGES = "pages";

    public static final String TAG_FILTERED_PAGES = "filtered_pages";

    public static final String TAG_GENERATION = "generation";

    public static final String TAG_RESOLVED = "resolved";

    public WrittenBookItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    public static boolean makeSureTagIsValid(@Nullable CompoundTag compoundTag0) {
        if (!WritableBookItem.makeSureTagIsValid(compoundTag0)) {
            return false;
        } else if (!compoundTag0.contains("title", 8)) {
            return false;
        } else {
            String $$1 = compoundTag0.getString("title");
            return $$1.length() > 32 ? false : compoundTag0.contains("author", 8);
        }
    }

    public static int getGeneration(ItemStack itemStack0) {
        return itemStack0.getTag().getInt("generation");
    }

    public static int getPageCount(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        return $$1 != null ? $$1.getList("pages", 8).size() : 0;
    }

    @Override
    public Component getName(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        if ($$1 != null) {
            String $$2 = $$1.getString("title");
            if (!StringUtil.isNullOrEmpty($$2)) {
                return Component.literal($$2);
            }
        }
        return super.getName(itemStack0);
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        if (itemStack0.hasTag()) {
            CompoundTag $$4 = itemStack0.getTag();
            String $$5 = $$4.getString("author");
            if (!StringUtil.isNullOrEmpty($$5)) {
                listComponent2.add(Component.translatable("book.byAuthor", $$5).withStyle(ChatFormatting.GRAY));
            }
            listComponent2.add(Component.translatable("book.generation." + $$4.getInt("generation")).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        if ($$3.m_60713_(Blocks.LECTERN)) {
            return LecternBlock.tryPlaceBook(useOnContext0.getPlayer(), $$1, $$2, $$3, useOnContext0.getItemInHand()) ? InteractionResult.sidedSuccess($$1.isClientSide) : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        player1.openItemGui($$3, interactionHand2);
        player1.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess($$3, level0.isClientSide());
    }

    public static boolean resolveBookComponents(ItemStack itemStack0, @Nullable CommandSourceStack commandSourceStack1, @Nullable Player player2) {
        CompoundTag $$3 = itemStack0.getTag();
        if ($$3 != null && !$$3.getBoolean("resolved")) {
            $$3.putBoolean("resolved", true);
            if (!makeSureTagIsValid($$3)) {
                return false;
            } else {
                ListTag $$4 = $$3.getList("pages", 8);
                ListTag $$5 = new ListTag();
                for (int $$6 = 0; $$6 < $$4.size(); $$6++) {
                    String $$7 = resolvePage(commandSourceStack1, player2, $$4.getString($$6));
                    if ($$7.length() > 32767) {
                        return false;
                    }
                    $$5.add($$6, (Tag) StringTag.valueOf($$7));
                }
                if ($$3.contains("filtered_pages", 10)) {
                    CompoundTag $$8 = $$3.getCompound("filtered_pages");
                    CompoundTag $$9 = new CompoundTag();
                    for (String $$10 : $$8.getAllKeys()) {
                        String $$11 = resolvePage(commandSourceStack1, player2, $$8.getString($$10));
                        if ($$11.length() > 32767) {
                            return false;
                        }
                        $$9.putString($$10, $$11);
                    }
                    $$3.put("filtered_pages", $$9);
                }
                $$3.put("pages", $$5);
                return true;
            }
        } else {
            return false;
        }
    }

    private static String resolvePage(@Nullable CommandSourceStack commandSourceStack0, @Nullable Player player1, String string2) {
        Component $$5;
        try {
            $$5 = Component.Serializer.fromJsonLenient(string2);
            $$5 = ComponentUtils.updateForEntity(commandSourceStack0, $$5, player1, 0);
        } catch (Exception var5) {
            $$5 = Component.literal(string2);
        }
        return Component.Serializer.toJson($$5);
    }

    @Override
    public boolean isFoil(ItemStack itemStack0) {
        return true;
    }
}