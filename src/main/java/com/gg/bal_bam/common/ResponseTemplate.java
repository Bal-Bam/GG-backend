package com.gg.bal_bam.common;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"status", "message", "results"})
public class ResponseTemplate<T> {
    private final boolean status;
    private final String message;
    private final T results;

    public ResponseTemplate(boolean status, String message, T results) {
        this.status = status;
        this.message = message;
        this.results = results;
    }

    public static <T> ResponseTemplate<T> ok(T results) {
        return new ResponseTemplate<>(true, "요청에 성공했습니다.", results);
    }

    public static <T> ResponseTemplate<T> ok() {
        return new ResponseTemplate<>(true, "요청에 성공했습니다.", null);
    }

    public static <T> ResponseTemplate<T> ok(T results, String message) {
        return new ResponseTemplate<>(true, message, results);
    }

    public static <T> ResponseTemplate<T> fail(T results) {
        return new ResponseTemplate<>(false, "요청에 실패했습니다.", null);
    }


    public static <T> ResponseTemplate<T> fail(T results, String message) {
        return new ResponseTemplate<>(false, message, results);
    }
}
