package utn.programacion2.TPintegrador.main;

import utn.programacion2.TPintegrador.dao.DomicilioFiscalDAO;
import utn.programacion2.TPintegrador.dao.EmpresaDAO;
import utn.programacion2.TPintegrador.service.DomicilioFiscalService;
import utn.programacion2.TPintegrador.service.EmpresaService;

public class Main {
    public static void main(String[] args) {
        System.out.println("""
                UTN - TUPaD
                Programacion II
                Trabajo Final Integrador 2025
                """);
        var domicilioDao = new DomicilioFiscalDAO();
        var empresaDao = new EmpresaDAO(domicilioDao);
        var domicilioFiscalService = new DomicilioFiscalService(domicilioDao);
        var empresaService = new EmpresaService(
                empresaDao,
                domicilioFiscalService
        );

        System.out.println(empresaService.getAll());
    }
}