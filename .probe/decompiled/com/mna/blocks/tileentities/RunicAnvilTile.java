package com.mna.blocks.tileentities;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.events.RunicAnvilShouldActivateEvent;
import com.mna.blocks.runeforging.RunicAnvilBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.enchantments.auras.Aura;
import com.mna.events.EventDispatcher;
import com.mna.items.ItemInit;
import com.mna.items.armor.BrokenMageArmor;
import com.mna.items.ritual.ItemPractitionersPatch;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.ritual.PractitionersPouchPatches;
import com.mna.items.runes.ItemRune;
import com.mna.items.runes.ItemRunePattern;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.manaweaving.ManaweavingRecipe;
import com.mna.recipes.runeforging.RuneforgingRecipe;
import com.mna.tools.ContainerTools;
import com.mna.tools.InventoryUtilities;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import net.minecraftforge.eventbus.api.Event.Result;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RunicAnvilTile extends TileEntityWithInventory implements IForgeBlockEntity, WorldlyContainer, GeoBlockEntity {

    private static final int MAX_ITEMS = 2;

    public static final int PATTERN_SLOT_INDEX = 0;

    public static final int MATERIAL_SLOT_INDEX = 1;

    public static final int TOOL_SLOT_INDEX = 2;

    public static final int ADVANCE_RESPONSE_ADVANCED = 0;

    public static final int ADVANCE_RESPONSE_NO_MATCH = 1;

    public static final int ADVANCE_RESPONSE_LOW_TIER = 2;

    public static final int ADVANCE_RESPONSE_CLIENT_WORLD = 3;

    public static final int ADVANCE_RESPONSE_CRAFTING_COMPLETE = 4;

    public static final int ADVANCE_RESPONSE_EVENT_CANCELED = 5;

    public int craftProgress = 0;

    public int maxCraftProgress = 10;

    private ItemStack __cachedRecipeOutput = ItemStack.EMPTY;

    private RuneforgingRecipe __cachedRecipe;

    private Optional<ManaweavingRecipe> __cachedEnchantRecipe;

    private boolean autoCacheRecipe = true;

    private boolean settingCraftOutput = false;

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public RunicAnvilTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state, 2);
        this.__cachedRecipeOutput = ItemStack.EMPTY;
    }

    public RunicAnvilTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.RUNIC_ANVIL.get(), pos, state);
    }

    public int advanceCrafting(Player crafter, int playerTier) {
        return this.advanceCrafting(crafter, playerTier, true);
    }

    public int advanceCrafting(Player crafter, int playerTier, boolean allowDurability) {
        int cacheResponse = this.cacheRecipe(crafter, playerTier);
        if (this.m_58904_().isClientSide()) {
            return 3;
        } else if (this.__cachedRecipeOutput.isEmpty()) {
            return cacheResponse;
        } else {
            this.craftProgress++;
            int targetProgress = this.getMaxCraftProgress();
            if (this.craftProgress < targetProgress) {
                this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 2);
                return 0;
            } else {
                this.craftProgress = 0;
                if (this.__cachedRecipe != null) {
                    if (!EventDispatcher.DispatchRuneforgeCraft(this.__cachedRecipe, this.__cachedRecipeOutput, crafter)) {
                        return 5;
                    }
                } else if ((this.__cachedEnchantRecipe != null || EnchantmentHelper.getEnchantments(this.__cachedRecipeOutput).size() > 0) && !EventDispatcher.DispatchRuneforgeEnchant(this.__cachedRecipeOutput, crafter)) {
                    return 5;
                }
                this.completeCraftingAndReset(crafter, allowDurability);
                this.m_58904_().setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(RunicAnvilBlock.ACTIVE, false));
                this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 2);
                return 4;
            }
        }
    }

    private void completeCraftingAndReset(Player crafter, boolean allowPatternDurability) {
        this.settingCraftOutput = true;
        if (this.__cachedRecipe != null) {
            for (ItemStack byproduct : this.__cachedRecipe.rollByproducts(this.f_58857_.getRandom())) {
                InventoryUtilities.DropItemAt(byproduct, Vec3.atCenterOf(this.m_58899_().above()), this.f_58857_, true);
            }
        }
        ItemStack patternStack = this.m_8020_(0);
        if (!allowPatternDurability || !(patternStack.getItem() instanceof ItemRunePattern)) {
            this.setItem(0, this.__cachedRecipeOutput);
            this.setItem(1, ItemStack.EMPTY);
        } else if (!ItemRunePattern.incrementDamage(patternStack)) {
            this.setItem(1, this.__cachedRecipeOutput);
        } else {
            this.m_58904_().playSound(null, this.m_58899_(), SoundEvents.NETHER_BRICKS_BREAK, SoundSource.BLOCKS, 1.0F, (float) (0.8 + Math.random() * 0.4));
            this.setItem(0, this.__cachedRecipeOutput);
            this.setItem(1, ItemStack.EMPTY);
        }
        this.settingCraftOutput = false;
        if (crafter != null & crafter instanceof ServerPlayer) {
            CustomAdvancementTriggers.RUNIC_ANVIL_CRAFT.trigger((ServerPlayer) crafter, this.m_8020_(0));
        }
        this.__cachedRecipeOutput = ItemStack.EMPTY;
        this.__cachedRecipe = null;
        this.__cachedEnchantRecipe = null;
    }

    public int getMaxCraftProgress() {
        return this.__cachedRecipe != null ? this.__cachedRecipe.getHits() : this.maxCraftProgress;
    }

    private int cacheRecipe(Player player, int playerTier) {
        int output = 1;
        if (this.cacheSpecialRecipes(playerTier)) {
            boolean isActive = (Boolean) this.m_58900_().m_61143_(RunicAnvilBlock.ACTIVE);
            if (!isActive) {
                this.m_58904_().setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(RunicAnvilBlock.ACTIVE, true), 3);
            }
        } else {
            CraftingContainer inv = this.createDummyCraftingInventory();
            this.__cachedRecipe = (RuneforgingRecipe) this.m_58904_().getRecipeManager().getRecipeFor(RecipeInit.RUNEFORGING_TYPE.get(), inv, this.f_58857_).orElse(null);
            this.__cachedRecipeOutput = ItemStack.EMPTY;
            if (this.__cachedRecipe == null) {
                ItemStack rune = this.m_8020_(1);
                if (!rune.isEmpty()) {
                    output = this.cacheEnchantRecipe(rune, player, playerTier);
                }
            } else if (this.__cachedRecipe.getTier() <= playerTier) {
                this.__cachedRecipeOutput = this.__cachedRecipe.getResultItem();
                output = 0;
            } else {
                this.__cachedRecipe = null;
                this.__cachedRecipeOutput = ItemStack.EMPTY;
                output = 2;
            }
            boolean isActive = (Boolean) this.m_58900_().m_61143_(RunicAnvilBlock.ACTIVE);
            if (!this.__cachedRecipeOutput.isEmpty()) {
                if (!isActive) {
                    this.m_58904_().setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(RunicAnvilBlock.ACTIVE, true), 3);
                }
            } else if (isActive) {
                this.f_58857_.setBlock(this.f_58858_, (BlockState) this.m_58900_().m_61124_(RunicAnvilBlock.ACTIVE, false), 0);
            }
        }
        this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        return output;
    }

    private int cacheEnchantRecipe(ItemStack rune, Player player, int playerTier) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(rune);
        if (enchantments.size() == 0) {
            this.__cachedEnchantRecipe = null;
            this.__cachedRecipeOutput = ItemStack.EMPTY;
            return 1;
        } else {
            this.__cachedEnchantRecipe = this.m_58904_().getRecipeManager().getRecipes().stream().filter(r -> r.getType() == RecipeInit.MANAWEAVING_RECIPE_TYPE.get() && ((ManaweavingRecipe) r).matches(enchantments)).map(r -> (ManaweavingRecipe) r).findFirst();
            int highestEnchantmentLevel = 0;
            for (Integer entry : enchantments.values()) {
                if (entry > highestEnchantmentLevel) {
                    highestEnchantmentLevel = entry;
                }
            }
            int freeformTier = 5;
            if (highestEnchantmentLevel <= 2) {
                freeformTier = 3;
            } else if (highestEnchantmentLevel <= 4) {
                freeformTier = 4;
            }
            if (rune.getItem() == ItemInit.RUNE_PROJECTION.get() && GeneralConfigValues.ProjectionCanEnchantAnything) {
                this.__cachedEnchantRecipe = Optional.empty();
                freeformTier = 1;
            }
            if ((!this.__cachedEnchantRecipe.isPresent() || ((ManaweavingRecipe) this.__cachedEnchantRecipe.get()).getTier() <= playerTier) && (this.__cachedEnchantRecipe.isPresent() || playerTier >= freeformTier)) {
                this.__cachedRecipeOutput = this.m_8020_(0).copy();
                boolean compatible = true;
                for (Entry<Enchantment, Integer> e : enchantments.entrySet()) {
                    if (player != null && e.getKey() instanceof Aura aura && !aura.canEnchant(this.__cachedRecipeOutput, player)) {
                        compatible = false;
                        break;
                    }
                    if (!((Enchantment) e.getKey()).canEnchant(this.__cachedRecipeOutput)) {
                        compatible = false;
                        break;
                    }
                }
                Map<Enchantment, Integer> existingEnchants = EnchantmentHelper.getEnchantments(this.__cachedRecipeOutput);
                for (Entry<Enchantment, Integer> ee : existingEnchants.entrySet()) {
                    for (Entry<Enchantment, Integer> e : enchantments.entrySet()) {
                        if (e.getKey() != ee.getKey() || (Integer) e.getValue() <= (Integer) ee.getValue()) {
                            if (player != null && e.getKey() instanceof Aura aura && !aura.canEnchant(this.__cachedRecipeOutput, player)) {
                                compatible = false;
                                break;
                            }
                            if (!((Enchantment) e.getKey()).canEnchant(this.__cachedRecipeOutput)) {
                                compatible = false;
                                break;
                            }
                            if (e.getKey() == ee.getKey() && (Integer) e.getValue() <= (Integer) ee.getValue()) {
                                compatible = false;
                                break;
                            }
                            if (player != null && e.getKey() instanceof Aura aura && !aura.isCompatibleWith((Enchantment) ee.getKey(), player)) {
                                compatible = false;
                                break;
                            }
                            if (!((Enchantment) e.getKey()).isCompatibleWith((Enchantment) ee.getKey())) {
                                compatible = false;
                                break;
                            }
                        }
                    }
                }
                if (compatible) {
                    existingEnchants.forEach((ex, i) -> {
                        if (!enchantments.containsKey(ex) || (Integer) enchantments.get(ex) < i) {
                            enchantments.put(ex, i);
                        }
                    });
                    EnchantmentHelper.setEnchantments(enchantments, this.__cachedRecipeOutput);
                    return 0;
                } else {
                    this.__cachedRecipeOutput = ItemStack.EMPTY;
                    return 1;
                }
            } else {
                this.__cachedEnchantRecipe = null;
                this.__cachedRecipeOutput = ItemStack.EMPTY;
                return 2;
            }
        }
    }

    private boolean cacheSpecialRecipes(int playerTier) {
        RunicAnvilShouldActivateEvent event = new RunicAnvilShouldActivateEvent(this.m_8020_(0), this.m_8020_(1));
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult() == Result.ALLOW ? true : this.cachePouch() || this.cachePattern() || this.cacheRepairs();
    }

    private boolean cacheRepairs() {
        ItemStack armorStack = this.m_8020_(0);
        if (armorStack.getItem() instanceof BrokenMageArmor && BrokenMageArmor.hasRestore(armorStack)) {
            ItemStack patchStack = this.m_8020_(1);
            return patchStack.getItem() == ItemInit.INFUSED_SILK.get();
        } else {
            return false;
        }
    }

    private boolean cachePouch() {
        ItemStack pouchStack = this.m_8020_(0);
        if (!(pouchStack.getItem() instanceof ItemPractitionersPouch)) {
            return false;
        } else {
            ItemStack patchStack = this.m_8020_(1);
            if (!(patchStack.getItem() instanceof ItemPractitionersPatch)) {
                return false;
            } else {
                PractitionersPouchPatches patch = ((ItemPractitionersPatch) patchStack.getItem()).getPatch();
                int patchLevel = ((ItemPractitionersPatch) patchStack.getItem()).getLevel();
                return ((ItemPractitionersPouch) pouchStack.getItem()).getPatchLevel(pouchStack, patch) >= patchLevel ? false : ((ItemPractitionersPouch) pouchStack.getItem()).countAppliedPatchesForLimit(pouchStack, patchStack) < 4;
            }
        }
    }

    private boolean cachePattern() {
        ItemStack runeStack = this.m_8020_(0);
        if (!(runeStack.getItem() instanceof ItemRune)) {
            return false;
        } else {
            ItemStack patchStack = this.m_8020_(1);
            return patchStack.getItem() == Items.PAPER;
        }
    }

    public ItemStack getOutputStack() {
        return this.__cachedRecipeOutput;
    }

    @Nullable
    public RuneforgingRecipe getCachedRecipe() {
        return this.__cachedRecipe;
    }

    private CraftingContainer createDummyCraftingInventory() {
        return ContainerTools.createTemporaryContainer(2, 1, NonNullList.of(null, this.m_8020_(0), this.m_8020_(1)));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("craft_progress", this.craftProgress);
    }

    @Override
    public void load(CompoundTag compound) {
        this.craftProgress = compound.getInt("craft_progress");
        super.load(compound);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag base = super.m_5995_();
        CompoundTag sub_1 = new CompoundTag();
        this.m_8020_(0).save(sub_1);
        base.put("invSync_1", sub_1);
        CompoundTag sub_2 = new CompoundTag();
        this.m_8020_(1).save(sub_2);
        base.put("invSync_2", sub_2);
        base.putInt("craft_progress", this.craftProgress);
        return base;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        CompoundTag sub = tag.getCompound("invSync_1");
        this.setItem(0, ItemStack.of(sub));
        CompoundTag sub2 = tag.getCompound("invSync_2");
        this.setItem(1, ItemStack.of(sub2));
        this.craftProgress = tag.getInt("craft_progress");
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag data = pkt.getTag();
        CompoundTag sub = data.getCompound("invSync_1");
        this.setItem(0, ItemStack.of(sub));
        CompoundTag sub2 = data.getCompound("invSync_2");
        this.setItem(1, ItemStack.of(sub2));
        this.craftProgress = data.getInt("craft_progress");
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public int getMaxStackSize() {
        return this.settingCraftOutput ? 64 : 1;
    }

    public boolean pushItemStack(ItemStack stack, Player player, int playerTier) {
        if (stack.getCount() != 1) {
            return false;
        } else if (this.m_8020_(0).isEmpty()) {
            this.setItem(0, stack);
            return true;
        } else if (this.m_8020_(1).isEmpty()) {
            this.setInventorySlotContentsNoCache(1, stack);
            this.cacheRecipe(player, playerTier);
            return true;
        } else {
            return false;
        }
    }

    private void setInventorySlotContentsNoCache(int index, ItemStack stack) {
        this.autoCacheRecipe = false;
        this.setItem(index, stack);
        this.autoCacheRecipe = true;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
        if (this.autoCacheRecipe) {
            this.cacheRecipe(null, 5);
        }
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return this.removeItemNoUpdate(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack output = super.removeItemNoUpdate(index);
        if (!output.isEmpty() && this.autoCacheRecipe) {
            this.cacheRecipe(null, 5);
        }
        return output;
    }

    public ItemStack popItemStack() {
        if (this.craftProgress > 0) {
            return ItemStack.EMPTY;
        } else {
            this.__cachedRecipeOutput = ItemStack.EMPTY;
            if (!this.m_8020_(1).isEmpty()) {
                return this.removeItemNoUpdate(1);
            } else {
                return !this.m_8020_(0).isEmpty() ? this.removeItemNoUpdate(0) : ItemStack.EMPTY;
            }
        }
    }

    public ItemStack[] getDisplayedItems() {
        return new ItemStack[] { this.m_8020_(0), this.m_8020_(1) };
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        if (itemStackIn.getCount() != 1) {
            return false;
        } else if (index == 1) {
            return !this.m_8020_(0).isEmpty() && this.m_8020_(1).isEmpty();
        } else {
            return index != 0 ? false : this.m_8020_(1).isEmpty() && this.m_8020_(0).isEmpty();
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (!ItemStack.matches(this.m_8020_(index), stack)) {
            return false;
        } else if (index != 0) {
            return index == 1 ? !this.m_8020_(1).isEmpty() : false;
        } else {
            return this.m_8020_(1).isEmpty() && !this.m_8020_(0).isEmpty();
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0, 1 };
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> this.m_58900_().m_61143_(RunicAnvilBlock.ACTIVE) ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.anvil_armature.in").thenLoop("animation.anvil_armature.idle")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.anvil_armature.out").thenLoop("animation.anvil_armature.hidden"))));
    }
}