package com.simibubi.create.foundation.advancement;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Components;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class CreateAdvancement {

    static final ResourceLocation BACKGROUND = Create.asResource("textures/gui/advancements.png");

    static final String LANG = "advancement.create.";

    static final String SECRET_SUFFIX = "\n§7(Hidden Advancement)";

    private Advancement.Builder builder = Advancement.Builder.advancement();

    private SimpleCreateTrigger builtinTrigger;

    private CreateAdvancement parent;

    Advancement datagenResult;

    private String id;

    private String title;

    private String description;

    public CreateAdvancement(String id, UnaryOperator<CreateAdvancement.Builder> b) {
        this.id = id;
        CreateAdvancement.Builder t = new CreateAdvancement.Builder();
        b.apply(t);
        if (!t.externalTrigger) {
            this.builtinTrigger = AllTriggers.addSimple(id + "_builtin");
            this.builder.addCriterion("0", this.builtinTrigger.instance());
        }
        this.builder.display(t.icon, Components.translatable(this.titleKey()), Components.translatable(this.descriptionKey()).withStyle(s -> s.withColor(14393875)), id.equals("root") ? BACKGROUND : null, t.type.frame, t.type.toast, t.type.announce, t.type.hide);
        if (t.type == CreateAdvancement.TaskType.SECRET) {
            this.description = this.description + "\n§7(Hidden Advancement)";
        }
        AllAdvancements.ENTRIES.add(this);
    }

    private String titleKey() {
        return "advancement.create." + this.id;
    }

    private String descriptionKey() {
        return this.titleKey() + ".desc";
    }

    public boolean isAlreadyAwardedTo(Player player) {
        if (player instanceof ServerPlayer sp) {
            Advancement advancement = sp.m_20194_().getAdvancements().getAdvancement(Create.asResource(this.id));
            return advancement == null ? true : sp.getAdvancements().getOrStartProgress(advancement).isDone();
        } else {
            return true;
        }
    }

    public void awardTo(Player player) {
        if (player instanceof ServerPlayer sp) {
            if (this.builtinTrigger == null) {
                throw new UnsupportedOperationException("Advancement " + this.id + " uses external Triggers, it cannot be awarded directly");
            } else {
                this.builtinTrigger.trigger(sp);
            }
        }
    }

    void save(Consumer<Advancement> t) {
        if (this.parent != null) {
            this.builder.parent(this.parent.datagenResult);
        }
        this.datagenResult = this.builder.save(t, Create.asResource(this.id).toString());
    }

    void provideLang(BiConsumer<String, String> consumer) {
        consumer.accept(this.titleKey(), this.title);
        consumer.accept(this.descriptionKey(), this.description);
    }

    class Builder {

        private CreateAdvancement.TaskType type = CreateAdvancement.TaskType.NORMAL;

        private boolean externalTrigger;

        private int keyIndex;

        private ItemStack icon;

        CreateAdvancement.Builder special(CreateAdvancement.TaskType type) {
            this.type = type;
            return this;
        }

        CreateAdvancement.Builder after(CreateAdvancement other) {
            CreateAdvancement.this.parent = other;
            return this;
        }

        CreateAdvancement.Builder icon(ItemProviderEntry<?> item) {
            return this.icon(item.asStack());
        }

        CreateAdvancement.Builder icon(ItemLike item) {
            return this.icon(new ItemStack(item));
        }

        CreateAdvancement.Builder icon(ItemStack stack) {
            this.icon = stack;
            return this;
        }

        CreateAdvancement.Builder title(String title) {
            CreateAdvancement.this.title = title;
            return this;
        }

        CreateAdvancement.Builder description(String description) {
            CreateAdvancement.this.description = description;
            return this;
        }

        CreateAdvancement.Builder whenBlockPlaced(Block block) {
            return this.externalTrigger(ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block));
        }

        CreateAdvancement.Builder whenIconCollected() {
            return this.externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(this.icon.getItem()));
        }

        CreateAdvancement.Builder whenItemCollected(ItemProviderEntry<?> item) {
            return this.whenItemCollected(item.asStack().getItem());
        }

        CreateAdvancement.Builder whenItemCollected(ItemLike itemProvider) {
            return this.externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(itemProvider));
        }

        CreateAdvancement.Builder whenItemCollected(TagKey<Item> tag) {
            return this.externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems(new ItemPredicate(tag, null, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY)));
        }

        CreateAdvancement.Builder awardedForFree() {
            return this.externalTrigger(InventoryChangeTrigger.TriggerInstance.hasItems());
        }

        CreateAdvancement.Builder externalTrigger(CriterionTriggerInstance trigger) {
            CreateAdvancement.this.builder.addCriterion(String.valueOf(this.keyIndex), trigger);
            this.externalTrigger = true;
            this.keyIndex++;
            return this;
        }
    }

    static enum TaskType {

        SILENT(FrameType.TASK, false, false, false), NORMAL(FrameType.TASK, true, false, false), NOISY(FrameType.TASK, true, true, false), EXPERT(FrameType.GOAL, true, true, false), SECRET(FrameType.GOAL, true, true, true);

        private FrameType frame;

        private boolean toast;

        private boolean announce;

        private boolean hide;

        private TaskType(FrameType frame, boolean toast, boolean announce, boolean hide) {
            this.frame = frame;
            this.toast = toast;
            this.announce = announce;
            this.hide = hide;
        }
    }
}