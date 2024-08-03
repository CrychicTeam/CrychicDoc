package org.violetmoon.zeta.client.config;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.client.config.definition.BooleanClientDefinition;
import org.violetmoon.zeta.client.config.definition.ClientDefinitionExt;
import org.violetmoon.zeta.client.config.definition.DoubleClientDefinition;
import org.violetmoon.zeta.client.config.definition.IConfigDefinitionProvider;
import org.violetmoon.zeta.client.config.definition.IntegerClientDefinition;
import org.violetmoon.zeta.client.config.definition.SectionClientDefinition;
import org.violetmoon.zeta.client.config.definition.StringClientDefinition;
import org.violetmoon.zeta.client.config.definition.StringListClientDefinition;
import org.violetmoon.zeta.config.Definition;
import org.violetmoon.zeta.config.SectionDefinition;
import org.violetmoon.zeta.config.ValueDefinition;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.network.message.C2SUpdateFlag;

public class ClientConfigManager {

    final ZetaClient zc;

    public ClientConfigManager(ZetaClient zc) {
        this.zc = zc;
    }

    @NotNull
    public <D extends Definition> ClientDefinitionExt<D> getExt(D def) {
        if (def.hint instanceof IConfigDefinitionProvider) {
            return (ClientDefinitionExt<D>) ((IConfigDefinitionProvider) def.hint).getClientConfigDefinition((SectionDefinition) def);
        } else if (def instanceof SectionDefinition) {
            return new SectionClientDefinition();
        } else {
            if (def instanceof ValueDefinition<?> val) {
                if (val.defaultValue instanceof Boolean) {
                    return new BooleanClientDefinition();
                }
                if (val.defaultValue instanceof String) {
                    return new StringClientDefinition();
                }
                if (val.defaultValue instanceof Integer) {
                    return new IntegerClientDefinition();
                }
                if (val.defaultValue instanceof Double) {
                    return new DoubleClientDefinition();
                }
                if (val.defaultValue instanceof List) {
                    return new StringListClientDefinition();
                }
            }
            throw new IllegalArgumentException(def + " is not a legal config value");
        }
    }

    @LoadEvent
    public void configChanged(ZConfigChanged event) {
        if (Minecraft.getInstance().getConnection() != null) {
            this.zc.sendToServer(C2SUpdateFlag.createPacket());
        }
    }
}