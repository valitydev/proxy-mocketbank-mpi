package com.rbkmoney.proxy.mocketbank.mpi.utils.constant;

/**
 * Indicates the algorithm used to generate the Cardholder Authentication Verification Value.
 */
public class MpiCavvAlgorithm {

    /**
     * 0: HMAC (as per SET TransStain) (no longer in use for version 1.0.2)
     */
    public static final String HMAC_AS_PER_SET_TRANS_STAIN = "0";

    /**
     * 1: CVV (no longer in use for version 1.0.2).
     */
    public static final String CVV_NO_LONGER = "1";

    /**
     * 2: CVV with ATN.
     */
    public static final String CVV_WITH_ATN = "2";

    /**
     * 3: MasterCard SPA algorithm.
     */
    public static final String MASTERCARD_SPA_ALGORITHM = "3";

}
