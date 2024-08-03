package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.atm;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.builtin.TeamBankReference;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyValueWidget;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.ATMScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.TeamSelectWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.TeamButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.menus.ATMMenu;
import io.github.lightman314.lightmanscurrency.common.menus.slots.SimpleSlot;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import io.github.lightman314.lightmanscurrency.common.teams.TeamSaveData;
import io.github.lightman314.lightmanscurrency.network.message.bank.CPacketBankTransferPlayer;
import io.github.lightman314.lightmanscurrency.network.message.bank.CPacketBankTransferTeam;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;

public class TransferTab extends ATMTab {

    public static final int RESPONSE_DURATION = 100;

    private int responseTimer = 0;

    MoneyValueWidget amountWidget;

    EditBox playerInput;

    TeamSelectWidget teamSelection;

    IconButton buttonToggleMode;

    EasyButton buttonTransfer;

    long selectedTeam = -1L;

    boolean playerMode = true;

    public TransferTab(ATMScreen screen) {
        super(screen);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_STORE_COINS;
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_ATM_TRANSFER.get();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        SimpleSlot.SetInactive(this.screen.m_6262_());
        this.responseTimer = 0;
        if (firstOpen) {
            ((ATMMenu) this.screen.m_6262_()).clearMessage();
        }
        this.amountWidget = this.addChild(new MoneyValueWidget(screenArea.pos, firstOpen ? null : this.amountWidget, MoneyValue.empty(), MoneyValueWidget.EMPTY_CONSUMER));
        this.amountWidget.allowFreeInput = false;
        this.amountWidget.drawBG = false;
        this.buttonToggleMode = this.addChild(new IconButton(screenArea.pos.offset(screenArea.width - 30, 64), this::ToggleMode, () -> this.playerMode ? IconData.of(Items.PLAYER_HEAD) : IconAndButtonUtil.ICON_ALEX_HEAD).withAddons(EasyAddonHelper.toggleTooltip(() -> this.playerMode, LCText.TOOLTIP_ATM_TRANSFER_MODE_TEAM.get(), LCText.TOOLTIP_ATM_TRANSFER_MODE_PLAYER.get())));
        this.playerInput = this.addChild(new EditBox(this.getFont(), screenArea.x + 10, screenArea.y + 104, screenArea.width - 20, 20, Component.empty()));
        this.playerInput.f_93624_ = this.playerMode;
        this.teamSelection = this.addChild(new TeamSelectWidget(screenArea.pos.offset(10, 84), 2, TeamButton.Size.NORMAL, this::getTeamList, this::selectedTeam, this::SelectTeam));
        this.teamSelection.f_93624_ = !this.playerMode;
        this.buttonTransfer = this.addChild(new EasyTextButton(screenArea.pos.offset(10, 126), screenArea.width - 20, 20, () -> this.playerMode ? LCText.BUTTON_ATM_TRANSFER_PLAYER.get() : LCText.BUTTON_ATM_TRANSFER_TEAM.get(), this::PressTransfer));
        this.buttonTransfer.f_93623_ = false;
    }

    private List<Team> getTeamList() {
        List<Team> results = Lists.newArrayList();
        BankReference source = ((ATMMenu) this.screen.m_6262_()).getBankAccountReference();
        Team blockTeam = null;
        if (source instanceof TeamBankReference teamBankReference) {
            blockTeam = TeamSaveData.GetTeam(true, teamBankReference.teamID);
        }
        for (Team team : TeamSaveData.GetAllTeams(true)) {
            if (team.hasBankAccount() && team != blockTeam) {
                results.add(team);
            }
        }
        return results;
    }

    public Team selectedTeam() {
        return this.selectedTeam >= 0L ? TeamSaveData.GetTeam(true, this.selectedTeam) : null;
    }

    public void SelectTeam(int teamIndex) {
        try {
            Team team = (Team) this.getTeamList().get(teamIndex);
            if (team.getID() == this.selectedTeam) {
                return;
            }
            this.selectedTeam = team.getID();
        } catch (Throwable var3) {
        }
    }

    private void PressTransfer(EasyButton button) {
        if (this.playerMode) {
            new CPacketBankTransferPlayer(this.playerInput.getValue(), this.amountWidget.getCurrentValue()).send();
            this.playerInput.setValue("");
            this.amountWidget.changeValue(MoneyValue.empty());
        } else if (this.selectedTeam >= 0L) {
            new CPacketBankTransferTeam(this.selectedTeam, this.amountWidget.getCurrentValue()).send();
            this.amountWidget.changeValue(MoneyValue.empty());
        }
    }

    private void ToggleMode(EasyButton button) {
        this.playerMode = !this.playerMode;
        this.teamSelection.f_93624_ = !this.playerMode;
        this.playerInput.f_93624_ = this.playerMode;
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        this.hideCoinSlots(gui);
        Component balance = (Component) (((ATMMenu) this.screen.m_6262_()).getBankAccount() == null ? LCText.GUI_BANK_NO_SELECTED_ACCOUNT.get() : ((ATMMenu) this.screen.m_6262_()).getBankAccount().getBalanceText());
        gui.drawString(balance, 8, 72, 4210752);
        if (this.hasMessage()) {
            TextRenderUtil.drawCenteredMultilineText(gui, this.getMessage(), 2, this.screen.getXSize() - 4, 5, 4210752);
            this.amountWidget.f_93624_ = false;
        } else {
            this.amountWidget.f_93624_ = true;
        }
    }

    @Override
    public void tick() {
        if (this.playerMode) {
            this.buttonTransfer.f_93623_ = !this.playerInput.getValue().isBlank() && !this.amountWidget.getCurrentValue().isEmpty();
        } else {
            Team team = this.selectedTeam();
            this.buttonTransfer.f_93623_ = team != null && team.hasBankAccount() && !this.amountWidget.getCurrentValue().isEmpty();
        }
        if (this.hasMessage()) {
            this.responseTimer++;
            if (this.responseTimer >= 100) {
                this.responseTimer = 0;
                ((ATMMenu) this.screen.m_6262_()).clearMessage();
            }
        }
    }

    private boolean hasMessage() {
        return ((ATMMenu) this.screen.m_6262_()).hasTransferMessage();
    }

    private MutableComponent getMessage() {
        return ((ATMMenu) this.screen.m_6262_()).getTransferMessage();
    }

    @Override
    public void closeAction() {
        SimpleSlot.SetActive(this.screen.m_6262_());
        this.responseTimer = 0;
        ((ATMMenu) this.screen.m_6262_()).clearMessage();
    }

    @Override
    public boolean blockInventoryClosing() {
        return this.playerMode;
    }
}