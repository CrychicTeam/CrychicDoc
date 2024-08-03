package com.simibubi.create.foundation.ponder.ui;

import com.mojang.blaze3d.platform.ClipboardManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.BoxElement;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.ponder.PonderChapter;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.element.TextWindowElement;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.FontHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.Pointing;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.ponder.DebugScenes;
import com.simibubi.create.infrastructure.ponder.PonderIndex;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;

public class PonderUI extends NavigatableSimiScreen {

    public static int ponderTicks;

    public static float ponderPartialTicksPaused;

    public static final String PONDERING = "ponder.pondering";

    public static final String IDENTIFY_MODE = "ponder.identify_mode";

    public static final String IN_CHAPTER = "ponder.in_chapter";

    public static final String IDENTIFY = "ponder.identify";

    public static final String PREVIOUS = "ponder.previous";

    public static final String CLOSE = "ponder.close";

    public static final String NEXT = "ponder.next";

    public static final String NEXT_UP = "ponder.next_up";

    public static final String REPLAY = "ponder.replay";

    public static final String SLOW_TEXT = "ponder.slow_text";

    private List<PonderScene> scenes;

    private List<PonderTag> tags;

    private List<PonderButton> tagButtons;

    private List<LerpedFloat> tagFades;

    private LerpedFloat fadeIn;

    ItemStack stack;

    PonderChapter chapter = null;

    private boolean userViewMode;

    private boolean identifyMode;

    private ItemStack hoveredTooltipItem;

    private BlockPos hoveredBlockPos;

    private ClipboardManager clipboardHelper;

    private BlockPos copiedBlockPos;

    private LerpedFloat finishingFlash;

    private LerpedFloat nextUp;

    private int finishingFlashWarmup = 0;

    private int nextUpWarmup = 0;

    private LerpedFloat lazyIndex;

    private int index = 0;

    private PonderTag referredToByTag;

    private PonderButton left;

    private PonderButton right;

    private PonderButton scan;

    private PonderButton chap;

    private PonderButton userMode;

    private PonderButton close;

    private PonderButton replay;

    private PonderButton slowMode;

    private int skipCooling = 0;

    private int extendedTickLength = 0;

    private int extendedTickTimer = 0;

    public static PonderUI of(ResourceLocation id) {
        return new PonderUI(PonderRegistry.compile(id));
    }

    public static PonderUI of(ItemStack item) {
        return new PonderUI(PonderRegistry.compile(RegisteredObjects.getKeyOrThrow(item.getItem())));
    }

    public static PonderUI of(ItemStack item, PonderTag tag) {
        PonderUI ponderUI = new PonderUI(PonderRegistry.compile(RegisteredObjects.getKeyOrThrow(item.getItem())));
        ponderUI.referredToByTag = tag;
        return ponderUI;
    }

    public static PonderUI of(PonderChapter chapter) {
        PonderUI ui = new PonderUI(PonderRegistry.compile(chapter));
        ui.chapter = chapter;
        return ui;
    }

    protected PonderUI(List<PonderScene> scenes) {
        ResourceLocation component = ((PonderScene) scenes.get(0)).getComponent();
        if (ForgeRegistries.ITEMS.containsKey(component)) {
            this.stack = new ItemStack(ForgeRegistries.ITEMS.getValue(component));
        } else {
            this.stack = new ItemStack(ForgeRegistries.BLOCKS.getValue(component));
        }
        this.tags = new ArrayList(PonderRegistry.TAGS.getTags(component));
        this.scenes = scenes;
        if (scenes.isEmpty()) {
            List<PonderStoryBoardEntry> l = Collections.singletonList(new PonderStoryBoardEntry(DebugScenes::empty, "create", "debug/scene_1", new ResourceLocation("minecraft", "stick")));
            scenes.addAll(PonderRegistry.compile(l));
        }
        this.lazyIndex = LerpedFloat.linear().startWithValue((double) this.index);
        this.fadeIn = LerpedFloat.linear().startWithValue(0.0).chase(1.0, 0.1F, LerpedFloat.Chaser.EXP);
        this.clipboardHelper = new ClipboardManager();
        this.finishingFlash = LerpedFloat.linear().startWithValue(0.0).chase(0.0, 0.1F, LerpedFloat.Chaser.EXP);
        this.nextUp = LerpedFloat.linear().startWithValue(0.0).chase(0.0, 0.4F, LerpedFloat.Chaser.EXP);
    }

