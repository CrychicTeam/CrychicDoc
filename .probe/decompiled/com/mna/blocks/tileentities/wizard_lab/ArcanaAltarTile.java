package com.mna.blocks.tileentities.wizard_lab;

import com.mna.ManaAndArtifice;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.IManaweaveNotifiable;
import com.mna.api.blocks.tile.BlockPosCache;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.api.sound.SFX;
import com.mna.api.tools.MATags;
import com.mna.blocks.artifice.ArcanaAltarBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.containers.block.ContainerArcaneAltar;
import com.mna.items.ItemInit;
import com.mna.items.manaweaving.ItemManaweaveBottle;
import com.mna.network.ServerMessageDispatcher;
import com.mna.network.messages.to_client.SpawnParticleEffectMessage;
import com.mna.particles.types.movers.ParticleSphereOrbitMover;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.InventoryUtilities;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class ArcanaAltarTile extends WizardLabTile implements GeoAnimatable, IManaweaveNotifiable, MenuProvider {

    private static final int ITEM_CONSUME_WAIT = 20;

    private static final int ITEM_RECHECK_WAIT = 100;

    private static final int INVENTORY_RESCAN_DELAY = 600;

    public static final int COMPLETION_TICKS = 60;

    public static final int SLOT_RECIPE = 0;

    public static final int SLOT_INPUT = 1;

    public static final int SLOT_OUTPUT = 2;

    public static final float growthPerTick = 0.005F;

    private SpellRecipe recipe;

    private HashMap<ResourceLocation, Integer> remainingReagents;

    private HashMap<ResourceLocation, Integer> remainingManaweaves;

    private int totalItems;

    private int totalWeaves;

    private ArrayList<ItemStack> remainingManaweaveItems;

    private IManaweavePattern requestingPattern;

    private List<Item> requestingStack = new ArrayList();

    private ArrayList<Pair<IItemHandler, Direction>> inventories;

    private ArcanaAltarTile.States state = ArcanaAltarTile.States.IDLE;

    private int waitCounter = 0;

    private int inventoryRescanWaitTime = 0;

    private CompoundTag pullParticleMeta;

    private BlockPosCache cache;

    private float targetItemCollectPct = 0.0F;

    private float itemCollectPct = 0.0F;

    private float manaweaveCollectPct = 0.0F;

    private int finalizeTicks = 0;

    public ArcanaAltarTile(BlockPos pWorldPosition, BlockState pBlockState) {
        this(TileEntityInit.ALTAR_OF_ARCANA.get(), pWorldPosition, pBlockState);
    }

    public ArcanaAltarTile(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState, 3);
        this.remainingManaweaves = new HashMap();
        this.remainingReagents = new HashMap();
        this.remainingManaweaveItems = new ArrayList();
        this.inventories = new ArrayList();
        this.cache = new BlockPosCache(this, 8, (p, w) -> {
            BlockEntity be = w.getBlockEntity(p);
            if (be == null) {
                return false;
            } else {
                LazyOptional<IItemHandler> handler = be.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
                return handler.isPresent();
            }
        });
        this.pullParticleMeta = new CompoundTag();
        this.pullParticleMeta.putLong("destination", this.m_58899_().asLong());
    }

    public void reCacheRequirements() {
        this.remainingReagents.clear();
        this.remainingManaweaves.clear();
        this.remainingManaweaveItems.clear();
        ItemStack stack = this.m_8020_(0);
        if (!stack.isEmpty()) {
            SpellRecipe recipe = SpellRecipe.fromNBT(stack.getTag());
            if (recipe.isValid()) {
                this.addAllReagents(SpellRecipe.getShapeReagents(stack));
                this.addAllReagents(SpellRecipe.getComponentReagents(stack));
                this.addAllReagents(SpellRecipe.getModifierReagents(stack, 0));
                this.addAllReagents(SpellRecipe.getModifierReagents(stack, 1));
                this.addAllReagents(SpellRecipe.getModifierReagents(stack, 2));
                SpellRecipe.getPatterns(stack).forEach(rLoc -> {
                    if (this.remainingManaweaves.containsKey(rLoc)) {
                        this.remainingManaweaves.put(rLoc, (Integer) this.remainingManaweaves.get(rLoc) + 1);
                    } else {
                        this.remainingManaweaves.put(rLoc, 1);
                    }
                });
                this.remainingManaweaves.entrySet().forEach(e -> {
                    ItemStack bottle = new ItemStack(ItemInit.MANAWEAVE_BOTTLE.get());
                    IManaweavePattern pattern = ManaweavingPatternHelper.GetManaweavingRecipe(this.m_58904_(), (ResourceLocation) e.getKey());
                    if (pattern != null) {
                        ItemManaweaveBottle.setPattern(pattern, bottle);
                        bottle.setCount((Integer) e.getValue());
                        this.remainingManaweaveItems.add(bottle);
                    }
                });
                this.totalItems = this.remainingReagents.values().stream().mapToInt(i -> i).sum();
                this.totalWeaves = this.remainingManaweaves.values().stream().mapToInt(i -> i).sum();
            }
        }
    }

    public HashMap<ResourceLocation, Integer> getRemainingReagents() {
        return this.remainingReagents;
    }

    public List<ItemStack> getRemainingManaweaves() {
        return this.remainingManaweaveItems;
    }

    @OnlyIn(Dist.CLIENT)
    public Vector3f getCandlePos(int index, float partialTick) {
        float tickOffset = (float) Math.sin((double) (((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTick + (float) (index * 5 * index)) / 40.0F)) * 0.0125F;
        new Vector3f();
        Vector3f offsets = switch(index) {
            case 1 ->
                new Vector3f(0.171875F, 1.125F + tickOffset, 0.25F);
            case 2 ->
                new Vector3f(0.140625F, 1.15625F + tickOffset, 0.5F);
            case 3 ->
                new Vector3f(0.296875F, 1.25F + tickOffset, 0.75F);
            case 4 ->
                new Vector3f(0.703725F, 1.21875F + tickOffset, 0.75F);
            case 5 ->
                new Vector3f(0.859375F, 1.1875F + tickOffset, 0.5F);
            default ->
                new Vector3f(0.828125F, 1.09375F + tickOffset, 0.25F);
        };
        offsets.add(-0.5F, 0.0F, -0.5F);
        return offsets;
    }

    public ArcanaAltarTile.States getState() {
        return this.state;
    }

    @Nullable
    public IManaweavePattern getRequestingPattern() {
        return this.requestingPattern;
    }

    public List<Item> getRequestingStack() {
        return this.requestingStack;
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, ArcanaAltarTile tile) {
        if (tile.waitCounter > 0) {
            tile.waitCounter--;
        }
        if (tile.inventoryRescanWaitTime > 0) {
            tile.inventoryRescanWaitTime--;
        }
        if (tile.cache.isSearching()) {
            tile.cache.tick();
        }
        WizardLabTile.Tick(tile.m_58904_(), pos, state, tile);
        if (tile.state == ArcanaAltarTile.States.FINALIZING) {
            tile.finalizeTicks++;
        }
        if (tile.m_58904_().isClientSide()) {
            tile.ClientTick();
        }
    }

    public void ClientTick() {
        if (this.isActive()) {
            this.spawnParticleForAffinity(Affinity.EARTH, 6);
            this.spawnParticleForAffinity(Affinity.ENDER, 5);
            this.spawnParticleForAffinity(Affinity.WIND, 4);
            this.spawnParticleForAffinity(Affinity.FIRE, 3);
            this.spawnParticleForAffinity(Affinity.ARCANE, 2);
            this.spawnParticleForAffinity(Affinity.WATER, 1);
            Vec3 pos = Vec3.upFromBottomCenterOf(this.m_58899_(), 2.5);
            if (this.state.ordinal() >= ArcanaAltarTile.States.GATHERING_REAGENTS.ordinal() && this.getActiveTicks() > 60) {
                float amount = 20.0F;
                for (int i = 0; (float) i < amount; i++) {
                    float pct = (float) i / amount;
                    double radius = 0.5 * (double) pct;
                    double angle = (double) (360.0F * pct);
                    int[] color = Math.random() < 0.5 ? Affinity.ARCANE.getColor() : Affinity.ARCANE.getSecondaryColor();
                    this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setColor(color[0], color[1], color[2]).setMover(new ParticleSphereOrbitMover(pos.x(), pos.y(), pos.z(), 0.1, angle, radius)), pos.x(), pos.y(), pos.z(), 0.0, 0.0, 0.0);
                }
            }
            if (this.state == ArcanaAltarTile.States.FINALIZING && this.finalizeTicks > 10) {
                for (int i = 0; i < 150; i++) {
                    this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), pos.x(), pos.y() - 1.4, pos.z(), -0.5 + Math.random(), 0.01, -0.5 + Math.random());
                }
            }
            if (this.itemCollectPct < this.targetItemCollectPct) {
                this.itemCollectPct += 0.005F;
            }
            if (this.itemCollectPct > this.targetItemCollectPct) {
                this.itemCollectPct = this.targetItemCollectPct;
            }
        }
    }

    private void spawnParticleForAffinity(Affinity aff, int index) {
        boolean bigBurst = false;
        int age = bigBurst ? 20 : 5;
        int amount = bigBurst ? 20 : 1;
        int[] color = aff.getColor();
        MAParticleType type = new MAParticleType(ParticleInit.FLAME.get()).setColor(color[0], color[1], color[2]).setScale(0.02F).setMaxAge(age + (int) (Math.random() * (double) age));
        BlockPos pos = this.m_58899_();
        Vector3f offset = new Vector3f((float) pos.m_123341_(), (float) pos.m_123342_(), (float) pos.m_123343_());
        Vector3f candle = this.getCandlePos(index, 0.0F);
        BlockState state = this.m_58900_();
        switch((Direction) state.m_61143_(ArcanaAltarBlock.f_54117_)) {
            case EAST:
                candle.rotate(Axis.YP.rotationDegrees(270.0F));
                break;
            case SOUTH:
                candle.rotate(Axis.YP.rotationDegrees(180.0F));
                break;
            case WEST:
                candle.rotate(Axis.YP.rotationDegrees(90.0F));
            case NORTH:
        }
        if (index % 2 == 0) {
            candle.add(0.5F, 0.34375F, 0.5F);
        } else {
            candle.add(0.5F, 0.40625F, 0.5F);
        }
        offset.add(candle);
        float puffSize = -0.005F;
        float puffSize2 = 0.01F;
        for (int i = 0; i < amount; i++) {
            Vector3f velocity = bigBurst ? new Vector3f(puffSize + (float) Math.random() * puffSize2, 0.01F + (float) Math.random() * 0.005F, puffSize + (float) Math.random() * puffSize2) : new Vector3f(0.0F, 0.025F, 0.0F);
            this.m_58904_().addParticle(type, (double) offset.x(), (double) offset.y(), (double) offset.z(), (double) velocity.x(), (double) velocity.y(), (double) velocity.z());
        }
    }

    private void setState(ArcanaAltarTile.States newState) {
        this.state = newState;
        this.syncAndSave();
    }

    private void addAllReagents(Collection<ResourceLocation> reagents) {
        if (reagents != null) {
            reagents.forEach(rLoc -> {
                if (this.remainingReagents.containsKey(rLoc)) {
                    this.remainingReagents.put(rLoc, (Integer) this.remainingReagents.get(rLoc) + 1);
                } else {
                    this.remainingReagents.put(rLoc, 1);
                }
            });
        }
    }

    private void cacheNearbyInventories() {
        if (!this.m_58904_().isClientSide() && this.cache.finishedSearchingThisTick()) {
            this.inventories.clear();
            this.cache.getCachedPositions().forEach(bp -> {
                BlockEntity be = this.m_58904_().getBlockEntity(bp);
                if (be != null) {
                    LazyOptional<IItemHandler> handler = be.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
                    if (handler.isPresent()) {
                        this.inventories.add(new Pair((IItemHandler) handler.resolve().get(), null));
                    }
                }
            });
        }
    }

    private void tickLogic_gatherReagents() {
        if (this.getActiveTicks() >= 100) {
            if (this.remainingReagents.size() == 0) {
                this.setState(ArcanaAltarTile.States.GATHERING_MANAWEAVES);
            } else if (this.waitCounter <= 0) {
                this.cacheNearbyInventories();
                ResourceLocation search = (ResourceLocation) this.remainingReagents.keySet().iterator().next();
                List<Item> items;
                for (items = MATags.smartLookupItem(search); items == null || items.size() == 0; items = MATags.smartLookupItem(search)) {
                    this.remainingReagents.remove(search);
                    if (this.remainingReagents.size() == 0) {
                        return;
                    }
                    search = (ResourceLocation) this.remainingReagents.keySet().iterator().next();
                }
                int count = (Integer) this.remainingReagents.get(search);
                if (InventoryUtilities.consumeAcrossInventories(items, count, true, false, true, this.inventories)) {
                    InventoryUtilities.consumeAcrossInventories(items, count, true, false, false, this.inventories);
                    BlockPos randomInventoryPos = this.cache.getCachedPositions().get((int) ((double) this.cache.getCachedPositions().size() * Math.random()));
                    Vec3 origin = Vec3.atCenterOf(randomInventoryPos);
                    ServerMessageDispatcher.sendParticleEffect(this.m_58904_().dimension(), 32.0F, origin.x, origin.y, origin.z, SpawnParticleEffectMessage.ParticleTypes.ENSORCELLATION_ITEM_PULL, this.pullParticleMeta);
                    this.syncAndSave();
                    this.remainingReagents.remove(search);
                    this.waitCounter = 20;
                    this.requestingStack.clear();
                    this.syncAndSave();
                } else {
                    this.requestingStack.clear();
                    this.requestingStack.addAll(items);
                    this.syncAndSave();
                    if (this.inventoryRescanWaitTime <= 0) {
                        this.inventoryRescanWaitTime = 600;
                        this.cache.queueRecheck();
                    }
                    this.waitCounter = 100;
                }
            }
        }
    }

    private void tickLogic_gatherManaweaves() {
        if (this.remainingManaweaves.size() == 0) {
            this.m_58904_().playSound(null, (double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), SFX.Event.AltarOfArcana.COMPLETE, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.setState(ArcanaAltarTile.States.FINALIZING);
        } else if (this.waitCounter <= 0) {
            this.cacheNearbyInventories();
            ItemStack search = new ItemStack(ItemInit.MANAWEAVE_BOTTLE.get());
            ResourceLocation rLoc = (ResourceLocation) this.remainingManaweaves.keySet().iterator().next();
            IManaweavePattern pattern = ManaweavingPatternHelper.GetManaweavingRecipe(this.m_58904_(), rLoc);
            if (pattern == null) {
                this.remainingManaweaves.remove(rLoc);
            } else {
                int count = (Integer) this.remainingManaweaves.get(rLoc);
                ItemManaweaveBottle.setPattern(pattern, search);
                search.setCount(count);
                if (InventoryUtilities.consumeAcrossInventories(search, true, true, true, this.inventories)) {
                    InventoryUtilities.consumeAcrossInventories(search, true, true, false, this.inventories);
                    BlockPos randomInventoryPos = this.cache.getCachedPositions().get((int) ((double) this.cache.getCachedPositions().size() * Math.random()));
                    Vec3 origin = Vec3.atCenterOf(randomInventoryPos);
                    ServerMessageDispatcher.sendParticleEffect(this.m_58904_().dimension(), 32.0F, origin.x, origin.y, origin.z, SpawnParticleEffectMessage.ParticleTypes.ENSORCELLATION_ITEM_PULL, this.pullParticleMeta);
                    this.syncAndSave();
                    this.remainingManaweaves.remove(rLoc);
                    this.waitCounter = 20;
                    this.syncAndSave();
                } else {
                    if (this.requestingPattern != pattern) {
                        this.requestingPattern = pattern;
                        this.syncAndSave();
                    }
                    if (this.inventoryRescanWaitTime <= 0) {
                        this.inventoryRescanWaitTime = 600;
                        this.cache.queueRecheck();
                    }
                    this.waitCounter = 100;
                }
            }
        }
    }

    private void tickLogic_finalize() {
        if (this.finalizeTicks >= 60) {
            ItemStack output = this.m_8020_(1).copy();
            if (output.getItem() == ItemInit.VELLUM.get()) {
                output = new ItemStack(ItemInit.SPELL.get());
            }
            Player craftingPlayer = this.getCrafter();
            if (craftingPlayer != null && craftingPlayer instanceof ServerPlayer) {
                CustomAdvancementTriggers.CRAFT_SPELL.trigger((ServerPlayer) craftingPlayer, this.recipe);
            }
            this.recipe.writeToNBT(output.getOrCreateTag());
            this.m_6211_();
            this.setItem(2, output);
            this.setState(ArcanaAltarTile.States.IDLE);
        }
    }

    @Override
    public boolean notify(Level world, BlockPos pos, BlockState state, List<IManaweavePattern> patterns, @Nullable LivingEntity activator) {
        if (this.requestingPattern != null && patterns.contains(this.requestingPattern)) {
            Integer remainingCount = (Integer) this.remainingManaweaves.get(this.requestingPattern.getRegistryId());
            if (remainingCount != null) {
                int remaining = remainingCount - 1;
                this.remainingManaweaves.put(this.requestingPattern.getRegistryId(), remaining);
                if ((Integer) this.remainingManaweaves.get(this.requestingPattern.getRegistryId()) <= 0) {
                    this.remainingManaweaves.remove(this.requestingPattern.getRegistryId());
                }
            }
            this.requestingPattern = null;
            this.syncAndSave();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends WizardLabTile> state) {
        return this.isActive() ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.laboratory_ensorcellation_armature.in").thenLoop("animation.laboratory_ensorcellation_armature.working")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.laboratory_ensorcellation_armature.out").thenLoop("animation.laboratory_ensorcellation_armature.idle"));
    }

    @Override
    protected void tickActiveLogic() {
        switch(this.state) {
            case FINALIZING:
                this.tickLogic_finalize();
                break;
            case GATHERING_MANAWEAVES:
                this.tickLogic_gatherManaweaves();
                break;
            case GATHERING_REAGENTS:
                this.tickLogic_gatherReagents();
            case IDLE:
        }
    }

    @Override
    public void onCraftStart(Player crafter) {
        if (!this.m_58904_().isClientSide()) {
            this.reCacheRequirements();
            this.itemCollectPct = 0.0F;
            this.manaweaveCollectPct = 0.0F;
            this.finalizeTicks = 0;
            this.requestingPattern = null;
            this.requestingStack.clear();
            if (this.cache.getCachedPositions().size() == 0) {
                this.inventoryRescanWaitTime = 600;
                this.cache.queueRecheck();
            }
            this.m_58904_().playSound(null, (double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), SFX.Event.AltarOfArcana.START, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.setState(ArcanaAltarTile.States.GATHERING_REAGENTS);
        }
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SFX.Loops.ALTAR_OF_ARCANA;
    }

    @Override
    protected boolean canActivate(Player crafter) {
        ItemStack recipeStack = this.m_8020_(0);
        if (SpellRecipe.stackContainsSpell(recipeStack)) {
            this.recipe = SpellRecipe.fromNBT(recipeStack.getTag());
        }
        if (this.recipe == null || !this.recipe.isValid()) {
            return false;
        } else if (this.m_8020_(1).isEmpty() || this.m_8020_(0).isEmpty()) {
            return false;
        } else if (!this.m_8020_(2).isEmpty()) {
            return false;
        } else {
            LazyOptional<IPlayerProgression> lazyProgression = crafter.getCapability(PlayerProgressionProvider.PROGRESSION);
            if (!lazyProgression.isPresent()) {
                return false;
            } else {
                IPlayerProgression progression = (IPlayerProgression) lazyProgression.resolve().get();
                return this.recipe.getTier(this.f_58857_) > progression.getTier() || this.recipe.getComplexity() > (float) progression.getTierMaxComplexity() ? false : this.recipe.getFaction() == null || this.recipe.getFaction().getAlliedFactions().contains(progression.getAlliedFaction());
            }
        }
    }

    @Override
    protected boolean canContinue() {
        if (this.recipe == null || !this.recipe.isValid()) {
            return false;
        } else {
            return this.m_8020_(1).isEmpty() || this.m_8020_(0).isEmpty() ? false : this.m_8020_(2).isEmpty();
        }
    }

    @Override
    protected List<Integer> getSyncedInventorySlots() {
        ArrayList<Integer> output = new ArrayList();
        output.add(1);
        output.add(0);
        output.add(2);
        return output;
    }

    @Override
    public float getPctComplete() {
        return this.state == ArcanaAltarTile.States.IDLE ? 0.0F : 0.375F * this.itemCollectPct + 0.375F * this.manaweaveCollectPct + 0.25F * ((float) this.finalizeTicks / 60.0F);
    }

    public float getItemCollectPct(float partialTick) {
        return this.itemCollectPct == this.targetItemCollectPct ? this.itemCollectPct : this.itemCollectPct + 0.005F * partialTick;
    }

    public float getManaweaveCollectPct() {
        return this.manaweaveCollectPct;
    }

    public int getCompletionTicks() {
        return this.finalizeTicks;
    }

    public float getBeamPct(float partialTick) {
        return Math.min(((float) this.finalizeTicks + partialTick) / 10.0F, 1.0F);
    }

    @Override
    protected CompoundTag getMeta() {
        CompoundTag meta = new CompoundTag();
        meta.putInt("state", this.state.ordinal());
        if (this.requestingPattern != null) {
            meta.putString("requestingPattern", this.requestingPattern.getRegistryId().toString());
        }
        if (!this.requestingStack.isEmpty()) {
            ListTag itemList = new ListTag();
            this.requestingStack.forEach(i -> itemList.add(StringTag.valueOf(ForgeRegistries.ITEMS.getKey(i).toString())));
            meta.put("requestingStack", itemList);
        }
        int remainingItems = this.remainingReagents.values().stream().mapToInt(i -> i).sum();
        int remainingWeaves = this.remainingManaweaves.values().stream().mapToInt(i -> i).sum();
        this.itemCollectPct = 1.0F - (float) remainingItems / (float) this.totalItems;
        this.manaweaveCollectPct = 1.0F - (float) remainingWeaves / (float) this.totalWeaves;
        meta.putFloat("pctItems", this.itemCollectPct);
        meta.putFloat("pctWeaves", this.manaweaveCollectPct);
        meta.putInt("finalizeTicks", this.finalizeTicks);
        return meta;
    }

    @Override
    protected void loadMeta(CompoundTag tag) {
        if (tag.contains("state")) {
            this.state = ArcanaAltarTile.States.values()[tag.getInt("state")];
        }
        if (tag.contains("pctItems")) {
            this.targetItemCollectPct = tag.getFloat("pctItems");
            if (this.itemCollectPct > this.targetItemCollectPct) {
                this.itemCollectPct = this.targetItemCollectPct;
            }
        } else {
            this.targetItemCollectPct = 0.0F;
            this.itemCollectPct = 0.0F;
        }
        if (tag.contains("finalizeTicks")) {
            this.finalizeTicks = tag.getInt("finalizeTicks");
        } else {
            this.finalizeTicks = 0;
        }
        if (tag.contains("pctWeaves")) {
            this.manaweaveCollectPct = tag.getFloat("pctWeaves");
        } else {
            this.manaweaveCollectPct = 0.0F;
        }
        if (tag.contains("requestingPattern")) {
            this.requestingPattern = ManaweavingPatternHelper.GetManaweavingRecipe(this.m_58904_(), new ResourceLocation(tag.getString("requestingPattern")));
        } else {
            this.requestingPattern = null;
        }
        if (tag.contains("requestingStack")) {
            this.requestingStack.clear();
            ListTag itemList = tag.getList("requestingStack", 8);
            itemList.forEach(t -> {
                ResourceLocation rLoc = new ResourceLocation(((StringTag) t).getAsString());
                Item item = ForgeRegistries.ITEMS.getValue(rLoc);
                if (item != null) {
                    this.requestingStack.add(item);
                }
            });
        } else {
            this.requestingStack.clear();
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        CompoundTag recipeData = new CompoundTag();
        if (this.recipe != null) {
            this.recipe.writeToNBT(recipeData);
            compound.put("recipe", recipeData);
        }
        compound.putInt("state", this.state.ordinal());
        if (this.remainingReagents.size() > 0) {
            ListTag reagentsList = new ListTag();
            this.remainingReagents.forEach((rLoc, count) -> {
                CompoundTag reagentTag = new CompoundTag();
                reagentTag.putString("rLoc", rLoc.toString());
                reagentTag.putInt("count", count);
                reagentsList.add(reagentTag);
            });
            compound.put("remainingReagents", reagentsList);
        }
        if (this.remainingManaweaves.size() > 0) {
            ListTag manaweavesList = new ListTag();
            this.remainingManaweaves.forEach((rLoc, count) -> {
                CompoundTag reagentTag = new CompoundTag();
                reagentTag.putString("rLoc", rLoc.toString());
                reagentTag.putInt("count", count);
                manaweavesList.add(reagentTag);
            });
            compound.put("remainingManaweaves", manaweavesList);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.recipe = SpellRecipe.fromNBT(compound.getCompound("recipe"));
        if (compound.contains("state")) {
            this.state = ArcanaAltarTile.States.values()[compound.getInt("state")];
        }
        if (compound.contains("remainingReagents")) {
            this.remainingReagents.clear();
            ListTag reagentsList = compound.getList("remainingReagents", 10);
            reagentsList.forEach(t -> {
                CompoundTag tag = (CompoundTag) t;
                ResourceLocation rLoc = new ResourceLocation(tag.getString("rLoc"));
                int count = tag.getInt("count");
                this.remainingReagents.put(rLoc, count);
            });
        }
        if (compound.contains("remainingManaweaves")) {
            this.remainingManaweaves.clear();
            ListTag manaweavesList = compound.getList("remainingManaweaves", 10);
            manaweavesList.forEach(t -> {
                CompoundTag tag = (CompoundTag) t;
                ResourceLocation rLoc = new ResourceLocation(tag.getString("rLoc"));
                int count = tag.getInt("count");
                this.remainingManaweaves.put(rLoc, count);
            });
        }
    }

    @Override
    protected boolean shouldLoopingSoundPlay(String ID) {
        return super.shouldLoopingSoundPlay(ID) && this.state != ArcanaAltarTile.States.FINALIZING && this.state != ArcanaAltarTile.States.IDLE;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory pInventory, Player pPlayer) {
        return new ContainerArcaneAltar(id, pInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("mna:container.ensorcellation_station");
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index == 0) {
            if (SpellRecipe.stackContainsSpell(stack)) {
                this.recipe = SpellRecipe.fromNBT(stack.getTag());
                this.reCacheRequirements();
            } else {
                this.recipe = null;
            }
        }
        super.setItem(index, stack);
    }

    public static enum States {

        IDLE, GATHERING_REAGENTS, GATHERING_MANAWEAVES, FINALIZING
    }
}