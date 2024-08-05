package com.rekindled.embers.research;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.gui.GuiCodex;
import com.rekindled.embers.util.Vec2i;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ResearchBase {

    public String name = "";

    public double u = 0.0;

    public double v = 0.0;

    public ItemStack icon = ItemStack.EMPTY;

    public int x = 0;

    public int y = 0;

    public List<ResearchBase> ancestors = new ArrayList();

    public ResearchBase subCategory = null;

    public ResourceLocation iconBackground = ResearchManager.PAGE_ICONS;

    public ResourceLocation background = new ResourceLocation("embers", "textures/gui/codex_normal.png");

    public ResearchBase firstPage;

    public int pageNumber;

    List<ResearchBase> pages = new ArrayList();

    public float selectedAmount = 0.0F;

    public float selectionTarget = 0.0F;

    public float shownAmount = 0.0F;

    public float shownTarget = 0.0F;

    public boolean checked;

    public ResearchBase(String location, ItemStack icon, double x, double y) {
        this.name = location;
        this.icon = icon;
        this.x = 48 + (int) (x * 24.0);
        this.y = 48 + (int) (y * 24.0);
    }

    public ResearchBase(String location, ItemStack icon, Vec2i pos) {
        this(location, icon, (double) pos.x, (double) pos.y);
    }

    public List<ResearchCategory> getNeededFor() {
        ArrayList<ResearchCategory> neededFor = new ArrayList();
        if (ConfigManager.CODEX_PROGRESSION.get()) {
            for (ResearchCategory category : ResearchManager.researches) {
                if (category.prerequisites.contains(this)) {
                    neededFor.add(category);
                }
            }
        }
        return neededFor;
    }

    public List<ResearchBase> getAllRequirements() {
        if (this.subCategory == null) {
            return this.ancestors;
        } else {
            List<ResearchBase> requirements = new ArrayList();
            requirements.addAll(this.ancestors);
            requirements.add(this.subCategory);
            return requirements;
        }
    }

    public void findByTag(String match, Map<ResearchBase, Integer> result, Set<ResearchCategory> categories) {
        if (!result.containsKey(this)) {
            String[] matchParts = match.split("\\|");
            int totalScore = 0;
            for (String matchPart : matchParts) {
                if (!matchPart.isEmpty()) {
                    int tagScore = this.matchTags(matchPart);
                    int nameScore = this.scoreMatches(this.getName(), matchPart);
                    int textScore = this.scoreMatches(this.getText().getString(), matchPart);
                    int score = textScore + tagScore * 100 + nameScore * 1000;
                    if (score <= 0) {
                        return;
                    }
                    totalScore += score;
                }
            }
            if (totalScore > 0) {
                result.put(this, totalScore);
            }
        }
    }

    public int matchTags(String match) {
        int score = 0;
        int matches = 0;
        for (String tag : this.getTags()) {
            int matchScore = this.scoreMatches(tag, match);
            if (matchScore > 0) {
                matches++;
            }
            score += matchScore;
        }
        return score + matches * 100;
    }

    private int scoreMatches(String tag, String match) {
        tag = tag.toLowerCase();
        match = match.toLowerCase();
        int matches = 0;
        int positionalScore = 0;
        int index = 0;
        do {
            index = tag.indexOf(match, index);
            if (index >= 0) {
                matches++;
                positionalScore += tag.length() - index;
                index++;
            }
        } while (index >= 0);
        return matches * 10 + positionalScore;
    }

    public ResearchBase addAncestor(ResearchBase base) {
        this.ancestors.add(base);
        return this;
    }

    public ResearchBase setIconBackground(ResourceLocation resourceLocation, double u, double v) {
        this.iconBackground = resourceLocation;
        this.u = u;
        this.v = v;
        return this;
    }

    public ResearchBase setBackground(ResourceLocation resourceLocation) {
        this.background = resourceLocation;
        return this;
    }

    public ResearchBase addPage(ResearchBase page) {
        if (this.firstPage != null) {
            return this.firstPage.addPage(page);
        } else {
            this.pages.add(page);
            page.pageNumber = this.getPageCount();
            page.firstPage = this.getFirstPage();
            return this;
        }
    }

    public boolean isHidden() {
        return false;
    }

    public void check(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public boolean areAncestorsChecked() {
        return !ConfigManager.CODEX_PROGRESSION.get() ? true : this.isChecked() || this.ancestors.stream().allMatch(ResearchBase::isChecked);
    }

    @OnlyIn(Dist.CLIENT)
    public List<Component> getTooltip(boolean showTooltips) {
        ArrayList<Component> tooltip = new ArrayList();
        if (showTooltips || !this.isChecked()) {
            for (ResearchCategory neededFor : this.getNeededFor()) {
                tooltip.add(Component.translatable("embers.research.prerequisite", neededFor.getName()));
            }
        }
        return tooltip;
    }

    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return I18n.get("embers.research.page." + this.name);
    }

    @OnlyIn(Dist.CLIENT)
    public String getTitle() {
        return this.hasMultiplePages() ? I18n.get("embers.research.multipage", I18n.get("embers.research.page." + this.getFirstPage().name + ".title"), this.pageNumber + 1, this.getPageCount() + 1) : I18n.get("embers.research.page." + this.name + ".title");
    }

    @OnlyIn(Dist.CLIENT)
    private String[] getTags() {
        String translateKey = "embers.research.page." + this.name + ".tags";
        return I18n.exists(translateKey) ? I18n.get(translateKey).split(";") : new String[0];
    }

    @OnlyIn(Dist.CLIENT)
    public Component getText() {
        return Component.translatable("embers.research.page." + this.name + ".desc");
    }

    @OnlyIn(Dist.CLIENT)
    public List<FormattedCharSequence> getLines(Font fontRenderer, FormattedText s, int width) {
        return fontRenderer.split(s, width);
    }

    public ResourceLocation getBackground() {
        return this.background;
    }

    public ResourceLocation getIconBackground() {
        return this.iconBackground;
    }

    public double getIconBackgroundU() {
        return this.u;
    }

    public double getIconBackgroundV() {
        return this.v;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public boolean hasMultiplePages() {
        return this.getPageCount() > 0;
    }

    public ResearchBase getPage(int i) {
        i = Mth.clamp(i, 0, this.getPageCount());
        return i <= 0 ? this.getFirstPage() : (ResearchBase) this.getPages().get(i - 1);
    }

    public ResearchBase getFirstPage() {
        return this.firstPage != null ? this.firstPage : this;
    }

    public ResearchBase getNextPage() {
        return this.getPage(this.pageNumber + 1);
    }

    public ResearchBase getPreviousPage() {
        return this.getPage(this.pageNumber - 1);
    }

    public int getPageCount() {
        return this.getPages().size();
    }

    public List<ResearchBase> getPages() {
        return this.firstPage != null ? this.firstPage.pages : this.pages;
    }

    public boolean onOpen(GuiCodex gui) {
        return true;
    }

    public boolean onClose(GuiCodex gui) {
        return true;
    }

    public void renderPageContent(GuiGraphics graphics, GuiCodex gui, int basePosX, int basePosY, Font fontRenderer) {
        List<FormattedCharSequence> strings = this.getLines(fontRenderer, this.getText(), 152);
        for (int i = 0; i < Math.min(strings.size(), 17); i++) {
            GuiCodex.drawTextGlowing(fontRenderer, graphics, (FormattedCharSequence) strings.get(i), basePosX + 20, basePosY + 43 + i * (9 + 3));
        }
    }

    public void getAllResearch(Set<ResearchBase> result) {
        if (!result.contains(this)) {
            result.add(this);
        }
    }

    public boolean isPathTowards(ResearchBase target) {
        return this == target;
    }
}