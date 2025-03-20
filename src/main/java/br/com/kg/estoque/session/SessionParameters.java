package br.com.kg.estoque.session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import lombok.Getter;
import lombok.Setter;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionParameters {

    @Getter @Setter
    private boolean sidebarAberto = true;

    @Getter @Setter
    private String themeSwitch;

    @Getter @Setter
    private Integer ano;
}
