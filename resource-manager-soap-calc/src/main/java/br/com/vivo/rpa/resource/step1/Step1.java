package br.com.vivo.rpa.resource.step1;

import br.com.vivo.rpa.resource.Step;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class Step1 extends Step<Step1Request, Step1Response> {

    @Override
    public String id() {
        return "1";
    }

    @Override
    public List<Step1Response> execute(Step1Request request) {
        return List.of(new Step1Response(request.getNumber1() + request.getNumber2()));
    }
}
