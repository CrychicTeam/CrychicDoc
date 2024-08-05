package noppes.npcs.client;

import java.util.List;
import net.minecraft.network.syncher.SynchedEntityData;

public interface ISynchedEntityData {

    List<SynchedEntityData.DataItem<?>> getAll();
}