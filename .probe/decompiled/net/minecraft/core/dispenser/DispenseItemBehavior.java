package net.minecraft.core.dispenser;

import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

public interface DispenseItemBehavior {

    Logger LOGGER = LogUtils.getLogger();

    DispenseItemBehavior NOOP = (p_123400_, p_123401_) -> p_123401_;

    ItemStack dispense(BlockSource var1, ItemStack var2);

    static void bootStrap() {
        DispenserBlock.registerBehavior(Items.ARROW, new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level p_123407_, Position p_123408_, ItemStack p_123409_) {
                Arrow $$3 = new Arrow(p_123407_, p_123408_.x(), p_123408_.y(), p_123408_.z());
                $$3.f_36705_ = AbstractArrow.Pickup.ALLOWED;
                return $$3;
            }
        });
        DispenserBlock.registerBehavior(Items.TIPPED_ARROW, new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level p_123420_, Position p_123421_, ItemStack p_123422_) {
                Arrow $$3 = new Arrow(p_123420_, p_123421_.x(), p_123421_.y(), p_123421_.z());
                $$3.setEffectsFromItem(p_123422_);
                $$3.f_36705_ = AbstractArrow.Pickup.ALLOWED;
                return $$3;
            }
        });
        DispenserBlock.registerBehavior(Items.SPECTRAL_ARROW, new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level p_123456_, Position p_123457_, ItemStack p_123458_) {
                AbstractArrow $$3 = new SpectralArrow(p_123456_, p_123457_.x(), p_123457_.y(), p_123457_.z());
                $$3.pickup = AbstractArrow.Pickup.ALLOWED;
                return $$3;
            }
        });
        DispenserBlock.registerBehavior(Items.EGG, new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level p_123468_, Position p_123469_, ItemStack p_123470_) {
                return Util.make(new ThrownEgg(p_123468_, p_123469_.x(), p_123469_.y(), p_123469_.z()), p_123466_ -> p_123466_.m_37446_(p_123470_));
            }
        });
        DispenserBlock.registerBehavior(Items.SNOWBALL, new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level p_123476_, Position p_123477_, ItemStack p_123478_) {
                return Util.make(new Snowball(p_123476_, p_123477_.x(), p_123477_.y(), p_123477_.z()), p_123474_ -> p_123474_.m_37446_(p_123478_));
            }
        });
        DispenserBlock.registerBehavior(Items.EXPERIENCE_BOTTLE, new AbstractProjectileDispenseBehavior() {

            @Override
            protected Projectile getProjectile(Level p_123485_, Position p_123486_, ItemStack p_123487_) {
                return Util.make(new ThrownExperienceBottle(p_123485_, p_123486_.x(), p_123486_.y(), p_123486_.z()), p_123483_ -> p_123483_.m_37446_(p_123487_));
            }

            @Override
            protected float getUncertainty() {
                return super.getUncertainty() * 0.5F;
            }

            @Override
            protected float getPower() {
                return super.getPower() * 1.25F;
            }
        });
        DispenserBlock.registerBehavior(Items.SPLASH_POTION, new DispenseItemBehavior() {

            @Override
            public ItemStack dispense(BlockSource p_123491_, ItemStack p_123492_) {
                return (new AbstractProjectileDispenseBehavior() {

                    @Override
                    protected Projectile getProjectile(Level p_123501_, Position p_123502_, ItemStack p_123503_) {
                        return Util.make(new ThrownPotion(p_123501_, p_123502_.x(), p_123502_.y(), p_123502_.z()), p_123499_ -> p_123499_.m_37446_(p_123503_));
                    }

                    @Override
                    protected float getUncertainty() {
                        return super.getUncertainty() * 0.5F;
                    }

                    @Override
                    protected float getPower() {
                        return super.getPower() * 1.25F;
                    }
                }).m_6115_(p_123491_, p_123492_);
            }
        });
        DispenserBlock.registerBehavior(Items.LINGERING_POTION, new DispenseItemBehavior() {

            @Override
            public ItemStack dispense(BlockSource p_123507_, ItemStack p_123508_) {
                return (new AbstractProjectileDispenseBehavior() {

                    @Override
                    protected Projectile getProjectile(Level p_123517_, Position p_123518_, ItemStack p_123519_) {
                        return Util.make(new ThrownPotion(p_123517_, p_123518_.x(), p_123518_.y(), p_123518_.z()), p_123515_ -> p_123515_.m_37446_(p_123519_));
                    }

                    @Override
                    protected float getUncertainty() {
                        return super.getUncertainty() * 0.5F;
                    }

                    @Override
                    protected float getPower() {
                        return super.getPower() * 1.25F;
                    }
                }).m_6115_(p_123507_, p_123508_);
            }
        });
        DefaultDispenseItemBehavior $$0 = new DefaultDispenseItemBehavior() {

            @Override
            public ItemStack execute(BlockSource p_123523_, ItemStack p_123524_) {
                Direction $$2 = (Direction) p_123523_.getBlockState().m_61143_(DispenserBlock.FACING);
                EntityType<?> $$3 = ((SpawnEggItem) p_123524_.getItem()).getType(p_123524_.getTag());
                try {
                    $$3.spawn(p_123523_.getLevel(), p_123524_, null, p_123523_.getPos().relative($$2), MobSpawnType.DISPENSER, $$2 != Direction.UP, false);
                } catch (Exception var6) {
                    f_181892_.error("Error while dispensing spawn egg from dispenser at {}", p_123523_.getPos(), var6);
                    return ItemStack.EMPTY;
                }
                p_123524_.shrink(1);
                p_123523_.getLevel().m_142346_(null, GameEvent.ENTITY_PLACE, p_123523_.getPos());
                return p_123524_;
            }
        };
        for (SpawnEggItem $$1 : SpawnEggItem.eggs()) {
            DispenserBlock.registerBehavior($$1, $$0);
        }
        DispenserBlock.registerBehavior(Items.ARMOR_STAND, new DefaultDispenseItemBehavior() {

            @Override
            public ItemStack execute(BlockSource p_123461_, ItemStack p_123462_) {
                Direction $$2 = (Direction) p_123461_.getBlockState().m_61143_(DispenserBlock.FACING);
                BlockPos $$3 = p_123461_.getPos().relative($$2);
                ServerLevel $$4 = p_123461_.getLevel();
                Consumer<ArmorStand> $$5 = EntityType.appendDefaultStackConfig(p_277236_ -> p_277236_.m_146922_($$2.toYRot()), $$4, p_123462_, null);
                ArmorStand $$6 = EntityType.ARMOR_STAND.spawn($$4, p_123462_.getTag(), $$5, $$3, MobSpawnType.DISPENSER, false, false);
                if ($$6 != null) {
                    p_123462_.shrink(1);
                }
                return p_123462_;
            }
        });
        DispenserBlock.registerBehavior(Items.SADDLE, new OptionalDispenseItemBehavior() {

            @Override
            public ItemStack execute(BlockSource p_123529_, ItemStack p_123530_) {
                BlockPos $$2 = p_123529_.getPos().relative((Direction) p_123529_.getBlockState().m_61143_(DispenserBlock.FACING));
                List<LivingEntity> $$3 = p_123529_.getLevel().m_6443_(LivingEntity.class, new AABB($$2), p_123527_ -> !(p_123527_ instanceof Saddleable $$1) ? false : !$$1.isSaddled() && $$1.isSaddleable());
                if (!$$3.isEmpty()) {
                    ((Saddleable) $$3.get(0)).equipSaddle(SoundSource.BLOCKS);
                    p_123530_.shrink(1);
                    this.m_123573_(true);
                    return p_123530_;
                } else {
                    return super.m_7498_(p_123529_, p_123530_);
                }
            }
        });
        DefaultDispenseItemBehavior $$2 = new OptionalDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource p_123535_, ItemStack p_123536_) {
                BlockPos $$2 = p_123535_.getPos().relative((Direction) p_123535_.getBlockState().m_61143_(DispenserBlock.FACING));
                for (AbstractHorse $$4 : p_123535_.getLevel().m_6443_(AbstractHorse.class, new AABB($$2), p_289248_ -> p_289248_.m_6084_() && p_289248_.canWearArmor())) {
                    if ($$4.isArmor(p_123536_) && !$$4.isWearingArmor() && $$4.isTamed()) {
                        $$4.getSlot(401).set(p_123536_.split(1));
                        this.m_123573_(true);
                        return p_123536_;
                    }
                }
                return super.m_7498_(p_123535_, p_123536_);
            }
        };
        DispenserBlock.registerBehavior(Items.LEATHER_HORSE_ARMOR, $$2);
        DispenserBlock.registerBehavior(Items.IRON_HORSE_ARMOR, $$2);
        DispenserBlock.registerBehavior(Items.GOLDEN_HORSE_ARMOR, $$2);
        DispenserBlock.registerBehavior(Items.DIAMOND_HORSE_ARMOR, $$2);
        DispenserBlock.registerBehavior(Items.WHITE_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.ORANGE_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.CYAN_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.BLUE_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.BROWN_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.BLACK_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.GRAY_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.GREEN_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.LIGHT_BLUE_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.LIGHT_GRAY_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.LIME_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.MAGENTA_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.PINK_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.PURPLE_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.RED_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.YELLOW_CARPET, $$2);
        DispenserBlock.registerBehavior(Items.CHEST, new OptionalDispenseItemBehavior() {

            @Override
            public ItemStack execute(BlockSource p_123541_, ItemStack p_123542_) {
                BlockPos $$2 = p_123541_.getPos().relative((Direction) p_123541_.getBlockState().m_61143_(DispenserBlock.FACING));
                for (AbstractChestedHorse $$4 : p_123541_.getLevel().m_6443_(AbstractChestedHorse.class, new AABB($$2), p_289249_ -> p_289249_.m_6084_() && !p_289249_.hasChest())) {
                    if ($$4.m_30614_() && $$4.getSlot(499).set(p_123542_)) {
                        p_123542_.shrink(1);
                        this.m_123573_(true);
                        return p_123542_;
                    }
                }
                return super.m_7498_(p_123541_, p_123542_);
            }
        });
        DispenserBlock.registerBehavior(Items.FIREWORK_ROCKET, new DefaultDispenseItemBehavior() {

            @Override
            public ItemStack execute(BlockSource p_123547_, ItemStack p_123548_) {
                Direction $$2 = (Direction) p_123547_.getBlockState().m_61143_(DispenserBlock.FACING);
                FireworkRocketEntity $$3 = new FireworkRocketEntity(p_123547_.getLevel(), p_123548_, p_123547_.x(), p_123547_.y(), p_123547_.x(), true);
                DispenseItemBehavior.setEntityPokingOutOfBlock(p_123547_, $$3, $$2);
                $$3.m_6686_((double) $$2.getStepX(), (double) $$2.getStepY(), (double) $$2.getStepZ(), 0.5F, 1.0F);
                p_123547_.getLevel().addFreshEntity($$3);
                p_123548_.shrink(1);
                return p_123548_;
            }

            @Override
            protected void playSound(BlockSource p_123545_) {
                p_123545_.getLevel().m_46796_(1004, p_123545_.getPos(), 0);
            }
        });
        DispenserBlock.registerBehavior(Items.FIRE_CHARGE, new DefaultDispenseItemBehavior() {

            @Override
            public ItemStack execute(BlockSource p_123556_, ItemStack p_123557_) {
                Direction $$2 = (Direction) p_123556_.getBlockState().m_61143_(DispenserBlock.FACING);
                Position $$3 = DispenserBlock.getDispensePosition(p_123556_);
                double $$4 = $$3.x() + (double) ((float) $$2.getStepX() * 0.3F);
                double $$5 = $$3.y() + (double) ((float) $$2.getStepY() * 0.3F);
                double $$6 = $$3.z() + (double) ((float) $$2.getStepZ() * 0.3F);
                Level $$7 = p_123556_.getLevel();
                RandomSource $$8 = $$7.random;
                double $$9 = $$8.triangle((double) $$2.getStepX(), 0.11485000000000001);
                double $$10 = $$8.triangle((double) $$2.getStepY(), 0.11485000000000001);
                double $$11 = $$8.triangle((double) $$2.getStepZ(), 0.11485000000000001);
                SmallFireball $$12 = new SmallFireball($$7, $$4, $$5, $$6, $$9, $$10, $$11);
                $$7.m_7967_(Util.make($$12, p_123552_ -> p_123552_.m_37010_(p_123557_)));
                p_123557_.shrink(1);
                return p_123557_;
            }

            @Override
            protected void playSound(BlockSource p_123554_) {
                p_123554_.getLevel().m_46796_(1018, p_123554_.getPos(), 0);
            }
        });
        DispenserBlock.registerBehavior(Items.OAK_BOAT, new BoatDispenseItemBehavior(Boat.Type.OAK));
        DispenserBlock.registerBehavior(Items.SPRUCE_BOAT, new BoatDispenseItemBehavior(Boat.Type.SPRUCE));
        DispenserBlock.registerBehavior(Items.BIRCH_BOAT, new BoatDispenseItemBehavior(Boat.Type.BIRCH));
        DispenserBlock.registerBehavior(Items.JUNGLE_BOAT, new BoatDispenseItemBehavior(Boat.Type.JUNGLE));
        DispenserBlock.registerBehavior(Items.DARK_OAK_BOAT, new BoatDispenseItemBehavior(Boat.Type.DARK_OAK));
        DispenserBlock.registerBehavior(Items.ACACIA_BOAT, new BoatDispenseItemBehavior(Boat.Type.ACACIA));
        DispenserBlock.registerBehavior(Items.CHERRY_BOAT, new BoatDispenseItemBehavior(Boat.Type.CHERRY));
        DispenserBlock.registerBehavior(Items.MANGROVE_BOAT, new BoatDispenseItemBehavior(Boat.Type.MANGROVE));
        DispenserBlock.registerBehavior(Items.BAMBOO_RAFT, new BoatDispenseItemBehavior(Boat.Type.BAMBOO));
        DispenserBlock.registerBehavior(Items.OAK_CHEST_BOAT, new BoatDispenseItemBehavior(Boat.Type.OAK, true));
        DispenserBlock.registerBehavior(Items.SPRUCE_CHEST_BOAT, new BoatDispenseItemBehavior(Boat.Type.SPRUCE, true));
        DispenserBlock.registerBehavior(Items.BIRCH_CHEST_BOAT, new BoatDispenseItemBehavior(Boat.Type.BIRCH, true));
        DispenserBlock.registerBehavior(Items.JUNGLE_CHEST_BOAT, new BoatDispenseItemBehavior(Boat.Type.JUNGLE, true));
        DispenserBlock.registerBehavior(Items.DARK_OAK_CHEST_BOAT, new BoatDispenseItemBehavior(Boat.Type.DARK_OAK, true));
        DispenserBlock.registerBehavior(Items.ACACIA_CHEST_BOAT, new BoatDispenseItemBehavior(Boat.Type.ACACIA, true));
        DispenserBlock.registerBehavior(Items.CHERRY_CHEST_BOAT, new BoatDispenseItemBehavior(Boat.Type.CHERRY, true));
        DispenserBlock.registerBehavior(Items.MANGROVE_CHEST_BOAT, new BoatDispenseItemBehavior(Boat.Type.MANGROVE, true));
        DispenserBlock.registerBehavior(Items.BAMBOO_CHEST_RAFT, new BoatDispenseItemBehavior(Boat.Type.BAMBOO, true));
        DispenseItemBehavior $$3 = new DefaultDispenseItemBehavior() {

            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource p_123561_, ItemStack p_123562_) {
                DispensibleContainerItem $$2 = (DispensibleContainerItem) p_123562_.getItem();
                BlockPos $$3 = p_123561_.getPos().relative((Direction) p_123561_.getBlockState().m_61143_(DispenserBlock.FACING));
                Level $$4 = p_123561_.getLevel();
                if ($$2.emptyContents(null, $$4, $$3, null)) {
                    $$2.checkExtraContent(null, $$4, p_123562_, $$3);
                    return new ItemStack(Items.BUCKET);
                } else {
                    return this.defaultDispenseItemBehavior.dispense(p_123561_, p_123562_);
                }
            }
        };
        DispenserBlock.registerBehavior(Items.LAVA_BUCKET, $$3);
        DispenserBlock.registerBehavior(Items.WATER_BUCKET, $$3);
        DispenserBlock.registerBehavior(Items.POWDER_SNOW_BUCKET, $$3);
        DispenserBlock.registerBehavior(Items.SALMON_BUCKET, $$3);
        DispenserBlock.registerBehavior(Items.COD_BUCKET, $$3);
        DispenserBlock.registerBehavior(Items.PUFFERFISH_BUCKET, $$3);
        DispenserBlock.registerBehavior(Items.TROPICAL_FISH_BUCKET, $$3);
        DispenserBlock.registerBehavior(Items.AXOLOTL_BUCKET, $$3);
        DispenserBlock.registerBehavior(Items.TADPOLE_BUCKET, $$3);
        DispenserBlock.registerBehavior(Items.BUCKET, new DefaultDispenseItemBehavior() {

            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource p_123566_, ItemStack p_123567_) {
                LevelAccessor $$2 = p_123566_.getLevel();
                BlockPos $$3 = p_123566_.getPos().relative((Direction) p_123566_.getBlockState().m_61143_(DispenserBlock.FACING));
                BlockState $$4 = $$2.m_8055_($$3);
                Block $$5 = $$4.m_60734_();
                if ($$5 instanceof BucketPickup) {
                    ItemStack $$6 = ((BucketPickup) $$5).pickupBlock($$2, $$3, $$4);
                    if ($$6.isEmpty()) {
                        return super.execute(p_123566_, p_123567_);
                    } else {
                        $$2.gameEvent(null, GameEvent.FLUID_PICKUP, $$3);
                        Item $$7 = $$6.getItem();
                        p_123567_.shrink(1);
                        if (p_123567_.isEmpty()) {
                            return new ItemStack($$7);
                        } else {
                            if (p_123566_.<DispenserBlockEntity>getEntity().addItem(new ItemStack($$7)) < 0) {
                                this.defaultDispenseItemBehavior.dispense(p_123566_, new ItemStack($$7));
                            }
                            return p_123567_;
                        }
                    }
                } else {
                    return super.execute(p_123566_, p_123567_);
                }
            }
        });
        DispenserBlock.registerBehavior(Items.FLINT_AND_STEEL, new OptionalDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource p_123412_, ItemStack p_123413_) {
                Level $$2 = p_123412_.getLevel();
                this.m_123573_(true);
                Direction $$3 = (Direction) p_123412_.getBlockState().m_61143_(DispenserBlock.FACING);
                BlockPos $$4 = p_123412_.getPos().relative($$3);
                BlockState $$5 = $$2.getBlockState($$4);
                if (BaseFireBlock.canBePlacedAt($$2, $$4, $$3)) {
                    $$2.setBlockAndUpdate($$4, BaseFireBlock.getState($$2, $$4));
                    $$2.m_142346_(null, GameEvent.BLOCK_PLACE, $$4);
                } else if (CampfireBlock.canLight($$5) || CandleBlock.canLight($$5) || CandleCakeBlock.canLight($$5)) {
                    $$2.setBlockAndUpdate($$4, (BlockState) $$5.m_61124_(BlockStateProperties.LIT, true));
                    $$2.m_142346_(null, GameEvent.BLOCK_CHANGE, $$4);
                } else if ($$5.m_60734_() instanceof TntBlock) {
                    TntBlock.explode($$2, $$4);
                    $$2.removeBlock($$4, false);
                } else {
                    this.m_123573_(false);
                }
                if (this.m_123570_() && p_123413_.hurt(1, $$2.random, null)) {
                    p_123413_.setCount(0);
                }
                return p_123413_;
            }
        });
        DispenserBlock.registerBehavior(Items.BONE_MEAL, new OptionalDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource p_123416_, ItemStack p_123417_) {
                this.m_123573_(true);
                Level $$2 = p_123416_.getLevel();
                BlockPos $$3 = p_123416_.getPos().relative((Direction) p_123416_.getBlockState().m_61143_(DispenserBlock.FACING));
                if (!BoneMealItem.growCrop(p_123417_, $$2, $$3) && !BoneMealItem.growWaterPlant(p_123417_, $$2, $$3, null)) {
                    this.m_123573_(false);
                } else if (!$$2.isClientSide) {
                    $$2.m_46796_(1505, $$3, 0);
                }
                return p_123417_;
            }
        });
        DispenserBlock.registerBehavior(Blocks.TNT, new DefaultDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource p_123425_, ItemStack p_123426_) {
                Level $$2 = p_123425_.getLevel();
                BlockPos $$3 = p_123425_.getPos().relative((Direction) p_123425_.getBlockState().m_61143_(DispenserBlock.FACING));
                PrimedTnt $$4 = new PrimedTnt($$2, (double) $$3.m_123341_() + 0.5, (double) $$3.m_123342_(), (double) $$3.m_123343_() + 0.5, null);
                $$2.m_7967_($$4);
                $$2.playSound(null, $$4.m_20185_(), $$4.m_20186_(), $$4.m_20189_(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
                $$2.m_142346_(null, GameEvent.ENTITY_PLACE, $$3);
                p_123426_.shrink(1);
                return p_123426_;
            }
        });
        DispenseItemBehavior $$4 = new OptionalDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource p_123429_, ItemStack p_123430_) {
                this.m_123573_(ArmorItem.dispenseArmor(p_123429_, p_123430_));
                return p_123430_;
            }
        };
        DispenserBlock.registerBehavior(Items.CREEPER_HEAD, $$4);
        DispenserBlock.registerBehavior(Items.ZOMBIE_HEAD, $$4);
        DispenserBlock.registerBehavior(Items.DRAGON_HEAD, $$4);
        DispenserBlock.registerBehavior(Items.SKELETON_SKULL, $$4);
        DispenserBlock.registerBehavior(Items.PIGLIN_HEAD, $$4);
        DispenserBlock.registerBehavior(Items.PLAYER_HEAD, $$4);
        DispenserBlock.registerBehavior(Items.WITHER_SKELETON_SKULL, new OptionalDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource p_123433_, ItemStack p_123434_) {
                Level $$2 = p_123433_.getLevel();
                Direction $$3 = (Direction) p_123433_.getBlockState().m_61143_(DispenserBlock.FACING);
                BlockPos $$4 = p_123433_.getPos().relative($$3);
                if ($$2.m_46859_($$4) && WitherSkullBlock.canSpawnMob($$2, $$4, p_123434_)) {
                    $$2.setBlock($$4, (BlockState) Blocks.WITHER_SKELETON_SKULL.defaultBlockState().m_61124_(SkullBlock.ROTATION, RotationSegment.convertToSegment($$3)), 3);
                    $$2.m_142346_(null, GameEvent.BLOCK_PLACE, $$4);
                    BlockEntity $$5 = $$2.getBlockEntity($$4);
                    if ($$5 instanceof SkullBlockEntity) {
                        WitherSkullBlock.checkSpawn($$2, $$4, (SkullBlockEntity) $$5);
                    }
                    p_123434_.shrink(1);
                    this.m_123573_(true);
                } else {
                    this.m_123573_(ArmorItem.dispenseArmor(p_123433_, p_123434_));
                }
                return p_123434_;
            }
        });
        DispenserBlock.registerBehavior(Blocks.CARVED_PUMPKIN, new OptionalDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource p_123437_, ItemStack p_123438_) {
                Level $$2 = p_123437_.getLevel();
                BlockPos $$3 = p_123437_.getPos().relative((Direction) p_123437_.getBlockState().m_61143_(DispenserBlock.FACING));
                CarvedPumpkinBlock $$4 = (CarvedPumpkinBlock) Blocks.CARVED_PUMPKIN;
                if ($$2.m_46859_($$3) && $$4.canSpawnGolem($$2, $$3)) {
                    if (!$$2.isClientSide) {
                        $$2.setBlock($$3, $$4.m_49966_(), 3);
                        $$2.m_142346_(null, GameEvent.BLOCK_PLACE, $$3);
                    }
                    p_123438_.shrink(1);
                    this.m_123573_(true);
                } else {
                    this.m_123573_(ArmorItem.dispenseArmor(p_123437_, p_123438_));
                }
                return p_123438_;
            }
        });
        DispenserBlock.registerBehavior(Blocks.SHULKER_BOX.asItem(), new ShulkerBoxDispenseBehavior());
        for (DyeColor $$5 : DyeColor.values()) {
            DispenserBlock.registerBehavior(ShulkerBoxBlock.getBlockByColor($$5).asItem(), new ShulkerBoxDispenseBehavior());
        }
        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE.asItem(), new OptionalDispenseItemBehavior() {

            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            private ItemStack takeLiquid(BlockSource p_123447_, ItemStack p_123448_, ItemStack p_123449_) {
                p_123448_.shrink(1);
                if (p_123448_.isEmpty()) {
                    p_123447_.getLevel().m_142346_(null, GameEvent.FLUID_PICKUP, p_123447_.getPos());
                    return p_123449_.copy();
                } else {
                    if (p_123447_.<DispenserBlockEntity>getEntity().addItem(p_123449_.copy()) < 0) {
                        this.defaultDispenseItemBehavior.dispense(p_123447_, p_123449_.copy());
                    }
                    return p_123448_;
                }
            }

            @Override
            public ItemStack execute(BlockSource p_123444_, ItemStack p_123445_) {
                this.m_123573_(false);
                ServerLevel $$2 = p_123444_.getLevel();
                BlockPos $$3 = p_123444_.getPos().relative((Direction) p_123444_.getBlockState().m_61143_(DispenserBlock.FACING));
                BlockState $$4 = $$2.m_8055_($$3);
                if ($$4.m_204338_(BlockTags.BEEHIVES, p_123442_ -> p_123442_.m_61138_(BeehiveBlock.HONEY_LEVEL) && p_123442_.getBlock() instanceof BeehiveBlock) && (Integer) $$4.m_61143_(BeehiveBlock.HONEY_LEVEL) >= 5) {
                    ((BeehiveBlock) $$4.m_60734_()).releaseBeesAndResetHoneyLevel($$2, $$4, $$3, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
                    this.m_123573_(true);
                    return this.takeLiquid(p_123444_, p_123445_, new ItemStack(Items.HONEY_BOTTLE));
                } else if ($$2.m_6425_($$3).is(FluidTags.WATER)) {
                    this.m_123573_(true);
                    return this.takeLiquid(p_123444_, p_123445_, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER));
                } else {
                    return super.m_7498_(p_123444_, p_123445_);
                }
            }
        });
        DispenserBlock.registerBehavior(Items.GLOWSTONE, new OptionalDispenseItemBehavior() {

            @Override
            public ItemStack execute(BlockSource p_123452_, ItemStack p_123453_) {
                Direction $$2 = (Direction) p_123452_.getBlockState().m_61143_(DispenserBlock.FACING);
                BlockPos $$3 = p_123452_.getPos().relative($$2);
                Level $$4 = p_123452_.getLevel();
                BlockState $$5 = $$4.getBlockState($$3);
                this.m_123573_(true);
                if ($$5.m_60713_(Blocks.RESPAWN_ANCHOR)) {
                    if ((Integer) $$5.m_61143_(RespawnAnchorBlock.CHARGE) != 4) {
                        RespawnAnchorBlock.charge(null, $$4, $$3, $$5);
                        p_123453_.shrink(1);
                    } else {
                        this.m_123573_(false);
                    }
                    return p_123453_;
                } else {
                    return super.m_7498_(p_123452_, p_123453_);
                }
            }
        });
        DispenserBlock.registerBehavior(Items.SHEARS.asItem(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(Items.HONEYCOMB, new OptionalDispenseItemBehavior() {

            @Override
            public ItemStack execute(BlockSource p_175747_, ItemStack p_175748_) {
                BlockPos $$2 = p_175747_.getPos().relative((Direction) p_175747_.getBlockState().m_61143_(DispenserBlock.FACING));
                Level $$3 = p_175747_.getLevel();
                BlockState $$4 = $$3.getBlockState($$2);
                Optional<BlockState> $$5 = HoneycombItem.getWaxed($$4);
                if ($$5.isPresent()) {
                    $$3.setBlockAndUpdate($$2, (BlockState) $$5.get());
                    $$3.m_46796_(3003, $$2, 0);
                    p_175748_.shrink(1);
                    this.m_123573_(true);
                    return p_175748_;
                } else {
                    return super.m_7498_(p_175747_, p_175748_);
                }
            }
        });
        DispenserBlock.registerBehavior(Items.POTION, new DefaultDispenseItemBehavior() {

            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource p_235896_, ItemStack p_235897_) {
                if (PotionUtils.getPotion(p_235897_) != Potions.WATER) {
                    return this.defaultDispenseItemBehavior.dispense(p_235896_, p_235897_);
                } else {
                    ServerLevel $$2 = p_235896_.getLevel();
                    BlockPos $$3 = p_235896_.getPos();
                    BlockPos $$4 = p_235896_.getPos().relative((Direction) p_235896_.getBlockState().m_61143_(DispenserBlock.FACING));
                    if (!$$2.m_8055_($$4).m_204336_(BlockTags.CONVERTABLE_TO_MUD)) {
                        return this.defaultDispenseItemBehavior.dispense(p_235896_, p_235897_);
                    } else {
                        if (!$$2.f_46443_) {
                            for (int $$5 = 0; $$5 < 5; $$5++) {
                                $$2.sendParticles(ParticleTypes.SPLASH, (double) $$3.m_123341_() + $$2.f_46441_.nextDouble(), (double) ($$3.m_123342_() + 1), (double) $$3.m_123343_() + $$2.f_46441_.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                            }
                        }
                        $$2.m_5594_(null, $$3, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        $$2.m_142346_(null, GameEvent.FLUID_PLACE, $$3);
                        $$2.m_46597_($$4, Blocks.MUD.defaultBlockState());
                        return new ItemStack(Items.GLASS_BOTTLE);
                    }
                }
            }
        });
    }

    static void setEntityPokingOutOfBlock(BlockSource blockSource0, Entity entity1, Direction direction2) {
        entity1.setPos(blockSource0.x() + (double) direction2.getStepX() * (0.5000099999997474 - (double) entity1.getBbWidth() / 2.0), blockSource0.y() + (double) direction2.getStepY() * (0.5000099999997474 - (double) entity1.getBbHeight() / 2.0) - (double) entity1.getBbHeight() / 2.0, blockSource0.z() + (double) direction2.getStepZ() * (0.5000099999997474 - (double) entity1.getBbWidth() / 2.0));
    }
}