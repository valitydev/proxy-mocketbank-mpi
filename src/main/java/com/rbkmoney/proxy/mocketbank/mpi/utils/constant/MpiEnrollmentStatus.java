package com.rbkmoney.proxy.mocketbank.mpi.utils.constant;

/**
 * 3-D Secure enrollment status.
 */
public class MpiEnrollmentStatus {

    /**
     * Authentication Available – Cardholder is enrolled, Activation During Shopping is
     * supported, or proof of attempted authentication available. The merchant uses the
     * URL of issuer ACS included in VERes to create the Payer Authentication Request.
     */
    public static final String AUTHENTICATION_AVAILABLE = "Y";

    /**
     * Cardholder Not Participating – Cardholder is not enrolled.
     */
    public static final String CARDHOLDER_NOT_PARTICIPATING = "N";

    /**
     * Unable to Authenticate or Card Not Eligible for Attempts
     * (such as a Commercial or anonymous Prepaid card).
     */
    public static final String UNABLE_TO_AUTHENTICATE = "U";

}
