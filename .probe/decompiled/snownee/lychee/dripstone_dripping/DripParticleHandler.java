package snownee.lychee.dripstone_dripping;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface DripParticleHandler {

    DripParticleHandler.Simple SIMPLE_DUMMY = new DripParticleHandler.Simple(0, false);

    void addParticle(Level var1, BlockPos var2, BlockState var3, double var4, double var6, double var8);

    int getColor(ClientLevel var1, BlockState var2, double var3, double var5, double var7, double var9, double var11, double var13);

    boolean isGlowing(ClientLevel var1, BlockState var2);

    public static record Simple(int color, boolean glowing) implements DripParticleHandler {

        @Override
        public void addParticle(Level level, BlockPos blockPos, BlockState blockState, double x, double y, double z) {
            level.addParticle(new BlockParticleOption(DripstoneRecipeMod.DRIPSTONE_DRIPPING, blockState), x, y, z, 0.0, 0.0, 0.0);
        }

        @Override
        public int getColor(ClientLevel level, BlockState blockState, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return this.color;
        }

        @Override
        public boolean isGlowing(ClientLevel level, BlockState blockState) {
            return this.glowing;
        }
    }
}