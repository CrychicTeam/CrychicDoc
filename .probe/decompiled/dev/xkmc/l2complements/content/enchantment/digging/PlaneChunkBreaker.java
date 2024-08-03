package dev.xkmc.l2complements.content.enchantment.digging;

import net.minecraft.core.BlockPos;

public record PlaneChunkBreaker(int h) implements SimpleNumberDesc {

    @Override
    public BlockBreakerInstance getInstance(DiggerContext ctx) {
        int d = (this.h << ctx.level() - 1) - 1;
        int r = 15;
        int sx = ctx.dire().getStepX();
        int sy = ctx.dire().getStepY();
        int sz = ctx.dire().getStepZ();
        int x0 = sx > 0 ? -d : 0;
        int x1 = sx == 0 ? r : (sx < 0 ? d : 0);
        int y0 = sy > 0 ? -d : 0;
        int y1 = sy == 0 ? r : (sy < 0 ? d : 0);
        int z0 = sz > 0 ? -d : 0;
        int z1 = sz == 0 ? r : (sz < 0 ? d : 0);
        return new WrappedInstance(pos -> this.modulate(sx, sy, sz, pos), new RectInstance(x0, x1, y0, y1, z0, z1));
    }

    @Override
    public int range(int lv) {
        return this.h << lv - 1;
    }

    private BlockPos modulate(int x, int y, int z, BlockPos pos) {
        int px = x == 0 ? pos.m_123341_() & -16 : pos.m_123341_();
        int py = y == 0 ? pos.m_123342_() & -16 : pos.m_123342_();
        int pz = z == 0 ? pos.m_123343_() & -16 : pos.m_123343_();
        return new BlockPos(px, py, pz);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}