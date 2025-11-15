package utn.programacion2.TPintegrador.main;

import utn.programacion2.TPintegrador.entities.DomicilioFiscal;
import utn.programacion2.TPintegrador.entities.Empresa;

import java.util.Scanner;

public class AppMenu implements Runnable {

    private boolean running;
    private final Scanner scanner;
    private final ServiceManager manager;

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        this.manager = ServiceManager.getInstance();
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Crear Empresa");
            System.out.println("2. Leer empresa por Id");
            System.out.println("3. Listar todas las empresas");
            System.out.println("4. Actulizar empresa");
            System.out.println("5. Eliminar empresa");
            System.out.println("6. Crear Domicilio Fiscal");
            System.out.println("7. Leer domicilio fiscal por Id");
            System.out.println("8. Listar todos los domicilios fiscales");
            System.out.println("9. Actualizar domicilio fiscal");
            System.out.println("10. Eliminar domicilio fiscal");
            System.out.println("11. Leer empresa por CUIT");
            System.out.println("12. Buscar empresa por razón social");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");

            String input = scanner.nextLine().trim();
            int opcion;

            try {
                opcion = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 1 -> crearEmpresa();
                case 2 -> leerEmpresaPorId();
                case 3 -> listarEmpresas();
                case 4 -> actualizarEmpresa();
                case 5 -> eliminarEmpresa();
                case 6 -> crearDomicilioFiscal();
                case 7 -> leerDomicilioFiscalPorId();
                case 8 -> listarDomiciliosFiscales();
                case 9 -> actualizarDomicilioFiscal();
                case 10 -> eliminarDomicilioFiscal();
                case 11 -> leerEmpresaPorCuit();
                case 12 -> buscarEmpresaPorRazonSocial();
                case 0 -> {
                    System.out.println("\nPrograma finalizado.");
                    running = false;
                }
                default -> System.out.println("Opción no reconocida.");
            }
        }
        scanner.close();
    }

    private void buscarEmpresaPorRazonSocial() {
        System.out.print("\nIngresa la razón social de la empresa: ");
        String razonSocial = scanner.nextLine().trim();

        var empresas = manager.getEmpresaService().buscarPorRazonSocial(razonSocial);

        if (!empresas.isEmpty()) {
            System.out.println("\nEmpresas encontradas:");
            for (Empresa empresa : empresas) {
                System.out.println(empresa);
            }
        }
    }

    // METODOS PARA GESTIÓN DE EMPRESAS

    private void crearEmpresa() {
        System.out.println("\n--- Crear Nueva Empresa ---");

        System.out.print("Razón social: ");
        String razonSocial = scanner.nextLine().trim();

        System.out.print("CUIT (11 dígitos): ");
        String cuit = scanner.nextLine().trim();

        if (cuit.length() != 11) {
            System.out.println("CUIT inválido. Debe tener exactamente 11 dígitos numéricos.");
            while (cuit.length() != 11) {
                System.out.print("Por favor, ingresa un CUIT válido (11 dígitos): ");
                cuit = scanner.nextLine().trim();
            }
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        email = email.isEmpty() ? null : email;

        System.out.print("Actividad principal: ");
        String actividad = scanner.nextLine().trim();
        actividad = actividad.isEmpty() ? null : actividad;

        // Crear domicilio fiscal
        System.out.println("\n--- Datos del Domicilio Fiscal ---");
        System.out.print("Calle: ");
        String calle = scanner.nextLine().trim();

        System.out.print("Número: ");
        String numero = scanner.nextLine().trim();

        System.out.print("Ciudad: ");
        String ciudad = scanner.nextLine().trim();

        System.out.print("Provincia: ");
        String provincia = scanner.nextLine().trim();

        System.out.print("Código postal (opcional): ");
        String codigoPostal = scanner.nextLine().trim();
        codigoPostal = codigoPostal.isEmpty() ? null : codigoPostal;

        System.out.print("País: ");
        String pais = scanner.nextLine().trim();

        // Crear objeto domicilio
        var domicilio = new DomicilioFiscal(calle, numero, ciudad, provincia, codigoPostal, pais);

        // Crear objeto empresa
        var empresa = new Empresa();
        empresa.setRazonSocial(razonSocial);
        empresa.setCuit(cuit);
        empresa.setEmail(email);
        empresa.setActividadPrincipal(actividad);
        empresa.setDomicilioFiscal(domicilio);

        // Guardar empresa
        manager.getEmpresaService().insertar(empresa);
    }

    private void leerEmpresaPorId() {
        System.out.print("\nIngresa el ID de la empresa: ");
        String line = scanner.nextLine().trim();
        long id;
        try {
            id = Long.parseLong(line);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debes ingresar un número entero.");
            return;
        }

        try {
            var empresa = manager.getEmpresaService().getById(id);
            if (empresa != null) {
                System.out.println(empresa);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar la empresa: " + e.getMessage());
        }
    }

    private void leerEmpresaPorCuit() {
        System.out.print("\nIngresa el CUIT de la empresa: ");
        String cuit = scanner.nextLine().trim();

        if (cuit.length() != 11) {
            System.out.println("CUIT inválido. Debe tener exactamente 11 dígitos numéricos.");
            while (cuit.length() != 11) {
                System.out.print("Por favor, ingresa un CUIT válido (11 dígitos): ");
                cuit = scanner.nextLine().trim();
            }
        }

        try {
            var empresa = manager.getEmpresaService().buscarPorCuit(cuit);
            if (empresa != null) {
                System.out.println(empresa);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar la empresa: " + e.getMessage());
        }
    }

    private void listarEmpresas() {
        System.out.println("\n--- Lista de Empresas ---");
        try {
            var empresas = manager.getEmpresaService().getAll();
            if (!empresas.isEmpty()) {
                empresas.forEach(e -> System.out.println("\n" + e));
            }
        } catch (Exception e) {
            System.out.println("Error al listar empresas: " + e.getMessage());
        }
    }

    private void actualizarEmpresa() {
        System.out.println("\n--- Actualizar Empresa ---");

        // Obtener el ID de la empresa a actualizar
        System.out.print("Ingresa el ID de la empresa a actualizar: ");
        String line = scanner.nextLine().trim();
        long id;
        try {
            id = Long.parseLong(line);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debes ingresar un número entero.");
            return;
        }

        // Buscar la empresa existente
        var empresa = manager.getEmpresaService().getById(id);
        if (empresa == null) {
            return;
        }

        System.out.println("\nEmpresa actual: " + empresa);
        System.out.println("\n¿Qué dato desea actualizar?");
        System.out.println("1. Razón social");
        System.out.println("2. CUIT");
        System.out.println("3. Email");
        System.out.println("4. Actividad principal");
        System.out.println("0. Cancelar");
        System.out.print("Selecciona una opción: ");

        String opt = scanner.nextLine().trim();
        int opcion;
        try {
            opcion = Integer.parseInt(opt);
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida.");
            return;
        }

        switch (opcion) {
            case 1 -> {
                System.out.print("Nueva razón social (actual: " + empresa.getRazonSocial() + "): ");
                String nuevaRazonSocial = scanner.nextLine().trim();
                if (!nuevaRazonSocial.isEmpty()) {
                    empresa.setRazonSocial(nuevaRazonSocial);
                }
            }
            case 2 -> {
                System.out.print("Nuevo CUIT (actual: " + empresa.getCuit() + "): ");
                String nuevoCuit = scanner.nextLine().trim();
                if (!nuevoCuit.isEmpty()) {
                    empresa.setCuit(nuevoCuit);
                }
            }
            case 3 -> {
                System.out.print("Nuevo email (actual: " + empresa.getEmail() + "): ");
                String nuevoEmail = scanner.nextLine().trim();
                if (!nuevoEmail.isEmpty()) {
                    empresa.setEmail(nuevoEmail);
                }
            }
            case 4 -> {
                System.out.print("Nueva actividad principal (actual: " + empresa.getActividadPrincipal() + "): ");
                String nuevaActividad = scanner.nextLine().trim();
                if (!nuevaActividad.isEmpty()) {
                    empresa.setActividadPrincipal(nuevaActividad);
                }
            }
            case 0 -> {
                System.out.println("Actualización cancelada.");
                return;
            }
            default -> {
                System.out.println("Opción no reconocida.");
                return;
            }
        }

        // Guardar los cambios
        manager.getEmpresaService().actualizar(empresa);
    }

    private void eliminarEmpresa() {
        System.out.println("\n--- Eliminar Empresa ---");

        // Obtener el ID de la empresa a eliminar
        System.out.print("Ingresa el ID de la empresa a eliminar: ");
        String line = scanner.nextLine().trim();
        long id;
        try {
            id = Long.parseLong(line);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debes ingresar un número entero.");
            return;
        }
        manager.getEmpresaService().eliminar(id);
    }

    // METODOS PARA GESTIÓN DE DOMICILIOS FISCALES

    // crear
    private void crearDomicilioFiscal() {
        // implementar
        System.out.println("\n--- Crear Nuevo Domicilio Fiscal ---");

        System.out.println("Pais:");
        String pais = scanner.nextLine().trim();

        System.out.println("Ciudad:");
        String ciudad = scanner.nextLine().trim();

        System.out.println("Provincia:");
        String provincia = scanner.nextLine().trim();

        System.out.println("Calle:");
        String calle = scanner.nextLine().trim();

        System.out.println("Número:");
        String numero = scanner.nextLine().trim();

        System.out.println("Código Postal:");
        String codigoPostal = scanner.nextLine().trim();

        // Crear objeto domicilio
        var domicilio = new DomicilioFiscal(calle, numero, ciudad, provincia, codigoPostal, pais);

        // Guardar domicilio
        manager.getDomicilioFiscalService().insertar(domicilio);
    }

    // leer por id
    private void leerDomicilioFiscalPorId() {
        System.out.println("\n--- Leer Domicilio Fiscal por ID ---");
        System.out.print("Ingresa el ID del domicilio fiscal: ");
        String line = scanner.nextLine().trim();
        long id;
        try {
            id = Long.parseLong(line);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debes ingresar un número entero.");
            return;
        }

        var domicilio = manager.getDomicilioFiscalService().getById(id);
        if (domicilio != null) {
            System.out.println(domicilio);
        }
    }

    // listar todos
    private void listarDomiciliosFiscales() {
        System.out.println("\n--- Lista de Domicilios Fiscales ---");
            var domicilios = manager.getDomicilioFiscalService().getAll();
            if (!domicilios.isEmpty()) {
                domicilios.forEach(d -> System.out.println("\n" + d));
            }

    }

    // actualizar
    private void actualizarDomicilioFiscal() {
        System.out.println("\n--- Actualizar Domicilio Fiscal ---");
        // Obtener el ID del domicilio a actualizar
        System.out.print("Ingresa el ID del domicilio fiscal a actualizar: ");
        String line = scanner.nextLine().trim();
        long id;
        try {
            id = Long.parseLong(line);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debes ingresar un número entero.");
            return;
        }

        // Buscar el domicilio existente
        var domicilio = manager.getDomicilioFiscalService().getById(id);
        if (domicilio == null) {
            return;
        }

        System.out.println("\nDomicilio fiscal actual: " + domicilio);
        System.out.println("\n¿Qué dato desea actualizar?");
        System.out.println("1. Calle");
        System.out.println("2. Número");
        System.out.println("3. Ciudad");
        System.out.println("4. Provincia");
        System.out.println("5. Código Postal");
        System.out.println("6. País");
        System.out.println("0. Cancelar");
        System.out.print("Selecciona una opción: ");

        String opt = scanner.nextLine().trim();
        int opcion;

        try {
            opcion = Integer.parseInt(opt);
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida.");
            return;
        }

        switch(opcion) {
            case 1 -> {
                System.out.print("Nueva calle (actual: " + domicilio.getCalle() + "): ");
                String nuevaCalle = scanner.nextLine().trim();
                if (!nuevaCalle.isEmpty()) {
                    domicilio.setCalle(nuevaCalle);
                }
            }
            case 2 -> {
                System.out.print("Nuevo número (actual: " + domicilio.getNumero() + "): ");
                String nuevoNumero = scanner.nextLine().trim();
                if (!nuevoNumero.isEmpty()) {
                    domicilio.setNumero(nuevoNumero);
                }
            }
            case 3 -> {
                System.out.print("Nueva ciudad (actual: " + domicilio.getCiudad() + "): ");
                String nuevaCiudad = scanner.nextLine().trim();
                if (!nuevaCiudad.isEmpty()) {
                    domicilio.setCiudad(nuevaCiudad);
                }
            }
            case 4 -> {
                System.out.print("Nueva provincia (actual: " + domicilio.getProvincia() + "): ");
                String nuevaProvincia = scanner.nextLine().trim();
                if (!nuevaProvincia.isEmpty()) {
                    domicilio.setProvincia(nuevaProvincia);
                }
            }
            case 5 -> {
                System.out.print("Nuevo código postal (actual: " + domicilio.getCodigoPostal() + "): ");
                String nuevoCodigoPostal = scanner.nextLine().trim();
                if (!nuevoCodigoPostal.isEmpty()) {
                    domicilio.setCodigoPostal(nuevoCodigoPostal);
                }
            }
            case 6 -> {
                System.out.print("Nuevo país (actual: " + domicilio.getPais() + "): ");
                String nuevoPais = scanner.nextLine().trim();
                if (!nuevoPais.isEmpty()) {
                    domicilio.setPais(nuevoPais);
                }
            }
            case 0 -> System.out.println("Actualización cancelada.");
            default -> System.out.println("Opción no reconocida.");
        }
        manager.getDomicilioFiscalService().actualizar(domicilio);
    }

    // eliminar
    private void eliminarDomicilioFiscal() {
        System.out.println("\n--- Eliminar Domicilio Fiscal ---");

        // Obtener el ID del domicilio a eliminar
        System.out.println("Ingresa el ID del domicilio a eliminar: ");
        String line = scanner.nextLine().trim();
        long id;

        try {
            id = Long.parseLong(line);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Debes ingresar un número entero.");
            return;
        }
        manager.getDomicilioFiscalService().eliminar(id);
    }
}
