package dev.xkmc.modulargolems.content.item.upgrade;

import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.modifier.base.ModifierInstance;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class UpgradeItem extends Item {

    public static final List<UpgradeItem> LIST = new ArrayList();

    private final boolean foil;

    protected UpgradeItem(Item.Properties props, boolean foil) {
        super(props);
        this.foil = foil;
        LIST.add(this);
    }

    public abstract List<ModifierInstance> get();

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.foil;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        for (ModifierInstance e : this.get()) {
            list.add(e.mod().getTooltip(e.level()));
            list.addAll(e.mod().getDetail(e.level()));
        }
    }

    public boolean fitsOn(GolemType<?, ?> type) {
        boolean fits = false;
        for (ModifierInstance e : this.get()) {
            fits |= e.mod().fitsOn(type);
        }
        return fits;
    }
}