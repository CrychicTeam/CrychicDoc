package portb.biggerstacks.net;

import net.minecraft.network.FriendlyByteBuf;
import portb.biggerstacks.config.StackSizeRules;
import portb.configlib.xml.RuleSet;

public class ClientboundRulesHandshakePacket {

    private final RuleSet rules;

    public ClientboundRulesHandshakePacket(FriendlyByteBuf buf) {
        this.rules = RuleSet.fromBytes(buf.readByteArray());
    }

    public ClientboundRulesHandshakePacket() {
        this.rules = StackSizeRules.getRuleSet();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByteArray(this.rules.toBytes());
    }

    public RuleSet getRules() {
        return this.rules;
    }
}