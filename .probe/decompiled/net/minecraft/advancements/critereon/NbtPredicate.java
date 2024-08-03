package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NbtPredicate {

    public static final NbtPredicate ANY = new NbtPredicate(null);

    @Nullable
    private final CompoundTag tag;

    public NbtPredicate(@Nullable CompoundTag compoundTag0) {
        this.tag = compoundTag0;
    }

    public boolean matches(ItemStack itemStack0) {
        return this == ANY ? true : this.matches(itemStack0.getTag());
    }

    public boolean matches(Entity entity0) {
        return this == ANY ? true : this.matches(getEntityTagToCompare(entity0));
    }

    public boolean matches(@Nullable Tag tag0) {
        return tag0 == null ? this == ANY : this.tag == null || NbtUtils.compareNbt(this.tag, tag0, true);
    }

    public JsonElement serializeToJson() {
        return (JsonElement) (this != ANY && this.tag != null ? new JsonPrimitive(this.tag.toString()) : JsonNull.INSTANCE);
    }

    public static NbtPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            CompoundTag $$1;
            try {
                $$1 = TagParser.parseTag(GsonHelper.convertToString(jsonElement0, "nbt"));
            } catch (CommandSyntaxException var3) {
                throw new JsonSyntaxException("Invalid nbt tag: " + var3.getMessage());
            }
            return new NbtPredicate($$1);
        } else {
            return ANY;
        }
    }

    public static CompoundTag getEntityTagToCompare(Entity entity0) {
        CompoundTag $$1 = entity0.saveWithoutId(new CompoundTag());
        if (entity0 instanceof Player) {
            ItemStack $$2 = ((Player) entity0).getInventory().getSelected();
            if (!$$2.isEmpty()) {
                $$1.put("SelectedItem", $$2.save(new CompoundTag()));
            }
        }
        return $$1;
    }
}