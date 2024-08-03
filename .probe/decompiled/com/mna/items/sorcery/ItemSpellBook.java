package com.mna.items.sorcery;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.items.ITieredItem;
import com.mna.api.items.inventory.ISpellBookInventory;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.gui.containers.providers.NamedSpellBook;
import com.mna.inventory.InventorySpellBook;
import com.mna.items.ItemInit;
import com.mna.items.base.IRadialMenuItem;
import com.mna.items.renderers.books.SpellBookRenderer;
import com.mna.network.ClientMessageDispatcher;
import com.mna.network.messages.to_server.SpellBookSlotChangeMessage;
import com.mna.spells.crafting.SpellRecipe;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.items.IItemHandler;

public class ItemSpellBook extends ItemSpell implements IRadialMenuItem, ITieredItem<ItemSpellBook>, DyeableLeatherItem {

    public static final String KEY_ACTIVE_SLOT = "active_slot";

    protected static final String KEY_USE_DURATION = "cur_spell_use_duration";

    public static String NBT_ID = "mna:spell_book_data";

    public static final int MAX_ACTIVE_SLOT = 8;

    protected int tier = -1;

    public final boolean renderBookModel;

    public final boolean useMnABookModel;

    private final boolean renderInFirstPerson;

    private final ResourceLocation open_model;

    private final ResourceLocation closed_model;

    public ItemSpellBook(boolean renderBookModel) {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
        this.renderBookModel = renderBookModel;
        this.useMnABookModel = true;
        this.open_model = null;
        this.closed_model = null;
        this.renderInFirstPerson = false;
    }

    public ItemSpellBook(Item.Properties properties, @Nullable ResourceLocation openModel, @Nullable ResourceLocation closedModel, boolean renderInFirstPerson) {
        super(properties);
        this.renderBookModel = this.useMnABookModel = openModel != null && closedModel != null;
        this.open_model = openModel;
        this.closed_model = closedModel;
        this.renderInFirstPerson = renderInFirstPerson;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> ItemSpellBook.this.useMnABookModel ? new SpellBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()) : new SpellBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), ItemSpellBook.this.open_model, ItemSpellBook.this.closed_model, ItemSpellBook.this.renderInFirstPerson));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack book = player.m_21120_(hand);
        player.m_6674_(hand);
        return !world.isClientSide && (hand == InteractionHand.OFF_HAND || !this.openGuiIfModifierPressed(book, player, world)) && book.getItem() instanceof ItemSpellBook ? this.castCurrentSpell(book, player, world, hand) : InteractionResultHolder.success(player.m_21120_(hand));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return context.getPlayer().m_6144_() ? this.castCurrentSpell(stack, context.getPlayer(), context.getLevel(), context.getHand()).getResult() : InteractionResult.PASS;
    }

    private InteractionResultHolder<ItemStack> castCurrentSpell(ItemStack book, Player player, Level world, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(book);
        } else {
            ISpellBookInventory bookInv = this.getInventory(book, (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null));
            ItemStack selectedStack = book.getItem() != ItemInit.GRIMOIRE.get() && !(book.getItem() instanceof ItemBookOfRote) ? bookInv.getActiveSpells()[getActiveSpellSlot(book)] : bookInv.getActiveSpells()[getActiveSpellSlot(book)];
            if (!selectedStack.isEmpty()) {
                InteractionResultHolder<ItemStack> result = ItemSpell.castSpellOnUse(selectedStack, world, player, hand, s -> true);
                switch(result.getResult()) {
                    case CONSUME:
                    case CONSUME_PARTIAL:
                        return InteractionResultHolder.consume(book);
                    case FAIL:
                        return InteractionResultHolder.fail(book);
                    case PASS:
                        return InteractionResultHolder.pass(book);
                    case SUCCESS:
                        return InteractionResultHolder.success(book);
                }
            }
            return InteractionResultHolder.fail(book);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        InventorySpellBook bookInv = new InventorySpellBook(stack);
        ItemStack selectedStack = bookInv.getStackInSlot(getActiveSpellSlot(stack) * 5);
        return super.getUseDuration(selectedStack);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity player, ItemStack stack, int count) {
        if (player instanceof Player) {
            ItemStack selectedStack = this.getSelectedStack(stack, (Player) player);
            super.onUseTick(pLevel, player, selectedStack, count);
        }
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (nbt != null && nbt.contains(NBT_ID)) {
            stack.setTag(nbt);
        }
        return null;
    }

    public static int getActiveSpellSlot(ItemStack item) {
        return item.hasTag() ? item.getTag().getInt("active_slot") : 0;
    }

    @Nullable
    public ISpellBookInventory getInventory(ItemStack item, @Nullable IPlayerMagic magic) {
        return new InventorySpellBook(item);
    }

    public static void setSlot(Player player, ItemStack stack, int slot, boolean offhand, boolean packet) {
        if (stack.getItem() instanceof ItemSpellBook) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("active_slot", slot);
            CompoundTag spellCompound = ((ItemSpellBook) stack.getItem()).getSpellCompound(stack, player);
            tag.putInt("cur_spell_use_duration", SpellRecipe.fromNBT(spellCompound).getMaxChannelTime());
            if (packet) {
                ClientMessageDispatcher.sendSpellBookSlotChange(slot, offhand);
            } else {
                ISpellBookInventory inv = ((ItemSpellBook) stack.getItem()).getInventory(stack, (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null));
                if (inv != null) {
                    ItemStack spell = inv.getActiveSpells()[slot % inv.getActiveSpells().length];
                    if (!spell.isEmpty()) {
                        stack.setHoverName(spell.getHoverName());
                    } else {
                        stack.resetHoverName();
                    }
                }
            }
        }
    }

    public static void handleSlotChangeMessage(SpellBookSlotChangeMessage msg, ServerPlayer player) {
        ItemStack stack = msg.isOffhand() ? player.m_21206_() : player.m_21205_();
        if (stack.getItem() instanceof ItemSpellBook) {
            setSlot(player, stack, msg.getSlot(), msg.isOffhand(), false);
        }
    }

    @Override
    public CompoundTag getSpellCompound(ItemStack stack, @Nullable Player player) {
        if (stack.getItem() instanceof ItemSpellBook) {
            IItemHandler bookInv = (IItemHandler) this.getInventory(stack, null);
            ItemStack selectedStack = bookInv.getStackInSlot(getActiveSpellSlot(stack) * 5);
            return selectedStack.getTag();
        } else {
            return new CompoundTag();
        }
    }

    protected ItemStack getSelectedStack(ItemStack bookStack, @Nullable Player player) {
        if (bookStack.getItem() instanceof ItemSpellBook) {
            IItemHandler bookInv = (IItemHandler) this.getInventory(bookStack, null);
            return bookInv.getStackInSlot(getActiveSpellSlot(bookStack) * 5);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedSpellBook(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.m_7373_(stack, worldIn, tooltip, flagIn);
        IRadialMenuItem.super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean canAcceptSpell(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canAcceptSpell(ItemStack stack, ISpellDefinition spell) {
        return false;
    }

    @Override
    public int getCachedTier() {
        return this.tier;
    }

    @Override
    public void setCachedTier(int tier) {
        this.tier = tier;
    }
}