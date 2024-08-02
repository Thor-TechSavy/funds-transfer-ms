package com.quicktransfer.fundstransfer.exception;

public class FundsTransferException extends RuntimeException {

    private static final long serialVersionUID = -800602044298656620L;

    /**
     * Constructor with the error message
     * @param errormessage The error message
     */
    public FundsTransferException(String errormessage) {
        super(errormessage);
    }

    /**
     * Constructor with the error message and the origin exception.
     * @param errorMessage The error message.
     * @param throwable The origin exception
     */
    public FundsTransferException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
