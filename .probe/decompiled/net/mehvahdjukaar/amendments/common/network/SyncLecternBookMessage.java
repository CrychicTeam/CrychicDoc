package net.mehvahdjukaar.amendments.common.network;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.mehvahdjukaar.amendments.common.LecternEditMenu;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.TextFilter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.LecternBlockEntity;

public class SyncLecternBookMessage implements Message {

    private final List<String> pages;

    private final Optional<String> title;

    private final BlockPos pos;

    private final boolean takeBook;

    public SyncLecternBookMessage(BlockPos pos, List<String> list, Optional<String> optional, boolean takeBook) {
        this.pos = pos;
        this.pages = ImmutableList.copyOf(list);
        this.title = optional;
        this.takeBook = takeBook;
    }

    public SyncLecternBookMessage(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.pages = buffer.readCollection(FriendlyByteBuf.limitValue(Lists::newArrayListWithCapacity, 200), b -> b.readUtf(8192));
        this.title = buffer.readOptional(b -> b.readUtf(128));
        this.takeBook = buffer.readBoolean();
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeCollection(this.pages, (friendlyByteBuf, string) -> friendlyByteBuf.writeUtf(string, 8192));
        buffer.writeOptional(this.title, (friendlyByteBuf, string) -> friendlyByteBuf.writeUtf(string, 128));
        buffer.writeBoolean(this.takeBook);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        ServerPlayer player = (ServerPlayer) context.getSender();
        Level level = player.m_9236_();
        if (level.getBlockEntity(this.pos) instanceof LecternBlockEntity be) {
            ItemStack book = be.getBook();
            if (book.getItem() instanceof WritableBookItem) {
                List<String> list = Lists.newArrayList();
                this.title.ifPresent(list::add);
                Stream<String> limit = this.pages.stream().limit(100L);
                limit.forEach(list::add);
                Consumer<List<FilteredText>> consumer = this.title.isPresent() ? l -> this.signBook(be, player, book, (FilteredText) l.get(0), l.subList(1, l.size())) : l -> this.updateBookContents(be, player, book, l);
                this.filterTextPacket(player, list, TextFilter::m_5925_).thenAcceptAsync(consumer, level.getServer());
            }
        }
    }

    private <T, R> CompletableFuture<R> filterTextPacket(ServerPlayer player, T message, BiFunction<TextFilter, T, CompletableFuture<R>> processor) {
        return ((CompletableFuture) processor.apply(player.getTextFilter(), message)).thenApply(object -> {
            if (!player.connection.isAcceptingMessages()) {
                throw new CancellationException("disconnected");
            } else {
                return object;
            }
        });
    }

    private void updateBookContents(LecternBlockEntity be, ServerPlayer player, ItemStack itemstack, List<FilteredText> pages) {
        this.updateBookPages(be, player, pages, UnaryOperator.identity(), itemstack);
    }

    private void signBook(LecternBlockEntity be, ServerPlayer player, ItemStack itemstack, FilteredText title, List<FilteredText> pages) {
        ItemStack newStack = new ItemStack(Items.WRITTEN_BOOK);
        CompoundTag compoundtag = itemstack.getTag();
        if (compoundtag != null) {
            newStack.setTag(compoundtag.copy());
        }
        newStack.addTagElement("author", StringTag.valueOf(player.m_7755_().getString()));
        if (player.isTextFilteringEnabled()) {
            newStack.addTagElement("title", StringTag.valueOf(title.filteredOrEmpty()));
        } else {
            newStack.addTagElement("filtered_title", StringTag.valueOf(title.filteredOrEmpty()));
            newStack.addTagElement("title", StringTag.valueOf(title.raw()));
        }
        this.updateBookPages(be, player, pages, string -> Component.Serializer.toJson(Component.literal(string)), newStack);
    }

    private void updateBookPages(LecternBlockEntity be, ServerPlayer player, List<FilteredText> pages, UnaryOperator<String> unaryOperator, ItemStack book) {
        ListTag listtag = new ListTag();
        if (player.isTextFilteringEnabled()) {
            Stream<StringTag> stringTagStream = pages.stream().map(arg -> StringTag.valueOf((String) unaryOperator.apply(arg.filteredOrEmpty())));
            stringTagStream.forEach(listtag::add);
        } else {
            CompoundTag compoundtag = new CompoundTag();
            int i = 0;
            for (int j = pages.size(); i < j; i++) {
                FilteredText filteredtext = (FilteredText) pages.get(i);
                String s = filteredtext.raw();
                listtag.add(StringTag.valueOf((String) unaryOperator.apply(s)));
                if (filteredtext.isFiltered()) {
                    compoundtag.putString(String.valueOf(i), (String) unaryOperator.apply(filteredtext.filteredOrEmpty()));
                }
            }
            if (!compoundtag.isEmpty()) {
                book.addTagElement("filtered_pages", compoundtag);
            }
        }
        book.addTagElement("pages", listtag);
        be.setBook(book);
        player.m_9236_().sendBlockUpdated(this.pos, be.m_58900_(), be.m_58900_(), 3);
        if (this.takeBook && player.f_36096_ instanceof LecternEditMenu m) {
            m.m_6366_(player, 3);
        }
    }
}