package dev.xkmc.l2hostility.init.network;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2hostility.compat.jei.ITraitLootRecipe;
import dev.xkmc.l2hostility.init.loot.TraitLootModifier;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class LootDataToClient extends SerialPacketBase {

    public static List<ITraitLootRecipe> LIST_CACHE = new ArrayList();

    @SerialField
    public ArrayList<CompoundTag> list = new ArrayList();

    @Deprecated
    public LootDataToClient() {
    }

    public LootDataToClient(List<TraitLootModifier> list) {
        for (TraitLootModifier e : list) {
            Optional<Tag> res = IGlobalLootModifier.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, e).result();
            if (res.isPresent() && res.get() instanceof CompoundTag ct) {
                this.list.add(ct);
            }
        }
    }

    public void handle(NetworkEvent.Context context) {
        LIST_CACHE = new ArrayList();
        for (CompoundTag ct : this.list) {
            Optional<Pair<IGlobalLootModifier, Tag>> ans = IGlobalLootModifier.DIRECT_CODEC.decode(NbtOps.INSTANCE, ct).result();
            if (!ans.isEmpty() && ((Pair) ans.get()).getFirst() instanceof TraitLootModifier mod) {
                LIST_CACHE.add(mod);
            }
        }
    }
}