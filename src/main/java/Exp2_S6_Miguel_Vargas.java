/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * S6 - Entornos de Desarrollo Integrado (IDEs) 
 * @author mvarg
 */
public class Exp2_S6_Miguel_Vargas {

    //variables estáticas
    static int entradaAcumulada = 0;
    static double totalAcumulado = 0;
    static String ultimaEntradaComprada = "No hay entradas compradas aún."; //almacenara los datos de la ultima entrada que se compre
    
    //estas variables servirán para poder eliminar la ultima entrada comprada
    static int ultimaZonaSeleccionada = -1; // Zona de la última compra (1 = VIP, 2 = Normal, 3 = Palco)
    static int ultimaFila = -1;             //fila del asiento
    static int ultimaColumna = -1;          //columna del asiento
    static double precioBaseUltimaEntrada = 0;

    //distribución de asientos por zona
    static char[][] zonaVip = {{'O', 'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O', 'O'}};
    static char[][] zonaNormal = {{'O', 'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O', 'O'}};
    static char[][] zonaPalco = {{'O', 'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O', 'O'}, {'O', 'O', 'O', 'O', 'O', 'O'}};

    //precios base por zona
    static double precioVip = 20000;
    static double precioNormal = 7000;
    static double precioPalco = 12000;
    static double precioFinal;

    //lista para almacenar entradas compradas
    static List<Entrada> entradasCompradas = new ArrayList<>();

    static class Entrada {
        int zonaSeleccionada;
        int fila;
        int columna;
        double precioBase;
        char filaChar;
    
        public Entrada(int zonaSeleccionada, int fila, int columna, double precioBase, char filaChar) {
            this.zonaSeleccionada = zonaSeleccionada;
            this.fila = fila;
            this.columna = columna;
            this.precioBase = precioBase;
            this.filaChar = filaChar;
        }
    
        @Override
        public String toString() {
            String zona = (zonaSeleccionada == 1) ? "VIP" : (zonaSeleccionada == 2) ? "Normal" : "Palco";
            return "Zona: " + zona + ", Asiento: " + filaChar + (columna + 1) + ", Precio: $" + precioBase;
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        //saludo de bienvenida
        System.out.println("---------------------");
        System.out.println("     TEATRO MORO");
        System.out.println("---------------------");
        System.out.println("Bienvenido a nuestro sistema de compra");
        System.out.println("Actualmente tenemos el siguiente show disponible:");
        System.out.println("-- De vuelta a clases con el GOTH --");

        //inicio del menu
        while (continuar) {

            menu();

            if (scanner.hasNextInt()) {

                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        seleccionarEntradas(scanner);
                        break;

                    case 2:
                        System.out.println("\n--- Plano de asientos disponibles ---");
                        System.out.println("--------------");
                        System.out.println("  ESCENARIO");
                        System.out.println("--------------");
                
                        System.out.println("\nZona VIP:");
                        mostrarPlano(zonaVip);
                
                        System.out.println("\nZona Normal:");
                        mostrarPlano(zonaNormal);
                
                        System.out.println("\nZona Palco:");
                        mostrarPlano(zonaPalco);
                        break;

                    case 3:
                        promocionesDisponibles();
                        break;

                    case 4:
                        modificarCompra(scanner);
                        break;

                    case 5:
                        if (!entradasCompradas.isEmpty()) {         //solo permitir pagos si hay entradas acumuladas
                            procesarPago(scanner);
                            continuar = false;
                        } else {
                            System.out.println("No hay compras realizadas. Por favor, compre sus entradas antes de proceder al pago.");
                        }
                        break;

                    case 6:
                        salirMenu(scanner);
                        continuar = false;
                        break;
                
                    default:
                        System.out.println("Opcion invalida");
                        break;
                }

            } else {

                System.out.println("Opcion invalida, por favor intente nuevamente");
                scanner.next();
            }

        }

        scanner.close();
        
    }

    //método para opción 6 - salir
    public static void salirMenu(Scanner scanner) {
        if (entradasCompradas.isEmpty()) {
            System.out.println("\nGracias por usar nuestro sistema. ¡Hasta luego!");
        } else {
            System.out.println("\nTiene compras pendientes de pago.");
            System.out.println("Se le redirigirá automáticamente al menú de pago.\n");
            
            // Invoca directamente el método de pago
            procesarPago(scanner);
            }
        }

