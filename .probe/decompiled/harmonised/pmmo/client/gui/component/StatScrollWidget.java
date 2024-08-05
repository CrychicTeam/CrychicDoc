package harmonised.pmmo.client.gui.component;

import com.mojang.blaze3d.vertex.Tesselator;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.client.gui.GlossarySelectScreen;
import harmonised.pmmo.client.utils.ClientUtils;
import harmonised.pmmo.client.utils.DP;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.PerksConfig;
import harmonised.pmmo.config.codecs.CodecTypes;
import harmonised.pmmo.config.codecs.DataSource;
import harmonised.pmmo.config.codecs.EnhancementsData;
import harmonised.pmmo.config.codecs.LocationData;
import harmonised.pmmo.config.codecs.PlayerData;
import harmonised.pmmo.config.codecs.VeinData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.setup.datagen.LangProvider;
import harmonised.pmmo.util.RegistryUtil;
import harmonised.pmmo.util.TagUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ForgeRegistries;

public class StatScrollWidget extends ScrollPanel {

    Minecraft mc = Minecraft.getInstance();

    Core core = Core.get(LogicalSide.CLIENT);

    private final List<StatScrollWidget.Element> content = new ArrayList();

    private Supplier<List<ItemStack>> itemSupplier = () -> CreativeModeTabs.searchTab().getDisplayItems().stream().toList();

    private static final String PREDICATE_KEY = "usesPredicate";

    private StatScrollWidget(int width, int height, int top, int left) {
        super(Minecraft.getInstance(), width, height, top, left, 4);
        CreativeModeTabs.tryRebuildTabContents(this.mc.player.connection.enabledFeatures(), this.mc.player.m_36337_(), this.mc.player.f_108545_.m_9598_());
    }

    public StatScrollWidget(int width, int height, int top, int left, int pointless) {
        this(width, height, top, left);
        this.populateLocation(List.of(this.mc.level.m_46472_().location()), new ReqType[] { ReqType.TRAVEL }, new ModifierDataType[] { ModifierDataType.DIMENSION }, "", false, true, true);
        this.populateLocation(List.of(((ResourceKey) this.mc.level.m_204166_(this.mc.player.m_20183_()).unwrapKey().get()).location()), new ReqType[] { ReqType.TRAVEL }, new ModifierDataType[] { ModifierDataType.BIOME }, "", true, true, true);
    }

    public StatScrollWidget(int width, int height, int top, int left, ItemStack stack) {
        this(width, height, top, left);
        EventType[] events = stack.getItem() instanceof BlockItem ? EventType.BLOCKITEM_APPLICABLE_EVENTS : EventType.ITEM_APPLICABLE_EVENTS;
        ReqType[] reqs = stack.getItem() instanceof BlockItem ? ReqType.BLOCKITEM_APPLICABLE_EVENTS : ReqType.ITEM_APPLICABLE_EVENTS;
        this.populateItems(List.of(stack), events, reqs, ModifierDataType.values(), "", true, true);
    }

    public StatScrollWidget(int width, int height, int top, int left, Entity entity) {
        this(width, height, top, left);
        this.populateEntity(List.of(entity), EventType.ENTITY_APPLICABLE_EVENTS, ReqType.ENTITY_APPLICABLE_EVENTS, entity instanceof Player, "");
    }

    public StatScrollWidget(int width, int height, int top, int left, BlockPos pos) {
        this(width, height, top, left);
        this.populateBlockFromWorld(pos, EventType.BLOCK_APPLICABLE_EVENTS, ReqType.BLOCK_APPLICABLE_EVENTS);
    }

    public StatScrollWidget(int width, int height, int top, int left, GlossarySelectScreen.SELECTION selection, GlossarySelectScreen.OBJECT object, String skill, GuiEnumGroup type) {
        this(width, height, top, left);
        this.generateGlossary(selection, object, skill, type);
    }

    private int step(int level) {
        return level * 10;
    }

