package se.mickelus.tetra.blocks.workbench;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.workbench.action.ConfigAction;
import se.mickelus.tetra.blocks.workbench.action.RepairAction;
import se.mickelus.tetra.blocks.workbench.action.WorkbenchAction;
import se.mickelus.tetra.blocks.workbench.action.WorkbenchActionPacket;
import se.mickelus.tetra.craftingeffect.CraftingEffectRegistry;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;
import se.mickelus.tetra.properties.IToolProvider;
import se.mickelus.tetra.properties.PropertyHelper;

@ParametersAreNonnullByDefault
public class WorkbenchTile extends BlockEntity implements MenuProvider {

    public static final String identifier = "workbench";

    public static final int inventorySlots = 4;

    public static final int maxMaterialSlots = 3;

    private static final String inventoryKey = "inv";

    private static final String currentSlotKey = "current_slot";

    private static final String schematicKey = "schematic";

    private static final WorkbenchAction[] defaultActions = new WorkbenchAction[] { new RepairAction() };

    public static RegistryObject<BlockEntityType<WorkbenchTile>> type;

    private static WorkbenchAction[] actions = new WorkbenchAction[0];

    private final LazyOptional<ItemStackHandler> handler;

    private final ItemStack previousTarget = ItemStack.EMPTY;

    private final Map<String, Runnable> changeListeners = new HashMap();

    private UpgradeSchematic currentSchematic;

    private String currentSlot;

    private ActionInteraction interaction;

    public WorkbenchTile(BlockPos blockPos0, BlockState blockState1) {
        super(type.get(), blockPos0, blockState1);
        this.handler = LazyOptional.of(this::createHandler);
    }

    public static void init(PacketHandler packetHandler) {
        packetHandler.registerPacket(WorkbenchPacketUpdate.class, WorkbenchPacketUpdate::new);
        packetHandler.registerPacket(WorkbenchPacketCraft.class, WorkbenchPacketCraft::new);
        packetHandler.registerPacket(WorkbenchActionPacket.class, WorkbenchActionPacket::new);
        packetHandler.registerPacket(WorkbenchPacketTweak.class, WorkbenchPacketTweak::new);
        DataManager.instance.actionData.onReload(() -> {
            WorkbenchAction[] configActions = (WorkbenchAction[]) DataManager.instance.actionData.getData().values().stream().flatMap(Arrays::stream).toArray(ConfigAction[]::new);
            actions = (WorkbenchAction[]) ArrayUtils.addAll(defaultActions, configActions);
        });
    }

    public static ItemStack consumeCraftingToolEffects(ItemStack upgradedStack, String slot, boolean isReplacing, ToolAction tool, int level, Player player, Level world, BlockPos pos, BlockState blockState, boolean consumeResources) {
        ItemStack providingStack = PropertyHelper.getPlayerProvidingItemStack(tool, level, player);
        if (!providingStack.isEmpty()) {
            if (providingStack.getItem() instanceof IToolProvider) {
                upgradedStack = ((IToolProvider) providingStack.getItem()).onCraftConsume(providingStack, upgradedStack, player, tool, level, consumeResources);
            }
        } else {
            ItemStack toolbeltResult = PropertyHelper.consumeCraftToolToolbelt(player, upgradedStack, tool, level, consumeResources);
            if (toolbeltResult != null) {
                upgradedStack = toolbeltResult;
            } else {
                upgradedStack = (ItemStack) CastOptional.cast(blockState.m_60734_(), AbstractWorkbenchBlock.class).map(block -> block.onCraftConsumeTool(world, pos, blockState, upgradedStack, slot, isReplacing, player, tool, level, consumeResources)).orElse(upgradedStack);
            }
        }
        return upgradedStack;
    }

