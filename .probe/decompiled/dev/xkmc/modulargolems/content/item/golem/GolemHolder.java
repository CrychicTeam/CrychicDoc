package dev.xkmc.modulargolems.content.item.golem;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.l2library.util.nbt.ItemListTag;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.capability.GolemConfigStorage;
import dev.xkmc.modulargolems.content.config.GolemMaterial;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class GolemHolder<T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> extends Item {

    public static final String KEY_MATERIAL = "golem_materials";

    public static final String KEY_UPGRADES = "golem_upgrades";

    public static final String KEY_ENTITY = "golem_entity";

    public static final String KEY_DISPLAY = "golem_display";

    public static final String KEY_ICON = "golem_icon";

    public static final String KEY_CONFIG = "golem_config";

    public static final String KEY_PART = "part";

    public static final String KEY_MAT = "material";

    private final RegistryEntry<GolemType<T, P>> type;

    public static ArrayList<GolemMaterial> getMaterial(ItemStack stack) {
        ArrayList<GolemMaterial> ans = new ArrayList();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("golem_materials", 9)) {
            ListTag list = tag.getList("golem_materials", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag elem = list.getCompound(i);
                GolemPart<?, ?> part = (GolemPart<?, ?>) ForgeRegistries.ITEMS.getValue(new ResourceLocation(elem.getString("part")));
                ResourceLocation mat = new ResourceLocation(elem.getString("material"));
                if (part != null) {
                    ans.add(part.parseMaterial(mat));
                }
            }
        }
        return ans;
    }

    public static ArrayList<UpgradeItem> getUpgrades(ItemStack stack) {
        ArrayList<UpgradeItem> ans = new ArrayList();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("golem_upgrades", 9)) {
            ListTag list = tag.getList("golem_upgrades", 8);
            for (int i = 0; i < list.size(); i++) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(list.getString(i)));
                if (item instanceof UpgradeItem up) {
                    ans.add(up);
                }
            }
        }
        return ans;
    }

    public static Optional<Pair<UUID, Integer>> getGolemConfig(ItemStack stack) {
        CompoundTag tag = stack.getTagElement("golem_config");
        return tag != null ? Optional.of(Pair.of(tag.getUUID("id"), tag.getInt("color"))) : Optional.empty();
    }

    public static void setGolemConfig(ItemStack stack, UUID id, int color) {
        CompoundTag tag = ItemCompoundTag.of(stack).getSubTag("golem_config").getOrCreate();
        tag.putUUID("id", id);
        tag.putInt("color", color);
    }

    public static void addMaterial(ItemStack stack, GolemPart<?, ?> item, ResourceLocation material) {
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
        assert rl != null;
        ItemCompoundTag tag = ItemCompoundTag.of(stack);
        CompoundTag elem = tag.getSubList("golem_materials", 10).addCompound().getOrCreate();
        elem.put("part", StringTag.valueOf(rl.toString()));
        elem.put("material", StringTag.valueOf(material.toString()));
    }

    public static ItemStack addUpgrade(ItemStack stack, UpgradeItem item) {
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
        assert rl != null;
        ItemCompoundTag tag = ItemCompoundTag.of(stack);
        tag.getSubList("golem_upgrades", 8).getOrCreate().add(StringTag.valueOf(rl.toString()));
        return stack;
    }

    public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> ItemStack setEntity(T entity) {
        GolemHolder<T, P> holder = GolemType.getGolemHolder(entity.getType());
        ItemStack stack = new ItemStack(holder);
        ItemCompoundTag tag = ItemCompoundTag.of(stack);
        GolemConfigEntry config = entity.getConfigEntry(null);
        if (config != null) {
            setGolemConfig(stack, config.getID(), config.getColor());
        }
        ItemListTag matlist = tag.getSubList("golem_materials", 10);
        for (GolemMaterial mat : entity.getMaterials()) {
            ResourceLocation rl = ForgeRegistries.ITEMS.getKey(mat.part());
            assert rl != null;
            CompoundTag elem = matlist.addCompound().getOrCreate();
            elem.put("part", StringTag.valueOf(rl.toString()));
            elem.put("material", StringTag.valueOf(mat.id().toString()));
        }
        ListTag uplist = tag.getSubList("golem_upgrades", 8).getOrCreate();
        for (Item item : entity.getUpgrades()) {
            ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
            assert rl != null;
            uplist.add(StringTag.valueOf(rl.toString()));
        }
        entity.m_20223_(tag.getSubTag("golem_entity").getOrCreate());
        Component name = entity.m_7770_();
        if (name != null) {
            stack.setHoverName(name);
        }
        return stack;
    }

    public static float getHealth(ItemStack stack) {
        return (Float) Optional.ofNullable(stack.getTag()).filter(e -> e.contains("golem_entity")).map(e -> e.getCompound("golem_entity")).map(e -> e.getFloat("Health")).orElse(-1.0F);
    }

    public static float getMaxHealth(ItemStack stack) {
        return (Float) Optional.ofNullable(stack.getTag()).filter(e -> e.contains("golem_entity")).map(e -> e.getCompound("golem_entity")).flatMap(e -> e.getList("Attributes", 10).stream().map(t -> (CompoundTag) t).filter(t -> t.getString("Name").equals("minecraft:generic.max_health")).findAny()).map(e -> e.getFloat("Base")).orElse(-1.0F);
    }

    public static void setHealth(ItemStack result, float health) {
        result.getOrCreateTag().getCompound("golem_entity").putFloat("Health", health);
    }

    public static ItemStack toEntityIcon(ItemStack golem, ItemStack... equipments) {
        ItemListTag list = ItemCompoundTag.of(golem).getSubList("golem_icon", 10);
        for (ItemStack e : equipments) {
            list.addCompound().setTag(e.serializeNBT());
        }
        return golem;
    }

    public GolemHolder(Item.Properties props, RegistryEntry<GolemType<T, P>> type) {
        super(props.stacksTo(1));
        this.type = type;
        GolemType.GOLEM_TYPE_TO_ITEM.put(type.getId(), this);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        CompoundTag root = stack.getTag();
        if (root != null) {
            if (root.contains("golem_entity") && entity.tickCount % 20 == 0) {
                float health = getHealth(stack);
                float maxHealth = getMaxHealth(stack);
                if (health > 0.0F && health < maxHealth) {
                    ArrayList<GolemMaterial> mats = getMaterial(stack);
                    ArrayList<UpgradeItem> upgrades = getUpgrades(stack);
                    Map<Attribute, Pair<GolemStatType, Double>> attr = GolemMaterial.collectAttributes(mats, upgrades);
                    HashMap<GolemModifier, Integer> modifiers = GolemMaterial.collectModifiers(mats, upgrades);
                    double heal = (Double) ((Pair) attr.getOrDefault(GolemTypes.GOLEM_REGEN.get(), Pair.of((GolemStatType) GolemTypes.STAT_REGEN.get(), 0.0))).getSecond();
                    GolemModifier.HealingContext ctx = new GolemModifier.HealingContext(health, maxHealth, entity);
                    for (Entry<GolemModifier, Integer> entry : modifiers.entrySet()) {
                        heal = ((GolemModifier) entry.getKey()).onInventoryHealTick(heal, ctx, (Integer) entry.getValue());
                    }
                    if (heal > 0.0) {
                        setHealth(stack, Math.min(maxHealth, (float) heal + health));
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if (Screen.hasAltDown()) {
            NBTAnalytic.analyze(stack, list);
        } else {
            if (!Screen.hasShiftDown()) {
                float max = getMaxHealth(stack);
                if (max >= 0.0F) {
                    float health = getHealth(stack);
                    float f = Mth.clamp(health / max, 0.0F, 1.0F);
                    int color = Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
                    MutableComponent hc = Component.literal(Math.round(health) + "").setStyle(Style.EMPTY.withColor(color));
                    list.add(MGLangData.HEALTH.get(hc, Math.round(max)).withStyle(health <= 0.0F ? ChatFormatting.RED : ChatFormatting.AQUA));
                }
                Optional<Pair<UUID, Integer>> config = getGolemConfig(stack);
                if (level != null && !config.isEmpty()) {
                    UUID id = (UUID) ((Pair) config.get()).getFirst();
                    Integer color = (Integer) ((Pair) config.get()).getSecond();
                    GolemConfigEntry entry = GolemConfigStorage.get(level).getOrCreateStorage(id, color, MGLangData.LOADING.get());
                    entry.clientTick(level, false);
                    list.add(entry.getDisplayName());
                } else {
                    list.add(MGLangData.NO_CONFIG.get());
                }
                ArrayList<GolemMaterial> mats = getMaterial(stack);
                ArrayList<UpgradeItem> upgrades = getUpgrades(stack);
                P[] parts = this.getEntityType().values();
                if (mats.size() == parts.length) {
                    for (int i = 0; i < parts.length; i++) {
                        list.add(parts[i].getDesc(((GolemMaterial) mats.get(i)).getDesc()));
                    }
                }
                list.add(MGLangData.SLOT.get(this.getRemaining(mats, upgrades)).withStyle(ChatFormatting.AQUA));
                HashMap<GolemModifier, Integer> modifiers = GolemMaterial.collectModifiers(mats, upgrades);
                if (modifiers.size() > 8) {
                    list.add(MGLangData.UPGRADE_COUNT.get(modifiers.size(), upgrades.size()));
                } else {
                    modifiers.forEach((kx, vx) -> list.add(kx.getTooltip(vx)));
                }
                GolemMaterial.collectAttributes(mats, upgrades).forEach((kx, vx) -> {
                    if (Math.abs((Double) vx.getSecond()) > 0.001) {
                        list.add(((GolemStatType) vx.getFirst()).getTotalTooltip((Double) vx.getSecond()));
                    }
                });
                list.add(MGLangData.SHIFT.get());
            } else {
                ArrayList<GolemMaterial> matsx = getMaterial(stack);
                ArrayList<UpgradeItem> upgradesx = getUpgrades(stack);
                HashMap<GolemModifier, Integer> map = GolemMaterial.collectModifiers(matsx, upgradesx);
                int size = map.size();
                int index = 0;
                for (Entry<GolemModifier, Integer> entry : map.entrySet()) {
                    index++;
                    GolemModifier k = (GolemModifier) entry.getKey();
                    Integer v = (Integer) entry.getValue();
                    list.add(k.getTooltip(v));
                    if (size <= 12 && (size <= 4 || level != null && level.isClientSide() && level.getGameTime() / 30L % (long) size == (long) (index - 1))) {
                        list.addAll(k.getDetail(v));
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        CompoundTag root = stack.getTag();
        if (root == null) {
            return InteractionResult.PASS;
        } else {
            Level level = player.m_9236_();
            Vec3 pos = target.m_20182_();
            if (this.summon(stack, level, pos, player, e -> e.checkRide(target))) {
                if (!level.isClientSide()) {
                    player.m_21008_(hand, ItemStack.EMPTY);
                }
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    public boolean canGrindstoneRepair(ItemStack stack) {
        return true;
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        CompoundTag root = stack.getTag();
        if (root == null) {
            return InteractionResult.PASS;
        } else {
            Level level = context.getLevel();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);
            BlockPos spawnPos;
            if (blockstate.m_60812_(level, blockpos).isEmpty()) {
                spawnPos = blockpos;
            } else {
                spawnPos = blockpos.relative(direction);
            }
            Vec3 pos = new Vec3((double) spawnPos.m_123341_() + 0.5, (double) spawnPos.m_123342_() + 0.05, (double) spawnPos.m_123343_() + 0.5);
            if (this.summon(stack, level, pos, context.getPlayer(), null)) {
                if (context.getPlayer() != null && !context.getLevel().isClientSide()) {
                    context.getPlayer().m_21008_(context.getHand(), ItemStack.EMPTY);
                }
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    public boolean summon(ItemStack stack, Level level, Vec3 pos, @Nullable Player player, @Nullable Consumer<AbstractGolemEntity<?, ?>> callback) {
        CompoundTag root = stack.getTag();
        if (root == null) {
            return false;
        } else if (root.contains("golem_entity") && getMaxHealth(stack) >= 0.0F) {
            if (getHealth(stack) <= 0.0F) {
                return false;
            } else {
                if (!level.isClientSide()) {
                    AbstractGolemEntity<?, ?> golem = ((GolemType) this.type.get()).create((ServerLevel) level, root.getCompound("golem_entity"));
                    UUID id = player == null ? null : player.m_20148_();
                    golem.updateAttributes(getMaterial(stack), getUpgrades(stack), id);
                    golem.m_20219_(pos);
                    getGolemConfig(stack).ifPresent(e -> golem.setConfigCard((UUID) e.getFirst(), (Integer) e.getSecond()));
                    if (stack.hasCustomHoverName()) {
                        golem.m_6593_(stack.getHoverName());
                    }
                    if (!golem.initMode(player)) {
                        return false;
                    }
                    level.m_7967_(golem);
                    stack.removeTagKey("golem_entity");
                    stack.shrink(1);
                    if (callback != null) {
                        callback.accept(golem);
                    }
                }
                return true;
            }
        } else if (!root.contains("golem_materials")) {
            return false;
        } else {
            if (!level.isClientSide()) {
                AbstractGolemEntity<?, ?> golemx = ((GolemType) this.type.get()).create(level);
                golemx.m_20219_(pos);
                UUID idx = player == null ? null : player.m_20148_();
                golemx.onCreate(getMaterial(stack), getUpgrades(stack), idx);
                getGolemConfig(stack).ifPresent(e -> golem.setConfigCard((UUID) e.getFirst(), (Integer) e.getSecond()));
                if (stack.hasCustomHoverName()) {
                    golemx.m_6593_(stack.getHoverName());
                }
                if (!golemx.initMode(player)) {
                    return false;
                }
                level.m_7967_(golemx);
                if (player == null || !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (callback != null) {
                    callback.accept(golemx);
                }
            }
            return true;
        }
    }

    @Nullable
    public T createDummy(ItemStack stack, Level level) {
        CompoundTag root = stack.getTag();
        if (root == null) {
            return null;
        } else {
            T golem;
            if (root.contains("golem_entity")) {
                golem = (T) ((GolemType) this.type.get()).create((ServerLevel) level, root.getCompound("golem_entity"));
                golem.updateAttributes(getMaterial(stack), getUpgrades(stack), null);
            } else {
                if (!root.contains("golem_materials")) {
                    return null;
                }
                golem = (T) ((GolemType) this.type.get()).create(level);
                golem.onCreate(getMaterial(stack), getUpgrades(stack), null);
            }
            getGolemConfig(stack).ifPresent(e -> golem.setConfigCard((UUID) e.getFirst(), (Integer) e.getSecond()));
            if (stack.hasCustomHoverName()) {
                golem.m_6593_(stack.getHoverName());
            }
            return golem;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().contains("golem_display") ? true : getMaxHealth(stack) >= 0.0F;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float f;
        if (stack.getTag() != null && stack.getTag().contains("golem_display")) {
            f = stack.getTag().getFloat("golem_display");
        } else {
            float health = getHealth(stack);
            float maxHealth = getMaxHealth(stack);
            f = Mth.clamp(health / maxHealth, 0.0F, 1.0F);
        }
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains("golem_display")) {
            float f = stack.getTag().getFloat("golem_display");
            return Math.round(f * 13.0F);
        } else {
            return Math.round(Mth.clamp(getHealth(stack) / getMaxHealth(stack), 0.0F, 1.0F) * 13.0F);
        }
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(GolemBEWLR.EXTENSIONS);
    }

    public GolemType<T, P> getEntityType() {
        return (GolemType<T, P>) this.type.get();
    }

    public void fillItemCategory(CreativeModeTabModifier tab) {
        for (ResourceLocation rl : GolemMaterialConfig.get().getAllMaterials()) {
            ItemStack stack = new ItemStack(this);
            for (P part : this.getEntityType().values()) {
                addMaterial(stack, part.toItem(), rl);
            }
            tab.m_246342_(stack);
        }
    }

    public ItemStack withUniformMaterial(ResourceLocation rl) {
        ItemStack stack = new ItemStack(this);
        for (P part : this.getEntityType().values()) {
            addMaterial(stack, part.toItem(), rl);
        }
        return stack;
    }

    public int getRemaining(ArrayList<GolemMaterial> mats, ArrayList<UpgradeItem> upgrades) {
        int base = this.getEntityType().values().length;
        if (this.type.get() == GolemTypes.TYPE_GOLEM.get()) {
            base = MGConfig.COMMON.largeGolemSlot.get();
        } else if (this.type.get() == GolemTypes.TYPE_HUMANOID.get()) {
            base = MGConfig.COMMON.humanoidGolemSlot.get();
        } else if (this.type.get() == GolemTypes.TYPE_DOG.get()) {
            base = MGConfig.COMMON.dogGolemSlot.get();
        }
        base -= upgrades.size();
        HashMap<GolemModifier, Integer> modifiers = GolemMaterial.collectModifiers(mats, upgrades);
        for (Entry<GolemModifier, Integer> ent : modifiers.entrySet()) {
            base += ((GolemModifier) ent.getKey()).addSlot(upgrades, (Integer) ent.getValue());
        }
        return base;
    }

    public void onDestroyed(ItemEntity entity, DamageSource source) {
        if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            for (UpgradeItem e : getUpgrades(entity.getItem())) {
                entity.m_9236_().m_7967_(new ItemEntity(entity.m_9236_(), entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), e.m_7968_()));
            }
        }
    }
}