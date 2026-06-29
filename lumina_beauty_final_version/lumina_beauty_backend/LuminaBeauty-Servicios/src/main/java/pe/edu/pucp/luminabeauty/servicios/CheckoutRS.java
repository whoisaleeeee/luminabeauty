package pe.edu.pucp.luminabeauty.servicios;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pe.edu.pucp.luminaBeauty.Business.CheckoutBL;
import pe.edu.pucp.luminaBeauty.Business.impl.CheckoutBLImpl;
import pe.edu.pucp.luminaBeauty.Model.CheckoutRequest;
import pe.edu.pucp.luminaBeauty.Model.CheckoutResponse;

@Path("CheckoutRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CheckoutRS {

    private final CheckoutBL checkoutBL = new CheckoutBLImpl();

    @POST
    @Path("procesar")
    public Response procesar(CheckoutRequest request) {
        try {
            CheckoutResponse resultado = checkoutBL.procesarCheckout(request);
            return Response.status(Response.Status.CREATED).entity(resultado).build();
        } catch (Exception ex) {
            ex.printStackTrace();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(CheckoutResponse.error(ex.getMessage()))
                    .build();
        }
    }
}
