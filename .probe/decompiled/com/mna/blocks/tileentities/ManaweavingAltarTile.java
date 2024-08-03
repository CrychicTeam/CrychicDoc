package com.mna.blocks.tileentities;

import com.mna.Registries;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.blocks.IRequirePlayerReference;
import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.api.sound.SFX;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.events.EventDispatcher;
import com.mna.network.ServerMessageDispatcher;
import com.mna.network.messages.to_client.SpawnParticleEffectMessage;
import com.mna.recipes.ItemAndPatternCraftingInventory;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternHelper;
import com.mna.recipes.manaweaving.ManaweavingRecipe;
import com.mna.tools.InventoryUtilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.joml.Vector3f;

public class ManaweavingAltarTile extends TileEntityWithInventory implements IRequirePlayerReference<ManaweavingAltarTile> {

    public static final int MAX_ITEMS = 9;

    private ArrayList<String> pendingPatterns;

    private ArrayList<IManaweavePattern> addedPatterns;

    private boolean crafting = false;

    private int craftTicks = 0;

    private ManaweavingRecipe __cachedRecipe;

    private ItemStack __cachedRecipeOutput = ItemStack.EMPTY;

    private UUID __crafterID;

    private Player __crafter;

    private boolean copyNBT = false;

    private int lastCraftPatternCount = 0;

    private int lastCraftTier = 0;

    private IFaction lastCraftFaction = null;

    private ResourceLocation lastCraftRecipeID;

    private ManaweavingRecipe _lastCraftRecipe;

    private NonNullList<ItemStack> lastCraftItems;

    private NonNullList<ItemStack> lastCraftItemsMerged;

    private ItemStack lastCraftOutput = ItemStack.EMPTY;

    private LazyOptional<IItemHandlerModifiable> handler;

