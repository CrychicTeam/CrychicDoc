package snownee.loquat;

import net.minecraft.server.level.ServerPlayer;
import snownee.loquat.core.area.Area;

public interface LoquatEvents {

    @FunctionalInterface
    public interface PlayerEnterArea {

        void enterArea(ServerPlayer var1, Area var2);
    }

    @FunctionalInterface
    public interface PlayerLeaveArea {

        void leaveArea(ServerPlayer var1, Area var2);
    }
}