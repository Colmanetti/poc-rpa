package br.com.vivo.rpa.mock.resource;

import br.com.vivo.rpa.mock.service.CallbackService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Tag(name = "default")
public class CallbackResource {

    @Inject CallbackService service;

    @POST
    @Path("{requestId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@NotEmpty @PathParam("requestId") String requestId, String responseJson) {
        return service.post(requestId, responseJson);
    }
}
