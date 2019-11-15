package com.dz.io.utils.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;

public class HttpUtils {
    private final String CONTEXT_BASE;

    public HttpUtils(String CONTEXT_BASE) {
        this.CONTEXT_BASE = CONTEXT_BASE;
    }

    public String post(final String context, final String body) {
        try {
            HttpResponse response = Request.Post(CONTEXT_BASE + context)
                    .bodyString(body, ContentType.APPLICATION_JSON).execute().returnResponse();
            assertIs200(response);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    public String get(String context) {
        try {
            HttpResponse response =  Request.Get(CONTEXT_BASE + context).execute().returnResponse();
            assertIs200(response);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertIs200(HttpResponse response) {
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
    }
}
