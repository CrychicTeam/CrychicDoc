package icyllis.modernui.lifecycle;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.UiThread;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import javax.annotation.Nonnull;

public class ViewModelProvider {

    private static final String DEFAULT_KEY = "ViewModelProvider.DefaultKey";

    private final ViewModelProvider.Factory mFactory;

    private final ViewModelStore mViewModelStore;

    public ViewModelProvider(@Nonnull ViewModelStoreOwner owner) {
        this(owner.getViewModelStore(), (ViewModelProvider.Factory) Objects.requireNonNullElseGet(owner.getDefaultViewModelProviderFactory(), ViewModelProvider.NewInstanceFactory::getInstance));
    }

    public ViewModelProvider(@Nonnull ViewModelStoreOwner owner, @Nonnull ViewModelProvider.Factory factory) {
        this(owner.getViewModelStore(), factory);
    }

    public ViewModelProvider(@Nonnull ViewModelStore store, @Nonnull ViewModelProvider.Factory factory) {
        this.mFactory = factory;
        this.mViewModelStore = store;
    }

    @Nonnull
    @UiThread
    public <T extends ViewModel> T get(@Nonnull Class<T> modelClass) {
        String canonicalName = modelClass.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        } else {
            return this.get("ViewModelProvider.DefaultKey:" + canonicalName, modelClass);
        }
    }

    @Nonnull
    @UiThread
    public <T extends ViewModel> T get(@Nonnull String key, @Nonnull Class<T> modelClass) {
        ViewModel viewModel = this.mViewModelStore.get(key);
        if (modelClass.isInstance(viewModel)) {
            if (this.mFactory instanceof ViewModelProvider.OnRequeryFactory) {
                ((ViewModelProvider.OnRequeryFactory) this.mFactory).onRequery(viewModel);
            }
            return (T) viewModel;
        } else {
            if (viewModel != null) {
                ModernUI.LOGGER.warn(ViewModel.MARKER, "Mismatched model class {} with an existing instance {}", modelClass, viewModel);
            }
            if (this.mFactory instanceof ViewModelProvider.KeyedFactory) {
                viewModel = ((ViewModelProvider.KeyedFactory) this.mFactory).create(key, modelClass);
            } else {
                viewModel = this.mFactory.create(modelClass);
            }
            this.mViewModelStore.put(key, viewModel);
            return (T) viewModel;
        }
    }

    @FunctionalInterface
    public interface Factory {

        @Nonnull
        <T extends ViewModel> T create(@Nonnull Class<T> var1);
    }

    abstract static class KeyedFactory extends ViewModelProvider.OnRequeryFactory implements ViewModelProvider.Factory {

        @Nonnull
        public abstract <T extends ViewModel> T create(@Nonnull String var1, @Nonnull Class<T> var2);

        @Nonnull
        @Override
        public final <T extends ViewModel> T create(@Nonnull Class<T> modelClass) {
            throw new UnsupportedOperationException("create(String, Class<?>) must be called on implementations of KeyedFactory");
        }
    }

    public static class NewInstanceFactory implements ViewModelProvider.Factory {

        private static ViewModelProvider.NewInstanceFactory sInstance;

        @Nonnull
        static ViewModelProvider.NewInstanceFactory getInstance() {
            if (sInstance == null) {
                sInstance = new ViewModelProvider.NewInstanceFactory();
            }
            return sInstance;
        }

        @Nonnull
        @Override
        public <T extends ViewModel> T create(@Nonnull Class<T> modelClass) {
            try {
                return (T) modelClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException var3) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, var3);
            }
        }
    }

    static class OnRequeryFactory {

        void onRequery(@Nonnull ViewModel viewModel) {
        }
    }
}