package sereneseasons.item;

import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;
import sereneseasons.season.SeasonTime;

public class CalendarItem extends Item {

    public CalendarItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        if (world != null) {
            if (ServerConfig.isDimensionWhitelisted(world.dimension())) {
                int seasonCycleTicks = SeasonHelper.getSeasonState(world).getSeasonCycleTicks();
                SeasonTime time = new SeasonTime(seasonCycleTicks);
                int subSeasonDuration = ServerConfig.subSeasonDuration.get();
                tooltip.add(Component.translatable("desc.sereneseasons." + time.getSubSeason().toString().toLowerCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable("desc.sereneseasons.day_counter", time.getDay() % subSeasonDuration + 1, subSeasonDuration).withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(Component.literal("???").withStyle(ChatFormatting.GRAY));
            }
        } else {
            tooltip.add(Component.literal("???").withStyle(ChatFormatting.GRAY));
        }
    }
}