    public ManaweavingAltarTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state, 9);
        this.addedPatterns = new ArrayList();
        this.pendingPatterns = new ArrayList();
        this.lastCraftItems = NonNullList.create();
        this.lastCraftItemsMerged = NonNullList.create();
    }

    public ManaweavingAltarTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.MANAWEAVING_ALTAR.get(), pos, state);
    }

    public void pushPattern(IManaweavePattern pattern, Player player) {
        if (!this.crafting) {
            this.setPlayerReference(player);
            this.resolvePendingPatterns();
            if (this.addedPatterns.size() != 6) {
                this.addedPatterns.add(pattern);
                if (this.MatchesRecipe(player)) {
                    this.setCrafting(true);
                }
                if (!this.m_58904_().isClientSide()) {
                    this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 2);
                }
            }
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
        if (!this.m_58904_().isClientSide()) {
            this.m_58904_().sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
        }
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (this.isCrafting()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack output = super.removeItemNoUpdate(index);
            if (!this.m_58904_().isClientSide()) {
                this.m_58904_().sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
            }
            return output;
        }
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (this.isCrafting()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack output = super.removeItem(index, count);
            if (!this.m_58904_().isClientSide()) {
                this.m_58904_().sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
            }
            return output;
        }
    }

    public void popPattern(Player player) {
        if (!this.crafting) {
            this.setPlayerReference(player);
            this.resolvePendingPatterns();
            if (this.addedPatterns.size() > 0) {
                this.addedPatterns.remove(this.addedPatterns.size() - 1);
                if (this.MatchesRecipe(player)) {
                    this.setCrafting(true);
                }
                if (!this.m_58904_().isClientSide()) {
                    this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 2);
                }
            }
        }
    }

    public boolean pushItem(Player player, ItemStack stack) {
        if (this.crafting) {
            return false;
        } else {
            this.setPlayerReference(player);
            if (stack.getCount() != 1) {
                return false;
            } else {
                boolean success = false;
                for (int i = 0; i < 9; i++) {
                    if (this.getItem(i).isEmpty()) {
                        this.setItem(i, stack);
                        success = true;
                        break;
                    }
                }
                if (success && this.MatchesRecipe(player)) {
                    this.setCrafting(true);
                }
                return success;
            }
        }
    }

    public ItemStack popItem(Player player) {
        if (this.crafting) {
            return ItemStack.EMPTY;
        } else {
            this.setPlayerReference(player);
            ItemStack output = ItemStack.EMPTY;
            for (int i = 8; i >= 0; i--) {
                if (!this.getItem(i).isEmpty()) {
                    output = this.removeItemNoUpdate(i);
                    break;
                }
            }
            if (this.MatchesRecipe(player)) {
                this.setCrafting(true);
            }
            return output;
        }
    }

    public void clearPatterns() {
        this.addedPatterns.clear();
    }

    public Collection<IManaweavePattern> getAddedPatterns() {
        this.resolvePendingPatterns();
        return this.addedPatterns;
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag base = super.m_5995_();
        CompoundTag sub = this.writeAdditonal(new CompoundTag());
        base.put("invSync", sub);
        return base;
    }

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        CompoundTag sub = tag.getCompound("invSync");
        this.readAdditional(sub);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag sub = pkt.getTag().getCompound("invSync");
        this.readAdditional(sub);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    public ItemStack[] getDisplayedItems() {
        ItemStack[] stacks = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            stacks[i] = this.getItem(i);
        }
        return stacks;
    }

    private boolean MatchesRecipe(Player player) {
        int tier = 1;
        IFaction faction = null;
        if (player != null) {
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            if (progression != null) {
                tier = progression.getTier();
                faction = progression.getAlliedFaction();
            }
        }
        this.__cachedRecipe = null;
        this.__cachedRecipeOutput = ItemStack.EMPTY;
        ManaweavingRecipe recipe = (ManaweavingRecipe) this.m_58904_().getRecipeManager().getRecipeFor(RecipeInit.MANAWEAVING_RECIPE_TYPE.get(), this.createCraftingInventory(), this.f_58857_).orElse(null);
        if (recipe != null && recipe.getTier() <= tier) {
            this.__cachedRecipe = recipe;
            this.__cachedRecipeOutput = recipe.getResultItem().copy();
            this.copyNBT = recipe.getCopyNBT();
        }
        if (this.__cachedRecipeOutput != null && !this.__cachedRecipeOutput.isEmpty()) {
            if (this.__cachedRecipe.getFactionRequirement() != null && this.__cachedRecipe.getFactionRequirement() != faction) {
                this.__cachedRecipeOutput = ItemStack.EMPTY;
                this.__cachedRecipe = null;
                if (player != null) {
                    player.m_213846_(Component.translatable("gui.mna.recipe.wrongfaction"));
                }
                return false;
            } else {
                this.lastCraftTier = recipe.getTier();
                this.lastCraftFaction = recipe.getFactionRequirement();
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        this.writeAdditonal(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        this.readAdditional(compound);
        super.load(compound);
    }

    private CompoundTag writeAdditonal(CompoundTag compound) {
        ContainerHelper.saveAllItems(compound, this.inventoryItems);
        this.resolvePendingPatterns();
        CompoundTag patterns = new CompoundTag();
        int count = 0;
        for (IManaweavePattern pattern : this.addedPatterns) {
            patterns.putString("pattern_" + count++, pattern.getRegistryId().toString());
        }
        compound.putInt("patterns_count", this.addedPatterns.size());
        compound.put("patterns", patterns);
        compound.putBoolean("crafting", this.crafting);
        compound.putBoolean("copyNBT", this.copyNBT);
        compound.putInt("craftTicks", this.craftTicks);
        CompoundTag lastCraft = new CompoundTag();
        lastCraft.put("lastCraftReagents", ContainerHelper.saveAllItems(new CompoundTag(), this.lastCraftItems));
        lastCraft.putInt("patterns_count", this.lastCraftPatternCount);
        lastCraft.putInt("tier", this.lastCraftTier);
        if (this.lastCraftFaction != null) {
            lastCraft.putString("faction", ((IForgeRegistry) Registries.Factions.get()).getKey(this.lastCraftFaction).toString());
        }
        lastCraft.put("lastCraftOutput", this.lastCraftOutput.save(new CompoundTag()));
        if (this.lastCraftRecipeID != null) {
            lastCraft.putString("lastCraftRecipeID", this.lastCraftRecipeID.toString());
        }
        compound.put("lastCraft", lastCraft);
        if (this.__crafterID != null) {
            compound.putString("crafter_uuid", this.__crafterID.toString());
        }
        return compound;
    }

    private void readAdditional(CompoundTag compound) {
        this.m_6211_();
        this.addedPatterns.clear();
        this.pendingPatterns.clear();
        this.lastCraftItems.clear();
        for (int i = 0; i < 9; i++) {
            this.lastCraftItems.add(ItemStack.EMPTY);
        }
        ContainerHelper.loadAllItems(compound, this.inventoryItems);
        if (compound.contains("patterns") && compound.contains("patterns_count")) {
            int count = compound.getInt("patterns_count");
            CompoundTag patterns = compound.getCompound("patterns");
            for (int i = 0; i < count; i++) {
                String identifier = "pattern_" + i;
                if (patterns.contains(identifier)) {
                    this.pendingPatterns.add(patterns.getString(identifier));
                }
            }
        }
        this.crafting = compound.getBoolean("crafting");
        this.craftTicks = compound.getInt("craftTicks");
        this.copyNBT = compound.getBoolean("copyNBT");
        if (compound.contains("crafter_uuid")) {
            this.__crafterID = UUID.fromString(compound.getString("crafter_uuid"));
        }
        if (compound.contains("lastCraft")) {
            CompoundTag lastCraft = compound.getCompound("lastCraft");
            this.lastCraftOutput = ItemStack.of(lastCraft.getCompound("lastCraftOutput"));
            ContainerHelper.loadAllItems(lastCraft.getCompound("lastCraftReagents"), this.lastCraftItems);
            this.lastCraftPatternCount = lastCraft.getInt("patterns_count");
            this.lastCraftTier = lastCraft.getInt("tier");
            if (lastCraft.contains("faction")) {
                ResourceLocation factionID = new ResourceLocation(lastCraft.getString("faction").toLowerCase());
                this.lastCraftFaction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(factionID);
            }
            this.lastCraftRecipeID = new ResourceLocation(lastCraft.getString("lastCraftRecipeID"));
            for (ItemStack stack : this.lastCraftItems) {
                Optional<ItemStack> existing = this.lastCraftItemsMerged.stream().filter(is -> ItemStack.matches(is, stack)).findFirst();
                if (existing.isPresent()) {
                    ((ItemStack) existing.get()).setCount(((ItemStack) existing.get()).getCount() + stack.getCount());
                } else {
                    this.lastCraftItemsMerged.add(stack.copy());
                }
            }
        }
    }

    public void spawnCraftingParticles() {
        if (this.m_58904_().isClientSide()) {
            Random rnd = new Random();
            BlockPos pos = this.m_58899_();
            Vector3f srcPoint = new Vector3f((float) pos.m_123341_() + 0.5F, (float) pos.m_123342_() + 0.85F, (float) pos.m_123343_() + 0.5F);
            for (int i = 0; i < 10; i++) {
                Vector3f lightPoint = new Vector3f(srcPoint.x() - 0.2F + rnd.nextFloat() * 0.4F, srcPoint.y(), srcPoint.z() - 0.2F + rnd.nextFloat() * 0.4F);
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.LIGHT_VELOCITY.get()).setScale(0.1F), (double) lightPoint.x(), (double) lightPoint.y(), (double) lightPoint.z(), 0.0, 0.01, 0.0);
            }
            float SPEED = 0.1F;
            float RADIUS = 0.25F;
            for (int i = 0; (double) i < (double) this.craftTicks * 1.5; i++) {
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_SPHERE_ORBIT.get()), (double) srcPoint.x(), (double) (srcPoint.y() + 0.4F), (double) srcPoint.z(), (double) SPEED, (double) rnd.nextInt(360), (double) RADIUS);
            }
            if (this.getCraftTicks() > this.getMaxCraftTicks() - 20) {
                RADIUS = 0.6F - rnd.nextFloat() * 0.25F;
                for (int i = 0; i < 100; i++) {
                    this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_SPHERE_ORBIT.get()), (double) srcPoint.x(), (double) (srcPoint.y() + 0.2F), (double) srcPoint.z(), (double) SPEED + rnd.nextGaussian() * 0.1F, (double) (-30 + rnd.nextInt(30)), (double) RADIUS);
                }
            }
        }
    }

    private void resolvePendingPatterns() {
        if (this.f_58857_ != null && this.pendingPatterns.size() > 0) {
            for (String s : this.pendingPatterns) {
                ManaweavingPattern p = ManaweavingPatternHelper.GetManaweavingRecipe(this.m_58904_(), new ResourceLocation(s));
                if (p != null) {
                    this.addedPatterns.add(p);
                }
            }
            this.pendingPatterns.clear();
        }
    }

    private void setCrafting(boolean crafting) {
        this.crafting = crafting;
        if (!this.m_58904_().isClientSide() && this.crafting) {
            this.m_58904_().playSound(null, this.m_58899_(), SFX.Event.Block.MANAWEAVE_ALTAR_CRAFT, SoundSource.BLOCKS, 1.0F, (float) (0.95F + Math.random() * 0.05F));
        }
    }

    public boolean isCrafting() {
        return this.crafting;
    }

    public int getCraftTicks() {
        return this.craftTicks;
    }

    public int getMaxCraftTicks() {
        return 85;
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, ManaweavingAltarTile tile) {
        if (tile.crafting) {
            tile.spawnCraftingParticles();
            tile.craftTicks++;
            if (tile.craftTicks >= tile.getMaxCraftTicks()) {
                tile.setCrafting(false);
                tile.craftTicks = 0;
                if (!tile.m_58904_().isClientSide()) {
                    if (!EventDispatcher.DispatchManaweaveCrafting(tile.__cachedRecipe, tile.__cachedRecipeOutput, tile.getCrafter())) {
                        level.sendBlockUpdated(tile.m_58899_(), tile.m_58900_(), tile.m_58900_(), 2);
                        return;
                    }
                    if (tile.copyNBT) {
                        CompoundTag nbt = tile.__cachedRecipeOutput.getOrCreateTag();
                        for (ItemStack stack : tile.inventoryItems) {
                            if (stack.hasTag()) {
                                nbt.merge(stack.getTag());
                            }
                        }
                    }
                    tile.copyCraftInputToLastCraft();
                    ServerMessageDispatcher.sendParticleEffect(tile.m_58904_().dimension(), 32.0F, (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.3F), (double) ((float) pos.m_123343_() + 0.5F), SpawnParticleEffectMessage.ParticleTypes.MANAWEAVE_CRAFT_COMPLETE);
                }
                tile.pendingPatterns.clear();
                tile.addedPatterns.clear();
                tile.m_6211_();
                Player crafter = tile.getCrafter();
                if (crafter != null & crafter instanceof ServerPlayer) {
                    MutableBoolean factionMatch = new MutableBoolean(false);
                    crafter.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                        if (p.getAlliedFaction() != null && tile.__cachedRecipe.getFactionRequirement() == p.getAlliedFaction()) {
                            factionMatch.setTrue();
                        }
                    });
                    CustomAdvancementTriggers.MANAWEAVE_CRAFT.trigger((ServerPlayer) crafter, tile.__cachedRecipeOutput, factionMatch.booleanValue());
                }
                if (tile.__cachedRecipe != null) {
                    tile.lastCraftRecipeID = tile.__cachedRecipe.m_6423_();
                    tile._lastCraftRecipe = tile.__cachedRecipe;
                    int count = 1;
                    for (ItemStack byproduct : tile.__cachedRecipe.rollByproducts(level.getRandom())) {
                        if (count++ < 9) {
                            tile.setItem(count, byproduct);
                        } else {
                            InventoryUtilities.DropItemAt(byproduct, Vec3.atCenterOf(pos.above()), level, true);
                        }
                    }
                }
                tile.setOutputItem(tile.__cachedRecipeOutput);
            }
        }
    }

    private void setOutputItem(ItemStack stack) {
        if (!this.m_58904_().isClientSide()) {
            this.ignoreQtyLimit = true;
            if (stack != null) {
                this.setItem(0, stack);
            }
            this.ignoreQtyLimit = false;
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 2);
        }
    }

    @Override
    public void setPlayerReference(Player player) {
        this.__crafter = player;
        this.__crafterID = player.m_20148_();
    }

    private boolean checkReCraftReagents(Player player) {
        if (player.isCreative()) {
            return true;
        } else {
            MutableBoolean progressionMet = new MutableBoolean(false);
            player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                if (p.getTier() < this.lastCraftTier) {
                    player.m_213846_(Component.translatable("gui.mna.recipe.lowtier"));
                } else if (this.lastCraftFaction != null && p.getAlliedFaction() != this.lastCraftFaction) {
                    player.m_213846_(Component.translatable("gui.mna.recipe.wrongfaction"));
                } else {
                    progressionMet.setTrue();
                }
            });
            if (!progressionMet.booleanValue()) {
                return false;
            } else {
                float mana = (float) this.getReCraftManaCost();
                MutableBoolean enoughMana = new MutableBoolean(true);
                player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> enoughMana.setValue(m.getCastingResource().hasEnoughAbsolute(player, mana)));
                if (!enoughMana.getValue()) {
                    player.m_213846_(Component.translatable("gui.mna.recipe.missing_mana"));
                    return false;
                } else {
                    MutableBoolean reagentsPresent = new MutableBoolean(true);
                    this.lastCraftItemsMerged.forEach(stack -> {
                        if (!stack.isEmpty() && !InventoryUtilities.hasStackInInventory(stack, true, true, new InvWrapper(player.getInventory()))) {
                            reagentsPresent.setFalse();
                        }
                    });
                    if (!reagentsPresent.getValue()) {
                        player.m_213846_(Component.translatable("gui.mna.recipe.missing_items"));
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    private void consumeReCraftReagents(Player player) {
        if (!player.isCreative()) {
            float mana = (float) this.getReCraftManaCost();
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().consume(player, mana));
            this.lastCraftItemsMerged.forEach(stack -> InventoryUtilities.removeItemFromInventory(stack, true, true, new InvWrapper(player.getInventory())));
        }
    }

    private void copyCraftInputToLastCraft() {
        this.lastCraftItems.clear();
        this.lastCraftItemsMerged.clear();
        for (ItemStack stack : this.inventoryItems) {
            this.lastCraftItems.add(stack.copy());
            Optional<ItemStack> existing = this.lastCraftItemsMerged.stream().filter(is -> ItemStack.matches(is, stack)).findFirst();
            if (existing.isPresent()) {
                ((ItemStack) existing.get()).setCount(((ItemStack) existing.get()).getCount() + stack.getCount());
            } else {
                this.lastCraftItemsMerged.add(stack.copy());
            }
        }
        this.lastCraftPatternCount = this.addedPatterns.size();
        this.lastCraftOutput = this.__cachedRecipeOutput.copy();
    }

    public boolean reCraft(Player player) {
        if (this.addedPatterns.size() != 0 || !this.m_7983_() || this.m_58904_().isClientSide()) {
            return false;
        } else if (this.checkReCraftReagents(player)) {
            ServerMessageDispatcher.sendParticleEffect(this.m_58904_().dimension(), 32.0F, (double) ((float) this.m_58899_().m_123341_() + 0.5F), (double) ((float) this.m_58899_().m_123342_() + 1.1F), (double) ((float) this.m_58899_().m_123343_() + 0.5F), SpawnParticleEffectMessage.ParticleTypes.MANAWEAVE_CRAFT_COMPLETE);
            this.m_58904_().playSound(null, (double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, (float) (0.9 + Math.random() * 0.2));
            this.setPlayerReference(player);
            this.consumeReCraftReagents(player);
            ItemStack output = this.lastCraftOutput.copy();
            if (player instanceof ServerPlayer) {
                MutableBoolean factionMatch = new MutableBoolean(false);
                player.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                    if (p.getAlliedFaction() != null && this.__cachedRecipe != null && this.__cachedRecipe.getFactionRequirement() == p.getAlliedFaction()) {
                        factionMatch.setTrue();
                    }
                });
                CustomAdvancementTriggers.MANAWEAVE_CRAFT.trigger((ServerPlayer) player, output, factionMatch.booleanValue());
            }
            boolean sendUpdate = false;
            if (!player.getInventory().add(output)) {
                this.setOutputItem(output);
                sendUpdate = true;
            }
            if (this._lastCraftRecipe == null && this.lastCraftRecipeID != null) {
                this.f_58857_.getRecipeManager().byKey(this.lastCraftRecipeID).ifPresent(recipe -> {
                    if (recipe instanceof ManaweavingRecipe mwRecipe) {
                        this._lastCraftRecipe = mwRecipe;
                    }
                });
            }
            if (this._lastCraftRecipe != null) {
                int count = 1;
                for (ItemStack byproduct : this._lastCraftRecipe.rollByproducts(this.f_58857_.getRandom())) {
                    if (!player.getInventory().add(byproduct)) {
                        if (count++ < 9) {
                            this.setItem(count, byproduct);
                            sendUpdate = true;
                        } else {
                            InventoryUtilities.DropItemAt(byproduct, Vec3.atCenterOf(this.m_58899_().above()), this.f_58857_, true);
                        }
                    }
                }
            }
            if (sendUpdate) {
                this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 2);
            }
            return true;
        } else {
            return false;
        }
    }

    public ItemStack getReCraftOutput() {
        return this.lastCraftOutput;
    }

    @Nullable
    public IFaction getLastCraftFaction() {
        return this.lastCraftFaction;
    }

    public int getLastCraftTier() {
        return this.lastCraftTier;
    }

    public int getLastCraftPatternCount() {
        return this.lastCraftPatternCount;
    }

    public void setReCraftRecipe(ItemStack output, List<ItemStack> inputs, IFaction faction, int tier, int patternCount) {
        this.lastCraftOutput = output;
        this.lastCraftItems.clear();
        this.lastCraftItemsMerged.clear();
        for (ItemStack stack : inputs) {
            this.lastCraftItems.add(stack.copy());
            Optional<ItemStack> existing = this.lastCraftItemsMerged.stream().filter(is -> ItemStack.matches(is, stack)).findFirst();
            if (existing.isPresent()) {
                ((ItemStack) existing.get()).setCount(((ItemStack) existing.get()).getCount() + stack.getCount());
            } else {
                this.lastCraftItemsMerged.add(stack.copy());
            }
        }
        this.lastCraftFaction = faction;
        this.lastCraftTier = tier;
        this.lastCraftPatternCount = patternCount;
    }

    public int getReCraftManaCost() {
        return this.lastCraftPatternCount * GeneralConfigValues.AverageManaweaveCost;
    }

    public List<ItemStack> getReCraftInput() {
        return (List<ItemStack>) this.lastCraftItems.stream().filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
    }

    private Player getCrafter() {
        if (this.__crafter == null && this.__crafterID != null) {
            this.__crafter = this.m_58904_().m_46003_(this.__crafterID);
        }
        return this.__crafter;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return this.getCrafter() != null && this.getItem(index).isEmpty() && itemStackIn.getCount() == 1;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return !this.isCrafting() && this.getCrafter() != null && !this.getItem(index).isEmpty() && stack.getCount() == 1;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expandTowards(0.0, 1.0, 0.0);
    }

    private ItemAndPatternCraftingInventory createCraftingInventory() {
        ArrayList<String> patterns = new ArrayList();
        for (IManaweavePattern p : this.addedPatterns) {
            patterns.add(p.getRegistryId().toString());
        }
        for (String s : this.pendingPatterns) {
            patterns.add(s);
        }
        ItemAndPatternCraftingInventory craftinginventory = new ItemAndPatternCraftingInventory(9, patterns);
        for (int i = 0; i < 9; i++) {
            craftinginventory.m_6836_(i, this.getItem(i));
        }
        return craftinginventory;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (this.handler == null) {
                this.handler = LazyOptional.of(this::createHandler);
            }
            return this.handler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    private IItemHandlerModifiable createHandler() {
        BlockState state = this.m_58900_();
        if (!(state.m_60734_() instanceof ChestBlock)) {
            return new InvWrapper(this);
        } else {
            Container inv = ChestBlock.getContainer((ChestBlock) state.m_60734_(), state, this.m_58904_(), this.m_58899_(), true);
            return new InvWrapper((Container) (inv == null ? this : inv));
        }
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return this.getCrafter() != null ? super.m_7013_(index, stack) : false;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.inventoryItems.get(index);
    }
}