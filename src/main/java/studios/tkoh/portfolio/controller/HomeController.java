package studios.tkoh.portfolio.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.dto.response.ApiResponse;

/**
 *
 * @author Studios TKOH!
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getApiStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("app", "Portfolio Hub API");
        status.put("version", "0.5-M1");
        status.put("status", "ONLINE");
        status.put("serverTime", LocalDateTime.now());
        status.put("documentation", "/swagger-ui/index.html");
        status.put("author", "Studios TKOH!");

        return ResponseEntity.ok(ApiResponse.ok("Bienvenido a Portfolio Hub API. El servicio está funcionando correctamente.", status));
    }
}
