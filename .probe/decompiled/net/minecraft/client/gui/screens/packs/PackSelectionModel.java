package net.minecraft.client.gui.screens.packs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;

public class PackSelectionModel {

    private final PackRepository repository;

    final List<Pack> selected;

    final List<Pack> unselected;

    final Function<Pack, ResourceLocation> iconGetter;

    final Runnable onListChanged;

    private final Consumer<PackRepository> output;

    public PackSelectionModel(Runnable runnable0, Function<Pack, ResourceLocation> functionPackResourceLocation1, PackRepository packRepository2, Consumer<PackRepository> consumerPackRepository3) {
        this.onListChanged = runnable0;
        this.iconGetter = functionPackResourceLocation1;
        this.repository = packRepository2;
        this.selected = Lists.newArrayList(packRepository2.getSelectedPacks());
        Collections.reverse(this.selected);
        this.unselected = Lists.newArrayList(packRepository2.getAvailablePacks());
        this.unselected.removeAll(this.selected);
        this.output = consumerPackRepository3;
    }

    public Stream<PackSelectionModel.Entry> getUnselected() {
        return this.unselected.stream().map(p_99920_ -> new PackSelectionModel.UnselectedPackEntry(p_99920_));
    }

    public Stream<PackSelectionModel.Entry> getSelected() {
        return this.selected.stream().map(p_99915_ -> new PackSelectionModel.SelectedPackEntry(p_99915_));
    }

    void updateRepoSelectedList() {
        this.repository.setSelected((Collection<String>) Lists.reverse(this.selected).stream().map(Pack::m_10446_).collect(ImmutableList.toImmutableList()));
    }

    public void commit() {
        this.updateRepoSelectedList();
        this.output.accept(this.repository);
    }

    public void findNewPacks() {
        this.repository.reload();
        this.selected.retainAll(this.repository.getAvailablePacks());
        this.unselected.clear();
        this.unselected.addAll(this.repository.getAvailablePacks());
        this.unselected.removeAll(this.selected);
    }

    public interface Entry {

        ResourceLocation getIconTexture();

        PackCompatibility getCompatibility();

        String getId();

        Component getTitle();

        Component getDescription();

        PackSource getPackSource();

        default Component getExtendedDescription() {
            return this.getPackSource().decorate(this.getDescription());
        }

        boolean isFixedPosition();

        boolean isRequired();

        void select();

        void unselect();

        void moveUp();

        void moveDown();

        boolean isSelected();

        default boolean canSelect() {
            return !this.isSelected();
        }

        default boolean canUnselect() {
            return this.isSelected() && !this.isRequired();
        }

        boolean canMoveUp();

        boolean canMoveDown();
    }

    abstract class EntryBase implements PackSelectionModel.Entry {

        private final Pack pack;

        public EntryBase(Pack pack0) {
            this.pack = pack0;
        }

        protected abstract List<Pack> getSelfList();

        protected abstract List<Pack> getOtherList();

        @Override
        public ResourceLocation getIconTexture() {
            return (ResourceLocation) PackSelectionModel.this.iconGetter.apply(this.pack);
        }

        @Override
        public PackCompatibility getCompatibility() {
            return this.pack.getCompatibility();
        }

        @Override
        public String getId() {
            return this.pack.getId();
        }

        @Override
        public Component getTitle() {
            return this.pack.getTitle();
        }

        @Override
        public Component getDescription() {
            return this.pack.getDescription();
        }

        @Override
        public PackSource getPackSource() {
            return this.pack.getPackSource();
        }

        @Override
        public boolean isFixedPosition() {
            return this.pack.isFixedPosition();
        }

        @Override
        public boolean isRequired() {
            return this.pack.isRequired();
        }

        protected void toggleSelection() {
            this.getSelfList().remove(this.pack);
            this.pack.getDefaultPosition().insert(this.getOtherList(), this.pack, Function.identity(), true);
            PackSelectionModel.this.onListChanged.run();
            PackSelectionModel.this.updateRepoSelectedList();
            this.updateHighContrastOptionInstance();
        }

        private void updateHighContrastOptionInstance() {
            if (this.pack.getId().equals("high_contrast")) {
                OptionInstance<Boolean> $$0 = Minecraft.getInstance().options.highContrast();
                $$0.set(!$$0.get());
            }
        }

        protected void move(int int0) {
            List<Pack> $$1 = this.getSelfList();
            int $$2 = $$1.indexOf(this.pack);
            $$1.remove($$2);
            $$1.add($$2 + int0, this.pack);
            PackSelectionModel.this.onListChanged.run();
        }

        @Override
        public boolean canMoveUp() {
            List<Pack> $$0 = this.getSelfList();
            int $$1 = $$0.indexOf(this.pack);
            return $$1 > 0 && !((Pack) $$0.get($$1 - 1)).isFixedPosition();
        }

        @Override
        public void moveUp() {
            this.move(-1);
        }

        @Override
        public boolean canMoveDown() {
            List<Pack> $$0 = this.getSelfList();
            int $$1 = $$0.indexOf(this.pack);
            return $$1 >= 0 && $$1 < $$0.size() - 1 && !((Pack) $$0.get($$1 + 1)).isFixedPosition();
        }

        @Override
        public void moveDown() {
            this.move(1);
        }
    }

    class SelectedPackEntry extends PackSelectionModel.EntryBase {

        public SelectedPackEntry(Pack pack0) {
            super(pack0);
        }

        @Override
        protected List<Pack> getSelfList() {
            return PackSelectionModel.this.selected;
        }

        @Override
        protected List<Pack> getOtherList() {
            return PackSelectionModel.this.unselected;
        }

        @Override
        public boolean isSelected() {
            return true;
        }

        @Override
        public void select() {
        }

        @Override
        public void unselect() {
            this.m_99950_();
        }
    }

    class UnselectedPackEntry extends PackSelectionModel.EntryBase {

        public UnselectedPackEntry(Pack pack0) {
            super(pack0);
        }

        @Override
        protected List<Pack> getSelfList() {
            return PackSelectionModel.this.unselected;
        }

        @Override
        protected List<Pack> getOtherList() {
            return PackSelectionModel.this.selected;
        }

        @Override
        public boolean isSelected() {
            return false;
        }

        @Override
        public void select() {
            this.m_99950_();
        }

        @Override
        public void unselect() {
        }
    }
}