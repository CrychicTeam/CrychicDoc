package dev.xkmc.l2complements.content.enchantment.digging;

public record CubicBlockBreaker(int radius) implements SimpleNumberDesc {

    @Override
    public BlockBreakerInstance getInstance(DiggerContext ctx) {
        int r = this.radius + ctx.level() - 1;
        return new RectInstance(-r, r, -r, r, -r, r);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int range(int lv) {
        return (this.radius + lv - 1) * 2 + 1;
    }

    @Override
    public boolean ignoreHardness() {
        return true;
    }
}