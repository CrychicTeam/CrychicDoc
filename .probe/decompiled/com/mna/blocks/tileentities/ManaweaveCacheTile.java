package com.mna.blocks.tileentities;

import com.mna.ManaAndArtifice;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.events.GenericProgressionEvent;
import com.mna.api.events.ProgressionEventIDs;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.interop.lootr.ILootrBridge;
import com.mna.network.messages.to_client.ShowDidYouKnow;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.manaweaving.ManaweaveCacheEffect;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

public class ManaweaveCacheTile extends RandomizableContainerBlockEntity implements ILootrBridge {

    public static final int NUM_PATTERNS = 3;

    public static final int INVENTORY_SIZE = 27;

    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    public Set<UUID> openers = new HashSet();

    private long resetTime = -1L;

    private boolean isOpen = false;

    private float openPct = 0.0F;

    private boolean isBuff = false;

    private boolean generated = false;

    private int tier = 1;

    private UUID tileId;

    private ResourceLocation lootTableId;

    private long lootTableSeed;

    private ResourceLocation buff;

    private int amplifier = 0;

    private int duration = 1;

    private ResourceLocation[] requiredPatterns = new ResourceLocation[3];

    private ManaweavingPattern[] requiredPatternsCache = new ManaweavingPattern[3];

    private ArrayList<ResourceLocation> addedPatterns = new ArrayList();

