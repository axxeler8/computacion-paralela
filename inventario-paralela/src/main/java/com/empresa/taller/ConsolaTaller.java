package com.empresa.taller;

import com.empresa.inventario.InventarioService;
import com.empresa.inventario.Repuesto;
import com.empresa.inventario.Reserva;
import com.empresa.inventario.Ubicacion;
import com.empresa.inventario.Vehiculo;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class ConsolaTaller {
    public static void main(String[] args) throws Exception {
        InventarioService svc = (InventarioService) Naming.lookup("rmi://localhost:1099/InventarioService");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Consola Taller ---");
            System.out.println("1) Ver repuestos");
            System.out.println("2) Consultar repuesto por SKU");
            System.out.println("3) Agregar repuesto");
            System.out.println("4) Liberar repuesto");
            System.out.println("5) Ver reservas");
            System.out.println("6) Consultar reserva por ID");
            System.out.println("7) Agregar reserva");
            System.out.println("8) Liberar reserva");
            System.out.println("9) Salir");
            System.out.print("Selecciona una opción: ");
            int op = Integer.parseInt(sc.nextLine());

            switch (op) {
                case 1: mostrarRepuestos(svc); break;
                case 2: consultarRepuestoPorSku(svc, sc); break;
                case 3: agregarRepuesto(svc, sc); break;
                case 4: liberarRepuesto(svc, sc); break;
                case 5: mostrarReservas(svc); break;
                case 6: consultarReservaPorId(svc, sc); break;
                case 7: agregarReserva(svc, sc); break;
                case 8: liberarReserva(svc, sc); break;
                case 9:
                    System.out.println("Saliendo...");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
            }
        }
    }

    private static void mostrarRepuestos(InventarioService svc) throws Exception {
        List<Repuesto> list = svc.verRepuestos();
        System.out.println("\n-- Lista de Repuestos --");
        for (Repuesto r : list) {
            System.out.printf("[SKU %d] %s | Cant: %d | Precio: %d | Cat: %s | Disp: %b%n",
                r.getSku(), r.getNombre(), r.getCantidad(), r.getPrecio(), r.getCategoria(), r.isDisponible());
        }
    }

    private static void consultarRepuestoPorSku(InventarioService svc, Scanner sc) throws Exception {
        Repuesto r = null;
        while (r == null) {
            System.out.print("\nSKU: ");
            int sku;
            try {
                sku = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("SKU inválido. Ingresa un número entero.");
                continue;
            }
            r = svc.consultarRepuesto(sku);
            if (r == null) {
                System.out.println("No existe ese SKU. Intenta nuevamente.");
            }
        }
        System.out.printf(
            "\nDetalles del Repuesto:\n" +
            "SKU: %d%n" +
            "Nombre: %s%n" +
            "Cantidad: %d%n" +
            "Precio: %d%n" +
            "Categoría: %s%n" +
            "Disponible: %b%n",
            r.getSku(), r.getNombre(), r.getCantidad(), r.getPrecio(), r.getCategoria(), r.isDisponible()
        );
    }

    private static void agregarRepuesto(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Agregar Repuesto --");
        int idUb;
        while (true) {
            System.out.print("ID de Ubicación: ");
            idUb = Integer.parseInt(sc.nextLine());
            if (svc.consultarUbicacion(idUb) == null) {
                System.out.println("⚠ Ubicación inexistente. Intenta de nuevo.");
            } else {
                break;
            }
        }
        Ubicacion ub = svc.consultarUbicacion(idUb);

        int sku;
        while (true) {
            System.out.print("SKU: ");
            sku = Integer.parseInt(sc.nextLine());
            if (svc.consultarRepuesto(sku) != null) {
                System.out.println("⚠ SKU ya existe. Intenta otro.");
            } else {
                break;
            }
        }

        int cantidad;
        while (true) {
            System.out.print("Cantidad: ");
            cantidad = Integer.parseInt(sc.nextLine());
            int stockActual = svc.consultarStockUbicacion(idUb);
            if (stockActual + cantidad > ub.getCapacidad()) {
                System.out.printf("⚠ Excede capacidad. Actual: %d, Máxima: %d. Intenta de nuevo.%n", stockActual, ub.getCapacidad());
            } else {
                break;
            }
        }

        System.out.print("Precio: ");
        int precio = Integer.parseInt(sc.nextLine());

        System.out.print("Categoría (texto): ");
        String categoria = sc.nextLine().trim();

        boolean disponible;
        while (true) {
            System.out.print("Disponible (1=si / 0=no): ");
            String in = sc.nextLine().trim();
            if ("1".equals(in)) { disponible = true; break; }
            if ("0".equals(in)) { disponible = false; break; }
            System.out.println("⚠ Responde 1 o 0. Intenta de nuevo.");
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        svc.agregarRepuesto(idUb, sku, cantidad, precio, categoria, disponible, nombre);
        System.out.println("✔ Repuesto agregado.");
    }

    private static void liberarRepuesto(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Liberar Repuesto --");
        System.out.print("ID de Ubicación: ");
        int idUb = Integer.parseInt(sc.nextLine());

        Repuesto rep;
        int sku;
        while (true) {
            System.out.print("SKU: ");
            sku = Integer.parseInt(sc.nextLine());
            rep = svc.consultarRepuestoEnUbicacion(idUb, sku);
            if (rep == null) {
                System.out.println("⚠ No existe el SKU " + sku + " en la ubicación " + idUb + ". Intenta de nuevo.");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Cantidad a liberar (máx " + rep.getCantidad() + "): ");
            int cantidad = Integer.parseInt(sc.nextLine());
            if (cantidad < 1 || cantidad > rep.getCantidad()) {
                System.out.println("⚠ Cantidad inválida. Debe estar entre 1 y " + rep.getCantidad() + ". Intenta de nuevo.");
            } else {
                svc.liberarRepuesto(idUb, sku, cantidad);
                System.out.println("✔ Repuesto liberado.");
                break;
            }
        }
    }

    private static void mostrarReservas(InventarioService svc) throws Exception {
        List<Reserva> lista = svc.verReservas();
        System.out.println("\n-- Lista de Reservas --");
        for (Reserva r : lista) {
            System.out.printf("[Reserva %d] Vehículo ID: %d | SKU: %d | Cantidad: %d%n",
                r.getIdReserva(), r.getIdVehiculo(), r.getSku(), r.getCantidad());
        }
    }

    private static void consultarReservaPorId(InventarioService svc, Scanner sc) throws Exception {
        Reserva r = null;
        while (r == null) {
            System.out.print("\nID de Reserva: ");
            int id;
            try {
                id = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Ingresa un número entero.");
                continue;
            }
            r = svc.consultarReserva(id);
            if (r == null) {
                System.out.println("No existe una reserva con ese ID. Intenta nuevamente.");
            }
        }
        System.out.printf(
            "\nDetalles de la Reserva:\nID Reserva: %d%nVehículo ID: %d%nSKU: %d%nCantidad: %d%n",
            r.getIdReserva(), r.getIdVehiculo(), r.getSku(), r.getCantidad()
        );
    }

    private static void agregarReserva(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Agregar Reserva --");
        int idVeh;
        while (true) {
            System.out.print("ID de Vehículo: ");
            idVeh = Integer.parseInt(sc.nextLine());
            Vehiculo veh = svc.consultarVehiculo(idVeh);
            if (veh == null) {
                System.out.println("⚠ Vehículo no encontrado. Intenta de nuevo.");
            } else {
                break;
            }
        }

        Repuesto rep;
        int sku;
        while (true) {
            System.out.print("SKU: ");
            sku = Integer.parseInt(sc.nextLine());
            rep = svc.consultarRepuesto(sku);
            if (rep == null) {
                System.out.println("⚠ No existe el SKU " + sku + ". Intenta de nuevo.");
            } else if (rep.getCantidad() < 1) {
                System.out.println("⚠ No hay stock disponible para el SKU " + sku + ".");
                return;
            } else {
                break;
            }
        }

        int cantidad;
        while (true) {
            System.out.print("Cantidad a reservar (máx " + rep.getCantidad() + "): ");
            cantidad = Integer.parseInt(sc.nextLine());
            if (cantidad < 1 || cantidad > rep.getCantidad()) {
                System.out.println("⚠ Cantidad inválida. Debe estar entre 1 y " + rep.getCantidad() + ". Intenta de nuevo.");
            } else {
                break;
            }
        }

        svc.agregarReserva(idVeh, sku, cantidad);
        System.out.println("✔ Reserva creada.");
    }

    private static void liberarReserva(InventarioService svc, Scanner sc) throws Exception {
        System.out.println("\n-- Liberar Reserva --");
        System.out.print("ID de Reserva: ");
        int idRes = Integer.parseInt(sc.nextLine());

        svc.liberarReserva(idRes);
        System.out.println("✔ Reserva liberada.");
    }
}
