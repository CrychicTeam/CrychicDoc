package com.mojang.realmsclient.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import com.mojang.realmsclient.util.RealmsTextureManager;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RealmsWorldSlotButton extends Button {

    public static final ResourceLocation SLOT_FRAME_LOCATION = new ResourceLocation("realms", "textures/gui/realms/slot_frame.png");

    public static final ResourceLocation EMPTY_SLOT_LOCATION = new ResourceLocation("realms", "textures/gui/realms/empty_frame.png");

    public static final ResourceLocation CHECK_MARK_LOCATION = new ResourceLocation("minecraft", "textures/gui/checkmark.png");

    public static final ResourceLocation DEFAULT_WORLD_SLOT_1 = new ResourceLocation("minecraft", "textures/gui/title/background/panorama_0.png");

    public static final ResourceLocation DEFAULT_WORLD_SLOT_2 = new ResourceLocation("minecraft", "textures/gui/title/background/panorama_2.png");

    public static final ResourceLocation DEFAULT_WORLD_SLOT_3 = new ResourceLocation("minecraft", "textures/gui/title/background/panorama_3.png");

    private static final Component SLOT_ACTIVE_TOOLTIP = Component.translatable("mco.configure.world.slot.tooltip.active");

    private static final Component SWITCH_TO_MINIGAME_SLOT_TOOLTIP = Component.translatable("mco.configure.world.slot.tooltip.minigame");

    private static final Component SWITCH_TO_WORLD_SLOT_TOOLTIP = Component.translatable("mco.configure.world.slot.tooltip");

    private static final Component MINIGAME = Component.translatable("mco.worldSlot.minigame");

    private final Supplier<RealmsServer> serverDataProvider;

    private final Consumer<Component> toolTipSetter;

    private final int slotIndex;

    @Nullable
    private RealmsWorldSlotButton.State state;

    public RealmsWorldSlotButton(int int0, int int1, int int2, int int3, Supplier<RealmsServer> supplierRealmsServer4, Consumer<Component> consumerComponent5, int int6, Button.OnPress buttonOnPress7) {
        super(int0, int1, int2, int3, CommonComponents.EMPTY, buttonOnPress7, f_252438_);
        this.serverDataProvider = supplierRealmsServer4;
        this.slotIndex = int6;
        this.toolTipSetter = consumerComponent5;
    }

    @Nullable
    public RealmsWorldSlotButton.State getState() {
        return this.state;
    }

    public void tick() {
        RealmsServer $$0 = (RealmsServer) this.serverDataProvider.get();
        if ($$0 != null) {
            RealmsWorldOptions $$1 = (RealmsWorldOptions) $$0.slots.get(this.slotIndex);
            boolean $$2 = this.slotIndex == 4;
            boolean $$3;
            String $$4;
            long $$5;
            String $$6;
            boolean $$7;
            if ($$2) {
                $$3 = $$0.worldType == RealmsServer.WorldType.MINIGAME;
                $$4 = MINIGAME.getString();
                $$5 = (long) $$0.minigameId;
                $$6 = $$0.minigameImage;
                $$7 = $$0.minigameId == -1;
            } else {
                $$3 = $$0.activeSlot == this.slotIndex && $$0.worldType != RealmsServer.WorldType.MINIGAME;
                $$4 = $$1.getSlotName(this.slotIndex);
                $$5 = $$1.templateId;
                $$6 = $$1.templateImage;
                $$7 = $$1.empty;
            }
            RealmsWorldSlotButton.Action $$13 = getAction($$0, $$3, $$2);
            Pair<Component, Component> $$14 = this.getTooltipAndNarration($$0, $$4, $$7, $$2, $$13);
            this.state = new RealmsWorldSlotButton.State($$3, $$4, $$5, $$6, $$7, $$2, $$13, (Component) $$14.getFirst());
            this.m_93666_((Component) $$14.getSecond());
        }
    }

    private static RealmsWorldSlotButton.Action getAction(RealmsServer realmsServer0, boolean boolean1, boolean boolean2) {
        if (boolean1) {
            if (!realmsServer0.expired && realmsServer0.state != RealmsServer.State.UNINITIALIZED) {
                return RealmsWorldSlotButton.Action.JOIN;
            }
        } else {
            if (!boolean2) {
                return RealmsWorldSlotButton.Action.SWITCH_SLOT;
            }
            if (!realmsServer0.expired) {
                return RealmsWorldSlotButton.Action.SWITCH_SLOT;
            }
        }
        return RealmsWorldSlotButton.Action.NOTHING;
    }

    private Pair<Component, Component> getTooltipAndNarration(RealmsServer realmsServer0, String string1, boolean boolean2, boolean boolean3, RealmsWorldSlotButton.Action realmsWorldSlotButtonAction4) {
        if (realmsWorldSlotButtonAction4 == RealmsWorldSlotButton.Action.NOTHING) {
            return Pair.of(null, Component.literal(string1));
        } else {
            Component $$5;
            if (boolean3) {
                if (boolean2) {
                    $$5 = CommonComponents.EMPTY;
                } else {
                    $$5 = CommonComponents.space().append(string1).append(CommonComponents.SPACE).append(realmsServer0.minigameName);
                }
            } else {
                $$5 = CommonComponents.space().append(string1);
            }
            Component $$8;
            if (realmsWorldSlotButtonAction4 == RealmsWorldSlotButton.Action.JOIN) {
                $$8 = SLOT_ACTIVE_TOOLTIP;
            } else {
                $$8 = boolean3 ? SWITCH_TO_MINIGAME_SLOT_TOOLTIP : SWITCH_TO_WORLD_SLOT_TOOLTIP;
            }
            Component $$10 = $$8.copy().append($$5);
            return Pair.of($$8, $$10);
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.state != null) {
            this.drawSlotFrame(guiGraphics0, this.m_252754_(), this.m_252907_(), int1, int2, this.state.isCurrentlyActiveSlot, this.state.slotName, this.slotIndex, this.state.imageId, this.state.image, this.state.empty, this.state.minigame, this.state.action, this.state.actionPrompt);
        }
    }

    private void drawSlotFrame(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, boolean boolean5, String string6, int int7, long long8, @Nullable String string9, boolean boolean10, boolean boolean11, RealmsWorldSlotButton.Action realmsWorldSlotButtonAction12, @Nullable Component component13) {
        boolean $$14 = this.m_198029_();
        if (this.m_5953_((double) int3, (double) int4) && component13 != null) {
            this.toolTipSetter.accept(component13);
        }
        Minecraft $$15 = Minecraft.getInstance();
        ResourceLocation $$16;
        if (boolean11) {
            $$16 = RealmsTextureManager.worldTemplate(String.valueOf(long8), string9);
        } else if (boolean10) {
            $$16 = EMPTY_SLOT_LOCATION;
        } else if (string9 != null && long8 != -1L) {
            $$16 = RealmsTextureManager.worldTemplate(String.valueOf(long8), string9);
        } else if (int7 == 1) {
            $$16 = DEFAULT_WORLD_SLOT_1;
        } else if (int7 == 2) {
            $$16 = DEFAULT_WORLD_SLOT_2;
        } else if (int7 == 3) {
            $$16 = DEFAULT_WORLD_SLOT_3;
        } else {
            $$16 = EMPTY_SLOT_LOCATION;
        }
        if (boolean5) {
            guiGraphics0.setColor(0.56F, 0.56F, 0.56F, 1.0F);
        }
        guiGraphics0.blit($$16, int1 + 3, int2 + 3, 0.0F, 0.0F, 74, 74, 74, 74);
        boolean $$23 = $$14 && realmsWorldSlotButtonAction12 != RealmsWorldSlotButton.Action.NOTHING;
        if ($$23) {
            guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        } else if (boolean5) {
            guiGraphics0.setColor(0.8F, 0.8F, 0.8F, 1.0F);
        } else {
            guiGraphics0.setColor(0.56F, 0.56F, 0.56F, 1.0F);
        }
        guiGraphics0.blit(SLOT_FRAME_LOCATION, int1, int2, 0.0F, 0.0F, 80, 80, 80, 80);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (boolean5) {
            this.renderCheckMark(guiGraphics0, int1, int2);
        }
        guiGraphics0.drawCenteredString($$15.font, string6, int1 + 40, int2 + 66, 16777215);
    }

    private void renderCheckMark(GuiGraphics guiGraphics0, int int1, int int2) {
        RenderSystem.enableBlend();
        guiGraphics0.blit(CHECK_MARK_LOCATION, int1 + 67, int2 + 4, 0.0F, 0.0F, 9, 8, 9, 8);
        RenderSystem.disableBlend();
    }

    public static enum Action {

        NOTHING, SWITCH_SLOT, JOIN
    }

    public static class State {

        final boolean isCurrentlyActiveSlot;

        final String slotName;

        final long imageId;

        @Nullable
        final String image;

        public final boolean empty;

        public final boolean minigame;

        public final RealmsWorldSlotButton.Action action;

        @Nullable
        final Component actionPrompt;

        State(boolean boolean0, String string1, long long2, @Nullable String string3, boolean boolean4, boolean boolean5, RealmsWorldSlotButton.Action realmsWorldSlotButtonAction6, @Nullable Component component7) {
            this.isCurrentlyActiveSlot = boolean0;
            this.slotName = string1;
            this.imageId = long2;
            this.image = string3;
            this.empty = boolean4;
            this.minigame = boolean5;
            this.action = realmsWorldSlotButtonAction6;
            this.actionPrompt = component7;
        }
    }
}