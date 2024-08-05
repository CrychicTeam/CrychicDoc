package com.mna.items.sorcery;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.items.inventory.ISpellBookInventory;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.gui.containers.providers.NamedRoteBook;
import com.mna.items.renderers.books.RoteBookRenderer;
import com.mna.spells.crafting.SpellRecipe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;

public class ItemBookOfRote extends ItemSpellBook {

    public ItemBookOfRote() {
        super(false);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new RoteBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedRoteBook();
    }

    @Override
    public ISpellBookInventory getInventory(ItemStack item, IPlayerMagic magic) {
        return magic == null ? null : magic.getRoteInventory();
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
                ItemStack selectedStack = magic.getRoteInventory().m_8020_(getActiveSpellSlot(stack));
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
            ItemStack held = player.m_21120_(hand);
            IPlayerRoteSpells rote = (IPlayerRoteSpells) player.getCapability(PlayerRoteSpellsProvider.ROTE).orElse(null);
            IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (rote == null || magic == null) {
                return InteractionResultHolder.fail(held);
            }
            ItemStack bookStack = player.m_21120_(hand);
            ItemStack selectedStack = this.getSelectedStack(bookStack, player);
            SpellRecipe recipe = SpellRecipe.fromNBT(selectedStack.getTag());
            if (recipe.isValid() && !magic.isModifierPressed()) {
                if (!player.isCreative()) {
                    ArrayList<Attribute> modified_attrs = new ArrayList();
                    if (!rote.isRote(recipe.getShape().getPart())) {
                        String shapeName = Component.translatable(recipe.getShape().getPart().getRegistryName().toString()).getString();
                        player.m_213846_(Component.translatable("item.mna.book_of_rote.part_not_rote", shapeName));
                        return InteractionResultHolder.fail(held);
                    }
                    modified_attrs.addAll((Collection) recipe.getShape().getContainedAttributes().stream().filter(a -> recipe.getShape().getValueWithoutMultipliers(a) != recipe.getShape().getDefaultValue(a)).collect(Collectors.toList()));
                    for (IModifiedSpellPart<SpellEffect> component : recipe.getComponents()) {
                        if (!rote.isRote(component.getPart())) {
                            String componentName = Component.translatable(component.getPart().getRegistryName().toString()).getString();
                            player.m_213846_(Component.translatable("item.mna.book_of_rote.part_not_rote", componentName));
                            return InteractionResultHolder.fail(held);
                        }
                        modified_attrs.addAll((Collection) component.getContainedAttributes().stream().filter(a -> component.getValueWithoutMultipliers(a) != component.getDefaultValue(a)).collect(Collectors.toList()));
                    }
                    for (int m = 0; m < modified_attrs.size(); m++) {
                        boolean modifier_rote = false;
                        Attribute attr = (Attribute) modified_attrs.get(m);
                        for (int i = 0; i < rote.getRoteModifiers().size(); i++) {
                            Modifier mod = (Modifier) rote.getRoteModifiers().get(i);
                            if (mod != null && mod.modifiesType(attr)) {
                                modifier_rote = true;
                                break;
                            }
                        }
                        if (!modifier_rote) {
                            player.m_213846_(Component.translatable("item.mna.spell.modifier_not_rote", attr));
                            return InteractionResultHolder.fail(held);
                        }
                    }
                }
                CompoundTag bookTag = bookStack.getOrCreateTag();
                bookTag.putInt("cur_spell_use_duration", recipe.getMaxChannelTime());
                bookStack.setTag(bookTag);
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public ItemStack getSelectedStack(ItemStack bookStack, @Nullable Player player) {
        if (bookStack.getItem() instanceof ItemSpellBook) {
            SimpleContainer bookInv = (SimpleContainer) this.getInventory(bookStack, player != null ? (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null) : null);
            if (bookInv != null) {
                return bookInv.getItem(getActiveSpellSlot(bookStack));
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 11278103;
    }
}