package io.github.lightman314.lightmanscurrency.common.menus.slots.easy;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import org.jetbrains.annotations.Nullable;

public abstract class EasyMultiBGSlot extends EasySlot {

    public EasyMultiBGSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    protected abstract List<Pair<ResourceLocation, ResourceLocation>> getPossibleNoItemIcons();

    @Nullable
    @Override
    public final Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        Minecraft mc = Minecraft.getInstance();
        int timer = (int) (mc.level.m_46467_() / 20L);
        List<Pair<ResourceLocation, ResourceLocation>> bgs = this.getPossibleNoItemIcons();
        return bgs != null && bgs.size() != 0 ? (Pair) bgs.get(timer % bgs.size()) : null;
    }
}