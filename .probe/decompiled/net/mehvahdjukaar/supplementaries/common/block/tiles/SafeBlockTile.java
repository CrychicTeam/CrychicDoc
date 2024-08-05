package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.block.IOwnerProtected;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.block.IKeyLockable;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SafeBlock;
import net.mehvahdjukaar.supplementaries.common.inventories.SafeContainerMenu;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SafeBlockTile extends OpeneableContainerBlockEntity implements IOwnerProtected, IKeyLockable {

    @Nullable
    private String password = null;

    private String ownerName = null;

    private UUID owner = null;

    public SafeBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.SAFE_TILE.get(), pos, state, 27);
    }

    public boolean handleAction(Player player, InteractionHand handIn) {
        ItemStack stack = player.m_21120_(handIn);
        Item item = stack.getItem();
        boolean cleared = false;
        if ((Boolean) CommonConfigs.Functional.SAFE_SIMPLE.get()) {
            if ((item == Items.TRIPWIRE_HOOK || stack.is(ModTags.KEYS)) && (this.isOwnedBy(player) || this.isNotOwnedBy(player) && player.isCreative())) {
                cleared = true;
            }
        } else if (player.m_6144_() && (player.isCreative() || this.getKeyStatus(stack).isCorrect())) {
            cleared = true;
        }
        if (cleared) {
            this.clearPassword();
            this.onPasswordCleared(player, this.f_58858_);
            return true;
        } else {
            BlockPos frontPos = this.f_58858_.relative((Direction) this.m_58900_().m_61143_(SafeBlock.FACING));
            if (!this.f_58857_.getBlockState(frontPos).m_60796_(this.f_58857_, frontPos)) {
                if ((Boolean) CommonConfigs.Functional.SAFE_SIMPLE.get()) {
                    UUID owner = this.getOwner();
                    if (owner == null) {
                        owner = player.m_20148_();
                        this.setOwner(owner);
                    }
                    if (!owner.equals(player.m_20148_())) {
                        player.displayClientMessage(Component.translatable("message.supplementaries.safe.owner", this.ownerName), true);
                        if (!player.isCreative()) {
                            return true;
                        }
                    }
                } else {
                    String key = this.getPassword();
                    if (key == null) {
                        String newKey = IKeyLockable.getKeyPassword(stack);
                        if (newKey != null) {
                            this.setPassword(newKey);
                            this.onKeyAssigned(this.f_58857_, this.f_58858_, player, newKey);
                            return true;
                        }
                    } else if (!this.canPlayerOpen(player, true) && !player.isCreative()) {
                        return true;
                    }
                }
                PlatHelper.openCustomMenu((ServerPlayer) player, this, this.f_58858_);
                PiglinAi.angerNearbyPiglins(player, true);
            }
            return true;
        }
    }

    public boolean canPlayerOpen(Player player, boolean feedbackMessage) {
        if (player == null || player.isCreative()) {
            return true;
        } else if (!(Boolean) CommonConfigs.Functional.SAFE_SIMPLE.get()) {
            return this.testIfHasCorrectKey(player, this.password, feedbackMessage, "safe");
        } else if (this.isNotOwnedBy(player)) {
            if (feedbackMessage) {
                player.displayClientMessage(Component.translatable("message.supplementaries.safe.owner", this.ownerName), true);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void clearPassword() {
        this.ownerName = null;
        this.owner = null;
        this.password = null;
    }

    @Nullable
    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(UUID owner) {
        if (this.f_58857_ != null) {
            if (owner != null) {
                Player p = this.f_58857_.m_46003_(owner);
                if (p != null) {
                    this.ownerName = p.getName().getString();
                }
                this.owner = owner;
            }
            this.m_6596_();
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
        }
    }

    @Override
    public boolean canOpen(Player player) {
        return !super.m_7525_(player) ? false : this.canPlayerOpen(player, false);
    }

    @Override
    public Component getDisplayName() {
        if ((Boolean) CommonConfigs.Functional.SAFE_SIMPLE.get()) {
            if (this.ownerName != null && this.shouldShowPassword()) {
                return Component.translatable("gui.supplementaries.safe.name", this.ownerName, super.m_5446_());
            }
        } else if (this.password != null && this.shouldShowPassword()) {
            return Component.translatable("gui.supplementaries.safe.password", this.password, super.m_5446_());
        }
        return super.m_5446_();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("gui.supplementaries.safe");
    }

    @Override
    protected void playOpenSound(BlockState state) {
        Vec3i vec3i = ((Direction) state.m_61143_(SafeBlock.FACING)).getNormal();
        double d0 = (double) this.f_58858_.m_123341_() + 0.5 + (double) vec3i.getX() / 2.0;
        double d1 = (double) this.f_58858_.m_123342_() + 0.5 + (double) vec3i.getY() / 2.0;
        double d2 = (double) this.f_58858_.m_123343_() + 0.5 + (double) vec3i.getZ() / 2.0;
        this.f_58857_.playSound(null, d0, d1, d2, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.65F);
    }

    @Override
    protected void playCloseSound(BlockState state) {
        Vec3i vec3i = ((Direction) state.m_61143_(SafeBlock.FACING)).getNormal();
        double d0 = (double) this.f_58858_.m_123341_() + 0.5 + (double) vec3i.getX() / 2.0;
        double d1 = (double) this.f_58858_.m_123342_() + 0.5 + (double) vec3i.getY() / 2.0;
        double d2 = (double) this.f_58858_.m_123343_() + 0.5 + (double) vec3i.getZ() / 2.0;
        this.f_58857_.playSound(null, d0, d1, d2, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.65F);
    }

    @Override
    protected void updateBlockState(BlockState state, boolean open) {
        this.f_58857_.setBlock(this.m_58899_(), (BlockState) state.m_61124_(SafeBlock.OPEN, open), 3);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Owner")) {
            this.owner = tag.getUUID("Owner");
        } else {
            this.owner = null;
        }
        if (tag.contains("OwnerName")) {
            this.ownerName = tag.getString("OwnerName");
        } else {
            this.owner = null;
        }
        if (tag.contains("Password")) {
            this.password = tag.getString("Password");
        } else {
            this.password = null;
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        this.saveOwner(compound);
        if (this.ownerName != null) {
            compound.putString("OwnerName", this.ownerName);
        }
        if (this.password != null) {
            compound.putString("Password", this.password);
        }
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return MiscUtils.isAllowedInShulker(stack, this.m_58904_()) && !this.getKeyStatus(stack).isCorrect();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv) {
        return inv.player.isSpectator() ? null : new SafeContainerMenu(id, inv, this);
    }
}