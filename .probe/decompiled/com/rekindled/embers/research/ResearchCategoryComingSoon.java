package com.rekindled.embers.research;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ResearchCategoryComingSoon extends ResearchCategory {

    public ResearchCategoryComingSoon(String name, double v) {
        super(name, v);
    }

    public ResearchCategoryComingSoon(String name, double u, double v) {
        super(name, u, v);
    }

    public ResearchCategoryComingSoon(String name, ResourceLocation loc, double u, double v) {
        super(name, loc, u, v);
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Component> getTooltip(boolean showTooltips) {
        ArrayList<Component> tooltip = new ArrayList();
        if (showTooltips) {
            tooltip.add(Component.translatable("embers.research.coming_soon"));
        }
        return tooltip;
    }
}