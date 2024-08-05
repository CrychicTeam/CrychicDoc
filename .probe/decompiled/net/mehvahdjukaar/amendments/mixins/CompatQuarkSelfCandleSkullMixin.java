package net.mehvahdjukaar.amendments.mixins;

import java.util.List;
import net.mehvahdjukaar.amendments.common.block.AbstractCandleSkullBlock;
import net.mehvahdjukaar.amendments.common.tile.CandleSkullBlockTile;
import net.mehvahdjukaar.moonlight.api.misc.OptionalMixin;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.violetmoon.quark.addons.oddities.module.MatrixEnchantingModule;
import org.violetmoon.quark.addons.oddities.util.Influence;
import org.violetmoon.quark.api.IEnchantmentInfluencer;

@OptionalMixin("vazkii.quark.api.IEnchantmentInfluencer")
@Mixin({ AbstractCandleSkullBlock.class })
public abstract class CompatQuarkSelfCandleSkullMixin implements IEnchantmentInfluencer {

    @Shadow
    public abstract ParticleType<? extends ParticleOptions> getParticle();

    @Unique
    private DyeColor amendments$getColor(BlockState s, BlockGetter level, BlockPos pos) {
        if ((Boolean) s.m_61143_(CandleBlock.LIT) && level.getBlockEntity(pos) instanceof CandleSkullBlockTile tile) {
            BlockState state = tile.getCandle();
            if (state.m_60734_() instanceof CandleBlock) {
                return BlocksColorAPI.getColor(state.m_60734_());
            }
        }
        return null;
    }

    @Override
    public float[] getEnchantmentInfluenceColor(BlockGetter world, BlockPos pos, BlockState state) {
        DyeColor color = this.amendments$getColor(state, world, pos);
        return color == null ? null : color.getTextureDiffuseColors();
    }

    @Nullable
    @Override
    public ParticleOptions getExtraParticleOptions(BlockGetter world, BlockPos pos, BlockState state) {
        return state.m_61143_(CandleBlock.LIT) && this.getParticle() != ParticleTypes.SMALL_FLAME ? ParticleTypes.SOUL : null;
    }

    @Override
    public double getExtraParticleChance(BlockGetter world, BlockPos pos, BlockState state) {
        return 0.25;
    }

    @Override
    public int getInfluenceStack(BlockGetter world, BlockPos pos, BlockState state) {
        return state.m_61143_(CandleBlock.LIT) ? (Integer) state.m_61143_(CandleBlock.CANDLES) + 1 : 0;
    }

    @Override
    public boolean influencesEnchantment(BlockGetter world, BlockPos pos, BlockState state, Enchantment enchantment) {
        DyeColor color = this.amendments$getColor(state, world, pos);
        if (color == null) {
            return false;
        } else {
            Influence influence = (Influence) MatrixEnchantingModule.candleInfluences.get(color);
            List<Enchantment> boosts = this.getParticle() != ParticleTypes.SMALL_FLAME ? influence.dampen() : influence.boost();
            return boosts.contains(enchantment);
        }
    }

    @Override
    public boolean dampensEnchantment(BlockGetter world, BlockPos pos, BlockState state, Enchantment enchantment) {
        DyeColor color = this.amendments$getColor(state, world, pos);
        if (color == null) {
            return false;
        } else {
            Influence influence = (Influence) MatrixEnchantingModule.candleInfluences.get(color);
            List<Enchantment> dampens = this.getParticle() != ParticleTypes.SMALL_FLAME ? influence.boost() : influence.dampen();
            return dampens.contains(enchantment);
        }
    }
}