    public static ItemStack applyCraftingBonusEffects(ItemStack upgradedStack, String slot, boolean isReplacing, Player player, ItemStack[] preMaterials, ItemStack[] postMaterials, Map<ToolAction, Integer> tools, UpgradeSchematic schematic, Level world, BlockPos pos, BlockState blockState, boolean consumeResources) {
        ItemStack result = upgradedStack.copy();
        ResourceLocation[] unlockedEffects = (ResourceLocation[]) CastOptional.cast(blockState.m_60734_(), AbstractWorkbenchBlock.class).map(block -> block.getCraftingEffects(world, pos, blockState)).orElse(new ResourceLocation[0]);
        Arrays.stream(CraftingEffectRegistry.getEffects(unlockedEffects, upgradedStack, slot, isReplacing, player, preMaterials, tools, schematic, world, pos, blockState)).forEach(craftingEffect -> craftingEffect.applyOutcomes(unlockedEffects, result, slot, isReplacing, player, preMaterials, postMaterials, tools, world, schematic, pos, blockState, consumeResources));
        return result;
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.handler.cast() : super.getCapability(cap, side);
    }

    @NotNull
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(4) {

            @Override
            protected void onContentsChanged(int slot) {
                ItemStack itemStack = this.getStackInSlot(slot);
                if (slot == 0 && (itemStack.isEmpty() || !ItemStack.matches(WorkbenchTile.this.getTargetItemStack(), itemStack))) {
                    WorkbenchTile.this.currentSchematic = null;
                    WorkbenchTile.this.currentSlot = null;
                    WorkbenchTile.this.emptyMaterialSlots();
                }
                if (slot == 0) {
                    WorkbenchTile.this.interaction = ActionInteraction.create(WorkbenchTile.this);
                }
                WorkbenchTile.this.setChanged();
                WorkbenchTile.this.f_58857_.sendBlockUpdated(WorkbenchTile.this.f_58858_, WorkbenchTile.this.m_58900_(), WorkbenchTile.this.m_58900_(), 3);
            }

            @Override
            public int getSlots() {
                return WorkbenchTile.this.currentSchematic != null ? WorkbenchTile.this.currentSchematic.getNumMaterialSlots() + 1 : 1;
            }
        };
    }

    public WorkbenchAction[] getAvailableActions(Player player) {
        ItemStack itemStack = this.getTargetItemStack();
        return (WorkbenchAction[]) Arrays.stream(actions).filter(action -> action.canPerformOn(player, this, itemStack)).toArray(WorkbenchAction[]::new);
    }

    public void performAction(Player player, String actionKey) {
        if (this.f_58857_.isClientSide) {
            TetraMod.packetHandler.sendToServer(new WorkbenchActionPacket(this.f_58858_, actionKey));
        } else {
            BlockState blockState = this.f_58857_.getBlockState(this.m_58899_());
            ItemStack targetStack = this.getTargetItemStack();
            Arrays.stream(actions).filter(action -> action.getKey().equals(actionKey)).findFirst().filter(action -> action.canPerformOn(player, this, targetStack)).filter(action -> this.checkActionTools(player, action, targetStack)).ifPresent(action -> {
                action.getRequiredTools(targetStack).forEach((requiredTool, requiredLevel) -> {
                    ItemStack providingStack = PropertyHelper.getPlayerProvidingItemStack(requiredTool, requiredLevel, player);
                    if (!providingStack.isEmpty()) {
                        if (providingStack.getItem() instanceof IToolProvider) {
                            ((IToolProvider) providingStack.getItem()).onActionConsume(providingStack, targetStack, player, requiredTool, requiredLevel, true);
                        }
                    } else {
                        ItemStack toolbeltResult = PropertyHelper.consumeActionToolToolbelt(player, targetStack, requiredTool, requiredLevel, true);
                        if (toolbeltResult == null) {
                            CastOptional.cast(this.m_58900_().m_60734_(), AbstractWorkbenchBlock.class).ifPresent(block -> block.onActionConsumeTool(this.f_58857_, this.m_58899_(), blockState, targetStack, player, requiredTool, requiredLevel, true));
                        }
                    }
                });
                action.perform(player, targetStack, this);
                this.setChanged();
                this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
            });
        }
    }

    public void performAction(String actionKey) {
        BlockState blockState = this.f_58857_.getBlockState(this.m_58899_());
        ItemStack targetStack = this.getTargetItemStack();
        Arrays.stream(actions).filter(action -> action.getKey().equals(actionKey)).findFirst().filter(action -> action.canPerformOn(null, this, targetStack)).filter(action -> this.checkActionTools(action, targetStack)).ifPresent(action -> {
            action.getRequiredTools(targetStack).forEach((requiredTool, requiredLevel) -> CastOptional.cast(this.m_58900_().m_60734_(), AbstractWorkbenchBlock.class).ifPresent(block -> block.onActionConsumeTool(this.f_58857_, this.m_58899_(), blockState, targetStack, null, requiredTool, requiredLevel, true)));
            action.perform(null, targetStack, this);
            this.setChanged();
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
        });
    }

    private boolean checkActionTools(Player player, WorkbenchAction action, ItemStack itemStack) {
        return action.getRequiredTools(itemStack).entrySet().stream().allMatch(requirement -> PropertyHelper.getCombinedToolLevel(player, this.m_58904_(), this.m_58899_(), this.f_58857_.getBlockState(this.m_58899_()), (ToolAction) requirement.getKey()) >= (Integer) requirement.getValue());
    }

    private boolean checkActionTools(WorkbenchAction action, ItemStack itemStack) {
        return action.getRequiredTools(itemStack).entrySet().stream().allMatch(requirement -> PropertyHelper.getBlockToolLevel(this.m_58904_(), this.m_58899_(), this.f_58857_.getBlockState(this.m_58899_()), (ToolAction) requirement.getKey()) >= (Integer) requirement.getValue());
    }

    public BlockInteraction[] getInteractions() {
        return this.interaction != null ? new BlockInteraction[] { this.interaction } : new BlockInteraction[0];
    }

    public UpgradeSchematic getCurrentSchematic() {
        return this.currentSchematic;
    }

    public void setCurrentSchematic(UpgradeSchematic schematic, String currentSlot) {
        this.currentSchematic = schematic;
        this.currentSlot = currentSlot;
        this.changeListeners.values().forEach(Runnable::run);
        this.sync();
    }

    public void clearSchematic() {
        this.setCurrentSchematic(null, null);
    }

    public void update(UpgradeSchematic currentSchematic, String currentSlot, Player player) {
        if (currentSchematic == null && player != null) {
            this.emptyMaterialSlots(player);
        }
        this.currentSchematic = currentSchematic;
        this.currentSlot = currentSlot;
        this.sync();
    }

    public String getCurrentSlot() {
        return this.currentSlot;
    }

    private void sync() {
        if (this.f_58857_.isClientSide) {
            TetraMod.packetHandler.sendToServer(new WorkbenchPacketUpdate(this.f_58858_, this.currentSchematic, this.currentSlot));
        } else {
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
            this.setChanged();
        }
    }

    public ItemStack getTargetItemStack() {
        return (ItemStack) this.handler.map(handler -> {
            ItemStack stack = handler.getStackInSlot(0);
            ItemStack placeholder = ItemUpgradeRegistry.instance.getReplacement(stack);
            return !placeholder.isEmpty() ? placeholder : stack;
        }).orElse(ItemStack.EMPTY);
    }

    public boolean isTargetPlaceholder() {
        return (Boolean) this.handler.map(handler -> handler.getStackInSlot(0)).map(stack -> ItemUpgradeRegistry.instance.getReplacement(stack)).map(placeholder -> !placeholder.isEmpty()).orElse(false);
    }

    public ItemStack[] getMaterials() {
        return (ItemStack[]) this.handler.map(handler -> {
            ItemStack[] result = new ItemStack[3];
            for (int i = 0; i < result.length; i++) {
                result[i] = handler.getStackInSlot(i + 1).copy();
            }
            return result;
        }).orElse(new ItemStack[0]);
    }

    public void initiateCrafting(Player player) {
        if (this.f_58857_.isClientSide) {
            TetraMod.packetHandler.sendToServer(new WorkbenchPacketCraft(this.f_58858_));
        }
        this.craft(player);
        this.sync();
    }

    public void craft(Player player) {
        ItemStack targetStack = this.getTargetItemStack();
        ItemStack upgradedStack = targetStack;
        IModularItem item = (IModularItem) CastOptional.cast(targetStack.getItem(), IModularItem.class).orElse(null);
        BlockState blockState = this.m_58900_();
        Map<ToolAction, Integer> availableTools = PropertyHelper.getCombinedToolLevels(player, this.m_58904_(), this.m_58899_(), blockState);
        ItemStack[] materials = this.getMaterials();
        ItemStack[] materialsAltered = (ItemStack[]) Arrays.stream(this.getMaterials()).map(ItemStack::m_41777_).toArray(ItemStack[]::new);
        if (item != null && this.currentSchematic != null && this.currentSchematic.canApplyUpgrade(player, targetStack, materialsAltered, this.currentSlot, availableTools)) {
            float severity = this.currentSchematic.getSeverity(targetStack, materialsAltered, this.currentSlot);
            boolean willReplace = this.currentSchematic.willReplace(targetStack, materialsAltered, this.currentSlot);
            if (willReplace) {
                TetraEnchantmentHelper.removeEnchantments(targetStack, this.currentSlot);
            }
            double durabilityFactor = targetStack.isDamageableItem() ? (double) targetStack.getDamageValue() * 1.0 / (double) targetStack.getMaxDamage() : 0.0;
            double honingFactor = Mth.clamp((double) item.getHoningProgress(targetStack) * 1.0 / (double) item.getHoningLimit(targetStack), 0.0, 1.0);
            Map<ToolAction, Integer> tools = this.currentSchematic.getRequiredToolLevels(targetStack, materials);
            upgradedStack = this.currentSchematic.applyUpgrade(targetStack, materialsAltered, true, this.currentSlot, player);
            upgradedStack = applyCraftingBonusEffects(upgradedStack, this.currentSlot, willReplace, player, materials, materialsAltered, tools, this.currentSchematic, this.f_58857_, this.f_58858_, blockState, true);
            for (Entry<ToolAction, Integer> entry : tools.entrySet()) {
                upgradedStack = consumeCraftingToolEffects(upgradedStack, this.currentSlot, willReplace, (ToolAction) entry.getKey(), (Integer) entry.getValue(), player, this.f_58857_, this.f_58858_, blockState, true);
            }
            item.assemble(upgradedStack, this.f_58857_, severity);
            if (this.currentSchematic.isHoning()) {
                IModularItem.removeHoneable(upgradedStack);
            } else if (ConfigHandler.moduleProgression.get() && !IModularItem.isHoneable(upgradedStack)) {
                item.setHoningProgress(upgradedStack, (int) Math.ceil(honingFactor * (double) item.getHoningLimit(upgradedStack)));
            }
            if (upgradedStack.isDamageableItem() && !(this.currentSchematic instanceof RepairSchematic)) {
                if (durabilityFactor > 0.0 && willReplace && this.currentSlot.equals(item.getRepairSlot(upgradedStack))) {
                    item.repair(upgradedStack);
                } else {
                    upgradedStack.setDamageValue((int) Math.ceil(durabilityFactor * (double) upgradedStack.getMaxDamage()));
                }
            }
            int xpCost = this.currentSchematic.getExperienceCost(targetStack, materials, this.currentSlot);
            if (!player.isCreative() && xpCost > 0) {
                player.giveExperienceLevels(-xpCost);
            }
        }
        ItemStack tempStack = upgradedStack;
        this.handler.ifPresent(handler -> {
            for (int i = 0; i < materialsAltered.length; i++) {
                handler.setStackInSlot(i + 1, materialsAltered[i]);
            }
            this.emptyMaterialSlots(player);
            handler.setStackInSlot(0, tempStack);
        });
        this.clearSchematic();
    }

    public ResourceLocation[] getUnlockedSchematics() {
        return (ResourceLocation[]) CastOptional.cast(this.m_58900_().m_60734_(), AbstractWorkbenchBlock.class).map(block -> block.getSchematics(this.f_58857_, this.f_58858_, this.m_58900_())).orElse(new ResourceLocation[0]);
    }

    public void applyTweaks(Player player, String slot, Map<String, Integer> tweaks) {
        if (this.f_58857_.isClientSide) {
            TetraMod.packetHandler.sendToServer(new WorkbenchPacketTweak(this.f_58858_, slot, tweaks));
        }
        this.tweak(player, slot, tweaks);
        this.sync();
    }

    public void tweak(Player player, String slot, Map<String, Integer> tweaks) {
        this.handler.ifPresent(handler -> {
            ItemStack tweakedStack = this.getTargetItemStack().copy();
            CastOptional.cast(tweakedStack.getItem(), IModularItem.class).ifPresent(item -> item.tweak(tweakedStack, slot, tweaks));
            handler.setStackInSlot(0, tweakedStack);
        });
    }

    public void addChangeListener(String key, Runnable runnable) {
        this.changeListeners.put(key, runnable);
    }

    public void removeChangeListener(String key) {
        this.changeListeners.remove(key);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ != null && this.f_58857_.isClientSide) {
            this.changeListeners.values().forEach(Runnable::run);
        }
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
        this.handler.ifPresent(handler -> handler.deserializeNBT(compound.getCompound("inv")));
        String schematicKey = compound.getString("schematic");
        this.currentSchematic = SchematicRegistry.getSchematic(schematicKey);
        if (compound.contains("current_slot")) {
            this.currentSlot = compound.getString("current_slot");
        }
        this.interaction = ActionInteraction.create(this);
        if (this.f_58857_ != null && this.f_58857_.isClientSide) {
            this.changeListeners.values().forEach(Runnable::run);
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        this.handler.ifPresent(handler -> compound.put("inv", handler.serializeNBT()));
        if (this.currentSchematic != null) {
            compound.putString("schematic", this.currentSchematic.getKey());
        }
        if (this.currentSlot != null) {
            compound.putString("current_slot", this.currentSlot);
        }
    }

    private void emptyMaterialSlots(Player player) {
        this.handler.ifPresent(handler -> {
            for (int i = 1; i < handler.getSlots(); i++) {
                this.transferStackToPlayer(player, i);
            }
            this.setChanged();
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
        });
    }

    private void emptyMaterialSlots() {
        this.handler.ifPresent(handler -> {
            if (!this.f_58857_.isClientSide) {
                for (int i = 1; i < 4; i++) {
                    ItemStack materialStack = handler.extractItem(i, handler.getSlotLimit(i), false);
                    if (!materialStack.isEmpty()) {
                        ItemEntity itemEntity = new ItemEntity(this.f_58857_, (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 1.1, (double) this.f_58858_.m_123343_() + 0.5, materialStack);
                        itemEntity.setDefaultPickUpDelay();
                        this.f_58857_.m_7967_(itemEntity);
                    }
                }
            } else {
                for (int ix = 1; ix < 4; ix++) {
                    handler.extractItem(ix, handler.getSlotLimit(ix), false);
                }
            }
        });
    }

    private void transferStackToPlayer(Player player, int index) {
        this.handler.ifPresent(handler -> {
            ItemStack itemStack = handler.extractItem(index, handler.getSlotLimit(index), false);
            if (!itemStack.isEmpty() && !player.getInventory().add(itemStack)) {
                player.drop(itemStack, false);
            }
        });
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("workbench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
        return new WorkbenchContainer(windowId, this, playerInventory, playerEntity);
    }
}