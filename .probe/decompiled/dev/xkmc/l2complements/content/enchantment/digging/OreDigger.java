package dev.xkmc.l2complements.content.enchantment.digging;

public record OreDigger(int r, int max) implements SimpleNumberDesc {

    @Override
    public BlockBreakerInstance getInstance(DiggerContext ctx) {
        return new VienInstance(-this.r, this.r, -this.r, this.r, -this.r, this.r, this.max << ctx.level() - 1, state -> state.m_60734_() == ctx.state().m_60734_());
    }

    @Override
    public int range(int lv) {
        return this.max << lv - 1;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}