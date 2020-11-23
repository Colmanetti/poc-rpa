package br.com.vivo.rpa.resource.step2;

import br.com.vivo.rpa.resource.DbStep;
import br.com.vivo.rpa.response.ResponseStrategy;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

import static br.com.vivo.rpa.resource.QueryCustomizers.set;

@ApplicationScoped
public class Step2 extends DbStep<Step2Request, Step2Response> {

    @Override
    public String id() {
        return "2";
    }

    @Override
    public List<Step2Response> execute(Step2Request request) {
        return list(set(request.getAge(), request.getGender()));
    }

    @Override
    public ResponseStrategy getResponseStrategy() {
        return ResponseStrategy.CACHE;
    }
}
