package com.shenyubao.langchainjava;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author shenyubao
 * @date 2023/6/23 22:55
 */
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class LangChainException extends RuntimeException {

    private String msg;
    private int code;

    public LangChainException(IError error) {
        super(error.msg());
        this.code = error.code();
        this.msg = error.msg();
    }

    public LangChainException(IError error, String messageDetail) {
        super(error.msg());
        this.code = error.code();
        this.msg = error.msg() + " ,detail: " + messageDetail;
    }

    public LangChainException(String msg) {
        super(msg);
        this.code = CommonError.SYS_ERROR.code();
        this.msg = msg;
    }

}
