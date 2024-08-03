package dev.xkmc.l2hostility.content.capability.chunk;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;

public class ChunkClearRenderer {

    private final int d;

    private final int greenCol;

    private final int redCol;

    private final int lineCol;

    private final long cx;

    private final long cz;

    private final long cy;

    private final boolean[][][] sections;

    private final boolean inClear;

    private final Matrix4f mat;

    private VertexConsumer cons;

    public static void render(PoseStack pose, Player player, ChunkDifficulty center, float pTick) {
        int r = 7;
        boolean[][][] sections = new boolean[r * 2 + 1][r * 2 + 1][r * 2 + 1];
        int cx = center.chunk.m_7697_().x;
        int cz = center.chunk.m_7697_().z;
        int py = Mth.floor((float) player.m_20188_());
        for (int i = 0; i < r * 2 + 1; i++) {
            for (int j = 0; j < r * 2 + 1; j++) {
                int ix = i - r + cx;
                int iz = j - r + cz;
                ChunkDifficulty ic = (ChunkDifficulty) ChunkDifficulty.at(player.m_9236_(), ix, iz).orElse(null);
                if (ic != null) {
                    for (int k = 0; k < r * 2 + 1; k++) {
                        int iy = py + (k - r) * 16;
                        sections[i][j][k] = !player.m_9236_().m_151562_(iy) && ic.getSection(iy).isCleared();
                    }
                }
            }
        }
        pose.pushPose();
        pose.translate(-Mth.lerp((double) pTick, player.f_19854_, player.m_20185_()), -Mth.lerp((double) pTick, player.f_19855_, player.m_20186_()) - (double) player.m_20192_(), -Mth.lerp((double) pTick, player.f_19856_, player.m_20189_()));
        new ChunkClearRenderer(pose, r, sections, (long) cx, (long) cz, (long) (py >> 4));
        pose.popPose();
    }

    private ChunkClearRenderer(PoseStack pose, int r, boolean[][][] sections, long cx, long cz, long cy) {
        this.mat = pose.last().pose();
        this.d = r * 2 + 1;
        this.sections = sections;
        this.cx = cx - (long) r;
        this.cz = cz - (long) r;
        this.cy = cy - (long) r;
        this.inClear = sections[r][r][r];
        this.lineCol = -1;
        this.greenCol = 520158976;
        this.redCol = 536805376;
        this.render();
    }

    private void render() {
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
        this.cons = source.getBuffer(RenderType.debugQuads());
        ChunkClearRenderer.Edge ex = new ChunkClearRenderer.Edge(1, 0, 0);
        ChunkClearRenderer.Edge ey = new ChunkClearRenderer.Edge(0, 1, 0);
        ChunkClearRenderer.Edge ez = new ChunkClearRenderer.Edge(0, 0, 1);
        for (int x = -1; x <= this.d; x++) {
            for (int z = -1; z <= this.d; z++) {
                for (int y = -1; y <= this.d; y++) {
                    ex.drawFace(x, y, z);
                    ey.drawFace(x, y, z);
                    ez.drawFace(x, y, z);
                }
            }
        }
        this.cons = source.getBuffer(RenderType.debugLineStrip(10.0));
        for (int x = -1; x <= this.d; x++) {
            for (int z = -1; z <= this.d; z++) {
                for (int y = -1; y <= this.d; y++) {
                    ex.drawEdge(x, y, z);
                    ey.drawEdge(x, y, z);
                    ez.drawEdge(x, y, z);
                }
            }
        }
        source.endLastBatch();
    }

    private boolean get(int x, int z, int y) {
        if (x < 0 || x >= this.d) {
            return false;
        } else if (z < 0 || z >= this.d) {
            return false;
        } else {
            return y >= 0 && y < this.d ? this.sections[x][z][y] : false;
        }
    }

    private class Edge {

        private final int dx;

        private final int dy;

        private final int dz;

        private final int ax;

        private final int ay;

        private final int az;

        private final int bx;

        private final int by;

        private final int bz;

        private Edge(int x, int y, int z) {
            this.dx = x;
            this.dy = y;
            this.dz = z;
            this.ax = 1 - this.dx;
            this.ay = 1 - this.ax;
            this.az = 0;
            this.bx = 1 - this.dx - this.ax;
            this.by = 1 - this.dy - this.ay;
            this.bz = 1 - this.dz - this.az;
        }

