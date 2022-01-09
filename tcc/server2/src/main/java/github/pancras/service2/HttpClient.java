package github.pancras.service2;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClient {
    public static HttpResponse get(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            return httpClient.execute(httpGet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
