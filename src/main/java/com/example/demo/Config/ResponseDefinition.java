package com.example.demo.Config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDefinition<T>  {
    private boolean success;
    private String errorMessage;
    private T data;
}