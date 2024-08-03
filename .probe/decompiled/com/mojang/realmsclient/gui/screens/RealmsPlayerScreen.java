package com.mojang.realmsclient.gui.screens;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.Ops;
import com.mojang.realmsclient.dto.PlayerInfo;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsObjectSelectionList;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class RealmsPlayerScreen extends RealmsScreen {

    private static final Logger LOGGER = LogUtils.getLogger();

    static final ResourceLocation OP_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/op_icon.png");

    static final ResourceLocation USER_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/user_icon.png");

    static final ResourceLocation CROSS_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/cross_player_icon.png");

    private static final ResourceLocation OPTIONS_BACKGROUND = new ResourceLocation("minecraft", "textures/gui/options_background.png");

    private static final Component QUESTION_TITLE = Component.translatable("mco.question");

    static final Component NORMAL_USER_TOOLTIP = Component.translatable("mco.configure.world.invites.normal.tooltip");

    static final Component OP_TOOLTIP = Component.translatable("mco.configure.world.invites.ops.tooltip");

    static final Component REMOVE_ENTRY_TOOLTIP = Component.translatable("mco.configure.world.invites.remove.tooltip");

    private static final int NO_ENTRY_SELECTED = -1;

    private final RealmsConfigureWorldScreen lastScreen;

    final RealmsServer serverData;

    RealmsPlayerScreen.InvitedObjectSelectionList invitedObjectSelectionList;

    int column1X;

    int columnWidth;

    private Button removeButton;

    private Button opdeopButton;

    int playerIndex = -1;

    private boolean stateChanged;

    public RealmsPlayerScreen(RealmsConfigureWorldScreen realmsConfigureWorldScreen0, RealmsServer realmsServer1) {
        super(Component.translatable("mco.configure.world.players.title"));
        this.lastScreen = realmsConfigureWorldScreen0;
        this.serverData = realmsServer1;
    }

    @Override
    public void init() {
        this.column1X = this.f_96543_ / 2 - 160;
        this.columnWidth = 150;
        int $$0 = this.f_96543_ / 2 + 12;
        this.invitedObjectSelectionList = new RealmsPlayerScreen.InvitedObjectSelectionList();
        this.invitedObjectSelectionList.m_93507_(this.column1X);
        this.m_7787_(this.invitedObjectSelectionList);
        for (PlayerInfo $$1 : this.serverData.players) {
            this.invitedObjectSelectionList.addEntry($$1);
        }
        this.playerIndex = -1;
        this.m_142416_(Button.builder(Component.translatable("mco.configure.world.buttons.invite"), p_280732_ -> this.f_96541_.setScreen(new RealmsInviteScreen(this.lastScreen, this, this.serverData))).bounds($$0, m_120774_(1), this.columnWidth + 10, 20).build());
        this.removeButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.configure.world.invites.remove.tooltip"), p_278866_ -> this.uninvite(this.playerIndex)).bounds($$0, m_120774_(7), this.columnWidth + 10, 20).build());
        this.opdeopButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.configure.world.invites.ops.tooltip"), p_278869_ -> {
            if (((PlayerInfo) this.serverData.players.get(this.playerIndex)).isOperator()) {
                this.deop(this.playerIndex);
            } else {
                this.op(this.playerIndex);
            }
        }).bounds($$0, m_120774_(9), this.columnWidth + 10, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_89122_ -> this.backButtonClicked()).bounds($$0 + this.columnWidth / 2 + 2, m_120774_(12), this.columnWidth / 2 + 10 - 2, 20).build());
        this.updateButtonStates();
    }

    void updateButtonStates() {
        this.removeButton.f_93624_ = this.shouldRemoveAndOpdeopButtonBeVisible(this.playerIndex);
        this.opdeopButton.f_93624_ = this.shouldRemoveAndOpdeopButtonBeVisible(this.playerIndex);
        this.invitedObjectSelectionList.updateButtons();
    }

    private boolean shouldRemoveAndOpdeopButtonBeVisible(int int0) {
        return int0 != -1;
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.backButtonClicked();
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    private void backButtonClicked() {
        if (this.stateChanged) {
            this.f_96541_.setScreen(this.lastScreen.getNewScreen());
        } else {
            this.f_96541_.setScreen(this.lastScreen);
        }
    }

    void op(int int0) {
        RealmsClient $$1 = RealmsClient.create();
        String $$2 = ((PlayerInfo) this.serverData.players.get(int0)).getUuid();
        try {
            this.updateOps($$1.op(this.serverData.id, $$2));
        } catch (RealmsServiceException var5) {
            LOGGER.error("Couldn't op the user");
        }
        this.updateButtonStates();
    }

    void deop(int int0) {
        RealmsClient $$1 = RealmsClient.create();
        String $$2 = ((PlayerInfo) this.serverData.players.get(int0)).getUuid();
        try {
            this.updateOps($$1.deop(this.serverData.id, $$2));
        } catch (RealmsServiceException var5) {
            LOGGER.error("Couldn't deop the user");
        }
        this.updateButtonStates();
    }

    private void updateOps(Ops ops0) {
        for (PlayerInfo $$1 : this.serverData.players) {
            $$1.setOperator(ops0.ops.contains($$1.getName()));
        }
    }

    void uninvite(int int0) {
        this.updateButtonStates();
        if (int0 >= 0 && int0 < this.serverData.players.size()) {
            PlayerInfo $$1 = (PlayerInfo) this.serverData.players.get(int0);
            RealmsConfirmScreen $$2 = new RealmsConfirmScreen(p_278868_ -> {
                if (p_278868_) {
                    RealmsClient $$2x = RealmsClient.create();
                    try {
                        $$2x.uninvite(this.serverData.id, $$1.getUuid());
                    } catch (RealmsServiceException var5) {
                        LOGGER.error("Couldn't uninvite user");
                    }
                    this.serverData.players.remove(this.playerIndex);
                    this.playerIndex = -1;
                    this.updateButtonStates();
                }
                this.stateChanged = true;
                this.f_96541_.setScreen(this);
            }, QUESTION_TITLE, Component.translatable("mco.configure.world.uninvite.player", $$1.getName()));
            this.f_96541_.setScreen($$2);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.invitedObjectSelectionList.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 17, 16777215);
        int $$4 = m_120774_(12) + 20;
        guiGraphics0.setColor(0.25F, 0.25F, 0.25F, 1.0F);
        guiGraphics0.blit(OPTIONS_BACKGROUND, 0, $$4, 0.0F, 0.0F, this.f_96543_, this.f_96544_ - $$4, 32, 32);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        String $$5 = this.serverData.players != null ? Integer.toString(this.serverData.players.size()) : "0";
        guiGraphics0.drawString(this.f_96547_, Component.translatable("mco.configure.world.invited.number", $$5), this.column1X, m_120774_(0), 10526880, false);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    class Entry extends ObjectSelectionList.Entry<RealmsPlayerScreen.Entry> {

        private static final int X_OFFSET = 3;

        private static final int Y_PADDING = 1;

        private static final int BUTTON_WIDTH = 8;

        private static final int BUTTON_HEIGHT = 7;

        private final PlayerInfo playerInfo;

        private final List<AbstractWidget> children = new ArrayList();

        private final ImageButton removeButton;

        private final ImageButton makeOpButton;

        private final ImageButton removeOpButton;

        public Entry(PlayerInfo playerInfo0) {
            this.playerInfo = playerInfo0;
            int $$1 = RealmsPlayerScreen.this.serverData.players.indexOf(this.playerInfo);
            int $$2 = RealmsPlayerScreen.this.invitedObjectSelectionList.m_93520_() - 16 - 9;
            int $$3 = RealmsPlayerScreen.this.invitedObjectSelectionList.m_7610_($$1) + 1;
            this.removeButton = new ImageButton($$2, $$3, 8, 7, 0, 0, 7, RealmsPlayerScreen.CROSS_ICON_LOCATION, 8, 14, p_279099_ -> RealmsPlayerScreen.this.uninvite($$1));
            this.removeButton.m_257544_(Tooltip.create(RealmsPlayerScreen.REMOVE_ENTRY_TOOLTIP));
            this.children.add(this.removeButton);
            $$2 += 11;
            this.makeOpButton = new ImageButton($$2, $$3, 8, 7, 0, 0, 7, RealmsPlayerScreen.USER_ICON_LOCATION, 8, 14, p_279435_ -> RealmsPlayerScreen.this.op($$1));
            this.makeOpButton.m_257544_(Tooltip.create(RealmsPlayerScreen.NORMAL_USER_TOOLTIP));
            this.children.add(this.makeOpButton);
            this.removeOpButton = new ImageButton($$2, $$3, 8, 7, 0, 0, 7, RealmsPlayerScreen.OP_ICON_LOCATION, 8, 14, p_279383_ -> RealmsPlayerScreen.this.deop($$1));
            this.removeOpButton.m_257544_(Tooltip.create(RealmsPlayerScreen.OP_TOOLTIP));
            this.children.add(this.removeOpButton);
            this.updateButtons();
        }

        public void updateButtons() {
            this.makeOpButton.f_93624_ = !this.playerInfo.isOperator();
            this.removeOpButton.f_93624_ = !this.makeOpButton.f_93624_;
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            if (!this.makeOpButton.m_6375_(double0, double1, int2)) {
                this.removeOpButton.m_6375_(double0, double1, int2);
            }
            this.removeButton.m_6375_(double0, double1, int2);
            return true;
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            int $$10;
            if (!this.playerInfo.getAccepted()) {
                $$10 = 10526880;
            } else if (this.playerInfo.getOnline()) {
                $$10 = 8388479;
            } else {
                $$10 = 16777215;
            }
            RealmsUtil.renderPlayerFace(guiGraphics0, RealmsPlayerScreen.this.column1X + 2 + 2, int2 + 1, 8, this.playerInfo.getUuid());
            guiGraphics0.drawString(RealmsPlayerScreen.this.f_96547_, this.playerInfo.getName(), RealmsPlayerScreen.this.column1X + 3 + 12, int2 + 1, $$10, false);
            this.children.forEach(p_280738_ -> {
                p_280738_.setY(int2 + 1);
                p_280738_.render(guiGraphics0, int6, int7, float9);
            });
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", this.playerInfo.getName());
        }
    }

    class InvitedObjectSelectionList extends RealmsObjectSelectionList<RealmsPlayerScreen.Entry> {

        public InvitedObjectSelectionList() {
            super(RealmsPlayerScreen.this.columnWidth + 10, RealmsPlayerScreen.m_120774_(12) + 20, RealmsPlayerScreen.m_120774_(1), RealmsPlayerScreen.m_120774_(12) + 20, 13);
        }

        public void updateButtons() {
            if (RealmsPlayerScreen.this.playerIndex != -1) {
                ((RealmsPlayerScreen.Entry) this.m_93500_(RealmsPlayerScreen.this.playerIndex)).updateButtons();
            }
        }

        public void addEntry(PlayerInfo playerInfo0) {
            this.m_7085_(RealmsPlayerScreen.this.new Entry(playerInfo0));
        }

        @Override
        public int getRowWidth() {
            return (int) ((double) this.f_93388_ * 1.0);
        }

        @Override
        public void selectItem(int int0) {
            super.selectItem(int0);
            this.selectInviteListItem(int0);
        }

        public void selectInviteListItem(int int0) {
            RealmsPlayerScreen.this.playerIndex = int0;
            RealmsPlayerScreen.this.updateButtonStates();
        }

        public void setSelected(@Nullable RealmsPlayerScreen.Entry realmsPlayerScreenEntry0) {
            super.m_6987_(realmsPlayerScreenEntry0);
            RealmsPlayerScreen.this.playerIndex = this.m_6702_().indexOf(realmsPlayerScreenEntry0);
            RealmsPlayerScreen.this.updateButtonStates();
        }

        @Override
        public void renderBackground(GuiGraphics guiGraphics0) {
            RealmsPlayerScreen.this.m_280273_(guiGraphics0);
        }

        @Override
        public int getScrollbarPosition() {
            return RealmsPlayerScreen.this.column1X + this.f_93388_ - 5;
        }

        @Override
        public int getMaxPosition() {
            return this.m_5773_() * 13;
        }
    }
}