    public ManaweaveCacheTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.MANAWEAVE_CACHE.get(), pos, state);
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, ManaweaveCacheTile tile) {
        if (!level.isClientSide() && !tile.generated) {
            tile.generate();
        }
        if (!level.isClientSide() && tile.isBuff && tile.shouldReset(state, System.currentTimeMillis())) {
            tile.reset(state);
        } else {
            if (tile.isOpen && tile.openPct < 1.0F) {
                tile.openPct += 0.1F;
            } else if (!tile.isOpen && tile.openPct > 0.0F) {
                tile.openPct -= 0.1F;
            }
            tile.openPct = MathUtils.clamp01(tile.openPct);
            if (tile.isOpen && level.isClientSide() && tile.isBuff) {
                Vec3 cp = Vec3.atCenterOf(pos);
                level.addParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), cp.x, cp.y + 0.675, cp.z, cp.x, cp.y + 1.325, cp.z);
            }
        }
    }

    public boolean notifyPattern(Player player, ResourceLocation pattern) {
        if (this.addedPatterns.size() >= 3) {
            return false;
        } else if (this.requiredPatterns[this.addedPatterns.size()] == null) {
            return false;
        } else if (!this.requiredPatterns[this.addedPatterns.size()].equals(pattern)) {
            return false;
        } else {
            this.addedPatterns.add(pattern);
            this.m_58904_().playSound(null, (double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (this.addedPatterns.size() >= 3) {
                this.open();
                MinecraftForge.EVENT_BUS.post(new GenericProgressionEvent(player, ProgressionEventIDs.OPEN_CACHE));
                if (player instanceof ServerPlayer) {
                    CustomAdvancementTriggers.OPEN_CACHE.trigger((ServerPlayer) player, this.tier);
                }
                if (this.isBuff) {
                    MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(this.buff);
                    if (effect != null) {
                        player.m_7292_(new MobEffectInstance(effect, this.duration, this.amplifier, false, false, true));
                    } else {
                        ManaAndArtifice.LOGGER.warn("Effect " + this.buff.toString() + " was not able to be resolved; skipping.");
                    }
                    ManaAndArtifice.instance.proxy.showDidYouKnow(player, ShowDidYouKnow.Messages.BUFF_CACHE);
                }
            }
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
            return true;
        }
    }

    public float openPct() {
        return this.openPct;
    }

    private void open() {
        this.isOpen = true;
        long resetBase = 3600000L;
        this.resetTime = System.currentTimeMillis() + resetBase + (long) ((int) (Math.random() * (double) resetBase));
        this.m_58904_().playSound(null, (double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_(), SFX.Event.Block.MANAWEAVE_CACHE_OPEN, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private void reset(BlockState state) {
        this.addedPatterns.clear();
        this.isOpen = false;
        this.m_58904_().playSound(null, (double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_(), SFX.Event.Block.MANAWEAVE_CACHE_OPEN, SoundSource.PLAYERS, 1.0F, 1.0F);
        this.m_58904_().sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
    }

    private boolean shouldReset(BlockState state, long time) {
        return this.isBuff && this.isOpen && System.currentTimeMillis() >= this.resetTime;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public boolean isBuff() {
        return this.isBuff;
    }

    public ManaweavingPattern[] getRequiredPatterns() {
        if (this.generated && this.requiredPatternsCache[0] == null) {
            for (int i = 0; i < 3; i++) {
                this.requiredPatternsCache[i] = ManaweavingPatternHelper.GetManaweavingRecipe(this.m_58904_(), this.requiredPatterns[i]);
            }
        }
        return this.requiredPatternsCache;
    }

    public boolean isPatternAdded(int index) {
        return this.addedPatterns.size() > index;
    }

    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(5.0);
    }

    private void generate() {
        this.tier = (int) (Math.random() * 5.0) + 1;
        List<ResourceLocation> patterns = this.getPatternsForTier();
        if (patterns.size() == 0) {
            this.tier = 0;
            patterns = this.getPatternsForTier();
        }
        for (int i = 0; i < 3; i++) {
            this.requiredPatterns[i] = (ResourceLocation) patterns.get((int) (Math.random() * (double) patterns.size()));
        }
        this.isBuff = Math.random() < 0.25;
        if (this.isBuff) {
            this.pickEffectForTier();
        }
        if (!this.isBuff) {
            this.setLootTable(RLoc.create("structure/tier_" + this.tier + "_cache"), this.f_58857_.random.nextLong());
        }
        this.tileId = UUID.randomUUID();
        this.generated = true;
        this.m_6596_();
        this.m_58904_().sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
    }

    private List<ResourceLocation> getPatternsForTier() {
        return (List<ResourceLocation>) this.m_58904_().getRecipeManager().<CraftingContainer, ManaweavingPattern>getAllRecipesFor(RecipeInit.MANAWEAVING_PATTERN_TYPE.get()).stream().filter(r -> r.getTier() <= this.tier).map(r -> r.m_6423_()).collect(Collectors.toList());
    }

    private void pickEffectForTier() {
        List<ManaweaveCacheEffect> effects = (List<ManaweaveCacheEffect>) this.m_58904_().getRecipeManager().<CraftingContainer, ManaweaveCacheEffect>getAllRecipesFor(RecipeInit.MANAWEAVE_CACHE_EFFECT_TYPE.get()).stream().filter(r -> r.getTier() <= this.tier).collect(Collectors.toList());
        if (effects.size() == 0) {
            this.isBuff = false;
        } else {
            ManaweaveCacheEffect myEffect = (ManaweaveCacheEffect) effects.get((int) (Math.random() * (double) effects.size()));
            this.buff = myEffect.getEffect();
            this.amplifier = myEffect.getMagnitude();
            this.duration = myEffect.getDurationMin() + (int) ((double) (myEffect.getDurationMax() - myEffect.getDurationMin()) * Math.random());
        }
    }

    private CompoundTag getCacheData() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("generated", this.generated);
        if (!this.generated) {
            return tag;
        } else {
            tag.putFloat("openPct", this.openPct);
            tag.putBoolean("isOpen", this.isOpen);
            tag.putBoolean("isBuff", this.isBuff);
            tag.putInt("tier", this.tier);
            tag.putString("tileId", this.tileId.toString());
            if (this.lootTableId != null) {
                tag.putString("lootTableId", this.lootTableId.toString());
                tag.putLong("lootTableSeed", this.lootTableSeed);
            }
            if (this.isBuff) {
                tag.putString("buff", this.buff.toString());
                tag.putInt("amplifier", this.amplifier);
                tag.putInt("duration", this.duration);
            }
            for (int i = 0; i < 3; i++) {
                tag.putString("pattern" + i, this.requiredPatterns[i] == null ? "" : this.requiredPatterns[i].toString());
            }
            tag.putInt("numAdded", this.addedPatterns.size());
            for (int i = 0; i < this.addedPatterns.size(); i++) {
                tag.putString("added" + i, ((ResourceLocation) this.addedPatterns.get(i)).toString());
            }
            ListTag list = new ListTag();
            for (UUID opener : this.openers) {
                list.add(NbtUtils.createUUID(opener));
            }
            tag.put("LootrOpeners", list);
            return tag;
        }
    }

    private void parseCachedData(CompoundTag tag) {
        this.generated = tag.getBoolean("generated");
        if (this.generated) {
            this.openPct = tag.getFloat("openPct");
            this.isOpen = tag.getBoolean("isOpen");
            this.isBuff = tag.getBoolean("isBuff");
            this.tier = tag.getInt("tier");
            this.tileId = UUID.fromString(tag.getString("tileId"));
            if (tag.contains("lootTableId")) {
                this.lootTableId = new ResourceLocation(tag.getString("lootTableId"));
                this.lootTableSeed = tag.getLong("lootTableSeed");
            }
            if (this.isBuff) {
                this.buff = new ResourceLocation(tag.getString("buff"));
                this.amplifier = tag.getInt("amplifier");
                this.duration = tag.getInt("duration");
            }
            for (int i = 0; i < 3; i++) {
                this.requiredPatterns[i] = new ResourceLocation(tag.getString("pattern" + i));
            }
            this.addedPatterns.clear();
            for (int i = 0; i < tag.getInt("numAdded"); i++) {
                this.addedPatterns.add(new ResourceLocation(tag.getString("added" + i)));
            }
            if (tag.contains("LootrOpeners")) {
                ListTag openers = tag.getList("LootrOpeners", 11);
                this.openers.clear();
                for (Tag item : openers) {
                    this.openers.add(NbtUtils.loadUUID(item));
                }
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.m_183515_(compound);
        compound.put("cache_data", this.getCacheData());
        if (!this.m_59634_(compound)) {
            ContainerHelper.saveAllItems(compound, this.items);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.m_142466_(compound);
        this.parseCachedData(compound.getCompound("cache_data"));
        if (!this.m_59631_(compound)) {
            ContainerHelper.loadAllItems(compound, this.items);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.getCacheData();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void handleUpdateTag(CompoundTag tag) {
        this.parseCachedData(tag);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) {
            this.parseCachedData(pkt.getTag());
        }
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("mna:container.manaweave_cache");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return ChestMenu.threeRows(id, inventory, this);
    }

    @Override
    public UUID getTileId() {
        return this.tileId;
    }

    @Override
    public void setLootTable(ResourceLocation lootTableIn, long seedIn) {
        this.lootTableId = lootTableIn;
        this.lootTableSeed = seedIn;
        super.setLootTable(lootTableIn, seedIn);
    }

    @Override
    public long getLootSeed() {
        return this.m_58904_().getRandom().nextLong();
    }

    @Override
    public ResourceLocation getLootTable() {
        return this.lootTableId;
    }

    @Override
    public void setLootrOpened(boolean opened) {
    }

    @Override
    public Set<UUID> getOpeners() {
        return this.openers;
    }

    @Override
    public void updatePacketViaState() {
    }
}