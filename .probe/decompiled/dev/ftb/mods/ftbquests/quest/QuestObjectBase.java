package dev.ftb.mods.ftbquests.quest;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.config.Tristate;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.math.Bits;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.ConfigIconItemStack;
import dev.ftb.mods.ftbquests.integration.RecipeModHelper;
import dev.ftb.mods.ftbquests.item.CustomIconItem;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import dev.ftb.mods.ftbquests.util.NBTUtils;
import dev.ftb.mods.ftbquests.util.NetUtils;
import dev.ftb.mods.ftbquests.util.ProgressChange;
import dev.ftb.mods.ftbquests.util.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class QuestObjectBase implements Comparable<QuestObjectBase> {

    private static final Pattern TAG_PATTERN = Pattern.compile("^[a-z0-9_]*$");

    private static Tristate sendNotifications = Tristate.DEFAULT;

    public final long id;

    protected boolean invalid = false;

    protected String rawTitle = "";

    private ItemStack rawIcon = ItemStack.EMPTY;

    private List<String> tags = new ArrayList(0);

    private Icon cachedIcon = null;

    private Component cachedTitle = null;

    private Set<String> cachedTags = null;

    public QuestObjectBase(long id) {
        this.id = id;
    }

    public static boolean isNull(@Nullable QuestObjectBase object) {
        return object == null || object.invalid;
    }

    public static long getID(@Nullable QuestObjectBase object) {
        return isNull(object) ? 0L : object.id;
    }

    public static String getCodeString(long id) {
        return String.format("%016X", id);
    }

    public static String getCodeString(@Nullable QuestObjectBase object) {
        return getCodeString(getID(object));
    }

    public static boolean shouldSendNotifications() {
        return sendNotifications.get(true);
    }

    public final boolean isValid() {
        return !this.invalid;
    }

    public final void setRawIcon(ItemStack rawIcon) {
        this.rawIcon = rawIcon;
    }

    public final String getRawTitle() {
        return this.rawTitle;
    }

    public final void setRawTitle(String rawTitle) {
        this.rawTitle = rawTitle;
    }

    public static long parseCodeString(String id) {
        if (!id.isEmpty() && !id.equals("-")) {
            try {
                return Long.parseLong(id.charAt(0) == '#' ? id.substring(1) : id, 16);
            } catch (Exception var2) {
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    public static Optional<Long> parseHexId(String id) {
        try {
            return Optional.of(Long.parseLong(id, 16));
        } catch (NumberFormatException var2) {
            return Optional.empty();
        }
    }

    public static Optional<String> titleToID(String s) {
        s = s.replace(' ', '_').replaceAll("\\W", "").toLowerCase().trim();
        while (s.startsWith("_")) {
            s = s.substring(1);
        }
        while (s.endsWith("_")) {
            s = s.substring(0, s.length() - 1);
        }
        return s.isEmpty() ? Optional.empty() : Optional.of(s);
    }

    public final String getCodeString() {
        return getCodeString(this.id);
    }

    public final String toString() {
        return this.getCodeString();
    }

    public final boolean equals(Object object) {
        return object == this;
    }

    public final int hashCode() {
        return Long.hashCode(this.id);
    }

    public abstract QuestObjectType getObjectType();

    public abstract BaseQuestFile getQuestFile();

    public Set<String> getTags() {
        if (this.tags.isEmpty()) {
            return Collections.emptySet();
        } else {
            if (this.cachedTags == null) {
                this.cachedTags = new LinkedHashSet(this.tags);
            }
            return this.cachedTags;
        }
    }

    public boolean hasTag(String tag) {
        return !this.tags.isEmpty() && this.getTags().contains(tag);
    }

    public void forceProgress(TeamData teamData, ProgressChange progressChange) {
    }

    public final void forceProgressRaw(TeamData teamData, ProgressChange progressChange) {
        if (!teamData.isLocked()) {
            teamData.clearCachedProgress();
            sendNotifications = progressChange.shouldNotify() ? Tristate.TRUE : Tristate.FALSE;
            this.forceProgress(teamData, progressChange);
            sendNotifications = Tristate.DEFAULT;
            teamData.clearCachedProgress();
            teamData.markDirty();
        }
    }

    @Nullable
    public Chapter getQuestChapter() {
        return null;
    }

    public long getParentID() {
        return 1L;
    }

    public void writeData(CompoundTag nbt) {
        if (!this.rawTitle.isEmpty()) {
            nbt.putString("title", this.rawTitle);
        }
        NBTUtils.write(nbt, "icon", this.rawIcon);
        if (!this.tags.isEmpty()) {
            ListTag tagList = new ListTag();
            for (String s : this.tags) {
                tagList.add(StringTag.valueOf(s));
            }
            nbt.put("tags", tagList);
        }
    }

    public void readData(CompoundTag nbt) {
        this.rawTitle = nbt.getString("title");
        this.rawIcon = NBTUtils.read(nbt, "icon");
        ListTag tagsList = nbt.getList("tags", 8);
        this.tags = new ArrayList(tagsList.size());
        for (int i = 0; i < tagsList.size(); i++) {
            this.tags.add(tagsList.getString(i));
        }
        if (nbt.contains("custom_id")) {
            this.tags.add(nbt.getString("custom_id"));
        }
    }

    public void writeNetData(FriendlyByteBuf buffer) {
        int flags = 0;
        flags = Bits.setFlag(flags, 1, !this.rawTitle.isEmpty());
        flags = Bits.setFlag(flags, 2, !this.rawIcon.isEmpty());
        flags = Bits.setFlag(flags, 4, !this.tags.isEmpty());
        buffer.writeVarInt(flags);
        if (!this.rawTitle.isEmpty()) {
            buffer.writeUtf(this.rawTitle, 32767);
        }
        if (!this.rawIcon.isEmpty()) {
            buffer.writeItem(this.rawIcon);
        }
        if (!this.tags.isEmpty()) {
            NetUtils.writeStrings(buffer, this.tags);
        }
    }

    public void readNetData(FriendlyByteBuf buffer) {
        int flags = buffer.readVarInt();
        this.rawTitle = Bits.getFlag(flags, 1) ? buffer.readUtf(32767) : "";
        this.rawIcon = Bits.getFlag(flags, 2) ? buffer.readItem() : ItemStack.EMPTY;
        this.tags = new ArrayList(0);
        if (Bits.getFlag(flags, 4)) {
            NetUtils.readStrings(buffer, this.tags);
        }
    }

    protected boolean hasTitleConfig() {
        return true;
    }

    protected boolean hasIconConfig() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void fillConfigGroup(ConfigGroup config) {
        if (this.hasTitleConfig()) {
            config.addString("title", this.rawTitle, v -> this.rawTitle = v, "").setNameKey("ftbquests.title").setOrder(-127);
        }
        if (this.hasIconConfig()) {
            config.add("icon", new ConfigIconItemStack(), this.rawIcon, v -> this.rawIcon = v, ItemStack.EMPTY).setNameKey("ftbquests.icon").setOrder(-126);
        }
        config.addList("tags", this.tags, new StringConfig(TAG_PATTERN), "").setNameKey("ftbquests.tags").setOrder(-125);
    }

    @OnlyIn(Dist.CLIENT)
    public abstract Component getAltTitle();

    @OnlyIn(Dist.CLIENT)
    public abstract Icon getAltIcon();

    @OnlyIn(Dist.CLIENT)
    public final Component getTitle() {
        if (this.cachedTitle != null) {
            return this.cachedTitle.copy();
        } else {
            if (!this.rawTitle.isEmpty()) {
                this.cachedTitle = TextUtils.parseRawText(this.rawTitle);
            } else {
                this.cachedTitle = this.getAltTitle();
            }
            return this.cachedTitle.copy();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public final MutableComponent getMutableTitle() {
        return this.getTitle().copy();
    }

    @OnlyIn(Dist.CLIENT)
    public final Icon getIcon() {
        if (this.cachedIcon == null) {
            if (!this.rawIcon.isEmpty()) {
                this.cachedIcon = CustomIconItem.getIcon(this.rawIcon);
            }
            if (this.cachedIcon == null || this.cachedIcon.isEmpty()) {
                this.cachedIcon = ThemeProperties.ICON.get(this);
            }
            if (this.cachedIcon.isEmpty()) {
                this.cachedIcon = this.getAltIcon();
            }
        }
        return this.cachedIcon;
    }

    public void deleteSelf() {
        this.getQuestFile().remove(this.id);
    }

    public void deleteChildren() {
    }

    @OnlyIn(Dist.CLIENT)
    public void editedFromGUI() {
        ClientQuestFile.INSTANCE.refreshGui();
    }

    public void editedFromGUIOnServer() {
    }

    public void onCreated() {
    }

    public Optional<String> getPath() {
        return Optional.empty();
    }

    public void clearCachedData() {
        this.cachedIcon = null;
        this.cachedTitle = null;
        this.cachedTags = null;
    }

    public ConfigGroup createSubGroup(ConfigGroup group) {
        return group.getOrCreateSubgroup(this.getObjectType().getId());
    }

    @OnlyIn(Dist.CLIENT)
    public void onEditButtonClicked(Runnable gui) {
        ConfigGroup group = new ConfigGroup("ftbquests", accepted -> {
            gui.run();
            if (accepted && this.validateEditedConfig()) {
                new EditObjectMessage(this).sendToServer();
            }
        }) {

            @Override
            public Component getName() {
                MutableComponent c = QuestObjectBase.this.getObjectType().getDescription().copy().withStyle(ChatFormatting.YELLOW);
                return Component.empty().append(c).append(": ").append(QuestObjectBase.this.getTitle());
            }
        };
        this.fillConfigGroup(this.createSubGroup(group));
        new EditConfigScreen(group).openGui();
    }

    protected boolean validateEditedConfig() {
        return true;
    }

    public Set<RecipeModHelper.Components> componentsToRefresh() {
        return EnumSet.noneOf(RecipeModHelper.Components.class);
    }

    public static <T extends QuestObjectBase> T copy(T orig, Supplier<T> factory) {
        T copied = (T) factory.get();
        if (copied == null) {
            return null;
        } else {
            CompoundTag tag = new CompoundTag();
            orig.writeData(tag);
            copied.readData(tag);
            return copied;
        }
    }

    public int compareTo(@NotNull QuestObjectBase other) {
        int typeCmp = Integer.compare(this.getObjectType().ordinal(), other.getObjectType().ordinal());
        return typeCmp == 0 ? this.getTitle().getString().toLowerCase().compareTo(other.getTitle().getString().toLowerCase()) : typeCmp;
    }
}