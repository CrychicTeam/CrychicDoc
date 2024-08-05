package journeymap.client.cartography;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicInteger;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import net.minecraft.core.BlockPos;

public class Stratum {

    private static AtomicInteger IDGEN = new AtomicInteger(0);

    private final int id;

    private ChunkMD chunkMd;

    private BlockMD blockMD;

    private int localX;

    private int y;

    private int localZ;

    private int lightLevel;

    private int lightOpacity;

    private boolean isFluid;

    private Integer dayColor;

    private Integer nightColor;

    private Integer caveColor;

    private float worldAmbientLight;

    private boolean worldHasNoSky;

    private boolean uninitialized = true;

    Stratum() {
        this.id = IDGEN.incrementAndGet();
    }

    Stratum set(ChunkMD chunkMd, BlockMD blockMD, int localX, int y, int localZ, Integer lightLevel) {
        if (chunkMd != null && blockMD != null) {
            try {
                this.setChunkMd(chunkMd);
                this.setBlockMD(blockMD);
                this.setX(localX);
                this.setY(y);
                this.setZ(localZ);
                this.setFluid(blockMD.isFluid() || blockMD.isFluid());
                if (blockMD.isLava()) {
                    this.setLightLevel(14);
                } else {
                    this.setLightLevel(lightLevel != null ? lightLevel : chunkMd.getSavedLightValue(localX, y + 1, localZ));
                }
                this.setLightOpacity(chunkMd.getLightOpacity(blockMD, localX, y, localZ));
                this.setDayColor(null);
                this.setNightColor(null);
                this.setCaveColor(null);
                this.uninitialized = false;
                return this;
            } catch (RuntimeException var8) {
                throw var8;
            }
        } else {
            throw new IllegalStateException(String.format("Can't have nulls: %s, %s", chunkMd, blockMD));
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Stratum that = (Stratum) o;
            if (this.getY() != that.getY()) {
                return false;
            } else {
                return this.getBlockMD() != null ? this.getBlockMD().equals(that.getBlockMD()) : that.getBlockMD() == null;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.getBlockMD() != null ? this.getBlockMD().hashCode() : 0;
        return 31 * result + this.getY();
    }

    public String toString() {
        String common = "Stratum{id=" + this.id + ", uninitialized=" + this.uninitialized + "%s}";
        return !this.uninitialized ? String.format(common, ", localX=" + this.getX() + ", y=" + this.getY() + ", localZ=" + this.getZ() + ", lightLevel=" + this.getLightLevel() + ", worldAmbientLight=" + this.getWorldAmbientLight() + ", lightOpacity=" + this.getLightOpacity() + ", isFluid=" + this.isFluid() + ", dayColor=" + (this.getDayColor() == null ? null : new Color(this.getDayColor())) + ", nightColor=" + (this.getNightColor() == null ? null : new Color(this.getNightColor())) + ", caveColor=" + (this.getCaveColor() == null ? null : new Color(this.getCaveColor()))) : String.format(common, "");
    }

    public ChunkMD getChunkMd() {
        return this.chunkMd;
    }

    public void setChunkMd(ChunkMD chunkMd) {
        this.chunkMd = chunkMd;
        if (chunkMd != null) {
            this.worldAmbientLight = chunkMd.getWorld().getSkyDarken(1.0F) * 15.0F;
            this.worldHasNoSky = chunkMd.hasNoSky();
        } else {
            this.worldAmbientLight = 15.0F;
            this.worldHasNoSky = false;
        }
    }

    public BlockMD getBlockMD() {
        if (this.blockMD.isFluid()) {
            boolean var1 = false;
        }
        return this.blockMD;
    }

    public void setBlockMD(BlockMD blockMD) {
        this.blockMD = blockMD;
    }

    public float getWorldAmbientLight() {
        return this.worldAmbientLight;
    }

    public boolean getWorldHasNoSky() {
        return this.worldHasNoSky;
    }

    public int getX() {
        return this.localX;
    }

    public void setX(int x) {
        this.localX = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.localZ;
    }

    public void setZ(int z) {
        this.localZ = z;
    }

    public int getLightLevel() {
        return this.lightLevel;
    }

    public void setLightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
    }

    public int getLightOpacity() {
        return this.lightOpacity;
    }

    public void setLightOpacity(int lightOpacity) {
        this.lightOpacity = lightOpacity;
    }

    public boolean isFluid() {
        return this.isFluid;
    }

    public void setFluid(boolean isFluid) {
        this.isFluid = isFluid;
    }

    public Integer getDayColor() {
        return this.dayColor;
    }

    public void setDayColor(Integer dayColor) {
        this.dayColor = dayColor;
    }

    public Integer getNightColor() {
        return this.nightColor;
    }

    public void setNightColor(Integer nightColor) {
        this.nightColor = nightColor;
    }

    public Integer getCaveColor() {
        return this.caveColor;
    }

    public void setCaveColor(Integer caveColor) {
        this.caveColor = caveColor;
    }

    public BlockPos getBlockPos() {
        return new BlockPos(this.chunkMd.getBlockPos(this.localX, this.y, this.localZ));
    }

    public boolean isUninitialized() {
        return this.uninitialized;
    }

    public void clear() {
        this.uninitialized = true;
        this.worldAmbientLight = 15.0F;
        this.worldHasNoSky = false;
        this.setChunkMd(null);
        this.setBlockMD(null);
        this.setX(0);
        this.setY(-1);
        this.setZ(0);
        this.setFluid(false);
        this.setLightLevel(-1);
        this.setLightOpacity(-1);
        this.setDayColor(null);
        this.setNightColor(null);
        this.setCaveColor(null);
    }
}