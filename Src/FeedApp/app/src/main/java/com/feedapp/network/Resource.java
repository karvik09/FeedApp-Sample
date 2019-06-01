package com.feedapp.network;

public class Resource<T> {

    public enum Status{
        LOADING,SUCCESS,ERROR
    }
    public enum Source{
        DB,NETWORK
    }

    public Status status;

    public T data;

    public Source source;

    public String message;

    public Resource(Status status, T data, Source source, String message) {
        this.status = status;
        this.data = data;
        this.source = source;
        this.message = message;
    }

    public static <T> Resource<T> success(T data, Source source){
        return new Resource<>(Status.SUCCESS, data, source, null);
    }

    public static <T> Resource<T> error(String message,T data ,Source source){
        return new Resource<>(Status.ERROR, data, source, message);
    }

    public static <T> Resource<T> loading(T data, Source source){
        return new Resource<>(Status.LOADING, data, source, null);
    }

}