    public void generateGlossary(GlossarySelectScreen.SELECTION selection, GlossarySelectScreen.OBJECT object, String skill, GuiEnumGroup type) {
        switch(selection) {
            case REQS:
                EventType[] events = new EventType[0];
                ModifierDataType[] bonuses = new ModifierDataType[0];
                switch(object) {
                    case ITEMS:
                        this.populateItems((List<ItemStack>) this.itemSupplier.get(), events, type == null ? ReqType.ITEM_APPLICABLE_EVENTS : new ReqType[] { (ReqType) type }, bonuses, skill, false, false);
                        return;
                    case BLOCKS:
                        this.populateBlocks(ForgeRegistries.BLOCKS.getValues(), events, type == null ? ReqType.BLOCK_APPLICABLE_EVENTS : new ReqType[] { (ReqType) type }, false, skill);
                        return;
                    case ENTITY:
                        this.populateEntity(ForgeRegistries.ENTITY_TYPES.getValues().stream().map(entityType -> entityType.create(this.mc.level)).filter(entity -> entity != null).toList(), events, type == null ? ReqType.ENTITY_APPLICABLE_EVENTS : new ReqType[] { (ReqType) type }, false, skill);
                        return;
                    case DIMENSIONS:
                        this.populateLocation(this.mc.player.connection.levels().stream().map(key -> key.location()).toList(), new ReqType[] { ReqType.TRAVEL }, bonuses, skill, false, false, false);
                        return;
                    case BIOMES:
                        this.populateLocation(this.mc.player.m_9236_().registryAccess().registryOrThrow(Registries.BIOME).keySet().stream().toList(), new ReqType[] { ReqType.TRAVEL }, bonuses, skill, true, false, false);
                        return;
                    case ENCHANTS:
                        this.populateEnchants(ForgeRegistries.ENCHANTMENTS.getValues().stream().map(ench -> RegistryUtil.getId(ench)).toList(), skill);
                        return;
                    default:
                        return;
                }
            case XP:
                ReqType[] reqs = new ReqType[0];
                ModifierDataType[] bonuses = new ModifierDataType[0];
                switch(object) {
                    case ITEMS:
                        this.populateItems((List<ItemStack>) this.itemSupplier.get(), type == null ? EventType.ITEM_APPLICABLE_EVENTS : new EventType[] { (EventType) type }, reqs, bonuses, skill, false, false);
                        return;
                    case BLOCKS:
                        this.populateBlocks(ForgeRegistries.BLOCKS.getValues(), type == null ? EventType.BLOCK_APPLICABLE_EVENTS : new EventType[] { (EventType) type }, reqs, false, skill);
                        return;
                    case ENTITY:
                        this.populateEntity(ForgeRegistries.ENTITY_TYPES.getValues().stream().map(entityType -> entityType.create(Minecraft.getInstance().level)).filter(entity -> entity != null).toList(), type == null ? EventType.ENTITY_APPLICABLE_EVENTS : new EventType[] { (EventType) type }, reqs, false, skill);
                        return;
                    case DIMENSIONS:
                    case BIOMES:
                    case ENCHANTS:
                    default:
                        return;
                    case EFFECTS:
                        this.populateEffects(ForgeRegistries.MOB_EFFECTS.getValues(), new EventType[] { EventType.EFFECT }, reqs, skill);
                        return;
                }
            case BONUS:
                ReqType[] reqs = new ReqType[0];
                EventType[] events = new EventType[0];
                switch(object) {
                    case ITEMS:
                        this.populateItems((List<ItemStack>) this.itemSupplier.get(), events, reqs, type == null ? ModifierDataType.values() : new ModifierDataType[] { (ModifierDataType) type }, skill, false, false);
                        return;
                    case BLOCKS:
                    case ENTITY:
                    default:
                        return;
                    case DIMENSIONS:
                        this.populateLocation(this.mc.player.connection.levels().stream().map(key -> key.location()).toList(), reqs, type == null ? ModifierDataType.values() : new ModifierDataType[] { (ModifierDataType) type }, skill, false, false, false);
                        return;
                    case BIOMES:
                        this.populateLocation(this.mc.player.m_9236_().registryAccess().registryOrThrow(Registries.BIOME).keySet().stream().toList(), reqs, type == null ? ModifierDataType.values() : new ModifierDataType[] { (ModifierDataType) type }, skill, true, false, false);
                        return;
                }
            case SALVAGE:
                if (object == GlossarySelectScreen.OBJECT.ITEMS) {
                    this.populateItems((List<ItemStack>) this.itemSupplier.get(), new EventType[0], new ReqType[0], new ModifierDataType[0], skill, true, false);
                }
                break;
            case VEIN:
                ReqType[] reqs = new ReqType[0];
                EventType[] events = new EventType[0];
                ModifierDataType[] bonuses = new ModifierDataType[0];
                switch(object) {
                    case ITEMS:
                        this.populateItems((List<ItemStack>) this.itemSupplier.get(), events, reqs, bonuses, skill, false, true);
                        return;
                    case BLOCKS:
                        this.populateBlocks(ForgeRegistries.BLOCKS.getValues(), events, reqs, true, skill);
                        return;
                    case ENTITY:
                    default:
                        return;
                    case DIMENSIONS:
                        this.populateLocation(this.mc.player.connection.levels().stream().map(key -> key.location()).toList(), reqs, bonuses, skill, false, true, false);
                        return;
                    case BIOMES:
                        this.populateLocation(this.mc.player.m_9236_().registryAccess().registryOrThrow(Registries.BIOME).keySet().stream().toList(), reqs, bonuses, skill, true, true, false);
                        return;
                }
            case MOB_SCALING:
                ReqType[] reqs = new ReqType[0];
                ModifierDataType[] bonuses = new ModifierDataType[0];
                switch(object) {
                    case DIMENSIONS:
                        this.populateLocation(this.mc.player.connection.levels().stream().map(key -> key.location()).toList(), reqs, bonuses, skill, false, false, true);
                        return;
                    case BIOMES:
                        this.populateLocation(this.mc.player.m_9236_().registryAccess().registryOrThrow(Registries.BIOME).keySet().stream().toList(), reqs, bonuses, skill, true, false, true);
                        return;
                    default:
                        return;
                }
            case PERKS:
                this.populatePerks();
        }
    }

