package prueba1_parii;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoManager {
    private RandomAccessFile rcods,remps;
    
    public EmpleadoManager(){
        try{
            //1-Asegurar que el folder de company exista
            File mf=new File("company");
            mf.mkdir();
            //2-Instanciar los RAFs dentro del folder de company
            rcods=new RandomAccessFile("company/codigos.emp","rw");
            remps=new RandomAccessFile("company/empleados.emp","rw");
            //3- Inicializar el archivo de codigos, sí, es nuevo.
            initCodes();
        }catch(IOException e){
            System.out.println("Error");
        }
    }
    /*
    Formato de Codigos.emp
    int code;
    */
    private void initCodes()throws IOException{
        //Cotegar el tamaño del archivo
        if(rcods.length()==0)
            rcods.writeInt(1);
    }
    
    //Crear la funcion getCode, para generar el codigo siguiente e indicarme cual es el codigo actual
    private int getCode()throws IOException{
        //Leer el archivo
        rcods.seek(0);
        int codigo=rcods.readInt();
        rcods.seek(0);
        rcods.writeInt(codigo+1);
        return codigo;
    }
    
    /*
    Formato de emplado.emp
    int code
    String name
    double salary
    long fechaContratacion
    long fechaDespido
    */
    
    public void addEmployee(String name, double monto)throws IOException{
        remps.seek(remps.length());
        //Code
        int code=getCode();
        remps.writeInt(code);
        //Name
        remps.writeUTF(name);
        //Salario
        remps.writeDouble(monto);
        //Fecha Contratacion
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        //Fecha Despido
        remps.writeLong(0);
        //Crear carpeta y archivos individual de cada empleado
        createEmployeeFolders(code);
    }
    private String employeeFolder(int code){
        return "company/empleado"+code;
    }
    
    private void createEmployeeFolders(int code)throws IOException{
        File edir=new File(employeeFolder(code));
        edir.mkdir();
        //Crear archivo del empleado
        this.createYearSalesFileFor(code);
    }
    
    private RandomAccessFile salesFileFor(int code)throws IOException{
        String dirPadre=employeeFolder(code);
        int yearActual=Calendar.getInstance().get(Calendar.YEAR);
        String path=dirPadre+"/ventas"+yearActual+".emp";
        
        return new RandomAccessFile(path,"rw");
    }
    /*
    Formato VentasYear.emp
    double ventas
    boolean estadoPagar
    */
    private void createYearSalesFileFor(int code)throws IOException{
        RandomAccessFile raf=salesFileFor(code);
        if(raf.length()==0){
            for(int mes=0;mes<12;mes++){
                raf.writeDouble(0);
                raf.writeBoolean(false);
            }
        }
    }
    
    public void listaEmpleados() throws IOException {
        remps.seek(0);
        System.out.println("**** LISTA DE EMPLEADOS ****");
        int numEmpleado = 1;
        while (remps.getFilePointer() < remps.length()) {
            int codigo = remps.readInt();
            String nombre = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();
            if (fechaDespido == 0) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(fechaContratacion);
                int dia = cal.get(Calendar.DAY_OF_MONTH);
                int mes = cal.get(Calendar.MONTH) + 1; 
                int anio = cal.get(Calendar.YEAR);
                String fechaContratacionStr = String.format("%02d/%02d/%04d", dia, mes, anio);
                
                System.out.println(numEmpleado + ". Codigo: " + codigo + " - Nombre: " + nombre + " - Salario: Lps " + salario + " - Fecha Contratacion: " + fechaContratacionStr);
                numEmpleado++;
            }
        }
    }

    public boolean empleadoActivo(int code) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int codigo = remps.readInt();
            String nombre = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();
            if (codigo == code) {
                if (fechaDespido == 0) {
                    remps.seek(remps.getFilePointer() - 24); 
                    return true;
                }
                return false; 
            }
        }
        return false; 
    }

    public boolean despedirEmpleado(int code) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int codigo = remps.readInt();
            String name = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();
            if (codigo == code) {
                if (fechaDespido == 0) {
                    remps.seek(remps.getFilePointer() - 8); 
                    remps.writeLong(Calendar.getInstance().getTimeInMillis());
                    System.out.println("Empleado despedido: " + name);
                    return true;
                }
                return false;
            }
        }
        return false; 
    }

    public void mostrarEmpleado(int code) throws IOException {
        remps.seek(0);
        while (remps.getFilePointer() < remps.length()) {
            int codigo = remps.readInt();
            String nombre = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();
            if (codigo == code && fechaDespido == 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(fechaContratacion);
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH) + 1;
                int anio = calendar.get(Calendar.YEAR);
                String fechaContratacionStr = String.format("%02d/%02d/%04d", dia, mes, anio);
                
                System.out.println("Codigo: " + codigo + " - Nombre: " + nombre + " - Salario: Lps " + salario + " - Fecha Contratacion: " + fechaContratacionStr);
                return;
            }
        }
        System.out.println("El empleado no se encontro o fue despedido.");
    }
}