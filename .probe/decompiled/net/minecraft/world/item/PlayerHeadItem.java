package net.minecraft.world.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

public class PlayerHeadItem extends StandingAndWallBlockItem {

    public static final String TAG_SKULL_OWNER = "SkullOwner";

    public PlayerHeadItem(Block block0, Block block1, Item.Properties itemProperties2) {
        super(block0, block1, itemProperties2, Direction.DOWN);
    }

    @Override
    public Component getName(ItemStack itemStack0) {
        if (itemStack0.is(Items.PLAYER_HEAD) && itemStack0.hasTag()) {
            String $$1 = null;
            CompoundTag $$2 = itemStack0.getTag();
            if ($$2.contains("SkullOwner", 8)) {
                $$1 = $$2.getString("SkullOwner");
            } else if ($$2.contains("SkullOwner", 10)) {
                CompoundTag $$3 = $$2.getCompound("SkullOwner");
                if ($$3.contains("Name", 8)) {
                    $$1 = $$3.getString("Name");
                }
            }
            if ($$1 != null) {
                return Component.translatable(this.m_5524_() + ".named", $$1);
            }
        }
        return super.m_7626_(itemStack0);
    }

    @Override
    public void verifyTagAfterLoad(CompoundTag compoundTag0) {
        super.m_142312_(compoundTag0);
        if (compoundTag0.contains("SkullOwner", 8) && !Util.isBlank(compoundTag0.getString("SkullOwner"))) {
            GameProfile $$1 = new GameProfile(null, compoundTag0.getString("SkullOwner"));
            SkullBlockEntity.updateGameprofile($$1, p_151177_ -> compoundTag0.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), p_151177_)));
        }
    }
}