package com.project.vetProject.core.result;

import lombok.Getter;

@Getter
public class ResultData<T> extends Result {
    private T data;  // İşlem sonucu döndürülen veri

    public ResultData(boolean status, String message, String code, T data) {
        super(status, message, code);
        this.data = data;
    }
}
