package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.io.AbstractConnection;
import info.journeymap.shaded.org.eclipse.jetty.io.Connection;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.util.ArrayUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.ssl.SslContextFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ManagedObject
public abstract class AbstractConnectionFactory extends ContainerLifeCycle implements ConnectionFactory {

    private final String _protocol;

    private final List<String> _protocols;

    private int _inputbufferSize = 8192;

    protected AbstractConnectionFactory(String protocol) {
        this._protocol = protocol;
        this._protocols = Collections.unmodifiableList(Arrays.asList(protocol));
    }

    protected AbstractConnectionFactory(String... protocols) {
        this._protocol = protocols[0];
        this._protocols = Collections.unmodifiableList(Arrays.asList(protocols));
    }

    @ManagedAttribute(value = "The protocol name", readonly = true)
    @Override
    public String getProtocol() {
        return this._protocol;
    }

    @Override
    public List<String> getProtocols() {
        return this._protocols;
    }

    @ManagedAttribute("The buffer size used to read from the network")
    public int getInputBufferSize() {
        return this._inputbufferSize;
    }

    public void setInputBufferSize(int size) {
        this._inputbufferSize = size;
    }

    protected AbstractConnection configure(AbstractConnection connection, Connector connector, EndPoint endPoint) {
        connection.setInputBufferSize(this.getInputBufferSize());
        if (connector instanceof ContainerLifeCycle) {
            ContainerLifeCycle aggregate = (ContainerLifeCycle) connector;
            for (Connection.Listener listener : aggregate.getBeans(Connection.Listener.class)) {
                connection.addListener(listener);
            }
        }
        for (Connection.Listener listener : this.getBeans(Connection.Listener.class)) {
            connection.addListener(listener);
        }
        return connection;
    }

    public String toString() {
        return String.format("%s@%x%s", this.getClass().getSimpleName(), this.hashCode(), this.getProtocols());
    }

    public static ConnectionFactory[] getFactories(SslContextFactory sslContextFactory, ConnectionFactory... factories) {
        factories = ArrayUtil.removeNulls(factories);
        if (sslContextFactory == null) {
            return factories;
        } else {
            for (ConnectionFactory factory : factories) {
                if (factory instanceof HttpConfiguration.ConnectionFactory) {
                    HttpConfiguration config = ((HttpConfiguration.ConnectionFactory) factory).getHttpConfiguration();
                    if (config.getCustomizer(SecureRequestCustomizer.class) == null) {
                        config.addCustomizer(new SecureRequestCustomizer());
                    }
                }
            }
            return ArrayUtil.prependToArray(new SslConnectionFactory(sslContextFactory, factories[0].getProtocol()), factories, ConnectionFactory.class);
        }
    }
}