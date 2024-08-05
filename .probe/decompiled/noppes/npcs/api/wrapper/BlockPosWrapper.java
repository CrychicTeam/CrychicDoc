package noppes.npcs.api.wrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import noppes.npcs.api.IPos;

public class BlockPosWrapper implements IPos {

    public static final BlockPosWrapper ZERO = new BlockPosWrapper(BlockPos.ZERO);

    private final BlockPos blockPos;

    public BlockPosWrapper(BlockPos pos) {
        this.blockPos = pos;
    }

    @Override
    public int getX() {
        return this.blockPos.m_123341_();
    }

    @Override
    public int getY() {
        return this.blockPos.m_123342_();
    }

    @Override
    public int getZ() {
        return this.blockPos.m_123343_();
    }

    @Override
    public IPos up() {
        return new BlockPosWrapper(this.blockPos.above());
    }

    @Override
    public IPos up(int n) {
        return new BlockPosWrapper(this.blockPos.above(n));
    }

    @Override
    public IPos down() {
        return new BlockPosWrapper(this.blockPos.below());
    }

    @Override
    public IPos down(int n) {
        return new BlockPosWrapper(this.blockPos.below(n));
    }

    @Override
    public IPos north() {
        return new BlockPosWrapper(this.blockPos.north());
    }

    @Override
    public IPos north(int n) {
        return new BlockPosWrapper(this.blockPos.north(n));
    }

    @Override
    public IPos east() {
        return new BlockPosWrapper(this.blockPos.north());
    }

    @Override
    public IPos east(int n) {
        return new BlockPosWrapper(this.blockPos.north(n));
    }

    @Override
    public IPos south() {
        return new BlockPosWrapper(this.blockPos.north());
    }

    @Override
    public IPos south(int n) {
        return new BlockPosWrapper(this.blockPos.north(n));
    }

    @Override
    public IPos west() {
        return new BlockPosWrapper(this.blockPos.north());
    }

    @Override
    public IPos west(int n) {
        return new BlockPosWrapper(this.blockPos.north(n));
    }

    @Override
    public IPos add(int x, int y, int z) {
        return new BlockPosWrapper(this.blockPos.offset(x, y, z));
    }

    @Override
    public IPos add(IPos pos) {
        return new BlockPosWrapper(this.blockPos.offset(pos.getMCBlockPos()));
    }

    @Override
    public IPos subtract(int x, int y, int z) {
        return new BlockPosWrapper(this.blockPos.offset(-x, -y, -z));
    }

    @Override
    public IPos subtract(IPos pos) {
        return new BlockPosWrapper(this.blockPos.offset(-pos.getX(), -pos.getY(), -pos.getZ()));
    }

    @Override
    public IPos offset(int direction) {
        return new BlockPosWrapper(this.blockPos.relative(Direction.from3DDataValue(direction)));
    }

    @Override
    public IPos offset(int direction, int n) {
        return new BlockPosWrapper(this.blockPos.relative(Direction.from3DDataValue(direction), n));
    }

    @Override
    public BlockPos getMCBlockPos() {
        return this.blockPos;
    }

    @Override
    public double[] normalize() {
        double d = Math.sqrt((double) (this.blockPos.m_123341_() * this.blockPos.m_123341_() + this.blockPos.m_123342_() * this.blockPos.m_123342_() + this.blockPos.m_123343_() * this.blockPos.m_123343_()));
        return new double[] { (double) this.getX() / d, (double) this.getY() / d, (double) this.getZ() / d };
    }

    @Override
    public double distanceTo(IPos pos) {
        double d0 = (double) (this.getX() - pos.getX());
        double d1 = (double) (this.getY() - pos.getY());
        double d2 = (double) (this.getZ() - pos.getZ());
        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }
}