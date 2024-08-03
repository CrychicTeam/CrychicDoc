package portb.biggerstacks.event;

import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.net.ClientboundRulesUpdatePacket;
import portb.biggerstacks.net.PacketHandler;
import portb.configlib.ConfigFileWatcher;
import portb.configlib.ConfigLib;
import portb.configlib.xml.RuleSet;

public class ServerLifecycleHandler {

    private final ConfigFileWatcher watcher = new ConfigFileWatcher(Constants.RULESET_FILE);

    private boolean stopped = false;

    public ServerLifecycleHandler() {
        StackSizeRules.setRuleSet(ConfigLib.readRuleset(Constants.RULESET_FILE));
        this.watcher.setOnUpdateAction(this::notifyClientsOfConfigChange);
        this.watcher.start();
    }

    private void notifyClientsOfConfigChange(RuleSet ruleSet) {
        StackSizeRules.setRuleSet(ruleSet);
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientboundRulesUpdatePacket(ruleSet));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void serverStopping(ServerStoppingEvent event) {
        if (!this.stopped) {
            this.watcher.stop();
            this.stopped = true;
        }
    }

    public void ensureStopped() {
        if (!this.stopped) {
            this.watcher.stop();
            this.stopped = true;
        }
    }
}