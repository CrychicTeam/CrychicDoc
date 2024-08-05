package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.Session;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.annotated.AbstractMethodAnnotationScanner;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.annotated.CallableMethod;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.annotated.InvalidSignatureException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.annotated.OptionalSessionCallableMethod;
import java.io.InputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class JettyAnnotatedScanner extends AbstractMethodAnnotationScanner<JettyAnnotatedMetadata> {

    private static final Logger LOG = Log.getLogger(JettyAnnotatedScanner.class);

    private static final ParamList validBinaryParams = new ParamList();

    private static final ParamList validConnectParams = new ParamList();

    private static final ParamList validCloseParams = new ParamList();

    private static final ParamList validErrorParams = new ParamList();

    private static final ParamList validFrameParams = new ParamList();

    private static final ParamList validTextParams = new ParamList();

    public void onMethodAnnotation(JettyAnnotatedMetadata metadata, Class<?> pojo, Method method, Annotation annotation) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("onMethodAnnotation({}, {}, {}, {})", metadata, pojo, method, annotation);
        }
        if (this.isAnnotation(annotation, OnWebSocketConnect.class)) {
            this.assertValidSignature(method, OnWebSocketConnect.class, validConnectParams);
            this.assertUnset(metadata.onConnect, OnWebSocketConnect.class, method);
            metadata.onConnect = new CallableMethod(pojo, method);
        } else if (this.isAnnotation(annotation, OnWebSocketMessage.class)) {
            if (this.isSignatureMatch(method, validTextParams)) {
                this.assertUnset(metadata.onText, OnWebSocketMessage.class, method);
                metadata.onText = new OptionalSessionCallableMethod(pojo, method);
            } else if (this.isSignatureMatch(method, validBinaryParams)) {
                this.assertUnset(metadata.onBinary, OnWebSocketMessage.class, method);
                metadata.onBinary = new OptionalSessionCallableMethod(pojo, method);
            } else {
                throw InvalidSignatureException.build(method, OnWebSocketMessage.class, validTextParams, validBinaryParams);
            }
        } else if (this.isAnnotation(annotation, OnWebSocketClose.class)) {
            this.assertValidSignature(method, OnWebSocketClose.class, validCloseParams);
            this.assertUnset(metadata.onClose, OnWebSocketClose.class, method);
            metadata.onClose = new OptionalSessionCallableMethod(pojo, method);
        } else if (this.isAnnotation(annotation, OnWebSocketError.class)) {
            this.assertValidSignature(method, OnWebSocketError.class, validErrorParams);
            this.assertUnset(metadata.onError, OnWebSocketError.class, method);
            metadata.onError = new OptionalSessionCallableMethod(pojo, method);
        } else if (this.isAnnotation(annotation, OnWebSocketFrame.class)) {
            this.assertValidSignature(method, OnWebSocketFrame.class, validFrameParams);
            this.assertUnset(metadata.onFrame, OnWebSocketFrame.class, method);
            metadata.onFrame = new OptionalSessionCallableMethod(pojo, method);
        }
    }

    public JettyAnnotatedMetadata scan(Class<?> pojo) {
        JettyAnnotatedMetadata metadata = new JettyAnnotatedMetadata();
        this.scanMethodAnnotations(metadata, pojo);
        return metadata;
    }

    static {
        validConnectParams.addParams(Session.class);
        validCloseParams.addParams(int.class, String.class);
        validCloseParams.addParams(Session.class, int.class, String.class);
        validErrorParams.addParams(Throwable.class);
        validErrorParams.addParams(Session.class, Throwable.class);
        validTextParams.addParams(String.class);
        validTextParams.addParams(Session.class, String.class);
        validTextParams.addParams(Reader.class);
        validTextParams.addParams(Session.class, Reader.class);
        validBinaryParams.addParams(byte[].class, int.class, int.class);
        validBinaryParams.addParams(Session.class, byte[].class, int.class, int.class);
        validBinaryParams.addParams(InputStream.class);
        validBinaryParams.addParams(Session.class, InputStream.class);
        validFrameParams.addParams(Frame.class);
        validFrameParams.addParams(Session.class, Frame.class);
    }
}