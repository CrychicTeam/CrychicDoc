package net.minecraft.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DisplayInfo {

    private final Component title;

    private final Component description;

    private final ItemStack icon;

    @Nullable
    private final ResourceLocation background;

    private final FrameType frame;

    private final boolean showToast;

    private final boolean announceChat;

    private final boolean hidden;

    private float x;

    private float y;

    public DisplayInfo(ItemStack itemStack0, Component component1, Component component2, @Nullable ResourceLocation resourceLocation3, FrameType frameType4, boolean boolean5, boolean boolean6, boolean boolean7) {
        this.title = component1;
        this.description = component2;
        this.icon = itemStack0;
        this.background = resourceLocation3;
        this.frame = frameType4;
        this.showToast = boolean5;
        this.announceChat = boolean6;
        this.hidden = boolean7;
    }

    public void setLocation(float float0, float float1) {
        this.x = float0;
        this.y = float1;
    }

    public Component getTitle() {
        return this.title;
    }

    public Component getDescription() {
        return this.description;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    @Nullable
    public ResourceLocation getBackground() {
        return this.background;
    }

    public FrameType getFrame() {
        return this.frame;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public boolean shouldShowToast() {
        return this.showToast;
    }

    public boolean shouldAnnounceChat() {
        return this.announceChat;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public static DisplayInfo fromJson(JsonObject jsonObject0) {
        Component $$1 = Component.Serializer.fromJson(jsonObject0.get("title"));
        Component $$2 = Component.Serializer.fromJson(jsonObject0.get("description"));
        if ($$1 != null && $$2 != null) {
            ItemStack $$3 = getIcon(GsonHelper.getAsJsonObject(jsonObject0, "icon"));
            ResourceLocation $$4 = jsonObject0.has("background") ? new ResourceLocation(GsonHelper.getAsString(jsonObject0, "background")) : null;
            FrameType $$5 = jsonObject0.has("frame") ? FrameType.byName(GsonHelper.getAsString(jsonObject0, "frame")) : FrameType.TASK;
            boolean $$6 = GsonHelper.getAsBoolean(jsonObject0, "show_toast", true);
            boolean $$7 = GsonHelper.getAsBoolean(jsonObject0, "announce_to_chat", true);
            boolean $$8 = GsonHelper.getAsBoolean(jsonObject0, "hidden", false);
            return new DisplayInfo($$3, $$1, $$2, $$4, $$5, $$6, $$7, $$8);
        } else {
            throw new JsonSyntaxException("Both title and description must be set");
        }
    }

    private static ItemStack getIcon(JsonObject jsonObject0) {
        if (!jsonObject0.has("item")) {
            throw new JsonSyntaxException("Unsupported icon type, currently only items are supported (add 'item' key)");
        } else {
            Item $$1 = GsonHelper.getAsItem(jsonObject0, "item");
            if (jsonObject0.has("data")) {
                throw new JsonParseException("Disallowed data tag found");
            } else {
                ItemStack $$2 = new ItemStack($$1);
                if (jsonObject0.has("nbt")) {
                    try {
                        CompoundTag $$3 = TagParser.parseTag(GsonHelper.convertToString(jsonObject0.get("nbt"), "nbt"));
                        $$2.setTag($$3);
                    } catch (CommandSyntaxException var4) {
                        throw new JsonSyntaxException("Invalid nbt tag: " + var4.getMessage());
                    }
                }
                return $$2;
            }
        }
    }

    public void serializeToNetwork(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeComponent(this.title);
        friendlyByteBuf0.writeComponent(this.description);
        friendlyByteBuf0.writeItem(this.icon);
        friendlyByteBuf0.writeEnum(this.frame);
        int $$1 = 0;
        if (this.background != null) {
            $$1 |= 1;
        }
        if (this.showToast) {
            $$1 |= 2;
        }
        if (this.hidden) {
            $$1 |= 4;
        }
        friendlyByteBuf0.writeInt($$1);
        if (this.background != null) {
            friendlyByteBuf0.writeResourceLocation(this.background);
        }
        friendlyByteBuf0.writeFloat(this.x);
        friendlyByteBuf0.writeFloat(this.y);
    }

    public static DisplayInfo fromNetwork(FriendlyByteBuf friendlyByteBuf0) {
        Component $$1 = friendlyByteBuf0.readComponent();
        Component $$2 = friendlyByteBuf0.readComponent();
        ItemStack $$3 = friendlyByteBuf0.readItem();
        FrameType $$4 = friendlyByteBuf0.readEnum(FrameType.class);
        int $$5 = friendlyByteBuf0.readInt();
        ResourceLocation $$6 = ($$5 & 1) != 0 ? friendlyByteBuf0.readResourceLocation() : null;
        boolean $$7 = ($$5 & 2) != 0;
        boolean $$8 = ($$5 & 4) != 0;
        DisplayInfo $$9 = new DisplayInfo($$3, $$1, $$2, $$6, $$4, $$7, false, $$8);
        $$9.setLocation(friendlyByteBuf0.readFloat(), friendlyByteBuf0.readFloat());
        return $$9;
    }

    public JsonElement serializeToJson() {
        JsonObject $$0 = new JsonObject();
        $$0.add("icon", this.serializeIcon());
        $$0.add("title", Component.Serializer.toJsonTree(this.title));
        $$0.add("description", Component.Serializer.toJsonTree(this.description));
        $$0.addProperty("frame", this.frame.getName());
        $$0.addProperty("show_toast", this.showToast);
        $$0.addProperty("announce_to_chat", this.announceChat);
        $$0.addProperty("hidden", this.hidden);
        if (this.background != null) {
            $$0.addProperty("background", this.background.toString());
        }
        return $$0;
    }

    private JsonObject serializeIcon() {
        JsonObject $$0 = new JsonObject();
        $$0.addProperty("item", BuiltInRegistries.ITEM.getKey(this.icon.getItem()).toString());
        if (this.icon.hasTag()) {
            $$0.addProperty("nbt", this.icon.getTag().toString());
        }
        return $$0;
    }
}