package ru.otus.java.pro.homeworks.http.server;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {
    private String uri;
    private HttpMethod httpMethod;
    private Map<String, String> headers;
    private Map<String, String> parameters;
    private String body;

    public String getUri() {
        return uri;
    }

    public HttpRequest(String rawRequest, int size) {
        if (validate(rawRequest, size)) {
            this.parse(rawRequest);
        } else {
            throw new HttpRequestException("Запрос превышает пределнодопустимый размер " + size + "МБ");
        }
    }

    private boolean validate(String data, int size) {
        float unit = 1024.0f;
        float dataBytes = data.getBytes(StandardCharsets.UTF_8).length;
        return dataBytes / (unit * unit) <= size;
    }

    private void parse(String request) {
        int startUriIndex = request.indexOf(' ');
        int endUriIndex = request.indexOf(' ', startUriIndex + 1);
        this.uri = request.substring(startUriIndex + 1, endUriIndex);
        try {
            this.httpMethod = HttpMethod.valueOf(request.substring(0, startUriIndex));
        } catch (IllegalArgumentException e) {
            throw new HttpRequestException("Invalid method");
        }
        if (uri.contains("?")) {
            int startParamIndex = uri.indexOf('?') + 1;
            try {
                this.parameters = Arrays.stream(
                                uri.substring(startParamIndex)
                                        .split("&"))
                        .map(p -> p.split("="))
                        .collect(Collectors.toMap(p -> p[0], v -> v[1]));
            } catch (Exception e) {
                throw new HttpRequestException("Invalid parameters");
            }
        }
        int startHeaderIndex = request.indexOf("\r\n") + 2;
        int endHeaderIndex = request.indexOf("\r\n\r\n");
        try {
            headers = request.substring(startHeaderIndex, endHeaderIndex).lines()
                    .map(line -> line.split(": ", 2))
                    .collect(Collectors.toMap(h -> h[0], v -> v[1]));
        } catch (Exception e) {
            throw new HttpRequestException("Invalid headers");
        }
        this.body = request.substring(endHeaderIndex + 4);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getParameter(String name) {
        return parameters != null ? parameters.get(name) : null;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return headers != null ? headers.get(key) : null;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "uri='" + uri + '\'' +
                ", httpMethod=" + httpMethod +
                ", headers=" + headers +
                ", parameters=" + parameters +
                ", body='" + body + '\'' +
                '}';
    }
}
