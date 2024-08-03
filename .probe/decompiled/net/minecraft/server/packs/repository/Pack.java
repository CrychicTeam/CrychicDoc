package net.minecraft.server.packs.repository;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.packs.FeatureFlagsMetadataSection;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.world.flag.FeatureFlagSet;
import org.slf4j.Logger;

public class Pack {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String id;

    private final Pack.ResourcesSupplier resources;

    private final Component title;

    private final Component description;

    private final PackCompatibility compatibility;

    private final FeatureFlagSet requestedFeatures;

    private final Pack.Position defaultPosition;

    private final boolean required;

    private final boolean fixedPosition;

    private final PackSource packSource;

    @Nullable
    public static Pack readMetaAndCreate(String string0, Component component1, boolean boolean2, Pack.ResourcesSupplier packResourcesSupplier3, PackType packType4, Pack.Position packPosition5, PackSource packSource6) {
        Pack.Info $$7 = readPackInfo(string0, packResourcesSupplier3);
        return $$7 != null ? create(string0, component1, boolean2, packResourcesSupplier3, $$7, packType4, packPosition5, false, packSource6) : null;
    }

    public static Pack create(String string0, Component component1, boolean boolean2, Pack.ResourcesSupplier packResourcesSupplier3, Pack.Info packInfo4, PackType packType5, Pack.Position packPosition6, boolean boolean7, PackSource packSource8) {
        return new Pack(string0, boolean2, packResourcesSupplier3, component1, packInfo4, packInfo4.compatibility(packType5), packPosition6, boolean7, packSource8);
    }

    private Pack(String string0, boolean boolean1, Pack.ResourcesSupplier packResourcesSupplier2, Component component3, Pack.Info packInfo4, PackCompatibility packCompatibility5, Pack.Position packPosition6, boolean boolean7, PackSource packSource8) {
        this.id = string0;
        this.resources = packResourcesSupplier2;
        this.title = component3;
        this.description = packInfo4.description();
        this.compatibility = packCompatibility5;
        this.requestedFeatures = packInfo4.requestedFeatures();
        this.required = boolean1;
        this.defaultPosition = packPosition6;
        this.fixedPosition = boolean7;
        this.packSource = packSource8;
    }

    @Nullable
    public static Pack.Info readPackInfo(String string0, Pack.ResourcesSupplier packResourcesSupplier1) {
        try {
            Pack.Info var6;
            try (PackResources $$2 = packResourcesSupplier1.open(string0)) {
                PackMetadataSection $$3 = $$2.getMetadataSection(PackMetadataSection.TYPE);
                if ($$3 == null) {
                    LOGGER.warn("Missing metadata in pack {}", string0);
                    return null;
                }
                FeatureFlagsMetadataSection $$4 = $$2.getMetadataSection(FeatureFlagsMetadataSection.TYPE);
                FeatureFlagSet $$5 = $$4 != null ? $$4.flags() : FeatureFlagSet.of();
                var6 = new Pack.Info($$3.getDescription(), $$3.getPackFormat(), $$5);
            }
            return var6;
        } catch (Exception var9) {
            LOGGER.warn("Failed to read pack metadata", var9);
            return null;
        }
    }

    public Component getTitle() {
        return this.title;
    }

    public Component getDescription() {
        return this.description;
    }

    public Component getChatLink(boolean boolean0) {
        return ComponentUtils.wrapInSquareBrackets(this.packSource.decorate(Component.literal(this.id))).withStyle(p_10441_ -> p_10441_.withColor(boolean0 ? ChatFormatting.GREEN : ChatFormatting.RED).withInsertion(StringArgumentType.escapeIfRequired(this.id)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.empty().append(this.title).append("\n").append(this.description))));
    }

    public PackCompatibility getCompatibility() {
        return this.compatibility;
    }

    public FeatureFlagSet getRequestedFeatures() {
        return this.requestedFeatures;
    }

    public PackResources open() {
        return this.resources.open(this.id);
    }

    public String getId() {
        return this.id;
    }

    public boolean isRequired() {
        return this.required;
    }

    public boolean isFixedPosition() {
        return this.fixedPosition;
    }

    public Pack.Position getDefaultPosition() {
        return this.defaultPosition;
    }

    public PackSource getPackSource() {
        return this.packSource;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof Pack $$1) ? false : this.id.equals($$1.id);
        }
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public static record Info(Component f_244592_, int f_244194_, FeatureFlagSet f_244041_) {

        private final Component description;

        private final int format;

        private final FeatureFlagSet requestedFeatures;

        public Info(Component f_244592_, int f_244194_, FeatureFlagSet f_244041_) {
            this.description = f_244592_;
            this.format = f_244194_;
            this.requestedFeatures = f_244041_;
        }

        public PackCompatibility compatibility(PackType p_249204_) {
            return PackCompatibility.forFormat(this.format, p_249204_);
        }
    }

    public static enum Position {

        TOP, BOTTOM;

        public <T> int insert(List<T> p_10471_, T p_10472_, Function<T, Pack> p_10473_, boolean p_10474_) {
            Pack.Position $$4 = p_10474_ ? this.opposite() : this;
            if ($$4 == BOTTOM) {
                int $$5;
                for ($$5 = 0; $$5 < p_10471_.size(); $$5++) {
                    Pack $$6 = (Pack) p_10473_.apply(p_10471_.get($$5));
                    if (!$$6.isFixedPosition() || $$6.getDefaultPosition() != this) {
                        break;
                    }
                }
                p_10471_.add($$5, p_10472_);
                return $$5;
            } else {
                int $$7;
                for ($$7 = p_10471_.size() - 1; $$7 >= 0; $$7--) {
                    Pack $$8 = (Pack) p_10473_.apply(p_10471_.get($$7));
                    if (!$$8.isFixedPosition() || $$8.getDefaultPosition() != this) {
                        break;
                    }
                }
                p_10471_.add($$7 + 1, p_10472_);
                return $$7 + 1;
            }
        }

        public Pack.Position opposite() {
            return this == TOP ? BOTTOM : TOP;
        }
    }

    @FunctionalInterface
    public interface ResourcesSupplier {

        PackResources open(String var1);
    }
}