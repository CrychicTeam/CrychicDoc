package net.zanckor.questapi.example.client.screen.questlog;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractTargetType;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.file.quest.register.QuestTemplateRegistry;
import net.zanckor.questapi.api.registry.EnumRegistry;
import net.zanckor.questapi.api.screen.AbstractQuestLog;
import net.zanckor.questapi.example.client.screen.button.TextButton;
import net.zanckor.questapi.mod.common.network.SendQuestPacket;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;
import net.zanckor.questapi.mod.common.network.packet.screen.RequestActiveQuests;
import net.zanckor.questapi.mod.common.util.MCUtilClient;
import org.jetbrains.annotations.NotNull;

public class QuestLog extends AbstractQuestLog {

    private static final ResourceLocation QUEST_LOG = new ResourceLocation("questapi", "textures/gui/questlog_api.png");

    private static final ResourceLocation TRACK_BUTTON = new ResourceLocation("questapi", "textures/gui/track_button.png");

    int selectedQuestPage = 0;

    int selectedInfoPage = 0;

    double xScreenPos;

    double yScreenPos;

    int imageWidth;

    int imageHeight;

    int questInfoScroll;

    float sin;

    UserQuest selectedQuest;

    int previousGuiScale;

    public QuestLog(Component component) {
        super(component);
    }

    @Override
    public Screen modifyScreen() {
        Minecraft mc = Minecraft.getInstance();
        this.previousGuiScale = mc.options.guiScale().get();
        this.resizeScreen(mc, 2);
        this.init();
        return this;
    }

    @Override
    public void onClose() {
        Minecraft mc = Minecraft.getInstance();
        this.resizeScreen(mc, this.previousGuiScale);
        super.m_7379_();
    }

    private void resizeScreen(Minecraft mc, int scale) {
        mc.options.guiScale().set(scale);
        mc.options.save();
        mc.resizeDisplay();
    }

