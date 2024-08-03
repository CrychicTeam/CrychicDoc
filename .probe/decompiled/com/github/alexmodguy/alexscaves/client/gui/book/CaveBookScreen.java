package com.github.alexmodguy.alexscaves.client.gui.book;

import com.github.alexmodguy.alexscaves.client.model.CaveBookModel;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.CaveBookProgress;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.joml.Vector3f;

public class CaveBookScreen extends Screen {

    private static final CaveBookModel BOOK_MODEL = new CaveBookModel();

    private static final ResourceLocation BOOK_TEXTURE = new ResourceLocation("alexscaves", "textures/gui/book/cave_book_model.png");

    public static final float MOUSE_LEAN_THRESHOLD = 0.75F;

    public static final int PAGE_SIZE_IN_LINES = 15;

    public static final int TEXT_COLOR = 8546881;

    public static final int TEXT_LINK_COLOR = 1118481;

    public static final int TEXT_LINK_HOVER_COLOR = 38143;

    public static final int TEXT_LINK_LOCKED_COLOR = 13879723;

    private final CaveBookProgress caveBookProgress;

    public boolean unlockTooltip;

    private boolean incrementingPage;

    private boolean decrementingPage;

    private float prevFlipProgress;

    private float flipProgress;

    private float prevOpenBookProgress;

    private float openBookProgress;

    private int tickCount = 0;

    private float flipSpeed = 0.1F;

    private int lastTurnClickTimestamp = -1;

    private boolean hoveringPageLeft;

    private boolean hoveringPageRight;

    protected ResourceLocation prevEntryJSON = null;

    protected ResourceLocation currentEntryJSON;

    protected ResourceLocation nextEntryJSON;

    private BookEntry currentEntry;

    private BookEntry prevEntry;

    private BookEntry nextEntry;

    private int entryPageNumber = 0;

    private int lastEntryPageBeforeLinkClick = -1;

    private int closeBookForTicks;

    private PageRenderer prevLeftPageRenderer = new PageRenderer(-2);

    private PageRenderer prevRightPageRenderer = new PageRenderer(-1);

    private PageRenderer leftPageRenderer = new PageRenderer(0);

    private PageRenderer rightPageRenderer = new PageRenderer(1);

    private PageRenderer nextLeftPageRenderer = new PageRenderer(2);

    private PageRenderer nextRightPageRenderer = new PageRenderer(3);

    public CaveBookScreen(String openTo) {
        super(Component.translatable("item.alexscaves.cave_book"));
        this.caveBookProgress = CaveBookProgress.getCaveBookProgress(Minecraft.getInstance().player);
        this.currentEntryJSON = new ResourceLocation("alexscaves", openTo);
        this.resetEntry();
    }

