package dev.xkmc.l2hostility.content.item.consumable;

import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class BottleOfSanity extends DrinkableBottleItem {

    public BottleOfSanity(Item.Properties prop) {
        super(prop);
    }

    @Override
    protected void doServerLogic(ServerPlayer player) {
        PlayerDifficulty cap = PlayerDifficulty.HOLDER.get(player);
        LevelEditor editor = cap.getLevelEditor();
        editor.setBase(0);
        cap.dimensions.clear();
        cap.sync();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if (!LHConfig.COMMON.banBottles.get()) {
            list.add(LangData.ITEM_BOTTLE_SANITY.get().withStyle(ChatFormatting.GRAY));
        }
    }
}