package br.com.vivo.rpa.mock.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ApplicationScoped
public class CallbackService {

    private static final Logger logger = Logger.getLogger(CallbackService.class);

    @ConfigProperty(name = "exportPath") String exportPath;

    public Response post(String requestId, String responseJson) {
        try {
            Files.writeString(Paths.get(exportPath, requestId + ".json"), responseJson);
            return Response.ok().build();
        } catch (IOException e) {
            logger.error("Error persisting the callback json!", e);
            return Response.serverError().build();
        }
    }
}
