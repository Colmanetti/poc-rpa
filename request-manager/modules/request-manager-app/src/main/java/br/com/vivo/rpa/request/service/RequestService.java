package br.com.vivo.rpa.request.service;

import br.com.vivo.rpa.request.RequestCommand;
import br.com.vivo.rpa.request.model.Step;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.UUID;

@ApplicationScoped
public class RequestService {

    @Channel("request-command")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10)
    @Inject Emitter<RequestCommand> emitter;
    @Inject ObjectMapper mapper;

    public Response post(Step step, String requestJson) {
        String jsonWithoutPrettyPrint;
        try {
            jsonWithoutPrettyPrint = removePrettyPrint(requestJson);
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid requestJson").build();
        }
        UUID requestId = UUID.randomUUID();
        RequestCommand requestCommand = RequestCommand.newBuilder().
                setRequestId(requestId).
                setResourceId(step.getResourceId()).
                setStepId(step.getId()).
                setRequestJson(jsonWithoutPrettyPrint).
                build();
        emitter.send(requestCommand).toCompletableFuture().join();
        return Response.ok().entity(requestId.toString()).build();
    }

    private String removePrettyPrint(String requestJson) throws JsonProcessingException {
        if (requestJson == null) {
            return null;
        }
        return mapper.readValue(requestJson, JsonNode.class).toString();
    }
}
