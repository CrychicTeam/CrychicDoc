package com.mojang.realmsclient.gui.screens;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.PendingInvite;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RowButton;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsObjectSelectionList;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class RealmsPendingInvitesScreen extends RealmsScreen {

    static final Logger LOGGER = LogUtils.getLogger();

    static final ResourceLocation ACCEPT_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/accept_icon.png");

    static final ResourceLocation REJECT_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/reject_icon.png");

    private static final Component NO_PENDING_INVITES_TEXT = Component.translatable("mco.invites.nopending");

    static final Component ACCEPT_INVITE_TOOLTIP = Component.translatable("mco.invites.button.accept");

    static final Component REJECT_INVITE_TOOLTIP = Component.translatable("mco.invites.button.reject");

    private final Screen lastScreen;

    @Nullable
    Component toolTip;

    boolean loaded;

    RealmsPendingInvitesScreen.PendingInvitationSelectionList pendingInvitationSelectionList;

    int selectedInvite = -1;

    private Button acceptButton;

    private Button rejectButton;

    public RealmsPendingInvitesScreen(Screen screen0, Component component1) {
        super(component1);
        this.lastScreen = screen0;
    }

    @Override
    public void init() {
        this.pendingInvitationSelectionList = new RealmsPendingInvitesScreen.PendingInvitationSelectionList();
        (new Thread("Realms-pending-invitations-fetcher") {

            public void run() {
                RealmsClient $$0 = RealmsClient.create();
                try {
                    List<PendingInvite> $$1 = $$0.pendingInvites().pendingInvites;
                    List<RealmsPendingInvitesScreen.Entry> $$2 = (List<RealmsPendingInvitesScreen.Entry>) $$1.stream().map(p_88969_ -> RealmsPendingInvitesScreen.this.new Entry(p_88969_)).collect(Collectors.toList());
                    RealmsPendingInvitesScreen.this.f_96541_.execute(() -> RealmsPendingInvitesScreen.this.pendingInvitationSelectionList.m_5988_($$2));
                } catch (RealmsServiceException var7) {
                    RealmsPendingInvitesScreen.LOGGER.error("Couldn't list invites");
                } finally {
                    RealmsPendingInvitesScreen.this.loaded = true;
                }
            }
        }).start();
        this.m_7787_(this.pendingInvitationSelectionList);
        this.acceptButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.invites.button.accept"), p_88940_ -> {
            this.accept(this.selectedInvite);
            this.selectedInvite = -1;
            this.updateButtonStates();
        }).bounds(this.f_96543_ / 2 - 174, this.f_96544_ - 32, 100, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280731_ -> this.f_96541_.setScreen(new RealmsMainScreen(this.lastScreen))).bounds(this.f_96543_ / 2 - 50, this.f_96544_ - 32, 100, 20).build());
        this.rejectButton = (Button) this.m_142416_(Button.builder(Component.translatable("mco.invites.button.reject"), p_88920_ -> {
            this.reject(this.selectedInvite);
            this.selectedInvite = -1;
            this.updateButtonStates();
        }).bounds(this.f_96543_ / 2 + 74, this.f_96544_ - 32, 100, 20).build());
        this.updateButtonStates();
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.f_96541_.setScreen(new RealmsMainScreen(this.lastScreen));
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    void updateList(int int0) {
        this.pendingInvitationSelectionList.removeAtIndex(int0);
    }

    void reject(final int int0) {
        if (int0 < this.pendingInvitationSelectionList.m_5773_()) {
            (new Thread("Realms-reject-invitation") {

                public void run() {
                    try {
                        RealmsClient $$0 = RealmsClient.create();
                        $$0.rejectInvitation(((RealmsPendingInvitesScreen.Entry) RealmsPendingInvitesScreen.this.pendingInvitationSelectionList.m_6702_().get(int0)).pendingInvite.invitationId);
                        RealmsPendingInvitesScreen.this.f_96541_.execute(() -> RealmsPendingInvitesScreen.this.updateList(int0));
                    } catch (RealmsServiceException var2) {
                        RealmsPendingInvitesScreen.LOGGER.error("Couldn't reject invite");
                    }
                }
            }).start();
        }
    }

    void accept(final int int0) {
        if (int0 < this.pendingInvitationSelectionList.m_5773_()) {
            (new Thread("Realms-accept-invitation") {

                public void run() {
                    try {
                        RealmsClient $$0 = RealmsClient.create();
                        $$0.acceptInvitation(((RealmsPendingInvitesScreen.Entry) RealmsPendingInvitesScreen.this.pendingInvitationSelectionList.m_6702_().get(int0)).pendingInvite.invitationId);
                        RealmsPendingInvitesScreen.this.f_96541_.execute(() -> RealmsPendingInvitesScreen.this.updateList(int0));
                    } catch (RealmsServiceException var2) {
                        RealmsPendingInvitesScreen.LOGGER.error("Couldn't accept invite");
                    }
                }
            }).start();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.toolTip = null;
        this.m_280273_(guiGraphics0);
        this.pendingInvitationSelectionList.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 12, 16777215);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(guiGraphics0, this.toolTip, int1, int2);
        }
        if (this.pendingInvitationSelectionList.m_5773_() == 0 && this.loaded) {
            guiGraphics0.drawCenteredString(this.f_96547_, NO_PENDING_INVITES_TEXT, this.f_96543_ / 2, this.f_96544_ / 2 - 20, 16777215);
        }
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    protected void renderMousehoverTooltip(GuiGraphics guiGraphics0, @Nullable Component component1, int int2, int int3) {
        if (component1 != null) {
            int $$4 = int2 + 12;
            int $$5 = int3 - 12;
            int $$6 = this.f_96547_.width(component1);
            guiGraphics0.fillGradient($$4 - 3, $$5 - 3, $$4 + $$6 + 3, $$5 + 8 + 3, -1073741824, -1073741824);
            guiGraphics0.drawString(this.f_96547_, component1, $$4, $$5, 16777215);
        }
    }

    void updateButtonStates() {
        this.acceptButton.f_93624_ = this.shouldAcceptAndRejectButtonBeVisible(this.selectedInvite);
        this.rejectButton.f_93624_ = this.shouldAcceptAndRejectButtonBeVisible(this.selectedInvite);
    }

    private boolean shouldAcceptAndRejectButtonBeVisible(int int0) {
        return int0 != -1;
    }

    class Entry extends ObjectSelectionList.Entry<RealmsPendingInvitesScreen.Entry> {

        private static final int TEXT_LEFT = 38;

        final PendingInvite pendingInvite;

        private final List<RowButton> rowButtons;

        Entry(PendingInvite pendingInvite0) {
            this.pendingInvite = pendingInvite0;
            this.rowButtons = Arrays.asList(new RealmsPendingInvitesScreen.Entry.AcceptRowButton(), new RealmsPendingInvitesScreen.Entry.RejectRowButton());
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            this.renderPendingInvitationItem(guiGraphics0, this.pendingInvite, int3, int2, int6, int7);
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            RowButton.rowButtonMouseClicked(RealmsPendingInvitesScreen.this.pendingInvitationSelectionList, this, this.rowButtons, int2, double0, double1);
            return true;
        }

        private void renderPendingInvitationItem(GuiGraphics guiGraphics0, PendingInvite pendingInvite1, int int2, int int3, int int4, int int5) {
            guiGraphics0.drawString(RealmsPendingInvitesScreen.this.f_96547_, pendingInvite1.worldName, int2 + 38, int3 + 1, 16777215, false);
            guiGraphics0.drawString(RealmsPendingInvitesScreen.this.f_96547_, pendingInvite1.worldOwnerName, int2 + 38, int3 + 12, 7105644, false);
            guiGraphics0.drawString(RealmsPendingInvitesScreen.this.f_96547_, RealmsUtil.convertToAgePresentationFromInstant(pendingInvite1.date), int2 + 38, int3 + 24, 7105644, false);
            RowButton.drawButtonsInRow(guiGraphics0, this.rowButtons, RealmsPendingInvitesScreen.this.pendingInvitationSelectionList, int2, int3, int4, int5);
            RealmsUtil.renderPlayerFace(guiGraphics0, int2, int3, 32, pendingInvite1.worldOwnerUuid);
        }

        @Override
        public Component getNarration() {
            Component $$0 = CommonComponents.joinLines(Component.literal(this.pendingInvite.worldName), Component.literal(this.pendingInvite.worldOwnerName), RealmsUtil.convertToAgePresentationFromInstant(this.pendingInvite.date));
            return Component.translatable("narrator.select", $$0);
        }

        class AcceptRowButton extends RowButton {

            AcceptRowButton() {
                super(15, 15, 215, 5);
            }

            @Override
            protected void draw(GuiGraphics guiGraphics0, int int1, int int2, boolean boolean3) {
                float $$4 = boolean3 ? 19.0F : 0.0F;
                guiGraphics0.blit(RealmsPendingInvitesScreen.ACCEPT_ICON_LOCATION, int1, int2, $$4, 0.0F, 18, 18, 37, 18);
                if (boolean3) {
                    RealmsPendingInvitesScreen.this.toolTip = RealmsPendingInvitesScreen.ACCEPT_INVITE_TOOLTIP;
                }
            }

            @Override
            public void onClick(int int0) {
                RealmsPendingInvitesScreen.this.accept(int0);
            }
        }

        class RejectRowButton extends RowButton {

            RejectRowButton() {
                super(15, 15, 235, 5);
            }

            @Override
            protected void draw(GuiGraphics guiGraphics0, int int1, int int2, boolean boolean3) {
                float $$4 = boolean3 ? 19.0F : 0.0F;
                guiGraphics0.blit(RealmsPendingInvitesScreen.REJECT_ICON_LOCATION, int1, int2, $$4, 0.0F, 18, 18, 37, 18);
                if (boolean3) {
                    RealmsPendingInvitesScreen.this.toolTip = RealmsPendingInvitesScreen.REJECT_INVITE_TOOLTIP;
                }
            }

            @Override
            public void onClick(int int0) {
                RealmsPendingInvitesScreen.this.reject(int0);
            }
        }
    }

    class PendingInvitationSelectionList extends RealmsObjectSelectionList<RealmsPendingInvitesScreen.Entry> {

        public PendingInvitationSelectionList() {
            super(RealmsPendingInvitesScreen.this.f_96543_, RealmsPendingInvitesScreen.this.f_96544_, 32, RealmsPendingInvitesScreen.this.f_96544_ - 40, 36);
        }

        public void removeAtIndex(int int0) {
            this.m_93514_(int0);
        }

        @Override
        public int getMaxPosition() {
            return this.m_5773_() * 36;
        }

        @Override
        public int getRowWidth() {
            return 260;
        }

        @Override
        public void renderBackground(GuiGraphics guiGraphics0) {
            RealmsPendingInvitesScreen.this.m_280273_(guiGraphics0);
        }

        @Override
        public void selectItem(int int0) {
            super.selectItem(int0);
            this.selectInviteListItem(int0);
        }

        public void selectInviteListItem(int int0) {
            RealmsPendingInvitesScreen.this.selectedInvite = int0;
            RealmsPendingInvitesScreen.this.updateButtonStates();
        }

        public void setSelected(@Nullable RealmsPendingInvitesScreen.Entry realmsPendingInvitesScreenEntry0) {
            super.m_6987_(realmsPendingInvitesScreenEntry0);
            RealmsPendingInvitesScreen.this.selectedInvite = this.m_6702_().indexOf(realmsPendingInvitesScreenEntry0);
            RealmsPendingInvitesScreen.this.updateButtonStates();
        }
    }
}