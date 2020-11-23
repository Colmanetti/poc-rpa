package br.com.vivo.rpa.resource;

import io.quarkus.runtime.configuration.ProfileManager;
import org.hibernate.query.NativeQuery;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hibernate.transform.Transformers.aliasToBean;

public abstract class DbStep<T, Y> extends Step<T, Y> {

    @Inject protected EntityManager em;

    protected String getSql() {
        return getSql(id());
    }

    private String getSql(String sqlFileName) {
        String sqlFile = String.format("/sql/%s.sql", sqlFileName);
        InputStream resource;
        if ("dev".equals(ProfileManager.getActiveProfile())) {
            resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(sqlFile);
        } else {
            resource = getClass().getResourceAsStream(sqlFile);
        }
        try {
            return toString(resource, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private String toString(InputStream is, String encoding) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding))) {
            for (int c = br.read(); c != -1; c = br.read()) {
                sb.append((char) c);
            }
        }
        return sb.toString();
    }

    protected List<Y> list(QueryCustomizer qc) {
        return list(Function.identity(), qc);
    }

    protected List<Y> list(Function<String, String> sqlEditor, QueryCustomizer qc) {
        String sql = getSql();
        Query query = em.createNativeQuery(sqlEditor.apply(sql));
        Class<Y> responseClass = getClassOfResponse();
        query.unwrap(NativeQuery.class).setResultTransformer(aliasToBean(responseClass));
        qc.customize(query);
        return ((List<?>) query.getResultList()).stream().map(responseClass::cast).collect(Collectors.toList());
    }
}
