package br.com.vivo.rpa.resource;

import javax.persistence.Query;
import java.util.Objects;

@FunctionalInterface
public interface QueryCustomizer {

    void customize (Query query);

    default QueryCustomizer combine (QueryCustomizer after) {
        Objects.requireNonNull(after);
        return query -> {
            customize(query);
            after.customize(query);
        };
    }
}
