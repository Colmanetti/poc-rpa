package br.com.vivo.rpa.resource;

import java.util.Map;

public final class QueryCustomizers {

    private QueryCustomizers() {
    }

    public static QueryCustomizer set (Object... parameters) {
        return query -> {
            for (int i = 0; i < parameters.length; i++) {
                query.setParameter(i + 1, parameters[i]);
            }
        };
    }

    public static QueryCustomizer map (Map<String, Object> map) {
        return query -> {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        };
    }
}
