package utn.programacion2.TPintegrador.entities;

/**
 * Clase Empresa (A)
 * Representa una empresa con asociación unidireccional 1→1 a DomicilioFiscal
 */
public class Empresa {
    private Long id;
    private Boolean eliminado;
    private String razonSocial;
    private String cuit;
    private String actividadPrincipal;
    private String email;
    private DomicilioFiscal domicilioFiscal;

    public Empresa() {
        this.eliminado = false;
    }

    public Empresa(Long id, Boolean eliminado, String razonSocial, String cuit,
                   String actividadPrincipal, String email, DomicilioFiscal domicilioFiscal) {
        this.id = id;
        this.eliminado = eliminado != null ? eliminado : false;
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.actividadPrincipal = actividadPrincipal;
        this.email = email;
        this.domicilioFiscal = domicilioFiscal;
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

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getActividadPrincipal() {
        return actividadPrincipal;
    }

    public void setActividadPrincipal(String actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DomicilioFiscal getDomicilioFiscal() {
        return domicilioFiscal;
    }

    public void setDomicilioFiscal(DomicilioFiscal domicilioFiscal) {
        this.domicilioFiscal = domicilioFiscal;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", razonSocial='" + razonSocial + '\'' +
                ", cuit='" + cuit + '\'' +
                ", actividadPrincipal='" + actividadPrincipal + '\'' +
                ", email='" + email + '\'' +
                ", domicilioFiscal=" + domicilioFiscal +
                '}';
    }
}
