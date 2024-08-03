package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.HttpCompliance;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpVersion;
import info.journeymap.shaded.org.eclipse.jetty.io.Connection;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.Name;

public class HttpConnectionFactory extends AbstractConnectionFactory implements HttpConfiguration.ConnectionFactory {

    private final HttpConfiguration _config;

    private HttpCompliance _httpCompliance;

    private boolean _recordHttpComplianceViolations = false;

    public HttpConnectionFactory() {
        this(new HttpConfiguration());
    }

    public HttpConnectionFactory(@Name("config") HttpConfiguration config) {
        this(config, null);
    }

    public HttpConnectionFactory(@Name("config") HttpConfiguration config, @Name("compliance") HttpCompliance compliance) {
        super(HttpVersion.HTTP_1_1.asString());
        this._config = config;
        this._httpCompliance = compliance == null ? HttpCompliance.RFC7230 : compliance;
        if (config == null) {
            throw new IllegalArgumentException("Null HttpConfiguration");
        } else {
            this.addBean(this._config);
        }
    }

    @Override
    public HttpConfiguration getHttpConfiguration() {
        return this._config;
    }

    public HttpCompliance getHttpCompliance() {
        return this._httpCompliance;
    }

    public boolean isRecordHttpComplianceViolations() {
        return this._recordHttpComplianceViolations;
    }

    public void setHttpCompliance(HttpCompliance httpCompliance) {
        this._httpCompliance = httpCompliance;
    }

    @Override
    public Connection newConnection(Connector connector, EndPoint endPoint) {
        HttpConnection conn = new HttpConnection(this._config, connector, endPoint, this._httpCompliance, this.isRecordHttpComplianceViolations());
        return this.configure(conn, connector, endPoint);
    }

    public void setRecordHttpComplianceViolations(boolean recordHttpComplianceViolations) {
        this._recordHttpComplianceViolations = recordHttpComplianceViolations;
    }
}