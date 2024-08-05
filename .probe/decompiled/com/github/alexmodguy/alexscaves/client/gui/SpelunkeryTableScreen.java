package com.github.alexmodguy.alexscaves.client.gui;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.inventory.SpelunkeryTableMenu;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.item.CaveInfoItem;
import com.github.alexmodguy.alexscaves.server.message.SpelunkeryTableChangeMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;

public class SpelunkeryTableScreen extends AbstractContainerScreen<SpelunkeryTableMenu> {

    protected static final Style GLYPH_FONT = Style.EMPTY.withFont(new ResourceLocation("minecraft", "alt"));

    public static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves", "textures/gui/spelunkery_table.png");

    public static final ResourceLocation TABLET_TEXTURE = new ResourceLocation("alexscaves", "textures/gui/spelunkery_table_tablet.png");

    public static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation("alexscaves", "textures/gui/spelunkery_table_widgets.png");

    public static final ResourceLocation DEFAULT_WORDS = new ResourceLocation("alexscaves", "minigame/en_us/magnetic_caves.txt");

    private int tickCount = 0;

    private int attemptsLeft = 0;

    private boolean draggingMagnify = false;

    private float magnifyPosX;

    private float magnifyPosY;

    private float prevMagnifyPosX;

    private float prevMagnifyPosY;

    private int lastMouseX;

    private int lastMouseY;

    private ResourceLocation prevWordsFile = null;

    private List<SpelunkeryTableWordButton> wordButtons = new ArrayList();

    private SpelunkeryTableWordButton targetWordButton = null;

    private int highlightColor = 16777215;

    private int level = 0;

    private boolean finishedLevel;

    private float prevPassLevelProgress = 0.0F;

    private float passLevelProgress = 0.0F;

    private int tutorialStep = 0;

    private boolean hasClickedLens = false;

    private boolean doneWithTutorial = false;

    private boolean invalidTablet = false;

    private ItemStack lastTablet;

    private Random random = new Random();

    public SpelunkeryTableScreen(SpelunkeryTableMenu menu, Inventory inventory, Component name) {
        super(menu, inventory, name);
        this.f_97726_ = 208;
        this.f_97727_ = 256;
        this.f_97728_ = this.f_97726_ / 2;
    }

