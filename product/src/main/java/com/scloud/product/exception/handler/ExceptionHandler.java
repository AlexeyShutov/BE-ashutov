package com.scloud.product.exception.handler;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.scloud.exception.ServiceUnavailableException;
import org.springframework.stereotype.Service;

@Service
public class ExceptionHandler {

    public RuntimeException unwrap(HystrixRuntimeException e) {
        Throwable cause = e.getFallbackException().getCause();
        RuntimeException result;
        if (cause instanceof HystrixTimeoutException)
            result = new ServiceUnavailableException("Service timed-out");
        else if (cause instanceof RuntimeException)
            result = new ServiceUnavailableException("The error threshold crossed, service is temporary down");
        else
            result = new RuntimeException(cause);

        return result;
    }
}