    //método para opción 5 - pagar
    public static void procesarPago(Scanner scanner) {
        System.out.println("\nSu compra es de " + entradaAcumulada + " entradas, por un total de $" + totalAcumulado);
        System.out.println("Seleccione el medio de pago:");
        System.out.println("1. Débito\n2. Crédito\n3. Transferencia\n4. Cancelar compra");
        
        int confirmaCompra;
        do {
            confirmaCompra = scanner.nextInt();
        } while (confirmaCompra < 1 || confirmaCompra > 4);
    
        switch (confirmaCompra) {
            case 1:
                generarBoleta();
                System.out.println("Pago con tarjeta de débito. Procesando...");
                confirmarCompra(scanner, "Débito");
                break;

            case 2:
                generarBoleta();
                System.out.println("Pago con tarjeta de crédito.");
                System.out.println("Indique la cantidad de cuotas (1 a 12 cuotas): ");
                int cuotas = scanner.nextInt();

                if (cuotas < 1 || cuotas > 12) {
                    System.out.println("Numero de cuotas seleccionado invalido");
                } else {
                    System.out.println("Tu compra sera cargada en tu tarjeta en " + cuotas + " cuotas");
                }
                confirmarCompra(scanner, "Crédito");
                break;

            case 3:
            generarBoleta();
                System.out.println("Pago mediante transferencia.");
                System.out.println("Recuerda que recibirás las instrucciones para la transferencia en tu correo");
                confirmarCompra(scanner, "Transferencia");
                break;
            
            case 4:
                System.out.println("Compra cancelada. Vuelve pronto.");
                System.exit(0); //finaliza el programa de forma automática 
                break;
        }
    }

    public static double obtenerUltimoPrecio() {
        
        return precioBaseUltimaEntrada; 
    }

