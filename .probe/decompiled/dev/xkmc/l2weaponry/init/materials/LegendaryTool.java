package dev.xkmc.l2weaponry.init.materials;

import dev.xkmc.l2damagetracker.contents.materials.api.ITool;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public record LegendaryTool<T extends Item>(LWToolTypes type, LegendaryToolFactory<T> tool) implements ITool {

    public T parse(LWToolMats mat, Item.Properties p) {
        return (T) Wrappers.cast(mat.type.getToolConfig().sup().get(mat.type, this, p));
    }

    public int getDamage(int i) {
        return this.type.getDamage(i);
    }

    public float getSpeed(float v) {
        return this.type.getSpeed(v);
    }

    public Item create(Tier tier, int i, float v, Item.Properties properties, ExtraToolConfig extraToolConfig) {
        return this.tool.get(tier, i, v, properties, extraToolConfig);
    }
}