    @Override
    protected void init() {
        super.init();
        this.tagButtons = new ArrayList();
        this.tagFades = new ArrayList();
        this.tags.forEach(t -> {
            int i = this.tagButtons.size();
            int x = 31;
            int y = 81 + i * 30;
            PonderButton b2 = new PonderButton(x, y).<ElementWidget>showing(t).withCallback((mX, mY) -> {
                this.centerScalingOn(mX, mY);
                ScreenOpener.transitionTo(new PonderTagScreen(t));
            });
            this.m_142416_(b2);
            this.tagButtons.add(b2);
            LerpedFloat chase = LerpedFloat.linear().startWithValue(0.0).chase(0.0, 0.05F, LerpedFloat.Chaser.exp(0.1));
            this.tagFades.add(chase);
        });
        Options bindings = this.f_96541_.options;
        int spacing = 8;
        int bX = (this.f_96543_ - 20) / 2 - (70 + 2 * spacing);
        int bY = this.f_96544_ - 20 - 31;
        int pX = this.f_96543_ / 2 - 110;
        int pY = bY + 20 + 4;
        int pW = this.f_96543_ - 2 * pX;
        this.m_142416_(new PonderProgressBar(this, pX, pY, pW, 1));
        this.m_142416_(this.scan = new PonderButton(bX, bY).<PonderButton>withShortcut(bindings.keyDrop).<ElementWidget>showing(AllIcons.I_MTD_SCAN).<ElementWidget>enableFade(0, 5).withCallback(() -> {
            this.identifyMode = !this.identifyMode;
            if (!this.identifyMode) {
                ((PonderScene) this.scenes.get(this.index)).deselect();
            } else {
                ponderPartialTicksPaused = this.f_96541_.getFrameTime();
            }
        }));
        this.scan.atZLevel(600.0F);
        this.m_142416_(this.slowMode = new PonderButton(this.f_96543_ - 20 - 31, bY).<ElementWidget>showing(AllIcons.I_MTD_SLOW_MODE).<ElementWidget>enableFade(0, 5).withCallback(() -> this.setComfyReadingEnabled(!this.isComfyReadingEnabled())));
        if (PonderIndex.editingModeActive()) {
            this.m_142416_(this.userMode = new PonderButton(this.f_96543_ - 50 - 31, bY).<ElementWidget>showing(AllIcons.I_MTD_USER_MODE).<ElementWidget>enableFade(0, 5).withCallback(() -> this.userViewMode = !this.userViewMode));
        }
        bX += 50 + spacing;
        this.m_142416_(this.left = new PonderButton(bX, bY).<PonderButton>withShortcut(bindings.keyLeft).<ElementWidget>showing(AllIcons.I_MTD_LEFT).<ElementWidget>enableFade(0, 5).withCallback(() -> this.scroll(false)));
        bX += 20 + spacing;
        this.m_142416_(this.close = new PonderButton(bX, bY).<PonderButton>withShortcut(bindings.keyInventory).<ElementWidget>showing(AllIcons.I_MTD_CLOSE).<ElementWidget>enableFade(0, 5).withCallback(this::m_7379_));
        bX += 20 + spacing;
        this.m_142416_(this.right = new PonderButton(bX, bY).<PonderButton>withShortcut(bindings.keyRight).<ElementWidget>showing(AllIcons.I_MTD_RIGHT).<ElementWidget>enableFade(0, 5).withCallback(() -> this.scroll(true)));
        bX += 50 + spacing;
        this.m_142416_(this.replay = new PonderButton(bX, bY).<PonderButton>withShortcut(bindings.keyDown).<ElementWidget>showing(AllIcons.I_MTD_REPLAY).<ElementWidget>enableFade(0, 5).withCallback(this::replay));
    }

