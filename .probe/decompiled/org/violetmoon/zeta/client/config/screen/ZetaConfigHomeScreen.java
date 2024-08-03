package org.violetmoon.zeta.client.config.screen;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.client.config.widget.CategoryButton;
import org.violetmoon.zeta.client.config.widget.CheckboxButton;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.SectionDefinition;
import org.violetmoon.zeta.config.ValueDefinition;
import org.violetmoon.zeta.module.ZetaCategory;

public class ZetaConfigHomeScreen extends ZetaScreen {

    protected final ChangeSet changeSet = new ChangeSet(this.z.configInternals);

    protected Button saveButton;

    public ZetaConfigHomeScreen(ZetaClient zc, Screen parent) {
        super(zc, parent);
    }

    @Override
    protected void init() {
        super.m_7856_();
        List<ZetaCategory> categories = this.z.modules.getInhabitedCategories();
        SectionDefinition generalSection = this.z.configManager.getGeneralSection();
        int buttonCount = categories.size();
        if (generalSection != null) {
            buttonCount++;
        }
        int perLine = 3;
        List<Integer> categoryButtonXPositions = new ArrayList(buttonCount);
        for (int i = 0; i < buttonCount; i += 3) {
            categoryButtonXPositions.addAll(this.centeredRow(this.f_96543_ / 2, 120, 10, Math.min(buttonCount - i, 3)));
        }
        for (int i = 0; i < buttonCount; i++) {
            int row = i / 3;
            int x = (Integer) categoryButtonXPositions.get(i);
            int y = 70 + row * 23;
            int bWidth = 120;
            if (i < categories.size()) {
                ZetaCategory category = (ZetaCategory) categories.get(i);
                ValueDefinition<Boolean> categoryEnabled = this.z.configManager.getCategoryEnabledOption(category);
                SectionDefinition categorySection = this.z.configManager.getCategorySection(category);
                bWidth -= 20;
                Button mainButton = (Button) this.m_142416_(new CategoryButton(x, y, bWidth, 20, this.componentFor(categorySection), (ItemStack) category.icon.get(), b -> Minecraft.getInstance().setScreen(new SectionScreen(this.zc, this, this.changeSet, categorySection))));
                Button checkButton = (Button) this.m_142416_(new CheckboxButton(this.zc, x + bWidth, y, this.changeSet, categoryEnabled));
                boolean active = category.requiredModsLoaded(this.z);
                mainButton.f_93623_ = active;
                checkButton.f_93623_ = active;
            } else {
                assert generalSection != null;
                this.m_142416_(new Button.Builder(this.componentFor(generalSection), b -> Minecraft.getInstance().setScreen(new SectionScreen(this.zc, this, this.changeSet, generalSection))).size(bWidth, 20).pos(x, y).build());
            }
        }
        this.saveButton = (Button) this.m_142416_(new Button.Builder(this.componentForSaveButton(), this::commit).size(200, 20).pos(this.f_96543_ / 2 - 100, this.f_96544_ - 30).build());
    }

    public List<Integer> centeredRow(int centerX, int buttonWidth, int hpad, int count) {
        int slop = (count % 2 == 0 ? hpad : buttonWidth) / 2;
        int fullButtonsLeftOfCenter = count / 2;
        int fullPaddingsLeftOfCenter = Math.max(0, (count - 1) / 2);
        int startX = centerX - slop - fullButtonsLeftOfCenter * buttonWidth - fullPaddingsLeftOfCenter * hpad;
        List<Integer> result = new ArrayList(count);
        int x = startX;
        for (int i = 0; i < count; i++) {
            result.add(x);
            x += buttonWidth + hpad;
        }
        return result;
    }

    private Component componentFor(SectionDefinition section) {
        MutableComponent comp = Component.translatable(this.z.modid + ".category." + section.name);
        if (this.changeSet.isDirty(section)) {
            comp.append(Component.literal("*").withStyle(ChatFormatting.GOLD));
        }
        return comp;
    }

    private Component componentForSaveButton() {
        MutableComponent comp = Component.translatable("quark.gui.config.save");
        int changeCount = this.changeSet.changeCount();
        if (changeCount > 0) {
            comp.append(" (").append(Component.literal(String.valueOf(changeCount)).withStyle(ChatFormatting.GOLD)).append(")");
        }
        return comp;
    }

    public void commit(Button button) {
        this.changeSet.applyAllChanges();
        this.returnToParent();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.f_96547_, ChatFormatting.BOLD + I18n.get("quark.gui.config.header", WordUtils.capitalizeFully(this.z.modid)), this.f_96543_ / 2, 15, 4775356);
    }

    @Override
    public void tick() {
        super.m_86600_();
        this.saveButton.m_93666_(this.componentForSaveButton());
    }
}