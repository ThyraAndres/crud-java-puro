package main.com.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.com.project.controlador.ArchivoBinarioGestor;
import main.com.project.controlador.EquipoGestor;
import main.com.project.controlador.UsuarioGestor;
import main.com.project.db.ArchivoDb;
import main.com.project.db.EquipoDb;
import main.com.project.db.UsuarioDb;
import main.com.project.entidad.ArchivoBinario;
import main.com.project.entidad.Equipo;
import main.com.project.entidad.Usuario;

public class Main {
    private static final String CARPETA_ARCHIVOS = "src/main/resources/archivos/";

    public static void main(String[] args) {
        // 1. Cargar usuarios existentes de MySQL al ArrayList
        UsuarioDb.cargarUsuarios();
        EquipoDb.cargarEquipos();

        // 2. Crear nuevo usuario (se guarda en ArrayList y MySQL)
        Usuario nuevoUsuario = new Usuario("Marias Garcias", "marias@example.com",
                "123");
        UsuarioGestor.crearUsuario(nuevoUsuario);
        UsuarioDb.guardarUsuario(nuevoUsuario);

        // 3. Crear nuevo Equipo
        // 1. CREATE - Crear nuevo equipo
        System.out.println("\n--- CREAR NUEVO EQUIPO ---");
        Equipo nuevoEquipo = new Equipo("iPhone Pro Max 16", "Nueva tecnología");
        EquipoGestor.crearEquipo(nuevoEquipo);
        EquipoDb.guardarEquipo(nuevoEquipo);
        System.out.println("Equipo creado: " + nuevoEquipo);

        // 2. READ - Leer/Buscar equipos
        System.out.println("\n--- BUSCAR EQUIPOS ---");
        // Buscar por ID (usando el ID del equipo recién creado)
        Equipo equipoEncontrado = EquipoGestor.buscarPorId(nuevoEquipo.getId());
        if (equipoEncontrado != null) {
            System.out.println("Equipo encontrado: " + equipoEncontrado);
        } else {
            System.out.println("Equipo no encontrado");
        }

        // 3. UPDATE - Actualizar equipo
        System.out.println("\n--- ACTUALIZAR EQUIPO ---");
        if (equipoEncontrado != null) {
            equipoEncontrado.setNombre("iPhone Pro Max 16 (2024)");
            equipoEncontrado.setDescripcion("Última tecnología 2024");
            EquipoDb.actualizarEquipo(equipoEncontrado);
            System.out.println("Equipo actualizado: " + equipoEncontrado);
        }

        // 4. DELETE - Eliminar equipo
        System.out.println("\n--- ELIMINAR EQUIPO ---");
        if (equipoEncontrado != null) {
            int idAEliminar = equipoEncontrado.getId();
            EquipoGestor.eliminarEquipo(idAEliminar);
            EquipoDb.eliminarEquipo(idAEliminar);
            System.out.println("Equipo con ID " + idAEliminar + " eliminado");

            // Verificar que se eliminó
            Equipo equipoEliminado = EquipoGestor.buscarPorId(idAEliminar);
            if (equipoEliminado == null) {
                System.out.println("Confirmación: El equipo ya no existe");
            }
        }

        // 4. Crear nuevo archivo
        try {
            // 1. Definir el archivo a guardar
            // String nombreArchivo = "Minuta.jpg";
            // String tipoArchivo = "image/jpg";
            String nombreArchivo = "Book.xlsx";
            String tipoArchivo = "excel/xlsx";
            String rutaCompleta = CARPETA_ARCHIVOS + "documentos/" + nombreArchivo;

            // 2. Leer el archivo del sistema de archivos
            Path path = Paths.get(rutaCompleta);
            byte[] contenido = Files.readAllBytes(path);

            // 3. Crear el objeto ArchivoBinario
            ArchivoBinario archivo = new ArchivoBinario(nombreArchivo, tipoArchivo, contenido);

            // 4. Guardar en la base de datos
            ArchivoDb.guardarArchivoBinario(archivo);

            // 5. Guardar en el gestor en memoria
            ArchivoBinarioGestor.crearArchivo(archivo);

            System.out.println("Archivo guardado exitosamente con ID: " + archivo.getId());

        } catch (IOException e) {
            System.err.println("Error al procesar el archivo:");
            e.printStackTrace();
        }
    }
}