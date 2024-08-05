package com.mna.gui;

import com.mna.api.affinity.Affinity;
import com.mna.api.cantrips.ICantrip;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.resource.ICastingResourceGuiProvider;
import com.mna.api.config.ClientConfigValues;
import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.api.items.ChargeableItem;
import com.mna.api.items.inventory.ISpellBookInventory;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.cantrips.CantripRegistry;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.magic.resources.CastingResourceGuiRegistry;
import com.mna.effects.EffectInit;
import com.mna.entities.constructs.animated.Construct;
import com.mna.gui.widgets.entity.ConstructDiagnosticsDisplay;
import com.mna.items.sorcery.ItemSpellBook;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.math.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.joml.Matrix4f;
import top.theillusivec4.curios.api.CuriosApi;

public class HUDOverlayRenderer {

    private Minecraft mc;

    private RecipeRendererBase pinnedRecipe;

    private ConstructDiagnosticsDisplay pinnedDiagnostics;

    private int cantripTimer = 0;

    private int cantripTimerMax = 1;

    private boolean showTimerBar = true;

    private String castingCantrip = null;

    private float cantripAlpha = 0.0F;

    public boolean isRenderingSpellBookChords = false;

    protected int screenWidth;

    protected int screenHeight;

    public static HUDOverlayRenderer instance;

    private static final int AFFINITIES_TO_DRAW = 2;

    private static final int NUMBER_WIDTH = 6;

    private static final int NUMBER_HEIGHT = 10;

    public HUDOverlayRenderer() {
        this.mc = Minecraft.getInstance();
        this.pinnedDiagnostics = new ConstructDiagnosticsDisplay(0, 0, null);
    }

