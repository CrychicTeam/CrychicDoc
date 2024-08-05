package portb.biggerstacks.net;

import net.minecraft.network.FriendlyByteBuf;
import portb.configlib.xml.RuleSet;

public class ClientboundRulesUpdatePacket {

    private final RuleSet rules;

    public ClientboundRulesUpdatePacket(RuleSet rules) {
        this.rules = rules;
    }

    public ClientboundRulesUpdatePacket(FriendlyByteBuf buf) {
        this.rules = RuleSet.fromBytes(buf.readByteArray());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByteArray(this.rules.toBytes());
    }

    public RuleSet getRules() {
        return this.rules;
    }
}