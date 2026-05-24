package com.pharmacy.generics;

public final class Result<T> {

    private final boolean success;
    private final T data;
    private final String  errorMessage;

    private Result(boolean success, T data, String errorMessage) {
        this.success      = success;
        this.data         = data;
        this.errorMessage = errorMessage;
    }

    public static <T> Result<T> ok(T data)    { return new Result<>(true,  data, null); }

    public static <T> Result<T> ok()          { return new Result<>(true,  null, null); }

    public static <T> Result<T> fail(String message) { return new Result<>(false, null, message); }

    public boolean isSuccess()       { return success; }
    public boolean isFailure()       { return !success; }
    public T       getData()         { return data; }
    public String  getErrorMessage() { return errorMessage; }

    @Override
    public String toString() {
        return success ? "Result.ok(" + data + ")" : "Result.fail(" + errorMessage + ")";
    }
}
