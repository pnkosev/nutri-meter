package pn.nutrimeter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pn.nutrimeter.web.interceptors.FaviconInterceptor;
import pn.nutrimeter.web.interceptors.PageTitleInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final FaviconInterceptor faviconInterceptor;
    private final PageTitleInterceptor pageTitleInterceptor;

    @Autowired
    public InterceptorConfig(FaviconInterceptor faviconInterceptor, PageTitleInterceptor pageTitleInterceptor) {
        this.faviconInterceptor = faviconInterceptor;
        this.pageTitleInterceptor = pageTitleInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(faviconInterceptor);
        registry.addInterceptor(pageTitleInterceptor);
    }
}
