package com.mna.blocks.tileentities.wizard_lab;

import com.mna.Registries;
import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.blocks.BlockInit;
import com.mna.blocks.sorcery.InscriptionTableBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.gui.containers.block.ContainerInscriptionTable;
import com.mna.items.ItemInit;
import com.mna.network.ClientMessageDispatcher;
import com.mna.network.ServerMessageDispatcher;
import com.mna.network.messages.to_server.InscriptionTableAttributeChangeMessage;
import com.mna.network.messages.to_server.InscriptionTableCraftingUpdateMessage;
import com.mna.network.messages.to_server.InscriptionTableRequestStartCraftingMessage;
import com.mna.network.messages.to_server.InscriptionTableSetComponentMessage;
import com.mna.network.messages.to_server.InscriptionTableSetModifierMessage;
import com.mna.network.messages.to_server.InscriptionTableSetShapeMessage;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.ModifiedSpellPart;
import com.mna.spells.crafting.SpellRecipe;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.IForgeRegistry;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class InscriptionTableTile extends TileEntityWithInventory implements MenuProvider, Consumer<FriendlyByteBuf>, GeoBlockEntity {

    private static final int SIZE_INVENTORY = 4;

    private static final int SLOT_INK = 0;

    private static final int SLOT_PAPER = 1;

    private static final int SLOT_FUEL = 2;

    private static final int SLOT_OUTPUT = 3;

    private static final int TICKS_PER_FUEL = 160;

    private static final int SCRIBE_TICKS_PER_COMPLEXITY = 20;

    private SpellRecipe recipe;

    private boolean isCrafting = false;

    private int craftTicksTotal = 0;

    private int craftTicksConsumed = 0;

    private int burnTicksRemaining = 0;

    private int inkDamageRemaining = 0;

    private int paperRemaining = 0;

    private int fuelRemaining = 0;

    private boolean creative;

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public InscriptionTableTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state, 4);
        this.recipe = new SpellRecipe();
    }

    public static void ServerTick(Level level, BlockPos pos, BlockState state, InscriptionTableTile tile) {
        if (tile.isCrafting) {
            if (!tile.creative) {
                if (tile.paperRemaining > 0 || tile.inkDamageRemaining > 0) {
                    tile.consumeMaterials();
                    return;
                }
                if (tile.burnTicksRemaining <= 5) {
                    if (!level.isClientSide()) {
                        tile.tryConsumeFuel();
                    }
                    return;
                }
            }
            tile.craftTicksConsumed++;
            tile.burnTicksRemaining--;
            if (!level.isClientSide()) {
                if (tile.craftTicksConsumed >= tile.craftTicksTotal) {
                    tile.completeSpellPart();
                } else if (tile.craftTicksConsumed > 0 && tile.craftTicksConsumed % 20 == 0) {
                    ServerMessageDispatcher.sendInscriptionTableCraftingUpdate(tile);
                }
            }
        }
    }

    public void setCreative(boolean creative) {
        this.creative = creative;
    }

    public boolean isCraftingPaused() {
        return this.isCrafting && (this.paperRemaining > 0 || this.inkDamageRemaining > 0);
    }

    private void completeSpellPart() {
        this.isCrafting = false;
        this.burnTicksRemaining = 0;
        this.craftTicksConsumed = 0;
        this.craftTicksTotal = 0;
        ItemStack outputStack = this.creative ? new ItemStack(ItemInit.SPELL.get()) : new ItemStack(ItemInit.ENCHANTED_VELLUM.get());
        this.recipe.setMysterious(false);
        this.recipe.writeToNBT(outputStack.getOrCreateTag());
        this.recipe.writeRecipeForRitual(this.f_58857_, outputStack.getOrCreateTag());
        this.m_6836_(3, outputStack);
        ServerMessageDispatcher.sendInscriptionTableCraftingUpdate(this);
        this.f_58857_.updateNeighbourForOutputSignal(this.f_58858_, this.m_58900_().m_60734_());
    }

    private void tryConsumeFuel() {
        ItemStack consumeStack = this.m_7407_(2, 1);
        if (!consumeStack.isEmpty()) {
            this.burnTicksRemaining = 160;
            ServerMessageDispatcher.sendInscriptionTableCraftingUpdate(this);
        }
    }

    public InscriptionTableTile.CraftCheckResult canBeginCrafting(IPlayerProgression progression) {
        if (this.isCrafting) {
            return InscriptionTableTile.CraftCheckResult.ALREADY_CRAFTING;
        } else if (!this.m_8020_(3).isEmpty()) {
            return InscriptionTableTile.CraftCheckResult.OUTPUT_FULL;
        } else if (this.getCurrentShape() == null) {
            return InscriptionTableTile.CraftCheckResult.NO_SHAPE;
        } else if (this.getCurrentComponent() == null) {
            return InscriptionTableTile.CraftCheckResult.NO_COMPONENT;
        } else if (this.getCurrentShape().getPart().isChanneled() && !this.getCurrentComponent().getPart().canBeChanneled()) {
            return InscriptionTableTile.CraftCheckResult.CANNOT_CHANNEL;
        } else if (this.creative) {
            return InscriptionTableTile.CraftCheckResult.READY;
        } else if (progression.getTier() < this.getCurrentRecipe().getTier(this.f_58857_)) {
            return InscriptionTableTile.CraftCheckResult.LOW_TIER;
        } else if (this.getCurrentRecipe().getComplexity() > (float) progression.getTierMaxComplexity()) {
            return InscriptionTableTile.CraftCheckResult.TOO_COMPLEX;
        } else if (!this.getCurrentRecipe().canFactionCraft(progression)) {
            return InscriptionTableTile.CraftCheckResult.WRONG_FACTION;
        } else {
            this.calculateMaterialRequirements();
            if (this.m_8020_(1).getCount() < this.paperRemaining) {
                return InscriptionTableTile.CraftCheckResult.MISSING_PAPER;
            } else if (this.m_8020_(0).getMaxDamage() - this.m_8020_(0).getDamageValue() < this.inkDamageRemaining) {
                return InscriptionTableTile.CraftCheckResult.MISSING_INK;
            } else {
                return this.m_8020_(2).getCount() == 0 ? InscriptionTableTile.CraftCheckResult.MISSING_ASH : InscriptionTableTile.CraftCheckResult.READY;
            }
        }
    }

    private void consumeMaterials() {
        boolean updated = false;
        ItemStack paperconsumed = this.m_7407_(1, this.paperRemaining);
        if (!paperconsumed.isEmpty()) {
            this.paperRemaining = this.paperRemaining - paperconsumed.getCount();
            updated = true;
        }
        ItemStack ink = this.m_8020_(0);
        if (!ink.isEmpty()) {
            int availableInk = this.m_8020_(0).getMaxDamage() - this.m_8020_(0).getDamageValue();
            if (availableInk > 0) {
                if (availableInk > this.inkDamageRemaining) {
                    this.m_8020_(0).setDamageValue(this.m_8020_(0).getDamageValue() + this.inkDamageRemaining);
                    this.inkDamageRemaining = 0;
                } else {
                    this.inkDamageRemaining -= availableInk;
                    this.m_8020_(0).setDamageValue(this.m_8020_(0).getMaxDamage());
                }
                updated = true;
            }
        }
        if (updated) {
            ServerMessageDispatcher.sendInscriptionTableCraftingUpdate(this);
        }
    }

    private void calculateMaterialRequirements() {
        this.paperRemaining = (int) Math.ceil((double) (this.getComplexity() / 5.0F));
        this.inkDamageRemaining = (int) Math.ceil((double) (this.getComplexity() / 2.5F));
        this.fuelRemaining = (int) Math.ceil((double) ((float) ((int) this.getComplexity() * 20) / 160.0F));
    }

    public InscriptionTableTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.INSCRIPTION_TABLE.get(), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new ContainerInscriptionTable(id, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("mna:container.inscription_table");
    }

    public void setCurrentShape(Shape shape) {
        if (!this.isCrafting) {
            this.recipe.setShape(shape);
            if (this.m_58898_()) {
                if (!this.f_58857_.isClientSide()) {
                    this.setChanged();
                } else {
                    ClientMessageDispatcher.sendInscriptionTableSetShape(this);
                }
            }
            this.calculateMaterialRequirements();
        }
    }

    public void setCurrentComponent(SpellEffect component) {
        if (!this.isCrafting) {
            this.recipe.clearComponents();
            if (component != null) {
                this.recipe.addComponent(component);
            }
            if (this.m_58898_()) {
                if (!this.f_58857_.isClientSide()) {
                    this.setChanged();
                } else {
                    ClientMessageDispatcher.sendInscriptionTableSetComponent(this);
                }
            }
            this.calculateMaterialRequirements();
        }
    }

    public void setCurrentModifier(Modifier modifier, int index) {
        if (!this.isCrafting) {
            this.recipe.setModifier(modifier, index);
            if (this.m_58898_()) {
                if (this.f_58857_.isClientSide()) {
                    ClientMessageDispatcher.sendInscriptionTableSetModifier(this, modifier != null ? modifier.getRegistryName() : new ResourceLocation(""), index);
                } else {
                    this.setChanged();
                }
            }
            this.calculateMaterialRequirements();
        }
    }

    public SpellRecipe getCurrentRecipe() {
        return this.recipe;
    }

    public ModifiedSpellPart<Shape> getCurrentShape() {
        return this.recipe.getShape();
    }

    public ModifiedSpellPart<SpellEffect> getCurrentComponent() {
        return this.recipe.getComponent(0);
    }

    public Modifier getCurrentModifier(int index) {
        return this.recipe.getModifier(index);
    }

    public void build() {
        if (this.recipe.isValid() && this.m_8020_(3).isEmpty()) {
            if (this.f_58857_.isClientSide()) {
                ClientMessageDispatcher.sendInscriptionTableRequestStartCrafting(this);
            } else {
                this.isCrafting = true;
                this.craftTicksTotal = this.creative ? 5 : (int) this.getComplexity() * 20;
                this.craftTicksConsumed = 0;
                if (this.m_58898_() && !this.f_58857_.isClientSide()) {
                    this.calculateMaterialRequirements();
                    ServerMessageDispatcher.sendInscriptionTableCraftingUpdate(this);
                    this.setChanged();
                    this.f_58857_.updateNeighbourForOutputSignal(this.f_58858_, this.m_58900_().m_60734_());
                }
            }
        }
    }

    public boolean isBuilding() {
        return this.isCrafting;
    }

    public void changeShapeAttributeValue(Attribute attribute, float newValue) {
        if (this.recipe.changeShapeAttributeValue(attribute, newValue) && this.m_58898_()) {
            if (this.f_58857_.isClientSide()) {
                ClientMessageDispatcher.sendInscriptionTableAttributeChange(this, attribute, newValue, InscriptionTableAttributeChangeMessage.ChangeType.SHAPE);
            } else {
                this.setChanged();
            }
        }
        this.calculateMaterialRequirements();
    }

    public void changeComponentAttributeValue(Attribute attribute, float newValue) {
        if (this.recipe.changeComponentAttributeValue(0, attribute, newValue) && this.m_58898_()) {
            if (this.f_58857_.isClientSide()) {
                ClientMessageDispatcher.sendInscriptionTableAttributeChange(this, attribute, newValue, InscriptionTableAttributeChangeMessage.ChangeType.COMPONENT);
            } else {
                this.setChanged();
            }
        }
        this.calculateMaterialRequirements();
    }

    public float getComplexity() {
        return this.recipe.getComplexity();
    }

    public float getManaCost(Player player) {
        this.recipe.calculateManaCost();
        SpellCaster.applyAdjusters(ItemStack.EMPTY, player, this.recipe, SpellCastStage.SPELLCRAFTING_MANA_COST_ESTIMATE);
        return this.recipe.isChanneled() ? this.recipe.getManaCost() * 20.0F : this.recipe.getManaCost();
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        CompoundTag recipeData = new CompoundTag();
        this.recipe.writeToNBT(recipeData);
        compound.put("recipe_data", recipeData);
        compound.putBoolean("isCrafting", this.isCrafting);
        compound.putInt("craftTicksTotal", this.craftTicksTotal);
        compound.putInt("craftTicksConsumed", this.craftTicksConsumed);
        compound.putInt("burnTicksRemaining", this.burnTicksRemaining);
        compound.putInt("inkDamageRemaining", this.inkDamageRemaining);
        compound.putInt("paperRemaining", this.paperRemaining);
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("recipe_data")) {
            this.recipe = SpellRecipe.fromNBT(compound.getCompound("recipe_data"));
        }
        this.isCrafting = compound.getBoolean("isCrafting");
        this.craftTicksTotal = compound.getInt("craftTicksTotal");
        this.craftTicksConsumed = compound.getInt("craftTicksConsumed");
        this.burnTicksRemaining = compound.getInt("burnTicksRemaining");
        this.inkDamageRemaining = compound.getInt("inkDamageRemaining");
        this.paperRemaining = compound.getInt("paperRemaining");
        super.load(compound);
    }

    public void accept(FriendlyByteBuf data) {
        data.writeBlockPos(this.m_58899_());
        CompoundTag nbt = new CompoundTag();
        this.recipe.writeToNBT(nbt);
        data.writeNbt(nbt);
        data.writeInt(this.craftTicksTotal);
        data.writeInt(this.craftTicksConsumed);
        data.writeInt(this.burnTicksRemaining);
        data.writeInt(this.inkDamageRemaining);
        data.writeInt(this.paperRemaining);
    }

    public InscriptionTableTile readFrom(FriendlyByteBuf data) {
        this.recipe = SpellRecipe.fromNBT(data.readNbt());
        this.craftTicksTotal = data.readInt();
        this.craftTicksConsumed = data.readInt();
        this.burnTicksRemaining = data.readInt();
        this.inkDamageRemaining = data.readInt();
        this.paperRemaining = data.readInt();
        return this;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    public int getCraftTicks() {
        return this.craftTicksTotal;
    }

    public int getCraftTicksConsumed() {
        return this.craftTicksConsumed;
    }

    public int getBurnTicksRemaining() {
        return this.burnTicksRemaining;
    }

    public int getPaperRemaining() {
        return this.paperRemaining;
    }

    public int getInkRemaining() {
        return this.inkDamageRemaining;
    }

    public int getFuelRemaining() {
        return this.fuelRemaining;
    }

    @Override
    public void setChanged() {
        super.m_6596_();
        int modelStateFlag = 0;
        if (!this.m_8020_(0).isEmpty()) {
            modelStateFlag |= 1;
        }
        if (!this.m_8020_(1).isEmpty()) {
            modelStateFlag |= 2;
        }
        if (!this.m_8020_(2).isEmpty()) {
            modelStateFlag |= 4;
        }
        if (this.f_58857_ != null) {
            this.f_58857_.setBlockAndUpdate(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(InscriptionTableBlock.CONTAINED_RESOURCES, modelStateFlag));
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side != Direction.DOWN && side != Direction.UP ? new int[] { 0, 1, 2 } : new int[] { 3 };
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return !this.isCrafting && direction != Direction.UP && direction != Direction.DOWN;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (this.isCrafting) {
            return false;
        } else {
            ItemStack existing = this.m_8020_(index);
            switch(index) {
                case 0:
                    return stack.getItem() == ItemInit.ARCANIST_INK.get() && existing.isEmpty();
                case 1:
                    return stack.getItem() == ItemInit.VELLUM.get() && (existing.isEmpty() || existing.getCount() < existing.getMaxStackSize());
                case 2:
                    return stack.getItem() == ItemInit.ARCANE_ASH.get() && (existing.isEmpty() || existing.getCount() < existing.getMaxStackSize());
                default:
                    return false;
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> this.isBuilding() && !this.isCraftingPaused() ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.inscription_table.quillwrite")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.inscription_table.quillidle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    private static InscriptionTableTile getAndVerify(Level world, BlockPos position) {
        if (world.isLoaded(position) && world.getBlockState(position).m_60734_() == BlockInit.INSCRIPTION_TABLE.get()) {
            BlockEntity te = world.getBlockEntity(position);
            if (te != null && te instanceof InscriptionTableTile) {
                return (InscriptionTableTile) te;
            }
        }
        return null;
    }

    public static final void handleShapeSet(ServerPlayer sendingPlayer, InscriptionTableSetShapeMessage message) {
        InscriptionTableTile te = getAndVerify(sendingPlayer.m_9236_(), message.getPosition());
        if (te != null) {
            Shape spellShape = (Shape) ((IForgeRegistry) Registries.Shape.get()).getValue(message.getShape());
            te.setCurrentShape(spellShape);
        }
    }

    public static final void handleComponentSet(ServerPlayer sendingPlayer, InscriptionTableSetComponentMessage message) {
        InscriptionTableTile te = getAndVerify(sendingPlayer.m_9236_(), message.getPosition());
        if (te != null) {
            SpellEffect spellComponent = (SpellEffect) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(message.getComponent());
            te.setCurrentComponent(spellComponent);
        }
    }

    public static final void handleModifierSet(ServerPlayer sendingPlayer, InscriptionTableSetModifierMessage message) {
        if (message.getIndex() >= 0 && message.getIndex() < 3) {
            InscriptionTableTile te = getAndVerify(sendingPlayer.m_9236_(), message.getPosition());
            if (te != null) {
                Modifier spellModifier = (Modifier) ((IForgeRegistry) Registries.Modifier.get()).getValue(message.getModifier());
                te.setCurrentModifier(spellModifier, message.getIndex());
            }
        }
    }

    public static final void handleAttributeValueChange(ServerPlayer sendingPlayer, InscriptionTableAttributeChangeMessage message) {
        InscriptionTableTile te = getAndVerify(sendingPlayer.m_9236_(), message.getPosition());
        if (te != null) {
            switch(message.getChangeType()) {
                case COMPONENT:
                    te.changeComponentAttributeValue(message.getAttribute(), message.getValue());
                    break;
                case SHAPE:
                    te.changeShapeAttributeValue(message.getAttribute(), message.getValue());
            }
        }
    }

    public static final void handleRequestStartCrafting(ServerPlayer sendingPlayer, InscriptionTableRequestStartCraftingMessage message) {
        InscriptionTableTile te = getAndVerify(sendingPlayer.m_9236_(), message.getPosition());
        if (te != null) {
            if (!te.isBuilding()) {
                te.build();
            }
        }
    }

    public static final void handleCraftingUpdate(Level world, InscriptionTableCraftingUpdateMessage message) {
        InscriptionTableTile te = getAndVerify(world, message.getPosition());
        if (te != null) {
            te.isCrafting = message.getBurnTimeRemaining() > 0;
            te.craftTicksTotal = message.getTotalCraftTicks();
            te.craftTicksConsumed = message.getCraftTicksPassed();
            te.burnTicksRemaining = message.getBurnTimeRemaining();
            te.inkDamageRemaining = message.getInkRemaining();
            te.paperRemaining = message.getPaperRemaining();
        }
    }

    public static enum CraftCheckResult {

        READY(""),
        NO_SHAPE("gui.mna.inscription.shape_missing"),
        NO_COMPONENT("gui.mna.inscription.component_missing"),
        MISSING_INK("gui.mna.inscription.ink_missing"),
        MISSING_PAPER("gui.mna.inscription.paper_missing"),
        MISSING_ASH("gui.mna.inscription.ash_missing"),
        OUTPUT_FULL("gui.mna.inscription.output_full"),
        LOW_TIER("gui.mna.inscription.low_tier"),
        TOO_COMPLEX("gui.mna.inscription.too_complex"),
        WRONG_FACTION("gui.mna.inscription.wrong_faction"),
        ALREADY_CRAFTING(""),
        CANNOT_CHANNEL("gui.mna.inscription.component_not_channeled");

        private String lang_key;

        private CraftCheckResult(String lang_key) {
            this.lang_key = lang_key;
        }

        public String getTranslationKey() {
            return this.lang_key;
        }
    }
}