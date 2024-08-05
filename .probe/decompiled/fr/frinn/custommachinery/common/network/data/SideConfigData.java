package fr.frinn.custommachinery.common.network.data;

import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.component.config.RelativeSide;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import fr.frinn.custommachinery.impl.component.config.SideMode;
import fr.frinn.custommachinery.impl.network.Data;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;

public class SideConfigData extends Data<SideConfig> {

    public SideConfigData(Short id, SideConfig value) {
        super((DataType<?, SideConfig>) Registration.SIDE_CONFIG_DATA.get(), id, value);
    }

    public static SideConfigData readData(short id, FriendlyByteBuf buffer) {
        Map<RelativeSide, SideMode> map = new HashMap();
        for (RelativeSide side : RelativeSide.values()) {
            map.put(side, SideMode.values()[buffer.readByte()]);
        }
        return new SideConfigData(id, new SideConfig(null, map, buffer.readBoolean(), buffer.readBoolean(), true));
    }

    @Override
    public void writeData(FriendlyByteBuf buffer) {
        super.writeData(buffer);
        for (RelativeSide side : RelativeSide.values()) {
            buffer.writeByte(this.getValue().getSideMode(side).ordinal());
        }
        buffer.writeBoolean(this.getValue().isAutoInput());
        buffer.writeBoolean(this.getValue().isAutoOutput());
    }
}