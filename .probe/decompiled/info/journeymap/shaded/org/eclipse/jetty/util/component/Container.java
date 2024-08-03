package info.journeymap.shaded.org.eclipse.jetty.util.component;

import java.util.Collection;

public interface Container {

    boolean addBean(Object var1);

    Collection<Object> getBeans();

    <T> Collection<T> getBeans(Class<T> var1);

    <T> T getBean(Class<T> var1);

    boolean removeBean(Object var1);

    void addEventListener(Container.Listener var1);

    void removeEventListener(Container.Listener var1);

    public interface InheritedListener extends Container.Listener {
    }

    public interface Listener {

        void beanAdded(Container var1, Object var2);

        void beanRemoved(Container var1, Object var2);
    }
}