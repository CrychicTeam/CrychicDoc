package com.mna.cantrips;

import com.mna.api.cantrips.ICantrip;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.util.TriConsumer;

public class Cantrip implements ICantrip {

    public static final int NUM_PATTERNS = 3;

    private ArrayList<ResourceLocation> default_combination;

    private TriConsumer<Player, ICantrip, InteractionHand> effector;

    private ResourceLocation id;

    private ResourceLocation icon;

    private ResourceLocation requiredAdvancement;

    private ItemStack default_stack = ItemStack.EMPTY;

    private Item valid_dynamic_item = null;

    private boolean stackLocked = true;

    private int delay = 0;

    private SoundEvent matchSound;

    private int tier = 1;

    public Cantrip(ResourceLocation id, TriConsumer<Player, ICantrip, InteractionHand> effector, ItemStack defaultStack, ResourceLocation... defaultCombination) {
        this(id, effector, defaultStack, Arrays.asList(defaultCombination));
    }

    public Cantrip(ResourceLocation id, TriConsumer<Player, ICantrip, InteractionHand> effector, ItemStack defaultStack, List<ResourceLocation> defaultCombination) {
        this.default_combination = new ArrayList();
        this.default_combination.addAll(defaultCombination);
        this.effector = effector;
        this.id = id;
        this.default_stack = defaultStack;
    }

    @Override
    public ResourceLocation getRequiredAdvancement() {
        return this.requiredAdvancement;
    }

    @Override
    public ICantrip setRequiredAdvancement(ResourceLocation advancementIdentifier) {
        this.requiredAdvancement = advancementIdentifier;
        return this;
    }

    public void callEffector(Player player, InteractionHand hand) {
        if (this.effector != null) {
            this.effector.accept(player, this, hand);
        }
    }

    public Cantrip dynamicItem(Item item) {
        this.stackLocked = false;
        this.valid_dynamic_item = item;
        return this;
    }

    public Cantrip setDelay(int ticks) {
        this.delay = ticks;
        return this;
    }

    public Cantrip setSound(SoundEvent sound) {
        this.matchSound = sound;
        return this;
    }

    public Cantrip setTier(int tier) {
        this.tier = tier;
        return this;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public int getDelay() {
        return this.delay;
    }

    @Override
    public SoundEvent getSound() {
        return this.matchSound;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public ResourceLocation getIcon() {
        return this.icon;
    }

    @Override
    public ICantrip setIcon(ResourceLocation icon) {
        this.icon = icon;
        return this;
    }

    @Override
    public boolean isStackLocked() {
        return this.stackLocked;
    }

    @Override
    public List<ResourceLocation> getDefaultCombination() {
        return this.default_combination;
    }

    @Override
    public ItemStack getDefaultStack() {
        return this.default_stack;
    }

    @Override
    public Item getValidDynamicItem() {
        return this.valid_dynamic_item;
    }
}