    @Override
    protected void init() {
        super.m_7856_();
        this.m_169413_();
        this.selectedQuest = null;
        this.questInfoScroll = 0;
        this.imageWidth = this.f_96543_ / 2;
        this.imageHeight = this.f_96543_ / 3;
        this.xScreenPos = (double) (this.f_96543_ - this.imageWidth);
        this.yScreenPos = (double) this.f_96544_ / 4.0;
        int xButtonPosition = (int) (this.xScreenPos - (double) this.imageWidth / 2.6);
        int yButtonPosition = (int) (this.yScreenPos + (double) ((float) this.f_96543_ / 500.0F * 40.0F));
        float buttonScale = (float) this.f_96543_ / 690.0F;
        int maxLength = 120;
        HashMap<Integer, Integer> displayedButton = new HashMap();
        IntStream.range(0, ClientHandler.activeQuestList.size()).forEachOrdered(index -> {
            int buttonIndex = index + 4 * this.selectedQuestPage;
            if (displayedButton.size() < 4 && ClientHandler.activeQuestList.size() > buttonIndex) {
                String title = I18n.get(((UserQuest) ClientHandler.activeQuestList.get(buttonIndex)).getTitle());
                UserQuest currentQuest = (UserQuest) ClientHandler.activeQuestList.get(buttonIndex);
                AtomicBoolean containsSelectedQuest = new AtomicBoolean(false);
                for (UserQuest trackedQuest : ClientHandler.trackedQuestList) {
                    if (trackedQuest != null && trackedQuest.getId().equals(currentQuest.getId())) {
                        containsSelectedQuest.set(true);
                    }
                }
                int yUVOffset = containsSelectedQuest.get() ? (int) (14.0F * buttonScale) : 0;
                int textLines = (int) Math.ceil((double) (title.length() * 5) / (double) maxLength);
                float buttonWidth = textLines <= 1 ? (float) (title.length() * 5) * buttonScale : (float) maxLength * buttonScale;
                Button questSelect = new TextButton(xButtonPosition, yButtonPosition, (int) buttonWidth, 20, (float) this.f_96543_ / 800.0F, Component.literal(title), 24, button -> {
                    this.selectedQuest = currentQuest;
                    SendQuestPacket.TO_SERVER(new RequestActiveQuests());
                });
                ImageButton trackQuest = new ImageButton((int) ((double) xButtonPosition * 0.925), 0, this.f_96543_ / 50, this.f_96543_ / 50, 0, yUVOffset, 0, TRACK_BUTTON, this.f_96543_ / 50, this.f_96543_ / 25, button -> {
                    ClientHandler.modifyTrackedQuests(!containsSelectedQuest.get(), currentQuest);
                    this.init();
                });
                displayedButton.put(displayedButton.size() + 1, index);
                questSelect.m_253211_((int) ((float) questSelect.m_252907_() + (float) buttonIndex * 30.0F * buttonScale));
                trackQuest.m_253211_(questSelect.m_252907_());
                this.m_142416_(trackQuest);
                this.m_142416_(questSelect);
            }
        });
        Button questPreviousPage = MCUtilClient.createButton((int) (this.xScreenPos - (double) this.imageWidth / 2.25), (int) (this.yScreenPos + (double) this.imageHeight * 0.81), this.f_96543_ / 50, this.f_96543_ / 50, Component.nullToEmpty(""), button -> {
            if (this.selectedQuestPage > 0) {
                this.selectedQuestPage--;
                this.init();
            }
        });
        Button questNextPage = MCUtilClient.createButton((int) (this.xScreenPos + (double) this.imageWidth / 2.5), (int) (this.yScreenPos + (double) this.imageHeight * 0.81), this.f_96543_ / 50, this.f_96543_ / 50, Component.nullToEmpty(""), button -> {
            if ((double) (this.selectedQuestPage + 1) < (double) ClientHandler.activeQuestList.size() / 4.0) {
                this.selectedQuestPage++;
                this.init();
            }
        });
        this.m_7787_(questPreviousPage);
        this.m_7787_(questNextPage);
        SendQuestPacket.TO_SERVER(new RequestActiveQuests());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int x, int y, float partialTicks) {
        float scale = (float) this.f_96543_ / 500.0F;
        PoseStack poseStack = graphics.pose();
        Minecraft.getInstance().getProfiler().push("background");
        graphics.blit(QUEST_LOG, (int) (this.xScreenPos - (double) (this.imageWidth / 2)), (int) this.yScreenPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        this.renderQuestData(graphics, poseStack);
        MCUtilClient.renderText(graphics, poseStack, (double) ((float) (this.xScreenPos - (double) this.imageWidth / 2.9)), (double) ((float) (this.yScreenPos + (double) (20.0F * scale))), 0.0F, scale, 40, "Quest Title", this.f_96547_);
        MCUtilClient.renderText(graphics, poseStack, (double) ((float) (this.xScreenPos + (double) this.imageWidth * 0.135)), (double) ((float) (this.yScreenPos + (double) (20.0F * scale))), 0.0F, scale, 40, "Quest Info", this.f_96547_);
        Minecraft.getInstance().getProfiler().pop();
        super.m_88315_(graphics, x, y, partialTicks);
    }

    public void renderQuestData(GuiGraphics graphics, PoseStack poseStack) {
        if (this.selectedQuest != null && !this.selectedQuest.isCompleted()) {
            HashMap<String, List<UserGoal>> userQuestHashMap = new HashMap();
            int xPosition = (int) ((double) this.f_96543_ / 1.925);
            float scale = (float) this.f_96543_ / 700.0F;
            poseStack.pushPose();
            poseStack.translate((double) xPosition, this.yScreenPos + (double) ((float) this.f_96543_ / 500.0F * 40.0F), 0.0);
            poseStack.scale(scale, scale, 0.0F);
            for (UserGoal questGoal : this.selectedQuest.getQuestGoals()) {
                String type = questGoal.getType();
                List<UserGoal> questGoalList = (List<UserGoal>) userQuestHashMap.get(type);
                if (questGoalList == null) {
                    questGoalList = new ArrayList();
                }
                questGoalList.add(questGoal);
                userQuestHashMap.put(type, questGoalList);
            }
            this.renderTitle(graphics, poseStack, this.f_96541_);
            if (this.selectedInfoPage == 0) {
                this.renderGoals(graphics, poseStack, this.f_96541_, userQuestHashMap);
            } else {
                this.renderDescription(graphics, poseStack);
            }
            if (this.selectedQuest.hasTimeLimit()) {
                MCUtilClient.renderLine(graphics, poseStack, 0.0F, 0.0F, 30.0F, I18n.get("tracker.questapi.time_limit") + this.selectedQuest.getTimeLimitInSeconds(), this.f_96547_);
            }
            poseStack.popPose();
        }
    }

    public void renderTitle(GuiGraphics graphics, PoseStack poseStack, Minecraft minecraft) {
        String title = I18n.get(this.selectedQuest.getTitle());
        MCUtilClient.renderLines(graphics, poseStack, 25.0F, 10, 30, I18n.get("tracker.questapi.quest") + title, minecraft.font);
    }

    public void renderGoals(GuiGraphics graphics, PoseStack poseStack, Minecraft minecraft, HashMap<String, List<UserGoal>> userQuestHashMap) {
        int scissorBottom = (int) ((double) this.f_96544_ * 0.4);
        int scissorTop = (int) ((double) this.f_96544_ * 0.7);
        RenderSystem.enableScissor(this.f_96543_, scissorBottom, this.f_96543_ / 2 + this.imageWidth / 2, scissorTop);
        Font font = minecraft.font;
        Player player = minecraft.player;
        poseStack.translate(0.0F, (float) (this.questInfoScroll + 10), 0.0F);
        this.sin = (float) ((double) this.sin + 0.5);
        for (Entry<String, List<UserGoal>> entry : userQuestHashMap.entrySet()) {
            List<UserGoal> questGoalList = (List<UserGoal>) entry.getValue();
            MCUtilClient.renderLine(graphics, poseStack, 30, 0.0F, 0.0F, 10.0F, Component.literal(I18n.get("tracker.questapi.quest_type") + I18n.get("quest_type." + ((UserGoal) questGoalList.get(0)).getTranslatableType().toLowerCase())).withStyle(ChatFormatting.BLACK), font);
            for (UserGoal questGoal : questGoalList) {
                Enum<?> goalEnum = EnumRegistry.getEnum("TARGET_TYPE_" + questGoal.getType(), EnumRegistry.getTargetType());
                AbstractTargetType translatableTargetType = QuestTemplateRegistry.getTranslatableTargetType(goalEnum);
                MutableComponent goalComponentTarget = translatableTargetType.handler(questGoal.getTarget(), questGoal, player, ChatFormatting.GRAY, ChatFormatting.BLACK);
                translatableTargetType.renderTarget(poseStack, goalComponentTarget.getString().length() * 6 - 4, 3, 0.7, Math.sin((double) this.sin), questGoal, questGoal.getTarget());
                MCUtilClient.renderLine(graphics, poseStack, 30, 0.0F, 0.0F, 10.0F, goalComponentTarget.withStyle(ChatFormatting.ITALIC), font);
            }
            poseStack.translate(0.0F, 10.0F, 0.0F);
        }
        this.renderDescription(graphics, poseStack);
        RenderSystem.disableScissor();
    }

    public void renderDescription(GuiGraphics graphics, PoseStack poseStack) {
        if (this.selectedQuest.getDescription() != null) {
            MCUtilClient.renderLine(graphics, poseStack, 30, 30.0F, 0.0F, 18.0F, "§l§nDESCRIPTION", this.f_96547_);
            poseStack.translate(-8.0F, 0.0F, 0.0F);
            MCUtilClient.renderLine(graphics, poseStack, 30, 0.0F, 0.0F, 18.0F, this.selectedQuest.getDescription(), this.f_96547_);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        boolean mouseXInBox = mouseX > this.xScreenPos && mouseX < this.xScreenPos + (double) this.imageWidth / 2.0;
        boolean mouseYInBox = mouseY > this.yScreenPos && mouseY < this.yScreenPos + (double) this.imageHeight;
        if (mouseXInBox && mouseYInBox) {
            this.questInfoScroll = (int) ((double) this.questInfoScroll + scroll * 4.0);
            if (this.questInfoScroll > 0) {
                this.questInfoScroll = 0;
            }
        }
        return super.m_6050_(mouseX, mouseY, scroll);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}