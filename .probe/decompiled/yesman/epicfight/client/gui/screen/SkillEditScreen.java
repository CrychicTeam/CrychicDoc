package yesman.epicfight.client.gui.screen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.client.CPChangeSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.world.capabilities.skill.CapabilitySkill;

@OnlyIn(Dist.CLIENT)
public class SkillEditScreen extends Screen {

    private static final ResourceLocation SKILL_EDIT_UI = new ResourceLocation("epicfight", "textures/gui/screen/skill_edit.png");

    private static final MutableComponent NO_SKILLS = Component.literal("gui.epicfight.no_skills");

    private static final int MAX_SHOWING_BUTTONS = 6;

    private final Player player;

    private final CapabilitySkill skills;

    private final Map<SkillSlot, SkillEditScreen.SlotButton> slotButtons = Maps.newHashMap();

    private final List<SkillEditScreen.LearnSkillButton> learnedSkillButtons = Lists.newArrayList();

    private int start;

    private SkillEditScreen.SlotButton selectedSlotButton;

    public SkillEditScreen(Player player, CapabilitySkill skills) {
        super(Component.translatable("gui.epicfight.skill_edit"));
        this.player = player;
        this.skills = skills;
    }

    @Override
    public void init() {
        int i = this.f_96543_ / 2 - 96;
        int j = this.f_96544_ / 2 - 82;
        this.slotButtons.clear();
        this.learnedSkillButtons.clear();
        for (SkillSlot skillSlot : SkillSlot.ENUM_MANAGER.universalValues()) {
            if (this.skills.hasCategory(skillSlot.category()) && skillSlot.category().learnable()) {
                SkillEditScreen.SlotButton slotButton = new SkillEditScreen.SlotButton(i, j, 18, 18, skillSlot, this.skills.skillContainers[skillSlot.universalOrdinal()].getSkill(), button -> {
                    this.start = 0;
                    for (Button shownButton : this.learnedSkillButtons) {
                        this.m_6702_().remove(shownButton);
                    }
                    this.learnedSkillButtons.clear();
                    int k = this.f_96543_ / 2 - 69;
                    int l = this.f_96544_ / 2 - 78;
                    for (Skill learnedSkill : this.skills.getLearnedSkills(skillSlot.category())) {
                        this.learnedSkillButtons.add(new SkillEditScreen.LearnSkillButton(k, l, 147, 24, learnedSkill, Component.translatable(learnedSkill.getTranslationKey()), pressedButton -> {
                            if (this.f_96541_.player.f_36078_ >= learnedSkill.getRequiredXp() || this.f_96541_.player.m_7500_()) {
                                if (!this.canPress(pressedButton)) {
                                    return;
                                }
                                this.skills.skillContainers[skillSlot.universalOrdinal()].setSkill(learnedSkill);
                                EpicFightNetworkManager.sendToServer(new CPChangeSkill(skillSlot.universalOrdinal(), -1, learnedSkill.toString(), !this.f_96541_.player.m_7500_()));
                                this.m_7379_();
                            }
                        }).setActive(this.skills.getSkillContainer(learnedSkill) == null));
                        l += 26;
                    }
                    for (Button shownButton : this.learnedSkillButtons) {
                        this.m_7787_(shownButton);
                    }
                    this.selectedSlotButton = (SkillEditScreen.SlotButton) button;
                }, Component.translatable(SkillSlot.ENUM_MANAGER.toTranslated(skillSlot)));
                this.slotButtons.put(skillSlot, slotButton);
                this.m_7787_(slotButton);
                j += 18;
            }
        }
        if (this.selectedSlotButton != null) {
            this.selectedSlotButton = (SkillEditScreen.SlotButton) this.slotButtons.get(this.selectedSlotButton.slot);
            this.selectedSlotButton.m_5691_();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        if (this.canScroll()) {
            int scrollPosition = (int) (140.0F * ((float) this.start / (float) (this.learnedSkillButtons.size() - 6)));
            guiGraphics.blit(SKILL_EDIT_UI, this.f_96543_ / 2 + 80, this.f_96544_ / 2 - 80 + scrollPosition, 12, 15, 231.0F, 2.0F, 12, 15, 256, 256);
        }
        int maxShowingButtons = Math.min(this.learnedSkillButtons.size(), 6);
        for (int i = this.start; i < maxShowingButtons + this.start; i++) {
            ((SkillEditScreen.LearnSkillButton) this.learnedSkillButtons.get(i)).render(guiGraphics, mouseX, mouseY, partialTicks);
        }
        for (SkillEditScreen.SlotButton sb : this.slotButtons.values()) {
            sb.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
        if (this.slotButtons.isEmpty()) {
            int lineHeight = 0;
            for (FormattedCharSequence s : this.f_96547_.split(NO_SKILLS, 110)) {
                guiGraphics.drawString(this.f_96547_, s, this.f_96543_ / 2 - 50, this.f_96544_ / 2 - 72 + lineHeight, 3158064, false);
                lineHeight += 10;
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        super.renderBackground(guiGraphics);
        guiGraphics.blit(SKILL_EDIT_UI, this.f_96543_ / 2 - 104, this.f_96544_ / 2 - 100, 0, 0, 208, 200);
    }

    private boolean canScroll() {
        return this.learnedSkillButtons.size() > 6;
    }

    private boolean canPress(Button button) {
        int buttonOrder = this.learnedSkillButtons.indexOf(button);
        return buttonOrder >= this.start && buttonOrder <= this.start + 6;
    }

    @Override
    public boolean mouseScrolled(double x, double y, double wheel) {
        if (!this.canScroll()) {
            return false;
        } else {
            if (wheel > 0.0) {
                if (this.start > 0) {
                    this.start--;
                    for (Button button : this.learnedSkillButtons) {
                        button.m_253211_(button.m_252907_() + 26);
                    }
                    return true;
                }
            } else if (this.start < this.learnedSkillButtons.size() - 6) {
                this.start++;
                for (Button button : this.learnedSkillButtons) {
                    button.m_253211_(button.m_252907_() - 26);
                }
                return true;
            }
            return false;
        }
    }

    class LearnSkillButton extends Button {

        private final Skill skill;

        public LearnSkillButton(int x, int y, int width, int height, Skill skill, Component title, Button.OnPress pressedAction) {
            super(x, y, width, height, title, pressedAction, Button.DEFAULT_NARRATION);
            this.skill = skill;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            this.f_93622_ = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            int texY = !this.f_93622_ && this.f_93623_ ? 200 : 224;
            guiGraphics.blit(SkillEditScreen.SKILL_EDIT_UI, this.m_252754_(), this.m_252907_(), 0, texY, this.f_93618_, this.f_93619_);
            RenderSystem.enableBlend();
            guiGraphics.blit(this.skill.getSkillTexture(), this.m_252754_() + 5, this.m_252907_() + 4, 16, 16, 0.0F, 0.0F, 128, 128, 128, 128);
            guiGraphics.drawString(SkillEditScreen.this.f_96547_, this.m_6035_(), this.m_252754_() + 26, this.m_252907_() + 2, -1, false);
            if (this.f_93623_) {
                int color = SkillEditScreen.this.f_96541_.player.f_36078_ < this.skill.getRequiredXp() && !SkillEditScreen.this.f_96541_.player.m_7500_() ? 16736352 : 8453920;
                guiGraphics.drawString(SkillEditScreen.this.f_96547_, Component.translatable("gui.epicfight.changing_cost", this.skill.getRequiredXp()), this.m_252754_() + 70, this.m_252907_() + 12, color, false);
            } else {
                guiGraphics.drawString(SkillEditScreen.this.f_96547_, Component.literal(SkillEditScreen.this.skills.getSkillContainer(this.skill).getSlot().toString().toLowerCase(Locale.ROOT)), this.m_252754_() + 26, this.m_252907_() + 12, 16736352, false);
            }
        }

        @Override
        public boolean mouseClicked(double x, double y, int pressType) {
            if (this.f_93624_ && pressType == 1) {
                boolean flag = this.clickedNoCountActive(x, y);
                if (flag) {
                    this.m_7435_(Minecraft.getInstance().getSoundManager());
                    SkillEditScreen.this.f_96541_.setScreen(new SkillBookScreen(SkillEditScreen.this.player, this.skill, null, SkillEditScreen.this));
                    return true;
                }
            }
            return super.m_6375_(x, y, pressType);
        }

        protected boolean clickedNoCountActive(double x, double y) {
            return this.f_93624_ && x >= (double) this.m_252754_() && y >= (double) this.m_252907_() && x < (double) (this.m_252754_() + this.f_93618_) && y < (double) (this.m_252907_() + this.f_93619_);
        }

        public SkillEditScreen.LearnSkillButton setActive(boolean active) {
            this.f_93623_ = active;
            return this;
        }
    }

    class SlotButton extends Button {

        private final Skill iconSkill;

        private final SkillSlot slot;

        public SlotButton(int x, int y, int width, int height, SkillSlot slot, Skill skill, Button.OnPress pressedAction, Component tooltipMessage) {
            super(x, y, width, height, Component.empty(), pressedAction, Button.DEFAULT_NARRATION);
            this.iconSkill = skill;
            this.slot = slot;
            this.m_257544_(Tooltip.create(tooltipMessage));
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            int y = !this.f_93622_ && SkillEditScreen.this.selectedSlotButton != this ? 17 : 35;
            guiGraphics.blit(SkillEditScreen.SKILL_EDIT_UI, this.m_252754_(), this.m_252907_(), 237, y, this.f_93618_, this.f_93619_);
            if (this.iconSkill != null) {
                RenderSystem.enableBlend();
                guiGraphics.blit(this.iconSkill.getSkillTexture(), this.m_252754_() + 1, this.m_252907_() + 1, 16, 16, 0.0F, 0.0F, 128, 128, 128, 128);
            }
        }
    }
}