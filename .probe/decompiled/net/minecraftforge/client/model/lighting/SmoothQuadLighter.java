package net.minecraftforge.client.model.lighting;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;

public class SmoothQuadLighter extends QuadLighter {

    private static final Direction[] SIDES = Direction.values();

    private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    private final boolean[][][] t = new boolean[3][3][3];

    private final int[][][] s = new int[3][3][3];

    private final int[][][] b = new int[3][3][3];

    private final float[][][][] skyLight = new float[3][2][2][2];

    private final float[][][][] blockLight = new float[3][2][2][2];

    private final float[][][] ao = new float[3][3][3];

    public SmoothQuadLighter(BlockColors colors) {
        super(colors);
    }

    @Override
    protected void computeLightingAt(BlockAndTintGetter level, BlockPos origin, BlockState state) {
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = 0; z <= 2; z++) {
                    this.pos.setWithOffset(origin, x - 1, y - 1, z - 1);
                    BlockState neighborState = level.m_8055_(this.pos);
                    this.t[x][y][z] = neighborState.m_60739_(level, this.pos) < 15;
                    int brightness = LevelRenderer.getLightColor(level, neighborState, this.pos);
                    this.s[x][y][z] = LightTexture.sky(brightness);
                    this.b[x][y][z] = LightTexture.block(brightness);
                    this.ao[x][y][z] = neighborState.m_60792_(level, this.pos);
                }
            }
        }
        for (Direction side : SIDES) {
            this.pos.setWithOffset(origin, side);
            BlockState neighborState = level.m_8055_(this.pos);
            BlockState thisStateShape = state.m_60815_() && state.m_60787_() ? state : Blocks.AIR.defaultBlockState();
            BlockState otherStateShape = neighborState.m_60815_() && neighborState.m_60787_() ? neighborState : Blocks.AIR.defaultBlockState();
            if (neighborState.m_60739_(level, this.pos) == 15 || Shapes.faceShapeOccludes(thisStateShape.m_60655_(level, origin, side), otherStateShape.m_60655_(level, this.pos, side.getOpposite()))) {
                int x = side.getStepX() + 1;
                int y = side.getStepY() + 1;
                int z = side.getStepZ() + 1;
                this.s[x][y][z] = Math.max(this.s[1][1][1] - 1, this.s[x][y][z]);
                this.b[x][y][z] = Math.max(this.b[1][1][1] - 1, this.b[x][y][z]);
            }
        }
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    int x1 = x * 2;
                    int y1 = y * 2;
                    int z1 = z * 2;
                    int sxyz = this.s[x1][y1][z1];
                    int bxyz = this.b[x1][y1][z1];
                    boolean txyz = this.t[x1][y1][z1];
                    int sxz = this.s[x1][1][z1];
                    int sxy = this.s[x1][y1][1];
                    int syz = this.s[1][y1][z1];
                    int bxz = this.b[x1][1][z1];
                    int bxy = this.b[x1][y1][1];
                    int byz = this.b[1][y1][z1];
                    boolean txz = this.t[x1][1][z1];
                    boolean txy = this.t[x1][y1][1];
                    boolean tyz = this.t[1][y1][z1];
                    int sx = this.s[x1][1][1];
                    int sy = this.s[1][y1][1];
                    int sz = this.s[1][1][z1];
                    int bx = this.b[x1][1][1];
                    int by = this.b[1][y1][1];
                    int bz = this.b[1][1][z1];
                    boolean tx = this.t[x1][1][1];
                    boolean ty = this.t[1][y1][1];
                    boolean tz = this.t[1][1][z1];
                    this.skyLight[0][x][y][z] = this.combine(sx, sxz, sxy, !txz && !txy ? sx : sxyz, tx, txz, txy, !txz && !txy ? tx : txyz);
                    this.blockLight[0][x][y][z] = this.combine(bx, bxz, bxy, !txz && !txy ? bx : bxyz, tx, txz, txy, !txz && !txy ? tx : txyz);
                    this.skyLight[1][x][y][z] = this.combine(sy, sxy, syz, !txy && !tyz ? sy : sxyz, ty, txy, tyz, !txy && !tyz ? ty : txyz);
                    this.blockLight[1][x][y][z] = this.combine(by, bxy, byz, !txy && !tyz ? by : bxyz, ty, txy, tyz, !txy && !tyz ? ty : txyz);
                    this.skyLight[2][x][y][z] = this.combine(sz, syz, sxz, !tyz && !txz ? sz : sxyz, tz, tyz, txz, !tyz && !txz ? tz : txyz);
                    this.blockLight[2][x][y][z] = this.combine(bz, byz, bxz, !tyz && !txz ? bz : bxyz, tz, tyz, txz, !tyz && !txz ? tz : txyz);
                }
            }
        }
    }

    @Override
    protected float calculateBrightness(float[] position) {
        float x = position[0];
        float y = position[1];
        float z = position[2];
        int sx = x < 0.0F ? 1 : 2;
        int sy = y < 0.0F ? 1 : 2;
        int sz = z < 0.0F ? 1 : 2;
        if (x < 0.0F) {
            x++;
        }
        if (y < 0.0F) {
            y++;
        }
        if (z < 0.0F) {
            z++;
        }
        float a = 0.0F;
        a += this.ao[sx - 1][sy - 1][sz - 1] * (1.0F - x) * (1.0F - y) * (1.0F - z);
        a += this.ao[sx - 1][sy - 1][sz - 0] * (1.0F - x) * (1.0F - y) * (0.0F + z);
        a += this.ao[sx - 1][sy - 0][sz - 1] * (1.0F - x) * (0.0F + y) * (1.0F - z);
        a += this.ao[sx - 1][sy - 0][sz - 0] * (1.0F - x) * (0.0F + y) * (0.0F + z);
        a += this.ao[sx - 0][sy - 1][sz - 1] * (0.0F + x) * (1.0F - y) * (1.0F - z);
        a += this.ao[sx - 0][sy - 1][sz - 0] * (0.0F + x) * (1.0F - y) * (0.0F + z);
        a += this.ao[sx - 0][sy - 0][sz - 1] * (0.0F + x) * (0.0F + y) * (1.0F - z);
        a += this.ao[sx - 0][sy - 0][sz - 0] * (0.0F + x) * (0.0F + y) * (0.0F + z);
        return Mth.clamp(a, 0.0F, 1.0F);
    }

    @Override
    protected int calculateLightmap(float[] position, byte[] normal) {
        int block = (int) (this.calcLightmap(this.blockLight, position[0], position[1], position[2]) * 240.0F);
        int sky = (int) (this.calcLightmap(this.skyLight, position[0], position[1], position[2]) * 240.0F);
        return block | sky << 16;
    }

    private float combine(int c, int s1, int s2, int s3, boolean t0, boolean t1, boolean t2, boolean t3) {
        if (c == 0 && !t0) {
            c = Math.max(0, Math.max(s1, s2) - 1);
        }
        if (s1 == 0 && !t1) {
            s1 = Math.max(0, c - 1);
        }
        if (s2 == 0 && !t2) {
            s2 = Math.max(0, c - 1);
        }
        if (s3 == 0 && !t3) {
            s3 = Math.max(0, Math.max(s1, s2) - 1);
        }
        return (float) (c + s1 + s2 + s3) / 60.0F;
    }

    protected float calcLightmap(float[][][][] light, float x, float y, float z) {
        x *= 2.0F;
        y *= 2.0F;
        z *= 2.0F;
        float l2 = x * x + y * y + z * z;
        if (l2 > 5.98F) {
            float s = (float) Math.sqrt((double) (5.98F / l2));
            x *= s;
            y *= s;
            z *= s;
        }
        float ax = x > 0.0F ? x : -x;
        float ay = y > 0.0F ? y : -y;
        float az = z > 0.0F ? z : -z;
        float e1 = 1.0001F;
        if (ax > 1.9999F && ay <= e1 && az <= e1) {
            x = x < 0.0F ? -1.9999F : 1.9999F;
        } else if (ay > 1.9999F && az <= e1 && ax <= e1) {
            y = y < 0.0F ? -1.9999F : 1.9999F;
        } else if (az > 1.9999F && ax <= e1 && ay <= e1) {
            z = z < 0.0F ? -1.9999F : 1.9999F;
        }
        ax = x > 0.0F ? x : -x;
        ay = y > 0.0F ? y : -y;
        az = z > 0.0F ? z : -z;
        if (ax <= e1 && ay + az > 2.9999F) {
            float s = 2.9999F / (ay + az);
            y *= s;
            z *= s;
        } else if (ay <= e1 && az + ax > 2.9999F) {
            float s = 2.9999F / (az + ax);
            z *= s;
            x *= s;
        } else if (az <= e1 && ax + ay > 2.9999F) {
            float s = 2.9999F / (ax + ay);
            x *= s;
            y *= s;
        } else if (ax + ay + az > 3.9999F) {
            float s = 3.9999F / (ax + ay + az);
            x *= s;
            y *= s;
            z *= s;
        }
        float l = 0.0F;
        float s = 0.0F;
        for (int ix = 0; ix <= 1; ix++) {
            for (int iy = 0; iy <= 1; iy++) {
                for (int iz = 0; iz <= 1; iz++) {
                    float vx = x * (float) (1 - ix * 2);
                    float vy = y * (float) (1 - iy * 2);
                    float vz = z * (float) (1 - iz * 2);
                    float s3 = vx + vy + vz + 4.0F;
                    float sx = vy + vz + 3.0F;
                    float sy = vz + vx + 3.0F;
                    float sz = vx + vy + 3.0F;
                    float bx = (2.0F * vx + vy + vz + 6.0F) / (s3 * sy * sz * (vx + 2.0F));
                    s += bx;
                    l += bx * light[0][ix][iy][iz];
                    float by = (2.0F * vy + vz + vx + 6.0F) / (s3 * sz * sx * (vy + 2.0F));
                    s += by;
                    l += by * light[1][ix][iy][iz];
                    float bz = (2.0F * vz + vx + vy + 6.0F) / (s3 * sx * sy * (vz + 2.0F));
                    s += bz;
                    l += bz * light[2][ix][iy][iz];
                }
            }
        }
        l /= s;
        return Mth.clamp(l, 0.0F, 1.0F);
    }
}