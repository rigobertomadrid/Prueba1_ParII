package prueba1_parii;

import java.io.IOException;
import java.util.Scanner;

public class ControlEmpleado {

    public static void main(String[] args) {
        EmpleadoManager emp_m = new EmpleadoManager();
        Scanner lea = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("**** MENU ****");
            System.out.println("1- Agregar empleado");
            System.out.println("2- Listar empleados no despedidos");
            System.out.println("3- Despedir empleado");
            System.out.println("4- Buscar empleado activo");
            System.out.println("5- Salir");
            System.out.print("Seleccione una opcion: ");
            try {
                opcion = Integer.parseInt(lea.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error. Intente de nuevo pero con los valores correctos.");
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre del empleado: ");
                    String nombre = lea.nextLine();
                    System.out.print("Ingrese el salario del empleado: ");
                    double salario = 0;
                    try {
                        salario = Double.parseDouble(lea.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Salario no valido");
                        break;
                    }
                    try {
                        emp_m.addEmployee(nombre, salario);
                        System.out.println("Empleado agregado exitosamente");
                    } catch (IOException e) {
                        System.out.println("Error al agregar empleado");
                    }
                    break;
                case 2:
                    try {
                        emp_m.listaEmpleados();
                    } catch (IOException e) {
                        System.out.println("Error al listar los empleados");
                    }
                    break;
                case 3:
                    System.out.print("Ingrese el codigo del empleado a despedir: ");
                    int codigoDespido = 0;
                    try {
                        codigoDespido = Integer.parseInt(lea.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El cosdigo no es correcto");
                        break;
                    }
                    try {
                        if (emp_m.despedirEmpleado(codigoDespido)) {
                            System.out.println("El empleado fue despedido exitosamente");
                        } else {
                            System.out.println("El empleado no se encontro o fue despedido");
                        }
                    } catch (IOException e) {
                        System.out.println("Error al despedir empleado");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese el codigo del empleado a buscar: ");
                    int buscar_codigo = 0;
                    try {
                        buscar_codigo = Integer.parseInt(lea.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("El codigo no es correcto");
                        break;
                    }
                    try {
                        if (emp_m.empleadoActivo(buscar_codigo)) {
                            emp_m.mostrarEmpleado(buscar_codigo);
                        } else {
                            System.out.println("El empleado no se encontro o fue despedido");
                        }
                    } catch (IOException e) {
                        System.out.println("Error al buscar este empleado");
                    }
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Ingrese una opcion valida");
                    break;
            }
        } while (opcion != 5);
    }
}
