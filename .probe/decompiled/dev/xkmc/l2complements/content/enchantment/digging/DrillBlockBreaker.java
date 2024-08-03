package dev.xkmc.l2complements.content.enchantment.digging;

public record DrillBlockBreaker(int radius) implements SimpleNumberDesc {

    @Override
    public BlockBreakerInstance getInstance(DiggerContext ctx) {
        int r = this.radius + (ctx.level() - 1) * 3;
        int x = -r * ctx.dire().getStepX();
        int y = -r * ctx.dire().getStepY();
        int z = -r * ctx.dire().getStepZ();
        return new RectInstance(Math.min(x, 0), Math.max(x, 0), Math.min(y, 0), Math.max(y, 0), Math.min(z, 0), Math.max(z, 0));
    }

    @Override
    public int range(int lv) {
        return this.radius + (lv - 1) * 3;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}