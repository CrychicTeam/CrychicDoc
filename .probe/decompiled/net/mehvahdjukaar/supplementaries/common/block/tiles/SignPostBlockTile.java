package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.Optional;
import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.block.IOwnerProtected;
import net.mehvahdjukaar.moonlight.api.block.MimicBlockTile;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.supplementaries.client.screens.SignPostScreen;
import net.mehvahdjukaar.supplementaries.common.block.ITextHolderProvider;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.TextHolder;
import net.mehvahdjukaar.supplementaries.common.block.blocks.StickBlock;
import net.mehvahdjukaar.supplementaries.common.items.SignPostItem;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.FramedBlocksCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SignPostBlockTile extends MimicBlockTile implements ITextHolderProvider, IOwnerProtected {

    public static final ModelDataKey<Boolean> FRAMED_KEY = ModBlockProperties.FRAMED;

    public static final ModelDataKey<Boolean> SLIM_KEY = ModBlockProperties.SLIM;

    public static final ModelDataKey<SignPostBlockTile.Sign> SIGN_UP_KEY = ModBlockProperties.SIGN_UP;

    public static final ModelDataKey<SignPostBlockTile.Sign> SIGN_DOWN_KEY = ModBlockProperties.SIGN_DOWN;

    private final SignPostBlockTile.Sign signUp = new SignPostBlockTile.Sign(false, true, 0.0F, WoodTypeRegistry.OAK_TYPE);

    private final SignPostBlockTile.Sign signDown = new SignPostBlockTile.Sign(false, false, 0.0F, WoodTypeRegistry.OAK_TYPE);

    private boolean isWaxed = false;

    private UUID owner = null;

    private boolean isSlim = false;

    private boolean framed = false;

    @Nullable
    private UUID playerWhoMayEdit;

    public SignPostBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.SIGN_POST_TILE.get(), pos, state);
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        super.addExtraModelData(builder);
        builder.with(FRAMED_KEY, this.framed).with(SIGN_UP_KEY, this.signUp).with(SIGN_DOWN_KEY, this.signDown).with(SLIM_KEY, this.isSlim);
    }

    @Override
    public TextHolder getTextHolder(int i) {
        return this.getSign(i == 0).text;
    }

    @Override
    public int textHoldersCount() {
        return 2;
    }

    public AABB getRenderBoundingBox() {
        BlockPos pos = this.m_58899_();
        return new AABB((double) pos.m_123341_() - 0.25, (double) pos.m_123342_(), (double) pos.m_123343_() - 0.25, (double) pos.m_123341_() + 1.25, (double) pos.m_123342_() + 1.0, (double) pos.m_123343_() + 1.25);
    }

    public float getPointingYaw(boolean up) {
        return up ? this.signUp.getPointing() : this.signDown.getPointing();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.framed = compound.getBoolean("Framed");
        this.signUp.load(compound.getCompound("SignUp"), this.f_58857_, this.f_58858_);
        this.signDown.load(compound.getCompound("SignDown"), this.f_58857_, this.f_58858_);
        this.loadOwner(compound);
        this.isSlim = this.mimic.m_60734_() instanceof StickBlock;
        if (compound.contains("Waxed")) {
            this.isWaxed = compound.getBoolean("Waxed");
        }
        if (this.f_58857_ != null && this.f_58857_.isClientSide) {
            this.requestModelReload();
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("Framed", this.framed);
        compound.put("SignUp", this.signUp.save());
        compound.put("SignDown", this.signDown.save());
        this.saveOwner(compound);
        if (this.isWaxed) {
            compound.putBoolean("Waxed", this.isWaxed);
        }
    }

    @Nullable
    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public boolean rotateSign(boolean up, float angle, boolean constrainAngle) {
        if (up && this.signUp.active) {
            this.signUp.rotateBy(angle, constrainAngle);
            return true;
        } else if (this.signDown.active) {
            this.signDown.rotateBy(angle, constrainAngle);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void openScreen(Level level, BlockPos pos, Player player) {
        SignPostScreen.open(this);
    }

    public boolean isSlim() {
        return this.isSlim;
    }

    public SignPostBlockTile.Sign getSignUp() {
        return this.signUp;
    }

    public SignPostBlockTile.Sign getSignDown() {
        return this.signDown;
    }

    public SignPostBlockTile.Sign getSign(boolean up) {
        return up ? this.getSignUp() : this.getSignDown();
    }

    public boolean isFramed() {
        return this.framed;
    }

    public boolean initializeSignAfterConversion(WoodType woodType, int r, boolean up, boolean slim, boolean framed) {
        SignPostBlockTile.Sign sign = this.getSign(up);
        if (!sign.active) {
            sign.active = true;
            sign.woodType = woodType;
            sign.yaw = 90.0F + (float) r * -22.5F;
            this.framed = framed;
            this.isSlim = slim;
            return true;
        } else {
            return false;
        }
    }

    public InteractionResult handleInteraction(BlockState state, ServerLevel level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit, ItemStack itemstack) {
        Item item = itemstack.getItem();
        boolean emptyHand = itemstack.isEmpty();
        boolean isSneaking = player.m_6144_() && emptyHand;
        boolean ind = this.getClickedSignIndex(hit.m_82450_());
        if (hit.getDirection().getAxis() != Direction.Axis.Y) {
            SignPostBlockTile.Sign sign = this.getSign(ind);
            if (!sign.active && item instanceof SignPostItem) {
                return InteractionResult.PASS;
            }
            if (isSneaking) {
                sign.toggleDirection();
                this.m_6596_();
                level.sendBlockUpdated(pos, state, state, 3);
                level.m_5594_(null, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0F, 0.6F);
                return InteractionResult.CONSUME;
            }
            if (item instanceof CompassItem) {
                BlockPos pointingPos = CompassItem.isLodestoneCompass(itemstack) ? this.getLodestonePos(level, itemstack) : this.getWorldSpawnPos(level);
                if (pointingPos != null) {
                    if (sign.active) {
                        sign.pointToward(pos, pointingPos);
                    }
                    this.m_6596_();
                    level.sendBlockUpdated(pos, state, state, 3);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.FAIL;
            }
            if (CompatHandler.FRAMEDBLOCKS && this.framed) {
                boolean success = FramedBlocksCompat.interactWithFramedSignPost(this, player, handIn, itemstack, level, pos);
                if (success) {
                    return InteractionResult.CONSUME;
                }
            }
        }
        return this.interactWithTextHolder(ind ? 0 : 1, level, pos, state, player, handIn);
    }

    public boolean getClickedSignIndex(Vec3 hit) {
        double y = hit.y;
        if (y < 0.0) {
            y += (double) (1 - (int) y);
        } else {
            y -= (double) ((int) y);
        }
        return y > 0.5;
    }

    public SignPostBlockTile.Sign getClickedSign(Vec3 hit) {
        return this.getSign(this.getClickedSignIndex(hit));
    }

    @Nullable
    private BlockPos getLodestonePos(Level world, ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            boolean flag = tag.contains("LodestonePos");
            boolean flag1 = tag.contains("LodestoneDimension");
            if (flag && flag1) {
                Optional<ResourceKey<Level>> optional = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get("LodestoneDimension")).result();
                if (optional.isPresent() && world.dimension() == optional.get()) {
                    return NbtUtils.readBlockPos(tag.getCompound("LodestonePos"));
                }
            }
        }
        return null;
    }

    @Nullable
    private BlockPos getWorldSpawnPos(Level world) {
        return world.dimensionType().natural() ? new BlockPos(world.getLevelData().getXSpawn(), world.getLevelData().getYSpawn(), world.getLevelData().getZSpawn()) : null;
    }

    @Override
    public void setWaxed(boolean waxed) {
        this.isWaxed = waxed;
    }

    @Override
    public boolean isWaxed() {
        return this.isWaxed;
    }

    @Override
    public void setPlayerWhoMayEdit(UUID playerWhoMayEdit) {
        this.playerWhoMayEdit = playerWhoMayEdit;
    }

    @Override
    public UUID getPlayerWhoMayEdit() {
        return this.playerWhoMayEdit;
    }

    public static final class Sign {

        public final TextHolder text;

        private boolean active;

        private boolean left;

        private float yaw;

        private WoodType woodType;

        private Sign(boolean active, boolean left, float yaw, WoodType woodType) {
            this.active = active;
            this.left = left;
            this.yaw = yaw;
            this.woodType = woodType;
            this.text = new TextHolder(1, 90);
        }

        public void load(CompoundTag compound, Level level, BlockPos pos) {
            this.active = compound.getBoolean("Active");
            this.left = compound.getBoolean("Left");
            this.yaw = compound.getFloat("Yaw");
            this.woodType = WoodTypeRegistry.fromNBT(compound.getString("WoodType"));
            this.text.load(compound, level, pos);
        }

        public CompoundTag save() {
            CompoundTag compound = new CompoundTag();
            compound.putFloat("Yaw", this.yaw);
            compound.putBoolean("Left", this.left);
            compound.putBoolean("Active", this.active);
            compound.putString("WoodType", this.woodType.toString());
            this.text.save(compound);
            return compound;
        }

        public void pointToward(BlockPos myPos, BlockPos targetPos) {
            float yaw = (float) (Math.atan2((double) targetPos.m_123341_() - (double) myPos.m_123341_(), (double) targetPos.m_123343_() - (double) myPos.m_123343_()) * 180.0F / (float) Math.PI);
            this.setYaw(yaw);
        }

        private float getPointing() {
            return Mth.wrapDegrees(-this.yaw - (float) (this.left ? 180 : 0));
        }

        private void setYaw(float yaw) {
            this.yaw = Mth.wrapDegrees(yaw - (float) (this.left ? 180 : 0));
        }

        private void rotateBy(float angle, boolean constrainAngle) {
            this.yaw = Mth.wrapDegrees(this.yaw + angle);
            if (constrainAngle) {
                this.yaw = this.yaw - this.yaw % 22.5F;
            }
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setLeft(boolean left) {
            this.left = left;
        }

        public void setWoodType(WoodType woodType) {
            this.woodType = woodType;
        }

        public boolean active() {
            return this.active;
        }

        public boolean left() {
            return this.left;
        }

        public float yaw() {
            return this.yaw;
        }

        public WoodType woodType() {
            return this.woodType;
        }

        public void toggleDirection() {
            this.left = !this.left;
        }

        public ItemStack getItem() {
            return new ItemStack((ItemLike) ModRegistry.SIGN_POST_ITEMS.get(this.woodType));
        }
    }
}