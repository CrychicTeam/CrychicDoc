package top.theillusivec4.curios.api;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

@Deprecated(since = "1.20.1", forRemoval = true)
@ScheduledForRemoval(inVersion = "1.22")
public final class SlotTypeMessage {

    public static final String REGISTER_TYPE = "register_type";

    public static final String MODIFY_TYPE = "modify_type";

    private final String identifier;

    private final Integer priority;

    private final int size;

    private final boolean visible;

    private final boolean cosmetic;

    private final ResourceLocation icon;

    private SlotTypeMessage(SlotTypeMessage.Builder builder) {
        this.identifier = builder.identifier;
        this.priority = builder.priority;
        this.size = builder.size;
        this.visible = builder.visible;
        this.cosmetic = builder.cosmetic;
        this.icon = builder.icon;
    }

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public String getIdentifier() {
        return this.identifier;
    }

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public ResourceLocation getIcon() {
        return this.icon;
    }

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public Integer getPriority() {
        return this.priority;
    }

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public int getSize() {
        return this.size;
    }

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public boolean isLocked() {
        return this.getSize() == 0;
    }

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public boolean isVisible() {
        return this.visible;
    }

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public boolean hasCosmetic() {
        return this.cosmetic;
    }

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public static class Builder {

        private final String identifier;

        private Integer priority;

        private int size = 1;

        private boolean visible = true;

        private boolean cosmetic = false;

        private ResourceLocation icon = null;

        @Deprecated(since = "1.20.1", forRemoval = true)
        @ScheduledForRemoval(inVersion = "1.22")
        public Builder(String identifier) {
            this.identifier = identifier;
        }

        @Deprecated(since = "1.20.1", forRemoval = true)
        @ScheduledForRemoval(inVersion = "1.22")
        public SlotTypeMessage.Builder icon(ResourceLocation icon) {
            this.icon = icon;
            return this;
        }

        @Deprecated(since = "1.20.1", forRemoval = true)
        @ScheduledForRemoval(inVersion = "1.22")
        public SlotTypeMessage.Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        @Deprecated(since = "1.20.1", forRemoval = true)
        @ScheduledForRemoval(inVersion = "1.22")
        public SlotTypeMessage.Builder size(int size) {
            this.size = size;
            return this;
        }

        @Deprecated(since = "1.20.1", forRemoval = true)
        @ScheduledForRemoval(inVersion = "1.22")
        public SlotTypeMessage.Builder lock() {
            this.size = 0;
            return this;
        }

        @Deprecated(since = "1.20.1", forRemoval = true)
        @ScheduledForRemoval(inVersion = "1.22")
        public SlotTypeMessage.Builder hide() {
            this.visible = false;
            return this;
        }

        @Deprecated(since = "1.20.1", forRemoval = true)
        @ScheduledForRemoval(inVersion = "1.22")
        public SlotTypeMessage.Builder cosmetic() {
            this.cosmetic = true;
            return this;
        }

        @Deprecated(since = "1.20.1", forRemoval = true)
        @ScheduledForRemoval(inVersion = "1.22")
        public SlotTypeMessage build() {
            return new SlotTypeMessage(this);
        }
    }
}