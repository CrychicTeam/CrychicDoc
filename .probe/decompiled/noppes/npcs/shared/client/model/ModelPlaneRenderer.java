package noppes.npcs.shared.client.model;

import net.minecraft.core.Direction;
import noppes.npcs.shared.common.util.NopVector3f;

public class ModelPlaneRenderer extends NopModelPart {

    private int xTexOffs;

    private int yTexOffs;

    public ModelPlaneRenderer(int i, int j) {
        super(64, 64, i, j);
        this.xTexOffs = i;
        this.yTexOffs = j;
    }

    public ModelPlaneRenderer(int textX, int textY, int i, int j) {
        super(textX, textY, i, j);
        this.xTexOffs = i;
        this.yTexOffs = j;
    }

    public ModelPlaneRenderer mirror(boolean bo) {
        this.mirror = bo;
        return this;
    }

    public ModelPlaneRenderer addPlane(float x, float y, float z, int sizeX, int sizeY, NopVector3f scale, Direction d) {
        if (d == Direction.DOWN || d == Direction.UP) {
            this.addPlane(x, y, z, sizeX, 0, sizeY, scale, d);
        }
        if (d == Direction.EAST || d == Direction.WEST) {
            this.addPlane(x, y, z, 0, sizeX, sizeY, scale, d);
        }
        if (d == Direction.SOUTH || d == Direction.NORTH) {
            this.addPlane(x, y, z, sizeX, sizeY, 0, scale, d);
        }
        return this;
    }

    public void addBackPlane(float f, float f1, float f2, int i, int j) {
        this.addPlane(f, f1, f2, i, j, 0, NopVector3f.ONE, Direction.SOUTH);
    }

    public ModelPlaneRenderer addSidePlane(float f, float f1, float f2, int j, int k) {
        this.addPlane(f, f1, f2, 0, j, k, NopVector3f.ONE, Direction.WEST);
        return this;
    }

    public void addTopPlane(float f, float f1, float f2, int i, int k) {
        this.addPlane(f, f1, f2, i, 0, k, NopVector3f.ONE, Direction.UP);
    }

    private void addPlane(float x, float y, float z, int dx, int dy, int dz, NopVector3f scale, Direction pos) {
        this.addBox(x, y, z, (float) dx, (float) dy, (float) dz);
        float xx = x + (float) dx;
        float yy = y + (float) dy;
        float zz = z + (float) dz;
        xx *= scale.x;
        yy *= scale.y;
        zz *= scale.z;
        if (this.mirror) {
            float var14 = xx;
            xx = x;
            x = var14;
        }
        NopModelPart.PositionTextureVertex lvt_18_2_ = new NopModelPart.PositionTextureVertex(x, y, z, 0.0F, 0.0F);
        NopModelPart.PositionTextureVertex lvt_19_1_ = new NopModelPart.PositionTextureVertex(xx, y, z, 0.0F, 8.0F);
        NopModelPart.PositionTextureVertex lvt_20_1_ = new NopModelPart.PositionTextureVertex(xx, yy, z, 8.0F, 8.0F);
        NopModelPart.PositionTextureVertex lvt_21_1_ = new NopModelPart.PositionTextureVertex(x, yy, z, 8.0F, 0.0F);
        NopModelPart.PositionTextureVertex lvt_22_1_ = new NopModelPart.PositionTextureVertex(x, y, zz, 0.0F, 0.0F);
        NopModelPart.PositionTextureVertex lvt_23_1_ = new NopModelPart.PositionTextureVertex(xx, y, zz, 0.0F, 8.0F);
        NopModelPart.PositionTextureVertex lvt_24_1_ = new NopModelPart.PositionTextureVertex(xx, yy, zz, 8.0F, 8.0F);
        NopModelPart.PositionTextureVertex lvt_25_1_ = new NopModelPart.PositionTextureVertex(x, yy, zz, 8.0F, 0.0F);
        NopModelPart.ModelBox box = (NopModelPart.ModelBox) this.cubes.get(this.cubes.size() - 1);
        if (pos == Direction.EAST) {
            box.polygons = new NopModelPart.TexturedQuad[] { new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { lvt_23_1_, lvt_19_1_, lvt_20_1_, lvt_24_1_ }, (float) this.xTexOffs, (float) this.yTexOffs, (float) (this.xTexOffs + dz), (float) (this.yTexOffs + dy), this.xTexSize, this.yTexSize, this.mirror, Direction.WEST) };
        }
        if (pos == Direction.DOWN) {
            box.polygons = new NopModelPart.TexturedQuad[] { new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { lvt_23_1_, lvt_22_1_, lvt_18_2_, lvt_19_1_ }, (float) this.xTexOffs, (float) this.yTexOffs, (float) (this.xTexOffs + dx), (float) (this.yTexOffs + dz), this.xTexSize, this.yTexSize, this.mirror, Direction.DOWN) };
        }
        if (pos == Direction.NORTH) {
            box.polygons = new NopModelPart.TexturedQuad[] { new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { lvt_19_1_, lvt_18_2_, lvt_21_1_, lvt_20_1_ }, (float) this.xTexOffs, (float) this.yTexOffs, (float) (this.xTexOffs + dx), (float) (this.yTexOffs + dy), this.xTexSize, this.yTexSize, this.mirror, Direction.NORTH) };
        }
        if (pos == Direction.UP) {
            box.polygons = new NopModelPart.TexturedQuad[] { new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { lvt_20_1_, lvt_21_1_, lvt_25_1_, lvt_24_1_ }, (float) (this.xTexOffs + dx), (float) (this.yTexOffs + dz), (float) this.xTexOffs, (float) this.yTexOffs, this.xTexSize, this.yTexSize, this.mirror, Direction.UP) };
        }
        if (pos == Direction.WEST) {
            box.polygons = new NopModelPart.TexturedQuad[] { new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { lvt_18_2_, lvt_22_1_, lvt_25_1_, lvt_21_1_ }, (float) this.xTexOffs, (float) this.yTexOffs, (float) (this.xTexOffs + dz), (float) (this.yTexOffs + dy), this.xTexSize, this.yTexSize, this.mirror, Direction.WEST) };
        }
        if (pos == Direction.SOUTH) {
            box.polygons = new NopModelPart.TexturedQuad[] { new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { lvt_22_1_, lvt_23_1_, lvt_24_1_, lvt_25_1_ }, (float) this.xTexOffs, (float) this.yTexOffs, (float) (this.xTexOffs + dx), (float) (this.yTexOffs + dy), this.xTexSize, this.yTexSize, this.mirror, Direction.SOUTH) };
        }
    }
}