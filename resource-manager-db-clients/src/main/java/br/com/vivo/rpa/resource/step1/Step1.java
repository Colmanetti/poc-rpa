package br.com.vivo.rpa.resource.step1;

import br.com.vivo.rpa.resource.DbStep;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

import static br.com.vivo.rpa.resource.QueryCustomizers.set;

@ApplicationScoped
public class Step1 extends DbStep<Step1Request, Step1Response> {

    @Override
    public String id() {
        return "1";
    }

    @Override
    public List<Step1Response> execute(Step1Request request) {
        return list(set(request.getAge()));
    }
}
