package org.violetmoon.quark.addons.oddities.block.be;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.addons.oddities.module.TinyPotatoModule;
import org.violetmoon.quark.addons.oddities.util.TinyPotatoInfo;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.zeta.util.MiscUtil;
import org.violetmoon.zeta.util.SimpleInventoryBlockEntity;

public class TinyPotatoBlockEntity extends SimpleInventoryBlockEntity implements Nameable {

    public static final String TAG_NAME = "name";

    public static final String TAG_ANGRY = "angery";

    private static final int JUMP_EVENT = 0;

    public int jumpTicks = 0;

    public Component name = Component.literal("");

    private int soundCd = 0;

    public boolean angry = false;

    private static final Map<String, String> GENDER = new HashMap();

    private static final Map<String, SoundEvent> SOUNDS = new HashMap();

    public TinyPotatoBlockEntity(BlockPos pos, BlockState state) {
        super(TinyPotatoModule.blockEntityType, pos, state);
    }

    public void interact(Player player, InteractionHand hand, ItemStack stack, Direction side) {
        int index = side.get3DDataValue();
        if (index >= 0) {
            ItemStack stackAt = this.m_8020_(index);
            if (!stackAt.isEmpty() && stack.isEmpty()) {
                player.m_21008_(hand, stackAt);
                this.m_6836_(index, ItemStack.EMPTY);
            } else if (!stack.isEmpty()) {
                ItemStack copy = stack.split(1);
                if (stack.isEmpty()) {
                    player.m_21008_(hand, stackAt);
                } else if (!stackAt.isEmpty()) {
                    player.getInventory().placeItemBackInInventory(stackAt);
                }
                this.m_6836_(index, copy);
            }
        }
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            this.jump();
            if (this.m_8077_()) {
                TinyPotatoInfo info = TinyPotatoInfo.fromComponent(this.name);
                String checkName = info.name().toLowerCase().trim();
                if (SOUNDS.containsKey(checkName) && this.soundCd == 0) {
                    SoundEvent playSound = (SoundEvent) SOUNDS.get(checkName);
                    this.soundCd = 20;
                    this.f_58857_.playSound(null, this.f_58858_, playSound, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
            ItemStack tater = ItemStack.EMPTY;
            boolean manyTater = false;
            for (int i = 0; i < this.getContainerSize(); i++) {
                ItemStack stackAt = this.m_8020_(i);
                if (!stackAt.isEmpty() && stackAt.is(TinyPotatoModule.tiny_potato.asItem())) {
                    if (!tater.isEmpty()) {
                        manyTater = true;
                        break;
                    }
                    tater = stackAt;
                }
            }
            if (!tater.isEmpty()) {
                String taterGender = manyTater ? "children" : "son";
                if (tater.hasCustomHoverName() && !manyTater) {
                    TinyPotatoInfo info = TinyPotatoInfo.fromComponent(tater.getHoverName());
                    taterGender = (String) GENDER.getOrDefault(info.name(), taterGender);
                }
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.sendSystemMessage(Component.translatable("quark.misc.my_" + taterGender), true);
                }
            }
        }
    }

    private void jump() {
        if (this.f_58857_ != null && this.jumpTicks == 0) {
            this.f_58857_.blockEvent(this.m_58899_(), this.m_58900_().m_60734_(), 0, 20);
        }
    }

    @Override
    public boolean triggerEvent(int id, int param) {
        if (id == 0) {
            this.jumpTicks = param;
            return true;
        } else {
            return super.m_7531_(id, param);
        }
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, TinyPotatoBlockEntity self) {
        if (self.jumpTicks > 0) {
            self.jumpTicks--;
        }
        if (!level.isClientSide) {
            if (level.random.nextInt(100) == 0) {
                self.jump();
            }
            if (self.soundCd > 0) {
                self.soundCd--;
            }
        }
    }

    @Override
    public void inventoryChanged(int i) {
        this.sync();
    }

    @Override
    public void setChanged() {
        super.m_6596_();
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            this.sync();
        }
    }

    @Override
    public void sync() {
        MiscUtil.syncTE(this);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void readSharedNBT(CompoundTag cmp) {
        super.readSharedNBT(cmp);
        this.name = Component.Serializer.fromJson(cmp.getString("name"));
        this.angry = cmp.getBoolean("angery");
    }

    @Override
    public void writeSharedNBT(CompoundTag cmp) {
        super.writeSharedNBT(cmp);
        cmp.putString("name", Component.Serializer.toJson(this.name));
        cmp.putBoolean("angery", this.angry);
    }

    @Override
    public int getContainerSize() {
        return 6;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, @NotNull ItemStack itemstack) {
        return this.m_8020_(slot).isEmpty();
    }

    @NotNull
    @Override
    public Component getName() {
        return Component.translatable(TinyPotatoModule.tiny_potato.getDescriptionId());
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name.getString().isEmpty() ? null : this.name;
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        if (this.m_8077_()) {
            Component customName = this.getCustomName();
            if (customName != null) {
                return customName;
            }
        }
        return this.getName();
    }

    static {
        GENDER.put("girlstater", "daughter");
        GENDER.put("lesbiabtater", "daughter");
        GENDER.put("lesbiamtater", "daughter");
        GENDER.put("lesbiantater", "daughter");
        GENDER.put("lesbitater", "daughter");
        GENDER.put("lessbientater", "daughter");
        GENDER.put("agendertater", "child");
        GENDER.put("enbytater", "child");
        GENDER.put("nbtater", "child");
        GENDER.put("nonbinarytater", "child");
        GENDER.put("robotater", "child");
        GENDER.put("wiretater", "child");
        GENDER.put("eutrotater", "child");
        GENDER.put("bob", "child");
        GENDER.put("snences", "child");
        GENDER.put("genderfluidtater", "child");
        GENDER.put("taterfluid", "child");
        GENDER.put("eggtater", "child");
        GENDER.put("tategg", "child");
        GENDER.put("transtater", "child");
        GENDER.put("manytater", "children");
        GENDER.put("pluraltater", "children");
        GENDER.put("snorps", "children");
        GENDER.put("systater", "children");
        GENDER.put("systemtater", "children");
        GENDER.put("tomater", "tomato");
        SOUNDS.put("shia labeouf", QuarkSounds.BLOCK_POTATO_DO_IT);
        SOUNDS.put("joe biden", QuarkSounds.BLOCK_POTATO_SODA);
        SOUNDS.put("yungnickyoung", QuarkSounds.BLOCK_POTATO_YUNG);
    }
}