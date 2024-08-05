package top.theillusivec4.curios.common.slottype;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.EnumUtils;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;

public final class SlotType implements ISlotType {

    private final String identifier;

    private final int order;

    private final int size;

    private final boolean useNativeGui;

    private final boolean hasCosmetic;

    private final ResourceLocation icon;

    private final ICurio.DropRule dropRule;

    private final boolean renderToggle;

    private final Set<ResourceLocation> validators;

    public static ISlotType from(CompoundTag tag) {
        SlotType.Builder builder = new SlotType.Builder(tag.getString("Identifier"));
        builder.icon(new ResourceLocation(tag.getString("Icon")));
        builder.order(tag.getInt("Order"));
        builder.size(tag.getInt("Size"));
        builder.useNativeGui(tag.getBoolean("UseNativeGui"));
        builder.hasCosmetic(tag.getBoolean("HasCosmetic"));
        builder.renderToggle(tag.getBoolean("ToggleRender"));
        builder.dropRule(ICurio.DropRule.values()[tag.getInt("DropRule")]);
        for (Tag tag1 : tag.getList("Validators", 8)) {
            if (tag1 instanceof StringTag stringTag) {
                builder.validator(new ResourceLocation(stringTag.getAsString()));
            }
        }
        return builder.build();
    }

    private SlotType(SlotType.Builder builder) {
        this.identifier = builder.identifier;
        this.order = builder.order;
        this.size = builder.size;
        this.useNativeGui = builder.useNativeGui;
        this.hasCosmetic = builder.hasCosmetic;
        this.icon = builder.icon;
        this.dropRule = builder.dropRule;
        this.renderToggle = builder.renderToggle;
        this.validators = builder.validators;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public ResourceLocation getIcon() {
        return this.icon;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public boolean useNativeGui() {
        return this.useNativeGui;
    }

    @Override
    public boolean hasCosmetic() {
        return this.hasCosmetic;
    }

    @Override
    public boolean canToggleRendering() {
        return this.renderToggle;
    }

    @Override
    public ICurio.DropRule getDropRule() {
        return this.dropRule;
    }

    @Override
    public Set<ResourceLocation> getValidators() {
        return this.validators;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SlotType that = (SlotType) o;
            return this.identifier.equals(that.identifier);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.identifier });
    }

    public int compareTo(ISlotType otherType) {
        if (this.order == otherType.getOrder()) {
            return this.identifier.compareTo(otherType.getIdentifier());
        } else {
            return this.order > otherType.getOrder() ? 1 : -1;
        }
    }

