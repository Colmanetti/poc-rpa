package br.com.vivo.rpa.resource;

import br.com.vivo.rpa.request.RequestCommand;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.*;

@ApplicationScoped
public class RequestConsumer {

    private final BlockingQueue<RequestCommand> requests = new LinkedBlockingQueue<>();

    @Inject ResourceService resourceService;

    void startup(@Observes StartupEvent se) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        executor.scheduleAtFixedRate(() -> {
            if (requests.size() > 0) {
                try {
                    resourceService.execute(requests.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    @Incoming("request-command")
    public void consume(RequestCommand requestCommand) {
        requests.add(requestCommand);
    }
}
