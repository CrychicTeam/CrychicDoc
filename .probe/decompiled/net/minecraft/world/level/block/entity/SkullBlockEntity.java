package net.minecraft.world.level.block.entity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Services;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SkullBlockEntity extends BlockEntity {

    public static final String TAG_SKULL_OWNER = "SkullOwner";

    public static final String TAG_NOTE_BLOCK_SOUND = "note_block_sound";

    @Nullable
    private static GameProfileCache profileCache;

    @Nullable
    private static MinecraftSessionService sessionService;

    @Nullable
    private static Executor mainThreadExecutor;

    @Nullable
    private GameProfile owner;

    @Nullable
    private ResourceLocation noteBlockSound;

    private int animationTickCount;

    private boolean isAnimating;

    public SkullBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.SKULL, blockPos0, blockState1);
    }

    public static void setup(Services services0, Executor executor1) {
        profileCache = services0.profileCache();
        sessionService = services0.sessionService();
        mainThreadExecutor = executor1;
    }

    public static void clear() {
        profileCache = null;
        sessionService = null;
        mainThreadExecutor = null;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        if (this.owner != null) {
            CompoundTag $$1 = new CompoundTag();
            NbtUtils.writeGameProfile($$1, this.owner);
            compoundTag0.put("SkullOwner", $$1);
        }
        if (this.noteBlockSound != null) {
            compoundTag0.putString("note_block_sound", this.noteBlockSound.toString());
        }
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        if (compoundTag0.contains("SkullOwner", 10)) {
            this.setOwner(NbtUtils.readGameProfile(compoundTag0.getCompound("SkullOwner")));
        } else if (compoundTag0.contains("ExtraType", 8)) {
            String $$1 = compoundTag0.getString("ExtraType");
            if (!StringUtil.isNullOrEmpty($$1)) {
                this.setOwner(new GameProfile(null, $$1));
            }
        }
        if (compoundTag0.contains("note_block_sound", 8)) {
            this.noteBlockSound = ResourceLocation.tryParse(compoundTag0.getString("note_block_sound"));
        }
    }

    public static void animation(Level level0, BlockPos blockPos1, BlockState blockState2, SkullBlockEntity skullBlockEntity3) {
        if (level0.m_276867_(blockPos1)) {
            skullBlockEntity3.isAnimating = true;
            skullBlockEntity3.animationTickCount++;
        } else {
            skullBlockEntity3.isAnimating = false;
        }
    }

    public float getAnimation(float float0) {
        return this.isAnimating ? (float) this.animationTickCount + float0 : (float) this.animationTickCount;
    }

    @Nullable
    public GameProfile getOwnerProfile() {
        return this.owner;
    }

    @Nullable
    public ResourceLocation getNoteBlockSound() {
        return this.noteBlockSound;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void setOwner(@Nullable GameProfile gameProfile0) {
        synchronized (this) {
            this.owner = gameProfile0;
        }
        this.updateOwnerProfile();
    }

    private void updateOwnerProfile() {
        updateGameprofile(this.owner, p_155747_ -> {
            this.owner = p_155747_;
            this.m_6596_();
        });
    }

    public static void updateGameprofile(@Nullable GameProfile gameProfile0, Consumer<GameProfile> consumerGameProfile1) {
        if (gameProfile0 != null && !StringUtil.isNullOrEmpty(gameProfile0.getName()) && (!gameProfile0.isComplete() || !gameProfile0.getProperties().containsKey("textures")) && profileCache != null && sessionService != null) {
            profileCache.getAsync(gameProfile0.getName(), p_182470_ -> Util.backgroundExecutor().execute(() -> Util.ifElse(p_182470_, p_276255_ -> {
                Property $$2 = (Property) Iterables.getFirst(p_276255_.getProperties().get("textures"), null);
                if ($$2 == null) {
                    MinecraftSessionService $$3 = sessionService;
                    if ($$3 == null) {
                        return;
                    }
                    p_276255_ = $$3.fillProfileProperties(p_276255_, true);
                }
                GameProfile $$4 = p_276255_;
                Executor $$5 = mainThreadExecutor;
                if ($$5 != null) {
                    $$5.execute(() -> {
                        GameProfileCache $$2x = profileCache;
                        if ($$2x != null) {
                            $$2x.add($$4);
                            consumerGameProfile1.accept($$4);
                        }
                    });
                }
            }, () -> {
                Executor $$2 = mainThreadExecutor;
                if ($$2 != null) {
                    $$2.execute(() -> consumerGameProfile1.accept(gameProfile0));
                }
            })));
        } else {
            consumerGameProfile1.accept(gameProfile0);
        }
    }
}