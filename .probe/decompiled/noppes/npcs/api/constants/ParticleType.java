package noppes.npcs.api.constants;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleType {

    public static final int NONE = 0;

    public static final int SMOKE = 1;

    public static final int PORTAL = 2;

    public static final int REDSTONE = 3;

    public static final int LIGHTNING = 4;

    public static final int LARGE_SMOKE = 5;

    public static final int MAGIC = 6;

    public static final int ENCHANT = 7;

    public static final int CRIT = 8;

    public static ParticleOptions getMCType(int type) {
        if (type == 1) {
            return ParticleTypes.SMOKE;
        } else if (type == 2) {
            return ParticleTypes.PORTAL;
        } else if (type == 3) {
            return new ParticleType.RedstoneParticleType();
        } else if (type == 4) {
            return ParticleTypes.ENCHANTED_HIT;
        } else if (type == 5) {
            return ParticleTypes.LARGE_SMOKE;
        } else if (type == 6) {
            return ParticleTypes.WITCH;
        } else if (type == 7) {
            return ParticleTypes.ENCHANT;
        } else {
            return type == 8 ? ParticleTypes.CRIT : null;
        }
    }

    static class RedstoneParticleType extends DustParticleOptions {

        protected RedstoneParticleType() {
            super(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.0F);
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf p_197553_1_) {
        }

        @Override
        public String writeToString() {
            return ForgeRegistries.PARTICLE_TYPES.getKey(ParticleTypes.DUST).toString();
        }
    }
}