package io.redspace.ironsspellbooks.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.network.ServerboundLearnSpell;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;

@OnlyIn(Dist.CLIENT)
public class EldritchResearchScreen extends Screen {

    private static final ResourceLocation WINDOW_LOCATION = new ResourceLocation("irons_spellbooks", "textures/gui/eldritch_research_screen/window.png");

    private static final ResourceLocation FRAME_LOCATION = new ResourceLocation("irons_spellbooks", "textures/gui/eldritch_research_screen/spell_frame.png");

    public static final int WINDOW_WIDTH = 252;

    public static final int WINDOW_HEIGHT = 140;

    private static final int WINDOW_INSIDE_X = 9;

    private static final int WINDOW_INSIDE_Y = 18;

    public static final int WINDOW_INSIDE_WIDTH = 234;

    public static final int WINDOW_INSIDE_HEIGHT = 113;

    private static final int WINDOW_TITLE_X = 8;

    private static final int WINDOW_TITLE_Y = 6;

    public static final int BACKGROUND_TILE_WIDTH = 16;

    public static final int BACKGROUND_TILE_HEIGHT = 16;

    public static final int BACKGROUND_TILE_COUNT_X = 14;

    public static final int BACKGROUND_TILE_COUNT_Y = 7;

    int leftPos;

    int topPos;

    InteractionHand activeHand;

    List<AbstractSpell> learnableSpells;

    List<EldritchResearchScreen.SpellNode> nodes;

    SyncedSpellData playerData;

    Vec2 maxViewportOffset;

    Vec2 viewportOffset;

    boolean isMouseHoldingSpell;

    boolean isMouseDragging;

    int heldSpellIndex = -1;

    int heldSpellTime = -1;

    int lastPlayerTick;

    static final int TIME_TO_HOLD = 15;

    private static final Component ALREADY_LEARNED = Component.translatable("ui.irons_spellbooks.research_already_learned").withStyle(ChatFormatting.DARK_AQUA);

    private static final Component UNLEARNED = Component.translatable("ui.irons_spellbooks.research_warning").withStyle(ChatFormatting.RED);

    public EldritchResearchScreen(Component pTitle, InteractionHand activeHand) {
        super(pTitle);
        this.activeHand = activeHand;
    }

