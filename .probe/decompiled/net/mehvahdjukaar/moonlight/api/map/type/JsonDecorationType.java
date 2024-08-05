package net.mehvahdjukaar.moonlight.api.map.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.markers.SimpleMapBlockMarker;
import net.mehvahdjukaar.moonlight.api.misc.StrOpt;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import org.jetbrains.annotations.Nullable;

public final class JsonDecorationType implements MapDecorationType<CustomMapDecoration, SimpleMapBlockMarker> {

    public static final Codec<JsonDecorationType> CODEC = RecordCodecBuilder.create(instance -> instance.group(StrOpt.of(RuleTest.CODEC, "target_block").forGetter(JsonDecorationType::getTarget), StrOpt.of(Codec.STRING, "name").forGetter(JsonDecorationType::getName), StrOpt.of(Codec.INT, "rotation", 0).forGetter(JsonDecorationType::getRotation), StrOpt.of(ColorUtils.CODEC, "map_color", 0).forGetter(JsonDecorationType::getDefaultMapColor), StrOpt.of(RegistryCodecs.homogeneousList(Registries.STRUCTURE), "target_structures").forGetter(JsonDecorationType::getAssociatedStructure), Codec.STRING.xmap(PlatHelper::isModLoaded, b -> "minecraft").optionalFieldOf("from_mod", true).forGetter(t -> t.enabled)).apply(instance, JsonDecorationType::new));

    public static final Codec<JsonDecorationType> NETWORK_CODEC = RecordCodecBuilder.create(instance -> instance.group(StrOpt.of(RuleTest.CODEC, "target_block").forGetter(JsonDecorationType::getTarget), StrOpt.of(Codec.STRING, "name").forGetter(JsonDecorationType::getName), StrOpt.of(Codec.INT, "rotation", 0).forGetter(JsonDecorationType::getRotation), StrOpt.of(ColorUtils.CODEC, "map_color", 0).forGetter(JsonDecorationType::getDefaultMapColor), Codec.BOOL.fieldOf("enabled").forGetter(t -> t.enabled)).apply(instance, JsonDecorationType::new));

    @Nullable
    private final RuleTest target;

    @Nullable
    private final String name;

    @Nullable
    private final HolderSet<Structure> structures;

    private final int mapColor;

    private final int rotation;

    private final boolean enabled;

    public JsonDecorationType(Optional<RuleTest> target) {
        this(target, Optional.empty(), 0, 0, true);
    }

    public JsonDecorationType(Optional<RuleTest> target, Optional<String> name, int rotation, int mapColor, boolean enabled) {
        this(target, name, rotation, mapColor, Optional.empty(), enabled);
    }

    public JsonDecorationType(Optional<RuleTest> target, Optional<String> name, int rotation, int mapColor, Optional<HolderSet<Structure>> structure, Boolean enabled) {
        this.target = (RuleTest) target.orElse(null);
        this.name = (String) name.orElse(null);
        this.rotation = rotation;
        this.structures = (HolderSet<Structure>) structure.orElse(null);
        this.mapColor = mapColor;
        this.enabled = enabled;
    }

    public Optional<RuleTest> getTarget() {
        return Optional.ofNullable(this.target);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public int getRotation() {
        return this.rotation;
    }

    @Override
    public Optional<HolderSet<Structure>> getAssociatedStructure() {
        return Optional.ofNullable(this.structures);
    }

    @Override
    public int getDefaultMapColor() {
        return this.mapColor;
    }

    @Override
    public boolean isFromWorld() {
        return this.target != null;
    }

    public ResourceLocation getId() {
        return Utils.getID(this);
    }

    @Nullable
    @Override
    public CustomMapDecoration loadDecorationFromBuffer(FriendlyByteBuf buffer) {
        try {
            return new CustomMapDecoration(this, buffer);
        } catch (Exception var3) {
            Moonlight.LOGGER.warn("Failed to load custom map decoration for decoration type" + this.getId() + ": " + var3);
            return null;
        }
    }

    @Nullable
    public SimpleMapBlockMarker loadMarkerFromNBT(CompoundTag compound) {
        SimpleMapBlockMarker marker = new SimpleMapBlockMarker(this);
        try {
            marker.loadFromNBT(compound);
            return marker;
        } catch (Exception var4) {
            Moonlight.LOGGER.warn("Failed to load world map marker for decoration type" + this.getId() + ": " + var4);
            return null;
        }
    }

    @Nullable
    public SimpleMapBlockMarker getWorldMarkerFromWorld(BlockGetter reader, BlockPos pos) {
        if (this.target != null && this.enabled && this.target.test(reader.getBlockState(pos), RandomSource.create())) {
            SimpleMapBlockMarker m = this.createEmptyMarker();
            m.setPos(pos);
            return m;
        } else {
            return null;
        }
    }

    public SimpleMapBlockMarker createEmptyMarker() {
        SimpleMapBlockMarker m = new SimpleMapBlockMarker(this);
        m.setRotation(this.rotation);
        m.setName(this.name == null ? null : Component.translatable(this.name));
        return m;
    }
}