    @Override
    protected void init() {
        super.init();
        this.f_97735_ = (this.f_96543_ - this.f_97726_) / 2;
        this.magnifyPosX = (float) (this.f_97735_ + 170);
        this.prevMagnifyPosX = this.magnifyPosX;
        this.magnifyPosY = (float) (this.f_97736_ + 130);
        this.prevMagnifyPosY = this.magnifyPosY;
        for (SpelunkeryTableWordButton button : this.wordButtons) {
            this.m_142416_(button);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        this.m_280273_(guiGraphics);
        this.renderBg(guiGraphics, partialTick, x, y);
        super.render(guiGraphics, x, y, partialTick);
        this.renderMagnify(guiGraphics, partialTick);
        this.renderTooltip(guiGraphics, x, y);
        this.renderDescText(guiGraphics);
        this.renderTabletText(guiGraphics);
    }

    private void renderDescText(GuiGraphics guiGraphics) {
        int i = this.f_97735_ - 58;
        int j = this.f_97736_;
        if (this.invalidTablet) {
            Component badTablet = Component.translatable("alexscaves.container.spelunkery_table.bad_tablet");
            guiGraphics.drawString(this.f_96547_, badTablet, this.f_97735_ + 105 - this.f_96547_.width(badTablet) / 2, j + 60, 0, false);
            CompoundTag badTag = ((SpelunkeryTableMenu) this.f_97732_).m_38853_(0).getItem().getTag();
            if (badTag != null && !badTag.isEmpty()) {
                int nbtLine = 0;
                for (String key : badTag.getAllKeys()) {
                    Component badData = Component.literal(key + ": " + badTag.get(key).getAsString());
                    guiGraphics.drawString(this.f_96547_, badData, this.f_97735_ + 105 - this.f_96547_.width(badData) / 2, j + 75 + nbtLine, 0, false);
                    nbtLine += 9;
                }
            }
        } else if (this.targetWordButton != null && this.hasTablet() && this.hasPaper()) {
            Component find = Component.translatable("alexscaves.container.spelunkery_table.find");
            Component attempts = Component.translatable("alexscaves.container.spelunkery_table.attempts");
            guiGraphics.drawString(this.f_96547_, find, i + 20 - this.f_96547_.width(find) / 2, j + 20, 10061676, false);
            guiGraphics.drawString(this.f_96547_, this.targetWordButton.getNormalText(), i + 20 - this.f_96547_.width(this.targetWordButton.getNormalText()) / 2, j + 35, this.highlightColor, false);
            guiGraphics.drawString(this.f_96547_, attempts, i + 20 - this.f_96547_.width(attempts) / 2, j + 60, 10061676, false);
            int tallySpace = 0;
            for (int tally = 1; tally <= this.attemptsLeft; tally++) {
                if (tally % 5 == 0) {
                    guiGraphics.blit(WIDGETS_TEXTURE, i + 10 + tallySpace - 22, j + 70, 3, 52, 27, 14);
                    tallySpace += 7;
                } else {
                    guiGraphics.blit(WIDGETS_TEXTURE, i + 10 + tallySpace, j + 70, 0, 52, 3, 14);
                    guiGraphics.blit(WIDGETS_TEXTURE, i + 10 + tallySpace, j + 70, 0, 52, 3, 14);
                    tallySpace += 4;
                }
            }
        }
    }

    private void renderTabletText(GuiGraphics guiGraphics) {
        float partialTick = Minecraft.getInstance().getPartialTick();
        float x = this.getMagnifyPosX(partialTick);
        float y = this.getMagnifyPosY(partialTick);
        if (this.hasTablet()) {
            guiGraphics.pose().pushPose();
            for (Renderable renderable : this.f_169369_) {
                if (renderable instanceof SpelunkeryTableWordButton tableWordButton) {
                    tableWordButton.renderTranslationText(this.tickCount, this.highlightColor, guiGraphics, this.f_96547_, x + 5.0F, x + 32.0F, y + 6.0F, y + 32.0F);
                }
            }
            guiGraphics.pose().popPose();
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.isFirstTimeUsing() && this.tutorialStep < 6) {
            int i = this.f_97735_;
            int j = this.f_97736_;
            int exclaimX = 0;
            int exclaimY = 0;
            if (this.tutorialStep == 0) {
                exclaimX = 54;
                exclaimY = 143;
                if (mouseX > i + exclaimX - 5 && mouseY > j + exclaimY - 5 && mouseX < i + exclaimX + 15 && mouseY < j + exclaimY + 15) {
                    Component tabletName = Component.translatable(ACItemRegistry.CAVE_TABLET.get().getDescriptionId()).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.YELLOW);
                    List<Component> step1Tooltip = List.of(Component.translatable("alexscaves.container.spelunkery_table.slot_info_tablet_0", tabletName).withStyle(ChatFormatting.GRAY), Component.translatable("alexscaves.container.spelunkery_table.slot_info_tablet_1").withStyle(ChatFormatting.GRAY));
                    guiGraphics.renderTooltip(this.f_96547_, step1Tooltip, Optional.empty(), mouseX, mouseY);
                }
            } else if (this.tutorialStep == 1) {
                exclaimX = 74;
                exclaimY = 143;
                if (mouseX > i + exclaimX - 5 && mouseY > j + exclaimY - 5 && mouseX < i + exclaimX + 15 && mouseY < j + exclaimY + 15) {
                    Component paperName = Component.translatable(Items.PAPER.getDescriptionId()).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.WHITE);
                    List<Component> step1Tooltip = List.of(Component.translatable("alexscaves.container.spelunkery_table.slot_info_paper", paperName).withStyle(ChatFormatting.GRAY));
                    guiGraphics.renderTooltip(this.f_96547_, step1Tooltip, Optional.empty(), mouseX, mouseY);
                }
            } else if (this.tutorialStep == 2) {
                exclaimX = 170;
                exclaimY = 23;
                if (mouseX > i + exclaimX - 5 && mouseY > j + exclaimY - 5 && mouseX < i + exclaimX + 15 && mouseY < j + exclaimY + 15) {
                    List<Component> step1Tooltip = List.of(Component.translatable("alexscaves.container.spelunkery_table.translate").withStyle(ChatFormatting.GRAY));
                    guiGraphics.renderTooltip(this.f_96547_, step1Tooltip, Optional.empty(), mouseX, mouseY);
                }
            } else if (this.tutorialStep == 3) {
                exclaimX = 185;
                exclaimY = 140;
                if (mouseX > i + exclaimX - 5 && mouseY > j + exclaimY - 5 && mouseX < i + exclaimX + 15 && mouseY < j + exclaimY + 15) {
                    List<Component> step1Tooltip = List.of(Component.translatable("alexscaves.container.spelunkery_table.glass").withStyle(ChatFormatting.GRAY));
                    guiGraphics.renderTooltip(this.f_96547_, step1Tooltip, Optional.empty(), mouseX, mouseY);
                }
            } else if (this.tutorialStep == 4) {
                exclaimX = -15;
                exclaimY = 15;
                if (mouseX > i + exclaimX - 5 && mouseY > j + exclaimY - 5 && mouseX < i + exclaimX + 15 && mouseY < j + exclaimY + 15) {
                    List<Component> step1Tooltip = List.of(Component.translatable("alexscaves.container.spelunkery_table.guess_name").withStyle(ChatFormatting.GRAY));
                    guiGraphics.renderTooltip(this.f_96547_, step1Tooltip, Optional.empty(), mouseX, mouseY);
                }
            } else if (this.tutorialStep == 5) {
                exclaimX = 35;
                exclaimY = 142;
                if (mouseX > i + exclaimX - 5 && mouseY > j + exclaimY - 5 && mouseX < i + exclaimX + 15 && mouseY < j + exclaimY + 15) {
                    Component scrollName = Component.translatable(ACItemRegistry.CAVE_CODEX.get().getDescriptionId()).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.YELLOW);
                    int toDoLevels = Math.max(0, 3 - this.level);
                    List<Component> step1Tooltip = List.of(Component.translatable(toDoLevels == 1 ? "alexscaves.container.spelunkery_table.level" : "alexscaves.container.spelunkery_table.levels", toDoLevels, scrollName).withStyle(ChatFormatting.GRAY));
                    guiGraphics.renderTooltip(this.f_96547_, step1Tooltip, Optional.empty(), mouseX, mouseY);
                }
            }
            guiGraphics.blit(WIDGETS_TEXTURE, i + exclaimX, j + exclaimY, this.tickCount % 20 < 10 ? 7 : 0, 70, 7, 16);
        }
        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public float getMagnifyPosX(float f) {
        return this.prevMagnifyPosX + (this.magnifyPosX - this.prevMagnifyPosX) * f;
    }

    public float getMagnifyPosY(float f) {
        return this.prevMagnifyPosY + (this.magnifyPosY - this.prevMagnifyPosY) * f;
    }

    private void renderMagnify(GuiGraphics guiGraphics, float partialTick) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        float actualPartialTick = Minecraft.getInstance().getFrameTime();
        float lerpX = this.getMagnifyPosX(actualPartialTick);
        float lerpY = this.getMagnifyPosY(actualPartialTick);
        float size = 0.1484375F;
        float u = 0.0F;
        float v = 0.0546875F;
        float x1 = lerpX + 38.0F;
        float y1 = lerpY + 38.0F;
        float u1 = u + size;
        float v1 = v + size;
        float zOffset = this.draggingMagnify ? 500.0F : 200.0F;
        bufferbuilder.m_252986_(matrix4f, lerpX, lerpY, zOffset).uv(u, v).endVertex();
        bufferbuilder.m_252986_(matrix4f, lerpX, y1, zOffset).uv(u, v1).endVertex();
        bufferbuilder.m_252986_(matrix4f, x1, y1, zOffset).uv(u1, v1).endVertex();
        bufferbuilder.m_252986_(matrix4f, x1, lerpY, zOffset).uv(u1, v).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int x, int y) {
        int i = this.f_97735_;
        int j = this.f_97736_;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.f_97726_, this.f_97727_);
        for (int bulb = 0; bulb < Math.min(this.level, 3); bulb++) {
            guiGraphics.blit(WIDGETS_TEXTURE, i + 92 + bulb * 15, j + 143, 0, 0, 13, 14);
        }
        if (this.hasPaper()) {
            guiGraphics.blit(WIDGETS_TEXTURE, i - 80, j + 10, 176, 0, 80, 149);
        }
        int tablet = this.hasTablet() ? (this.attemptsLeft <= 1 ? 2 : 1) : 0;
        if (tablet > 0) {
            guiGraphics.blit(TABLET_TEXTURE, i + 20, j + 19, 0, (tablet - 1) * 121, 168, 120);
        }
    }

    public boolean hasTablet() {
        return ((SpelunkeryTableMenu) this.f_97732_).m_38853_(0).hasItem() && ((SpelunkeryTableMenu) this.f_97732_).m_38853_(0).getItem().is(ACItemRegistry.CAVE_TABLET.get());
    }

    public boolean hasPaper() {
        return ((SpelunkeryTableMenu) this.f_97732_).m_38853_(1).hasItem() && ((SpelunkeryTableMenu) this.f_97732_).m_38853_(1).getItem().is(Items.PAPER);
    }

    public boolean isFirstTimeUsing() {
        return !AlexsCaves.PROXY.isSpelunkeryTutorialComplete();
    }

    @Override
    protected void containerTick() {
        this.tickCount++;
        if (this.lastTablet == null && this.hasTablet()) {
            this.lastTablet = ((SpelunkeryTableMenu) this.f_97732_).m_38853_(0).getItem();
        } else if (this.lastTablet != null && this.hasTablet() && this.lastTablet != ((SpelunkeryTableMenu) this.f_97732_).m_38853_(0).getItem()) {
            this.lastTablet = ((SpelunkeryTableMenu) this.f_97732_).m_38853_(0).getItem();
            this.invalidTablet = false;
            this.fullResetWords();
        }
        this.prevMagnifyPosX = this.magnifyPosX;
        this.prevMagnifyPosY = this.magnifyPosY;
        this.prevPassLevelProgress = this.passLevelProgress;
        int targetMagnifyX;
        int targetMagnifyY;
        int maxDistance;
        if (this.draggingMagnify) {
            targetMagnifyX = this.lastMouseX - 19;
            targetMagnifyY = this.lastMouseY - 19;
            maxDistance = 15;
        } else {
            targetMagnifyX = this.f_97735_ + 170;
            targetMagnifyY = this.f_97736_ + 130;
            maxDistance = 20;
        }
        Vec3 vec3 = new Vec3((double) ((float) targetMagnifyX - this.magnifyPosX), (double) ((float) targetMagnifyY - this.magnifyPosY), 0.0);
        if (vec3.length() > (double) maxDistance) {
            vec3 = vec3.normalize().scale((double) maxDistance);
        }
        this.magnifyPosX = (float) ((double) this.magnifyPosX + vec3.x);
        this.magnifyPosY = (float) ((double) this.magnifyPosY + vec3.y);
        if (this.finishedLevel && this.passLevelProgress < 10.0F) {
            this.passLevelProgress += 0.5F;
        }
        if (!this.finishedLevel && this.passLevelProgress > 0.0F) {
            this.passLevelProgress -= 0.5F;
        }
        boolean resetTabletFromWin = this.finishedLevel && this.passLevelProgress >= 10.0F && this.attemptsLeft > 0;
        if (!((SpelunkeryTableMenu) this.f_97732_).m_38853_(0).hasItem()) {
            this.prevWordsFile = null;
            this.invalidTablet = false;
        } else if (this.prevWordsFile == null || resetTabletFromWin) {
            this.prevWordsFile = this.getWordsForItem(((SpelunkeryTableMenu) this.f_97732_).m_38853_(0).getItem());
            if (this.prevWordsFile == null) {
                this.clearWordWidgets();
            } else {
                this.finishedLevel = false;
                this.generateWords(this.prevWordsFile);
            }
        }
        int currentColor = ((SpelunkeryTableMenu) this.f_97732_).getHighlightColor(Minecraft.getInstance().level);
        if (currentColor != -1) {
            this.highlightColor = currentColor;
        }
        if (resetTabletFromWin && this.level >= 3) {
            this.doneWithTutorial = true;
            SpelunkeryTableMenu.setTutorialComplete(Minecraft.getInstance().player, true);
            AlexsCaves.NETWORK_WRAPPER.sendToServer(new SpelunkeryTableChangeMessage(true));
            this.level = 0;
            this.fullResetWords();
        } else if (this.finishedLevel && this.passLevelProgress >= 10.0F && this.attemptsLeft <= 0) {
            this.level = 0;
            AlexsCaves.NETWORK_WRAPPER.sendToServer(new SpelunkeryTableChangeMessage(false));
            this.fullResetWords();
            Minecraft.getInstance().setScreen(null);
        }
        if (!this.hasTablet() && !this.wordButtons.isEmpty()) {
            this.clearWordWidgets();
        }
        if (this.doneWithTutorial) {
            this.tutorialStep = 6;
        } else if (!this.hasTablet()) {
            this.tutorialStep = 0;
        } else if (!this.hasPaper()) {
            this.tutorialStep = 1;
        } else if (this.attemptsLeft == 5 && this.level == 0) {
            this.tutorialStep = 2;
        } else if (!this.hasClickedLens) {
            this.tutorialStep = 3;
        } else if (this.level == 0) {
            this.tutorialStep = 4;
        } else {
            this.tutorialStep = 5;
        }
    }

    public void fullResetWords() {
        this.clearWordWidgets();
        this.prevWordsFile = this.getWordsForItem(((SpelunkeryTableMenu) this.f_97732_).m_38853_(0).getItem());
        if (this.prevWordsFile != null) {
            this.generateWords(this.prevWordsFile);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean prev = super.mouseClicked(mouseX, mouseY, button);
        if (!prev) {
        }
        return prev;
    }

    @Override
    public boolean mouseDragged(double width, double height, int button, double x, double y) {
        boolean prev = super.mouseDragged(width, height, button, x, y);
        if (prev) {
            this.lastMouseX = (int) width;
            this.lastMouseY = (int) height;
            if (!this.draggingMagnify && (float) this.lastMouseX >= this.magnifyPosX && (float) this.lastMouseX <= this.magnifyPosX + 38.0F && (float) this.lastMouseY >= this.magnifyPosY && (float) this.lastMouseY <= this.magnifyPosY + 38.0F) {
                this.draggingMagnify = true;
                if (this.tutorialStep > 2) {
                    this.hasClickedLens = true;
                }
            }
        }
        return prev;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.draggingMagnify = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.drawString(this.f_96547_, this.f_96539_, this.f_97728_ - this.f_96547_.width(this.f_96539_) / 2, this.f_97729_, 4210752, false);
    }

    private ResourceLocation getWordsForItem(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() == ACItemRegistry.CAVE_TABLET.get()) {
            String s1 = this.getMinigameStr(stack) + ".txt";
            String lang = Minecraft.getInstance().getLanguageManager().getSelected().toLowerCase();
            ResourceLocation resourceLocation = new ResourceLocation("alexscaves", "minigame/" + lang + "/" + s1);
            try {
                InputStream is = Minecraft.getInstance().getResourceManager().m_215595_(resourceLocation);
                is.close();
            } catch (Exception var6) {
                AlexsCaves.LOGGER.warn("Could not find language file for translation, defaulting to english");
                resourceLocation = new ResourceLocation("alexscaves", "minigame/en_us/" + s1);
            }
            return resourceLocation;
        } else {
            return null;
        }
    }

    private String getMinigameStr(ItemStack stack) {
        ResourceKey<Biome> biomeResourceKey = CaveInfoItem.getCaveBiome(stack);
        return biomeResourceKey == null ? "magnetic_caves" : biomeResourceKey.location().getPath();
    }

    private void clearWordWidgets() {
        for (SpelunkeryTableWordButton button : this.wordButtons) {
            this.m_169411_(button);
        }
        this.wordButtons.clear();
    }

    private void addWordWidget(SpelunkeryTableWordButton button) {
        this.wordButtons.add(button);
        this.m_142416_(button);
    }

    private void generateWords(ResourceLocation file) {
        this.clearWordWidgets();
        List<String> allWords;
        try {
            BufferedReader bufferedreader = Minecraft.getInstance().getResourceManager().m_215597_(file);
            allWords = IOUtils.readLines(bufferedreader);
        } catch (IOException var11) {
            allWords = new ArrayList();
            this.invalidTablet = true;
            AlexsCaves.LOGGER.error("Could not load in spelunkery minigame file {}", file);
        }
        int maxWidth = 160;
        int maxLines = 8;
        int wordLines = 0;
        int wordLineWidth = 0;
        int letterWidth = 6;
        Collections.shuffle(allWords);
        while (wordLines < maxLines && !allWords.isEmpty()) {
            MutableComponent component = Component.literal((String) allWords.remove(0));
            for (int maxWordWidth = component.getString().length() * letterWidth; wordLineWidth + maxWordWidth + 30 < maxWidth && !allWords.isEmpty(); wordLineWidth += maxWordWidth) {
                component = Component.literal(((String) allWords.remove(0)).toUpperCase());
                maxWordWidth = component.getString().length() * letterWidth;
                SpelunkeryTableWordButton tableWordButton = new SpelunkeryTableWordButton(this, this.f_96547_, 25 + wordLineWidth, 25 + 12 * wordLines, maxWordWidth, 12, component.withStyle(Style.EMPTY));
                this.addWordWidget(tableWordButton);
            }
            wordLineWidth = 0;
            wordLines++;
        }
        if (!this.wordButtons.isEmpty()) {
            if (Minecraft.getInstance().level != null) {
                this.targetWordButton = this.wordButtons.size() <= 1 ? (SpelunkeryTableWordButton) this.wordButtons.get(0) : (SpelunkeryTableWordButton) this.wordButtons.get(this.random.nextInt(this.wordButtons.size()));
            }
            this.attemptsLeft = 5;
        } else {
            this.targetWordButton = null;
        }
    }

    public int getHighlightColor() {
        return this.highlightColor;
    }

    public void onClickWord(SpelunkeryTableWordButton tableWordButton) {
        if (!this.finishedLevel) {
            if (tableWordButton == this.targetWordButton) {
                this.level++;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(this.level >= 3 ? ACSoundRegistry.SPELUNKERY_TABLE_SUCCESS_COMPLETE.get() : ACSoundRegistry.SPELUNKERY_TABLE_SUCCESS.get(), 1.0F));
                this.finishedLevel = true;
            } else {
                if (this.attemptsLeft > 0) {
                    this.attemptsLeft--;
                }
                if (this.attemptsLeft <= 1) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ACSoundRegistry.SPELUNKERY_TABLE_CRACK.get(), 1.0F));
                } else {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ACSoundRegistry.SPELUNKERY_TABLE_ATTEMPT_FAIL.get(), 1.0F));
                }
                if (this.attemptsLeft <= 0) {
                    this.finishedLevel = true;
                }
            }
        }
    }

    public float getRevealWordsAmount(float partialTick) {
        return this.finishedLevel ? Math.min((this.prevPassLevelProgress + (this.passLevelProgress - this.prevPassLevelProgress) * partialTick) * 0.33F, 1.0F) : 0.0F;
    }

    public boolean isTargetWord(SpelunkeryTableWordButton tableWordButton) {
        return this.targetWordButton == tableWordButton;
    }

    private boolean hasClickedAnyWord() {
        boolean flag = false;
        for (SpelunkeryTableWordButton button : this.wordButtons) {
            if (!button.f_93623_) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public void onClose() {
        if (this.hasPaper() && this.hasTablet() && this.hasClickedAnyWord() && this.level < 3) {
            AlexsCaves.NETWORK_WRAPPER.sendToServer(new SpelunkeryTableChangeMessage(false));
        }
        super.onClose();
    }
}