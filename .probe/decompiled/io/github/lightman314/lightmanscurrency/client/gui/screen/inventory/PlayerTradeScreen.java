package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyValueWidget;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyMenuScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollListener;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.PlayerTradeMenu;
import io.github.lightman314.lightmanscurrency.network.message.playertrading.CPacketPlayerTradeInteraction;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

public class PlayerTradeScreen extends EasyMenuScreen<PlayerTradeMenu> implements IScrollable {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/player_trading.png");

    public static final ResourceLocation GUI_CHAT_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/container/player_trading_chat.png");

    private int scroll = 0;

    private static final int CHAT_ROWS = 11;

    private static final int CHAT_SIZE = 10;

    private MoneyValueWidget valueInput;

    private EasyButton buttonPropose;

    private EasyButton buttonAccept;

    private IconButton buttonToggleChat;

    private boolean chatWarning = false;

    private EditBox chatBox;

    private boolean chatMode = false;

    private ScrollListener chatScrollListener;

    private final List<FormattedCharSequence> chatHistory = new ArrayList();

    private void setShaderColorForState(@Nonnull EasyGuiGraphics gui, int state) {
        switch(state) {
            case 1:
                gui.setColor(0.0F, 1.0F, 1.0F);
                break;
            case 2:
                gui.setColor(0.0F, 1.0F, 0.0F);
                break;
            default:
                gui.setColor(0.54509807F, 0.54509807F, 0.54509807F);
        }
    }

    public PlayerTradeScreen(PlayerTradeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.resize(176, 291);
        ((PlayerTradeMenu) this.f_97732_).setChatReceiver(this::receiveChat);
    }

