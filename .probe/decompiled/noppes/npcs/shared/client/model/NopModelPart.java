package noppes.npcs.shared.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Random;
import java.util.UUID;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.shared.common.util.NopVector3f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class NopModelPart {

    public float xTexSize = 64.0F;

    public float yTexSize = 32.0F;

    public int xTexOffs;

    public int yTexOffs;

    public float x;

    public float y;

    public float z;

    public float xRot;

    public float yRot;

    public float zRot;

    public boolean mirror;

    public boolean visible = true;

    public final ObjectList<NopModelPart.ModelBox> cubes = new ObjectArrayList();

    public final Object2ObjectArrayMap<String, NopModelPart> children = new Object2ObjectArrayMap();

    public NopVector3f scale = NopVector3f.ONE;

    public NopModelPart(int p_i225949_1_, int p_i225949_2_, int p_i225949_3_, int p_i225949_4_) {
        this.setTexSize(p_i225949_1_, p_i225949_2_);
        this.texOffs(p_i225949_3_, p_i225949_4_);
    }

    public NopModelPart(int p_i225949_1_, int p_i225949_2_) {
        this.setTexSize(p_i225949_1_, p_i225949_2_);
    }

    private NopModelPart() {
    }

    public NopModelPart createShallowCopy() {
        NopModelPart MpmModelPart = new NopModelPart();
        MpmModelPart.copyFrom(this);
        return MpmModelPart;
    }

    public void copyFrom(NopModelPart p_217177_1_) {
        this.xRot = p_217177_1_.xRot;
        this.yRot = p_217177_1_.yRot;
        this.zRot = p_217177_1_.zRot;
        this.x = p_217177_1_.x;
        this.y = p_217177_1_.y;
        this.z = p_217177_1_.z;
    }

    public void addChild(String name, NopModelPart p_78792_1_) {
        this.children.put(name, p_78792_1_);
    }

    public void addChild(NopModelPart p_78792_1_) {
        this.children.put(UUID.randomUUID().toString(), p_78792_1_);
    }

    public NopModelPart texOffs(int p_78784_1_, int p_78784_2_) {
        this.xTexOffs = p_78784_1_;
        this.yTexOffs = p_78784_2_;
        return this;
    }

    public NopModelPart addBox(String p_217178_1_, float p_217178_2_, float p_217178_3_, float p_217178_4_, int p_217178_5_, int p_217178_6_, int p_217178_7_, float p_217178_8_, int p_217178_9_, int p_217178_10_) {
        this.texOffs(p_217178_9_, p_217178_10_);
        this.addBox(this.xTexOffs, this.yTexOffs, p_217178_2_, p_217178_3_, p_217178_4_, (float) p_217178_5_, (float) p_217178_6_, (float) p_217178_7_, p_217178_8_, p_217178_8_, p_217178_8_, this.mirror, false);
        return this;
    }

    public NopModelPart addBox(float p_228300_1_, float p_228300_2_, float p_228300_3_, float p_228300_4_, float p_228300_5_, float p_228300_6_) {
        this.addBox(this.xTexOffs, this.yTexOffs, p_228300_1_, p_228300_2_, p_228300_3_, p_228300_4_, p_228300_5_, p_228300_6_, 0.0F, 0.0F, 0.0F, this.mirror, false);
        return this;
    }

    public NopModelPart addBox(float p_228304_1_, float p_228304_2_, float p_228304_3_, float p_228304_4_, float p_228304_5_, float p_228304_6_, boolean p_228304_7_) {
        this.addBox(this.xTexOffs, this.yTexOffs, p_228304_1_, p_228304_2_, p_228304_3_, p_228304_4_, p_228304_5_, p_228304_6_, 0.0F, 0.0F, 0.0F, p_228304_7_, false);
        return this;
    }

    public void addBox(float p_228301_1_, float p_228301_2_, float p_228301_3_, float p_228301_4_, float p_228301_5_, float p_228301_6_, float p_228301_7_) {
        this.addBox(this.xTexOffs, this.yTexOffs, p_228301_1_, p_228301_2_, p_228301_3_, p_228301_4_, p_228301_5_, p_228301_6_, p_228301_7_, p_228301_7_, p_228301_7_, this.mirror, false);
    }

    public void addBox(float p_228302_1_, float p_228302_2_, float p_228302_3_, float p_228302_4_, float p_228302_5_, float p_228302_6_, float p_228302_7_, float p_228302_8_, float p_228302_9_) {
        this.addBox(this.xTexOffs, this.yTexOffs, p_228302_1_, p_228302_2_, p_228302_3_, p_228302_4_, p_228302_5_, p_228302_6_, p_228302_7_, p_228302_8_, p_228302_9_, this.mirror, false);
    }

    public void addBox(float p_228303_1_, float p_228303_2_, float p_228303_3_, float p_228303_4_, float p_228303_5_, float p_228303_6_, float p_228303_7_, boolean p_228303_8_) {
        this.addBox(this.xTexOffs, this.yTexOffs, p_228303_1_, p_228303_2_, p_228303_3_, p_228303_4_, p_228303_5_, p_228303_6_, p_228303_7_, p_228303_7_, p_228303_7_, p_228303_8_, false);
    }

    private void addBox(int p_228305_1_, int p_228305_2_, float p_228305_3_, float p_228305_4_, float p_228305_5_, float p_228305_6_, float p_228305_7_, float p_228305_8_, float p_228305_9_, float p_228305_10_, float p_228305_11_, boolean p_228305_12_, boolean p_228305_13_) {
        this.cubes.add(new NopModelPart.ModelBox(p_228305_1_, p_228305_2_, p_228305_3_, p_228305_4_, p_228305_5_, p_228305_6_, p_228305_7_, p_228305_8_, p_228305_9_, p_228305_10_, p_228305_11_, p_228305_12_, this.xTexSize, this.yTexSize));
    }

    public void setPos(float p_78793_1_, float p_78793_2_, float p_78793_3_) {
        this.x = p_78793_1_;
        this.y = p_78793_2_;
        this.z = p_78793_3_;
    }

    public void setRotation(NopModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }

    public NopModelPart setRotation(NopVector3f rotate) {
        this.xRot = rotate.x;
        this.yRot = rotate.y;
        this.zRot = rotate.z;
        return this;
    }

    public NopModelPart setPos(NopVector3f pos) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
        return this;
    }

    public void render(PoseStack p_228308_1_, VertexConsumer p_228308_2_, int p_228308_3_, int p_228308_4_) {
        this.render(p_228308_1_, p_228308_2_, p_228308_3_, p_228308_4_, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack p_228309_1_, VertexConsumer p_228309_2_, int p_228309_3_, int p_228309_4_, float p_228309_5_, float p_228309_6_, float p_228309_7_, float p_228309_8_) {
        if (this.visible && (!this.cubes.isEmpty() || !this.children.isEmpty())) {
            p_228309_1_.pushPose();
            this.translateAndRotate(p_228309_1_);
            this.compile(p_228309_1_.last(), p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
            ObjectIterator var9 = this.children.values().iterator();
            while (var9.hasNext()) {
                NopModelPart MpmModelPart = (NopModelPart) var9.next();
                MpmModelPart.render(p_228309_1_, p_228309_2_, p_228309_3_, p_228309_4_, p_228309_5_, p_228309_6_, p_228309_7_, p_228309_8_);
            }
            p_228309_1_.popPose();
        }
    }

    public void translateAndRotate(PoseStack p_228307_1_) {
        p_228307_1_.translate((double) (this.x / 16.0F), (double) (this.y / 16.0F), (double) (this.z / 16.0F));
        if (this.zRot != 0.0F) {
            p_228307_1_.mulPose(Axis.ZP.rotation(this.zRot));
        }
        if (this.yRot != 0.0F) {
            p_228307_1_.mulPose(Axis.YP.rotation(this.yRot));
        }
        if (this.xRot != 0.0F) {
            p_228307_1_.mulPose(Axis.XP.rotation(this.xRot));
        }
        p_228307_1_.scale(this.scale.x, this.scale.y, this.scale.z);
    }

    private void compile(PoseStack.Pose p_228306_1_, VertexConsumer p_228306_2_, int p_228306_3_, int p_228306_4_, float p_228306_5_, float p_228306_6_, float p_228306_7_, float p_228306_8_) {
        Matrix4f matrix4f = p_228306_1_.pose();
        Matrix3f matrix3f = p_228306_1_.normal();
        ObjectListIterator var11 = this.cubes.iterator();
        while (var11.hasNext()) {
            NopModelPart.ModelBox MpmModelPart$modelbox = (NopModelPart.ModelBox) var11.next();
            for (NopModelPart.TexturedQuad MpmModelPart$texturedquad : MpmModelPart$modelbox.polygons) {
                Vector3f vector3f = new Vector3f(MpmModelPart$texturedquad.normal.x, MpmModelPart$texturedquad.normal.y, MpmModelPart$texturedquad.normal.z);
                vector3f.mul(matrix3f);
                float f = vector3f.x();
                float f1 = vector3f.y();
                float f2 = vector3f.z();
                for (int i = 0; i < 4; i++) {
                    NopModelPart.PositionTextureVertex MpmModelPart$positiontexturevertex = MpmModelPart$texturedquad.vertices[i];
                    float f3 = MpmModelPart$positiontexturevertex.pos.x() / 16.0F;
                    float f4 = MpmModelPart$positiontexturevertex.pos.y() / 16.0F;
                    float f5 = MpmModelPart$positiontexturevertex.pos.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
                    vector4f.mul(matrix4f);
                    p_228306_2_.vertex(vector4f.x(), vector4f.y(), vector4f.z(), p_228306_5_, p_228306_6_, p_228306_7_, p_228306_8_, MpmModelPart$positiontexturevertex.u, MpmModelPart$positiontexturevertex.v, p_228306_4_, p_228306_3_, f, f1, f2);
                }
            }
        }
    }

    public NopModelPart setTexSize(int p_78787_1_, int p_78787_2_) {
        this.xTexSize = (float) p_78787_1_;
        this.yTexSize = (float) p_78787_2_;
        return this;
    }

    public NopModelPart.ModelBox getRandomCube(Random p_228310_1_) {
        return (NopModelPart.ModelBox) this.cubes.get(p_228310_1_.nextInt(this.cubes.size()));
    }

    @OnlyIn(Dist.CLIENT)
    public static class ModelBox {

        public NopModelPart.TexturedQuad[] polygons;

        public float minX;

        public float minY;

        public float minZ;

        public float maxX;

        public float maxY;

        public float maxZ;

        public ModelBox(int p_i225950_1_, int p_i225950_2_, float p_i225950_3_, float p_i225950_4_, float p_i225950_5_, float p_i225950_6_, float p_i225950_7_, float p_i225950_8_, float p_i225950_9_, float p_i225950_10_, float p_i225950_11_, boolean p_i225950_12_, float p_i225950_13_, float p_i225950_14_) {
            this.minX = p_i225950_3_;
            this.minY = p_i225950_4_;
            this.minZ = p_i225950_5_;
            this.maxX = p_i225950_3_ + p_i225950_6_;
            this.maxY = p_i225950_4_ + p_i225950_7_;
            this.maxZ = p_i225950_5_ + p_i225950_8_;
            this.polygons = new NopModelPart.TexturedQuad[6];
            float f = p_i225950_3_ + p_i225950_6_;
            float f1 = p_i225950_4_ + p_i225950_7_;
            float f2 = p_i225950_5_ + p_i225950_8_;
            p_i225950_3_ -= p_i225950_9_;
            p_i225950_4_ -= p_i225950_10_;
            p_i225950_5_ -= p_i225950_11_;
            f += p_i225950_9_;
            f1 += p_i225950_10_;
            f2 += p_i225950_11_;
            if (p_i225950_12_) {
                float f3 = f;
                f = p_i225950_3_;
                p_i225950_3_ = f3;
            }
            NopModelPart.PositionTextureVertex MpmModelPart$positiontexturevertex7 = new NopModelPart.PositionTextureVertex(p_i225950_3_, p_i225950_4_, p_i225950_5_, 0.0F, 0.0F);
            NopModelPart.PositionTextureVertex MpmModelPart$positiontexturevertex = new NopModelPart.PositionTextureVertex(f, p_i225950_4_, p_i225950_5_, 0.0F, 8.0F);
            NopModelPart.PositionTextureVertex MpmModelPart$positiontexturevertex1 = new NopModelPart.PositionTextureVertex(f, f1, p_i225950_5_, 8.0F, 8.0F);
            NopModelPart.PositionTextureVertex MpmModelPart$positiontexturevertex2 = new NopModelPart.PositionTextureVertex(p_i225950_3_, f1, p_i225950_5_, 8.0F, 0.0F);
            NopModelPart.PositionTextureVertex MpmModelPart$positiontexturevertex3 = new NopModelPart.PositionTextureVertex(p_i225950_3_, p_i225950_4_, f2, 0.0F, 0.0F);
            NopModelPart.PositionTextureVertex MpmModelPart$positiontexturevertex4 = new NopModelPart.PositionTextureVertex(f, p_i225950_4_, f2, 0.0F, 8.0F);
            NopModelPart.PositionTextureVertex MpmModelPart$positiontexturevertex5 = new NopModelPart.PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
            NopModelPart.PositionTextureVertex MpmModelPart$positiontexturevertex6 = new NopModelPart.PositionTextureVertex(p_i225950_3_, f1, f2, 8.0F, 0.0F);
            float f4 = (float) p_i225950_1_;
            float f5 = (float) p_i225950_1_ + p_i225950_8_;
            float f6 = (float) p_i225950_1_ + p_i225950_8_ + p_i225950_6_;
            float f7 = (float) p_i225950_1_ + p_i225950_8_ + p_i225950_6_ + p_i225950_6_;
            float f8 = (float) p_i225950_1_ + p_i225950_8_ + p_i225950_6_ + p_i225950_8_;
            float f9 = (float) p_i225950_1_ + p_i225950_8_ + p_i225950_6_ + p_i225950_8_ + p_i225950_6_;
            float f10 = (float) p_i225950_2_;
            float f11 = (float) p_i225950_2_ + p_i225950_8_;
            float f12 = (float) p_i225950_2_ + p_i225950_8_ + p_i225950_7_;
            this.polygons[2] = new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { MpmModelPart$positiontexturevertex4, MpmModelPart$positiontexturevertex3, MpmModelPart$positiontexturevertex7, MpmModelPart$positiontexturevertex }, f5, f10, f6, f11, p_i225950_13_, p_i225950_14_, p_i225950_12_, Direction.DOWN);
            this.polygons[3] = new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { MpmModelPart$positiontexturevertex1, MpmModelPart$positiontexturevertex2, MpmModelPart$positiontexturevertex6, MpmModelPart$positiontexturevertex5 }, f6, f11, f7, f10, p_i225950_13_, p_i225950_14_, p_i225950_12_, Direction.UP);
            this.polygons[1] = new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { MpmModelPart$positiontexturevertex7, MpmModelPart$positiontexturevertex3, MpmModelPart$positiontexturevertex6, MpmModelPart$positiontexturevertex2 }, f4, f11, f5, f12, p_i225950_13_, p_i225950_14_, p_i225950_12_, Direction.WEST);
            this.polygons[4] = new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { MpmModelPart$positiontexturevertex, MpmModelPart$positiontexturevertex7, MpmModelPart$positiontexturevertex2, MpmModelPart$positiontexturevertex1 }, f5, f11, f6, f12, p_i225950_13_, p_i225950_14_, p_i225950_12_, Direction.NORTH);
            this.polygons[0] = new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { MpmModelPart$positiontexturevertex4, MpmModelPart$positiontexturevertex, MpmModelPart$positiontexturevertex1, MpmModelPart$positiontexturevertex5 }, f6, f11, f8, f12, p_i225950_13_, p_i225950_14_, p_i225950_12_, Direction.EAST);
            this.polygons[5] = new NopModelPart.TexturedQuad(new NopModelPart.PositionTextureVertex[] { MpmModelPart$positiontexturevertex3, MpmModelPart$positiontexturevertex4, MpmModelPart$positiontexturevertex5, MpmModelPart$positiontexturevertex6 }, f8, f11, f9, f12, p_i225950_13_, p_i225950_14_, p_i225950_12_, Direction.SOUTH);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class PositionTextureVertex {

        public final Vector3f pos;

        public final float u;

        public final float v;

        public PositionTextureVertex(float p_i1158_1_, float p_i1158_2_, float p_i1158_3_, float p_i1158_4_, float p_i1158_5_) {
            this(new Vector3f(p_i1158_1_, p_i1158_2_, p_i1158_3_), p_i1158_4_, p_i1158_5_);
        }

        public NopModelPart.PositionTextureVertex remap(float p_78240_1_, float p_78240_2_) {
            return new NopModelPart.PositionTextureVertex(this.pos, p_78240_1_, p_78240_2_);
        }

        public PositionTextureVertex(Vector3f p_i225952_1_, float p_i225952_2_, float p_i225952_3_) {
            this.pos = p_i225952_1_;
            this.u = p_i225952_2_;
            this.v = p_i225952_3_;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class TexturedQuad {

        public final NopModelPart.PositionTextureVertex[] vertices;

        public final Vector3f normal;

        public TexturedQuad(NopModelPart.PositionTextureVertex[] p_i225951_1_, float p_i225951_2_, float p_i225951_3_, float p_i225951_4_, float p_i225951_5_, float p_i225951_6_, float p_i225951_7_, boolean p_i225951_8_, Direction p_i225951_9_) {
            this.vertices = p_i225951_1_;
            float f = 0.0F / p_i225951_6_;
            float f1 = 0.0F / p_i225951_7_;
            p_i225951_1_[0] = p_i225951_1_[0].remap(p_i225951_4_ / p_i225951_6_ - f, p_i225951_3_ / p_i225951_7_ + f1);
            p_i225951_1_[1] = p_i225951_1_[1].remap(p_i225951_2_ / p_i225951_6_ + f, p_i225951_3_ / p_i225951_7_ + f1);
            p_i225951_1_[2] = p_i225951_1_[2].remap(p_i225951_2_ / p_i225951_6_ + f, p_i225951_5_ / p_i225951_7_ - f1);
            p_i225951_1_[3] = p_i225951_1_[3].remap(p_i225951_4_ / p_i225951_6_ - f, p_i225951_5_ / p_i225951_7_ - f1);
            if (p_i225951_8_) {
                int i = p_i225951_1_.length;
                for (int j = 0; j < i / 2; j++) {
                    NopModelPart.PositionTextureVertex MpmModelPart$positiontexturevertex = p_i225951_1_[j];
                    p_i225951_1_[j] = p_i225951_1_[i - 1 - j];
                    p_i225951_1_[i - 1 - j] = MpmModelPart$positiontexturevertex;
                }
            }
            this.normal = p_i225951_9_.step();
            if (p_i225951_8_) {
                this.normal.mul(-1.0F, 1.0F, 1.0F);
            }
        }
    }
}