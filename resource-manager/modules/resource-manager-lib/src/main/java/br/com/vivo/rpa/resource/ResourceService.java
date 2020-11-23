package br.com.vivo.rpa.resource;

import br.com.vivo.rpa.request.RequestCommand;
import br.com.vivo.rpa.response.ResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ApplicationScoped
public class ResourceService {

    private static final Logger logger = Logger.getLogger(ResourceService.class);

    @Inject ObjectMapper mapper;
    @Inject Instance<Step<?, ?>> steps;
    @ConfigProperty(name = "resourceId") String resourceId;
    @Inject @Channel("response-event") Emitter<ResponseEvent> emitter;

    private final Map<Step<?, ?>, ObjectReader> readers = new HashMap<>();
    private final Map<Step<?, ?>, ObjectWriter> writers = new HashMap<>();

    @Transactional
    public void execute(RequestCommand request) {
        if (request.getResourceId().equals(resourceId)) {
            steps.stream().
                    filter(step -> Objects.equals(step.id(), request.getStepId())).
                    findAny().
                    ifPresent(step -> {
                        try {
                            execute(step, request);
                        } catch (JsonProcessingException e) {
                            logger.error("Error processing json for request " + request.getRequestId(), e);
                        }
                    });
        }
    }

    private void execute(Step<?, ?> step, RequestCommand request) throws JsonProcessingException {
        Object response;
        if (request.getRequestJson().isEmpty()) {
            response = step.execute(null);
        } else {
            readers.putIfAbsent(step, mapper.readerFor(step.getClassOfRequest()));
            String requestJson = request.getRequestJson().get();
            response = step.execute(readers.get(step).readValue(requestJson));
        }
        writers.putIfAbsent(step, mapper.writerFor(response.getClass()));
        ResponseEvent responseEvent = ResponseEvent.newBuilder().
                setRequestId(request.getRequestId()).
                setResponseJson(writers.get(step).writeValueAsString(response)).
                setResponseStrategy(step.getResponseStrategy()).
                build();
        emitter.send(responseEvent);
    }
}
