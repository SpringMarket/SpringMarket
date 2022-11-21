package product.config.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AccessLogger {

    Logger log = LoggerFactory.getLogger("ACCESS");

    private String getURL(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRequestURL());

        String queryString = request.getQueryString();
        if (queryString != null) {
            sb.append("?").append(queryString);
        }
        return sb.toString();
    }

    public void log(HttpServletRequest request, HttpServletResponse response, long elapsed) {
        String accessLog = buildAccessLog(request, response, elapsed);
        log.info(accessLog);
//        log.info("\"host\": {}, method: {}, url: {}, status: {}, elapsed: {}", remoteHost, method, url, status, elapsed);
    }

    private String buildAccessLog(HttpServletRequest request, HttpServletResponse response, long elapsed) {

        String remoteHost = request.getRemoteHost();
        String method = request.getMethod();
        String url = getURL(request);
        int status = response.getStatus();

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (remoteHost != null) {
            sb
                    .append("\"").append("remoteHost").append("\"")
                    .append(":")
                    .append("\"").append(remoteHost).append("\"");
        }
        if (method != null) {
            sb
                    .append(",")
                    .append("\"").append("method").append("\"")
                    .append(":")
                    .append("\"").append(method).append("\"");
        }
        if (url != null) {
            sb
                    .append("\"").append("url").append("\"")
                    .append(":")
                    .append("\"").append(url).append("\"");
        }
        if (status != 0) {
            sb
                    .append(",")
                    .append("\"").append("status").append("\"")
                    .append(":")
                    .append("\"").append(status).append("\"");
        }
        if (elapsed != 0) {
            sb
                    .append(",")
                    .append("\"").append("elapsed").append("\"")
                    .append(":")
                    .append(elapsed);
        }
        sb.append("}");
        return sb.toString();
    }
}
