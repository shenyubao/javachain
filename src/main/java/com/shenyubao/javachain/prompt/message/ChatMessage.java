package com.shenyubao.javachain.prompt.message;

import com.shenyubao.javachain.prompt.BaseMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shenyubao
 * @date 2023/6/24 00:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChatMessage extends BaseMessage {

    private String role;

    @Override
    public String getType() {
        return "chat";
    }

    public ChatMessage(String content) {
        super(content);
    }

    public ChatMessage(){
        super();
    }

}