    @Override
    public CompoundTag writeNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Identifier", this.identifier);
        tag.putString("Icon", this.icon.toString());
        tag.putInt("Order", this.order);
        tag.putInt("Size", this.size);
        tag.putBoolean("UseNativeGui", this.useNativeGui);
        tag.putBoolean("HasCosmetic", this.hasCosmetic);
        tag.putBoolean("ToggleRender", this.renderToggle);
        tag.putInt("DropRule", this.dropRule.ordinal());
        ListTag list = new ListTag();
        for (ResourceLocation slotResultPredicate : this.validators) {
            list.add(StringTag.valueOf(slotResultPredicate.toString()));
        }
        tag.put("Validators", list);
        return tag;
    }

    public static class Builder {

        private final String identifier;

        private Integer order = null;

        private Integer size = null;

        private int sizeMod = 0;

        private Boolean useNativeGui = null;

        private Boolean hasCosmetic = null;

        private Boolean renderToggle = null;

        private ResourceLocation icon = new ResourceLocation("curios", "slot/empty_curio_slot");

        private ICurio.DropRule dropRule = ICurio.DropRule.DEFAULT;

        private Set<ResourceLocation> validators = null;

        public Builder(String identifier) {
            this.identifier = identifier;
        }

        public void apply(SlotType.Builder builder) {
            if (!builder.identifier.equals(this.identifier)) {
                Curios.LOGGER.error("Mismatched slot builders {} and {}", builder.identifier, this.identifier);
            } else {
                if (builder.order != null) {
                    this.order(builder.order);
                }
                if (builder.size != null) {
                    this.size(builder.size);
                }
                if (builder.useNativeGui != null) {
                    this.useNativeGui(builder.useNativeGui);
                }
                if (builder.hasCosmetic != null) {
                    this.hasCosmetic(builder.hasCosmetic);
                }
                if (builder.renderToggle != null) {
                    this.renderToggle(builder.renderToggle);
                }
                if (builder.icon != null) {
                    this.icon(builder.icon);
                }
                if (builder.dropRule != null) {
                    this.dropRule(builder.dropRule);
                }
                if (builder.validators != null) {
                    this.validators = Set.copyOf(builder.validators);
                }
            }
        }

        public SlotType.Builder icon(ResourceLocation icon) {
            this.icon = icon;
            return this;
        }

        public SlotType.Builder order(int order) {
            return this.order(order, false);
        }

        public SlotType.Builder order(int order, boolean replace) {
            this.order = !replace && this.order != null ? Math.min(this.order, order) : order;
            return this;
        }

        public SlotType.Builder size(int size) {
            return this.size(size, false);
        }

        public SlotType.Builder size(int size, String operation) {
            return this.size(size, operation, false);
        }

        public SlotType.Builder size(int size, boolean replace) {
            return this.size(size, "SET", replace);
        }

        public SlotType.Builder size(int size, String operation, boolean replace) {
            switch(operation) {
                case "SET":
                    this.size = !replace && this.size != null ? Math.max(this.size, size) : size;
                    if (replace) {
                        this.sizeMod = 0;
                    }
                    break;
                case "ADD":
                    this.sizeMod = replace ? size : this.sizeMod + size;
                    break;
                case "REMOVE":
                    this.sizeMod = replace ? -size : this.sizeMod - size;
            }
            return this;
        }

        public SlotType.Builder useNativeGui(boolean useNativeGui) {
            return this.useNativeGui(useNativeGui, false);
        }

        public SlotType.Builder useNativeGui(boolean useNativeGui, boolean replace) {
            this.useNativeGui = !replace && this.useNativeGui != null ? this.useNativeGui && useNativeGui : useNativeGui;
            return this;
        }

        public SlotType.Builder renderToggle(boolean renderToggle) {
            return this.renderToggle(renderToggle, false);
        }

        public SlotType.Builder renderToggle(boolean renderToggle, boolean replace) {
            this.renderToggle = !replace && this.renderToggle != null ? this.renderToggle && renderToggle : renderToggle;
            return this;
        }

        public SlotType.Builder hasCosmetic(boolean hasCosmetic) {
            return this.hasCosmetic(hasCosmetic, false);
        }

        public SlotType.Builder hasCosmetic(boolean hasCosmetic, boolean replace) {
            this.hasCosmetic = !replace && this.hasCosmetic != null ? this.hasCosmetic || hasCosmetic : hasCosmetic;
            return this;
        }

        public SlotType.Builder dropRule(ICurio.DropRule dropRule) {
            this.dropRule = dropRule;
            return this;
        }

        public SlotType.Builder dropRule(String dropRule) {
            ICurio.DropRule newRule = (ICurio.DropRule) EnumUtils.getEnum(ICurio.DropRule.class, dropRule);
            if (newRule == null) {
                Curios.LOGGER.error(dropRule + " is not a valid drop rule!");
            } else {
                this.dropRule = newRule;
            }
            return this;
        }

        public SlotType.Builder validator(ResourceLocation slotResultPredicate) {
            if (this.validators == null) {
                this.validators = new HashSet();
            }
            this.validators.add(slotResultPredicate);
            return this;
        }

        public SlotType build() {
            if (this.order == null) {
                this.order = 1000;
            }
            if (this.size == null) {
                this.size = 1;
            }
            this.size = this.size + this.sizeMod;
            this.size = Math.max(this.size, 0);
            if (this.useNativeGui == null) {
                this.useNativeGui = true;
            }
            if (this.hasCosmetic == null) {
                this.hasCosmetic = false;
            }
            if (this.renderToggle == null) {
                this.renderToggle = true;
            }
            if (this.validators == null) {
                this.validators = Set.of(new ResourceLocation("curios", "tag"));
            }
            return new SlotType(this);
        }
    }
}