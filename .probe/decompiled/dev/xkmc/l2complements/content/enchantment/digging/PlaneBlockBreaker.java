package dev.xkmc.l2complements.content.enchantment.digging;

public record PlaneBlockBreaker(int radius) implements SimpleNumberDesc {

    @Override
    public BlockBreakerInstance getInstance(DiggerContext ctx) {
        int r = this.radius + ctx.level() - 1;
        int x = r * (1 - Math.abs(ctx.dire().getStepX()));
        int y = r * (1 - Math.abs(ctx.dire().getStepY()));
        int z = r * (1 - Math.abs(ctx.dire().getStepZ()));
        return new RectInstance(-x, x, -y, y, -z, z);
    }

    @Override
    public int range(int lv) {
        return (this.radius + lv - 1) * 2 + 1;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}