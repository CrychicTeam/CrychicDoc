package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.tile.IEmberInjectable;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.util.Misc;
import com.rekindled.embers.util.sound.ISoundController;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CrystalSeedBlockEntity extends BlockEntity implements IEmberInjectable, ISoundController, IExtraCapabilityInformation {

    public String type;

    public ResourceLocation texture;

    public TagKey<Item> tag;

    public boolean[] willSpawn;

    public int size = 0;

    public int xp = 0;

    public static int bonusParts = 0;

    public int ticksExisted = 0;

    protected static Random random = new Random();

    public static final int SOUND_AMBIENT = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    public CrystalSeedBlockEntity(BlockPos pos, BlockState blockState, String type) {
        this(((RegistryManager.MetalCrystalSeed) RegistryManager.MetalCrystalSeed.seeds.get(type)).BLOCKENTITY.get(), pos, blockState, type);
    }

    public CrystalSeedBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState blockState, String type) {
        super(entityType, pos, blockState);
        this.willSpawn = getSpawns(this.xp);
        this.type = type;
        this.texture = new ResourceLocation("embers:textures/block/material_" + type + ".png");
        this.tag = ItemTags.create(new ResourceLocation("forge", "nuggets/" + type));
    }

    public static boolean[] getSpawns(int xp) {
        int segments = Math.max(6 + bonusParts, 1);
        segments += getLevelBonus(getLevel(xp));
        boolean[] willSpawn = new boolean[segments];
        for (int i = 0; i < willSpawn.length; i++) {
            willSpawn[i] = random.nextInt(3) == 0;
        }
        return willSpawn;
    }

    public static int getLevelBonus(int level) {
        if (level > 50) {
            return getLevelBonus(50) + (level - 50) / 25;
        } else if (level > 20) {
            return getLevelBonus(20) + (level - 20) / 10;
        } else if (level > 10) {
            return getLevelBonus(10) + (level - 10) / 5;
        } else {
            return level > 5 ? getLevelBonus(5) + (level - 5) / 3 : (level + 1) / 2;
        }
    }

    public static String getSpawnString(boolean[] willSpawn) {
        String result = "";
        for (int i = 0; i < willSpawn.length; i++) {
            result = result + (willSpawn[i] ? "1" : "0");
        }
        return result;
    }

    public void loadSpawnsFromString(String s) {
        this.willSpawn = new boolean[s.length()];
        for (int i = 0; i < s.length(); i++) {
            this.willSpawn[i] = s.substring(i, i + 1).compareTo("1") == 0;
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.loadSpawnsFromString(nbt.getString("spawns"));
        this.size = nbt.getInt("size");
        this.xp = nbt.getInt("xp");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putString("spawns", getSpawnString(this.willSpawn));
        nbt.putInt("size", this.size);
        nbt.putInt("xp", this.xp);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putString("spawns", getSpawnString(this.willSpawn));
        nbt.putInt("size", this.size);
        nbt.putInt("xp", this.xp);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CrystalSeedBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        blockEntity.handleSound();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CrystalSeedBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        if (blockEntity.size > 1000) {
            blockEntity.size = 0;
            ItemStack[] stacks = blockEntity.getNuggetDrops(blockEntity.willSpawn.length);
            double oneAng = 360.0 / (double) blockEntity.willSpawn.length;
            for (int i = 0; i < blockEntity.willSpawn.length; i++) {
                if (blockEntity.willSpawn[i]) {
                    ItemStack nuggetStack = stacks[i];
                    float offX = 0.4F * (float) Math.sin(Math.toRadians((double) i * oneAng));
                    float offZ = 0.4F * (float) Math.cos(Math.toRadians((double) i * oneAng));
                    level.m_7967_(new ItemEntity(level, (double) pos.m_123341_() + 0.5 + (double) offX, (double) ((float) pos.m_123342_() + 0.5F), (double) pos.m_123343_() + 0.5 + (double) offZ, nuggetStack));
                    level.playSound(null, (double) pos.m_123341_() + 0.5 + (double) offX, (double) ((float) pos.m_123342_() + 0.5F), (double) pos.m_123343_() + 0.5 + (double) offZ, EmbersSounds.METAL_SEED_PING.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
            blockEntity.willSpawn = getSpawns(blockEntity.xp);
            blockEntity.setChanged();
        }
    }

    protected ItemStack[] getNuggetDrops(int n) {
        return (ItemStack[]) IntStream.range(0, n).mapToObj(i -> new ItemStack(Misc.getTaggedItem(this.tag))).toArray(ItemStack[]::new);
    }

    public void addExperience(int xp) {
        this.xp += xp;
    }

    public int getRequiredExperienceForLevel(int level) {
        return level * (level + 1) / 2 * 1000;
    }

    public static int getLevel(int xp) {
        return (int) Math.floor((Math.sqrt(5.0) * Math.sqrt((double) (xp + 125)) - 25.0) / 50.0);
    }

    @Override
    public void inject(BlockEntity injector, double ember) {
        this.size++;
        this.addExperience(1);
        this.setChanged();
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    @Override
    public void playSound(int id) {
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.METAL_SEED_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() + 0.5F, (float) this.f_58858_.m_123343_() + 0.5F);
            default:
                this.soundsPlaying.add(id);
        }
    }

    @Override
    public void stopSound(int id) {
        this.soundsPlaying.remove(id);
    }

    @Override
    public boolean isSoundPlaying(int id) {
        return this.soundsPlaying.contains(id);
    }

    @Override
    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    @Override
    public boolean shouldPlaySound(int id) {
        return id == 1;
    }

    @Override
    public void addOtherDescription(List<Component> strings, Direction facing) {
        int level = getLevel(this.xp);
        int requiredCurrentXP = this.getRequiredExperienceForLevel(level);
        int requiredNextXP = this.getRequiredExperienceForLevel(level + 1);
        strings.add(Component.translatable("embers.tooltip.crystal.level", level));
        strings.add(Component.translatable("embers.tooltip.crystal.xp", this.xp - requiredCurrentXP, requiredNextXP - requiredCurrentXP));
    }
}