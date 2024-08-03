package com.craisinlord.integrated_api.utils;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;

public class BoxOctree {

    private static final int subdivideThreshold = 10;

    private static final int maximumDepth = 3;

    private final AABB boundary;

    private final Vec3i size;

    private final int depth;

    private final List<AABB> innerBoxes = new ArrayList();

    private final List<BoxOctree> childrenOctants = new ArrayList();

    public BoxOctree(AABB axisAlignedBB) {
        this(axisAlignedBB, 0);
    }

    private BoxOctree(AABB axisAlignedBB, int parentDepth) {
        this.boundary = axisAlignedBB.move(0.0, 0.0, 0.0);
        this.size = new Vec3i((int) this.boundary.getXsize(), (int) this.boundary.getYsize(), (int) this.boundary.getZsize());
        this.depth = parentDepth + 1;
    }

    private void subdivide() {
        if (!this.childrenOctants.isEmpty()) {
            throw new UnsupportedOperationException("Integrated API - Tried to subdivide when there are already children octants.");
        } else {
            int halfXSize = this.size.getX() / 2;
            int halfYSize = this.size.getY() / 2;
            int halfZSize = this.size.getZ() / 2;
            this.childrenOctants.add(new BoxOctree(new AABB(this.boundary.minX, this.boundary.minY, this.boundary.minZ, this.boundary.minX + (double) halfXSize, this.boundary.minY + (double) halfYSize, this.boundary.minZ + (double) halfZSize), this.depth));
            this.childrenOctants.add(new BoxOctree(new AABB(this.boundary.minX + (double) halfXSize, this.boundary.minY, this.boundary.minZ, this.boundary.maxX, this.boundary.minY + (double) halfYSize, this.boundary.minZ + (double) halfZSize), this.depth));
            this.childrenOctants.add(new BoxOctree(new AABB(this.boundary.minX, this.boundary.minY + (double) halfYSize, this.boundary.minZ, this.boundary.minX + (double) halfXSize, this.boundary.maxY, this.boundary.minZ + (double) halfZSize), this.depth));
            this.childrenOctants.add(new BoxOctree(new AABB(this.boundary.minX, this.boundary.minY, this.boundary.minZ + (double) halfZSize, this.boundary.minX + (double) halfXSize, this.boundary.minY + (double) halfYSize, this.boundary.maxZ), this.depth));
            this.childrenOctants.add(new BoxOctree(new AABB(this.boundary.minX + (double) halfXSize, this.boundary.minY + (double) halfYSize, this.boundary.minZ, this.boundary.maxX, this.boundary.maxY, this.boundary.minZ + (double) halfZSize), this.depth));
            this.childrenOctants.add(new BoxOctree(new AABB(this.boundary.minX, this.boundary.minY + (double) halfYSize, this.boundary.minZ + (double) halfZSize, this.boundary.minX + (double) halfXSize, this.boundary.maxY, this.boundary.maxZ), this.depth));
            this.childrenOctants.add(new BoxOctree(new AABB(this.boundary.minX + (double) halfXSize, this.boundary.minY, this.boundary.minZ + (double) halfZSize, this.boundary.maxX, this.boundary.minY + (double) halfYSize, this.boundary.maxZ), this.depth));
            this.childrenOctants.add(new BoxOctree(new AABB(this.boundary.minX + (double) halfXSize, this.boundary.minY + (double) halfYSize, this.boundary.minZ + (double) halfZSize, this.boundary.maxX, this.boundary.maxY, this.boundary.maxZ), this.depth));
            for (AABB parentInnerBox : this.innerBoxes) {
                for (BoxOctree octree : this.childrenOctants) {
                    if (octree.boundaryContainsFuzzy(parentInnerBox)) {
                        octree.addBox(parentInnerBox);
                    }
                }
            }
            this.innerBoxes.clear();
        }
    }

    public void addBox(AABB axisAlignedBB) {
        if (this.depth < 3 && this.innerBoxes.size() > 10) {
            this.subdivide();
        }
        if (!this.childrenOctants.isEmpty()) {
            for (BoxOctree octree : this.childrenOctants) {
                if (octree.boundaryContainsFuzzy(axisAlignedBB)) {
                    octree.addBox(axisAlignedBB);
                }
            }
        } else {
            for (AABB parentInnerBox : this.innerBoxes) {
                if (parentInnerBox.equals(axisAlignedBB)) {
                    return;
                }
            }
            this.innerBoxes.add(axisAlignedBB);
        }
    }

    public void removeBox(AABB axisAlignedBB) {
        if (!this.childrenOctants.isEmpty()) {
            for (BoxOctree octree : this.childrenOctants) {
                if (octree.boundaryContainsFuzzy(axisAlignedBB)) {
                    octree.removeBox(axisAlignedBB);
                }
            }
        } else {
            for (AABB innerBox : this.innerBoxes) {
                if (innerBox.equals(axisAlignedBB)) {
                    this.innerBoxes.remove(innerBox);
                    return;
                }
            }
        }
    }

    public boolean boundaryContainsFuzzy(AABB axisAlignedBB) {
        return this.boundary.inflate(axisAlignedBB.getSize() / 2.0).intersects(axisAlignedBB);
    }

    public boolean boundaryContains(AABB axisAlignedBB) {
        return this.boundary.contains(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ) && this.boundary.contains(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    }

    public boolean intersectsAnyBox(AABB axisAlignedBB) {
        if (!this.childrenOctants.isEmpty()) {
            for (BoxOctree octree : this.childrenOctants) {
                if (octree.intersectsAnyBox(axisAlignedBB)) {
                    return true;
                }
            }
        } else {
            for (AABB innerBox : this.innerBoxes) {
                if (innerBox.intersects(axisAlignedBB)) {
                    return true;
                }
            }
        }
        return false;
    }
}