package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Abrahadabra extends CurseCurioItem {

    public Abrahadabra(Item.Properties properties) {
        super(properties);
    }

    @Override
    public int getExtraLevel() {
        return LHConfig.COMMON.abrahadabraExtraLevel.get();
    }

    @Override
    public boolean reflectTrait(MobTrait trait) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.ABRAHADABRA.get().withStyle(ChatFormatting.GOLD));
    }
}