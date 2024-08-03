package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.BadMessageException;
import info.journeymap.shaded.org.eclipse.jetty.http.HostPortHttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpCompliance;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpFields;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpGenerator;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeaderValue;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpMethod;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpParser;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpURI;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpVersion;
import info.journeymap.shaded.org.eclipse.jetty.http.MetaData;
import info.journeymap.shaded.org.eclipse.jetty.io.Connection;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class HttpChannelOverHttp extends HttpChannel implements HttpParser.RequestHandler, HttpParser.ComplianceHandler {

    private static final Logger LOG = Log.getLogger(HttpChannelOverHttp.class);

    private static final HttpField PREAMBLE_UPGRADE_H2C = new HttpField(HttpHeader.UPGRADE, "h2c");

    private static final String ATTR_COMPLIANCE_VIOLATIONS = "info.journeymap.shaded.org.eclipse.jetty.http.compliance.violations";

    private final HttpFields _fields = new HttpFields();

    private final MetaData.Request _metadata = new MetaData.Request(this._fields);

    private final HttpConnection _httpConnection;

    private HttpField _connection;

    private HttpField _upgrade = null;

    private boolean _delayedForContent;

    private boolean _unknownExpectation = false;

    private boolean _expect100Continue = false;

    private boolean _expect102Processing = false;

    private List<String> _complianceViolations;

    private HttpFields _trailers;

    public HttpChannelOverHttp(HttpConnection httpConnection, Connector connector, HttpConfiguration config, EndPoint endPoint, HttpTransport transport) {
        super(connector, config, endPoint, transport);
        this._httpConnection = httpConnection;
        this._metadata.setURI(new HttpURI());
    }

    @Override
    protected HttpInput newHttpInput(HttpChannelState state) {
        return new HttpInputOverHTTP(state);
    }

    @Override
    public void recycle() {
        super.recycle();
        this._unknownExpectation = false;
        this._expect100Continue = false;
        this._expect102Processing = false;
        this._metadata.recycle();
        this._connection = null;
        this._fields.clear();
        this._upgrade = null;
        this._trailers = null;
    }

    @Override
    public boolean isExpecting100Continue() {
        return this._expect100Continue;
    }

    @Override
    public boolean isExpecting102Processing() {
        return this._expect102Processing;
    }

    @Override
    public boolean startRequest(String method, String uri, HttpVersion version) {
        this._metadata.setMethod(method);
        this._metadata.getURI().parseRequestTarget(method, uri);
        this._metadata.setHttpVersion(version);
        this._unknownExpectation = false;
        this._expect100Continue = false;
        this._expect102Processing = false;
        return false;
    }

    @Override
    public void parsedHeader(HttpField field) {
        HttpHeader header = field.getHeader();
        String value = field.getValue();
        if (header != null) {
            switch(header) {
                case CONNECTION:
                    this._connection = field;
                    break;
                case HOST:
                    if (!this._metadata.getURI().isAbsolute() && field instanceof HostPortHttpField) {
                        HostPortHttpField hp = (HostPortHttpField) field;
                        this._metadata.getURI().setAuthority(hp.getHost(), hp.getPort());
                    }
                    break;
                case EXPECT:
                    if (this._metadata.getHttpVersion() == HttpVersion.HTTP_1_1) {
                        HttpHeaderValue expect = HttpHeaderValue.CACHE.get(value);
                        switch(expect == null ? HttpHeaderValue.UNKNOWN : expect) {
                            case CONTINUE:
                                this._expect100Continue = true;
                                break;
                            case PROCESSING:
                                this._expect102Processing = true;
                                break;
                            default:
                                String[] values = field.getValues();
                                for (int i = 0; values != null && i < values.length; i++) {
                                    expect = HttpHeaderValue.CACHE.get(values[i].trim());
                                    if (expect == null) {
                                        this._unknownExpectation = true;
                                    } else {
                                        switch(expect) {
                                            case CONTINUE:
                                                this._expect100Continue = true;
                                                break;
                                            case PROCESSING:
                                                this._expect102Processing = true;
                                                break;
                                            default:
                                                this._unknownExpectation = true;
                                        }
                                    }
                                }
                        }
                    }
                    break;
                case UPGRADE:
                    this._upgrade = field;
            }
        }
        this._fields.add(field);
    }

    @Override
    public void parsedTrailer(HttpField field) {
        if (this._trailers == null) {
            this._trailers = new HttpFields();
        }
        this._trailers.add(field);
    }

    @Override
    public void continue100(int available) throws IOException {
        if (this.isExpecting100Continue()) {
            this._expect100Continue = false;
            if (available == 0) {
                if (this.getResponse().isCommitted()) {
                    throw new IOException("Committed before 100 Continues");
                }
                boolean committed = this.sendResponse(HttpGenerator.CONTINUE_100_INFO, null, false);
                if (!committed) {
                    throw new IOException("Concurrent commit while trying to send 100-Continue");
                }
            }
        }
    }

    @Override
    public void earlyEOF() {
        this._httpConnection.getGenerator().setPersistent(false);
        if (this._metadata.getMethod() == null) {
            this._httpConnection.close();
        } else if (this.onEarlyEOF() || this._delayedForContent) {
            this._delayedForContent = false;
            this.handle();
        }
    }

    @Override
    public boolean content(ByteBuffer content) {
        HttpInput.Content c = this._httpConnection.newContent(content);
        boolean handle = this.onContent(c) || this._delayedForContent;
        this._delayedForContent = false;
        return handle;
    }

    @Override
    public void asyncReadFillInterested() {
        this._httpConnection.asyncReadFillInterested();
    }

    @Override
    public void badMessage(int status, String reason) {
        this._httpConnection.getGenerator().setPersistent(false);
        try {
            this.onRequest(this._metadata);
            this.getRequest().getHttpInput().earlyEOF();
        } catch (Exception var4) {
            LOG.ignore(var4);
        }
        this.onBadMessage(status, reason);
    }

    @Override
    public boolean headerComplete() {
        if (this._complianceViolations != null && !this._complianceViolations.isEmpty()) {
            this.getRequest().setAttribute("info.journeymap.shaded.org.eclipse.jetty.http.compliance.violations", this._complianceViolations);
            this._complianceViolations = null;
        }
        boolean persistent;
        switch(this._metadata.getHttpVersion()) {
            case HTTP_0_9:
                persistent = false;
                break;
            case HTTP_1_0:
                if (this.getHttpConfiguration().isPersistentConnectionsEnabled()) {
                    if (this._connection != null) {
                        if (this._connection.contains(HttpHeaderValue.KEEP_ALIVE.asString())) {
                            persistent = true;
                        } else {
                            persistent = this._fields.contains(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE.asString());
                        }
                    } else {
                        persistent = false;
                    }
                } else {
                    persistent = false;
                }
                if (!persistent) {
                    persistent = HttpMethod.CONNECT.is(this._metadata.getMethod());
                }
                if (persistent) {
                    this.getResponse().getHttpFields().add(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE);
                }
                break;
            case HTTP_1_1:
                if (this._unknownExpectation) {
                    this.badMessage(417, null);
                    return false;
                }
                if (this.getHttpConfiguration().isPersistentConnectionsEnabled()) {
                    if (this._connection != null) {
                        if (this._connection.contains(HttpHeaderValue.CLOSE.asString())) {
                            persistent = false;
                        } else {
                            persistent = !this._fields.contains(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE.asString());
                        }
                    } else {
                        persistent = true;
                    }
                } else {
                    persistent = false;
                }
                if (!persistent) {
                    persistent = HttpMethod.CONNECT.is(this._metadata.getMethod());
                }
                if (!persistent) {
                    this.getResponse().getHttpFields().add(HttpHeader.CONNECTION, HttpHeaderValue.CLOSE);
                }
                if (this._upgrade != null && this.upgrade()) {
                    return true;
                }
                break;
            case HTTP_2:
                this._upgrade = PREAMBLE_UPGRADE_H2C;
                if (HttpMethod.PRI.is(this._metadata.getMethod()) && "*".equals(this._metadata.getURI().toString()) && this._fields.size() == 0 && this.upgrade()) {
                    return true;
                }
                this.badMessage(426, null);
                this._httpConnection.getParser().close();
                return false;
            default:
                throw new IllegalStateException("unsupported version " + this._metadata.getHttpVersion());
        }
        if (!persistent) {
            this._httpConnection.getGenerator().setPersistent(false);
        }
        this.onRequest(this._metadata);
        this._delayedForContent = this.getHttpConfiguration().isDelayDispatchUntilContent() && (this._httpConnection.getParser().getContentLength() > 0L || this._httpConnection.getParser().isChunking()) && !this.isExpecting100Continue() && !this.isCommitted() && this._httpConnection.isRequestBufferEmpty();
        return !this._delayedForContent;
    }

    private boolean upgrade() throws BadMessageException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("upgrade {} {}", this, this._upgrade);
        }
        if (this._upgrade == PREAMBLE_UPGRADE_H2C || this._connection != null && this._connection.contains("upgrade")) {
            ConnectionFactory.Upgrading factory = null;
            for (ConnectionFactory f : this.getConnector().getConnectionFactories()) {
                if (f instanceof ConnectionFactory.Upgrading && f.getProtocols().contains(this._upgrade.getValue())) {
                    factory = (ConnectionFactory.Upgrading) f;
                    break;
                }
            }
            if (factory == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No factory for {} in {}", this._upgrade, this.getConnector());
                }
                return false;
            } else {
                HttpFields response101 = new HttpFields();
                Connection upgrade_connection = factory.upgradeConnection(this.getConnector(), this.getEndPoint(), this._metadata, response101);
                if (upgrade_connection == null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Upgrade ignored for {} by {}", this._upgrade, factory);
                    }
                    return false;
                } else {
                    try {
                        if (this._upgrade != PREAMBLE_UPGRADE_H2C) {
                            this.sendResponse(new MetaData.Response(HttpVersion.HTTP_1_1, 101, response101, 0L), null, true);
                        }
                    } catch (IOException var5) {
                        throw new BadMessageException(500, null, var5);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Upgrade from {} to {}", this.getEndPoint().getConnection(), upgrade_connection);
                    }
                    this.getRequest().setAttribute("info.journeymap.shaded.org.eclipse.jetty.server.HttpConnection.UPGRADE", upgrade_connection);
                    this.getResponse().setStatus(101);
                    this.getHttpTransport().onCompleted();
                    return true;
                }
            }
        } else {
            throw new BadMessageException(400);
        }
    }

    @Override
    protected void handleException(Throwable x) {
        this._httpConnection.getGenerator().setPersistent(false);
        super.handleException(x);
    }

    @Override
    public void abort(Throwable failure) {
        super.abort(failure);
        this._httpConnection.getGenerator().setPersistent(false);
    }

    @Override
    public boolean contentComplete() {
        boolean handle = this.onContentComplete() || this._delayedForContent;
        this._delayedForContent = false;
        return handle;
    }

    @Override
    public boolean messageComplete() {
        if (this._trailers != null) {
            this.onTrailers(this._trailers);
        }
        return this.onRequestComplete();
    }

    @Override
    public int getHeaderCacheSize() {
        return this.getHttpConfiguration().getHeaderCacheSize();
    }

    @Override
    public void onComplianceViolation(HttpCompliance compliance, HttpCompliance required, String reason) {
        if (this._httpConnection.isRecordHttpComplianceViolations()) {
            if (this._complianceViolations == null) {
                this._complianceViolations = new ArrayList();
            }
            String violation = String.format("%s<%s: %s for %s", compliance, required, reason, this.getHttpTransport());
            this._complianceViolations.add(violation);
            if (LOG.isDebugEnabled()) {
                LOG.debug(violation);
            }
        }
    }
}