package se.mickelus.tetra.blocks.forged.hammer;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.TetraRegistries;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.advancements.BlockUseCriterion;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.blocks.workbench.AbstractWorkbenchBlock;
import se.mickelus.tetra.items.cell.ThermalCellItem;
import se.mickelus.tetra.util.TierHelper;

@ParametersAreNonnullByDefault
public class HammerBaseBlockEntity extends BlockEntity {

    private static final String moduleAKey = "modA";

    private static final String moduleBKey = "modB";

    private static final String slotsKey = "slots";

    private static final String indexKey = "slot";

    private static final String redstoneKey = "rs";

    public static RegistryObject<BlockEntityType<HammerBaseBlockEntity>> type;

    private HammerEffect moduleA;

    private HammerEffect moduleB;

    private ItemStack[] slots;

    private int redstonePower = 0;

    public HammerBaseBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(type.get(), blockPos0, blockState1);
        this.slots = new ItemStack[2];
    }

    public static void writeModules(CompoundTag compound, HammerEffect moduleA, HammerEffect moduleB) {
        if (moduleA != null) {
            compound.put("modA", ByteTag.valueOf((byte) moduleA.ordinal()));
        }
        if (moduleB != null) {
            compound.put("modB", ByteTag.valueOf((byte) moduleB.ordinal()));
        }
    }

    public static void writeCells(CompoundTag compound, ItemStack... cells) {
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] != null) {
                CompoundTag nbttagcompound = new CompoundTag();
                nbttagcompound.putByte("slot", (byte) i);
                cells[i].save(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }
        compound.put("slots", nbttaglist);
    }

    public boolean setModule(boolean isA, Item item) {
        HammerEffect newModule = HammerEffect.fromItem(item);
        if (newModule != null) {
            if (isA) {
                if (this.moduleA == null) {
                    this.moduleA = newModule;
                    this.sync();
                    return true;
                }
            } else if (this.moduleB == null) {
                this.moduleB = newModule;
                this.sync();
                return true;
            }
        }
        return false;
    }

    public Item removeModule(boolean isA) {
        if (isA) {
            if (this.moduleA != null) {
                Item item = this.moduleA.getItem();
                this.moduleA = null;
                this.sync();
                return item;
            }
        } else if (this.moduleB != null) {
            Item item = this.moduleB.getItem();
            this.moduleB = null;
            this.sync();
            return item;
        }
        return null;
    }

    public boolean hasEffect(HammerEffect effect) {
        return effect == this.moduleA || effect == this.moduleB;
    }

    public HammerEffect getEffect(boolean isA) {
        return isA ? this.moduleA : this.moduleB;
    }

    public int getEffectLevel(HammerEffect effect) {
        int level = 0;
        if (effect == this.moduleA) {
            level++;
        }
        if (effect == this.moduleB) {
            level++;
        }
        return level;
    }

    public int getHammerLevel() {
        switch(this.getEffectLevel(HammerEffect.power)) {
            case 1:
                return TierHelper.getIndex(Tiers.NETHERITE) + 1;
            case 2:
                return TierHelper.getIndex(TetraRegistries.forgeHammerTier) + 1;
            default:
                return TierHelper.getIndex(Tiers.DIAMOND) + 1;
        }
    }

    public boolean isFunctional() {
        return this.isFueled() && this.getEffect(true) != null && this.getEffect(false) != null;
    }

    public boolean isFueled() {
        for (int i = 0; i < this.slots.length; i++) {
            if (this.getCellFuel(i) <= 0) {
                return false;
            }
        }
        return true;
    }

    public void consumeFuel() {
        if (!this.f_58857_.isClientSide) {
            int fuelUsage = this.fuelUsage();
            for (int i = 0; i < this.slots.length; i++) {
                this.consumeFuel(i, fuelUsage);
            }
            this.applyConsumeEffect();
            this.sync();
        }
    }

    public void consumeFuel(int index, int amount) {
        if (index >= 0 && index < this.slots.length && this.slots[index] != null && this.slots[index].getItem() instanceof ThermalCellItem) {
            ThermalCellItem item = (ThermalCellItem) this.slots[index].getItem();
            ThermalCellItem.drainCharge(this.slots[index], amount);
        }
    }

    public float getJamChance() {
        return 0.3F - 0.15F * (float) this.getEffectLevel(HammerEffect.reliable);
    }

    public void updateRedstonePower() {
        if (this.f_58857_ != null) {
            int updatedPower = 0;
            for (Direction direction : Direction.values()) {
                updatedPower += this.f_58857_.m_277185_(this.f_58858_.relative(direction), direction);
            }
            if (updatedPower != this.redstonePower) {
                this.redstonePower = updatedPower;
                this.sync();
            }
        }
    }

    private int tickrate() {
        return this.redstonePower != 0 ? (int) Math.max(600.0F / (float) this.redstonePower, 10.0F) : 20;
    }

    private void applyConsumeEffect() {
        Direction facing = (Direction) this.m_58904_().getBlockState(this.m_58899_()).m_61143_(HammerBaseBlock.facingProp);
        Vec3 pos = Vec3.atCenterOf(this.m_58899_());
        Vec3 oppositePos = pos.add(Vec3.atLowerCornerOf(facing.getOpposite().getNormal()).scale(0.55));
        pos = pos.add(Vec3.atLowerCornerOf(facing.getNormal()).scale(0.55));
        if (this.hasEffect(HammerEffect.power)) {
            this.spawnParticle(ParticleTypes.ENCHANTED_HIT, Vec3.atLowerCornerOf(this.m_58899_()).add(0.5, -0.9, 0.5), 15, 0.1F);
        }
        if (this.hasEffect(HammerEffect.power)) {
            this.spawnParticle(ParticleTypes.WHITE_ASH, Vec3.atLowerCornerOf(this.m_58899_()).add(0.5, -0.9, 0.5), 15, 0.1F);
            int count = this.f_58857_.random.nextInt(2 + this.getEffectLevel(HammerEffect.power) * 4);
            if (count > 2) {
                this.spawnParticle(ParticleTypes.LAVA, pos, 2, 0.06F);
                this.spawnParticle(ParticleTypes.LARGE_SMOKE, pos, 2, 0.0F);
                this.spawnParticle(ParticleTypes.LAVA, oppositePos, 2, 0.06F);
                this.spawnParticle(ParticleTypes.LARGE_SMOKE, oppositePos, 2, 0.0F);
                LinkedList<BlockPos> flammableBlocks = new LinkedList();
                for (int x = -3; x < 3; x++) {
                    for (int y = -3; y < 2; y++) {
                        for (int z = -3; z < 3; z++) {
                            BlockPos firePos = this.m_58899_().offset(x, y, z);
                            if (this.f_58857_.m_46859_(firePos)) {
                                flammableBlocks.add(firePos);
                            }
                        }
                    }
                }
                Collections.shuffle(flammableBlocks);
                flammableBlocks.stream().limit((long) count).forEach(blockPos -> this.f_58857_.setBlock(blockPos, Blocks.FIRE.defaultBlockState(), 11));
            }
        }
        if (this.f_58857_.random.nextFloat() < this.getJamChance()) {
            TileEntityOptional.from(this.f_58857_, this.m_58899_().below(), HammerHeadBlockEntity.class).ifPresent(head -> {
                head.setJammed(true);
                this.f_58857_.m_45976_(ServerPlayer.class, new AABB(this.m_58899_()).inflate(10.0, 5.0, 10.0)).forEach(player -> BlockUseCriterion.trigger(player, head.m_58900_(), ItemStack.EMPTY, ImmutableMap.builder().put("jammed", "true").build()));
            });
            this.f_58857_.playSound(null, this.m_58899_(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.8F, 0.5F);
        }
    }

    private int fuelUsage() {
        int usage = 5;
        usage += this.getEffectLevel(HammerEffect.power) * 4;
        usage = (int) ((double) usage * (1.0 - (double) this.getEffectLevel(HammerEffect.efficient) * 0.4));
        return Math.max(usage, 1);
    }

    public boolean hasCellInSlot(int index) {
        return index >= 0 && index < this.slots.length && this.slots[index] != null;
    }

    public int getCellFuel(int index) {
        if (index >= 0 && index < this.slots.length && this.slots[index] != null && this.slots[index].getItem() instanceof ThermalCellItem) {
            ThermalCellItem item = (ThermalCellItem) this.slots[index].getItem();
            return ThermalCellItem.getCharge(this.slots[index]);
        } else {
            return -1;
        }
    }

    public ItemStack removeCellFromSlot(int index) {
        if (index >= 0 && index < this.slots.length && this.slots[index] != null) {
            ItemStack itemStack = this.slots[index];
            this.slots[index] = null;
            this.sync();
            return itemStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < this.slots.length && this.slots[index] != null ? this.slots[index] : ItemStack.EMPTY;
    }

    public boolean putCellInSlot(ItemStack itemStack, int index) {
        if (itemStack.getItem() instanceof ThermalCellItem && index >= 0 && index < this.slots.length && this.slots[index] == null) {
            this.slots[index] = itemStack;
            this.sync();
            return true;
        } else {
            return false;
        }
    }

    private void spawnParticle(ParticleOptions particle, Vec3 pos, int count, float speed) {
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).sendParticles(particle, pos.x, pos.y, pos.z, count, 0.0, 0.0, 0.0, (double) speed);
        }
    }

    public Direction getFacing() {
        return (Direction) this.m_58900_().m_61143_(HammerBaseBlock.facingProp);
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.slots = new ItemStack[2];
        if (compound.contains("slots")) {
            ListTag tagList = compound.getList("slots", 10);
            for (int i = 0; i < tagList.size(); i++) {
                CompoundTag itemCompound = tagList.getCompound(i);
                int slot = itemCompound.getByte("slot") & 255;
                if (slot < this.slots.length) {
                    this.slots[slot] = ItemStack.of(itemCompound);
                }
            }
        }
        this.moduleA = null;
        if (compound.contains("modA")) {
            byte data = compound.getByte("modA");
            if (data < HammerEffect.values().length) {
                this.moduleA = HammerEffect.values()[data];
            }
        }
        this.moduleB = null;
        if (compound.contains("modB")) {
            byte data = compound.getByte("modB");
            if (data < HammerEffect.values().length) {
                this.moduleB = HammerEffect.values()[data];
            }
        }
        this.redstonePower = compound.getInt("rs");
    }

    private void sync() {
        this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
        this.m_6596_();
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        writeCells(compound, this.slots);
        writeModules(compound, this.moduleA, this.moduleB);
        compound.putInt("rs", this.redstonePower);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (this.redstonePower > 0 && level.getGameTime() % (long) this.tickrate() == 0L && level.m_276867_(pos) && this.isFunctional()) {
            BlockPos targetPos = pos.below(2);
            BlockState targetState = level.getBlockState(targetPos);
            HammerHeadBlockEntity head = (HammerHeadBlockEntity) TileEntityOptional.from(level, pos.below(), HammerHeadBlockEntity.class).orElse(null);
            if (head == null || head.isJammed()) {
                return;
            }
            CastOptional.cast(targetState.m_60734_(), IInteractiveBlock.class).map(block -> block.getPotentialInteractions(level, targetPos, targetState, Direction.UP, Collections.singletonList(TetraToolActions.hammer))).stream().flatMap(Arrays::stream).filter(interaction -> TetraToolActions.hammer.equals(interaction.requiredTool)).filter(interaction -> this.getHammerLevel() >= interaction.requiredLevel).findFirst().ifPresent(interaction -> {
                interaction.applyOutcome(level, targetPos, targetState, null, null, Direction.UP);
                if (!(targetState.m_60734_() instanceof AbstractWorkbenchBlock)) {
                    if (!level.isClientSide) {
                        this.consumeFuel();
                    } else {
                        head.activate();
                        level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.2F, (float) (0.5 + Math.random() * 0.2));
                    }
                } else {
                    head.activate();
                }
            });
        }
    }
}