    @SubscribeEvent(receiveCanceled = true, priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void onRenderGameOverlay(RenderGuiOverlayEvent.Pre event) {
        Player entityPlayerSP = instance.mc.player;
        instance.screenWidth = instance.mc.getWindow().getGuiScaledWidth();
        instance.screenHeight = instance.mc.getWindow().getGuiScaledHeight();
        if (entityPlayerSP != null) {
            if (!entityPlayerSP.m_6084_()) {
                instance.setPinnedRecipe(null);
                instance.setTrackedConstruct(null);
            }
            if (!entityPlayerSP.m_21023_(EffectInit.MIND_VISION.get()) && !entityPlayerSP.m_21023_(EffectInit.POSSESSION.get())) {
                if (instance.renderColdDarkFade(event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), event.getGuiGraphics()) || instance.renderTeleportHud(event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), event.getGuiGraphics())) {
                    event.setCanceled(true);
                    Minecraft.getInstance().setScreen(null);
                }
            } else {
                MutableComponent prompt = Component.translatable("gui.mna.doublejumptoexitprompt");
                int x = event.getWindow().getGuiScaledWidth() / 2;
                int y = event.getWindow().getGuiScaledHeight() - 9 - 5;
                event.getGuiGraphics().drawCenteredString(instance.mc.font, prompt, x, y, ChatFormatting.GOLD.getColor());
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (instance.cantripTimer > 0) {
            instance.cantripTimer--;
            if (instance.cantripTimer <= 10) {
                instance.cantripAlpha -= 0.1F;
            } else if (instance.cantripTimer >= instance.cantripTimerMax - 10) {
                instance.cantripAlpha += 0.1F;
            }
            if (instance.cantripTimer == 0) {
                instance.castingCantrip = null;
            }
        }
    }

    public void renderHUD(GuiGraphics pGuiGraphics, int screenWidth, int screenHeight, float partialTicks) {
        Player player = this.mc.player;
        Font fr = this.mc.font;
        IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if (magic != null && magic.isMagicUnlocked()) {
            Pair<Integer, Integer> coord = this.getHudCoordinates(screenWidth, screenHeight);
            int yPos = (Integer) coord.getSecond();
            int xPos = (Integer) coord.getFirst();
            RenderSystem.setShader(GameRenderer::m_172649_);
            RenderSystem.setShaderTexture(0, GuiTextures.Hud.BARS);
            this.renderManaBar(pGuiGraphics, xPos, yPos, magic, this.mc.player, partialTicks);
            if (ClientConfigValues.HudAffinity) {
                this.renderAffinity(pGuiGraphics, magic, fr, screenWidth, screenHeight);
            }
        }
    }

    private Pair<Integer, Integer> getHudCoordinates(int screenWidth, int screenHeight) {
        int UIWidth = 170;
        int UIHeight = 35;
        switch(ClientConfigValues.HudPosition) {
            case BottomCenter:
                return new Pair(screenWidth / 2 - UIWidth / 2, screenHeight - UIHeight - 45);
            case BottomLeft:
                return new Pair(10, screenHeight - UIHeight);
            case BottomRight:
                return new Pair(screenWidth - UIWidth, screenHeight - UIHeight);
            case MiddleLeft:
                if (this.pinnedRecipe != null) {
                    return new Pair(-10, screenHeight / 2 + (int) ((float) this.pinnedRecipe.m_93694_() * this.getPinSizeScale()) / 2);
                }
                return new Pair(-10, screenHeight / 2 - UIHeight / 2);
            case MiddleRight:
                return new Pair(screenWidth - UIWidth, screenHeight / 2 - UIHeight / 2);
            case TopCenter:
                return new Pair(screenWidth / 2 - UIWidth / 2, 1);
            case TopRight:
                return new Pair(screenWidth - UIWidth, 1);
            case TopLeft:
            default:
                return new Pair(-10, 1);
        }
    }

    private float getPinSizeScale() {
        return ClientConfigValues.PinnedRecipeScale == ClientConfigValues.PinnedRecipeSize.Small ? 0.35F : (ClientConfigValues.PinnedRecipeScale == ClientConfigValues.PinnedRecipeSize.Medium ? 0.55F : 0.75F);
    }

    public void renderPinnedRecipe(GuiGraphics pGuiGraphics, int screenWidth, int screenHeight) {
        if (this.pinnedRecipe != null && this.mc.player != null) {
            float scale = this.getPinSizeScale();
            float yPos = (float) (screenHeight / 2) - (float) this.pinnedRecipe.m_93694_() * scale / 2.0F;
            this.pinnedRecipe.setScale(scale);
            this.pinnedRecipe.m_252865_(0);
            this.pinnedRecipe.m_253211_((int) yPos);
            this.pinnedRecipe.m_88315_(pGuiGraphics, 0, 0, 0.0F);
            RenderSystem.disableDepthTest();
        }
    }

    public void renderPinnedDiagnostics(GuiGraphics pGuiGraphics, int screenWidth, int screenHeight) {
        if (this.pinnedDiagnostics.isValid() && this.mc.player != null) {
            float scale = this.getPinSizeScale();
            float yPos = (float) (screenHeight / 2 - this.pinnedDiagnostics.m_93694_() / 2);
            this.pinnedDiagnostics.m_252865_(0);
            this.pinnedDiagnostics.m_253211_((int) yPos);
            this.pinnedDiagnostics.m_88315_(pGuiGraphics, 0, 0, 0.0F);
            RenderSystem.disableDepthTest();
        }
    }

    private void renderManaBar(GuiGraphics pGuiGraphics, int xPos, int yPos, IPlayerMagic magic, Player player, float partialTicks) {
        if (!(magic.getCastingResource().getMaxAmount() <= 0.0F)) {
            ICastingResourceGuiProvider provider = CastingResourceGuiRegistry.Instance.getGuiProvider(magic.getCastingResource().getRegistryName());
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate((float) xPos, (float) yPos, 0.0F);
            int var23 = 14;
            int var24 = 6;
            pGuiGraphics.fill(var23 + provider.getFillStartX(), var24 + provider.getFillStartY(), var23 + provider.getFillStartX() + provider.getFillWidth(), var24 + provider.getFillStartY() + provider.getFillHeight(), 2130706432);
            int scaledManaBarWidth = (int) (magic.getCastingResource().getAmount() / magic.getCastingResource().getMaxAmount() * (float) provider.getFillWidth());
            pGuiGraphics.pose().pushPose();
            if (scaledManaBarWidth > 0) {
                pGuiGraphics.fill(var23 + provider.getFillStartX(), var24 + provider.getFillStartY(), var23 + provider.getFillStartX() + scaledManaBarWidth, var24 + provider.getFillStartY() + provider.getFillHeight(), provider.getBarColor());
                SpellRecipe mainHand = null;
                SpellRecipe offHand = null;
                if (player.m_21205_().getItem() instanceof ItemSpellBook) {
                    mainHand = SpellRecipe.fromNBT(((ItemSpellBook) player.m_21205_().getItem()).getSpellCompound(player.m_21205_(), player));
                } else {
                    mainHand = SpellRecipe.fromNBT(player.m_21205_().getTag());
                }
                if (player.m_21206_().getItem() instanceof ItemSpellBook) {
                    offHand = SpellRecipe.fromNBT(((ItemSpellBook) player.m_21206_().getItem()).getSpellCompound(player.m_21206_(), player));
                } else {
                    offHand = SpellRecipe.fromNBT(player.m_21206_().getTag());
                }
                if (mainHand != null && mainHand.isValid()) {
                    SpellCaster.applyAdjusters(player.m_21205_(), player, mainHand, SpellCastStage.CALCULATING_MANA_COST);
                }
                if (mainHand != null && offHand.isValid()) {
                    SpellCaster.applyAdjusters(player.m_21206_(), player, offHand, SpellCastStage.CALCULATING_MANA_COST);
                }
                float manaCost = mainHand.getManaCost() + offHand.getManaCost();
                if (manaCost > 0.0F) {
                    float manaCostPct = MathUtils.clamp01(manaCost / magic.getCastingResource().getMaxAmount());
                    int color = provider.getBarManaCostEstimateColor();
                    if (manaCost > magic.getCastingResource().getAmount()) {
                        color = 16711680;
                    }
                    int blitWidth = (int) ((float) provider.getFrameWidth() * manaCostPct);
                    int startX = Math.max(var23 + provider.getFillStartX() + scaledManaBarWidth - blitWidth, var23 + provider.getFillStartX());
                    pGuiGraphics.fill(startX, var24 + provider.getFillStartY(), startX + blitWidth, var24 + provider.getFillStartY() + provider.getFillHeight(), color);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
            RenderSystem.setShader(GameRenderer::m_172649_);
            int m = (int) ((float) magic.getMagicXP() / (float) magic.getXPForLevel(magic.getMagicLevel() + 1) * (float) provider.getFillWidth());
            pGuiGraphics.fill(var23 + provider.getXPBarOffsetX(), var24 + provider.getXPBarOffsetY(), var23 + provider.getXPBarOffsetX() + m, var24 + provider.getXPBarOffsetY() + provider.getXPBarHeight(), provider.getXPBarColor());
            RenderSystem.setShaderTexture(0, provider.getTexture());
            pGuiGraphics.blit(GuiTextures.Hud.BARS, var23, var24, provider.getFrameU(), provider.getFrameV(), provider.getFrameWidth(), provider.getFrameHeight());
            int lcs = this.getLowChargeItemStatus();
            if (lcs != 0) {
                int delta = (provider.getFrameHeight() - provider.getFillHeight()) / 2 + provider.getLowChargeOffsetY();
                int v = lcs == 2 ? 18 : 0;
                pGuiGraphics.blit(GuiTextures.Hud.BARS, var23 + provider.getFrameWidth() + 2, var24 + delta, 20, 18, 230.0F, (float) v, 20, 18, 256, 256);
            }
            int l = magic.getMagicLevel();
            int digitOffset = 0;
            List<Integer> digits = new ArrayList();
            while (l > 0) {
                int digit = l % 10;
                l /= 10;
                digits.add(digit);
            }
            int levelXOffset = digits.size() == 1 ? 4 : 0;
            int offsetY = 0;
            switch(ClientConfigValues.HudPosition) {
                case BottomCenter:
                case BottomLeft:
                case BottomRight:
                    offsetY = -30;
                default:
                    for (int i = digits.size() - 1; i >= 0; i--) {
                        int u = 250;
                        int v = 10 * (Integer) digits.get(i);
                        pGuiGraphics.blit(GuiTextures.Hud.BARS, var23 + provider.getLevelDisplayX() + digitOffset + levelXOffset, var24 + provider.getLevelDisplayY() + offsetY, u, v, 6, 10);
                        digitOffset += 7;
                    }
                    ItemStack deco = provider.getBadgeItem();
                    if (!deco.isEmpty()) {
                        pGuiGraphics.renderItem(deco, xPos + provider.getBadgeItemOffsetX(), yPos + provider.getBadgeItemOffsetY());
                    }
                    pGuiGraphics.pose().popPose();
                    Font fr = this.mc.font;
                    String s = String.format("%.2f / %.2f", magic.getCastingResource().getAmount(), magic.getCastingResource().getMaxAmount());
                    int textWidth = fr.width(s);
                    int textX = var23 + provider.getResourceNumericOffsetX() + provider.getFillWidth() / 2 - textWidth / 2;
                    pGuiGraphics.drawString(fr, s, textX, var24 + provider.getResourceNumericOffsetY(), provider.getResourceNumericTextColor(), false);
                    pGuiGraphics.pose().popPose();
            }
        }
    }

    private int getLowChargeItemStatus() {
        MutableFloat lowestPct = new MutableFloat(1.0F);
        this.mc.player.m_150109_().armor.stream().forEach(isx -> lowestPct.setValue(Math.min(lowestPct.getValue(), this.getChargePct(isx))));
        this.mc.player.m_150109_().items.stream().forEach(isx -> lowestPct.setValue(Math.min(lowestPct.getValue(), this.getChargePct(isx))));
        LazyOptional<IItemHandlerModifiable> lazyCurios = CuriosApi.getCuriosHelper().getEquippedCurios(this.mc.player);
        if (lazyCurios.isPresent()) {
            IItemHandlerModifiable curios = (IItemHandlerModifiable) lazyCurios.resolve().get();
            for (int i = 0; i < curios.getSlots(); i++) {
                ItemStack is = curios.getStackInSlot(i);
                lowestPct.setValue(Math.min(lowestPct.getValue(), this.getChargePct(is)));
            }
        }
        if (lowestPct.getValue() == 0.0F) {
            return 2;
        } else {
            return lowestPct.getValue() < 0.1F ? 1 : 0;
        }
    }

    private float getChargePct(ItemStack is) {
        if (is.getItem() instanceof ChargeableItem) {
            ChargeableItem ci = (ChargeableItem) is.getItem();
            return ci.getMana(is) / ci.getMaxMana();
        } else {
            return 1.0F;
        }
    }

    void renderAffinity(GuiGraphics pGuiGraphics, IPlayerMagic magic, Font fr, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (fr == null) {
            fr = mc.font;
        }
        if (magic == null) {
            Player p = mc.player;
            if (p == null) {
                return;
            }
            LazyOptional<IPlayerMagic> cap = p.getCapability(PlayerMagicProvider.MAGIC);
            if (!cap.isPresent()) {
                return;
            }
            magic = (IPlayerMagic) cap.resolve().get();
        }
        List<Entry<Affinity, Float>> affs = (List<Entry<Affinity, Float>>) magic.getSortedAffinityDepths().entrySet().stream().limit(2L).collect(Collectors.toList());
        int y = screenHeight - 5 - 14 * affs.size();
        int x = screenWidth - 45;
        int count = 0;
        if (ClientConfigValues.HudPosition == ClientConfigValues.HudPos.BottomRight) {
            y = screenHeight - 45;
        }
        for (Entry<Affinity, Float> aff : affs) {
            if (count > 2 || (Float) aff.getValue() < 0.5F) {
                break;
            }
            ItemStack affStack = (ItemStack) GuiTextures.affinityIcons.get(aff.getKey());
            if (!affStack.isEmpty()) {
                pGuiGraphics.renderItem(affStack, x, y);
                String s = String.format("%.1f%%", aff.getValue());
                pGuiGraphics.drawCenteredString(fr, s, x + 16 + fr.width(s) / 2, y + 4, 14737632);
                if (ClientConfigValues.HudPosition == ClientConfigValues.HudPos.BottomRight) {
                    x -= 45;
                } else {
                    y += 14;
                }
                count++;
            }
        }
        if (count > 0) {
            String s = I18n.get("gui.mna.affinity");
            if (ClientConfigValues.HudPosition == ClientConfigValues.HudPos.BottomRight) {
                pGuiGraphics.drawCenteredString(fr, s, screenWidth - 45 * affs.size() - fr.width(s) / 2, y + 3, 14737632);
            } else {
                pGuiGraphics.drawCenteredString(fr, s, screenWidth - fr.width(s) / 2, screenHeight - 5 - 14 * affs.size() - 9, 14737632);
            }
        }
    }

    public void setPinnedRecipe(RecipeRendererBase renderer) {
        this.pinnedRecipe = renderer;
    }

    public void setTrackedConstruct(Construct construct) {
        this.pinnedDiagnostics.setConstruct(construct);
    }

    public Construct getTrackedConstruct() {
        return this.pinnedDiagnostics.getConstruct();
    }

    public void setCastingCantrip(String cantrip, int timer) {
        this.castingCantrip = cantrip;
        if (timer == 0) {
            this.cantripTimer = 60;
            this.showTimerBar = false;
        } else {
            this.cantripTimer = timer;
            this.cantripTimerMax = Math.max(timer, 1);
        }
        this.cantripAlpha = 0.0F;
    }

    public void renderCantripTimer(GuiGraphics pGuiGraphics, int screenWidth, int screenHeight, float partialTicks) {
        if (this.castingCantrip != null) {
            Optional<ICantrip> cantrip = CantripRegistry.INSTANCE.getCantrip(new ResourceLocation(this.castingCantrip));
            if (cantrip != null && cantrip.isPresent()) {
                RenderSystem.setShader(GameRenderer::m_172814_);
                int x = screenWidth / 2 - GuiTextures.Cantrip_Icon_Size.x / 2;
                int y = screenHeight / 2 - GuiTextures.Cantrip_Icon_Size.y;
                float alphaF = MathUtils.clamp01(this.cantripAlpha);
                int alpha = (int) (255.0F * alphaF);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alphaF);
                pGuiGraphics.blit(((ICantrip) cantrip.get()).getIcon(), x, y, 0, 0.0F, 0.0F, GuiTextures.Cantrip_Icon_Size.x, GuiTextures.Cantrip_Icon_Size.y, GuiTextures.Cantrip_Icon_Size.x, GuiTextures.Cantrip_Icon_Size.y);
                if (this.showTimerBar) {
                    y += GuiTextures.Cantrip_Icon_Size.y + 10;
                    float pct = ((float) this.cantripTimer + partialTicks) / (float) this.cantripTimerMax;
                    this.renderTimer(pGuiGraphics, pct, alpha, x, y, GuiTextures.Cantrip_Icon_Size.x, 0, 0, 255);
                }
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    public void renderCenteredTimer(GuiGraphics pGuiGraphics, float pct, int alpha, int screenWidth, int screenHeight, int width, int r, int g, int b) {
        this.renderTimer(pGuiGraphics, pct, alpha, screenWidth / 2 - width / 2, screenHeight / 2 + 10, width, r, g, b);
    }

    private void renderTimer(GuiGraphics pGuiGraphics, float pct, int alpha, int x, int y, int width, int r, int g, int b) {
        pGuiGraphics.fill(x, y, (int) ((float) x + (float) width * pct), y + 3, FastColor.ARGB32.color(255, r, g, b));
    }

    protected boolean renderColdDarkFade(int width, int height, GuiGraphics pGuiGraphics) {
        if (this.mc.player.m_21023_(EffectInit.COLD_DARK.get())) {
            int delay = 50;
            int fadeTime = 100;
            int duration = this.mc.player.m_21124_(EffectInit.COLD_DARK.get()).getDuration();
            if (duration < delay + fadeTime) {
                RenderSystem.disableDepthTest();
                float opacity = (float) (fadeTime - (duration - delay)) / (float) fadeTime;
                if (opacity > 1.0F) {
                    opacity = 1.0F;
                }
                int color = (int) (255.0F * opacity) << 24 | 1052704;
                pGuiGraphics.fill(0, 0, width, height, color);
                RenderSystem.enableDepthTest();
            }
            return true;
        } else {
            return false;
        }
    }

    protected boolean renderTeleportHud(int width, int height, GuiGraphics pGuiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        IPlayerMagic playerMagic = (IPlayerMagic) mc.player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if (playerMagic != null && playerMagic.getTeleportElapsedTicks() != 0) {
            long gameTime = mc.player.m_9236_().getGameTime();
            int transitionTicks = playerMagic.getTeleportElapsedTicks();
            int transitionDuration = playerMagic.getTeleportTotalTicks();
            int endFadeTicks = Math.max(transitionDuration - 40, transitionDuration);
            float warpPct = (float) transitionTicks / (float) transitionDuration;
            float portalAlpha = (float) Math.max(transitionTicks - (transitionDuration - endFadeTicks), 0) / (float) endFadeTicks;
            long angle = gameTime * 10L;
            float size = (float) Math.max(width, height) * warpPct;
            RenderSystem.disableDepthTest();
            int color = (int) (89.25 * (double) warpPct) << 24;
            pGuiGraphics.fill(0, 0, width, height, color);
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate((float) (width / 2), (float) (height / 2), 0.0F);
            pGuiGraphics.pose().mulPose(Axis.ZN.rotationDegrees((float) angle));
            float pX1 = -size;
            float pY1 = -size;
            float pMinU = 0.0F;
            float pMaxU = 1.0F;
            float pMinV = 0.0F;
            float pMaxV = 1.0F;
            float pBlitOffset = 0.0F;
            RenderSystem.setShaderTexture(0, GuiTextures.Overlay.TELEPORTING);
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (1.0F - portalAlpha) * 0.5F);
            Matrix4f matrix4f = pGuiGraphics.pose().last().pose();
            BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferbuilder.m_252986_(matrix4f, pX1, pY1, pBlitOffset).uv(pMinU, pMinV).endVertex();
            bufferbuilder.m_252986_(matrix4f, pX1, size, pBlitOffset).uv(pMinU, pMaxV).endVertex();
            bufferbuilder.m_252986_(matrix4f, size, size, pBlitOffset).uv(pMaxU, pMaxV).endVertex();
            bufferbuilder.m_252986_(matrix4f, size, pY1, pBlitOffset).uv(pMaxU, pMinV).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            pGuiGraphics.pose().popPose();
            return true;
        } else {
            return false;
        }
    }

    protected void renderTextureOverlay(ResourceLocation pTextureLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, pAlpha);
        RenderSystem.setShaderTexture(0, pTextureLocation);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.m_5483_(0.0, (double) this.screenHeight, -90.0).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.m_5483_((double) this.screenWidth, (double) this.screenHeight, -90.0).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.m_5483_((double) this.screenWidth, 0.0, -90.0).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.m_5483_(0.0, 0.0, -90.0).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void renderSpellBookChordsHud(Player player, GuiGraphics pGuiGraphics) {
        if (this.isRenderingSpellBookChords) {
            IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic != null) {
                ItemStack mhStack = player.m_21120_(InteractionHand.MAIN_HAND);
                ItemStack ohStack = player.m_21120_(InteractionHand.OFF_HAND);
                ISpellBookInventory bookInv = null;
                if (mhStack.getItem() instanceof ItemSpellBook) {
                    bookInv = ((ItemSpellBook) mhStack.getItem()).getInventory(mhStack, magic);
                } else if (ohStack.getItem() instanceof ItemSpellBook) {
                    bookInv = ((ItemSpellBook) ohStack.getItem()).getInventory(ohStack, magic);
                }
                if (bookInv != null) {
                    ItemStack[] spells = bookInv.getActiveSpells();
                    if (spells.length != 0) {
                        int loops = Math.min(spells.length, 9);
                        int xStep = loops * 4;
                        int width = xStep * loops;
                        int xStart = (this.screenWidth - width) / 2;
                        int xEnd = xStart + width;
                        int yStart = 32;
                        int height = 24;
                        int solidColor = FastColor.ARGB32.color(255, 42, 42, 42);
                        pGuiGraphics.fillGradient(xStart, yStart, xEnd, yStart + height, solidColor, FastColor.ARGB32.color(0, 42, 42, 42));
                        int xCur = xStart;
                        for (int i = 0; i < 10; i++) {
                            pGuiGraphics.fill(xCur, yStart, xCur + 2, yStart + height, solidColor);
                            xCur += xStep;
                        }
                        xCur = xStart;
                        for (int i = 0; i < loops; i++) {
                            pGuiGraphics.renderItem(spells[i], xCur + xStep / 2 - 8, yStart + height / 2 - 8);
                            xCur += xStep;
                        }
                        xCur = xStart;
                        for (int i = 1; i < 10; i++) {
                            pGuiGraphics.drawString(this.mc.font, Component.literal(String.format("%d", i)), xCur + xStep / 2 + 8, yStart + height - 9, 16777215);
                            xCur += xStep;
                        }
                    }
                }
            }
        }
    }

    public RecipeRendererBase getPinnedrecipe() {
        return this.pinnedRecipe;
    }

    public ConstructDiagnosticsDisplay getConstructDisplay() {
        return this.pinnedDiagnostics;
    }
}