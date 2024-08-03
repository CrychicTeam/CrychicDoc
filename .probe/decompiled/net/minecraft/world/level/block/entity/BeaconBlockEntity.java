package net.minecraft.world.level.block.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

public class BeaconBlockEntity extends BlockEntity implements MenuProvider, Nameable {

    private static final int MAX_LEVELS = 4;

    public static final MobEffect[][] BEACON_EFFECTS = new MobEffect[][] { { MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED }, { MobEffects.DAMAGE_RESISTANCE, MobEffects.JUMP }, { MobEffects.DAMAGE_BOOST }, { MobEffects.REGENERATION } };

    private static final Set<MobEffect> VALID_EFFECTS = (Set<MobEffect>) Arrays.stream(BEACON_EFFECTS).flatMap(Arrays::stream).collect(Collectors.toSet());

    public static final int DATA_LEVELS = 0;

    public static final int DATA_PRIMARY = 1;

    public static final int DATA_SECONDARY = 2;

    public static final int NUM_DATA_VALUES = 3;

    private static final int BLOCKS_CHECK_PER_TICK = 10;

    private static final Component DEFAULT_NAME = Component.translatable("container.beacon");

    List<BeaconBlockEntity.BeaconBeamSection> beamSections = Lists.newArrayList();

    private List<BeaconBlockEntity.BeaconBeamSection> checkingBeamSections = Lists.newArrayList();

    int levels;

    private int lastCheckY;

    @Nullable
    MobEffect primaryPower;

    @Nullable
    MobEffect secondaryPower;

    @Nullable
    private Component name;

    private LockCode lockKey = LockCode.NO_LOCK;

