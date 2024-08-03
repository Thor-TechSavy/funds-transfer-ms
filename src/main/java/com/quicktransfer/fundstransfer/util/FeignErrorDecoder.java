package com.quicktransfer.fundstransfer.util;

import com.quicktransfer.fundstransfer.exception.FundsTransferException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;

public class FeignErrorDecoder implements ErrorDecoder {

        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() == HttpStatus.BAD_REQUEST.value()) {
                return new FundsTransferException("Bad Request: " + response.reason());
            }
            return defaultErrorDecoder.decode(methodKey, response);
        }
}
