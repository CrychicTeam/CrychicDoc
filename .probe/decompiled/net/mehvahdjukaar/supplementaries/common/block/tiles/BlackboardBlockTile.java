package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.block.IOwnerProtected;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.IExtraModelDataProvider;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.supplementaries.client.BlackboardManager;
import net.mehvahdjukaar.supplementaries.client.screens.BlackBoardScreen;
import net.mehvahdjukaar.supplementaries.common.block.IOnePlayerGui;
import net.mehvahdjukaar.supplementaries.common.block.IWaxable;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BlackboardBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.NoticeBoardBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlackboardBlockTile extends BlockEntity implements IOwnerProtected, IOnePlayerGui, IWaxable, IExtraModelDataProvider {

    public static final ModelDataKey<BlackboardManager.Key> BLACKBOARD_KEY = ModBlockProperties.BLACKBOARD;

    private UUID owner = null;

    private boolean waxed = false;

    private byte[][] pixels = new byte[16][16];

    @Nullable
    private UUID playerWhoMayEdit = null;

    private BlackboardManager.Key textureKey = null;

    public BlackboardBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.BLACKBOARD_TILE.get(), pos, state);
        this.clear();
    }

    @Override
    public ExtraModelData getExtraModelData() {
        return ExtraModelData.builder().with(BLACKBOARD_KEY, this.getTextureKey()).build();
    }

    public BlackboardManager.Key getTextureKey() {
        if (this.textureKey == null) {
            this.refreshTextureKey();
        }
        return this.textureKey;
    }

    public void refreshTextureKey() {
        this.textureKey = BlackboardManager.Key.of(packPixels(this.pixels), (Boolean) this.m_58900_().m_61143_(BlackboardBlock.GLOWING));
    }

    @Override
    public void afterDataPacket(ExtraModelData oldData) {
        this.refreshTextureKey();
        IExtraModelDataProvider.super.afterDataPacket(oldData);
    }

    @Override
    public void setChanged() {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
            super.setChanged();
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.loadFromTag(compound);
        this.loadOwner(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        this.savePixels(compound);
        this.saveOwner(compound);
    }

    public CompoundTag savePixels(CompoundTag compound) {
        if (this.waxed) {
            compound.putBoolean("Waxed", true);
        }
        compound.putLongArray("Pixels", packPixels(this.pixels));
        return compound;
    }

    public void loadFromTag(CompoundTag compound) {
        this.waxed = compound.contains("Waxed") && compound.getBoolean("Waxed");
        this.pixels = new byte[16][16];
        if (compound.contains("Pixels")) {
            this.pixels = unpackPixels(compound.getLongArray("Pixels"));
        }
    }

    public static long[] packPixels(byte[][] pixels) {
        long[] packed = new long[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            long l = 0L;
            for (int j = 0; j < pixels[i].length; j++) {
                l |= (long) (pixels[i][j] & 15) << j * 4;
            }
            packed[i] = l;
        }
        return packed;
    }

    public static byte[][] unpackPixels(long[] packed) {
        byte[][] bytes = new byte[16][16];
        for (int i = 0; i < packed.length; i++) {
            for (int j = 0; j < 16; j++) {
                bytes[i][j] = (byte) ((int) (packed[i] >> j * 4 & 15L));
            }
        }
        return bytes;
    }

    public static String packPixelsToString(long[] packed) {
        StringBuilder builder = new StringBuilder();
        for (long l : packed) {
            char a = (char) ((int) (l & 65535L));
            char b = (char) ((int) (l >> 16 & 65535L));
            char c = (char) ((int) (l >> 32 & 65535L));
            char d = (char) ((int) (l >> 48 & 65535L));
            builder.append(a).append(b).append(c).append(d);
        }
        return builder.toString();
    }

    public static long[] unpackPixelsFromString(String packed) {
        long[] unpacked = new long[16];
        char[] chars = packed.toCharArray();
        int j = 0;
        for (int i = 0; i + 3 < chars.length; i += 4) {
            unpacked[j] = (long) chars[i + 3] << 48 | (long) chars[i + 2] << 32 | (long) chars[i + 1] << 16 | (long) chars[i];
            j++;
        }
        return unpacked;
    }

    public static long[] unpackPixelsFromStringWhiteOnly(String packed) {
        long[] unpacked = new long[16];
        char[] chars = packed.toCharArray();
        int j = 0;
        for (int i = 0; i + 3 < chars.length; i += 4) {
            long l = 0L;
            char c = chars[i];
            for (int k = 0; k < 4; k++) {
                l |= (long) ((c >> k & 1) << 4 * k);
            }
            char c2 = chars[i + 1];
            for (int k = 0; k < 4; k++) {
                l |= (long) (c2 >> k & 1) << 16 + 4 * k;
            }
            char c3 = chars[i + 2];
            for (int k = 0; k < 4; k++) {
                l |= (long) (c3 >> k & 1) << 32 + 4 * k;
            }
            char c4 = chars[i + 3];
            for (int k = 0; k < 4; k++) {
                l |= (long) (c4 >> k & 1) << 48 + 4 * k;
            }
            unpacked[j] = l;
            j++;
        }
        return unpacked;
    }

    public static String packPixelsToStringWhiteOnly(long[] packed) {
        StringBuilder builder = new StringBuilder();
        for (long l : packed) {
            char c = 0;
            for (int k = 0; k < 4; k++) {
                byte h = (byte) ((int) (l >> 4 * k & 1L));
                c = (char) (c | h << k);
            }
            char c1 = 0;
            for (int k = 0; k < 4; k++) {
                byte h = (byte) ((int) (l >> 16 + 4 * k & 1L));
                c1 = (char) (c1 | h << k);
            }
            char c2 = 0;
            for (int k = 0; k < 4; k++) {
                byte h = (byte) ((int) (l >> 32 + 4 * k & 1L));
                c2 = (char) (c2 | h << k);
            }
            char c3 = 0;
            for (int k = 0; k < 4; k++) {
                byte h = (byte) ((int) (l >> 48 + 4 * k & 1L));
                c3 = (char) (c3 | h << k);
            }
            builder.append(c).append(c1).append(c2).append(c3);
        }
        return builder.toString();
    }

    public void clear() {
        for (int x = 0; x < this.pixels.length; x++) {
            for (int y = 0; y < this.pixels[x].length; y++) {
                this.pixels[x][y] = 0;
            }
        }
    }

    public boolean isEmpty() {
        boolean flag = false;
        for (byte[] pixel : this.pixels) {
            for (byte b : pixel) {
                if (b != 0) {
                    flag = true;
                    break;
                }
            }
        }
        return !flag;
    }

    public void setPixel(int x, int y, byte b) {
        this.pixels[x][y] = b;
    }

    public byte getPixel(int xx, int yy) {
        return this.pixels[xx][yy];
    }

    public void setPixels(byte[][] pixels) {
        this.pixels = pixels;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(NoticeBoardBlock.FACING);
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

    @Override
    public void openScreen(Level level, BlockPos pos, Player player) {
        BlackBoardScreen.open(this);
    }

    @Override
    public void setWaxed(boolean b) {
        this.waxed = b;
    }

    @Override
    public boolean isWaxed() {
        return this.waxed;
    }

    @Override
    public UUID getPlayerWhoMayEdit() {
        return this.playerWhoMayEdit;
    }

    @Override
    public void setPlayerWhoMayEdit(UUID playerWhoMayEdit) {
        this.playerWhoMayEdit = playerWhoMayEdit;
    }
}