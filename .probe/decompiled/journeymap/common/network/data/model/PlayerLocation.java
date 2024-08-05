package journeymap.common.network.data.model;

import java.util.UUID;

public interface PlayerLocation {

    int getEntityId();

    UUID getUniqueId();

    double getX();

    double getY();

    double getZ();

    byte getYaw();

    byte getPitch();

    boolean isVisible();
}