package com.rbkmoney.proxy.mocketbank.mpi.utils.constant;

/**
 * Transaction Status.
 */
public class MpiTransactionStatus {

    /**
     * The merchant submits an authorization request including the
     * ECI and CAVV supplied in the PARes.
     */
    public static final String AUTHENTICATION_SUCCESSFUL = "Y";

    /**
     * The merchant must not submit a failed authentication for
     * authorization.
     */
    public static final String AUTHENTICATION_FAILED = "N";

    /**
     * The merchant may process an authorization request using the
     * appropriate ECI.
     */
    public static final String AUTHENTICATION_COULD_NOT_BE_PERFORMED = "U";

    /**
     * The merchant submits an authorization request including the
     * ECI and CAVV supplied in the PARes.
     */
    public static final String ATTEMPTS_PROCESSING_PERFORMED = "A";

}
