package utn.programacion2.TPintegrador.entities;

/**
 * Clase DomicilioFiscal (B)
 * Representa el domicilio fiscal de una empresa
 */
public class DomicilioFiscal {
    private Long id;
    private Boolean eliminado;
    private String calle;
    private Integer numero;
    private String ciudad;
    private String provincia;
    private String codigoPostal;
    private String pais;

    public DomicilioFiscal() {
        this.eliminado = false;
    }

    public DomicilioFiscal(Long id, Boolean eliminado, String calle, Integer numero,
                           String ciudad, String provincia, String codigoPostal, String pais) {
        this.id = id;
        this.eliminado = eliminado != null ? eliminado : false;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.pais = pais;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "DomicilioFiscal{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", calle='" + calle + '\'' +
                ", numero=" + numero +
                ", ciudad='" + ciudad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}
