package br.com.vivo.rpa.request.model;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.PathParam;

public class Step {

    @NotEmpty
    @PathParam("stepId")
    private String id;

    @NotEmpty
    @PathParam("resourceId")
    private String resourceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