    @Override
    protected void initBackTrackIcon(PonderButton backTrack) {
        backTrack.showing(this.stack);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.skipCooling > 0) {
            this.skipCooling--;
        }
        if (this.referredToByTag != null) {
            for (int i = 0; i < this.scenes.size(); i++) {
                PonderScene ponderScene = (PonderScene) this.scenes.get(i);
                if (ponderScene.getTags().contains(this.referredToByTag)) {
                    if (i != this.index) {
                        ((PonderScene) this.scenes.get(this.index)).fadeOut();
                        this.index = i;
                        ((PonderScene) this.scenes.get(this.index)).begin();
                        this.lazyIndex.chase((double) this.index, 0.25, LerpedFloat.Chaser.EXP);
                        this.identifyMode = false;
                    }
                    break;
                }
            }
            this.referredToByTag = null;
        }
        this.lazyIndex.tickChaser();
        this.fadeIn.tickChaser();
        this.finishingFlash.tickChaser();
        this.nextUp.tickChaser();
        PonderScene activeScene = (PonderScene) this.scenes.get(this.index);
        this.extendedTickLength = 0;
        if (this.isComfyReadingEnabled()) {
            activeScene.forEachVisible(TextWindowElement.class, twe -> this.extendedTickLength = 2);
        }
        if (this.extendedTickTimer == 0) {
            if (!this.identifyMode) {
                ponderTicks++;
                if (this.skipCooling == 0) {
                    activeScene.tick();
                }
            }
            if (!this.identifyMode) {
                float lazyIndexValue = this.lazyIndex.getValue();
                if (Math.abs(lazyIndexValue - (float) this.index) > 0.001953125F) {
                    ((PonderScene) this.scenes.get(lazyIndexValue < (float) this.index ? this.index - 1 : this.index + 1)).tick();
                }
            }
            this.extendedTickTimer = this.extendedTickLength;
        } else {
            this.extendedTickTimer--;
        }
        if (activeScene.getCurrentTime() == activeScene.getTotalTime() - 1) {
            this.finishingFlashWarmup = 30;
            this.nextUpWarmup = 50;
        }
        if (this.finishingFlashWarmup > 0) {
            this.finishingFlashWarmup--;
            if (this.finishingFlashWarmup == 0) {
                this.finishingFlash.setValue(1.0);
                this.finishingFlash.setValue(1.0);
            }
        }
        if (this.nextUpWarmup > 0) {
            this.nextUpWarmup--;
            if (this.nextUpWarmup == 0) {
                this.nextUp.updateChaseTarget(1.0F);
            }
        }
        this.updateIdentifiedItem(activeScene);
    }

    public PonderScene getActiveScene() {
        return (PonderScene) this.scenes.get(this.index);
    }

    public void seekToTime(int time) {
        if (this.getActiveScene().getCurrentTime() > time) {
            this.replay();
        }
        this.getActiveScene().seekToTime(time);
        if (time != 0) {
            this.coolDownAfterSkip();
        }
    }

    public void updateIdentifiedItem(PonderScene activeScene) {
        this.hoveredTooltipItem = ItemStack.EMPTY;
        this.hoveredBlockPos = null;
        if (this.identifyMode) {
            Window w = this.f_96541_.getWindow();
            double mouseX = this.f_96541_.mouseHandler.xpos() * (double) w.getGuiScaledWidth() / (double) w.getScreenWidth();
            double mouseY = this.f_96541_.mouseHandler.ypos() * (double) w.getGuiScaledHeight() / (double) w.getScreenHeight();
            PonderScene.SceneTransform t = activeScene.getTransform();
            Vec3 vec1 = t.screenToScene(mouseX, mouseY, 1000, 0.0F);
            Vec3 vec2 = t.screenToScene(mouseX, mouseY, -100, 0.0F);
            Pair<ItemStack, BlockPos> pair = activeScene.rayTraceScene(vec1, vec2);
            this.hoveredTooltipItem = pair.getFirst();
            this.hoveredBlockPos = pair.getSecond();
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.scroll(delta > 0.0) ? true : super.m_6050_(mouseX, mouseY, delta);
    }

    protected void replay() {
        this.identifyMode = false;
        PonderScene scene = (PonderScene) this.scenes.get(this.index);
        if (m_96638_()) {
            List<PonderStoryBoardEntry> list = (List<PonderStoryBoardEntry>) PonderRegistry.ALL.get(scene.getComponent());
            PonderStoryBoardEntry sb = (PonderStoryBoardEntry) list.get(this.index);
            StructureTemplate activeTemplate = PonderRegistry.loadSchematic(sb.getSchematicLocation());
            PonderWorld world = new PonderWorld(BlockPos.ZERO, Minecraft.getInstance().level);
            activeTemplate.placeInWorld(world, BlockPos.ZERO, BlockPos.ZERO, new StructurePlaceSettings(), RandomSource.create(), 2);
            world.createBackup();
            scene = PonderRegistry.compileScene(this.index, sb, world);
            scene.begin();
            this.scenes.set(this.index, scene);
        }
        scene.begin();
    }

    protected boolean scroll(boolean forward) {
        int prevIndex = this.index;
        this.index = forward ? this.index + 1 : this.index - 1;
        this.index = Mth.clamp(this.index, 0, this.scenes.size() - 1);
        if (prevIndex != this.index) {
            ((PonderScene) this.scenes.get(prevIndex)).fadeOut();
            ((PonderScene) this.scenes.get(this.index)).begin();
            this.lazyIndex.chase((double) this.index, 0.25, LerpedFloat.Chaser.EXP);
            this.identifyMode = false;
            return true;
        } else {
            this.index = prevIndex;
            return false;
        }
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        partialTicks = getPartialTicks();
        RenderSystem.enableBlend();
        this.renderVisibleScenes(graphics, mouseX, mouseY, this.skipCooling > 0 ? 0.0F : (this.identifyMode ? ponderPartialTicksPaused : partialTicks));
        this.renderWidgets(graphics, mouseX, mouseY, this.identifyMode ? ponderPartialTicksPaused : partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        super.m_280273_(graphics);
    }

    protected void renderVisibleScenes(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderScene(graphics, mouseX, mouseY, this.index, partialTicks);
        float lazyIndexValue = this.lazyIndex.getValue(partialTicks);
        if (Math.abs(lazyIndexValue - (float) this.index) > 0.001953125F) {
            this.renderScene(graphics, mouseX, mouseY, lazyIndexValue < (float) this.index ? this.index - 1 : this.index + 1, partialTicks);
        }
    }

    protected void renderScene(GuiGraphics graphics, int mouseX, int mouseY, int i, float partialTicks) {
        SuperRenderTypeBuffer buffer = SuperRenderTypeBuffer.getInstance();
        PonderScene scene = (PonderScene) this.scenes.get(i);
        double value = (double) this.lazyIndex.getValue(this.f_96541_.getFrameTime());
        double diff = (double) i - value;
        double slide = Mth.lerp(diff * diff, 200.0, 600.0) * diff;
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.backupProjectionMatrix();
        Matrix4f matrix4f = new Matrix4f(RenderSystem.getProjectionMatrix());
        matrix4f.translate(0.0F, 0.0F, 800.0F);
        RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.DISTANCE_TO_ORIGIN);
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(0.0F, 0.0F, -800.0F);
        scene.getTransform().updateScreenParams(this.f_96543_, this.f_96544_, slide);
        scene.getTransform().apply(ms, partialTicks);
        scene.getTransform().updateSceneRVE(partialTicks);
        scene.renderScene(buffer, ms, partialTicks);
        buffer.draw();
        BoundingBox bounds = scene.getBounds();
        ms.pushPose();
        if (!scene.shouldHidePlatformShadow()) {
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            ms.pushPose();
            ms.translate((float) scene.getBasePlateOffsetX(), 0.0F, (float) scene.getBasePlateOffsetZ());
            UIRenderHelper.flipForGuiRender(ms);
            float flash = this.finishingFlash.getValue(partialTicks) * 0.9F;
            float alpha = flash;
            flash *= flash;
            flash = flash * 2.0F - 1.0F;
            flash *= flash;
            flash = 1.0F - flash;
            for (int f = 0; f < 4; f++) {
                ms.translate((float) scene.getBasePlateSize(), 0.0F, 0.0F);
                ms.pushPose();
                ms.translate(0.0F, 0.0F, -9.765625E-4F);
                if (flash > 0.0F) {
                    ms.pushPose();
                    ms.scale(1.0F, 0.5F + flash * 0.75F, 1.0F);
                    graphics.fillGradient(0, -1, -scene.getBasePlateSize(), 0, 0, 13041609, new Color(-1429798967).scaleAlpha(alpha).getRGB());
                    ms.popPose();
                }
                ms.translate(0.0F, 0.0F, 0.001953125F);
                graphics.fillGradient(0, 0, -scene.getBasePlateSize(), 4, 0, 1711276032, 0);
                ms.popPose();
                ms.mulPose(Axis.YP.rotationDegrees(-90.0F));
            }
            ms.popPose();
            RenderSystem.disableCull();
            RenderSystem.disableDepthTest();
        }
        if (PonderIndex.editingModeActive() && !this.userViewMode) {
            ms.scale(-1.0F, -1.0F, 1.0F);
            ms.scale(0.0625F, 0.0625F, 0.0625F);
            ms.translate(1.0F, -8.0F, -0.015625F);
            ms.pushPose();
            ms.translate(4.0F, -3.0F, 0.0F);
            ms.translate(0.0F, 0.0F, -0.001953125F);
            for (int x = 0; x <= bounds.getXSpan(); x++) {
                ms.translate(-16.0F, 0.0F, 0.0F);
                graphics.drawString(this.f_96547_, x == bounds.getXSpan() ? "x" : x + "", 0, 0, -1, false);
            }
            ms.popPose();
            ms.pushPose();
            ms.scale(-1.0F, 1.0F, 1.0F);
            ms.translate(0.0F, -3.0F, -4.0F);
            ms.mulPose(Axis.YP.rotationDegrees(-90.0F));
            ms.translate(-8.0F, -2.0F, 0.03125F);
            for (int z = 0; z <= bounds.getZSpan(); z++) {
                ms.translate(16.0F, 0.0F, 0.0F);
                graphics.drawString(this.f_96547_, z == bounds.getZSpan() ? "z" : z + "", 0, 0, -1, false);
            }
            ms.popPose();
            ms.pushPose();
            ms.translate((float) (bounds.getXSpan() * -8), 0.0F, (float) (bounds.getZSpan() * 8));
            ms.mulPose(Axis.YP.rotationDegrees(-90.0F));
            for (Direction d : Iterate.horizontalDirections) {
                ms.mulPose(Axis.YP.rotationDegrees(90.0F));
                ms.pushPose();
                ms.translate(0.0F, 0.0F, (float) (bounds.getZSpan() * 16));
                ms.mulPose(Axis.XP.rotationDegrees(-90.0F));
                graphics.drawString(this.f_96547_, d.name().substring(0, 1), 0, 0, 1728053247, false);
                graphics.drawString(this.f_96547_, "|", 2, 10, 1157627903, false);
                graphics.drawString(this.f_96547_, ".", 2, 14, 587202559, false);
                ms.popPose();
            }
            ms.popPose();
            buffer.draw();
        }
        ms.popPose();
        ms.popPose();
        RenderSystem.restoreProjectionMatrix();
    }

    protected void renderWidgets(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.disableDepthTest();
        float fade = this.fadeIn.getValue(partialTicks);
        float lazyIndexValue = this.lazyIndex.getValue(partialTicks);
        float indexDiff = Math.abs(lazyIndexValue - (float) this.index);
        PonderScene activeScene = (PonderScene) this.scenes.get(this.index);
        PonderScene nextScene = this.scenes.size() > this.index + 1 ? (PonderScene) this.scenes.get(this.index + 1) : null;
        boolean noWidgetsHovered = true;
        for (GuiEventListener child : this.m_6702_()) {
            noWidgetsHovered &= !child.isMouseOver((double) mouseX, (double) mouseY);
        }
        int tooltipColor = Theme.i(Theme.Key.TEXT_DARKER);
        this.renderChapterTitle(graphics, fade, indexDiff, activeScene, tooltipColor);
        this.renderNavigationMenu(graphics);
        PoseStack ms = graphics.pose();
        if (this.identifyMode) {
            if (noWidgetsHovered && mouseY < this.f_96544_ - 80) {
                ms.pushPose();
                ms.translate((float) mouseX, (float) mouseY, 100.0F);
                if (this.hoveredTooltipItem.isEmpty()) {
                    MutableComponent text = Lang.translateDirect("ponder.identify_mode", ((MutableComponent) this.f_96541_.options.keyDrop.getTranslatedKeyMessage()).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY);
                    graphics.renderTooltip(this.f_96547_, this.f_96547_.split(text, this.f_96543_ / 3), 0, 0);
                } else {
                    graphics.renderTooltip(this.f_96547_, this.hoveredTooltipItem, 0, 0);
                }
                if (this.hoveredBlockPos != null && PonderIndex.editingModeActive() && !this.userViewMode) {
                    ms.translate(0.0F, -15.0F, 0.0F);
                    boolean copied = this.copiedBlockPos != null && this.hoveredBlockPos.equals(this.copiedBlockPos);
                    MutableComponent coords = Components.literal(this.hoveredBlockPos.m_123341_() + ", " + this.hoveredBlockPos.m_123342_() + ", " + this.hoveredBlockPos.m_123343_()).withStyle(copied ? ChatFormatting.GREEN : ChatFormatting.GOLD);
                    graphics.renderTooltip(this.f_96547_, coords, 0, 0);
                }
                ms.popPose();
            }
            this.scan.flash();
        } else {
            this.scan.dim();
        }
        if (PonderIndex.editingModeActive()) {
            if (this.userViewMode) {
                this.userMode.flash();
            } else {
                this.userMode.dim();
            }
        }
        if (this.isComfyReadingEnabled()) {
            this.slowMode.flash();
        } else {
            this.slowMode.dim();
        }
        this.renderSceneOverlay(graphics, partialTicks, lazyIndexValue, indexDiff);
        boolean finished = activeScene.isFinished();
        if (finished) {
            this.jumpToNextScene(graphics, partialTicks, nextScene);
        }
        this.f_169369_.forEach(w -> {
            if (w instanceof PonderButton button) {
                button.fade().startWithValue((double) fade);
            }
        });
        if (this.index == 0 || this.index == 1 && lazyIndexValue < (float) this.index) {
            this.left.fade().startWithValue((double) lazyIndexValue);
        }
        if (this.index == this.scenes.size() - 1 || this.index == this.scenes.size() - 2 && lazyIndexValue > (float) this.index) {
            this.right.fade().startWithValue((double) ((float) this.scenes.size() - lazyIndexValue - 1.0F));
        }
        if (finished) {
            this.right.flash();
        } else {
            this.right.dim();
            this.nextUp.updateChaseTarget(0.0F);
        }
        this.renderPonderTags(graphics, mouseX, mouseY, partialTicks, fade, activeScene);
        this.renderHoverTooltips(graphics, tooltipColor);
        RenderSystem.enableDepthTest();
    }

    protected void renderPonderTags(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, float fade, PonderScene activeScene) {
        PoseStack ms = graphics.pose();
        List<PonderTag> sceneTags = activeScene.getTags();
        boolean highlightAll = sceneTags.contains(PonderTag.HIGHLIGHT_ALL);
        double s = Minecraft.getInstance().getWindow().getGuiScale();
        IntStream.range(0, this.tagButtons.size()).forEach(i -> {
            ms.pushPose();
            LerpedFloat chase = (LerpedFloat) this.tagFades.get(i);
            PonderButton button = (PonderButton) this.tagButtons.get(i);
            if (button.m_5953_((double) mouseX, (double) mouseY)) {
                chase.updateChaseTarget(1.0F);
            } else {
                chase.updateChaseTarget(0.0F);
            }
            chase.tickChaser();
            if (highlightAll) {
                button.flash();
            } else {
                button.dim();
            }
            int x = button.m_252754_() + button.m_5711_() + 4;
            int y = button.m_252907_() - 2;
            ms.translate((float) x, (float) y + 5.0F * (1.0F - fade), 800.0F);
            float fadedWidth = 200.0F * chase.getValue(partialTicks);
            UIRenderHelper.streak(graphics, 0.0F, 0, 12, 26, (int) fadedWidth);
            RenderSystem.enableScissor((int) ((double) x * s), 0, (int) ((double) fadedWidth * s), (int) ((double) this.f_96544_ * s));
            String tagName = ((PonderTag) this.tags.get(i)).getTitle();
            graphics.drawString(this.f_96547_, tagName, 3, 8, Theme.i(Theme.Key.TEXT_ACCENT_SLIGHT), false);
            RenderSystem.disableScissor();
            ms.popPose();
        });
    }

    protected void renderSceneOverlay(GuiGraphics graphics, float partialTicks, float lazyIndexValue, float indexDiff) {
        PoseStack ms = graphics.pose();
        float scenePT = this.skipCooling > 0 ? 0.0F : partialTicks;
        ms.pushPose();
        ms.translate(0.0F, 0.0F, 100.0F);
        this.renderOverlay(graphics, this.index, scenePT);
        if (indexDiff > 0.001953125F) {
            this.renderOverlay(graphics, lazyIndexValue < (float) this.index ? this.index - 1 : this.index + 1, scenePT);
        }
        ms.popPose();
    }

    protected void jumpToNextScene(GuiGraphics graphics, float partialTicks, PonderScene nextScene) {
        PoseStack ms = graphics.pose();
        if (nextScene != null && this.nextUp.getValue() > 0.0625F && !nextScene.getId().equals(Create.asResource("creative_motor_mojang"))) {
            ms.pushPose();
            ms.translate((float) (this.right.m_252754_() + 10), (float) (this.right.m_252907_() - 6) + this.nextUp.getValue(partialTicks) * 5.0F, 400.0F);
            int boxWidth = Math.max(this.f_96547_.width(nextScene.getTitle()), this.f_96547_.width(Lang.translateDirect("ponder.next_up"))) + 5;
            renderSpeechBox(graphics, 0, 0, boxWidth, 20, this.right.m_198029_(), Pointing.DOWN, false);
            ms.translate(0.0F, -29.0F, 100.0F);
            graphics.drawCenteredString(this.f_96547_, Lang.translateDirect("ponder.next_up"), 0, 0, Theme.i(Theme.Key.TEXT_DARKER));
            graphics.drawCenteredString(this.f_96547_, nextScene.getTitle(), 0, 10, Theme.i(Theme.Key.TEXT));
            ms.popPose();
        }
    }

    protected void renderHoverTooltips(GuiGraphics graphics, int tooltipColor) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(0.0F, 0.0F, 500.0F);
        int tooltipY = this.f_96544_ - 16;
        if (this.scan.m_198029_()) {
            graphics.drawCenteredString(this.f_96547_, Lang.translateDirect("ponder.identify"), this.scan.m_252754_() + 10, tooltipY, tooltipColor);
        }
        if (this.index != 0 && this.left.m_198029_()) {
            graphics.drawCenteredString(this.f_96547_, Lang.translateDirect("ponder.previous"), this.left.m_252754_() + 10, tooltipY, tooltipColor);
        }
        if (this.close.m_198029_()) {
            graphics.drawCenteredString(this.f_96547_, Lang.translateDirect("ponder.close"), this.close.m_252754_() + 10, tooltipY, tooltipColor);
        }
        if (this.index != this.scenes.size() - 1 && this.right.m_198029_()) {
            graphics.drawCenteredString(this.f_96547_, Lang.translateDirect("ponder.next"), this.right.m_252754_() + 10, tooltipY, tooltipColor);
        }
        if (this.replay.m_198029_()) {
            graphics.drawCenteredString(this.f_96547_, Lang.translateDirect("ponder.replay"), this.replay.m_252754_() + 10, tooltipY, tooltipColor);
        }
        if (this.slowMode.m_198029_()) {
            graphics.drawCenteredString(this.f_96547_, Lang.translateDirect("ponder.slow_text"), this.slowMode.m_252754_() + 5, tooltipY, tooltipColor);
        }
        if (PonderIndex.editingModeActive() && this.userMode.m_198029_()) {
            graphics.drawCenteredString(this.f_96547_, "Editor View", this.userMode.m_252754_() + 10, tooltipY, tooltipColor);
        }
        ms.popPose();
    }

    protected void renderChapterTitle(GuiGraphics graphics, float fade, float indexDiff, PonderScene activeScene, int tooltipColor) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(0.0F, 0.0F, 400.0F);
        int x = 59;
        int y = 31;
        String title = activeScene.getTitle();
        int wordWrappedHeight = this.f_96547_.wordWrapHeight(title, this.left.m_252754_() - 51);
        int streakHeight = 26 + wordWrappedHeight;
        UIRenderHelper.streak(graphics, 0.0F, x - 4, y - 12 + streakHeight / 2, streakHeight, (int) (150.0F * fade));
        UIRenderHelper.streak(graphics, 180.0F, x - 4, y - 12 + streakHeight / 2, streakHeight, (int) (30.0F * fade));
        new BoxElement().<BoxElement>withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<BoxElement>gradientBorder(Theme.p(Theme.Key.PONDER_IDLE)).<RenderElement>at(21.0F, 21.0F, 100.0F).<RenderElement>withBounds(30, 30).render(graphics);
        GuiGameElement.of(this.stack).scale(2.0).<RenderElement>at((float) x - 39.0F, (float) y - 11.0F).render(graphics);
        graphics.drawString(this.f_96547_, Lang.translateDirect("ponder.pondering"), x, y - 6, tooltipColor, false);
        y += 8;
        x += 0;
        ms.translate((float) x, (float) y, 0.0F);
        ms.mulPose(Axis.XN.rotationDegrees(indexDiff * -75.0F));
        ms.translate(0.0F, 0.0F, 5.0F);
        FontHelper.drawSplitString(ms, this.f_96547_, title, 0, 0, this.left.m_252754_() - 51, Theme.c(Theme.Key.TEXT).scaleAlpha(1.0F - indexDiff).getRGB());
        ms.popPose();
        if (this.chapter != null) {
            ms.pushPose();
            ms.translate((float) (this.chap.m_252754_() - 8), (float) this.chap.m_252907_(), 0.0F);
            UIRenderHelper.streak(graphics, 180.0F, 4, 10, 26, (int) (150.0F * fade));
            this.drawRightAlignedString(graphics, ms, Lang.translateDirect("ponder.in_chapter").getString(), 0, 0, tooltipColor);
            this.drawRightAlignedString(graphics, ms, this.chapter.getTitle(), 0, 12, Theme.i(Theme.Key.TEXT));
            ms.popPose();
        }
    }

    protected void renderNavigationMenu(GuiGraphics graphics) {
        Color c1 = Theme.c(Theme.Key.PONDER_BACK_ARROW).setAlpha(64);
        Color c2 = Theme.c(Theme.Key.PONDER_BACK_ARROW).setAlpha(32);
        Color c3 = Theme.c(Theme.Key.PONDER_BACK_ARROW).setAlpha(16);
        UIRenderHelper.breadcrumbArrow(graphics, this.f_96543_ / 2 - 20, this.f_96544_ - 51, 0, 20, 20, 5, c1, c2);
        UIRenderHelper.breadcrumbArrow(graphics, this.f_96543_ / 2 + 20, this.f_96544_ - 51, 0, -20, 20, -5, c1, c2);
        UIRenderHelper.breadcrumbArrow(graphics, this.f_96543_ / 2 - 90, this.f_96544_ - 51, 0, 70, 20, 5, c1, c3);
        UIRenderHelper.breadcrumbArrow(graphics, this.f_96543_ / 2 + 90, this.f_96544_ - 51, 0, -70, 20, -5, c1, c3);
    }

    private void renderOverlay(GuiGraphics graphics, int i, float partialTicks) {
        if (!this.identifyMode) {
            PoseStack ms = graphics.pose();
            ms.pushPose();
            PonderScene story = (PonderScene) this.scenes.get(i);
            story.renderOverlay(this, graphics, this.skipCooling > 0 ? 0.0F : (this.identifyMode ? ponderPartialTicksPaused : partialTicks));
            ms.popPose();
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (this.identifyMode && this.hoveredBlockPos != null && PonderIndex.editingModeActive()) {
            long handle = this.f_96541_.getWindow().getWindow();
            if (this.copiedBlockPos != null && button == 1) {
                this.clipboardHelper.setClipboard(handle, "util.select.fromTo(" + this.copiedBlockPos.m_123341_() + ", " + this.copiedBlockPos.m_123342_() + ", " + this.copiedBlockPos.m_123343_() + ", " + this.hoveredBlockPos.m_123341_() + ", " + this.hoveredBlockPos.m_123342_() + ", " + this.hoveredBlockPos.m_123343_() + ")");
                this.copiedBlockPos = this.hoveredBlockPos;
                return true;
            } else {
                if (m_96638_()) {
                    this.clipboardHelper.setClipboard(handle, "util.select.position(" + this.hoveredBlockPos.m_123341_() + ", " + this.hoveredBlockPos.m_123342_() + ", " + this.hoveredBlockPos.m_123343_() + ")");
                } else {
                    this.clipboardHelper.setClipboard(handle, "util.grid.at(" + this.hoveredBlockPos.m_123341_() + ", " + this.hoveredBlockPos.m_123342_() + ", " + this.hoveredBlockPos.m_123343_() + ")");
                }
                this.copiedBlockPos = this.hoveredBlockPos;
                return true;
            }
        } else {
            return super.m_6375_(x, y, button);
        }
    }

    @Override
    public boolean keyPressed(int code, int p_keyPressed_2_, int p_keyPressed_3_) {
        Options settings = Minecraft.getInstance().options;
        int sCode = settings.keyDown.getKey().getValue();
        int aCode = settings.keyLeft.getKey().getValue();
        int dCode = settings.keyRight.getKey().getValue();
        int qCode = settings.keyDrop.getKey().getValue();
        if (code == sCode) {
            this.replay();
            return true;
        } else if (code == aCode) {
            this.scroll(false);
            return true;
        } else if (code == dCode) {
            this.scroll(true);
            return true;
        } else if (code == qCode) {
            this.identifyMode = !this.identifyMode;
            if (!this.identifyMode) {
                ((PonderScene) this.scenes.get(this.index)).deselect();
            }
            return true;
        } else {
            return super.keyPressed(code, p_keyPressed_2_, p_keyPressed_3_);
        }
    }

    @Override
    protected String getBreadcrumbTitle() {
        return this.chapter != null ? this.chapter.getTitle() : this.stack.getItem().getDescription().getString();
    }

    public Font getFontRenderer() {
        return this.f_96547_;
    }

    protected boolean isMouseOver(double mouseX, double mouseY, int x, int y, int w, int h) {
        boolean hovered = !(mouseX < (double) x) && !(mouseX > (double) (x + w));
        return hovered & (!(mouseY < (double) y) && !(mouseY > (double) (y + h)));
    }

    public static void renderSpeechBox(GuiGraphics graphics, int x, int y, int w, int h, boolean highlighted, Pointing pointing, boolean returnWithLocalTransform) {
        PoseStack ms = graphics.pose();
        if (!returnWithLocalTransform) {
            ms.pushPose();
        }
        int divotRotation = 0;
        int divotSize = 8;
        int distance = 1;
        int divotRadius = divotSize / 2;
        Couple<Color> borderColors = Theme.p(highlighted ? Theme.Key.PONDER_BUTTON_HOVER : Theme.Key.PONDER_IDLE);
        int boxX;
        int boxY;
        int divotX;
        int divotY;
        Color c;
        short var19;
        switch(pointing) {
            case DOWN:
            default:
                var19 = 0;
                boxX = x - w / 2;
                boxY = y - (h + divotSize + 1 + distance);
                divotX = x - divotRadius;
                divotY = y - (divotSize + distance);
                c = borderColors.getSecond();
                break;
            case LEFT:
                var19 = 90;
                boxX = x + divotSize + 1 + distance;
                boxY = y - h / 2;
                divotX = x + distance;
                divotY = y - divotRadius;
                c = Color.mixColors(borderColors, 0.5F);
                break;
            case RIGHT:
                var19 = 270;
                boxX = x - (w + divotSize + 1 + distance);
                boxY = y - h / 2;
                divotX = x - (divotSize + distance);
                divotY = y - divotRadius;
                c = Color.mixColors(borderColors, 0.5F);
                break;
            case UP:
                var19 = 180;
                boxX = x - w / 2;
                boxY = y + divotSize + 1 + distance;
                divotX = x - divotRadius;
                divotY = y + distance;
                c = borderColors.getFirst();
        }
        new BoxElement().<BoxElement>withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<BoxElement>gradientBorder(borderColors).<RenderElement>at((float) boxX, (float) boxY, 100.0F).<RenderElement>withBounds(w, h).render(graphics);
        ms.pushPose();
        ms.translate((float) (divotX + divotRadius), (float) (divotY + divotRadius), 10.0F);
        ms.mulPose(Axis.ZP.rotationDegrees((float) var19));
        ms.translate((float) (-divotRadius), (float) (-divotRadius), 0.0F);
        AllGuiTextures.SPEECH_TOOLTIP_BACKGROUND.render(graphics, 0, 0);
        AllGuiTextures.SPEECH_TOOLTIP_COLOR.render(graphics, 0, 0, c);
        ms.popPose();
        if (returnWithLocalTransform) {
            ms.translate((float) boxX, (float) boxY, 0.0F);
        } else {
            ms.popPose();
        }
    }

    public ItemStack getHoveredTooltipItem() {
        return this.hoveredTooltipItem;
    }

    public ItemStack getSubject() {
        return this.stack;
    }

    @Override
    public boolean isEquivalentTo(NavigatableSimiScreen other) {
        return other instanceof PonderUI ? ItemHelper.sameItem(this.stack, ((PonderUI) other).stack) : super.isEquivalentTo(other);
    }

    @Override
    public void shareContextWith(NavigatableSimiScreen other) {
        if (other instanceof PonderUI ponderUI) {
            ponderUI.referredToByTag = this.referredToByTag;
        }
    }

    public static float getPartialTicks() {
        float renderPartialTicks = Minecraft.getInstance().getFrameTime();
        if (Minecraft.getInstance().screen instanceof PonderUI) {
            PonderUI ui = (PonderUI) Minecraft.getInstance().screen;
            return ui.identifyMode ? ponderPartialTicksPaused : (renderPartialTicks + (float) (ui.extendedTickLength - ui.extendedTickTimer)) / (float) (ui.extendedTickLength + 1);
        } else {
            return renderPartialTicks;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    public void coolDownAfterSkip() {
        this.skipCooling = 15;
    }

    @Override
    public void removed() {
        super.m_7861_();
        this.hoveredTooltipItem = ItemStack.EMPTY;
    }

    public void drawRightAlignedString(GuiGraphics graphics, PoseStack ms, String string, int x, int y, int color) {
        graphics.drawString(this.f_96547_, string, (float) (x - this.f_96547_.width(string)), (float) y, color, false);
    }

    public boolean isComfyReadingEnabled() {
        return AllConfigs.client().comfyReading.get();
    }

    public void setComfyReadingEnabled(boolean slowTextMode) {
        AllConfigs.client().comfyReading.set(Boolean.valueOf(slowTextMode));
    }
}