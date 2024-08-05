package net.minecraft.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BrushItem extends Item {

    public static final int ANIMATION_DURATION = 10;

    private static final int USE_DURATION = 200;

    private static final double MAX_BRUSH_DISTANCE = Math.sqrt(ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE) - 1.0;

    public BrushItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Player $$1 = useOnContext0.getPlayer();
        if ($$1 != null && this.calculateHitResult($$1).getType() == HitResult.Type.BLOCK) {
            $$1.m_6672_(useOnContext0.getHand());
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.BRUSH;
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 200;
    }

    @Override
    public void onUseTick(Level level0, LivingEntity livingEntity1, ItemStack itemStack2, int int3) {
        if (int3 >= 0 && livingEntity1 instanceof Player $$4) {
            HitResult $$6 = this.calculateHitResult(livingEntity1);
            if ($$6 instanceof BlockHitResult $$7 && $$6.getType() == HitResult.Type.BLOCK) {
                int $$9 = this.getUseDuration(itemStack2) - int3 + 1;
                boolean $$10 = $$9 % 10 == 5;
                if ($$10) {
                    BlockPos $$11 = $$7.getBlockPos();
                    BlockState $$12 = level0.getBlockState($$11);
                    HumanoidArm $$13 = livingEntity1.getUsedItemHand() == InteractionHand.MAIN_HAND ? $$4.getMainArm() : $$4.getMainArm().getOpposite();
                    this.spawnDustParticles(level0, $$7, $$12, livingEntity1.m_20252_(0.0F), $$13);
                    SoundEvent $$15;
                    if ($$12.m_60734_() instanceof BrushableBlock $$14) {
                        $$15 = $$14.getBrushSound();
                    } else {
                        $$15 = SoundEvents.BRUSH_GENERIC;
                    }
                    level0.m_247517_($$4, $$11, $$15, SoundSource.BLOCKS);
                    if (!level0.isClientSide() && level0.getBlockEntity($$11) instanceof BrushableBlockEntity $$17) {
                        boolean $$18 = $$17.brush(level0.getGameTime(), $$4, $$7.getDirection());
                        if ($$18) {
                            EquipmentSlot $$19 = itemStack2.equals($$4.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                            itemStack2.hurtAndBreak(1, livingEntity1, p_279044_ -> p_279044_.broadcastBreakEvent($$19));
                        }
                    }
                }
                return;
            }
            livingEntity1.releaseUsingItem();
        } else {
            livingEntity1.releaseUsingItem();
        }
    }

    private HitResult calculateHitResult(LivingEntity livingEntity0) {
        return ProjectileUtil.getHitResultOnViewVector(livingEntity0, p_281111_ -> !p_281111_.isSpectator() && p_281111_.isPickable(), MAX_BRUSH_DISTANCE);
    }

    public void spawnDustParticles(Level level0, BlockHitResult blockHitResult1, BlockState blockState2, Vec3 vec3, HumanoidArm humanoidArm4) {
        double $$5 = 3.0;
        int $$6 = humanoidArm4 == HumanoidArm.RIGHT ? 1 : -1;
        int $$7 = level0.getRandom().nextInt(7, 12);
        BlockParticleOption $$8 = new BlockParticleOption(ParticleTypes.BLOCK, blockState2);
        Direction $$9 = blockHitResult1.getDirection();
        BrushItem.DustParticlesDelta $$10 = BrushItem.DustParticlesDelta.fromDirection(vec3, $$9);
        Vec3 $$11 = blockHitResult1.m_82450_();
        for (int $$12 = 0; $$12 < $$7; $$12++) {
            level0.addParticle($$8, $$11.x - (double) ($$9 == Direction.WEST ? 1.0E-6F : 0.0F), $$11.y, $$11.z - (double) ($$9 == Direction.NORTH ? 1.0E-6F : 0.0F), $$10.xd() * (double) $$6 * 3.0 * level0.getRandom().nextDouble(), 0.0, $$10.zd() * (double) $$6 * 3.0 * level0.getRandom().nextDouble());
        }
    }

    static record DustParticlesDelta(double f_271456_, double f_271284_, double f_271522_) {

        private final double xd;

        private final double yd;

        private final double zd;

        private static final double ALONG_SIDE_DELTA = 1.0;

        private static final double OUT_FROM_SIDE_DELTA = 0.1;

        private DustParticlesDelta(double f_271456_, double f_271284_, double f_271522_) {
            this.xd = f_271456_;
            this.yd = f_271284_;
            this.zd = f_271522_;
        }

        public static BrushItem.DustParticlesDelta fromDirection(Vec3 p_273421_, Direction p_272987_) {
            double $$2 = 0.0;
            return switch(p_272987_) {
                case DOWN, UP ->
                    new BrushItem.DustParticlesDelta(p_273421_.z(), 0.0, -p_273421_.x());
                case NORTH ->
                    new BrushItem.DustParticlesDelta(1.0, 0.0, -0.1);
                case SOUTH ->
                    new BrushItem.DustParticlesDelta(-1.0, 0.0, 0.1);
                case WEST ->
                    new BrushItem.DustParticlesDelta(-0.1, 0.0, -1.0);
                case EAST ->
                    new BrushItem.DustParticlesDelta(0.1, 0.0, 1.0);
            };
        }
    }
}