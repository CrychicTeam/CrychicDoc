package net.minecraft.client.sounds;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.Library;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class ChannelAccess {

    private final Set<ChannelAccess.ChannelHandle> channels = Sets.newIdentityHashSet();

    final Library library;

    final Executor executor;

    public ChannelAccess(Library library0, Executor executor1) {
        this.library = library0;
        this.executor = executor1;
    }

    public CompletableFuture<ChannelAccess.ChannelHandle> createHandle(Library.Pool libraryPool0) {
        CompletableFuture<ChannelAccess.ChannelHandle> $$1 = new CompletableFuture();
        this.executor.execute(() -> {
            Channel $$2 = this.library.acquireChannel(libraryPool0);
            if ($$2 != null) {
                ChannelAccess.ChannelHandle $$3 = new ChannelAccess.ChannelHandle($$2);
                this.channels.add($$3);
                $$1.complete($$3);
            } else {
                $$1.complete(null);
            }
        });
        return $$1;
    }

    public void executeOnChannels(Consumer<Stream<Channel>> consumerStreamChannel0) {
        this.executor.execute(() -> consumerStreamChannel0.accept(this.channels.stream().map(p_174978_ -> p_174978_.channel).filter(Objects::nonNull)));
    }

    public void scheduleTick() {
        this.executor.execute(() -> {
            Iterator<ChannelAccess.ChannelHandle> $$0 = this.channels.iterator();
            while ($$0.hasNext()) {
                ChannelAccess.ChannelHandle $$1 = (ChannelAccess.ChannelHandle) $$0.next();
                $$1.channel.updateStream();
                if ($$1.channel.stopped()) {
                    $$1.release();
                    $$0.remove();
                }
            }
        });
    }

    public void clear() {
        this.channels.forEach(ChannelAccess.ChannelHandle::m_120156_);
        this.channels.clear();
    }

    public class ChannelHandle {

        @Nullable
        Channel channel;

        private boolean stopped;

        public boolean isStopped() {
            return this.stopped;
        }

        public ChannelHandle(Channel channel0) {
            this.channel = channel0;
        }

        public void execute(Consumer<Channel> consumerChannel0) {
            ChannelAccess.this.executor.execute(() -> {
                if (this.channel != null) {
                    consumerChannel0.accept(this.channel);
                }
            });
        }

        public void release() {
            this.stopped = true;
            ChannelAccess.this.library.releaseChannel(this.channel);
            this.channel = null;
        }
    }
}