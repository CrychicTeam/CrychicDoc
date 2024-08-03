package journeymap.client.ui.dialog;

import java.util.ArrayList;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.option.CategorySlot;
import journeymap.client.ui.option.OptionSlotFactory;
import journeymap.common.Journeymap;
import journeymap.common.properties.MultiplayerCategory;
import journeymap.common.properties.MultiplayerProperties;
import journeymap.common.properties.ServerCategory;
import net.minecraft.ChatFormatting;

public class MultiplayerOptionsManager extends ServerOptionsManager {

    MultiplayerProperties multiplayerProperties;

    public MultiplayerOptionsManager(JmUI returnDisplay) {
        super(returnDisplay, Constants.getString("jm.options.multiplayer.title"), new ArrayList(ServerCategory.values));
    }

    @Override
    public void init() {
        super.init();
        if (!this.buttonServer.m_6035_().getString().contains(ChatFormatting.STRIKETHROUGH.toString())) {
            this.buttonServer.setEnabled(true);
        }
        this.buttonMultiplayer.setEnabled(false);
    }

    @Override
    protected void requestInitData() {
        JourneymapClient.getInstance().getDispatcher().sendMultiplayerOptionsRequest();
    }

    public void setData(String payload) {
        try {
            this.multiplayerProperties = new MultiplayerProperties().loadForClient(payload, false);
            this.slotMap.put(MultiplayerCategory.Multiplayer, this.multiplayerProperties);
            this.init();
        } catch (Exception var3) {
            Journeymap.getLogger().error("Error getting data", var3);
        }
    }

    @Override
    protected void save() {
        JourneymapClient.getInstance().getDispatcher().sendMultiplayerOptionsSaveRequest(this.multiplayerProperties.toJsonString(false));
    }

    @Override
    protected List<CategorySlot> getCategorySlotList() {
        return OptionSlotFactory.getOptionSlots(this.getToolbars(), this.slotMap, !JourneymapClient.getInstance().getStateHandler().isExpandedRadarEnabled(), false);
    }
}