    private void populateItems(List<ItemStack> items, EventType[] events, ReqType[] reqs, ModifierDataType[] modifiers, String skillFilter, boolean includeSalvage, boolean includeVein) {
        for (ItemStack stack : items) {
            int lengthBeforeProcessing = this.content.size() + 1;
            if (items.size() > 1) {
                this.content.add(new StatScrollWidget.RenderableElement(stack.getDisplayName(), 1, stack.getRarity().color.getColor(), Config.SECTION_HEADER_COLOR.get(), stack));
            }
            this.addEventSection(event -> {
                Map<String, Long> map = this.core.getExperienceAwards(event, stack, this.mc.player, new CompoundTag());
                if (stack.getItem() instanceof BlockItem) {
                    map = this.core.getCommonXpAwardData(new HashMap(), event, RegistryUtil.getId(stack), this.mc.player, ObjectType.BLOCK, TagUtils.stackTag(stack));
                }
                return map;
            }, events, skillFilter);
            this.addReqSection(reqType -> {
                Map<String, Integer> reqMap = this.core.getReqMap(reqType, stack, true);
                if (reqType == ReqType.USE_ENCHANTMENT) {
                    this.core.getEnchantReqs(stack).forEach((skill, level) -> reqMap.merge(skill, level, (o, n) -> o > n ? o : n));
                }
                if (stack.getItem() instanceof BlockItem) {
                    reqMap.putAll(this.core.getCommonReqData(new HashMap(), ObjectType.BLOCK, RegistryUtil.getId(stack), reqType, TagUtils.stackTag(stack)));
                }
                return reqMap;
            }, CoreUtils.getEffects(this.core.getLoader().getLoader(ObjectType.ITEM).getData(RegistryUtil.getId(stack)).getNegativeEffect(), true), reqs, skillFilter);
            this.addModifierSection(mod -> this.core.getTooltipRegistry().bonusTooltipExists(RegistryUtil.getId(stack), mod) ? this.core.getTooltipRegistry().getBonusTooltipData(RegistryUtil.getId(stack), mod, stack) : this.core.getObjectModifierMap(ObjectType.ITEM, RegistryUtil.getId(stack), mod, TagUtils.stackTag(stack)), modifiers, skillFilter);
            if (includeSalvage) {
                this.addSalvageSection(this.core.getLoader().ITEM_LOADER.getData(RegistryUtil.getId(stack)).salvage());
            }
            if (includeVein) {
                this.addItemVeinSection(this.core.getLoader().ITEM_LOADER.getData(RegistryUtil.getId(stack)).veinData(), stack.getItem() instanceof BlockItem);
            }
            if (lengthBeforeProcessing == this.content.size()) {
                this.content.remove(this.content.size() - 1);
            }
        }
    }

    private void populateBlockFromWorld(BlockPos block, EventType[] events, ReqType[] reqs) {
        this.addEventSection(event -> this.core.getExperienceAwards(event, block, Minecraft.getInstance().level, null, new CompoundTag()), events, "");
        this.addReqSection(reqType -> this.core.getReqMap(reqType, block, Minecraft.getInstance().level), new ArrayList(), reqs, "");
        this.addBlockVeinSection(this.core.getLoader().BLOCK_LOADER.getData(RegistryUtil.getId(Minecraft.getInstance().level.m_8055_(block))).veinData());
    }

    private void populateBlocks(Collection<Block> blocks, EventType[] events, ReqType[] reqs, boolean includeVein, String skillFilter) {
        for (Block block : blocks) {
            int lengthBeforeProcessing = this.content.size() + 1;
            ItemStack stack = new ItemStack(block.asItem());
            ResourceLocation id = RegistryUtil.getId(block);
            this.content.add(new StatScrollWidget.RenderableElement(stack.getDisplayName(), 1, stack.getRarity().color.getColor(), Config.SECTION_HEADER_COLOR.get(), block));
            this.addEventSection(event -> this.core.getTooltipRegistry().xpGainTooltipExists(id, event) ? Collections.singletonMap("usesPredicate", 0L) : this.core.getObjectExperienceMap(ObjectType.BLOCK, id, event, new CompoundTag()), events, skillFilter);
            this.addReqSection(reqType -> this.core.getPredicateRegistry().predicateExists(id, reqType) ? Collections.singletonMap("usesPredicate", 0) : this.core.getObjectSkillMap(ObjectType.BLOCK, id, reqType, new CompoundTag()), new ArrayList(), reqs, skillFilter);
            if (includeVein) {
                this.addBlockVeinSection(this.core.getLoader().BLOCK_LOADER.getData(RegistryUtil.getId(block)).veinData());
            }
            if (lengthBeforeProcessing == this.content.size()) {
                this.content.remove(this.content.size() - 1);
            }
        }
    }

    private void populateEntity(List<? extends Entity> entities, EventType[] events, ReqType[] reqs, boolean isPlayer, String skillFilter) {
        for (Entity entity : entities) {
            int lengthBeforeProcessing = this.content.size() + 1;
            if (entities.size() > 1) {
                this.content.add(new StatScrollWidget.RenderableElement(entity.getDisplayName(), 1, 15658734, Config.SECTION_HEADER_COLOR.get(), entity));
            }
            this.addEventSection(event -> this.core.getExperienceAwards(event, entity, null, new CompoundTag()), events, skillFilter);
            this.addReqSection(reqType -> this.core.getReqMap(reqType, entity), new ArrayList(), reqs, skillFilter);
            if (isPlayer) {
                this.addPlayerSection(entity);
            }
            if (lengthBeforeProcessing == this.content.size()) {
                this.content.remove(this.content.size() - 1);
            }
        }
    }

