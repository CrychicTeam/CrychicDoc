package dev.xkmc.l2archery.content.item;

import dev.xkmc.l2archery.content.feature.core.PotionArrowFeature;
import dev.xkmc.l2archery.init.data.LangData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public interface IBowConfig extends IGeneralConfig {

    float speed();

    float fov();

    int pull_time();

    int fov_time();

    PotionArrowFeature getEffects();

    default void addStatTooltip(List<Component> list) {
        LangData.STAT_DAMAGE.getWithSign(list, (double) this.damage());
        LangData.STAT_PUNCH.getWithSign(list, (double) this.punch());
        list.add(LangData.STAT_PULL_TIME.get((double) this.pull_time() / 20.0));
        list.add(LangData.STAT_SPEED.get(this.speed() * 20.0F));
        list.add(LangData.STAT_FOV.get(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format((double) (1.0F / (1.0F - this.fov())))));
    }
}