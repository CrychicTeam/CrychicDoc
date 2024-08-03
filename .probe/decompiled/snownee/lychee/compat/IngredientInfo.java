package snownee.lychee.compat;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientInfo {

    public final Ingredient ingredient;

    public List<Component> tooltips = List.of();

    public int count = 1;

    public boolean isCatalyst;

    public IngredientInfo(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void addTooltip(Component line) {
        if (this.tooltips.isEmpty()) {
            this.tooltips = Lists.newArrayList();
        }
        this.tooltips.add(line);
    }

    public static enum Type {

        NORMAL, AIR, ANY
    }
}