        private void drawEdge(int x, int y, int z) {
            boolean s = ChunkClearRenderer.this.get(x, z, y);
            boolean a = ChunkClearRenderer.this.get(x + this.ax, z + this.az, y + this.ay);
            boolean b = ChunkClearRenderer.this.get(x + this.bx, z + this.bz, y + this.by);
            boolean ab = ChunkClearRenderer.this.get(x + this.ax + this.bx, z + this.az + this.bz, y + this.ay + this.by);
            if (s || a || b || ab) {
                int fa = 0;
                int fb = 0;
                if (s) {
                    fa++;
                    fb++;
                }
                if (a) {
                    fa--;
                    fb++;
                }
                if (b) {
                    fa++;
                    fb--;
                }
                if (ab) {
                    fa--;
                    fb--;
                }
                this.line(x, y, z, (float) fa, (float) fb);
            }
        }

        private void drawFace(int x, int y, int z) {
            boolean s = ChunkClearRenderer.this.get(x, z, y);
            boolean n = ChunkClearRenderer.this.get(x + this.dx, z + this.dz, y + this.dy);
            if (s != n) {
                int df = s ? 1 : -1;
                this.faceVertex(x + this.dx, y + this.dy, z + this.dz, df);
                this.faceVertex(x + this.dx + this.ax, y + this.dy + this.ay, z + this.dz + this.az, df);
                this.faceVertex(x + 1, y + 1, z + 1, df);
                this.faceVertex(x + this.dx + this.bx, y + this.dy + this.by, z + this.dz + this.bz, df);
            }
        }

        private void faceVertex(int x, int y, int z, int f) {
            int c = ChunkClearRenderer.this.inClear ? ChunkClearRenderer.this.redCol : ChunkClearRenderer.this.greenCol;
            float df = ChunkClearRenderer.this.inClear ? -0.003F : 0.003F;
            float fx = df * (float) f * (float) this.dx;
            float fy = df * (float) f * (float) this.dy;
            float fz = df * (float) f * (float) this.dz;
            long x0 = ChunkClearRenderer.this.cx + (long) x << 4;
            long y0 = ChunkClearRenderer.this.cy + (long) y << 4;
            long z0 = ChunkClearRenderer.this.cz + (long) z << 4;
            this.vertex((float) x0 + fx, (float) y0 + fy, (float) z0 + fz, c);
        }

        private void line(int x, int y, int z, float fa, float fb) {
            float df = ChunkClearRenderer.this.inClear ? -0.003F : 0.003F;
            float fx = df * (fa * (float) this.ax + fb * (float) this.bx);
            float fy = df * (fa * (float) this.ay + fb * (float) this.by);
            float fz = df * (fa * (float) this.az + fb * (float) this.bz);
            long x0 = ChunkClearRenderer.this.cx + (long) x + 1L - (long) this.dx << 4;
            long y0 = ChunkClearRenderer.this.cy + (long) y + 1L - (long) this.dy << 4;
            long z0 = ChunkClearRenderer.this.cz + (long) z + 1L - (long) this.dz << 4;
            long x1 = ChunkClearRenderer.this.cx + (long) x + 1L << 4;
            long y1 = ChunkClearRenderer.this.cy + (long) y + 1L << 4;
            long z1 = ChunkClearRenderer.this.cz + (long) z + 1L << 4;
            this.vertex((float) x0 + fx, (float) y0 + fy, (float) z0 + fz, 0);
            this.vertex((float) x0 + fx, (float) y0 + fy, (float) z0 + fz, ChunkClearRenderer.this.lineCol);
            this.vertex((float) x1 + fx, (float) y1 + fy, (float) z1 + fz, ChunkClearRenderer.this.lineCol);
            this.vertex((float) x1 + fx, (float) y1 + fy, (float) z1 + fz, 0);
        }

        private void vertex(float x, float y, float z, int c) {
            ChunkClearRenderer.this.cons.vertex(ChunkClearRenderer.this.mat, x, y, z).color(c).endVertex();
        }
    }
}