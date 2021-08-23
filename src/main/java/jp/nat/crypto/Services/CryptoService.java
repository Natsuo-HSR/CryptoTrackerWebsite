package jp.nat.crypto.Services;

import jp.nat.crypto.Models.Cryptocurrency;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.*;

public class CryptoService {


    private static String apiKey = "1982de07-4c2f-4eba-a6e3-8b1eeb372b50";
    private static String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest";
    private static String cryptoes = "BTC,ETH,USDT,BNB,ADA,DOGE,XRP,LTC,USDC,DOT,BUSD,ETC,BCH,EOS,LINK,MATIC,THETA,WBTC,XLM,DAI";

    public CryptoService() {}

    public static void main(String[] args) {
        System.out.print("RESPONSE: ");
        String response = makeRequest();
        System.out.println(response);

        System.out.println("PARSED: ");
        ArrayList<Cryptocurrency> cr = parseToJSON(response);
        System.out.println(cr);
    }

    public static ArrayList<Cryptocurrency> getData() {
        String response = makeRequest();
        ArrayList<Cryptocurrency> cr = parseToJSON(response);
        return cr;
    }

    private static String makeRequest() {
        List<NameValuePair> parameters = new ArrayList<>();

        parameters.add(new BasicNameValuePair("symbol", cryptoes));

        try {
            String result = makeAPICall(uri, parameters);
            return result;
        } catch (IOException e) {
            return "IOException error";
        } catch (URISyntaxException e) {
            return "URISyntaxException error";
        }
    }

    private static String makeAPICall(String uri, List<NameValuePair> parameters)
            throws URISyntaxException, IOException {
        String response_content = "";

        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);

        CloseableHttpResponse response = client.execute(request);

        try {
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);

        } finally {
            response.close();
        }

        return response_content;
    }

    private static ArrayList<Cryptocurrency> parseToJSON(String response) {
        ArrayList<Cryptocurrency> parsedJSON = new ArrayList<>();
        JSONObject jo = new JSONObject(response);


        long id = 1;
        for (String s : cryptoes.split(",")) {
            String name = jo.getJSONObject("data")
                    .getJSONObject(s)
                    .get("name").toString();
            String symbol = jo.getJSONObject("data")
                    .getJSONObject(s)
                    .get("symbol").toString();
            Double costCurrentUSD = Double.parseDouble(jo.getJSONObject("data")
                    .getJSONObject(s)
                    .getJSONObject("quote")
                    .getJSONObject("USD")
                    .get("price").toString());
            Double costCurrentRUB = costCurrentUSD * 74;
            String deltaDayPercentage = jo.getJSONObject("data")
                    .getJSONObject(s)
                    .getJSONObject("quote")
                    .getJSONObject("USD")
                    .get("percent_change_24h").toString();
            String deltaWeekPercentage = jo.getJSONObject("data")
                    .getJSONObject(s)
                    .getJSONObject("quote")
                    .getJSONObject("USD")
                    .get("percent_change_7d").toString();
            String tradingVolumeDay = jo.getJSONObject("data")
                    .getJSONObject(s)
                    .getJSONObject("quote")
                    .getJSONObject("USD")
                    .get("volume_24h").toString();

            Cryptocurrency cr = new Cryptocurrency();
            cr.setId(id++);
            cr.setName(name);
            cr.setSymbol(symbol);
            cr.setCostCurrentUSD(costCurrentUSD);
            cr.setCostCurrentRUB(costCurrentRUB);
            cr.setDeltaDayPercentage(deltaDayPercentage);
            cr.setDeltaWeekPercentage(deltaWeekPercentage);
            cr.setTradingVolumeDay(tradingVolumeDay);
            parsedJSON.add(cr);
        }
        return parsedJSON;
    }


}
