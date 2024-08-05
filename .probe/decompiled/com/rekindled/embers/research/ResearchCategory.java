package com.rekindled.embers.research;

import com.google.common.collect.Lists;
import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.util.Vec2i;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ResearchCategory {

    public static final ArrayList<ResearchBase> NO_PREREQUISITES = Lists.newArrayList();

    public String name = "";

    public double u = 192.0;

    public double v = 0.0;

    public ResourceLocation texture = new ResourceLocation("embers", "textures/gui/codex_index.png");

    public ResourceLocation background = new ResourceLocation("embers", "textures/gui/codex_category.png");

    public ArrayList<ResearchBase> researches = new ArrayList();

    public ArrayList<ResearchBase> prerequisites = new ArrayList();

    public LinkedList<Vec2i> goodLocations = new LinkedList();

    public ResearchCategory(String name, double v) {
        this.name = name;
        this.v = v;
    }

    public ResearchCategory(String name, double u, double v) {
        this.name = name;
        this.u = u;
        this.v = v;
    }

    public ResearchCategory(String name, ResourceLocation loc, double u, double v) {
        this.name = name;
        this.v = v;
        this.u = u;
        this.texture = loc;
    }

    public ResearchCategory addResearch(ResearchBase base) {
        this.researches.add(base);
        return this;
    }

    public ResearchCategory pushGoodLocations(Vec2i... locations) {
        Collections.addAll(this.goodLocations, locations);
        return this;
    }

    public Vec2i popGoodLocation() {
        return this.goodLocations.isEmpty() ? null : (Vec2i) this.goodLocations.removeFirst();
    }

    public void findByTag(String match, Map<ResearchBase, Integer> result, Set<ResearchCategory> categories) {
        if (!categories.contains(this)) {
            categories.add(this);
            for (ResearchBase research : this.researches) {
                research.findByTag(match, result, categories);
            }
        }
    }

    public ResearchCategory addPrerequisite(ResearchBase base) {
        this.prerequisites.add(base);
        return this;
    }

    public List<ResearchBase> getPrerequisites() {
        return ConfigManager.CODEX_PROGRESSION.get() ? this.prerequisites : NO_PREREQUISITES;
    }

    public boolean isChecked() {
        return this.getPrerequisites().stream().allMatch(ResearchBase::isChecked);
    }

    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return I18n.get("embers.research." + this.name);
    }

    @OnlyIn(Dist.CLIENT)
    public List<Component> getTooltip(boolean showTooltips) {
        ArrayList<Component> tooltip = new ArrayList();
        boolean isChecked = this.isChecked();
        if (showTooltips || !isChecked) {
            for (ResearchBase prerequisite : this.getPrerequisites()) {
                if (!prerequisite.isChecked()) {
                    tooltip.add(Component.translatable("embers.research.prerequisite.locked", prerequisite.getName()));
                }
            }
        }
        return tooltip;
    }

    public double getIconU() {
        return this.u;
    }

    public double getIconV() {
        return this.v;
    }

    public ResourceLocation getBackgroundTexture() {
        return this.background;
    }

    public ResourceLocation getIndexTexture() {
        return this.texture;
    }

    public void getAllResearch(Set<ResearchBase> result) {
        for (ResearchBase research : this.researches) {
            research.getAllResearch(result);
        }
    }
}