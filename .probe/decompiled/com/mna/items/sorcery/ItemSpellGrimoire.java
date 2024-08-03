package com.mna.items.sorcery;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.faction.FactionIDs;
import com.mna.api.items.inventory.ISpellBookInventory;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.gui.containers.providers.NamedGrimoire;
import com.mna.items.renderers.books.CouncilGrimoireBookRenderer;
import com.mna.items.renderers.books.DemonGrimoireBookRenderer;
import com.mna.items.renderers.books.FeyGrimoireBookRenderer;
import com.mna.items.renderers.books.GrimoireBookRenderer;
import com.mna.items.renderers.books.UndeadGrimoireBookRenderer;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;

public class ItemSpellGrimoire extends ItemSpellBook {

    private final boolean showSubtitle;

    private final ResourceLocation forFaction;

    public ItemSpellGrimoire() {
        this(null, false);
    }

    public ItemSpellGrimoire(ResourceLocation faction, boolean renderBook) {
        super(renderBook);
        this.tier = faction == null ? 3 : 4;
        this.showSubtitle = faction != null;
        this.forFaction = faction;
    }

    public ItemSpellGrimoire(Item.Properties properties, @Nullable ResourceLocation faction, @Nullable ResourceLocation openModel, @Nullable ResourceLocation closedModel, boolean renderInFirstPerson) {
        super(properties, openModel, closedModel, renderInFirstPerson);
        this.tier = faction == null ? 3 : 4;
        this.showSubtitle = faction != null;
        this.forFaction = faction;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> {
                if (ItemSpellGrimoire.this.forFaction == FactionIDs.COUNCIL) {
                    return new CouncilGrimoireBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
                } else if (ItemSpellGrimoire.this.forFaction == FactionIDs.DEMONS) {
                    return new DemonGrimoireBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
                } else if (ItemSpellGrimoire.this.forFaction == FactionIDs.FEY) {
                    return new FeyGrimoireBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
                } else {
                    return (BlockEntityWithoutLevelRenderer) (ItemSpellGrimoire.this.forFaction == FactionIDs.UNDEAD ? new UndeadGrimoireBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()) : new GrimoireBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));
                }
            });

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedGrimoire();
    }

    @Override
    public ISpellBookInventory getInventory(ItemStack item, IPlayerMagic magic) {
        return magic == null ? null : magic.getGrimoireInventory();
    }

    @Override
    public CompoundTag getSpellCompound(ItemStack stack, Player player) {
        if (player == null) {
            return new CompoundTag();
        } else {
            IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic == null) {
                return new CompoundTag();
            } else {
                ItemStack selectedStack = magic.getGrimoireInventory().m_8020_(getActiveSpellSlot(stack));
                return selectedStack.getTag();
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains("cur_spell_use_duration") ? tag.getInt("cur_spell_use_duration") : 9999;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            ItemStack bookStack = player.m_21120_(hand);
            ItemStack selectedStack = this.getSelectedStack(bookStack, player);
            SpellRecipe recipe = SpellRecipe.fromNBT(selectedStack.getTag());
            if (recipe.isValid()) {
                CompoundTag bookTag = bookStack.getOrCreateTag();
                bookTag.putInt("cur_spell_use_duration", recipe.getMaxChannelTime());
                bookStack.setTag(bookTag);
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    protected ItemStack getSelectedStack(ItemStack bookStack, @Nullable Player player) {
        if (bookStack.getItem() instanceof ItemSpellBook) {
            SimpleContainer bookInv = (SimpleContainer) this.getInventory(bookStack, player != null ? (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null) : null);
            if (bookInv != null) {
                return bookInv.getItem(getActiveSpellSlot(bookStack));
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.showSubtitle) {
            tooltip.add(Component.translatable(stack.getDescriptionId() + ".subtitle"));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 5841080;
    }
}