package hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class TlsChecker implements Callable<TlsCheckResponse> {
    
    private String url;
    
    public TlsChecker(String url) {
        this.url = url;
    }
    
    @Override
    public TlsCheckResponse call() throws Exception {
        TlsCheckResponse tlsCheckResponse = new TlsCheckResponse();
        tlsCheckResponse.setUrl(url);
        url = url.replaceAll("https://", "");
        url = url.replaceAll("http://", "");
        
        tlsCheckResponse.setIsTlsAvailable(isSslXEnabled("-tls1", url + ":443"));
        tlsCheckResponse.setIsTlsAvailableOnly(!(isSslXEnabled("-ssl2", url + ":443") || isSslXEnabled("-ssl3", url
                + ":443"))
                && tlsCheckResponse.getIsTlsAvailable());
        // list.add(tlsCheckResponse);
        tlsCheckResponse.setTitle(getTitle(url, tlsCheckResponse.getIsTlsAvailable()));
        return tlsCheckResponse;
    }
    
    private boolean isSslXEnabled(String sslType, String url) {
        ProcessBuilder processBuilder = new ProcessBuilder("openssl", "s_client", sslType, "-connect", url);
        processBuilder.redirectErrorStream(true);
        
        Process p;
        String response = null;
        try {
            p = processBuilder.start();
            response = new BufferedReader(new InputStreamReader(p.getInputStream())).lines().collect(
                    Collectors.joining("\n"));
            System.out.println(response);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return response.contains("SSL-Session");
    }
    
    private String getTitle(String urlString, boolean isTlsEnabled) throws IOException {
        if (isTlsEnabled) {
            urlString = "https://" + urlString;
        }
        
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        int status = con.getResponseCode();
        if (status != 200) {
            return "";
        }
        
        StringBuffer content = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        String title = content.substring(content.indexOf("<title>") + 7, content.indexOf("</title>"));
        return title;
    }
    
}
