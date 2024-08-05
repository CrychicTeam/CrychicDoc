package snownee.loquat.util;

import java.util.stream.DoubleStream;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.phys.AABB;

public interface AABBSerializer {

    static ListTag write(AABB aabb) {
        ListTag doubleList = new ListTag();
        DoubleStream.of(new double[] { aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ }).mapToObj(DoubleTag::m_128500_).forEach(doubleList::add);
        return doubleList;
    }

    static AABB read(ListTag doubleList) {
        return new AABB(doubleList.getDouble(0), doubleList.getDouble(1), doubleList.getDouble(2), doubleList.getDouble(3), doubleList.getDouble(4), doubleList.getDouble(5));
    }
}