package org.gtikim.nettywrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class NettyWrapReadyEvent implements ApplicationListener<ApplicationReadyEvent> {
    private final Environment env;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("env: [" + env.toString() + "]");
        log.debug(env.toString());
    }
}
