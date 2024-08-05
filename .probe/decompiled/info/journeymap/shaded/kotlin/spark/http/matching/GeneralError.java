package info.journeymap.shaded.kotlin.spark.http.matching;

import info.journeymap.shaded.kotlin.spark.CustomErrorPages;
import info.journeymap.shaded.kotlin.spark.ExceptionHandlerImpl;
import info.journeymap.shaded.kotlin.spark.ExceptionMapper;
import info.journeymap.shaded.kotlin.spark.RequestResponseFactory;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;

final class GeneralError {

    private static final Logger LOG = LoggerFactory.getLogger(GeneralError.class);

    static void modify(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Body body, RequestWrapper requestWrapper, ResponseWrapper responseWrapper, Exception e) {
        ExceptionHandlerImpl handler = ExceptionMapper.getInstance().getHandler(e);
        if (handler != null) {
            handler.handle(e, requestWrapper, responseWrapper);
            String bodyAfterFilter = responseWrapper.getDelegate().body();
            if (bodyAfterFilter != null) {
                body.set(bodyAfterFilter);
            }
        } else {
            LOG.error("", (Throwable) e);
            httpResponse.setStatus(500);
            if (CustomErrorPages.existsFor(500)) {
                requestWrapper.setDelegate(RequestResponseFactory.create(httpRequest));
                responseWrapper.setDelegate(RequestResponseFactory.create(httpResponse));
                body.set(CustomErrorPages.getFor(500, requestWrapper, responseWrapper));
            } else {
                body.set("<html><body><h2>500 Internal Server Error</h2></body></html>");
            }
        }
    }
}