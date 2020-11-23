package br.com.vivo.rpa.request.resource;

import br.com.vivo.rpa.request.model.Step;
import br.com.vivo.rpa.request.service.RequestService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Tag(name = "default")
public class RequestResource {

    @Inject RequestService service;

    @POST
    @Path("{resourceId}/{stepId}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(@Valid @BeanParam Step step, String requestJson) {
        if (requestJson == null || requestJson.isBlank()) {
            return service.post(step, null);
        } else {
            return service.post(step, requestJson);
        }
    }
}