    private void populateEffects(Collection<MobEffect> effects, EventType[] events, ReqType[] reqs, String skillFilter) {
        for (MobEffect effect : effects) {
            int lengthBeforeProcessing = this.content.size() + 1;
            if (effects.size() > 1) {
                this.content.addAll(StatScrollWidget.TextElement.build(effect.getDisplayName(), this.width, 1, 15658734, true, Config.SECTION_HEADER_COLOR.get()));
            }
            List<StatScrollWidget.TextElement> holder = new ArrayList();
            for (int lvl = 0; lvl <= this.getEffectHighestConfiguration(effect); lvl++) {
                Map<String, Long> xpMap = this.core.getExperienceAwards(new MobEffectInstance(effect, 30, lvl), null, new CompoundTag());
                if (!xpMap.isEmpty() && !xpMap.entrySet().stream().allMatch(entry -> (Long) entry.getValue() == 0L)) {
                    holder.addAll(StatScrollWidget.TextElement.build(Component.literal(String.valueOf(lvl)), this.width, 1, 16777215, false, 0));
                    for (Entry<String, Long> map : xpMap.entrySet()) {
                        if ((Long) map.getValue() != 0L) {
                            holder.addAll(StatScrollWidget.TextElement.build((String) map.getKey(), (Long) map.getValue(), this.width, this.step(1), CoreUtils.getSkillColor((String) map.getKey())));
                        }
                    }
                }
            }
            if (holder.size() > 0) {
                this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.EVENT_HEADER.asComponent().withStyle(ChatFormatting.BOLD), this.width, 1, 15658734, true, Config.SECTION_HEADER_COLOR.get()));
                this.content.addAll(holder);
            }
            if (lengthBeforeProcessing == this.content.size()) {
                this.content.remove(this.content.size() - 1);
            }
        }
    }

    private int getEffectHighestConfiguration(MobEffect effect) {
        DataSource<?> data = (DataSource<?>) this.core.getLoader().getLoader(ObjectType.EFFECT).getData().get(RegistryUtil.getId(effect));
        return data == null ? 0 : (Integer) ((EnhancementsData) data).skillArray().keySet().stream().max(Comparator.naturalOrder()).orElse(-1);
    }

    private void populateLocation(List<ResourceLocation> locations, ReqType[] reqs, ModifierDataType[] modifiers, String skillFilter, boolean isBiome, boolean includeVein, boolean includeScaling) {
        locations.forEach(loc -> {
            int lengthBeforeProcessing = this.content.size() + 1;
            if (locations.size() > 1) {
                this.content.addAll(StatScrollWidget.TextElement.build(Component.literal(loc.toString()).withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD), this.width, 1, 15658734, true, Config.SECTION_HEADER_COLOR.get()));
            }
            this.addReqSection(reqType -> this.core.getObjectSkillMap(isBiome ? ObjectType.BIOME : ObjectType.DIMENSION, loc, reqType, new CompoundTag()), (List<MobEffectInstance>) (isBiome ? CoreUtils.getEffects(this.core.getLoader().getLoader(ObjectType.BIOME).getData(loc).getNegativeEffect(), true) : new ArrayList()), reqs, skillFilter);
            if (reqs.length > 0 && isBiome) {
                this.addReqEffectSection(CoreUtils.getEffects(isBiome ? this.core.getLoader().getLoader(ObjectType.BIOME).getData(loc).getPositiveEffect() : this.core.getLoader().getLoader(ObjectType.DIMENSION).getData(loc).getPositiveEffect(), false), false);
            }
            this.addModifierSection(mod -> this.core.getObjectModifierMap(isBiome ? ObjectType.BIOME : ObjectType.DIMENSION, loc, mod, new CompoundTag()), modifiers, skillFilter);
            if (includeVein) {
                this.addVeinBlacklistSection(isBiome ? ObjectType.BIOME : ObjectType.DIMENSION, loc);
            }
            if (includeScaling) {
                this.addMobModifierSection(isBiome ? ObjectType.BIOME : ObjectType.DIMENSION, loc);
            }
            if (lengthBeforeProcessing == this.content.size()) {
                this.content.remove(this.content.size() - 1);
            }
        });
    }

    private void populateEnchants(List<ResourceLocation> enchants, String skillFilter) {
        enchants.forEach(ench -> {
            int lengthBeforeProcessing = this.content.size() + 1;
            if (enchants.size() > 1) {
                this.content.addAll(StatScrollWidget.TextElement.build(Component.literal(ench.toString()).withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD), this.width, 1, 15658734, true, Config.SECTION_HEADER_COLOR.get()));
            }
            List<StatScrollWidget.TextElement> holder = new ArrayList();
            for (int i = 0; i <= ForgeRegistries.ENCHANTMENTS.getValue(ench).getMaxLevel(); i++) {
                Map<String, Integer> reqMap = (Map<String, Integer>) this.core.getEnchantmentReqs(ench, i).entrySet().stream().filter(entry -> ((String) entry.getKey()).contains(skillFilter)).collect(Collectors.toMap(e -> (String) e.getKey(), e -> (Integer) e.getValue()));
                if (!reqMap.isEmpty() && !reqMap.entrySet().stream().allMatch(entry -> (Integer) entry.getValue() == 0)) {
                    holder.addAll(StatScrollWidget.TextElement.build(Component.literal(String.valueOf(i)), this.width, 1, 16777215, false, 0));
                    for (Entry<String, Integer> map : reqMap.entrySet()) {
                        if ((Integer) map.getValue() != 0) {
                            holder.addAll(StatScrollWidget.TextElement.build((String) map.getKey(), (Integer) map.getValue(), this.width, this.step(1), CoreUtils.getSkillColor((String) map.getKey())));
                        }
                    }
                }
            }
            if (holder.size() > 0) {
                this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.REQ_HEADER.asComponent().withStyle(ChatFormatting.BOLD), this.width, 1, 15658734, true, Config.SECTION_HEADER_COLOR.get()));
                this.content.addAll(holder);
            }
            if (lengthBeforeProcessing == this.content.size()) {
                this.content.remove(this.content.size() - 1);
            }
        });
    }

    private void populatePerks() {
        for (EventType cause : EventType.values()) {
            List<StatScrollWidget.TextElement> holder = new ArrayList();
            Player player = Minecraft.getInstance().player;
            ((List) PerksConfig.PERK_SETTINGS.get().getOrDefault(cause, new ArrayList())).forEach(nbt -> {
                ResourceLocation perkID = new ResourceLocation(nbt.getString("perk"));
                nbt.putInt("level", nbt.contains("skill") ? Core.get(player.m_9236_()).getData().getPlayerSkillLevel(nbt.getString("skill"), player.m_20148_()) : 0);
                holder.addAll(StatScrollWidget.TextElement.build(Component.translatable("perk." + perkID.getNamespace() + "." + perkID.getPath()), this.width, this.step(1), 65280, false, 65280));
                holder.addAll(StatScrollWidget.TextElement.build(this.core.getPerkRegistry().getDescription(perkID).m_6881_(), this.width, this.step(1), 10079487, false, 10079487));
                for (MutableComponent line : this.core.getPerkRegistry().getStatusLines(perkID, player, nbt)) {
                    holder.addAll(StatScrollWidget.TextElement.build(line, this.width, this.step(2), 11206655, false, 11206655));
                }
            });
            if (holder.size() > 0) {
                this.content.addAll(StatScrollWidget.TextElement.build(cause, this.width, 1, 15658734, true, Config.SECTION_HEADER_COLOR.get()));
                this.content.addAll(holder);
            }
        }
    }

    private void addEventSection(Function<EventType, Map<String, Long>> xpSrc, EventType[] events, String skillFilter) {
        if (events.length > 0) {
            List<StatScrollWidget.TextElement> holder = new ArrayList();
            for (EventType event : events) {
                Map<String, Long> xpAwards = (Map<String, Long>) CoreUtils.processSkillGroupXP((Map<String, Long>) xpSrc.apply(event)).entrySet().stream().filter(entry -> ((String) entry.getKey()).contains(skillFilter)).collect(Collectors.toMap(e -> (String) e.getKey(), e -> (Long) e.getValue()));
                if (xpAwards.containsKey("usesPredicate")) {
                    holder.addAll(StatScrollWidget.TextElement.build(LangProvider.ADDON_AFFECTED_ATTRIBUTE.asComponent(), this.width, 5, 16751619, false, 0));
                } else if (!xpAwards.isEmpty()) {
                    holder.addAll(StatScrollWidget.TextElement.build(event, this.width, 1, 16777215, false, 0));
                    for (Entry<String, Long> map : xpAwards.entrySet()) {
                        holder.addAll(StatScrollWidget.TextElement.build((String) map.getKey(), (Long) map.getValue(), this.width, 5, CoreUtils.getSkillColor((String) map.getKey())));
                    }
                }
            }
            if (holder.size() > 0) {
                this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.EVENT_HEADER.asComponent().withStyle(ChatFormatting.BOLD), this.width, 1, 15658734, true, Config.SECTION_HEADER_COLOR.get()));
                this.content.addAll(holder);
            }
        }
    }

    private void addReqSection(Function<ReqType, Map<String, Integer>> reqSrc, List<MobEffectInstance> reqEffects, ReqType[] reqs, String skillFilter) {
        if (reqs.length > 0) {
            List<StatScrollWidget.TextElement> holder = new ArrayList();
            for (ReqType reqType : reqs) {
                Map<String, Integer> reqMap = (Map<String, Integer>) CoreUtils.processSkillGroupReqs((Map<String, Integer>) reqSrc.apply(reqType)).entrySet().stream().filter(entry -> ((String) entry.getKey()).contains(skillFilter)).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
                if (!reqMap.isEmpty() && !reqMap.entrySet().stream().allMatch(entry -> (Integer) entry.getValue() == 0)) {
                    holder.addAll(StatScrollWidget.TextElement.build(reqType, this.width, 1, 16777215, false, 0));
                    for (Entry<String, Integer> map : reqMap.entrySet()) {
                        if ((Integer) map.getValue() != 0) {
                            holder.addAll(StatScrollWidget.TextElement.build((String) map.getKey(), (Integer) map.getValue(), this.width, this.step(1), CoreUtils.getSkillColor((String) map.getKey())));
                        }
                    }
                }
            }
            if (!holder.isEmpty()) {
                this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.REQ_HEADER.asComponent().withStyle(ChatFormatting.BOLD), this.width, 1, 15658734, true, Config.SECTION_HEADER_COLOR.get()));
                this.content.addAll(holder);
                this.addReqEffectSection(reqEffects, true);
            }
        }
    }

    private void addReqEffectSection(List<MobEffectInstance> reqEffects, boolean isNegative) {
        if (reqEffects.size() > 0) {
            this.content.addAll(StatScrollWidget.TextElement.build(isNegative ? LangProvider.REQ_EFFECTS_HEADER.asComponent() : LangProvider.BIOME_EFFECT_POS.asComponent(), this.width, 1, 16777215, true, Config.SECTION_HEADER_COLOR.get()));
            for (MobEffectInstance mei : reqEffects) {
                this.content.addAll(StatScrollWidget.TextElement.build(mei.getEffect().getDisplayName(), this.width, this.step(1), 16777215, false, 0));
            }
        }
    }

    private void addModifierSection(Function<ModifierDataType, Map<String, Double>> bonusSrc, ModifierDataType[] mods, String skillFilter) {
        if (mods.length > 0) {
            List<StatScrollWidget.TextElement> holder = new ArrayList();
            for (ModifierDataType mod : mods) {
                Map<String, Double> modifiers = (Map<String, Double>) ((Map) bonusSrc.apply(mod)).entrySet().stream().filter(entry -> ((String) entry.getKey()).contains(skillFilter)).collect(Collectors.toMap(e -> (String) e.getKey(), e -> (Double) e.getValue()));
                if (!modifiers.isEmpty()) {
                    this.content.addAll(StatScrollWidget.TextElement.build(mod, this.width, 1, 16777215, false, 0));
                    modifiers.forEach((key, value) -> this.content.addAll(StatScrollWidget.TextElement.build(key, value, this.width, this.step(1), CoreUtils.getSkillColor(key))));
                }
            }
            if (holder.size() > 0) {
                this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.MODIFIER_HEADER.asComponent().withStyle(ChatFormatting.BOLD), this.width, 1, 15658734, true, Config.SECTION_HEADER_COLOR.get()));
                this.content.addAll(holder);
            }
        }
    }

    private void addSalvageSection(Map<ResourceLocation, CodecTypes.SalvageData> salvage) {
        if (!salvage.isEmpty()) {
            this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.SALVAGE_HEADER.asComponent().withStyle(ChatFormatting.BOLD), this.width, 1, 16777215, true, Config.SECTION_HEADER_COLOR.get()));
            for (Entry<ResourceLocation, CodecTypes.SalvageData> salvageEntry : salvage.entrySet()) {
                CodecTypes.SalvageData data = (CodecTypes.SalvageData) salvageEntry.getValue();
                ItemStack resultStack = new ItemStack(ForgeRegistries.ITEMS.getValue((ResourceLocation) salvageEntry.getKey()));
                this.content.addAll(StatScrollWidget.TextElement.build(resultStack.getDisplayName(), this.width, this.step(1), 16777215, true, Config.SALVAGE_ITEM_COLOR.get()));
                if (!data.levelReq().isEmpty()) {
                    this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.SALVAGE_LEVEL_REQ.asComponent().withStyle(ChatFormatting.UNDERLINE), this.width, this.step(1), 16777215, false, 0));
                    for (Entry<String, Integer> req : data.levelReq().entrySet()) {
                        this.content.addAll(StatScrollWidget.TextElement.build((String) req.getKey(), (Integer) req.getValue(), this.width, this.step(2), CoreUtils.getSkillColor((String) req.getKey())));
                    }
                }
                this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.SALVAGE_CHANCE.asComponent(data.baseChance(), data.maxChance()).withStyle(ChatFormatting.UNDERLINE), this.width, this.step(1), 16777215, false, 0));
                this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.SALVAGE_MAX.asComponent(data.salvageMax()).withStyle(ChatFormatting.UNDERLINE), this.width, this.step(1), 16777215, false, 0));
                if (!data.chancePerLevel().isEmpty()) {
                    this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.SALVAGE_CHANCE_MOD.asComponent().withStyle(ChatFormatting.UNDERLINE), this.width, this.step(1), 16777215, false, 0));
                    for (Entry<String, Double> perLevel : data.chancePerLevel().entrySet()) {
                        this.content.addAll(StatScrollWidget.TextElement.build((String) perLevel.getKey(), (Double) perLevel.getValue(), this.width, this.step(2), CoreUtils.getSkillColor((String) perLevel.getKey())));
                    }
                }
                if (!data.xpAward().isEmpty()) {
                    this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.SALVAGE_XP_AWARD.asComponent().withStyle(ChatFormatting.UNDERLINE), this.width, this.step(1), 16777215, false, 0));
                    for (Entry<String, Long> award : data.xpAward().entrySet()) {
                        this.content.addAll(StatScrollWidget.TextElement.build((String) award.getKey(), (Long) award.getValue(), this.width, this.step(2), CoreUtils.getSkillColor((String) award.getKey())));
                    }
                }
            }
        }
    }

    private void addItemVeinSection(VeinData veinData, boolean isBlockItem) {
        if (!veinData.equals(VeinData.EMPTY)) {
            this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.VEIN_HEADER.asComponent().withStyle(ChatFormatting.BOLD), this.width, 1, 16777215, true, Config.SECTION_HEADER_COLOR.get()));
            this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.VEIN_RATE.asComponent((Double) veinData.chargeRate.orElse(0.0) * 2.0), this.width, this.step(1), 16777215, false, 0));
            this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.VEIN_CAP.asComponent(veinData.chargeCap.orElse(0)), this.width, this.step(1), 16777215, false, 0));
            if (isBlockItem) {
                this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.VEIN_CONSUME.asComponent(veinData.consumeAmount.orElse(0)), this.width, this.step(1), 16777215, false, 0));
            }
        }
    }

    private void addBlockVeinSection(VeinData veinData) {
        if (veinData.consumeAmount != VeinData.EMPTY.consumeAmount) {
            this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.VEIN_HEADER.asComponent().withStyle(ChatFormatting.BOLD), this.width, 1, 16777215, true, Config.SECTION_HEADER_COLOR.get()));
            this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.VEIN_CONSUME.asComponent(veinData.consumeAmount.orElse(0)), this.width, this.step(1), 16777215, false, 0));
        }
    }

    private void addPlayerSection(Entity entity) {
        this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.PLAYER_HEADER.asComponent(), this.width, this.step(1), 16777215, true, Config.SECTION_HEADER_COLOR.get()));
        PlayerData data = this.core.getLoader().PLAYER_LOADER.getData(new ResourceLocation(entity.getUUID().toString()));
        this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.PLAYER_IGNORE_REQ.asComponent(data.ignoreReq()), this.width, this.step(2), 16777215, false, 0));
        if (!data.bonuses().isEmpty()) {
            this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.PLAYER_BONUSES.asComponent(), this.width, this.step(2), 16777215, true, Config.SALVAGE_ITEM_COLOR.get()));
            for (Entry<String, Double> bonus : data.bonuses().entrySet()) {
                this.content.addAll(StatScrollWidget.TextElement.build((String) bonus.getKey(), (Double) bonus.getValue(), this.width, this.step(3), CoreUtils.getSkillColor((String) bonus.getKey())));
            }
        }
        Map<String, Long> rawXp = this.core.getData().getXpMap(entity.getUUID());
        LinkedHashMap<String, Integer> orderedMap = new LinkedHashMap();
        List<String> skillKeys = new ArrayList(rawXp.keySet().stream().toList());
        skillKeys.sort(Comparator.comparingLong(a -> (Long) rawXp.get(a)).reversed());
        skillKeys.forEach(skill -> orderedMap.put(skill, this.core.getData().getLevelFromXP((Long) rawXp.get(skill))));
        this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.SKILL_LIST_HEADER.asComponent(), this.step(1), this.width, 16777215, true, Config.SECTION_HEADER_COLOR.get()));
        for (Entry<String, Integer> rawMap : orderedMap.entrySet()) {
            this.content.addAll(StatScrollWidget.TextElement.build((String) rawMap.getKey(), (Integer) rawMap.getValue(), this.width, this.step(2), CoreUtils.getSkillColor((String) rawMap.getKey())));
        }
    }

    private void addVeinBlacklistSection(ObjectType type, ResourceLocation location) {
        LocationData loader = (LocationData) this.core.getLoader().getLoader(type).getData(location);
        if (!loader.veinBlacklist().isEmpty()) {
            this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.VEIN_BLACKLIST_HEADER.asComponent().withStyle(ChatFormatting.BOLD), this.width, this.step(1), 16777215, false, 0));
            for (ResourceLocation blockID : loader.veinBlacklist()) {
                this.content.addAll(StatScrollWidget.TextElement.build(Component.literal(blockID.toString()), this.width, this.step(2), 15658734, false, 0));
            }
        }
    }

    private void addMobModifierSection(ObjectType type, ResourceLocation location) {
        if (type == ObjectType.BIOME || type == ObjectType.DIMENSION) {
            LocationData loader = (LocationData) this.core.getLoader().getLoader(type).getData(location);
            if (!loader.mobModifiers().isEmpty()) {
                this.content.addAll(StatScrollWidget.TextElement.build(LangProvider.MOB_MODIFIER_HEADER.asComponent().withStyle(ChatFormatting.BOLD), this.width, this.step(1), 16777215, false, 0));
                for (Entry<ResourceLocation, Map<String, Double>> mobMap : loader.mobModifiers().entrySet()) {
                    Entity entity = ForgeRegistries.ENTITY_TYPES.getValue((ResourceLocation) mobMap.getKey()).create(this.mc.level);
                    this.content.add(new StatScrollWidget.RenderableElement(entity.getName(), this.step(1), 16777215, Config.SALVAGE_ITEM_COLOR.get(), entity));
                    for (Entry<String, Double> map : ((Map) mobMap.getValue()).entrySet()) {
                        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation((String) map.getKey()));
                        MutableComponent text = attribute == null ? Component.literal((String) map.getKey()) : Component.translatable(attribute.getDescriptionId());
                        text.append(Component.literal(": " + map.getValue()));
                        this.content.addAll(StatScrollWidget.TextElement.build(text, this.width, this.step(2), 16777215, false, 16777215));
                    }
                }
            }
        }
    }

    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput0) {
    }

    @Override
    protected int getContentHeight() {
        return this.content.size() * 12;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        return super.mouseScrolled(mouseX, mouseY, scroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int partialTicks) {
        return super.mouseClicked(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
        for (int i = 0; i < this.content.size(); i++) {
            ((StatScrollWidget.Element) this.content.get(i)).render(guiGraphics, this.left, (int) ((float) (relativeY + i * 12) - this.scrollDistance), this.width, tess);
        }
    }

    private interface Element {

        void render(GuiGraphics var1, int var2, int var3, int var4, Tesselator var5);
    }

    private static record RenderableElement(Component text, int xOffset, int color, int headerColor, ItemStack stack, Block block, Entity entity) implements StatScrollWidget.Element {

        RenderableElement(Component text, int xOffset, int color, int headerColor, ItemStack stack) {
            this(text, xOffset, color, headerColor, stack, null, null);
        }

        RenderableElement(Component text, int xOffset, int color, int headerColor, Block block) {
            this(text, xOffset, color, headerColor, null, block, null);
        }

        RenderableElement(Component text, int xOffset, int color, int headerColor, Entity entity) {
            this(text, xOffset, color, headerColor, null, null, entity);
        }

        @Override
        public void render(GuiGraphics graphics, int x, int y, int width, Tesselator tess) {
            graphics.fill(RenderType.gui(), x, y, x + width, y + 12, this.headerColor());
            Font font = Minecraft.getInstance().font;
            if (this.stack() == null && this.block() == null) {
                if (this.entity != null && this.entity instanceof LivingEntity) {
                    int scale = Math.max(1, 10 / Math.max(1, (int) this.entity.getBoundingBox().getSize()));
                    InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, x + width - 20, y + 12, scale, 0.0F, 0.0F, (LivingEntity) this.entity);
                    graphics.drawString(font, this.entity.getDisplayName(), x, y, 16777215);
                }
            } else {
                ItemStack renderStack = this.stack() == null ? new ItemStack(this.block().asItem()) : this.stack();
                graphics.renderItem(renderStack, x + width - 25, y);
                graphics.drawString(font, renderStack.getDisplayName(), x + 10, y, 16777215);
            }
        }
    }

    private static record TextElement(ClientTooltipComponent text, int xOffset, int color, boolean isHeader, int headerColor) implements StatScrollWidget.Element {

        public static List<StatScrollWidget.TextElement> build(Component component, int width, int xOffset, int color, boolean isHeader, int headerColor) {
            return format(component.copy(), width, xOffset, color, isHeader, headerColor);
        }

        public static List<StatScrollWidget.TextElement> build(String key, int value, int width, int xOffset, int color) {
            return format(Component.translatable("pmmo." + key).append(Component.literal(": " + value)), width, xOffset, color, false, 0);
        }

        public static List<StatScrollWidget.TextElement> build(String key, long value, int width, int xOffset, int color) {
            return format(Component.translatable("pmmo." + key).append(Component.literal(": " + value)), width, xOffset, color, false, 0);
        }

        public static List<StatScrollWidget.TextElement> build(String key, double value, int width, int xOffset, int color) {
            return format(Component.translatable("pmmo." + key).append(Component.literal(": " + DP.dp(value * 100.0) + "%")), width, xOffset, color, false, 0);
        }

        public static List<StatScrollWidget.TextElement> build(Enum<?> type, int width, int xOffset, int color, boolean isHeader, int headerColor) {
            return format(Component.translatable("pmmo.enum." + type.name()), width - xOffset, xOffset, color, isHeader, headerColor);
        }

        @Override
        public void render(GuiGraphics graphics, int x, int y, int width, Tesselator tess) {
            if (this.isHeader()) {
                graphics.fill(RenderType.gui(), x, y, x + width, y + 12, this.headerColor());
            }
            MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(tess.getBuilder());
            this.text().renderText(Minecraft.getInstance().font, x + this.xOffset(), y, graphics.pose().last().pose(), buffer);
            buffer.endBatch();
        }

        private static List<StatScrollWidget.TextElement> format(MutableComponent component, int width, int xOffset, int color, boolean isHeader, int headerColor) {
            return ClientUtils.ctc(component.withStyle(component.getStyle().withColor(color)), width).stream().map(line -> new StatScrollWidget.TextElement(line, xOffset, color, isHeader, headerColor)).toList();
        }
    }
}