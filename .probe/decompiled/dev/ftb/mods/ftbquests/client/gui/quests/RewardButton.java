package dev.ftb.mods.ftbquests.client.gui.quests;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.WidgetType;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.client.gui.ContextMenuBuilder;
import dev.ftb.mods.ftbquests.quest.reward.ItemReward;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class RewardButton extends Button {

    private final QuestScreen questScreen;

    private final Reward reward;

    public RewardButton(Panel panel, Reward reward) {
        super(panel, reward.getTitle(), reward.getIcon());
        this.questScreen = (QuestScreen) panel.getGui();
        this.reward = reward;
        this.setSize(18, 18);
    }

    @Override
    public Component getTitle() {
        return (Component) (this.reward.isTeamReward() ? super.getTitle().copy().withStyle(ChatFormatting.BLUE) : super.getTitle());
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        this.questScreen.addInfoTooltip(list, this.reward);
        if (this.reward.addTitleInMouseOverText()) {
            if (this.reward instanceof ItemReward itemReward) {
                TooltipFlag.Default flag = Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL;
                itemReward.getItem().getTooltipLines(FTBQuestsClient.getClientPlayer(), flag).forEach(list::add);
            } else {
                list.add(this.getTitle());
            }
        }
        if (!this.reward.isTeamReward() && !this.questScreen.file.selfTeamData.isRewardBlocked(this.reward)) {
            this.reward.addMouseOverText(list);
            if (!list.shouldRender()) {
                list.zOffset = 580;
            }
        } else {
            this.getIngredientUnderMouse().ifPresent(ingredient -> {
                if (ingredient.tooltip() && ingredient.ingredient() instanceof ItemStack stack && !stack.isEmpty()) {
                    List<Component> list1 = new ArrayList();
                    GuiHelper.addStackTooltip(stack, list1);
                    list1.forEach(list::add);
                }
            });
            list.blankLine();
            this.reward.addMouseOverText(list);
            if (this.reward.isTeamReward()) {
                list.add(Component.translatable("ftbquests.reward.team_reward").withStyle(ChatFormatting.BLUE, ChatFormatting.UNDERLINE));
            } else if (this.questScreen.file.selfTeamData.isRewardBlocked(this.reward)) {
                list.add(Component.translatable("ftbquests.reward.this_blocked", this.questScreen.file.selfTeamData).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            }
        }
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if (!this.isMouseOver()) {
            return false;
        } else {
            if (button.isRight() || this.getWidgetType() != WidgetType.DISABLED) {
                this.onClicked(button);
            }
            return true;
        }
    }

    @Override
    public WidgetType getWidgetType() {
        return ClientQuestFile.exists() && ClientQuestFile.INSTANCE.selfTeamData.isCompleted(this.reward.getQuest()) ? super.getWidgetType() : WidgetType.DISABLED;
    }

    @Override
    public void onClicked(MouseButton button) {
        if (button.isLeft()) {
            if (ClientQuestFile.exists()) {
                this.reward.onButtonClicked(this, ClientQuestFile.INSTANCE.selfTeamData.getClaimType(Minecraft.getInstance().player.m_20148_(), this.reward).canClaim());
            }
        } else if (button.isRight() && ClientQuestFile.exists() && ClientQuestFile.INSTANCE.canEdit()) {
            this.playClickSound();
            ContextMenuBuilder.create(this.reward, this.questScreen).openContextMenu(this.getGui());
        }
    }

    @Override
    public Optional<PositionedIngredient> getIngredientUnderMouse() {
        return PositionedIngredient.of(this.reward.getIngredient(this), this);
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if (this.isMouseOver()) {
            super.drawBackground(graphics, theme, x, y, w, h);
        }
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        int bs = h >= 32 ? 32 : 16;
        GuiHelper.setupDrawing();
        this.drawBackground(graphics, theme, x, y, w, h);
        this.drawIcon(graphics, theme, x + (w - bs) / 2, y + (h - bs) / 2, bs, bs);
        if (this.questScreen.file.selfTeamData != null) {
            if (this.questScreen.getContextMenu().isEmpty()) {
            }
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 200.0F);
            RenderSystem.enableBlend();
            boolean completed = false;
            if (this.questScreen.file.selfTeamData.getClaimType(Minecraft.getInstance().player.m_20148_(), this.reward).isClaimed()) {
                ThemeProperties.CHECK_ICON.get().draw(graphics, x + w - 9, y + 1, 8, 8);
                completed = true;
            } else if (this.questScreen.file.selfTeamData.isCompleted(this.reward.getQuest())) {
                ThemeProperties.ALERT_ICON.get().draw(graphics, x + w - 9, y + 1, 8, 8);
            }
            poseStack.popPose();
            if (!completed) {
                String s = this.reward.getButtonText();
                if (!s.isEmpty()) {
                    poseStack.pushPose();
                    poseStack.translate((double) (x + 19) - (double) theme.getStringWidth(s) / 2.0, (double) (y + 15), 200.0);
                    poseStack.scale(0.5F, 0.5F, 1.0F);
                    theme.drawString(graphics, s, 0, 0, Color4I.WHITE, 2);
                    poseStack.popPose();
                }
            }
        }
    }
}