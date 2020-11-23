package br.com.vivo.rpa.response;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

@ApplicationScoped
public class ResponseEventConsumer {

    private static final Logger logger = Logger.getLogger(ResponseEventConsumer.class);

    @RestClient
    @Inject CallbackService callbackService;
    @Inject ReactiveRedisClient cacheService;

    @Incoming("response-event")
    public CompletionStage<?> consume(Message<ResponseEvent> message) {
        ResponseEvent responseEvent = message.getPayload();
        switch (responseEvent.getResponseStrategy()) {
            case CACHE:
                return cache(message);
            case CALLBACK:
                return callback(message);
            default:
                logger.error("Unexpected strategy for request id " + responseEvent.getRequestId() + "!");
                return message.nack(new IllegalArgumentException("Unexpected strategy!"));
        }
    }

    public CompletionStage<?> callback(Message<ResponseEvent> message) {
        ResponseEvent event = message.getPayload();
        String errorMessage = "Error executing callback for request id " + event.getRequestId() + "!";
        Consumer<Throwable> exceptionHandler = throwable -> {
            logger.error(errorMessage, throwable);
            message.nack(throwable);
        };
        Consumer<Response> responseHandler = response -> {
            if (response.getStatus() >= 200 && response.getStatus() < 300) {
                message.ack();
            } else {
                String responseEntity;
                if (response.hasEntity()) {
                    responseEntity = response.readEntity(String.class);
                } else {
                    responseEntity = " The response entity was empty.";
                }
                logger.error(errorMessage + responseEntity);
                message.nack(new RuntimeException(errorMessage));
            }
        };
        return callbackService.post(event.getRequestId().toString(), event.getResponseJson()).
                onItem().invoke(responseHandler).
                onFailure().invoke(exceptionHandler).
                subscribeAsCompletionStage();
    }

    public CompletionStage<?> cache(Message<ResponseEvent> message) {
        ResponseEvent event = message.getPayload();
        Consumer<Throwable> exceptionHandler = throwable -> {
            String errorMessage = "Error persisting cache for request id " + event.getRequestId() + "!";
            logger.error(errorMessage, throwable);
            message.nack(throwable);
        };
        return cacheService.
                set(Arrays.asList(event.getRequestId().toString(), event.getResponseJson())).
                onItem().invoke(rows -> message.ack()).
                onFailure().invoke(exceptionHandler).
                subscribeAsCompletionStage();
    }
}