    @Override
    protected void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.resetColor();
        if (this.chatMode) {
            gui.blit(GUI_CHAT_TEXTURE, 0, 69, 0, 0, this.getXSize(), this.getYSize() - 69);
            int yPos = 175;
            for (int i = this.scroll; i < 11 + this.scroll && i < this.chatHistory.size(); i++) {
                gui.drawString((FormattedCharSequence) this.chatHistory.get(i), 7, yPos, 4210752);
                yPos -= 10;
            }
        } else {
            gui.blit(GUI_TEXTURE, 0, 69, 0, 0, this.getXSize(), this.getYSize() - 69);
            this.setShaderColorForState(gui, ((PlayerTradeMenu) this.f_97732_).myState());
            gui.blit(GUI_TEXTURE, 77, 119, this.getXSize(), 0, 22, 15);
            this.setShaderColorForState(gui, ((PlayerTradeMenu) this.f_97732_).otherState());
            gui.blit(GUI_TEXTURE, 77, 134, this.getXSize(), 15, 22, 15);
            gui.resetColor();
            Component leftName = ((PlayerTradeMenu) this.f_97732_).isHost() ? ((PlayerTradeMenu) this.f_97732_).getTradeData().getHostName() : ((PlayerTradeMenu) this.f_97732_).getTradeData().getGuestName();
            Component rightName = ((PlayerTradeMenu) this.f_97732_).isHost() ? ((PlayerTradeMenu) this.f_97732_).getTradeData().getGuestName() : ((PlayerTradeMenu) this.f_97732_).getTradeData().getHostName();
            gui.drawString(leftName, 8, 75, 4210752);
            gui.drawString(rightName, this.getXSize() - 8 - this.f_96547_.width(rightName), 75, 4210752);
            Component var5 = ((PlayerTradeMenu) this.f_97732_).isHost() ? ((PlayerTradeMenu) this.f_97732_).getTradeData().getHostMoney().getText() : ((PlayerTradeMenu) this.f_97732_).getTradeData().getGuestMoney().getText();
            Component var7 = ((PlayerTradeMenu) this.f_97732_).isHost() ? ((PlayerTradeMenu) this.f_97732_).getTradeData().getGuestMoney().getText() : ((PlayerTradeMenu) this.f_97732_).getTradeData().getHostMoney().getText();
            gui.drawString(var5, 8, 85, 4210752);
            gui.drawString(var7, this.getXSize() - 8 - this.f_96547_.width(var7), 85, 4210752);
        }
    }

    @Override
    protected void initialize(ScreenArea screenArea) {
        this.valueInput = this.addChild(new MoneyValueWidget(screenArea.pos, this.valueInput, MoneyValue.empty(), this::onValueChanged));
        this.valueInput.allowFreeInput = false;
        this.buttonPropose = this.addChild(new EasyTextButton(screenArea.pos.offset(8, 179), 70, 20, LCText.BUTTON_PLAYER_TRADING_PROPOSE.get(), this::OnPropose));
        this.buttonAccept = this.addChild(new EasyTextButton(screenArea.pos.offset(98, 179), 70, 20, LCText.BUTTON_PLAYER_TRADING_ACCEPT.get(), this::OnAccept));
        this.buttonAccept.f_93623_ = false;
        this.buttonToggleChat = this.addChild(new IconButton(screenArea.pos.offset(screenArea.width, 69), this::ToggleChatMode, this::getToggleIcon).withAddons(EasyAddonHelper.tooltip(this::getToggleTooltip)));
        this.chatBox = this.addChild(new EditBox(this.f_96547_, screenArea.pos.x + 9, screenArea.pos.y + 120 + 69, screenArea.width - 22, 12, EasyText.empty()));
        this.chatBox.setBordered(false);
        this.chatBox.setMaxLength(256);
        this.chatScrollListener = this.addChild(new ScrollListener(screenArea.ofSize(screenArea.width, 118).offsetPosition(0, 69), this));
        this.chatScrollListener.inverted = true;
        this.validateWidgetStates();
    }

    @Override
    protected void screenTick() {
        int myState = ((PlayerTradeMenu) this.f_97732_).myState();
        int otherState = ((PlayerTradeMenu) this.f_97732_).otherState();
        this.valueInput.f_93623_ = myState < 1;
        this.buttonPropose.f_93623_ = myState < 2;
        this.buttonPropose.m_93666_(myState <= 0 ? LCText.BUTTON_PLAYER_TRADING_PROPOSE.get() : LCText.BUTTON_PLAYER_TRADING_CANCEL.get());
        this.buttonAccept.f_93623_ = myState > 0 && otherState > 0;
        this.buttonAccept.m_93666_(myState <= 1 ? LCText.BUTTON_PLAYER_TRADING_ACCEPT.get() : LCText.BUTTON_PLAYER_TRADING_CANCEL.get());
        if (this.chatMode) {
            this.m_7522_(this.chatBox);
        }
        this.buttonToggleChat.bgColor = this.chatWarning ? 16776960 : 16777215;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifier) {
        if (this.chatMode && !this.chatBox.getValue().isBlank() && keyCode == 257) {
            ((PlayerTradeMenu) this.f_97732_).SendChatToServer(this.chatBox.getValue());
            this.chatBox.setValue("");
            return true;
        } else {
            return this.f_96541_.options.keyInventory.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode)) ? true : super.keyPressed(keyCode, scanCode, modifier);
        }
    }

    private void onValueChanged(MoneyValue newValue) {
        CompoundTag message = new CompoundTag();
        MoneyView availableFunds = ((PlayerTradeMenu) this.f_97732_).getAvailableFunds();
        if (!availableFunds.containsValue(newValue)) {
            newValue = availableFunds.valueOf(newValue.getUniqueName());
            this.valueInput.changeValue(newValue);
        }
        message.put("ChangeMoney", newValue.save());
        new CPacketPlayerTradeInteraction(((PlayerTradeMenu) this.f_97732_).tradeID, message).send();
    }

    private void OnPropose(EasyButton button) {
        CompoundTag message = new CompoundTag();
        message.putBoolean("TogglePropose", true);
        new CPacketPlayerTradeInteraction(((PlayerTradeMenu) this.f_97732_).tradeID, message).send();
    }

    private void OnAccept(EasyButton button) {
        CompoundTag message = new CompoundTag();
        message.putBoolean("ToggleActive", true);
        new CPacketPlayerTradeInteraction(((PlayerTradeMenu) this.f_97732_).tradeID, message).send();
    }

    private void ToggleChatMode(EasyButton button) {
        this.chatMode = !this.chatMode;
        if (this.chatMode) {
            ((PlayerTradeMenu) this.f_97732_).hideSlots();
            this.chatWarning = false;
        } else {
            ((PlayerTradeMenu) this.f_97732_).showSlots();
        }
        this.validateWidgetStates();
    }

    private IconData getToggleIcon() {
        return this.chatWarning ? IconData.of(Items.WRITABLE_BOOK) : IconData.of(Items.BOOK);
    }

    private Component getToggleTooltip() {
        return this.chatMode ? LCText.TOOLTIP_PLAYER_TRADING_CHAT_CLOSE.get() : LCText.TOOLTIP_PLAYER_TRADING_CHAT_OPEN.get();
    }

    private void validateWidgetStates() {
        this.chatBox.setVisible(this.chatScrollListener.active = this.chatMode);
        this.buttonAccept.f_93624_ = this.buttonPropose.f_93624_ = this.valueInput.f_93624_ = !this.chatMode;
    }

    private void receiveChat(@Nonnull Component chat) {
        for (FormattedCharSequence line : this.f_96547_.split(chat, this.getXSize() - 14)) {
            this.chatHistory.add(0, line);
        }
        if (!this.chatMode) {
            this.chatWarning = true;
        }
    }

    @Override
    public int currentScroll() {
        return this.scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        this.scroll = newScroll;
    }

    @Override
    public int getMaxScroll() {
        return IScrollable.calculateMaxScroll(11, this.chatHistory.size());
    }
}