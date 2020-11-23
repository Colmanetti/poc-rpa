package br.com.vivo.rpa.resource;

import br.com.vivo.rpa.response.ResponseStrategy;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class Step<T, Y> {

    private Class<T> classOfRequest;
    private Class<Y> classOfResponse;

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void initialize() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        classOfRequest = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        classOfResponse = (Class<Y>) parameterizedType.getActualTypeArguments()[1];
    }

    public abstract String id();

    public abstract List<Y> execute(T request);

    public Class<T> getClassOfRequest() {
        return classOfRequest;
    }

    public Class<Y> getClassOfResponse() {
        return classOfResponse;
    }

    public ResponseStrategy getResponseStrategy() {
        return ResponseStrategy.CALLBACK;
    }
}
