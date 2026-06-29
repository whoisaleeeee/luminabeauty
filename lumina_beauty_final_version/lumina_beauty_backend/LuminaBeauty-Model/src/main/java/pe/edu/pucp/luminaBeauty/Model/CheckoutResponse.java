package pe.edu.pucp.luminaBeauty.Model;

public class CheckoutResponse {

    private boolean exitoso;
    private String mensaje;
    private int idPedido;
    private String codigoPedido;
    private int idEnvio;
    private int idPago;

    public static CheckoutResponse exito(
            Pedido pedido,
            Envio envio,
            Pago pago
    ) {
        CheckoutResponse response = new CheckoutResponse();

        response.exitoso = true;
        response.mensaje = "Pedido y pago registrados correctamente.";
        response.idPedido = pedido.getId_pedido();
        response.codigoPedido = pedido.getCodigo_pedido();
        response.idEnvio = envio.getId_envio();
        response.idPago = pago.getId_pago();

        return response;
    }

    public static CheckoutResponse error(String mensaje) {
        CheckoutResponse response = new CheckoutResponse();
        response.exitoso = false;
        response.mensaje = mensaje;
        return response;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }
}