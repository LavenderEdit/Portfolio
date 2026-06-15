package studios.tkoh.portfolio.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *
 * @author Studios TKOH!
 */
public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        
        // Forzamos a Spring a resolver el token y enviar la cookie al cliente
        if (csrfToken != null) {
            csrfToken.getToken();
            
            // Agregamos el token a una cabecera que Axios sí puede leer en cross-domain
            response.setHeader("X-CSRF-TOKEN-VALUE", csrfToken.getToken());
        }
        
        filterChain.doFilter(request, response);
    }
}
