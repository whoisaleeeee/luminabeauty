package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.CheckoutRequest;
import pe.edu.pucp.luminaBeauty.Model.CheckoutResponse;

public interface CheckoutBL {
    CheckoutResponse procesarCheckout(CheckoutRequest request) throws Exception;
}
