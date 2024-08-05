package dev.xkmc.l2hostility.content.item.beacon;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.NameSetable;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

@SerialClass
public class HostilityBeaconBlockEntity extends BaseBlockEntity implements TickableBlockEntity, MenuProvider, NameSetable, ContainerData {

    private static final int MAX_LEVELS = 3;

    public static final MobEffect[][] BEACON_EFFECTS = new MobEffect[][] { { MobEffects.WEAKNESS, (MobEffect) LCEffects.ICE.get() }, { (MobEffect) LCEffects.FLAME.get(), (MobEffect) LCEffects.CURSE.get() }, { (MobEffect) LCEffects.CLEANSE.get(), (MobEffect) LCEffects.STONE_CAGE.get() } };

    private static final int BLOCKS_CHECK_PER_TICK = 10;

    private static final Component DEFAULT_NAME = Component.translatable("container.beacon");

    List<HostilityBeaconBlockEntity.Section> beamSections = Lists.newArrayList();

    private List<HostilityBeaconBlockEntity.Section> checkingBeamSections = Lists.newArrayList();

    int levels;

    private int lastCheckY;

    @SerialField
    int power = -1;

    @Nullable
    private Component name;

    private LockCode lockKey = LockCode.NO_LOCK;

    public HostilityBeaconBlockEntity(BlockEntityType<? extends HostilityBeaconBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick() {
        if (this.f_58857_ != null) {
            BlockPos pos = this.m_58899_();
            int x = pos.m_123341_();
            int y = pos.m_123342_();
            int z = pos.m_123343_();
            BlockPos start;
            if (this.lastCheckY < y) {
                start = pos;
                this.checkingBeamSections = Lists.newArrayList();
                this.lastCheckY = y - 1;
            } else {
                start = new BlockPos(x, this.lastCheckY + 1, z);
            }
            HostilityBeaconBlockEntity.Section sec = this.checkingBeamSections.isEmpty() ? null : (HostilityBeaconBlockEntity.Section) this.checkingBeamSections.get(this.checkingBeamSections.size() - 1);
            int h = this.f_58857_.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
            for (int i = 0; i < 10 && start.m_123342_() <= h; i++) {
                BlockState state = this.f_58857_.getBlockState(start);
                float[] afloat = state.getBeaconColorMultiplier(this.f_58857_, start, pos);
                if (afloat != null) {
                    if (this.checkingBeamSections.size() <= 1) {
                        sec = new HostilityBeaconBlockEntity.Section(afloat);
                        this.checkingBeamSections.add(sec);
                    } else if (sec != null) {
                        if (Arrays.equals(afloat, sec.color)) {
                            sec.increaseHeight();
                        } else {
                            sec = new HostilityBeaconBlockEntity.Section(new float[] { (sec.color[0] + afloat[0]) / 2.0F, (sec.color[1] + afloat[1]) / 2.0F, (sec.color[2] + afloat[2]) / 2.0F });
                            this.checkingBeamSections.add(sec);
                        }
                    }
                } else {
                    if (sec == null || state.m_60739_(this.f_58857_, start) >= 15 && !state.m_60713_(Blocks.BEDROCK)) {
                        this.checkingBeamSections.clear();
                        this.lastCheckY = h;
                        break;
                    }
                    sec.increaseHeight();
                }
                start = start.above();
                this.lastCheckY++;
            }
            int oldLv = this.levels;
            if (this.f_58857_.getGameTime() % 80L == 0L) {
                if (!this.beamSections.isEmpty()) {
                    this.levels = updateBase(this.f_58857_, x, y, z);
                }
                if (this.levels > 0 && !this.beamSections.isEmpty()) {
                    this.applyEffects(this.f_58857_, pos);
                    playSound(this.f_58857_, pos, SoundEvents.BEACON_AMBIENT);
                }
            }
            if (this.lastCheckY >= h) {
                this.lastCheckY = this.f_58857_.m_141937_() - 1;
                boolean flag = oldLv > 0;
                this.beamSections = this.checkingBeamSections;
                if (!this.f_58857_.isClientSide) {
                    boolean flag1 = this.levels > 0;
                    if (!flag && flag1) {
                        playSound(this.f_58857_, pos, SoundEvents.BEACON_ACTIVATE);
                        AABB aabb = new AABB((double) x, (double) y, (double) z, (double) x, (double) (y - 4), (double) z).inflate(10.0, 5.0, 10.0);
                        for (ServerPlayer serverplayer : this.f_58857_.m_45976_(ServerPlayer.class, aabb)) {
                            CriteriaTriggers.CONSTRUCT_BEACON.trigger(serverplayer, this.levels);
                        }
                    } else if (flag && !flag1) {
                        playSound(this.f_58857_, pos, SoundEvents.BEACON_DEACTIVATE);
                    }
                }
            }
        }
    }

    private static int updateBase(Level level, int x, int y, int z) {
        int ans = 0;
        for (int iy = 1; iy <= 3; ans = iy++) {
            int y0 = y - iy;
            if (y0 < level.m_141937_()) {
                break;
            }
            boolean valid = true;
            for (int ix = x - iy; ix <= x + iy && valid; ix++) {
                for (int iz = z - iy; iz <= z + iy; iz++) {
                    if (!level.getBlockState(new BlockPos(ix, y0, iz)).m_204336_(LHTagGen.BEACON_BLOCK)) {
                        valid = false;
                        break;
                    }
                }
            }
            if (!valid) {
                break;
            }
        }
        return ans;
    }

    @Override
    public void setRemoved() {
        if (this.f_58857_ != null) {
            playSound(this.f_58857_, this.f_58858_, SoundEvents.BEACON_DEACTIVATE);
        }
        super.m_7651_();
    }

    private void applyEffects(Level level, BlockPos pos) {
        if (!level.isClientSide && this.power >= 0) {
            MobEffect eff = BEACON_EFFECTS[this.power / 2][this.power % 2];
            double d0 = (double) (this.levels * 10 + 10);
            int i = 0;
            int j = (9 + this.levels * 2) * 20;
            AABB aabb = new AABB(pos).inflate(d0).expandTowards(0.0, (double) level.m_141928_(), 0.0);
            for (LivingEntity e : level.m_45976_(LivingEntity.class, aabb)) {
                e.addEffect(new MobEffectInstance(eff, j, i, true, true));
            }
        }
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent sound) {
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public List<HostilityBeaconBlockEntity.Section> getBeamSections() {
        return (List<HostilityBeaconBlockEntity.Section>) (this.levels == 0 ? ImmutableList.of() : this.beamSections);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
        }
        this.lockKey = LockCode.fromTag(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Levels", this.levels);
        if (this.name != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        this.lockKey.addToTag(tag);
    }

    public void setCustomName(@Nullable Component name) {
        this.name = name;
    }

    @Nullable
    public Component getCustomName() {
        return this.name;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return this.f_58857_ != null && BaseContainerBlockEntity.canUnlock(player, this.lockKey, this.getDisplayName()) ? new HostilityBeaconMenu((MenuType<HostilityBeaconMenu>) LHBlocks.MT_BEACON.get(), id, inv, this, ContainerLevelAccess.create(this.f_58857_, this.m_58899_())) : null;
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    public Component getName() {
        return this.name != null ? this.name : DEFAULT_NAME;
    }

    @Override
    public void setLevel(Level level) {
        super.m_142339_(level);
        this.lastCheckY = level.m_141937_() - 1;
    }

    @Override
    public int get(int index) {
        return switch(index) {
            case 0 ->
                this.levels;
            case 1 ->
                this.power;
            default ->
                0;
        };
    }

    @Override
    public void set(int index, int value) {
        if (this.f_58857_ != null) {
            switch(index) {
                case 0:
                    this.levels = value;
                    break;
                case 1:
                    if (!this.f_58857_.isClientSide && !this.beamSections.isEmpty()) {
                        playSound(this.f_58857_, this.f_58858_, SoundEvents.BEACON_POWER_SELECT);
                    }
                    this.power = value;
            }
        }
    }

    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public static class Section {

        final float[] color;

        private int height;

        public Section(float[] color) {
            this.color = color;
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