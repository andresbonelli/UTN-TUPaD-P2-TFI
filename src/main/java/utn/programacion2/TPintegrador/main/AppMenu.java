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
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");

            String input = scanner.nextLine().trim();
            int opcion = -1;

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
                case 0 -> {
                    System.out.println("\nPrograma finalizado.");
                    running = false;
                }
                default -> System.out.println("Opción no reconocida.");
            }
        }
        scanner.close();
    }

    private void crearEmpresa() {
        System.out.println("\n--- Crear Nueva Empresa ---");

        System.out.print("Razón social: ");
        String razonSocial = scanner.nextLine().trim();

        System.out.print("CUIT (11 dígitos): ");
        String cuit = scanner.nextLine().trim();

        System.out.print("Email (opcional): ");
        String email = scanner.nextLine().trim();
        email = email.isEmpty() ? null : email;

        System.out.print("Actividad principal (opcional): ");
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
            if (empresa == null) {
                System.out.println("Empresa con ID " + id + " no encontrada.");
            } else {
                System.out.println("\nEmpresa encontrada:");
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
            if (empresas == null || empresas.isEmpty()) {
                System.out.println("No hay empresas registradas.");
            } else {
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

        try {
            // Buscar la empresa existente
            var empresa = manager.getEmpresaService().getById(id);
            if (empresa == null) {
                System.out.println("Empresa con ID " + id + " no encontrada.");
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
            System.out.println("Empresa actualizada exitosamente.");

        } catch (Exception e) {
            System.out.println("Error al actualizar empresa: " + e.getMessage());
        }
    }
}
