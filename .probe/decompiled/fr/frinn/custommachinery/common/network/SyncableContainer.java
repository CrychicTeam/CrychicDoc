package fr.frinn.custommachinery.common.network;

import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinery.api.network.IData;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.common.network.syncable.IntegerSyncable;
import fr.frinn.custommachinery.common.network.syncable.ItemStackSyncable;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class SyncableContainer extends AbstractContainerMenu {

    private final ServerPlayer player;

    private final List<ISyncable<?, ?>> stuffToSync = new ArrayList();

    private final ISyncableStuff syncableStuff;

    public SyncableContainer(@Nullable MenuType<?> type, int id, ISyncableStuff syncableStuff, Player player) {
        super(type, id);
        this.player = player instanceof ServerPlayer serverPlayer ? serverPlayer : null;
        this.syncableStuff = syncableStuff;
    }

    public void init() {
        this.stuffToSync.clear();
        this.stuffToSync.add(DataType.createSyncable(ItemStack.class, this::m_142621_, this::m_142503_));
        this.syncableStuff.getStuffToSync(this.stuffToSync::add);
    }

    public abstract boolean needFullSync();

    @Override
    public void broadcastChanges() {
        if (this.player != null) {
            if (this.needFullSync()) {
                List<IData<?>> toSync = new ArrayList();
                for (short id = 0; id < this.stuffToSync.size(); id++) {
                    toSync.add(((ISyncable) this.stuffToSync.get(id)).getData(id));
                }
                new SUpdateContainerPacket(this.f_38840_, toSync).sendTo(this.player);
                return;
            }
            List<IData<?>> toSync = new ArrayList();
            for (short id = 0; id < this.stuffToSync.size(); id++) {
                if (((ISyncable) this.stuffToSync.get(id)).needSync()) {
                    toSync.add(((ISyncable) this.stuffToSync.get(id)).getData(id));
                }
            }
            if (!toSync.isEmpty()) {
                new SUpdateContainerPacket(this.f_38840_, toSync).sendTo(this.player);
            }
        }
    }

    public void handleData(IData<?> data) {
        short id = data.getID();
        ISyncable syncable = (ISyncable) this.stuffToSync.get(id);
        if (syncable != null) {
            syncable.set(data.getValue());
        }
    }

    @Override
    protected DataSlot addDataSlot(DataSlot intReferenceHolder) {
        this.stuffToSync.add(IntegerSyncable.create(intReferenceHolder::m_6501_, intReferenceHolder::m_6422_));
        return intReferenceHolder;
    }

    @Override
    protected void addDataSlots(ContainerData array) {
        for (int i = 0; i < array.getCount(); i++) {
            int index = i;
            this.stuffToSync.add(IntegerSyncable.create(() -> array.get(index), integer -> array.set(index, integer)));
        }
    }

    @Override
    public void initializeContents(int stateId, List<ItemStack> items, ItemStack carried) {
    }

    protected Slot addSyncedSlot(Slot slot) {
        this.stuffToSync.add(ItemStackSyncable.create(slot::m_7993_, slot::m_5852_));
        return this.m_38897_(slot);
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }
}