package hello;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TlsCheckController {
    
    private static final String template = "Hello, %s!";
    private final AtomicLong    counter  = new AtomicLong();
    
    @RequestMapping(value = "/tlscheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TlsCheckResponse> greeting(@RequestParam(required = true, defaultValue = "") String urlList) {
        List<TlsCheckResponse> list = new ArrayList<>();
        List<Callable<TlsCheckResponse>> taskList = new ArrayList<>();
        // Process p =
        // Runtime.getRuntime().exec("openssl s_client -tsl1 -connect google.com:443");
        for (String url : urlList.split(",")) {
            taskList.add(new TlsChecker(url));
            // list.add(tlsCheckResponse);
        }
        
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try {
            List<Future<TlsCheckResponse>> futureList = executorService.invokeAll(taskList);
            for (Future<TlsCheckResponse> future : futureList) {
                list.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return list;
    }
    
}