    private final ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int p_58711_) {
            return switch(p_58711_) {
                case 0 ->
                    BeaconBlockEntity.this.levels;
                case 1 ->
                    MobEffect.getIdFromNullable(BeaconBlockEntity.this.primaryPower);
                case 2 ->
                    MobEffect.getIdFromNullable(BeaconBlockEntity.this.secondaryPower);
                default ->
                    0;
            };
        }

        @Override
        public void set(int p_58713_, int p_58714_) {
            switch(p_58713_) {
                case 0:
                    BeaconBlockEntity.this.levels = p_58714_;
                    break;
                case 1:
                    if (!BeaconBlockEntity.this.f_58857_.isClientSide && !BeaconBlockEntity.this.beamSections.isEmpty()) {
                        BeaconBlockEntity.playSound(BeaconBlockEntity.this.f_58857_, BeaconBlockEntity.this.f_58858_, SoundEvents.BEACON_POWER_SELECT);
                    }
                    BeaconBlockEntity.this.primaryPower = BeaconBlockEntity.getValidEffectById(p_58714_);
                    break;
                case 2:
                    BeaconBlockEntity.this.secondaryPower = BeaconBlockEntity.getValidEffectById(p_58714_);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public BeaconBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.BEACON, blockPos0, blockState1);
    }

    public static void tick(Level level0, BlockPos blockPos1, BlockState blockState2, BeaconBlockEntity beaconBlockEntity3) {
        int $$4 = blockPos1.m_123341_();
        int $$5 = blockPos1.m_123342_();
        int $$6 = blockPos1.m_123343_();
        BlockPos $$7;
        if (beaconBlockEntity3.lastCheckY < $$5) {
            $$7 = blockPos1;
            beaconBlockEntity3.checkingBeamSections = Lists.newArrayList();
            beaconBlockEntity3.lastCheckY = blockPos1.m_123342_() - 1;
        } else {
            $$7 = new BlockPos($$4, beaconBlockEntity3.lastCheckY + 1, $$6);
        }
        BeaconBlockEntity.BeaconBeamSection $$9 = beaconBlockEntity3.checkingBeamSections.isEmpty() ? null : (BeaconBlockEntity.BeaconBeamSection) beaconBlockEntity3.checkingBeamSections.get(beaconBlockEntity3.checkingBeamSections.size() - 1);
        int $$10 = level0.getHeight(Heightmap.Types.WORLD_SURFACE, $$4, $$6);
        for (int $$11 = 0; $$11 < 10 && $$7.m_123342_() <= $$10; $$11++) {
            BlockState $$12 = level0.getBlockState($$7);
            Block $$13 = $$12.m_60734_();
            if ($$13 instanceof BeaconBeamBlock) {
                float[] $$14 = ((BeaconBeamBlock) $$13).getColor().getTextureDiffuseColors();
                if (beaconBlockEntity3.checkingBeamSections.size() <= 1) {
                    $$9 = new BeaconBlockEntity.BeaconBeamSection($$14);
                    beaconBlockEntity3.checkingBeamSections.add($$9);
                } else if ($$9 != null) {
                    if (Arrays.equals($$14, $$9.color)) {
                        $$9.increaseHeight();
                    } else {
                        $$9 = new BeaconBlockEntity.BeaconBeamSection(new float[] { ($$9.color[0] + $$14[0]) / 2.0F, ($$9.color[1] + $$14[1]) / 2.0F, ($$9.color[2] + $$14[2]) / 2.0F });
                        beaconBlockEntity3.checkingBeamSections.add($$9);
                    }
                }
            } else {
                if ($$9 == null || $$12.m_60739_(level0, $$7) >= 15 && !$$12.m_60713_(Blocks.BEDROCK)) {
                    beaconBlockEntity3.checkingBeamSections.clear();
                    beaconBlockEntity3.lastCheckY = $$10;
                    break;
                }
                $$9.increaseHeight();
            }
            $$7 = $$7.above();
            beaconBlockEntity3.lastCheckY++;
        }
        int $$15 = beaconBlockEntity3.levels;
        if (level0.getGameTime() % 80L == 0L) {
            if (!beaconBlockEntity3.beamSections.isEmpty()) {
                beaconBlockEntity3.levels = updateBase(level0, $$4, $$5, $$6);
            }
            if (beaconBlockEntity3.levels > 0 && !beaconBlockEntity3.beamSections.isEmpty()) {
                applyEffects(level0, blockPos1, beaconBlockEntity3.levels, beaconBlockEntity3.primaryPower, beaconBlockEntity3.secondaryPower);
                playSound(level0, blockPos1, SoundEvents.BEACON_AMBIENT);
            }
        }
        if (beaconBlockEntity3.lastCheckY >= $$10) {
            beaconBlockEntity3.lastCheckY = level0.m_141937_() - 1;
            boolean $$16 = $$15 > 0;
            beaconBlockEntity3.beamSections = beaconBlockEntity3.checkingBeamSections;
            if (!level0.isClientSide) {
                boolean $$17 = beaconBlockEntity3.levels > 0;
                if (!$$16 && $$17) {
                    playSound(level0, blockPos1, SoundEvents.BEACON_ACTIVATE);
                    for (ServerPlayer $$18 : level0.m_45976_(ServerPlayer.class, new AABB((double) $$4, (double) $$5, (double) $$6, (double) $$4, (double) ($$5 - 4), (double) $$6).inflate(10.0, 5.0, 10.0))) {
                        CriteriaTriggers.CONSTRUCT_BEACON.trigger($$18, beaconBlockEntity3.levels);
                    }
                } else if ($$16 && !$$17) {
                    playSound(level0, blockPos1, SoundEvents.BEACON_DEACTIVATE);
                }
            }
        }
    }

    private static int updateBase(Level level0, int int1, int int2, int int3) {
        int $$4 = 0;
        for (int $$5 = 1; $$5 <= 4; $$4 = $$5++) {
            int $$6 = int2 - $$5;
            if ($$6 < level0.m_141937_()) {
                break;
            }
            boolean $$7 = true;
            for (int $$8 = int1 - $$5; $$8 <= int1 + $$5 && $$7; $$8++) {
                for (int $$9 = int3 - $$5; $$9 <= int3 + $$5; $$9++) {
                    if (!level0.getBlockState(new BlockPos($$8, $$6, $$9)).m_204336_(BlockTags.BEACON_BASE_BLOCKS)) {
                        $$7 = false;
                        break;
                    }
                }
            }
            if (!$$7) {
                break;
            }
        }
        return $$4;
    }

    @Override
    public void setRemoved() {
        playSound(this.f_58857_, this.f_58858_, SoundEvents.BEACON_DEACTIVATE);
        super.setRemoved();
    }

    private static void applyEffects(Level level0, BlockPos blockPos1, int int2, @Nullable MobEffect mobEffect3, @Nullable MobEffect mobEffect4) {
        if (!level0.isClientSide && mobEffect3 != null) {
            double $$5 = (double) (int2 * 10 + 10);
            int $$6 = 0;
            if (int2 >= 4 && mobEffect3 == mobEffect4) {
                $$6 = 1;
            }
            int $$7 = (9 + int2 * 2) * 20;
            AABB $$8 = new AABB(blockPos1).inflate($$5).expandTowards(0.0, (double) level0.m_141928_(), 0.0);
            List<Player> $$9 = level0.m_45976_(Player.class, $$8);
            for (Player $$10 : $$9) {
                $$10.m_7292_(new MobEffectInstance(mobEffect3, $$7, $$6, true, true));
            }
            if (int2 >= 4 && mobEffect3 != mobEffect4 && mobEffect4 != null) {
                for (Player $$11 : $$9) {
                    $$11.m_7292_(new MobEffectInstance(mobEffect4, $$7, 0, true, true));
                }
            }
        }
    }

    public static void playSound(Level level0, BlockPos blockPos1, SoundEvent soundEvent2) {
        level0.playSound(null, blockPos1, soundEvent2, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public List<BeaconBlockEntity.BeaconBeamSection> getBeamSections() {
        return (List<BeaconBlockEntity.BeaconBeamSection>) (this.levels == 0 ? ImmutableList.of() : this.beamSections);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Nullable
    static MobEffect getValidEffectById(int int0) {
        MobEffect $$1 = MobEffect.byId(int0);
        return VALID_EFFECTS.contains($$1) ? $$1 : null;
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.primaryPower = getValidEffectById(compoundTag0.getInt("Primary"));
        this.secondaryPower = getValidEffectById(compoundTag0.getInt("Secondary"));
        if (compoundTag0.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(compoundTag0.getString("CustomName"));
        }
        this.lockKey = LockCode.fromTag(compoundTag0);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        compoundTag0.putInt("Primary", MobEffect.getIdFromNullable(this.primaryPower));
        compoundTag0.putInt("Secondary", MobEffect.getIdFromNullable(this.secondaryPower));
        compoundTag0.putInt("Levels", this.levels);
        if (this.name != null) {
            compoundTag0.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        this.lockKey.addToTag(compoundTag0);
    }

    public void setCustomName(@Nullable Component component0) {
        this.name = component0;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int int0, Inventory inventory1, Player player2) {
        return BaseContainerBlockEntity.canUnlock(player2, this.lockKey, this.getDisplayName()) ? new BeaconMenu(int0, inventory1, this.dataAccess, ContainerLevelAccess.create(this.f_58857_, this.m_58899_())) : null;
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : DEFAULT_NAME;
    }

    @Override
    public void setLevel(Level level0) {
        super.setLevel(level0);
        this.lastCheckY = level0.m_141937_() - 1;
    }

    public static class BeaconBeamSection {

        final float[] color;

        private int height;

        public BeaconBeamSection(float[] float0) {
            this.color = float0;
            this.height = 1;
        }

        protected void increaseHeight() {
            this.height++;
        }

        public float[] getColor() {
            return this.color;
        }

        public int getHeight() {
            return this.height;
        }
    }
}