    @Override
    protected void init() {
        this.learnableSpells = SpellRegistry.getEnabledSpells().stream().filter(spell -> !spell.isLearned(null)).toList();
        if (this.f_96541_ != null) {
            this.playerData = ClientMagicData.getSyncedSpellData(this.f_96541_.player);
        }
        this.viewportOffset = Vec2.ZERO;
        this.leftPos = (this.f_96543_ - 252) / 2;
        this.topPos = (this.f_96544_ - 140) / 2;
        this.nodes = new ArrayList();
        float f = 6.282F / (float) this.learnableSpells.size();
        for (int i = 0; i < this.learnableSpells.size(); i++) {
            float r = 35.0F;
            int x = this.leftPos + 126 - 8 + (int) (r * Mth.cos(f * (float) i));
            int y = this.topPos + 70 - 8 + (int) (r * Mth.sin(f * (float) i));
            this.nodes.add(new EldritchResearchScreen.SpellNode((AbstractSpell) this.learnableSpells.get(i), x, y));
        }
        float maxDistX = 0.0F;
        float maxDistY = 0.0F;
        for (int i = 0; i < this.nodes.size(); i++) {
            for (int j = 1; j < this.nodes.size(); j++) {
                int x = Math.abs(((EldritchResearchScreen.SpellNode) this.nodes.get(i)).x - ((EldritchResearchScreen.SpellNode) this.nodes.get(j)).x);
                if ((float) x > maxDistX) {
                    maxDistX = (float) x;
                }
                int y = Math.abs(((EldritchResearchScreen.SpellNode) this.nodes.get(i)).y - ((EldritchResearchScreen.SpellNode) this.nodes.get(j)).y);
                if ((float) y > maxDistY) {
                    maxDistY = (float) y;
                }
            }
        }
        this.maxViewportOffset = new Vec2((float) ((int) maxDistX), (float) ((int) maxDistY));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.fillGradient(0, 0, this.f_96543_, this.f_96544_, -1072689136, -804253680);
        this.drawBackdrop(this.leftPos + 9, this.topPos + 18);
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.f_19797_ != this.lastPlayerTick) {
                this.lastPlayerTick = player.f_19797_;
                if (this.isMouseHoldingSpell && this.heldSpellIndex >= 0 && this.heldSpellIndex < this.nodes.size() && !((EldritchResearchScreen.SpellNode) this.nodes.get(this.heldSpellIndex)).spell.isLearned(player)) {
                    if (this.heldSpellTime > 15) {
                        this.heldSpellTime = -1;
                        Messages.sendToServer(new ServerboundLearnSpell(this.activeHand, ((EldritchResearchScreen.SpellNode) this.nodes.get(this.heldSpellIndex)).spell.getSpellId()));
                        player.playNotifySound(SoundRegistry.LEARN_ELDRITCH_SPELL.get(), SoundSource.MASTER, 1.0F, (float) Utils.random.nextIntBetweenInclusive(9, 11) * 0.1F);
                    }
                    this.heldSpellTime++;
                    if (this.lastPlayerTick % 2 == 0) {
                        player.playNotifySound(SoundEvents.SOUL_ESCAPE, SoundSource.MASTER, 1.0F, Mth.lerp((float) this.heldSpellTime / 15.0F, 0.5F, 1.5F));
                        player.playNotifySound(SoundRegistry.UI_TICK.get(), SoundSource.MASTER, 1.0F, Mth.lerp((float) this.heldSpellTime / 15.0F, 0.5F, 1.5F));
                    }
                } else if (this.heldSpellTime >= 0) {
                    this.heldSpellTime = Math.max(this.heldSpellTime - 3, -1);
                }
            }
            this.handleConnections(guiGraphics, partialTick);
            List<FormattedCharSequence> tooltip = null;
            for (int i = 0; i < this.nodes.size(); i++) {
                EldritchResearchScreen.SpellNode node = (EldritchResearchScreen.SpellNode) this.nodes.get(i);
                this.drawNode(guiGraphics, node, player, i == this.heldSpellIndex && this.heldSpellTime > 0);
                if (this.isHoveringNode(node, mouseX, mouseY)) {
                    tooltip = buildTooltip(node.spell, this.f_96547_);
                }
            }
            guiGraphics.blit(WINDOW_LOCATION, this.leftPos, this.topPos, 0, 0, 252, 140);
            if (tooltip != null) {
                guiGraphics.renderTooltip(this.f_96541_.font, tooltip, mouseX, mouseY);
            }
        }
    }

    private void renderProgressOverlay(int x, int y, float progress) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        this.fillRect(bufferbuilder, x, y, Mth.ceil(16.0F * progress), 16, 244, 65, 255, 127);
    }

    private void fillRect(BufferBuilder pRenderer, int pX, int pY, int pWidth, int pHeight, int pRed, int pGreen, int pBlue, int pAlpha) {
        RenderSystem.setShader(GameRenderer::m_172811_);
        pRenderer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        pRenderer.m_5483_((double) (pX + 0), (double) (pY + 0), 0.0).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pRenderer.m_5483_((double) (pX + 0), (double) (pY + pHeight), 0.0).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pRenderer.m_5483_((double) (pX + pWidth), (double) (pY + pHeight), 0.0).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pRenderer.m_5483_((double) (pX + pWidth), (double) (pY + 0), 0.0).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        BufferUploader.drawWithShader(pRenderer.end());
    }

    private void drawNode(GuiGraphics guiGraphics, EldritchResearchScreen.SpellNode node, LocalPlayer player, boolean drawProgress) {
        this.drawWithClipping(node.spell.getSpellIconResource(), guiGraphics, node.x, node.y, 0, 0, 16, 16, 16, 16, this.leftPos + 9, this.topPos + 18, 234, 113);
        if (drawProgress) {
            this.renderProgressOverlay(node.x, node.y, (float) this.heldSpellTime / 15.0F);
        }
        this.drawWithClipping(FRAME_LOCATION, guiGraphics, node.x - 8, node.y - 8, node.spell.isLearned(player) ? 32 : 0, 0, 32, 32, 64, 32, this.leftPos + 9, this.topPos + 18, 234, 113);
    }

    private void drawWithClipping(ResourceLocation texture, GuiGraphics guiGraphics, int x, int y, int uvx, int uvy, int width, int height, int imageWidth, int imageHeight, int bbx, int bby, int bbw, int bbh) {
        x = (int) ((float) x + this.viewportOffset.x);
        if (x < bbx) {
            int xDiff = bbx - x;
            width -= xDiff;
            uvx += xDiff;
            x += xDiff;
        } else if (x > bbx + bbw - width) {
            int xDiff = x - (bbx + bbw - width);
            width -= xDiff;
        }
        y = (int) ((float) y + this.viewportOffset.y);
        if (y < bby) {
            int yDiff = bby - y;
            height -= yDiff;
            uvy += yDiff;
            y += yDiff;
        } else if (y > bby + bbh - height) {
            int yDiff = y - (bby + bbh - height);
            height -= yDiff;
        }
        if (width > 0 && height > 0) {
            guiGraphics.blit(texture, x, y, width, height, (float) uvx, (float) uvy, width, height, imageWidth, imageHeight);
        }
    }

    public static List<FormattedCharSequence> buildTooltip(AbstractSpell spell, Font font) {
        boolean learned = spell.isLearned(Minecraft.getInstance().player);
        MutableComponent name = spell.getDisplayName(null).withStyle(learned ? ChatFormatting.DARK_AQUA : ChatFormatting.RED);
        List<FormattedCharSequence> description = font.split(Component.translatable(String.format("%s.guide", spell.getComponentId())).withStyle(ChatFormatting.GRAY), 180);
        ArrayList<FormattedCharSequence> hoverText = new ArrayList();
        hoverText.add(FormattedCharSequence.forward(name.getString(), name.getStyle().withUnderlined(true)));
        hoverText.addAll(description);
        hoverText.add(FormattedCharSequence.EMPTY);
        hoverText.add((learned ? ALREADY_LEARNED : UNLEARNED).getVisualOrderText());
        return hoverText;
    }

    private void handleConnections(GuiGraphics guiGraphics, float partialTick) {
        guiGraphics.fill(0, 0, this.f_96543_, this.f_96544_, 0);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i < this.nodes.size() - 1; i++) {
            Vec2 a = new Vec2((float) ((EldritchResearchScreen.SpellNode) this.nodes.get(i)).x, (float) ((EldritchResearchScreen.SpellNode) this.nodes.get(i)).y);
            Vec2 b = new Vec2((float) ((EldritchResearchScreen.SpellNode) this.nodes.get(i + 1)).x, (float) ((EldritchResearchScreen.SpellNode) this.nodes.get(i + 1)).y);
            Vec2 org = new Vec2(-(b.y - a.y), b.x - a.x).normalized().scale(1.5F);
            double x1m1 = (double) (a.x + org.x + 8.0F + this.viewportOffset.x);
            double x2m1 = (double) (b.x + org.x + 8.0F + this.viewportOffset.x);
            double y1m1 = (double) (a.y + org.y + 8.0F + this.viewportOffset.y);
            double y2m1 = (double) (b.y + org.y + 8.0F + this.viewportOffset.y);
            double x1m2 = (double) (a.x - org.x + 8.0F + this.viewportOffset.x);
            double x2m2 = (double) (b.x - org.x + 8.0F + this.viewportOffset.x);
            double y1m2 = (double) (a.y - org.y + 8.0F + this.viewportOffset.y);
            double y2m2 = (double) (b.y - org.y + 8.0F + this.viewportOffset.y);
            float f = Mth.sin(((float) Minecraft.getInstance().player.f_19797_ + partialTick) * 0.1F);
            float glowIntensity = f * f;
            Vector4f color = new Vector4f(0.5294118F, 0.6039216F, 0.68235296F, 0.5F);
            Vector4f glowcolor = new Vector4f(0.95686275F, 0.25490198F, 1.0F, 0.5F);
            Vector4f color1 = lerpColor(color, glowcolor, glowIntensity * (float) (((EldritchResearchScreen.SpellNode) this.nodes.get(i)).spell.isLearned(Minecraft.getInstance().player) ? 1 : 0));
            Vector4f color2 = lerpColor(color, glowcolor, glowIntensity * (float) (((EldritchResearchScreen.SpellNode) this.nodes.get(i + 1)).spell.isLearned(Minecraft.getInstance().player) ? 1 : 0));
            double alphaTopLeft = Mth.clamp(x1m1 + (double) this.viewportOffset.x - (double) this.leftPos, 0.0, 18.0) / 9.0 * 2.0 * Mth.clamp(y1m1 + (double) this.viewportOffset.y - (double) this.topPos, 0.0, 36.0) / 18.0 * 2.0;
            buffer.m_5483_(x1m1, y1m1, 0.0).color(color1.x(), color1.y(), color1.z(), this.fadeOutTowardEdges(guiGraphics, x1m1, y1m1)).endVertex();
            buffer.m_5483_(x2m1, y2m1, 0.0).color(color2.x(), color2.y(), color2.z(), this.fadeOutTowardEdges(guiGraphics, x2m1, y2m1)).endVertex();
            buffer.m_5483_(x2m2, y2m2, 0.0).color(color2.x(), color2.y(), color2.z(), this.fadeOutTowardEdges(guiGraphics, x2m2, y2m2)).endVertex();
            buffer.m_5483_(x1m2, y1m2, 0.0).color(color1.x(), color1.y(), color1.z(), this.fadeOutTowardEdges(guiGraphics, x1m2, y1m2)).endVertex();
        }
        tesselator.end();
    }

    private float fadeOutTowardEdges(GuiGraphics guiGraphics, double x, double y) {
        int px = (int) Mth.clamp(x + (double) this.viewportOffset.x - (double) this.leftPos, 0.0, 18.0);
        int py = (int) Mth.clamp(y + (double) this.viewportOffset.y - (double) this.topPos, 0.0, 36.0);
        int px2 = (int) Mth.clamp(234.0 - (x + (double) this.viewportOffset.x - (double) this.leftPos), 0.0, 18.0);
        int py2 = (int) Mth.clamp(113.0 - (y + (double) this.viewportOffset.y - (double) this.topPos), 0.0, 36.0);
        return Mth.clamp((float) px / 4.5F, 0.0F, 1.0F) * Mth.clamp((float) py / 9.0F, 0.0F, 1.0F) * Mth.clamp((float) px2 / 4.5F, 0.0F, 1.0F) * Mth.clamp((float) py2 / 9.0F, 0.0F, 1.0F);
    }

    private int colorFromRGBA(Vector4f rgba) {
        int r = (int) (rgba.x() * 255.0F) & 0xFF;
        int g = (int) (rgba.y() * 255.0F) & 0xFF;
        int b = (int) (rgba.z() * 255.0F) & 0xFF;
        int a = (int) (rgba.w() * 255.0F) & 0xFF;
        return (r << 24) + (g << 16) + (b << 8) + a;
    }

    private void drawBackdrop(int left, int top) {
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172755_);
        RenderSystem.setShaderTexture(0, TheEndPortalRenderer.END_PORTAL_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        float f = Minecraft.getInstance().player != null ? (float) Minecraft.getInstance().player.f_19797_ * 0.086F : 0.0F;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.m_5483_((double) ((float) left), (double) ((float) top + 113.0F), 0.0).uv(f, f).color(1, 1, 1, 1).endVertex();
        bufferbuilder.m_5483_((double) ((float) left + 234.0F), (double) ((float) top + 113.0F), 0.0).color(1, 1, 1, 1).endVertex();
        bufferbuilder.m_5483_((double) ((float) left + 234.0F), (double) ((float) top), 0.0).color(1, 1, 1, 1).endVertex();
        bufferbuilder.m_5483_((double) ((float) left), (double) ((float) top), 0.0).color(1, 1, 1, 1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }

    private static Vector4f lerpColor(Vector4f a, Vector4f b, float pDelta) {
        float f = 1.0F - pDelta;
        float x = a.x() * f + b.x() * pDelta;
        float y = a.y() * f + b.y() * pDelta;
        float z = a.z() * f + b.z() * pDelta;
        float w = a.w() * f + b.w() * pDelta;
        return new Vector4f(x, y, z, w);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        int mouseX = (int) pMouseX;
        int mouseY = (int) pMouseY;
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.m_21120_(this.activeHand).is(ItemRegistry.ELDRITCH_PAGE.get())) {
            for (int i = 0; i < this.nodes.size(); i++) {
                if (this.isHoveringNode((EldritchResearchScreen.SpellNode) this.nodes.get(i), mouseX, mouseY)) {
                    this.heldSpellIndex = i;
                    this.isMouseHoldingSpell = true;
                    break;
                }
            }
        }
        if (!this.isMouseHoldingSpell && this.isHovering(this.leftPos + 9, this.topPos + 18, 234, 113, mouseX, mouseY)) {
            this.isMouseDragging = true;
        }
        return super.m_6375_(pMouseX, pMouseY, pButton);
    }

    public boolean isHoveringNode(EldritchResearchScreen.SpellNode node, int mouseX, int mouseY) {
        return this.isHovering(node.x - 2 + (int) this.viewportOffset.x, node.y - 2 + (int) this.viewportOffset.y, 20, 20, mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        this.isMouseHoldingSpell = false;
        this.isMouseDragging = false;
        return super.m_6348_(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.isMouseDragging) {
        }
        return super.m_7979_(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey(pKeyCode, pScanCode);
        if (this.f_96541_.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.m_7379_();
            return true;
        } else {
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private boolean isHovering(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    static record NodeConnection(EldritchResearchScreen.SpellNode node1, EldritchResearchScreen.SpellNode node2) {
    }

    static record SpellNode(AbstractSpell spell, int x, int y) {
    }
}