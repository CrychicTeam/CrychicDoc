package top.theillusivec4.curios.common.inventory.container;

import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ICuriosMenu;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.CuriosRegistry;
import top.theillusivec4.curios.common.inventory.CosmeticCurioSlot;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketScroll;
import top.theillusivec4.curios.common.network.server.SPacketScroll;

public class CuriosContainer extends InventoryMenu implements ICuriosMenu {

    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[] { InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET };

    private static final EquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };

    public final LazyOptional<ICuriosItemHandler> curiosHandler;

    public final Player player;

    private final boolean isLocalWorld;

    private final CraftingContainer craftMatrix = new TransientCraftingContainer(this, 2, 2);

    private final ResultContainer craftResult = new ResultContainer();

    private int lastScrollIndex;

    private boolean cosmeticColumn;

    private boolean skip = false;

    public CuriosContainer(int windowId, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(windowId, playerInventory);
    }

    public CuriosContainer(int windowId, Inventory playerInventory) {
        this(windowId, playerInventory, false);
    }

    public CuriosContainer(int windowId, Inventory playerInventory, boolean skip) {
        super(playerInventory, playerInventory.player.m_9236_().isClientSide, playerInventory.player);
        this.f_38843_ = CuriosRegistry.CURIO_MENU.get();
        this.f_38840_ = windowId;
        this.f_150394_.clear();
        this.f_38841_.clear();
        this.f_38839_.clear();
        this.player = playerInventory.player;
        this.isLocalWorld = this.player.m_9236_().isClientSide;
        this.curiosHandler = CuriosApi.getCuriosInventory(this.player);
        if (skip) {
            this.skip = true;
        } else {
            this.m_38897_(new ResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 154, 28));
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    this.m_38897_(new Slot(this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
                }
            }
            for (int k = 0; k < 4; k++) {
                final EquipmentSlot equipmentslottype = VALID_EQUIPMENT_SLOTS[k];
                this.m_38897_(new Slot(playerInventory, 36 + (3 - k), 8, 8 + k * 18) {

                    @Override
                    public void set(@Nonnull ItemStack stack) {
                        ItemStack itemstack = this.m_7993_();
                        super.set(stack);
                        CuriosContainer.this.player.m_238392_(equipmentslottype, itemstack, stack);
                    }

                    @Override
                    public int getMaxStackSize() {
                        return 1;
                    }

                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return stack.canEquip(equipmentslottype, CuriosContainer.this.player);
                    }

                    @Override
                    public boolean mayPickup(@Nonnull Player playerIn) {
                        ItemStack itemstack = this.m_7993_();
                        return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.mayPickup(playerIn);
                    }

                    @OnlyIn(Dist.CLIENT)
                    @Override
                    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                        return Pair.of(InventoryMenu.BLOCK_ATLAS, CuriosContainer.ARMOR_SLOT_TEXTURES[equipmentslottype.getIndex()]);
                    }
                });
            }
            for (int l = 0; l < 3; l++) {
                for (int j1 = 0; j1 < 9; j1++) {
                    this.m_38897_(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
                }
            }
            for (int i1 = 0; i1 < 9; i1++) {
                this.m_38897_(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
            }
            this.m_38897_(new Slot(playerInventory, 40, 77, 62) {

                @OnlyIn(Dist.CLIENT)
                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
                }
            });
            this.curiosHandler.ifPresent(curios -> {
                Map<String, ICurioStacksHandler> curioMap = curios.getCurios();
                int slots = 0;
                int yOffset = 12;
                for (String identifier : curioMap.keySet()) {
                    ICurioStacksHandler stacksHandler = (ICurioStacksHandler) curioMap.get(identifier);
                    IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                    if (stacksHandler.isVisible()) {
                        for (int i = 0; i < stackHandler.getSlots() && slots < 8; i++) {
                            this.m_38897_(new CurioSlot(this.player, stackHandler, i, identifier, -18, yOffset, stacksHandler.getRenders(), stacksHandler.canToggleRendering()));
                            yOffset += 18;
                            slots++;
                        }
                    }
                }
                int var12 = 12;
                slots = 0;
                for (String identifierx : curioMap.keySet()) {
                    ICurioStacksHandler stacksHandler = (ICurioStacksHandler) curioMap.get(identifierx);
                    IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                    if (stacksHandler.isVisible()) {
                        for (int i = 0; i < stackHandler.getSlots() && slots < 8; i++) {
                            if (stacksHandler.hasCosmetic()) {
                                IDynamicStackHandler cosmeticHandler = stacksHandler.getCosmeticStacks();
                                this.cosmeticColumn = true;
                                this.m_38897_(new CosmeticCurioSlot(this.player, cosmeticHandler, i, identifierx, -37, var12));
                            }
                            var12 += 18;
                            slots++;
                        }
                    }
                }
            });
            this.scrollToIndex(0);
        }
    }

    public boolean hasCosmeticColumn() {
        return this.cosmeticColumn;
    }

    @Override
    public void resetSlots() {
        this.scrollToIndex(this.lastScrollIndex);
    }

    public void scrollToIndex(int indexIn) {
        this.curiosHandler.ifPresent(curios -> {
            Map<String, ICurioStacksHandler> curioMap = curios.getCurios();
            int slots = 0;
            int yOffset = 12;
            int index = 0;
            int startingIndex = indexIn;
            this.f_38839_.subList(46, this.f_38839_.size()).clear();
            this.f_38841_.subList(46, this.f_38841_.size()).clear();
            this.f_150394_.subList(46, this.f_150394_.size()).clear();
            for (String identifier : curioMap.keySet()) {
                ICurioStacksHandler stacksHandler = (ICurioStacksHandler) curioMap.get(identifier);
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                if (stacksHandler.isVisible()) {
                    for (int i = 0; i < stackHandler.getSlots() && slots < 8; i++) {
                        if (index >= startingIndex) {
                            slots++;
                        }
                        index++;
                    }
                }
            }
            startingIndex = Math.min(startingIndex, Math.max(0, index - 8));
            index = 0;
            slots = 0;
            for (String identifierx : curioMap.keySet()) {
                ICurioStacksHandler stacksHandler = (ICurioStacksHandler) curioMap.get(identifierx);
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                if (stacksHandler.isVisible()) {
                    for (int i = 0; i < stackHandler.getSlots() && slots < 8; i++) {
                        if (index >= startingIndex) {
                            this.m_38897_(new CurioSlot(this.player, stackHandler, i, identifierx, -18, yOffset, stacksHandler.getRenders(), stacksHandler.canToggleRendering()));
                            yOffset += 18;
                            slots++;
                        }
                        index++;
                    }
                }
            }
            index = 0;
            slots = 0;
            int var16 = 12;
            for (String identifierxx : curioMap.keySet()) {
                ICurioStacksHandler stacksHandler = (ICurioStacksHandler) curioMap.get(identifierxx);
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                if (stacksHandler.isVisible()) {
                    for (int i = 0; i < stackHandler.getSlots() && slots < 8; i++) {
                        if (index >= startingIndex) {
                            if (stacksHandler.hasCosmetic()) {
                                IDynamicStackHandler cosmeticHandler = stacksHandler.getCosmeticStacks();
                                this.cosmeticColumn = true;
                                this.m_38897_(new CosmeticCurioSlot(this.player, cosmeticHandler, i, identifierxx, -37, var16));
                            }
                            var16 += 18;
                            slots++;
                        }
                        index++;
                    }
                }
            }
            if (!this.isLocalWorld) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) this.player), new SPacketScroll(this.f_38840_, indexIn));
            }
            this.lastScrollIndex = indexIn;
        });
    }

    public void scrollTo(float pos) {
        this.curiosHandler.ifPresent(curios -> {
            int k = curios.getVisibleSlots() - 8;
            int j = (int) ((double) (pos * (float) k) + 0.5);
            if (j < 0) {
                j = 0;
            }
            if (j != this.lastScrollIndex) {
                if (this.isLocalWorld) {
                    NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketScroll(this.f_38840_, j));
                }
            }
        });
    }

    @Override
    public void slotsChanged(@Nonnull Container inventoryIn) {
        if (!this.player.m_9236_().isClientSide) {
            ServerPlayer playerMP = (ServerPlayer) this.player;
            ItemStack stack = ItemStack.EMPTY;
            MinecraftServer server = this.player.m_9236_().getServer();
            if (server == null) {
                return;
            }
            Optional<CraftingRecipe> recipe = server.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, this.craftMatrix, this.player.m_9236_());
            if (recipe.isPresent()) {
                CraftingRecipe craftingRecipe = (CraftingRecipe) recipe.get();
                if (this.craftResult.m_40135_(this.player.m_9236_(), playerMP, craftingRecipe)) {
                    stack = craftingRecipe.m_5874_(this.craftMatrix, this.player.m_9236_().registryAccess());
                }
            }
            this.craftResult.setItem(0, stack);
            this.m_150404_(0, stack);
            playerMP.connection.send(new ClientboundContainerSetSlotPacket(this.f_38840_, this.m_182425_(), 0, stack));
        }
    }

    @Override
    public void removed(@Nonnull Player playerIn) {
        super.removed(playerIn);
        if (!this.skip) {
            this.craftResult.clearContent();
            if (!playerIn.m_9236_().isClientSide) {
                this.m_150411_(playerIn, this.craftMatrix);
            }
        }
    }

    public boolean canScroll() {
        return (Integer) this.curiosHandler.map(curios -> curios.getVisibleSlots() > 8 ? 1 : 0).orElse(0) == 1;
    }

    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        return true;
    }

    @Override
    public void setItem(int pSlotId, int pStateId, @Nonnull ItemStack pStack) {
        if (this.skip) {
            super.m_182406_(pSlotId, pStateId, pStack);
        } else {
            if (this.f_38839_.size() > pSlotId) {
                super.m_182406_(pSlotId, pStateId, pStack);
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            EquipmentSlot entityequipmentslot = Mob.m_147233_(itemstack);
            if (index == 0) {
                if (!this.m_38903_(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index < 5) {
                if (!this.m_38903_(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 9) {
                if (!this.m_38903_(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (entityequipmentslot.getType() == EquipmentSlot.Type.ARMOR && !((Slot) this.f_38839_.get(8 - entityequipmentslot.getIndex())).hasItem()) {
                int i = 8 - entityequipmentslot.getIndex();
                if (!this.m_38903_(itemstack1, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 46 && !CuriosApi.getItemStackSlots(itemstack, playerIn.m_9236_()).isEmpty()) {
                if (!this.m_38903_(itemstack1, 46, this.f_38839_.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (entityequipmentslot == EquipmentSlot.OFFHAND && !((Slot) this.f_38839_.get(45)).hasItem()) {
                if (!this.m_38903_(itemstack1, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 36) {
                if (!this.m_38903_(itemstack1, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < 45) {
                if (!this.m_38903_(itemstack1, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 9, 45, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
            if (index == 0) {
                playerIn.drop(itemstack1, false);
            }
        }
        return itemstack;
    }

    @Nonnull
    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    @Override
    public void fillCraftSlotsStackedContents(@Nonnull StackedContents itemHelperIn) {
        this.craftMatrix.m_5809_(itemHelperIn);
    }

    @Override
    public void clearCraftingContent() {
        this.craftMatrix.m_6211_();
        this.craftResult.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipeIn) {
        return recipeIn.matches(this.craftMatrix, this.player.m_9236_());
    }

    @Override
    public int getGridWidth() {
        return this.craftMatrix.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.craftMatrix.getHeight();
    }
}