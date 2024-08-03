package net.mehvahdjukaar.amendments.common.tile;

import com.mojang.authlib.GameProfile;
import java.util.Map;
import java.util.Optional;
import net.mehvahdjukaar.amendments.AmendmentsClient;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class DoubleSkullBlockTile extends EnhancedSkullBlockTile {

    @Nullable
    protected SkullBlockEntity innerTileUp = null;

    private Block candleUp = null;

    private ResourceLocation waxTexture = null;

    public DoubleSkullBlockTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super((BlockEntityType) ModRegistry.SKULL_PILE_TILE.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.saveInnerTile("SkullUp", this.innerTileUp, tag);
        if (this.candleUp != null) {
            tag.putString("CandleAbove", Utils.getID(this.candleUp).toString());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.innerTileUp = this.loadInnerTile("SkullUp", this.innerTileUp, tag);
        Block b = null;
        if (tag.contains("CandleAbove")) {
            ResourceLocation candle = new ResourceLocation(tag.getString("CandleAbove"));
            Optional<Block> o = BuiltInRegistries.BLOCK.m_6612_(candle);
            if (o.isPresent()) {
                b = (Block) o.get();
            }
        }
        this.setCandleUp(b);
    }

    public ItemStack getSkullItemUp() {
        return this.innerTileUp != null ? new ItemStack(this.innerTileUp.m_58900_().m_60734_()) : ItemStack.EMPTY;
    }

    public void rotateUp(Rotation rotation) {
        if (this.innerTileUp != null) {
            BlockState state = this.innerTileUp.m_58900_();
            int r = (Integer) this.innerTileUp.m_58900_().m_61143_(SkullBlock.ROTATION);
            this.innerTileUp.m_155250_((BlockState) state.m_61124_(SkullBlock.ROTATION, rotation.rotate(r, 16)));
        }
    }

    public void rotateUpStep(int step) {
        if (this.innerTileUp != null) {
            BlockState state = this.innerTileUp.m_58900_();
            int r = (Integer) this.innerTileUp.m_58900_().m_61143_(SkullBlock.ROTATION);
            this.innerTileUp.m_155250_((BlockState) state.m_61124_(SkullBlock.ROTATION, (r - step + 16) % 16));
        }
    }

    @Override
    public void initialize(SkullBlockEntity oldTile, ItemStack skullStack, Player player, InteractionHand hand) {
        super.initialize(oldTile, skullStack, player, hand);
        if (skullStack.getItem() instanceof BlockItem bi && bi.getBlock() instanceof SkullBlock upSkull) {
            BlockPlaceContext context = new BlockPlaceContext(player, hand, skullStack, new BlockHitResult(new Vec3(0.5, 0.5, 0.5), Direction.UP, this.m_58899_(), false));
            BlockState state = upSkull.getStateForPlacement(context);
            if (state == null) {
                state = upSkull.m_49966_();
            }
            if (upSkull.m_142194_(this.m_58899_(), state) instanceof SkullBlockEntity blockEntity) {
                this.innerTileUp = blockEntity;
                GameProfile gameprofile = null;
                if (skullStack.hasTag()) {
                    CompoundTag compoundtag = skullStack.getTag();
                    if (compoundtag.contains("SkullOwner", 10)) {
                        gameprofile = NbtUtils.readGameProfile(compoundtag.getCompound("SkullOwner"));
                    } else if (compoundtag.contains("SkullOwner", 8) && !StringUtils.isBlank(compoundtag.getString("SkullOwner"))) {
                        gameprofile = new GameProfile(null, compoundtag.getString("SkullOwner"));
                    }
                }
                this.innerTileUp.setOwner(gameprofile);
            }
        }
    }

    public void updateWax(BlockState above) {
        this.setCandleUp(above.m_60734_());
        if (this.f_58857_ instanceof ServerLevel) {
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
        }
    }

    private void setCandleUp(Block above) {
        this.candleUp = null;
        if (above instanceof CandleBlock) {
            this.candleUp = above;
        }
        if (PlatHelper.getPhysicalSide().isClient()) {
            this.waxTexture = null;
            if (this.candleUp != null) {
                this.waxTexture = (ResourceLocation) ((Map) AmendmentsClient.SKULL_CANDLES_TEXTURES.get()).get(this.candleUp);
            }
        }
    }

    public ResourceLocation getWaxTexture() {
        return this.waxTexture;
    }

    @Nullable
    public BlockState getSkullUp() {
        return this.innerTileUp != null ? this.innerTileUp.m_58900_() : null;
    }

    @Nullable
    public BlockEntity getSkullTileUp() {
        return this.innerTileUp;
    }

    public static void ti2ck(Level level, BlockPos pos, BlockState state, DoubleSkullBlockTile e) {
        e.tick(level, pos, state);
        BlockEntity tileUp = e.getSkullTileUp();
        if (tileUp != null) {
            BlockState b = tileUp.getBlockState();
            if (b instanceof EntityBlock eb) {
                eb.getTicker(level, b, tileUp.getType());
            }
        }
    }
}