    //método de código opción 6 - pago
    public static void confirmarCompra(Scanner scanner, String metodoPago) {
        System.out.println("\nHas elegido el método de pago: " + metodoPago);
        System.out.println("Estamos procesando la compra...");
        
        try {
            Thread.sleep(2000);         //simulación de espera durante el procesamiento (2 segundos)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("¡Compra confirmada!");
        System.out.print("Para finalizar, ingrese su correo: ");
        scanner.nextLine();         //limpiar entrada
        String correo = scanner.nextLine();
        
        System.out.println("\nSu boleta y entradas serán enviadas al correo " + correo);
        System.out.println("Gracias por usar nuestro sistema. ¡Hasta luego!");

    }

    //método para opción 4 - modificar compra
    public static void modificarCompra(Scanner scanner) {
        System.out.println("\n--- Modificar Compra ---");
        System.out.println("1. Cambiar una entrada");
        System.out.println("2. Eliminar una entrada");
        System.out.println("3. Agregar más entradas");
        System.out.println("4. Regresar al menú principal");
        System.out.print("Seleccione una opción: ");
    
        int opcion = scanner.nextInt();
    
        switch (opcion) {
           
            case 1: 
                cambiarEntrada(scanner); 
                break;
            
            case 2: 
                eliminarEntrada(scanner); 
                break;
           
            case 3: 
                agregarEntradas(scanner); 
                break;
           
            case 4:
                return; //regresa al menú principal
            
            default: System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    //método para cambio de entradas
    public static void cambiarEntrada(Scanner scanner) {
        if (entradasCompradas.isEmpty()) {
            System.out.println("No hay entradas compradas para cambiar.");
            return;
        }
    
        //mostrar todas las entradas compradas
        System.out.println("\n--- Entradas Compradas ---");
        for (int i = 0; i < entradasCompradas.size(); i++) {
            System.out.println((i + 1) + ". " + entradasCompradas.get(i));
        }
    
        System.out.print("\nSeleccione el número de la entrada que desea cambiar: ");
        int indice = scanner.nextInt() - 1;
    
        if (indice >= 0 && indice < entradasCompradas.size()) {
            Entrada entrada = entradasCompradas.get(indice);
            char[][] antiguaZona = (entrada.zonaSeleccionada == 1) ? zonaVip : (entrada.zonaSeleccionada == 2) ? zonaNormal : zonaPalco;
            antiguaZona[entrada.fila][entrada.columna] = 'O'; //liberar asiento ocupado
    
            //solicitar nueva zona, fila y columna
            System.out.println("\nSeleccione una nueva zona (1. VIP / 2. Normal / 3. Palco):");
            int nuevaZona = scanner.nextInt();
            char[][] nuevaZonaActual = (nuevaZona == 1) ? zonaVip : (nuevaZona == 2) ? zonaNormal : zonaPalco;
            double nuevoPrecioBase = (nuevaZona == 1) ? 20000 : (nuevaZona == 2) ? 7000 : 12000;
    
            System.out.print("Seleccione nueva fila (A, B, C...): ");
            char nuevaFilaChar = scanner.next().toUpperCase().charAt(0);
            int nuevaFila = nuevaFilaChar - 'A';
    
            System.out.print("Seleccione nueva columna (1, 2, 3...): ");
            int nuevaColumna = scanner.nextInt() - 1;
    
            if (nuevaFila >= 0 && nuevaFila < nuevaZonaActual.length && nuevaColumna >= 0 && nuevaColumna < nuevaZonaActual[0].length) {
                if (nuevaZonaActual[nuevaFila][nuevaColumna] == 'O') {
                    nuevaZonaActual[nuevaFila][nuevaColumna] = 'X'; //reservar nuevo asiento
    
                    //actualizar entrada
                    entrada.zonaSeleccionada = nuevaZona;
                    entrada.fila = nuevaFila;
                    entrada.columna = nuevaColumna;
                    entrada.precioBase = nuevoPrecioBase;
                    entrada.filaChar = nuevaFilaChar;
    
                    System.out.println("Entrada modificada exitosamente.");
                } else {
                    System.out.println("El asiento seleccionado ya está ocupado.");
                }
            } else {
                System.out.println("Selección inválida.");
            }
        } else {
            System.out.println("Selección inválida.");
        }
    }

    //método para eliminación de entradas
    public static void eliminarEntrada(Scanner scanner) {
        if (entradasCompradas.isEmpty()) {
            System.out.println("No hay entradas compradas para eliminar.");
            return;
        }
    
        //mostrar todas las entradas compradas
        System.out.println("\n--- Entradas Compradas ---");
        for (int i = 0; i < entradasCompradas.size(); i++) {
            System.out.println((i + 1) + ". " + entradasCompradas.get(i));
        }
    
        System.out.print("\nSeleccione el número de la entrada que desea eliminar: ");
        int indice = scanner.nextInt() - 1;
    
        if (indice >= 0 && indice < entradasCompradas.size()) {
            Entrada entrada = entradasCompradas.get(indice);
            char[][] zonaActual = (entrada.zonaSeleccionada == 1) ? zonaVip : (entrada.zonaSeleccionada == 2) ? zonaNormal : zonaPalco;
            zonaActual[entrada.fila][entrada.columna] = 'O'; //liberar asiento ocupado
            totalAcumulado -= entrada.precioBase; //ajustar total acumulado
    
            entradasCompradas.remove(indice); //eliminar entrada de la lista
            System.out.println("Entrada eliminada exitosamente.");
        } else {
            System.out.println("Selección inválida.");
        }
    }

    public static void agregarEntradas(Scanner scanner) {
        if (entradasCompradas.size() >= 5) {
            System.out.println("Ya ha alcanzado el límite de 5 entradas por compra.");
            return;
        }
    
        System.out.println("¿Cuántas entradas desea agregar? (Máximo " + (5 - entradasCompradas.size()) + ")");
        int cantidad = scanner.nextInt();
    
        if (cantidad > 0 && (entradasCompradas.size() + cantidad) <= 5) {
            for (int i = 0; i < cantidad; i++) {
                System.out.println("\n--- Entrada #" + (entradasCompradas.size() + 1) + " ---");
    
                boolean compraExitosa = false;
                while (!compraExitosa) {
                    System.out.println("\nEntradas disponibles:");
                    System.out.println("   Entrada - Precio");
                    System.out.println("--------------------");
                    System.out.println("1. VIP       $20.000");
                    System.out.println("2. Normal    $ 7.000");
                    System.out.println("3. Palco     $12.000");
                    System.out.println("--------------------");
                    System.out.print("Seleccione una zona: ");
    
                    int zonaSeleccionada = scanner.nextInt();
                    char[][] zonaActual = (zonaSeleccionada == 1) ? zonaVip : (zonaSeleccionada == 2) ? zonaNormal : zonaPalco;
                    double precioBase = (zonaSeleccionada == 1) ? 20000 : (zonaSeleccionada == 2) ? 7000 : 12000;
    
                    System.out.print("Seleccione fila (A, B, C...): ");
                    char filaChar = scanner.next().toUpperCase().charAt(0);
                    int fila = filaChar - 'A';
    
                    System.out.print("Seleccione columna (1, 2, 3...): ");
                    int columna = scanner.nextInt() - 1;
    
                    if (fila >= 0 && fila < zonaActual.length && columna >= 0 && columna < zonaActual[0].length) {
                        if (zonaActual[fila][columna] == 'O') {
                            zonaActual[fila][columna] = 'X'; //reservar asiento
    
                            //crear y añadir entrada
                            Entrada nuevaEntrada = new Entrada(zonaSeleccionada, fila, columna, precioBase, filaChar);
                            entradasCompradas.add(nuevaEntrada);
                            totalAcumulado += precioBase; //actualizar total acumulado
                            compraExitosa = true;
                            System.out.println("Entrada agregada exitosamente.");
                        } else {
                            System.out.println("El asiento ya está ocupado.");
                        }
                    } else {
                        System.out.println("Selección inválida.");
                    }
                }
            }
        } else {
            System.out.println("Cantidad inválida. Intente nuevamente.");
        }
    }

    public static void menu() {
        System.out.println("\n--- Menu principal ---");
        System.out.println("1. Selección entradas entradas");
        System.out.println("2. Ver asientos disponibles");
        System.out.println("3. Promociones disponibles");
        System.out.println("4. Modificar compra");
        System.out.println("5. Pagar");
        System.out.println("6. Salir");
        System.out.println("Seleccione una de las opciones disponibles: ");
    }

    public static void mostrarDetalleCompra() {
        if (entradasCompradas.isEmpty()) {
            System.out.println("\nNo hay entradas compradas.");
            return;
        }
    
        System.out.println("\n--- Detalle de la Compra ---");
        double total = 0;
    
        for (int i = 0; i < entradasCompradas.size(); i++) {
            Entrada entrada = entradasCompradas.get(i);
            String zona = (entrada.zonaSeleccionada == 1) ? "VIP" : (entrada.zonaSeleccionada == 2) ? "Normal" : "Palco";
    
            System.out.println("Entrada #" + (i + 1));
            System.out.println("Zona: " + zona);
            System.out.println("Asiento: " + entrada.filaChar + (entrada.columna + 1));
            System.out.println("Precio: $" + entrada.precioBase);
            System.out.println("---------------------");
    
            total += entrada.precioBase; //sumar al total
        }
    
        System.out.println("\nTOTAL DE LA COMPRA: $" + total);
    }

    //método para la opción 1 - seleccionar entradas
    public static void seleccionarEntradas(Scanner scanner) {
        System.out.println("¿Cuántas entradas desea comprar? (Máximo 5)");
        int cantidad = scanner.nextInt();
    
        if (cantidad > 0 && cantidad <= 5) {
            for (int i = 0; i < cantidad; i++) {
                System.out.println("\n--- Entrada #" + (i + 1) + " ---");
    
                boolean compraExitosa = false;
        
                while (!compraExitosa) {
                    System.out.println("\nEntradas disponibles:");
                    System.out.println("   Entrada - Precio");
                    System.out.println("--------------------");
                    System.out.println("1. VIP       $20.000");
                    System.out.println("2. Normal    $ 7.000");
                    System.out.println("3. Palco     $12.000");
                    System.out.println("--------------------");
                    System.out.print("Seleccione una zona: ");
        
                    int zonaSeleccionada = scanner.nextInt();
                    char[][] zonaActual = null;
                    double precioBase = 0;
        
                    if (zonaSeleccionada == 1) {
                        zonaActual = zonaVip;
                        precioBase = precioVip;
                        System.out.println("\nEntrada VIP seleccionada.");
                    } else if (zonaSeleccionada == 2) {
                        zonaActual = zonaNormal;
                        precioBase = precioNormal;
                        System.out.println("\nEntrada normal seleccionada.");
                    } else if (zonaSeleccionada == 3) {
                        zonaActual = zonaPalco;
                        precioBase = precioPalco;
                        System.out.println("\nEntrada palco seleccionada.");
                    } else {
                        System.out.println("Selección invalida. Intente nuevamente.");
                        continue;
                    }
        
                    // Mostrar el plano de la zona seleccionada con filas y columnas etiquetadas
                    mostrarPlano(zonaActual);
        
                    //solicitar la fila y columna del asiento
                    System.out.print("Seleccione fila (A, B, C...): ");
                    char filaChar = scanner.next().toUpperCase().charAt(0);
                    int fila = filaChar - 'A';          //convertir letra a índice
        
                    System.out.print("Seleccione columna (1, 2, 3...): ");
                    int columna = scanner.nextInt() - 1;            //convertir entrada a índice
        
                    if (fila >= 0 && fila < zonaActual.length && columna >= 0 && columna < zonaActual[0].length) {
                        if (zonaActual[fila][columna] == 'O') {
                            zonaActual[fila][columna] = 'X';            //marcar asiento como ocupado
                            System.out.println("Asiento reservado exitosamente.");
        
                            //solicitar la edad para calcular descuento
                            System.out.print("Ingrese su edad: ");
                            if (scanner.hasNextInt()) {
                                int edad = scanner.nextInt();
                                double descuento = 0;
        
                                if (edad >= 60) {
                                    descuento = 0.15;
                                    System.out.println("Se aplicará un descuento del 15%.");
                                } else if (edad >= 18 && edad <= 25) {
                                    descuento = 0.10;
                                    System.out.println("Se aplicará un descuento del 10%.");
                                } else {
                                    System.out.println("No hay descuentos disponibles actualmente.");
                                }
        
                                //calcular precio final
                                precioFinal = precioBase * (1 - descuento);

                                //crear y añadir entrada a la lista
                                Entrada nuevaEntrada = new Entrada(zonaSeleccionada, fila, columna, precioFinal, filaChar);
                                entradasCompradas.add(nuevaEntrada);

                                totalAcumulado += precioFinal; // actualizar el total acumulado
                                compraExitosa = true;

                            } else {
                                System.out.println("Edad inválida. Intente nuevamente.");
                                scanner.next();         //limpiar entrada no válida
                            }
                        } else {
                            System.out.println("El asiento ya está ocupado. Intente nuevamente.");
                        }
                    } else {
                        System.out.println("Selección inválida. Intente nuevamente.");
                    }
                }
    
                System.out.println("Entrada #" + (i + 1) + " reservada exitosamente.");
            }
    
            //mostrar el detalle completo al finalizar la selección
            mostrarDetalleCompra();

        } else {
            System.out.println("Cantidad inválida. Intente nuevamente.");
        }
    }

    //método para la opción 2 - ver asientos disponibles
    public static void mostrarPlano(char[][] zona) {
        System.out.print("  ");                       //espacio inicial para alinear columnas
        for (int col = 0; col < zona[0].length; col++) {
            System.out.print((col + 1) + " ");          //etiquetas de columnas (1, 2, 3...)
        }
        System.out.println();
    
        for (int fila = 0; fila < zona.length; fila++) {
            System.out.print((char) ('A' + fila) + " ");    //etiquetas de filas (A, B, C...)
            for (int col = 0; col < zona[fila].length; col++) {
                System.out.print(zona[fila][col] + " ");
            }
            System.out.println();
        }
    }

    //método para la opción 3 - promociones
    static void promocionesDisponibles() {
        System.out.println("\n--- Promociones Disponibles ---");
        System.out.println("- 10% de descuento para estudiantes.");
        System.out.println("- 15% de descuento para personas de la tercera edad.");
    }

    //método para generar boleta de compra
    public static void generarBoleta() {
       
        System.out.println("\n--------------- BOLETA ---------------");
        System.out.println("             TEATRO MORO");
        System.out.println(" SHOW: De vuelta a clases con el GOTH");
        System.out.println("--------------------------------------");
    
        double totalNeto = 0;
        double totalIVA = 0;
        double totalFinal = 0;
    
        for (int i = 0; i < entradasCompradas.size(); i++) {
            Entrada entrada = entradasCompradas.get(i);
            String zona = (entrada.zonaSeleccionada == 1) ? "VIP" : (entrada.zonaSeleccionada == 2) ? "Normal" : "Palco";
    
            double ivaPorEntrada = entrada.precioBase * 0.19;
            double precioNetoEntrada = entrada.precioBase - ivaPorEntrada;
    
            System.out.println("Entrada #" + (i + 1));
            System.out.println("Zona         : " + zona);
            System.out.println("Asiento      : " + entrada.filaChar + (entrada.columna + 1));
            System.out.println("Precio       : $" + entrada.precioBase);
            System.out.println("--------------------------------------");
    
            totalNeto += precioNetoEntrada;
            totalIVA += ivaPorEntrada;
            totalFinal += entrada.precioBase;
        }
    
        System.out.println("-------- RESUMEN DE LA COMPRA --------");
        System.out.println("Subtotal (Neto): $" + totalNeto);
        System.out.println("IVA (19%)      : $" + totalIVA);
        System.out.println("TOTAL          : $" + totalFinal);
        System.out.println("--------------------------------------");
        System.out.println("¡Gracias por tu compra!");
    }

}
