package net.minecraft.world.level.levelgen.structure.pools;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

public class JigsawJunction {

    private final int sourceX;

    private final int sourceGroundY;

    private final int sourceZ;

    private final int deltaY;

    private final StructureTemplatePool.Projection destProjection;

    public JigsawJunction(int int0, int int1, int int2, int int3, StructureTemplatePool.Projection structureTemplatePoolProjection4) {
        this.sourceX = int0;
        this.sourceGroundY = int1;
        this.sourceZ = int2;
        this.deltaY = int3;
        this.destProjection = structureTemplatePoolProjection4;
    }

    public int getSourceX() {
        return this.sourceX;
    }

    public int getSourceGroundY() {
        return this.sourceGroundY;
    }

    public int getSourceZ() {
        return this.sourceZ;
    }

    public int getDeltaY() {
        return this.deltaY;
    }

    public StructureTemplatePool.Projection getDestProjection() {
        return this.destProjection;
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> dynamicOpsT0) {
        Builder<T, T> $$1 = ImmutableMap.builder();
        $$1.put(dynamicOpsT0.createString("source_x"), dynamicOpsT0.createInt(this.sourceX)).put(dynamicOpsT0.createString("source_ground_y"), dynamicOpsT0.createInt(this.sourceGroundY)).put(dynamicOpsT0.createString("source_z"), dynamicOpsT0.createInt(this.sourceZ)).put(dynamicOpsT0.createString("delta_y"), dynamicOpsT0.createInt(this.deltaY)).put(dynamicOpsT0.createString("dest_proj"), dynamicOpsT0.createString(this.destProjection.getName()));
        return new Dynamic(dynamicOpsT0, dynamicOpsT0.createMap($$1.build()));
    }

    public static <T> JigsawJunction deserialize(Dynamic<T> dynamicT0) {
        return new JigsawJunction(dynamicT0.get("source_x").asInt(0), dynamicT0.get("source_ground_y").asInt(0), dynamicT0.get("source_z").asInt(0), dynamicT0.get("delta_y").asInt(0), StructureTemplatePool.Projection.byName(dynamicT0.get("dest_proj").asString("")));
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 != null && this.getClass() == object0.getClass()) {
            JigsawJunction $$1 = (JigsawJunction) object0;
            if (this.sourceX != $$1.sourceX) {
                return false;
            } else if (this.sourceZ != $$1.sourceZ) {
                return false;
            } else {
                return this.deltaY != $$1.deltaY ? false : this.destProjection == $$1.destProjection;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int $$0 = this.sourceX;
        $$0 = 31 * $$0 + this.sourceGroundY;
        $$0 = 31 * $$0 + this.sourceZ;
        $$0 = 31 * $$0 + this.deltaY;
        return 31 * $$0 + this.destProjection.hashCode();
    }

    public String toString() {
        return "JigsawJunction{sourceX=" + this.sourceX + ", sourceGroundY=" + this.sourceGroundY + ", sourceZ=" + this.sourceZ + ", deltaY=" + this.deltaY + ", destProjection=" + this.destProjection + "}";
    }
}