package top.theillusivec4.curios.api;

import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

@Deprecated(forRemoval = true)
@ScheduledForRemoval(inVersion = "1.22")
public enum SlotTypePreset {

    HEAD("head", 40),
    NECKLACE("necklace", 60),
    BACK("back", 80),
    BODY("body", 100),
    BRACELET("bracelet", 120),
    HANDS("hands", 140),
    RING("ring", 160),
    BELT("belt", 180),
    CHARM("charm", 200),
    CURIO("curio", 20);

    final String id;

    final int priority;

    private SlotTypePreset(String id, int priority) {
        this.id = id;
        this.priority = priority;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public static Optional<SlotTypePreset> findPreset(String id) {
        try {
            return Optional.of(valueOf(id.toUpperCase()));
        } catch (IllegalArgumentException var2) {
            return Optional.empty();
        }
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public String getIdentifier() {
        return this.id;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    public SlotTypeMessage.Builder getMessageBuilder() {
        return new SlotTypeMessage.Builder(this.id).priority(this.priority).icon(new ResourceLocation("curios", "slot/empty_" + this.getIdentifier() + "_slot"));
    }
}