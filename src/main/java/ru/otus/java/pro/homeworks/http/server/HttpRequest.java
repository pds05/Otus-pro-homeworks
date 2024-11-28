package ru.otus.java.pro.homeworks.http.server;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String rawRequest;
    private String uri;
    private HttpMethod httpMethod;
    private Map<String, String> titles = new HashMap<>();
    private Map<String, String> parameters;
    private String body;

    public String getUri() {
        return uri;
    }

    public HttpRequest(String rawRequest, int size) {
        this.rawRequest = rawRequest;
        if (validate(rawRequest, size)) {
            this.parse();
        } else {
            throw new HttpRequestException("Запрос превышает пределнодопустимый размер " + size + "МБ");
        }
    }

    private boolean validate(String data, int size) {
        float unit = 1024.0f;
        float dataBytes = data.getBytes(StandardCharsets.UTF_8).length;
        return dataBytes / (unit * unit) <= size;
    }

    private void parse() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        String method = rawRequest.substring(0, startIndex);
        try {
            this.httpMethod = HttpMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
            throw new HttpRequestException("Invalid method " + method);
        }
        this.parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                this.parameters.put(keyValue[0], keyValue[1]);
            }
        }
        int startHeaderIndex = rawRequest.indexOf("\r\n") + 2;
        int endHeaderIndex = rawRequest.indexOf("\r\n\r\n");
        String[] titlesValues = rawRequest.substring(startHeaderIndex, endHeaderIndex).split("\\r\\n");
        for (int i = 1; i < titlesValues.length; i++) {
            String[] titleValue = titlesValues[i].split(": ");
            this.titles.put(titleValue[0], titleValue[1]);
        }
        this.body = rawRequest.substring(endHeaderIndex + 4);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getTitle(String key) {
        return titles.get(key);
    }

    public String getBody() {
        return body;
    }

    public void printInfo(boolean showRawRequest) {
        System.out.println("uri: " + uri);
        System.out.println("method: " + httpMethod);
        System.out.println("parameters: " + parameters);
        System.out.println("titles: " + titles);
        if (showRawRequest) {
            System.out.println(rawRequest);
        }
    }
}
