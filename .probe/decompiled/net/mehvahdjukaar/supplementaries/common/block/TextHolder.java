package net.mehvahdjukaar.supplementaries.common.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.mehvahdjukaar.moonlight.api.client.util.TextUtil;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.api.IAntiqueTextProvider;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class TextHolder implements IAntiqueTextProvider {

    private static final Int2ObjectArrayMap<Codec<Component[]>> CODEC_CACHE = new Int2ObjectArrayMap();

    private final int lines;

    private final int maxWidth;

    private final Component[] messages;

    private final Component[] filteredMessages;

    private final FormattedCharSequence[] renderMessages;

    private DyeColor color = DyeColor.BLACK;

    private boolean renderMessagedFiltered;

    private boolean hasGlowingText = false;

    private boolean hasAntiqueInk = false;

    private static Codec<Component[]> compCodec(int size) {
        return (Codec<Component[]>) CODEC_CACHE.computeIfAbsent(size, s -> ExtraCodecs.FLAT_COMPONENT.listOf().comapFlatMap(list -> Util.fixedSize(list, s).map(l -> (Component[]) l.toArray(Component[]::new)), components -> Arrays.stream(components).toList()));
    }

    public TextHolder(int size, int maxWidth) {
        this.lines = size;
        this.maxWidth = maxWidth;
        this.renderMessages = new FormattedCharSequence[size];
        this.messages = new Component[size];
        this.filteredMessages = new Component[size];
        Arrays.fill(this.messages, CommonComponents.EMPTY);
        Arrays.fill(this.filteredMessages, CommonComponents.EMPTY);
    }

    public int getMaxLineCharacters() {
        return (int) ((float) this.getMaxLineVisualWidth() / 6.0F);
    }

    public int getMaxLineVisualWidth() {
        return this.maxWidth;
    }

    public void load(CompoundTag compound, Level level, BlockPos pos) {
        if (compound.contains("TextHolder")) {
            CompoundTag com = compound.getCompound("TextHolder");
            this.color = DyeColor.byName(com.getString("color"), DyeColor.BLACK);
            this.hasGlowingText = com.getBoolean("has_glowing_text");
            this.hasAntiqueInk = com.getBoolean("has_antique_ink");
            if (this.lines != 0) {
                try {
                    Component[] v = this.decodeMessage(com.get("message"), level, pos);
                    System.arraycopy(v, 0, this.messages, 0, v.length);
                    Tag filtered = com.get("filtered_message");
                    if (filtered != null) {
                        v = this.decodeMessage(filtered, level, pos);
                        System.arraycopy(v, 0, this.filteredMessages, 0, v.length);
                    } else {
                        System.arraycopy(this.messages, 0, this.filteredMessages, 0, this.messages.length);
                    }
                    for (int j = 0; j < this.renderMessages.length; j++) {
                        this.renderMessages[j] = null;
                    }
                } catch (Exception var8) {
                    Supplementaries.LOGGER.error("Failed to load textholder data for block at {}", pos);
                }
            }
        }
    }

    private Component[] decodeMessage(Tag com, Level level, BlockPos pos) {
        return (Component[]) Arrays.stream((Component[]) ((Pair) compCodec(this.lines).decode(NbtOps.INSTANCE, com).getOrThrow(false, s -> {
        })).getFirst()).map(c -> {
            if (level instanceof ServerLevel sl) {
                try {
                    return ComponentUtils.updateForEntity(SignBlockEntity.createCommandSourceStack(null, sl, pos), c, null, 0);
                } catch (CommandSyntaxException var5) {
                }
            }
            return c;
        }).toArray(Component[]::new);
    }

    public CompoundTag save(CompoundTag compound) {
        CompoundTag com = new CompoundTag();
        com.putString("color", this.color.getName());
        com.putBoolean("has_glowing_text", this.hasGlowingText);
        com.putBoolean("has_antique_ink", this.hasAntiqueInk);
        if (this.lines != 0) {
            com.put("message", (Tag) compCodec(this.lines).encodeStart(NbtOps.INSTANCE, this.messages).getOrThrow(false, s -> {
            }));
            if (this.hasFilteredMessage()) {
                com.put("filtered_message", (Tag) compCodec(this.lines).encodeStart(NbtOps.INSTANCE, this.filteredMessages).getOrThrow(false, s -> {
                }));
            }
        }
        compound.put("TextHolder", com);
        return compound;
    }

    private boolean hasFilteredMessage() {
        for (int i = 0; i < this.filteredMessages.length; i++) {
            Component component = this.filteredMessages[i];
            if (!component.equals(this.messages[i])) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return this.lines;
    }

    public Component getMessage(int line, boolean filtered) {
        if (line >= this.lines) {
            throw new IndexOutOfBoundsException("Tried to access lie " + line + " of Text Holder of size " + this.lines);
        } else {
            return this.getMessages(filtered)[line];
        }
    }

    public Component[] getMessages(boolean filtered) {
        return filtered ? this.filteredMessages : this.messages;
    }

    public void setMessage(int i, Component component) {
        this.setMessage(i, component, component);
    }

    public void setMessage(int i, Component message, Component filtered) {
        if (this.hasAntiqueInk) {
            MutableComponent t = message.copy();
            message = t.setStyle(message.getStyle().withFont(ModTextures.ANTIQUABLE_FONT));
            t = filtered.copy();
            filtered = t.setStyle(filtered.getStyle().withFont(ModTextures.ANTIQUABLE_FONT));
        }
        this.messages[i] = message;
        this.filteredMessages[i] = filtered;
        this.renderMessages[i] = null;
    }

    public DyeColor getColor() {
        return this.color;
    }

    public boolean setColor(DyeColor newColor) {
        if (newColor != this.color) {
            this.color = newColor;
            return true;
        } else {
            return false;
        }
    }

    public boolean hasGlowingText() {
        return this.hasGlowingText;
    }

    public void setHasGlowingText(boolean glowing) {
        this.hasGlowingText = glowing;
    }

    public InteractionResult playerInteract(Level level, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        Item item = stack.getItem();
        boolean success = false;
        boolean commandSuccess = this.executeClickCommandsIfPresent(player, level, pos);
        if (item == Items.INK_SAC) {
            if (this.hasGlowingText || this.hasAntiqueInk) {
                level.playSound(null, pos, SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.setAntiqueInk(false);
                this.hasGlowingText = false;
                success = true;
            }
        } else if (item == ModRegistry.ANTIQUE_INK.get()) {
            if (!this.hasAntiqueInk) {
                level.playSound(null, pos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.setAntiqueInk(true);
                success = true;
            }
        } else if (item == Items.GLOW_INK_SAC) {
            if (!this.hasGlowingText) {
                level.playSound(null, pos, SoundEvents.GLOW_INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.hasGlowingText = true;
                success = true;
            }
        } else {
            DyeColor dyeColor = ForgeHelper.getColor(stack);
            if (dyeColor != null && this.setColor(dyeColor)) {
                level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                success = true;
            }
        }
        if (success) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return commandSuccess ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
        }
    }

    public boolean hasEditableText(boolean filtering) {
        return Arrays.stream(this.getMessages(filtering)).allMatch(component -> component.equals(CommonComponents.EMPTY) || component.getContents() instanceof LiteralContents);
    }

    public boolean executeClickCommandsIfPresent(Player player, Level level, BlockPos pos) {
        boolean success = false;
        Component[] messages = this.getMessages(player.isTextFilteringEnabled());
        for (Component component : messages) {
            Style style = component.getStyle();
            ClickEvent clickEvent = style.getClickEvent();
            if (clickEvent != null && clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                player.m_20194_().getCommands().performPrefixedCommand(SignBlockEntity.createCommandSourceStack(player, level, pos), clickEvent.getValue());
                success = true;
            }
        }
        return success;
    }

    @Override
    public boolean hasAntiqueInk() {
        return this.hasAntiqueInk;
    }

    @Override
    public void setAntiqueInk(boolean hasInk) {
        this.hasAntiqueInk = hasInk;
        for (int i = 0; i < this.messages.length; i++) {
            this.setMessage(i, this.messages[i], this.filteredMessages[i]);
        }
    }

    public void clearEffects() {
        this.setColor(DyeColor.BLACK);
        this.setAntiqueInk(false);
        this.setHasGlowingText(false);
    }

    public boolean isEmpty(@Nullable Player player) {
        boolean b = player == null || player.isTextFilteringEnabled();
        return !Arrays.stream(this.getMessages(b)).anyMatch(component -> !component.getString().isEmpty());
    }

    public void clear() {
        Arrays.fill(this.filteredMessages, CommonComponents.EMPTY);
        Arrays.fill(this.messages, CommonComponents.EMPTY);
        this.clearEffects();
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public FormattedCharSequence getRenderMessages(int line, Font font) {
        if (line >= this.lines) {
            throw new IndexOutOfBoundsException("Tried to access lie " + line + " of Text Holder of size " + this.lines);
        } else {
            boolean filtered = Minecraft.getInstance().isTextFilteringEnabled();
            if (this.renderMessages[line] == null || this.renderMessagedFiltered != filtered) {
                List<FormattedCharSequence> list = font.split(this.getMessage(line, filtered), this.getMaxLineVisualWidth());
                this.renderMessages[line] = list.isEmpty() ? FormattedCharSequence.EMPTY : (FormattedCharSequence) list.get(0);
            }
            return this.renderMessages[line];
        }
    }

    @OnlyIn(Dist.CLIENT)
    public TextUtil.RenderProperties computeRenderProperties(int combinedLight, Vector3f normal, BooleanSupplier shouldShowGlow) {
        return TextUtil.renderProperties(this.getColor(), this.hasGlowingText(), ClientConfigs.getSignColorMult(), combinedLight, this.hasAntiqueInk() ? Style.EMPTY.withFont(ModTextures.ANTIQUABLE_FONT) : Style.EMPTY, normal, shouldShowGlow);
    }

    @OnlyIn(Dist.CLIENT)
    public TextUtil.RenderProperties getGUIRenderTextProperties() {
        return this.computeRenderProperties(15728880, Direction.UP.step(), () -> true);
    }

    public void acceptClientMessages(Player player, List<FilteredText> list) {
        for (int i = 0; i < list.size(); i++) {
            FilteredText filteredText = (FilteredText) list.get(i);
            Style style = this.getMessage(i, player.isTextFilteringEnabled()).getStyle();
            if (player.isTextFilteringEnabled()) {
                this.setMessage(i, Component.literal(filteredText.filteredOrEmpty()).setStyle(style));
            } else {
                this.setMessage(i, Component.literal(filteredText.raw()).setStyle(style), Component.literal(filteredText.filteredOrEmpty()).setStyle(style));
            }
        }
    }
}