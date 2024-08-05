package net.minecraft.world.level.border;

public interface BorderChangeListener {

    void onBorderSizeSet(WorldBorder var1, double var2);

    void onBorderSizeLerping(WorldBorder var1, double var2, double var4, long var6);

    void onBorderCenterSet(WorldBorder var1, double var2, double var4);

    void onBorderSetWarningTime(WorldBorder var1, int var2);

    void onBorderSetWarningBlocks(WorldBorder var1, int var2);

    void onBorderSetDamagePerBlock(WorldBorder var1, double var2);

    void onBorderSetDamageSafeZOne(WorldBorder var1, double var2);

    public static class DelegateBorderChangeListener implements BorderChangeListener {

        private final WorldBorder worldBorder;

        public DelegateBorderChangeListener(WorldBorder worldBorder0) {
            this.worldBorder = worldBorder0;
        }

        @Override
        public void onBorderSizeSet(WorldBorder worldBorder0, double double1) {
            this.worldBorder.setSize(double1);
        }

        @Override
        public void onBorderSizeLerping(WorldBorder worldBorder0, double double1, double double2, long long3) {
            this.worldBorder.lerpSizeBetween(double1, double2, long3);
        }

        @Override
        public void onBorderCenterSet(WorldBorder worldBorder0, double double1, double double2) {
            this.worldBorder.setCenter(double1, double2);
        }

        @Override
        public void onBorderSetWarningTime(WorldBorder worldBorder0, int int1) {
            this.worldBorder.setWarningTime(int1);
        }

        @Override
        public void onBorderSetWarningBlocks(WorldBorder worldBorder0, int int1) {
            this.worldBorder.setWarningBlocks(int1);
        }

        @Override
        public void onBorderSetDamagePerBlock(WorldBorder worldBorder0, double double1) {
            this.worldBorder.setDamagePerBlock(double1);
        }

        @Override
        public void onBorderSetDamageSafeZOne(WorldBorder worldBorder0, double double1) {
            this.worldBorder.setDamageSafeZone(double1);
        }
    }
}