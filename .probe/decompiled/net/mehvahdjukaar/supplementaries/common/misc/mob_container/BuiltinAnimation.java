package net.mehvahdjukaar.supplementaries.common.misc.mob_container;

import com.mojang.serialization.Codec;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class BuiltinAnimation<T extends Entity> {

    protected float jumpY = 0.0F;

    protected float prevJumpY = 0.0F;

    protected float yVel = 0.0F;

    void tick(T mob, Level world, BlockPos pos) {
        if (world.isClientSide) {
            mob.yOld = mob.getY();
            float dy = this.jumpY - this.prevJumpY;
            if (dy != 0.0F) {
                mob.setPos(mob.getX(), mob.getY() + (double) dy, mob.getZ());
            }
            this.prevJumpY = this.jumpY;
        }
    }

    @Nullable
    public static <E extends Entity> BuiltinAnimation<E> get(E entity, BuiltinAnimation.Type type) {
        if (type == BuiltinAnimation.Type.BUILTIN) {
            if (entity instanceof Slime slime) {
                return (BuiltinAnimation<E>) (new BuiltinAnimation.SlimeAnim<>(slime));
            }
            if (entity instanceof Chicken chicken) {
                return new BuiltinAnimation.ChickenAnim(chicken);
            }
            if (entity instanceof Rabbit rabbit) {
                return new BuiltinAnimation.RabbitAnim(rabbit);
            }
            if (entity instanceof Parrot parrot) {
                return new BuiltinAnimation.ParrotAnim(parrot);
            }
            if (entity instanceof Endermite endermite) {
                return new BuiltinAnimation.EndermiteAnim(endermite);
            }
        } else if (type == BuiltinAnimation.Type.FLOATING) {
            return new BuiltinAnimation.FloatingAnim<>(entity);
        }
        return null;
    }

    private static class ChickenAnim<M extends Chicken> extends BuiltinAnimation<M> {

        public ChickenAnim(Chicken chicken) {
        }

        public void tick(M mob, Level world, BlockPos pos) {
            RandomSource rand = world.getRandom();
            if (!world.isClientSide) {
                if (--mob.eggTime <= 0) {
                    mob.m_19998_(Items.EGG);
                    mob.eggTime = rand.nextInt(6000) + 6000;
                }
            } else {
                mob.aiStep();
                if ((double) world.random.nextFloat() > (mob.m_20096_() ? 0.99 : 0.88)) {
                    mob.m_6853_(!mob.m_20096_());
                }
            }
        }
    }

    private static class EndermiteAnim<M extends Endermite> extends BuiltinAnimation<M> {

        public EndermiteAnim(Endermite endermite) {
        }

        public void tick(M mob, Level world, BlockPos pos) {
            if (world.isClientSide && world.random.nextFloat() > 0.7F) {
                world.addParticle(ParticleTypes.PORTAL, (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.2F), (double) ((float) pos.m_123343_() + 0.5F), (world.random.nextDouble() - 0.5) * 2.0, -world.random.nextDouble(), (world.random.nextDouble() - 0.5) * 2.0);
            }
        }
    }

    private static class FloatingAnim<M extends Entity> extends BuiltinAnimation<M> {

        FloatingAnim(M entity) {
        }

        @Override
        public void tick(M mob, Level world, BlockPos pos) {
            if (world.isClientSide) {
                this.jumpY = 0.04F * Mth.sin((float) mob.tickCount / 10.0F) - 0.03F;
            }
        }
    }

    private static class ParrotAnim<M extends Parrot> extends BuiltinAnimation<M> {

        public ParrotAnim(Parrot parrot) {
        }

        public void tick(M mob, Level world, BlockPos pos) {
            if (world.isClientSide) {
                mob.aiStep();
                boolean p = mob.isPartyParrot();
                mob.m_6853_(p);
                this.jumpY = p ? 0.0F : 0.0625F;
                super.tick(mob, world, pos);
            }
        }
    }

    private static class RabbitAnim<M extends Rabbit> extends BuiltinAnimation<M> {

        public RabbitAnim(Rabbit rabbit) {
        }

        public void tick(M mob, Level world, BlockPos pos) {
            if (world.isClientSide) {
                if (this.yVel != 0.0F) {
                    this.jumpY = Math.max(0.0F, this.jumpY + this.yVel);
                }
                if (this.jumpY != 0.0F) {
                    this.yVel -= 0.017F;
                } else {
                    if (this.yVel != 0.0F) {
                        this.yVel = 0.0F;
                    }
                    if ((double) world.random.nextFloat() > 0.985) {
                        this.yVel = 0.093F;
                        mob.startJumping();
                    }
                }
                mob.aiStep();
                super.tick(mob, world, pos);
            }
        }
    }

    private static class SlimeAnim<M extends Slime> extends BuiltinAnimation<M> {

        SlimeAnim(M slime) {
        }

        public void tick(M mob, Level world, BlockPos pos) {
            if (world.isClientSide) {
                mob.squish = mob.squish + (mob.targetSquish - mob.squish) * 0.5F;
                mob.oSquish = mob.squish;
                if (this.yVel != 0.0F) {
                    this.jumpY = Math.max(0.0F, this.jumpY + this.yVel);
                }
                if (this.jumpY != 0.0F) {
                    this.yVel -= 0.01F;
                } else {
                    if (this.yVel != 0.0F) {
                        this.yVel = 0.0F;
                        mob.targetSquish = -0.5F;
                    }
                    if ((double) world.getRandom().nextFloat() > 0.985) {
                        this.yVel = 0.08F;
                        mob.targetSquish = 1.0F;
                    }
                }
                mob.targetSquish *= 0.6F;
                super.tick(mob, world, pos);
            }
        }
    }

    public static enum Type implements StringRepresentable {

        NONE, LAND, AIR, FLOATING, BUILTIN;

        public static final Codec<BuiltinAnimation.Type> CODEC = StringRepresentable.fromEnum(BuiltinAnimation.Type::values);

        public boolean isFlying() {
            return this == AIR || this == FLOATING;
        }

        public boolean isLand() {
            return this == LAND;
        }

        public boolean isFloating() {
            return this == FLOATING;
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}