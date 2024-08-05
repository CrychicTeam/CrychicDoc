package noppes.npcs.api.entity.data.role;

import noppes.npcs.api.entity.data.INPCRole;

public interface IRoleTransporter extends INPCRole {

    IRoleTransporter.ITransportLocation getLocation();

    public interface ITransportLocation {

        int getId();

        String getDimension();

        int getX();

        int getY();

        int getZ();

        String getName();

        int getType();
    }
}