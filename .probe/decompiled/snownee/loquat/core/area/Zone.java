package snownee.loquat.core.area;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.phys.AABB;
import snownee.loquat.util.AABBSerializer;

public record Zone(List<AABB> aabbs) {

    public static Zone deserialize(CompoundTag data) {
        ListTag list = data.getList("AABBs", 9);
        List<AABB> aabbs = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            aabbs.add(AABBSerializer.read(list.getList(i)));
        }
        return new Zone(List.copyOf(aabbs));
    }

    public CompoundTag serialize(CompoundTag data) {
        ListTag list = new ListTag();
        for (AABB aabb : this.aabbs) {
            list.add(AABBSerializer.write(aabb));
        }
        data.put("AABBs", list);
        return data;
    }
}