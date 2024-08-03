package org.violetmoon.quark.addons.oddities.block;

import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.violetmoon.quark.addons.oddities.block.be.MatrixEnchantingTableBlockEntity;
import org.violetmoon.quark.addons.oddities.module.MatrixEnchantingModule;
import org.violetmoon.quark.api.IEnchantmentInfluencer;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class MatrixEnchantingTableBlock extends EnchantmentTableBlock implements IZetaBlock {

    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public MatrixEnchantingTableBlock(ZetaModule module) {
        super(BlockBehaviour.Properties.copy(Blocks.ENCHANTING_TABLE));
        this.module = module;
        module.zeta.registry.registerBlock(this, "matrix_enchanter", true);
    }

    @NotNull
    @Override
    public MutableComponent getName() {
        return Blocks.ENCHANTING_TABLE.getName();
    }

    public MatrixEnchantingTableBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MatrixEnchantingTableBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return m_152132_(type, MatrixEnchantingModule.blockEntityType, MatrixEnchantingTableBlockEntity::tick);
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult raytrace) {
        if (!(worldIn.getBlockEntity(pos) instanceof MatrixEnchantingTableBlockEntity)) {
            worldIn.setBlockEntity(this.newBlockEntity(pos, state));
        }
        if (Quark.ZETA.modules.isEnabled(MatrixEnchantingModule.class)) {
            if (player instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openScreen(serverPlayer, (MatrixEnchantingTableBlockEntity) worldIn.getBlockEntity(pos), pos);
            }
        } else {
            worldIn.setBlockAndUpdate(pos, Blocks.ENCHANTING_TABLE.defaultBlockState());
        }
        return InteractionResult.sidedSuccess(worldIn.isClientSide);
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        boolean enabled = Quark.ZETA.modules.isEnabled(MatrixEnchantingModule.class);
        boolean showInfluences = enabled && MatrixEnchantingModule.allowInfluencing;
        boolean allowUnderwater = enabled && MatrixEnchantingModule.allowUnderwaterEnchanting;
        boolean allowShort = enabled && MatrixEnchantingModule.allowShortBlockEnchanting;
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i > -2 && i < 2 && j == -1) {
                    j = 2;
                }
                if (rand.nextInt(16) == 0) {
                    for (int k = 0; k <= 1; k++) {
                        BlockPos blockpos = pos.offset(i, k, j);
                        BlockState state = worldIn.getBlockState(blockpos);
                        BlockPos test = pos.offset(i / 2, 0, j / 2);
                        if (!worldIn.m_46859_(test) && (!allowUnderwater || worldIn.getBlockState(test).m_60734_() != Blocks.WATER) && (!allowShort || !MatrixEnchantingTableBlockEntity.isShortBlock(worldIn, test))) {
                            break;
                        }
                        if (showInfluences) {
                            IEnchantmentInfluencer influencer = MatrixEnchantingTableBlockEntity.getInfluencerFromBlock(state, worldIn, blockpos);
                            if (influencer != null) {
                                float[] comp = influencer.getEnchantmentInfluenceColor(worldIn, blockpos, state);
                                ParticleOptions extra = influencer.getExtraParticleOptions(worldIn, blockpos, state);
                                double chance = influencer.getExtraParticleChance(worldIn, blockpos, state);
                                if (comp != null || extra != null) {
                                    int steps = 20;
                                    double dx = (double) (pos.m_123341_() - blockpos.m_123341_()) / (double) steps;
                                    double dy = (double) (pos.m_123342_() - blockpos.m_123342_()) / (double) steps;
                                    double dz = (double) (pos.m_123343_() - blockpos.m_123343_()) / (double) steps;
                                    for (int p = 0; p < steps; p++) {
                                        boolean doDust = comp != null && rand.nextDouble() < 0.5;
                                        boolean doExtra = extra != null && rand.nextDouble() < chance;
                                        if (doDust || doExtra) {
                                            double px = (double) blockpos.m_123341_() + 0.5 + dx * (double) p + rand.nextDouble() * 0.2 - 0.1;
                                            double py = (double) blockpos.m_123342_() + 0.5 + dy * (double) p + Math.sin((double) p / (double) steps * Math.PI) * 0.5 + rand.nextDouble() * 0.2 - 0.1;
                                            double pz = (double) blockpos.m_123343_() + 0.5 + dz * (double) p + rand.nextDouble() * 0.2 - 0.1;
                                            if (doDust) {
                                                worldIn.addParticle(new DustParticleOptions(new Vector3f(comp[0], comp[1], comp[2]), 1.0F), px, py, pz, 0.0, 0.0, 0.0);
                                            }
                                            if (doExtra) {
                                                worldIn.addParticle(extra, px, py, pz, 0.0, 0.0, 0.0);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (state.getEnchantPowerBonus(worldIn, blockpos) > 0.0F) {
                            worldIn.addParticle(ParticleTypes.ENCHANT, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 2.0, (double) pos.m_123343_() + 0.5, (double) ((float) i + rand.nextFloat()) - 0.5, (double) ((float) k - rand.nextFloat()) - 1.0, (double) ((float) j + rand.nextFloat()) - 0.5);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setPlacedBy(@NotNull Level worldIn, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasCustomHoverName() && worldIn.getBlockEntity(pos) instanceof MatrixEnchantingTableBlockEntity matrixEnchanter) {
            matrixEnchanter.setCustomName(stack.getHoverName());
        }
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (worldIn.getBlockEntity(pos) instanceof MatrixEnchantingTableBlockEntity enchanter) {
            enchanter.dropItem(0);
            enchanter.dropItem(1);
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }
}