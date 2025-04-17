/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import java.util.Scanner;

/**
 * S6 - Entornos de Desarrollo Integrado (IDEs) 
 * @author mvarg
 */
public class Exp2_S6_Miguel_Vargas {

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
            continuar = false; //eliminar despues de completar el menu

            if (scanner.hasNextInt()) {

                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        System.out.println("texto de prueba 1");
                        break;

                    case 2:
                        System.out.println("texto de prueba 2");
                        break;

                    case 3:
                        System.out.println("texto de prueba 3");
                        break;

                    case 4:
                        System.out.println("texto de prueba 4");
                        break;

                    case 5:
                        System.out.println("texto de prueba 5");
                        break;

                    case 6:
                        System.out.println("texto de prueba 6");
                        break;

                    case 7:
                        System.out.println("texto de prueba 7");
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
        
    }

    public static void menu() {
        System.out.println("\n--- Menu principal ---");
        System.out.println("1. Comprar entradas");
        System.out.println("2. Ver asientos disponibles");
        System.out.println("3. Promociones disponibles");
        System.out.println("4. Búsqueda de entradas");
        System.out.println("5. Eliminar ultima compra");
        System.out.println("6. Pagar");
        System.out.println("7. Salir");
        System.out.println("Seleccione una de las opciones disponibles: ");
    }
}