    public CaveBookScreen() {
        this("books/root.json");
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void init() {
    }

    @Override
    public void tick() {
        this.prevFlipProgress = this.flipProgress;
        this.prevOpenBookProgress = this.openBookProgress;
        this.tickCount++;
        if (!(this.flipProgress < 1.0F) || !(this.openBookProgress >= 1.0F) || !this.decrementingPage && !this.incrementingPage) {
            if (this.incrementingPage) {
                if (this.nextEntryJSON != null) {
                    this.prevEntryJSON = this.currentEntryJSON;
                    this.currentEntryJSON = this.nextEntryJSON;
                    this.nextEntryJSON = null;
                    this.nextEntry = null;
                    this.resetEntry();
                } else {
                    this.entryPageNumber++;
                }
                this.incrementingPage = false;
                this.updatePageRenderers();
            }
            if (this.decrementingPage) {
                this.entryPageNumber--;
                if (this.entryPageNumber < 0 && this.prevEntry != null) {
                    int i = this.lastEntryPageBeforeLinkClick == -1 ? 0 : this.lastEntryPageBeforeLinkClick;
                    this.lastEntryPageBeforeLinkClick = -1;
                    this.currentEntryJSON = this.prevEntryJSON;
                    this.resetEntry();
                    this.entryPageNumber = i;
                }
                this.decrementingPage = false;
                this.updatePageRenderers();
            }
            this.prevFlipProgress = this.flipProgress = 0.0F;
        } else {
            this.flipProgress = Math.min(1.0F, this.flipProgress + this.flipSpeed);
        }
        if (this.isBookOpened()) {
            if (this.openBookProgress == 0.0F) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ACSoundRegistry.CAVE_BOOK_OPEN.get(), 1.0F));
            }
            if (this.openBookProgress < 1.0F) {
                this.openBookProgress += 0.1F;
            }
        } else {
            if (this.openBookProgress > 0.0F) {
                this.openBookProgress = Math.max(this.openBookProgress - 0.15F, 0.0F);
            }
            if (this.closeBookForTicks > 0) {
                this.closeBookForTicks--;
            }
            if (this.closeBookForTicks == 0 && this.nextEntryJSON != null) {
                this.prevEntryJSON = this.currentEntryJSON;
                this.currentEntryJSON = this.nextEntryJSON;
                this.nextEntryJSON = null;
                this.nextEntry = null;
                this.resetEntry();
            }
        }
    }

    public void resetEntry() {
        if (this.currentEntryJSON != null) {
            this.currentEntry = this.readBookEntry(this.currentEntryJSON);
            if (this.currentEntry != null) {
                this.currentEntry.init(this);
            }
        }
        if (this.currentEntry != null && this.currentEntry.getParent() != null && !this.currentEntry.getParent().isEmpty()) {
            this.prevEntryJSON = new ResourceLocation(getBookFileDirectory() + this.currentEntry.getParent());
        } else {
            this.prevEntryJSON = null;
        }
        if (this.prevEntryJSON != null) {
            this.prevEntry = this.readBookEntry(this.prevEntryJSON);
            if (this.prevEntry != null) {
                this.prevEntry.init(this);
            }
        }
        if (this.nextEntryJSON != null) {
            this.nextEntry = this.readBookEntry(this.nextEntryJSON);
            if (this.nextEntry != null) {
                this.nextEntry.init(this);
            }
        }
        if (this.currentEntry != null) {
            this.entryPageNumber = 0;
        }
        this.updatePageRenderers();
    }

    public void updatePageRenderers() {
        boolean flag = this.prevEntryJSON != null && this.entryPageNumber == 0;
        int pgOffsetReturningFromLink = this.lastEntryPageBeforeLinkClick != -1 && this.entryPageNumber == 0 ? this.lastEntryPageBeforeLinkClick : 0;
        this.leftPageRenderer.setEntryPageNumber(this.entryPageNumber);
        this.leftPageRenderer.setEntry(this.currentEntry);
        this.rightPageRenderer.setEntryPageNumber(this.entryPageNumber);
        this.rightPageRenderer.setEntry(this.currentEntry);
        this.prevLeftPageRenderer.setEntryPageNumber(this.entryPageNumber + pgOffsetReturningFromLink);
        this.prevLeftPageRenderer.setEntry(this.entryPageNumber == 0 && this.prevEntry != null ? this.prevEntry : this.currentEntry);
        this.prevRightPageRenderer.setEntryPageNumber(this.entryPageNumber + pgOffsetReturningFromLink);
        this.prevRightPageRenderer.setEntry(this.entryPageNumber == 0 && this.prevEntry != null ? this.prevEntry : this.currentEntry);
        this.nextLeftPageRenderer.setEntryPageNumber(this.entryPageNumber);
        this.nextLeftPageRenderer.setEntry(this.nextEntryJSON != null ? this.nextEntry : this.currentEntry);
        this.nextRightPageRenderer.setEntryPageNumber(this.entryPageNumber);
        this.nextRightPageRenderer.setEntry(this.nextEntryJSON != null ? this.nextEntry : this.currentEntry);
        if (this.nextEntryJSON != null) {
            this.nextLeftPageRenderer.enteringNewPageFlag = true;
            this.nextRightPageRenderer.enteringNewPageFlag = true;
        } else {
            this.nextLeftPageRenderer.enteringNewPageFlag = false;
            this.nextRightPageRenderer.enteringNewPageFlag = false;
        }
        if (flag) {
            this.prevLeftPageRenderer.leavingNewPageFlag = true;
            this.prevRightPageRenderer.leavingNewPageFlag = true;
        } else {
            this.prevLeftPageRenderer.leavingNewPageFlag = false;
            this.prevRightPageRenderer.leavingNewPageFlag = false;
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(0, 0, this.f_96543_, this.f_96544_, -1000, -1072689136, -804253680);
        MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(this, guiGraphics));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float fakePartialTickThatsZeroForSomeReason) {
        float partialTick = Minecraft.getInstance().getPartialTick();
        PoseStack poseStack = guiGraphics.pose();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        float ageInTicks = (float) this.tickCount + partialTick;
        float openGuiAmount = Math.min(ageInTicks, 5.0F) / 5.0F;
        float invOpenGuiAmount = 1.0F - openGuiAmount;
        float openBookAmount = this.prevOpenBookProgress + (this.openBookProgress - this.prevOpenBookProgress) * partialTick;
        float bookScale = 221.0F - invOpenGuiAmount * 180.0F;
        float flip = this.prevFlipProgress + (this.flipProgress - this.prevFlipProgress) * partialTick;
        float pageAngle = (float) Math.PI * (this.incrementingPage ? 1.0F - flip : flip);
        float pageFlipBump = (float) Math.sin((double) flip * Math.PI);
        float pageUp = this.incrementingPage ? 1.0F - flip : 0.0F;
        int i = this.f_96543_ / 2;
        int j = this.f_96544_ / 2;
        float mouseLeanX = (float) (mouseX - i) / bookScale;
        float mouseLeanY = (float) (mouseY - j) / bookScale;
        this.hoveringPageLeft = mouseLeanX < -0.75F && this.canGoLeft();
        this.hoveringPageRight = mouseLeanX > 0.75F && this.canGoRight();
        poseStack.pushPose();
        poseStack.translate((float) i + invOpenGuiAmount * (float) i * 0.5F, (float) (j + 6) + 15.0F * pageFlipBump, 100.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YN.rotationDegrees(50.0F * invOpenGuiAmount));
        poseStack.scale(bookScale, bookScale, bookScale);
        poseStack.pushPose();
        BOOK_MODEL.setupAnim(null, openBookAmount, pageAngle, pageUp, -20.0F * openBookAmount - 10.0F * pageFlipBump, 0.0F);
        BOOK_MODEL.mouseOver(mouseLeanX, mouseLeanY, ageInTicks, flip, this.canGoLeft(), this.canGoRight());
        BOOK_MODEL.m_7695_(poseStack, bufferSource.getBuffer(ForgeRenderTypes.getUnlitTranslucent(BOOK_TEXTURE)), 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.renderBookContents(poseStack, mouseX, mouseY, partialTick);
        guiGraphics.flush();
        poseStack.popPose();
        poseStack.popPose();
        if (this.currentEntry != null) {
            this.currentEntry.mouseOver(this, this.entryPageNumber, mouseLeanX, mouseLeanY);
        }
        super.render(guiGraphics, mouseX, mouseY, fakePartialTickThatsZeroForSomeReason);
        this.renderBackground(guiGraphics);
        if (this.unlockTooltip) {
            List<Component> list = new ArrayList();
            list.add(Component.translatable("book.alexscaves.page_locked_0").withStyle(ChatFormatting.GRAY));
            list.add(Component.translatable("book.alexscaves.page_locked_1").withStyle(ChatFormatting.GRAY));
            guiGraphics.renderTooltip(this.f_96547_, list, Optional.empty(), mouseX - 5, mouseY - 5);
        }
    }

    private void renderBookContents(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        boolean left = this.hoveringPageLeft || this.decrementingPage;
        boolean right = this.hoveringPageRight || this.incrementingPage;
        float flip = this.prevFlipProgress + (this.flipProgress - this.prevFlipProgress) * partialTick;
        if (left) {
            this.renderForPageType(this.leftPageRenderer, 2, poseStack, mouseX, mouseY, partialTick);
        } else if (flip < 0.9F) {
            this.renderForPageType(this.leftPageRenderer, 0, poseStack, mouseX, mouseY, partialTick);
        }
        if (right) {
            this.renderForPageType(this.rightPageRenderer, 3, poseStack, mouseX, mouseY, partialTick);
        } else if (flip < 0.9F) {
            this.renderForPageType(this.rightPageRenderer, 1, poseStack, mouseX, mouseY, partialTick);
        }
        if (this.incrementingPage) {
            this.renderForPageType(this.nextLeftPageRenderer, 2, poseStack, mouseX, mouseY, partialTick);
            if (flip > 0.1F) {
                this.renderForPageType(this.nextRightPageRenderer, 1, poseStack, mouseX, mouseY, partialTick);
            }
        }
        if (this.decrementingPage) {
            this.renderForPageType(this.prevRightPageRenderer, 3, poseStack, mouseX, mouseY, partialTick);
            if (flip > 0.1F) {
                this.renderForPageType(this.prevLeftPageRenderer, 0, poseStack, mouseX, mouseY, partialTick);
            }
        }
    }

    private void renderForPageType(PageRenderer contents, int kind, PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        poseStack.pushPose();
        BOOK_MODEL.translateToPage(poseStack, Math.min(kind, 2));
        switch(kind) {
            case 0:
                poseStack.translate(-0.1F, -0.1885F, -0.005F);
                break;
            case 1:
                poseStack.translate(-0.725F, -0.1885F, -0.005F);
                break;
            case 2:
                poseStack.translate(-0.0375F, -0.015F, -0.01F);
                break;
            case 3:
                poseStack.translate(-0.7125F, 0.0054F, -0.005F);
        }
        poseStack.translate(0.75F, 0.0F, 0.4F);
        poseStack.scale(0.005F, 0.005F, 0.005F);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        if (kind == 3) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }
        poseStack.scale(1.0F, 1.0F, 0.01F);
        contents.renderPage(this, poseStack, mouseX, mouseY, partialTick, kind >= 2);
        poseStack.popPose();
    }

    private boolean isBookOpened() {
        return this.tickCount >= 12 && this.currentEntry != null && this.closeBookForTicks <= 0;
    }

    public boolean canGoLeft() {
        return this.isBookOpened() && (this.entryPageNumber > 0 || this.prevEntryJSON != null);
    }

    public boolean canGoRight() {
        return this.isBookOpened() && this.entryPageNumber + 1 < this.currentEntry.getPageCount();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int i = this.f_96543_ / 2;
        float distFromMiddleX = (float) ((mouseX - (double) i) / 220.0);
        boolean prev = super.m_6375_(mouseX, mouseY, button);
        if (!prev) {
            if (this.currentEntry != null && this.currentEntry.consumeMouseClick(this)) {
                return true;
            } else {
                if (this.tickCount - this.lastTurnClickTimestamp < 8) {
                    this.flipSpeed = 0.3F;
                } else {
                    this.flipSpeed = 0.1F;
                }
                this.lastTurnClickTimestamp = this.tickCount;
                if (!this.decrementingPage && !this.incrementingPage) {
                    if (distFromMiddleX < -0.75F && this.canGoLeft()) {
                        this.decrementingPage = true;
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ACSoundRegistry.CAVE_BOOK_TURN.get(), 1.0F));
                        return true;
                    }
                    if (distFromMiddleX > 0.75F && this.canGoRight()) {
                        this.incrementingPage = true;
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ACSoundRegistry.CAVE_BOOK_TURN.get(), 1.0F));
                        return true;
                    }
                }
                return false;
            }
        } else {
            return true;
        }
    }

    @Nullable
    protected BookEntry readBookEntry(ResourceLocation res) {
        Optional<Resource> resource = null;
        BookEntry page = null;
        try {
            resource = Minecraft.getInstance().getResourceManager().m_213713_(res);
            if (resource.isPresent()) {
                BufferedReader inputstream = ((Resource) resource.get()).openAsReader();
                page = BookEntry.deserialize(inputstream);
            }
        } catch (IOException var5) {
            if (!(var5 instanceof AccessDeniedException)) {
                var5.printStackTrace();
            }
        }
        return page;
    }

    public CaveBookProgress getCaveBookProgress() {
        return this.caveBookProgress;
    }

    public int getEntryPageNumber() {
        return this.entryPageNumber;
    }

    public static String getBookFileDirectory() {
        return "alexscaves:books/";
    }

    public boolean attemptChangePage(ResourceLocation changePageTo, boolean goingForwards) {
        if (!this.currentEntryJSON.equals(changePageTo)) {
            this.lastEntryPageBeforeLinkClick = this.entryPageNumber;
        }
        if (goingForwards) {
            this.prevEntryJSON = this.currentEntryJSON;
        }
        this.nextEntryJSON = changePageTo;
        if (goingForwards) {
            this.closeBookForTicks = 10;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ACSoundRegistry.CAVE_BOOK_CLOSE.get(), 1.0F));
        }
        return true;
    }

    public static void fixLighting() {
        Vector3f light0 = new Vector3f(1.0F, 1.0F, -1.0F);
        Vector3f light1 = new Vector3f(1.0F, 1.0F, -1.0F);
        RenderSystem.setShaderLights(light0, light1);
    }

    public int getEntryVisiblity(String linkTo) {
        ResourceLocation resourceLocation = new ResourceLocation(getBookFileDirectory() + linkTo);
        BookEntry dummyEntry = this.readBookEntry(resourceLocation);
        int visiblity = 0;
        if (dummyEntry != null) {
            visiblity = dummyEntry.getVisibility(this);
        }
        if (visiblity != 2 && Minecraft.getInstance().player != null && Minecraft.getInstance().player.m_7500_()) {
            visiblity = 0;
        }
        return visiblity;
    }
}