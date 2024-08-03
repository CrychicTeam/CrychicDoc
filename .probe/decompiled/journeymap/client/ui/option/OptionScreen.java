package journeymap.client.ui.option;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.InternalStateHandler;
import journeymap.client.JourneymapClient;
import journeymap.client.api.impl.OptionsDisplayFactory;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.JmUI;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public abstract class OptionScreen extends JmUI {

    protected final int panelTop = 70;

    protected final int panelBottomMargin = 35;

    protected Button buttonClose;

    protected Button buttonAbout;

    protected Button buttonServer;

    protected Button buttonMultiplayer;

    protected Button buttonAddons;

    protected List<Button> specialBottomButtons = new ArrayList();

    protected Button clientOptions;

    protected Screen returnDisplay;

    public OptionScreen(String title, Screen returnDisplay) {
        super(title, returnDisplay);
        if (returnDisplay == null) {
            returnDisplayStack.pop();
        }
    }

    public OptionScreen(String title) {
        super(title);
        returnDisplayStack.pop();
    }

    @Override
    public void init() {
        this.setRenderBottomBar(true);
        this.getRenderables().clear();
        this.buttonClose = (Button) this.m_142416_(new Button(Constants.getString("jm.common.close"), button -> this.closeAndReturn()));
        this.buttonClose.setDefaultStyle(false);
        this.buttonAbout = (Button) this.m_142416_(new Button(Constants.getString("jm.common.splash_about"), button -> UIManager.INSTANCE.openSplash(this)));
        this.buttonAbout.setDefaultStyle(false);
        this.buttonServer = new Button(Constants.getString("jm.server.edit.label.admin.edit"), button -> UIManager.INSTANCE.openServerEditor(null));
        this.setUpTopButton(this.buttonServer);
        this.buttonMultiplayer = new Button(Constants.getString("jm.options.multiplayer.button_label"), button -> UIManager.INSTANCE.openMultiplayerEditor(null));
        this.setUpTopButton(this.buttonMultiplayer);
        this.buttonAddons = new Button(Constants.getString("jm.common.addon_options"), button -> UIManager.INSTANCE.openAddonOptionsEditor(null));
        this.setUpTopButton(this.buttonAddons);
        if (OptionsDisplayFactory.PROPERTIES_REGISTRY.isEmpty()) {
            this.buttonAddons.setTooltip(Constants.getString("jm.common.addon_options_invalid"));
            this.disableButton(this.buttonAddons);
        }
        this.clientOptions = new Button(Constants.getString("jm.common.client_options"), button -> UIManager.INSTANCE.openOptionsManager(null));
        this.setUpTopButton(this.clientOptions);
        InternalStateHandler stateHandler = JourneymapClient.getInstance().getStateHandler();
        if (!stateHandler.canServerAdmin() && !stateHandler.isReadOnlyServerAdmin()) {
            boolean isAdmin = stateHandler.isServerAdmin();
            boolean hasServer = stateHandler.isJourneyMapServerConnection();
            if (!isAdmin && hasServer && !stateHandler.isReadOnlyServerAdmin()) {
                this.buttonServer.setTooltip(Constants.getString("jm.server.button.no_permission.tooltip"));
            } else if (!hasServer) {
                this.buttonServer.setTooltip(Constants.getString("jm.server.button.no_server.tooltip"));
            }
            this.disableButton(this.buttonServer);
        }
        IntegratedServer server = this.f_96541_.getSingleplayerServer();
        boolean isSinglePlayer = server != null && !server.isPublished();
        if (!stateHandler.isMultiplayerOptionsAllowed() || isSinglePlayer || !stateHandler.isJourneyMapServerConnection()) {
            boolean hasServer = stateHandler.isJourneyMapServerConnection();
            if (isSinglePlayer) {
                this.buttonMultiplayer.setTooltip(Constants.getString("menu.singleplayer"));
            } else if (hasServer) {
                this.buttonMultiplayer.setTooltip(Constants.getString("jm.server.button.no_permission.tooltip"));
            } else {
                this.buttonMultiplayer.setTooltip(Constants.getString("jm.server.button.no_server.tooltip"));
            }
            this.disableButton(this.buttonMultiplayer);
        }
        ButtonList bottomRow;
        if (this.specialBottomButtons.isEmpty()) {
            bottomRow = new ButtonList(this.buttonAbout, this.buttonClose);
        } else {
            bottomRow = new ButtonList();
            bottomRow.add(this.buttonAbout);
            bottomRow.addAll(this.specialBottomButtons);
            bottomRow.add(this.buttonClose);
        }
        bottomRow.equalizeWidths(this.getFontRenderer());
        bottomRow.setWidths(Math.max(100, this.buttonAbout.m_5711_()));
        bottomRow.layoutCenteredHorizontal(this.f_96543_ / 2, this.f_96544_ - 25, true, 4);
        ButtonList topRow = new ButtonList(this.clientOptions, this.buttonAddons, this.buttonServer, this.buttonMultiplayer);
        topRow.equalizeWidths(this.getFontRenderer());
        topRow.setWidths(Math.max(100, this.clientOptions.m_5711_()));
        topRow.layoutCenteredHorizontal(this.f_96543_ / 2, 45, true, 1);
        this.getRenderables().addAll(bottomRow);
        this.getRenderables().addAll(topRow);
    }

    private void setUpTopButton(Button button) {
        this.m_142416_(button);
        button.setEnabled(true);
        button.setDefaultStyle(false);
        button.setDrawBackground(false);
        button.setDrawBackgroundOnDisable(false);
    }

    private void disableButton(Button button) {
        button.setEnabled(false);
        button.setDefaultStyle(false);
        button.setDrawBackground(true);
        button.setDrawBackgroundOnDisable(true);
        MutableComponent component = Component.literal(ChatFormatting.STRIKETHROUGH + button.getLabel());
        button.m_93666_(component);
    }

    @Override
    protected void renderBottomBar(PoseStack stack) {
        DrawUtil.drawRectangle(stack, 0.0, (double) (this.f_96544_ - 30), (double) this.f_96543_, (double) this.f_96544_, 0, 0.6F);
    }
}