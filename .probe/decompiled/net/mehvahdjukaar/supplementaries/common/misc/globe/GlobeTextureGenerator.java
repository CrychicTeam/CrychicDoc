package net.mehvahdjukaar.supplementaries.common.misc.globe;

import java.util.Random;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.minecraft.util.Mth;

public class GlobeTextureGenerator {

    private static final int SIDE = 8;

    private static final int WIDTH = 32;

    private static final int HEIGHT = 16;

    private static final int SCALE = 20;

    private final Random rand;

    private final GlobeTextureGenerator.Pixel[][] pixels;

    public static byte[][] generate(long seed) {
        GlobeTextureGenerator gen = new GlobeTextureGenerator(new Random(seed));
        gen.generateLand();
        gen.applyEffects();
        gen.fixBottomFace();
        return gen.getByteMatrix();
    }

    private GlobeTextureGenerator(Random random) {
        this.rand = random;
        this.pixels = new GlobeTextureGenerator.Pixel[32][16];
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                this.pixels[x][y] = new GlobeTextureGenerator.Pixel(getFace(x, y) == GlobeTextureGenerator.Face.NA);
            }
        }
    }

    public GlobeTextureGenerator.Pixel pfp(GlobeTextureGenerator.Pos p) {
        return this.pixels[p.x][p.y];
    }

    public double dist(double x, double y, double x1, double y1) {
        return (double) Mth.sqrt((float) ((x - x1) * (x - x1) + (y - y1) * (y - y1)));
    }

    public void fixBottomFace() {
        int N = 8;
        GlobeTextureGenerator.Pixel[][] mat = new GlobeTextureGenerator.Pixel[N][N];
        for (int x = 16; x < 24; x++) {
            System.arraycopy(this.pixels[x], 0, mat[x - 16], 0, 8);
        }
        for (int i = 0; i < N / 2; i++) {
            for (int j = 0; j < N; j++) {
                GlobeTextureGenerator.Pixel temp = mat[i][j];
                mat[i][j] = mat[N - i - 1][N - j - 1];
                mat[N - i - 1][N - j - 1] = temp;
            }
        }
        for (int k = 0; k < 8; k++) {
            System.arraycopy(mat[k], 0, this.pixels[k + 16], 0, 8);
        }
    }

    public byte[][] getByteMatrix() {
        byte[][] matrix = new byte[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 3, 2, 2, 2, 3, 3 }, { 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 2, 1, 1, 1, 2, 3 }, { 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 1, 1, 1, 1, 1, 2 }, { 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 2 }, { 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 2 }, { 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 3 }, { 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 2, 1, 1, 2, 2, 3 }, { 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 2, 2, 3, 3, 2 }, { 3, 3, 3, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 3, 3, 3 }, { 3, 2, 1, 1, 1, 2, 2, 3, 3, 2, 1, 1, 1, 1, 2, 3 }, { 3, 2, 1, 1, 1, 1, 2, 2, 3, 1, 1, 1, 1, 1, 2, 3 }, { 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 2 }, { 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 2 }, { 3, 1, 1, 1, 1, 1, 1, 2, 3, 2, 1, 1, 1, 1, 1, 3 }, { 3, 2, 2, 1, 1, 1, 2, 3, 3, 2, 2, 1, 1, 2, 2, 3 }, { 3, 3, 3, 2, 2, 3, 3, 3, 3, 3, 2, 2, 2, 3, 3, 3 }, { 3, 3, 2, 2, 2, 3, 3, 2, 3, 3, 3, 2, 2, 2, 3, 3 }, { 3, 2, 1, 1, 1, 1, 2, 3, 3, 2, 1, 1, 1, 2, 2, 3 }, { 3, 1, 1, 1, 1, 1, 1, 3, 3, 2, 1, 1, 1, 1, 1, 3 }, { 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 2 }, { 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 2 }, { 2, 2, 1, 1, 1, 1, 2, 3, 2, 2, 1, 1, 1, 1, 1, 2 }, { 3, 2, 2, 1, 1, 1, 2, 3, 3, 2, 2, 1, 1, 1, 2, 3 }, { 3, 3, 3, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 3, 3, 3 }, { 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 2, 2, 2, 3, 2 }, { 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 2, 1, 1, 1, 2, 3 }, { 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 1, 1, 1, 1, 1, 2 }, { 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 2 }, { 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 2 }, { 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 1, 1, 1, 1, 2, 3 }, { 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 1, 1, 1, 2, 2, 3 }, { 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 2, 2, 2, 3, 3, 2 } };
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                byte color = this.pixels[x][y].getColor();
                if (color != 0) {
                    matrix[x][y] = (byte) Math.max(color, matrix[x][y]);
                }
            }
        }
        return matrix;
    }

    private static GlobeTextureGenerator.Face getFace(int x, int y) {
        if (y < 8) {
            if (x < 8) {
                return GlobeTextureGenerator.Face.NA;
            } else if (x < 16) {
                return GlobeTextureGenerator.Face.TOP;
            } else {
                return x < 24 ? GlobeTextureGenerator.Face.BOT : GlobeTextureGenerator.Face.NA;
            }
        } else if (x < 8) {
            return GlobeTextureGenerator.Face.F1;
        } else if (x < 16) {
            return GlobeTextureGenerator.Face.F2;
        } else {
            return x < 24 ? GlobeTextureGenerator.Face.F3 : GlobeTextureGenerator.Face.F4;
        }
    }

    public void applyEffects() {
        this.shadeWater();
        this.generateIce();
        this.generateHotBiomes();
        this.shadeTemperateHot();
        this.shadeHot();
        this.shadeTemperateCold();
        this.shadeCold();
        this.coastEffects();
        this.generateMushrooms();
        this.generateIcebergs2();
        if (MiscUtils.FESTIVITY.isChristmas()) {
            this.christmas();
        } else if (MiscUtils.FESTIVITY.isEarthDay()) {
            this.meltice();
        }
    }

    public void meltice() {
        for (GlobeTextureGenerator.Pixel[] pixel : this.pixels) {
            for (GlobeTextureGenerator.Pixel value : pixel) {
                if (value.biome == GlobeTextureGenerator.Biome.COLD) {
                    value.biome = GlobeTextureGenerator.Biome.TEMPERATE;
                }
                if (value.specialFeature == GlobeTextureGenerator.Feature.ICEBERG) {
                    value.specialFeature = GlobeTextureGenerator.Feature.NORMAL;
                }
            }
        }
    }

    public void desertify() {
        for (GlobeTextureGenerator.Pixel[] pixel : this.pixels) {
            for (GlobeTextureGenerator.Pixel value : pixel) {
                if (value.biome == GlobeTextureGenerator.Biome.TEMPERATE) {
                    value.biome = GlobeTextureGenerator.Biome.HOT;
                }
            }
        }
    }

    public void christmas() {
        for (GlobeTextureGenerator.Pixel[] pixel : this.pixels) {
            for (GlobeTextureGenerator.Pixel value : pixel) {
                value.biome = GlobeTextureGenerator.Biome.COLD;
                if (value.specialFeature != GlobeTextureGenerator.Feature.NORMAL) {
                    value.specialFeature = GlobeTextureGenerator.Feature.ICEBERG;
                }
            }
        }
    }

    public void generateMushrooms() {
        int min = 0;
        int additional = 3;
        int count = min + this.rand.nextInt(additional);
        int c = 0;
        while (c < count) {
            int x = this.rand.nextInt(32);
            int y = 8 + this.rand.nextInt(8);
            float p = this.pixels[x][y].isWater() ? 0.9F : 0.1F;
            if (this.rand.nextFloat() < p) {
                c++;
                this.pixels[x][y].specialFeature = GlobeTextureGenerator.Feature.MUSHROOM;
            }
        }
    }

    public void generateIcebergs() {
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                GlobeTextureGenerator.Pos pos = new GlobeTextureGenerator.Pos(x, y);
                GlobeTextureGenerator.Pixel pixel = this.pfp(pos);
                if (pixel.isWater() && (double) this.rand.nextFloat() < 0.005) {
                    this.pixels[x][y].specialFeature = GlobeTextureGenerator.Feature.ICEBERG;
                }
            }
        }
    }

    public void generateIcebergs2() {
        int min = 1;
        int additional = 3;
        int count = min + this.rand.nextInt(additional);
        int c = 0;
        for (int tries = 0; c < count && tries < 1000; tries++) {
            int x = this.rand.nextInt(32);
            int y = 8 + this.rand.nextInt(8);
            GlobeTextureGenerator.Pos p = new GlobeTextureGenerator.Pos(x, y);
            if (this.pixels[x][y].isWater() && this.pfp(p.up()).isWater() && this.pfp(p.down()).isWater() && this.pfp(p.left()).isWater() && this.pfp(p.right()).isWater()) {
                c++;
                this.pixels[x][y].specialFeature = GlobeTextureGenerator.Feature.ICEBERG;
            }
        }
    }

    public void generateHotBiomes() {
        int min = 6;
        int additional = 4;
        int count = min + this.rand.nextInt(additional);
        int c = 0;
        try {
            while (c < count) {
                int x = this.rand.nextInt(32);
                int y = 8 + this.rand.nextInt(8);
                double k = (double) this.rand.nextFloat();
                double p = 0.5 * Math.sin(((double) y - k) * 2.0 * Math.PI / 4.0) + 0.3;
                if ((double) this.rand.nextFloat() < p && this.pixels[x][y].isLand()) {
                    c++;
                    this.setHotBiome(new GlobeTextureGenerator.Pos(x, y), 8);
                }
            }
        } catch (Exception var11) {
            boolean y = true;
        }
    }

    public void setHotBiome(GlobeTextureGenerator.Pos p, int dist) {
        int x = p.x;
        int y = p.y;
        if (dist >= 0 && !this.pixels[x][y].isHot()) {
            int d = dist - this.rand.nextInt(10);
            this.pixels[x][y].biome = GlobeTextureGenerator.Biome.HOT;
            this.setHotBiome(p.up(), d - 2);
            this.setHotBiome(p.down(), d - 2);
            this.setHotBiome(p.left(), d);
            this.setHotBiome(p.right(), d);
        }
    }

    public void genBiomes() {
        int min = 6;
        int additional = 4;
        int c = 0;
        int count = min + this.rand.nextInt(additional);
        while (c < 0) {
            float chance = this.rand.nextFloat();
            if ((double) chance < 0.5) {
                if (this.doGenHot()) {
                    c++;
                }
            } else if ((double) chance < 0.6) {
            }
        }
    }

    public boolean doGenHot() {
        int x = this.rand.nextInt(32);
        int y = 8 + this.rand.nextInt(8);
        double k = (double) this.rand.nextFloat();
        double p = 0.5 * Math.sin(((double) y - k) * 2.0 * Math.PI / 4.0) + 0.3;
        if ((double) this.rand.nextFloat() < p && this.pixels[x][y].isLand()) {
            this.setHotBiome(new GlobeTextureGenerator.Pos(x, y), 8);
            return true;
        } else {
            return false;
        }
    }

    public void generateHot() {
        int j = 4 + this.rand.nextInt(2);
        GlobeTextureGenerator.Pos[] list = new GlobeTextureGenerator.Pos[j];
        for (int i = 0; i < j; i++) {
            list[i] = new GlobeTextureGenerator.Pos(this.rand.nextInt(32), 8 + this.rand.nextInt(8));
        }
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                GlobeTextureGenerator.Pos pos = new GlobeTextureGenerator.Pos(x, y);
                GlobeTextureGenerator.Pixel pixel = this.pfp(pos);
                if (y >= 8) {
                    boolean flag = false;
                    for (int m = 0; m < j; m++) {
                        GlobeTextureGenerator.Pos k = list[m];
                        if (this.dist((double) k.x, (double) k.y, (double) x, (double) y) < (double) (2 + this.rand.nextInt(2))) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        double k = (double) this.rand.nextFloat();
                        double p = 0.5 * Math.sin(((double) y - k) * 2.0 * Math.PI / 4.0) + 0.3;
                        if ((double) this.rand.nextFloat() < p) {
                            this.pixels[x][y].biome = GlobeTextureGenerator.Biome.HOT;
                        }
                    }
                }
            }
        }
    }

    public void shadeCold() {
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                GlobeTextureGenerator.Pos pos = new GlobeTextureGenerator.Pos(x, y);
                GlobeTextureGenerator.Pixel pixel = this.pfp(pos);
                if (pixel.isCold() && pixel.isLand() && y < 8) {
                    double p = 0.2;
                    if (this.pfp(pos.up()).isTemperate()) {
                        p += 0.15;
                    }
                    if (this.pfp(pos.down()).isTemperate()) {
                        p += 0.15;
                    }
                    if (this.pfp(pos.left()).isTemperate()) {
                        p += 0.15;
                    }
                    if (this.pfp(pos.right()).isTemperate()) {
                        p += 0.15;
                    }
                    if ((double) this.rand.nextFloat() < p) {
                        this.pixels[x][y].shaded = true;
                    }
                }
            }
        }
    }

    public void shadeHot() {
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                GlobeTextureGenerator.Pos pos = new GlobeTextureGenerator.Pos(x, y);
                GlobeTextureGenerator.Pixel pixel = this.pfp(pos);
                if (pixel.isHot() && pixel.isLand()) {
                    double p = 0.1;
                    if (this.pfp(pos.up()).isTemperate()) {
                        p += this.pfp(pos.up()).isShaded() ? 0.19 : 0.35;
                    }
                    if (this.pfp(pos.down()).isTemperate()) {
                        p += this.pfp(pos.down()).isShaded() ? 0.19 : 0.35;
                    }
                    if (this.pfp(pos.right()).isTemperate()) {
                        p += this.pfp(pos.right()).isShaded() ? 0.19 : 0.35;
                    }
                    if (this.pfp(pos.left()).isTemperate()) {
                        p += this.pfp(pos.left()).isShaded() ? 0.19 : 0.35;
                    }
                    if ((double) this.rand.nextFloat() < p) {
                        this.pixels[x][y].shaded = true;
                    }
                }
            }
        }
    }

    public void shadeTemperateHot() {
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                GlobeTextureGenerator.Pos pos = new GlobeTextureGenerator.Pos(x, y);
                GlobeTextureGenerator.Pixel pixel = this.pfp(pos);
                if (pixel.isTemperate() && pixel.isLand() && y >= 8) {
                    double p = 0.1;
                    if (this.pfp(pos.up()).isHot()) {
                        p += 0.25;
                    }
                    if (this.pfp(pos.down()).isHot()) {
                        p += 0.25;
                    }
                    if (this.pfp(pos.left()).isHot()) {
                        p += 0.25;
                    }
                    if (this.pfp(pos.right()).isHot()) {
                        p += 0.25;
                    }
                    if ((double) this.rand.nextFloat() < p) {
                        this.pixels[x][y].shaded = true;
                    }
                }
            }
        }
    }

    public void shadeTemperateCold() {
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                GlobeTextureGenerator.Pos pos = new GlobeTextureGenerator.Pos(x, y);
                GlobeTextureGenerator.Pixel pixel = this.pfp(pos);
                if (pixel.isTemperate() && pixel.isLand() && y < 8) {
                    double p = 0.1;
                    if (this.pfp(pos.up()).isCold()) {
                        p += 0.25;
                    }
                    if (this.pfp(pos.down()).isCold()) {
                        p += 0.25;
                    }
                    if (this.pfp(pos.left()).isCold()) {
                        p += 0.25;
                    }
                    if (this.pfp(pos.right()).isCold()) {
                        p += 0.25;
                    }
                    if ((double) this.rand.nextFloat() < p) {
                        this.pixels[x][y].specialFeature = GlobeTextureGenerator.Feature.SUNKEN;
                    }
                }
            }
        }
    }

    public void generateIce() {
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                GlobeTextureGenerator.Pos pos = new GlobeTextureGenerator.Pos(x, y);
                GlobeTextureGenerator.Pixel pixel = this.pfp(pos);
                boolean flag = false;
                double d = this.dist((double) x + 0.5, (double) y + 0.5, 12.0, 4.0);
                if ((double) this.rand.nextFloat() > (d - 0.8) / 2.0) {
                    flag = true;
                }
                double d2 = this.dist((double) x + 0.5, (double) y + 0.5, 20.0, 4.0);
                if ((double) this.rand.nextFloat() > (d2 - 0.8) / 2.0) {
                    flag = true;
                }
                if (flag) {
                    this.pixels[x][y].biome = GlobeTextureGenerator.Biome.COLD;
                    this.pixels[x][y].setLand();
                }
            }
        }
    }

    public void averageOut() {
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                GlobeTextureGenerator.Pos pos = new GlobeTextureGenerator.Pos(x, y);
                GlobeTextureGenerator.Pixel pixel = this.pfp(pos);
                if (y >= 8 && pixel.isLand() && (double) this.rand.nextFloat() > 0.8) {
                    int t = 0;
                    t += this.pfp(pos.up()).getTemp();
                    t += this.pfp(pos.down()).getTemp();
                    t += this.pfp(pos.left()).getTemp();
                    t += this.pfp(pos.right()).getTemp();
                    t += this.pfp(pos.up().left()).getTemp();
                    t += this.pfp(pos.up().right()).getTemp();
                    t += this.pfp(pos.down().left()).getTemp();
                    t += this.pfp(pos.down().right()).getTemp();
                    t += pixel.getTemp();
                    double av = (double) ((float) t / 9.0F);
                    this.setTemperature(x, y, (int) (av + 0.5));
                }
            }
        }
    }

    public void setTemperature(int x, int y, int t) {
        if (t < 2) {
            this.pixels[x][y].biome = GlobeTextureGenerator.Biome.TEMPERATE;
            this.pixels[x][y].shaded = t % 2 != 0;
        } else {
            this.pixels[x][y].biome = GlobeTextureGenerator.Biome.HOT;
            this.pixels[x][y].shaded = t % 2 == 0;
        }
    }

    public void shadeWater() {
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                GlobeTextureGenerator.Pos pos = new GlobeTextureGenerator.Pos(x, y);
                GlobeTextureGenerator.Pixel pixel = this.pfp(pos);
                GlobeTextureGenerator.Pixel p2 = this.pfp(pos.up());
                if (pixel.isWater() && p2.isLand()) {
                    this.pixels[x][y].shaded = true;
                }
            }
        }
    }

    public void coastEffects() {
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                GlobeTextureGenerator.Pos pos = new GlobeTextureGenerator.Pos(x, y);
                GlobeTextureGenerator.Pixel pixel = this.pfp(pos);
                if (pixel.isLand() && (this.pfp(pos.right()).isWater() || this.pfp(pos.up()).isWater() || this.pfp(pos.down()).isWater() || this.pfp(pos.left()).isWater()) && (double) this.rand.nextFloat() > 0.7) {
                    this.pixels[x][y].specialFeature = pixel.biome != GlobeTextureGenerator.Biome.COLD ? GlobeTextureGenerator.Feature.SUNKEN : GlobeTextureGenerator.Feature.ICEBERG;
                }
            }
        }
    }

    public void generateLand() {
        int min = 10;
        int additional = 18;
        int count = min + this.rand.nextInt(additional);
        for (int i = 0; i < count; i++) {
            int x = this.rand.nextInt(32);
            int y = this.rand.nextInt(16);
            this.setLand(new GlobeTextureGenerator.Pos(x, y), 10);
        }
    }

    public void setLand(GlobeTextureGenerator.Pos p, int dist) {
        int x = p.x;
        int y = p.y;
        if (dist >= 0 && !this.pixels[x][y].isLand()) {
            this.pixels[x][y].setLand();
            this.setLand(p.up(), dist - this.rand.nextInt(10));
            this.setLand(p.down(), dist - this.rand.nextInt(10));
            this.setLand(p.left(), dist - this.rand.nextInt(10));
            this.setLand(p.right(), dist - this.rand.nextInt(10));
        }
    }

    private static enum Biome {

        TEMPERATE,
        HOT,
        COLD,
        MUSHROOM,
        MOUNTAIN,
        MESA
    }

    private static class Col {

        public static final byte BLACK = 0;

        public static final byte WATER = 1;

        public static final byte WATER_S = 2;

        public static final byte WATER_D = 3;

        public static final byte SUNKEN = 4;

        public static final byte GREEN = 5;

        public static final byte GREEN_S = 6;

        public static final byte HOT_S = 7;

        public static final byte HOT = 8;

        public static final byte COLD = 9;

        public static final byte COLD_S = 10;

        public static final byte ICEBERG = 11;

        public static final byte MUSHROOM = 12;

        public static final byte MUSHROOM_S = 13;

        public static final byte TAIGA = 14;

        public static final byte MESA = 15;

        public static final byte MESA_S = 16;

        public static final byte MOUNTAIN = 17;

        public static final byte MOUNTAIN_S = 18;
    }

    private static enum Face {

        F1,
        F2,
        F3,
        F4,
        TOP,
        BOT,
        NA
    }

    private static enum Feature {

        NORMAL, SUNKEN, ICEBERG, MUSHROOM
    }

    private static class Pixel {

        private GlobeTextureGenerator.TerrainType terrain = GlobeTextureGenerator.TerrainType.WATER;

        private GlobeTextureGenerator.Biome biome = GlobeTextureGenerator.Biome.TEMPERATE;

        private boolean shaded = false;

        private GlobeTextureGenerator.Feature specialFeature = GlobeTextureGenerator.Feature.NORMAL;

        public Pixel(boolean isnull) {
            if (isnull) {
                this.terrain = GlobeTextureGenerator.TerrainType.NULL;
            }
        }

        public int getTemp() {
            if (this.biome == GlobeTextureGenerator.Biome.TEMPERATE) {
                return this.isShaded() ? 1 : 0;
            } else {
                return this.isShaded() ? 3 : 2;
            }
        }

        public void setLand() {
            this.terrain = GlobeTextureGenerator.TerrainType.LAND;
        }

        public void setWater() {
            this.terrain = GlobeTextureGenerator.TerrainType.WATER;
        }

        public boolean isIceberg() {
            return this.specialFeature == GlobeTextureGenerator.Feature.ICEBERG;
        }

        public boolean isMushroom() {
            return this.specialFeature == GlobeTextureGenerator.Feature.MUSHROOM;
        }

        public boolean isSunken() {
            return this.specialFeature == GlobeTextureGenerator.Feature.SUNKEN;
        }

        public boolean isShaded() {
            return this.shaded;
        }

        public boolean isLand() {
            return this.terrain == GlobeTextureGenerator.TerrainType.LAND;
        }

        public boolean isHot() {
            return this.biome == GlobeTextureGenerator.Biome.HOT;
        }

        public boolean isTemperate() {
            return this.biome == GlobeTextureGenerator.Biome.TEMPERATE;
        }

        public boolean isCold() {
            return this.biome == GlobeTextureGenerator.Biome.COLD;
        }

        public boolean isWater() {
            return this.terrain == GlobeTextureGenerator.TerrainType.WATER;
        }

        public boolean isNull() {
            return this.terrain == GlobeTextureGenerator.TerrainType.NULL;
        }

        public byte getColor() {
            boolean s = this.isShaded();
            if (this.isSunken()) {
                return 4;
            } else if (this.isIceberg()) {
                return 11;
            } else if (this.isMushroom()) {
                return 12;
            } else {
                switch(this.terrain) {
                    case LAND:
                        if (this.isLand()) {
                            return (byte) (switch(this.biome) {
                                case HOT ->
                                    s ? 7 : 8;
                                case COLD ->
                                    s ? 10 : 9;
                                case MUSHROOM ->
                                    s ? 13 : 12;
                                default ->
                                    s ? 6 : 5;
                            });
                        }
                    case WATER:
                        return (byte) (s ? 2 : 1);
                    default:
                        return 0;
                }
            }
        }
    }

    private static record Pos(int x, int y) {

        public GlobeTextureGenerator.Pos up() {
            GlobeTextureGenerator.Face f = GlobeTextureGenerator.getFace(this.x, this.y);
            if (f == GlobeTextureGenerator.Face.NA) {
                return this;
            } else if (this.y == 8) {
                return switch(f) {
                    case F2 ->
                        new GlobeTextureGenerator.Pos(this.x, this.y - 1);
                    case F3 ->
                        new GlobeTextureGenerator.Pos(15, 24 - this.x - 1);
                    case F4 ->
                        new GlobeTextureGenerator.Pos(40 - this.x - 1, 0);
                    default ->
                        new GlobeTextureGenerator.Pos(8, this.x);
                };
            } else if (this.y == 0) {
                return f == GlobeTextureGenerator.Face.TOP ? new GlobeTextureGenerator.Pos(40 - this.x - 1, 8) : new GlobeTextureGenerator.Pos(this.x - 8, 15);
            } else {
                return new GlobeTextureGenerator.Pos(this.x, this.y - 1);
            }
        }

        private GlobeTextureGenerator.Pos down() {
            GlobeTextureGenerator.Face f = GlobeTextureGenerator.getFace(this.x, this.y);
            if (f == GlobeTextureGenerator.Face.NA) {
                return this;
            } else if (this.y == 15) {
                return switch(f) {
                    case F2 ->
                        new GlobeTextureGenerator.Pos(8 + this.x, 0);
                    case F3 ->
                        new GlobeTextureGenerator.Pos(23, this.x - 16);
                    case F4 ->
                        new GlobeTextureGenerator.Pos(48 - this.x - 1, 7);
                    default ->
                        new GlobeTextureGenerator.Pos(16, 8 - this.x - 1);
                };
            } else if (this.y == 7) {
                return f == GlobeTextureGenerator.Face.TOP ? new GlobeTextureGenerator.Pos(this.x, this.y + 1) : new GlobeTextureGenerator.Pos(48 - this.x - 1, 15);
            } else {
                return new GlobeTextureGenerator.Pos(this.x, this.y + 1);
            }
        }

        public GlobeTextureGenerator.Pos left() {
            GlobeTextureGenerator.Face f = GlobeTextureGenerator.getFace(this.x, this.y);
            if (f == GlobeTextureGenerator.Face.NA) {
                return this;
            } else if (this.x == 8 && f == GlobeTextureGenerator.Face.TOP) {
                return new GlobeTextureGenerator.Pos(this.y, 8);
            } else if (this.x == 16 && f == GlobeTextureGenerator.Face.BOT) {
                return new GlobeTextureGenerator.Pos(8 - this.y, 15);
            } else {
                int nx = this.x;
                if (this.x == 0) {
                    nx = 32;
                }
                return new GlobeTextureGenerator.Pos(nx - 1, this.y);
            }
        }

        public GlobeTextureGenerator.Pos right() {
            GlobeTextureGenerator.Face f = GlobeTextureGenerator.getFace(this.x, this.y);
            if (f == GlobeTextureGenerator.Face.NA) {
                return this;
            } else if (this.x == 15 && f == GlobeTextureGenerator.Face.TOP) {
                return new GlobeTextureGenerator.Pos(24 - this.y, 8);
            } else if (this.x == 23 && f == GlobeTextureGenerator.Face.BOT) {
                return new GlobeTextureGenerator.Pos(16 + this.y, 15);
            } else {
                int nx = this.x;
                if (this.x == 31) {
                    nx = -1;
                }
                return new GlobeTextureGenerator.Pos(nx + 1, this.y);
            }
        }
    }

    private static enum TerrainType {

        NULL, LAND, WATER
    }
}