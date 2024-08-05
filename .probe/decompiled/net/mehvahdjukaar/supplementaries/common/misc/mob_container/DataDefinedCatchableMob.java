package net.mehvahdjukaar.supplementaries.common.misc.mob_container;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.misc.StrOpt;
import net.mehvahdjukaar.supplementaries.api.CapturedMobInstance;
import net.mehvahdjukaar.supplementaries.api.ICatchableMob;
import net.mehvahdjukaar.supplementaries.common.items.JarItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public final class DataDefinedCatchableMob implements ICatchableMob {

    public static final Codec<DataDefinedCatchableMob> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.listOf().fieldOf("owners").forGetter(p -> p.owners), Codec.FLOAT.fieldOf("width_increment").forGetter(p -> p.widthIncrement), Codec.FLOAT.fieldOf("height_increment").forGetter(p -> p.heightIncrement), StrOpt.of(Codec.intRange(0, 15), "light_level", 0).forGetter(p -> p.lightLevel), StrOpt.of(DataDefinedCatchableMob.CaptureSettings.CODEC, "allowed_in").forGetter(p -> p.captureSettings), StrOpt.of(Codec.INT, "fish_index", 0).forGetter(p -> p.fishIndex), StrOpt.of(BuiltinAnimation.Type.CODEC, "animation", BuiltinAnimation.Type.NONE).forGetter(b -> b.builtinAnimation), StrOpt.of(DataDefinedCatchableMob.TickMode.CODEC, "tick_mode", DataDefinedCatchableMob.TickMode.NONE).forGetter(p -> p.tickMode), StrOpt.of(ResourceLocation.CODEC, "force_fluid").forGetter(p -> p.forceFluidID), StrOpt.of(DataDefinedCatchableMob.LootParam.CODEC, "loot").forGetter(p -> p.loot)).apply(instance, DataDefinedCatchableMob::new));

    private final List<ResourceLocation> owners;

    final float widthIncrement;

    final float heightIncrement;

    final int lightLevel;

    final Optional<DataDefinedCatchableMob.CaptureSettings> captureSettings;

    final int fishIndex;

    final BuiltinAnimation.Type builtinAnimation;

    final DataDefinedCatchableMob.TickMode tickMode;

    final Optional<ResourceLocation> forceFluidID;

    final Optional<DataDefinedCatchableMob.LootParam> loot;

    private Optional<Holder<SoftFluid>> forceFluid = null;

    public DataDefinedCatchableMob(List<ResourceLocation> owners, float widthIncrement, float heightIncrement, int lightLevel, Optional<DataDefinedCatchableMob.CaptureSettings> captureSettings, int fishIndex, BuiltinAnimation.Type builtinAnimation, DataDefinedCatchableMob.TickMode tickMode, Optional<ResourceLocation> forceFluidID, Optional<DataDefinedCatchableMob.LootParam> loot) {
        this.widthIncrement = widthIncrement;
        this.heightIncrement = heightIncrement;
        this.lightLevel = lightLevel;
        this.captureSettings = captureSettings;
        this.fishIndex = fishIndex;
        this.builtinAnimation = builtinAnimation;
        this.forceFluidID = forceFluidID;
        this.loot = loot;
        this.tickMode = tickMode;
        this.owners = owners;
    }

    List<ResourceLocation> getOwners() {
        return this.owners;
    }

    @Override
    public <T extends Entity> CapturedMobInstance<T> createCapturedMobInstance(T self, float containerWidth, float containerHeight) {
        return new DataCapturedMobInstance<>(self, containerWidth, containerHeight, this);
    }

    @Override
    public boolean canBeCaughtWithItem(Entity entity, Item item, Player player) {
        return (Boolean) this.captureSettings.map(settings -> settings.canCapture(entity, item)).orElseGet(() -> ICatchableMob.super.canBeCaughtWithItem(entity, item, player));
    }

    @Override
    public int getLightLevel(Level world, BlockPos pos) {
        return this.lightLevel;
    }

    @Override
    public boolean shouldHover(Entity self, boolean waterlogged) {
        BuiltinAnimation.Type cat = this.builtinAnimation;
        return cat.isLand() ? false : cat.isFlying() || ICatchableMob.super.shouldHover(self, waterlogged);
    }

    @Override
    public Optional<Holder<SoftFluid>> shouldRenderWithFluid() {
        if (this.forceFluid == null) {
            this.forceFluid = this.forceFluidID.flatMap(SoftFluidRegistry::getOptionalHolder);
        }
        return this.forceFluid;
    }

    @Override
    public int getFishTextureIndex() {
        return this.fishIndex;
    }

    @Override
    public float getHitBoxWidthIncrement(Entity entity) {
        return this.widthIncrement;
    }

    @Override
    public float getHitBoxHeightIncrement(Entity entity) {
        return this.heightIncrement;
    }

    protected static record CaptureSettings(DataDefinedCatchableMob.CatchMode jarMode, DataDefinedCatchableMob.CatchMode cageMode) {

        private static final Codec<DataDefinedCatchableMob.CaptureSettings> CODEC = RecordCodecBuilder.create(instance -> instance.group(DataDefinedCatchableMob.CatchMode.CODEC.fieldOf("jar").forGetter(DataDefinedCatchableMob.CaptureSettings::jarMode), DataDefinedCatchableMob.CatchMode.CODEC.fieldOf("cage").forGetter(DataDefinedCatchableMob.CaptureSettings::cageMode)).apply(instance, DataDefinedCatchableMob.CaptureSettings::new));

        public boolean canCapture(Entity entity, Item item) {
            return item instanceof JarItem ? this.jarMode.on && (!this.jarMode.onlyBaby || !(entity instanceof LivingEntity lex) || lex.isBaby()) : this.cageMode.on && (!this.cageMode.onlyBaby || !(entity instanceof LivingEntity le) || le.isBaby());
        }
    }

    protected static record CatchMode(boolean on, boolean onlyBaby) {

        private static final Codec<DataDefinedCatchableMob.CatchMode> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.BOOL.fieldOf("allow").forGetter(DataDefinedCatchableMob.CatchMode::on), StrOpt.of(Codec.BOOL, "only_baby", false).forGetter(DataDefinedCatchableMob.CatchMode::onlyBaby)).apply(instance, DataDefinedCatchableMob.CatchMode::new));
    }

    protected static record LootParam(ResourceLocation tableId, float chance) {

        private static final Codec<DataDefinedCatchableMob.LootParam> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("loot_table").forGetter(DataDefinedCatchableMob.LootParam::tableId), Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(DataDefinedCatchableMob.LootParam::chance)).apply(instance, DataDefinedCatchableMob.LootParam::new));

        public void tryDropping(ServerLevel serverLevel, BlockPos pos, Entity entity) {
            if (serverLevel.f_46441_.nextFloat() < this.chance) {
                LootTable lootTable = serverLevel.getServer().getLootData().m_278676_(this.tableId);
                LootParams.Builder builder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.BLOCK_STATE, serverLevel.m_8055_(pos)).withOptionalParameter(LootContextParams.THIS_ENTITY, entity);
                ObjectArrayList<ItemStack> l = lootTable.getRandomItems(builder.create(LootContextParamSets.GIFT));
                ObjectListIterator var7 = l.iterator();
                while (var7.hasNext()) {
                    ItemStack o = (ItemStack) var7.next();
                    entity.spawnAtLocation(o);
                }
            }
        }
    }

    static enum TickMode implements StringRepresentable {

        NONE, SERVER, CLIENT, BOTH;

        public static final Codec<DataDefinedCatchableMob.TickMode> CODEC = StringRepresentable.fromEnum(DataDefinedCatchableMob.TickMode::values);

        boolean isValid(Level level) {
            return switch(this) {
                case NONE ->
                    false;
                case CLIENT ->
                    level.isClientSide;
                case SERVER ->
                    !level.isClientSide;
                case BOTH ->
                    true;
            };
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}