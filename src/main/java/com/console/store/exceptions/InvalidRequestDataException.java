package com.console.store.exceptions;

import com.console.store.configs.servers.RedisIdentifier;
import com.console.store.dtos.request.CreateValueInfo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestDataException extends RedisConsoleException{
    private RedisIdentifier identifier;
    private CreateValueInfo createValueInfo;
    private BindingResult bindingResult;
    private boolean isDuplicate;

    public InvalidRequestDataException(RedisIdentifier identifier, CreateValueInfo createValueInfo) {
        super();
        this.identifier = identifier;
        this.createValueInfo = createValueInfo;
    }

    public InvalidRequestDataException(RedisIdentifier identifier, CreateValueInfo createValueInfo, boolean isDuplicate) {
        this(identifier, createValueInfo);
        this.isDuplicate = isDuplicate;
    }

    public InvalidRequestDataException(BindingResult bindingResult) {
        super();
        this.bindingResult = bindingResult;
    }

    @Override
    public String getMessage() {
        String message = "";
        if(bindingResult != null){
            List<String> messageList = new ArrayList<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                messageList.add(fieldError.getDefaultMessage());
            }

            message = messageList.toString();
        }
        else if(isDuplicate){
            message = "Duplicate Redis info [ RedisIdentifer : " + identifier.toString()
                    + " \n RedisDataFormat : " + createValueInfo.toString() + "]";
        }
        else{
            message = "Invalid Redis info [ RedisIdentifer : " + identifier.toString()
                    + " \n RedisDataFormat : " + createValueInfo.toString() + "]";
        }

        return message;
    }
}
