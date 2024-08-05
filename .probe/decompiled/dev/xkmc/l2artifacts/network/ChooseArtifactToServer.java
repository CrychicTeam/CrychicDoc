package dev.xkmc.l2artifacts.network;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.content.misc.SelectArtifactItem;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class ChooseArtifactToServer extends SerialPacketBase {

    @SerialField
    public int set;

    @SerialField
    public int slot;

    @SerialField
    public int rank;

    @Deprecated
    public ChooseArtifactToServer() {
    }

    public ChooseArtifactToServer(int set, int slot, int rank) {
        this.set = set;
        this.slot = slot;
        this.rank = rank;
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null) {
            ItemStack stack = player.m_21120_(InteractionHand.MAIN_HAND);
            if (!(stack.getItem() instanceof SelectArtifactItem)) {
                stack = player.m_21120_(InteractionHand.OFF_HAND);
            }
            if (stack.getItem() instanceof SelectArtifactItem) {
                List<SetEntry<?>> sets = L2Artifacts.REGISTRATE.SET_LIST;
                if (this.set < sets.size()) {
                    ItemEntry<BaseArtifact>[][] slots = ((SetEntry) sets.get(this.set)).items;
                    if (this.slot < slots.length) {
                        ItemEntry<BaseArtifact>[] ranks = slots[this.slot];
                        if (this.rank < ranks.length) {
                            if (!player.m_150110_().instabuild) {
                                stack.shrink(1);
                            }
                            ItemStack artifact = ranks[this.rank].asStack();
                            player.m_150109_().placeItemBackInInventory(artifact);
                        }
                    }
                }
            }
        }
    }
}