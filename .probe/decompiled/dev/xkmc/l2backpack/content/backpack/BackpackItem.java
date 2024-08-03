package dev.xkmc.l2backpack.content.backpack;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.common.BackpackModelItem;
import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.content.render.BaseItemRenderer;
import dev.xkmc.l2backpack.init.data.BackpackConfig;
import dev.xkmc.l2backpack.init.data.LangData;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class BackpackItem extends BaseBagItem implements BackpackModelItem {

    private static final String ROW = "rows";

    public static final int MAX_ROW = 8;

    public final DyeColor color;

    public static ItemStack initLootGen(ItemStack stack, ResourceLocation loot) {
        CompoundTag ctag = stack.getOrCreateTag();
        ctag.putString("loot", loot.toString());
        return stack;
    }

    public static ItemStack setRow(ItemStack result, int i) {
        result.getOrCreateTag().putInt("rows", i);
        return result;
    }

    public BackpackItem(DyeColor color, Item.Properties props) {
        super(props.stacksTo(1).fireResistant());
        this.color = color;
    }

    @Override
    public int getRows(ItemStack stack) {
        int ans = Mth.clamp(stack.getOrCreateTag().getInt("rows"), BackpackConfig.COMMON.initialRows.get(), 8);
        if (!stack.getOrCreateTag().contains("rows")) {
            stack.getOrCreateTag().putInt("rows", ans);
        }
        return ans;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        CompoundTag tag = stack.getOrCreateTag();
        int rows = tag.getInt("rows");
        if (rows == 0) {
            rows = BackpackConfig.COMMON.initialRows.get();
        }
        list.add(LangData.IDS.BACKPACK_SLOT.get(Math.max(1, rows), 8));
        if (tag.contains("loot")) {
            list.add(LangData.IDS.LOOT.get().withStyle(ChatFormatting.AQUA));
        } else {
            PickupConfig.addText(stack, list);
        }
        LangData.addInfo(list, LangData.Info.QUICK_INV_ACCESS, LangData.Info.KEYBIND, LangData.Info.UPGRADE, LangData.Info.LOAD, LangData.Info.EXIT, LangData.Info.PICKUP);
        LangData.altInsert(list);
    }

    @Override
    public void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack) {
        new BackpackMenuPvd(player, slot, this, stack).open();
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack stack) {
        return new ResourceLocation("l2backpack", "textures/block/backpack/" + this.color.getName() + ".png");
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(BaseItemRenderer.EXTENSIONS);
    }
}