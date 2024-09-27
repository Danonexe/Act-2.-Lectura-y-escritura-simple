import java.nio.file.Path
import java.io.File
import java.io.BufferedReader
import java.io.FileReader

fun leerCalificaciones(ficheroCalificaciones: Path): MutableMap<String, Map<String, String>> {
    val datos: MutableMap<String, Map<String, String>> = mutableMapOf()

    val br = BufferedReader(FileReader(ficheroCalificaciones.toFile()))
    br.use { reader ->

        reader.readLine()

        reader.forEachLine { linea ->

            val columnas = linea.split(";")

            val apellidos = columnas[0]
            val nombre = columnas[1]
            val asistencia = columnas[2]
            val parcial1 = columnas[3]
            val parcial2 = columnas[4]
            val ordinario1 = columnas[5]
            val ordinario2 = columnas[6]
            val practicas = columnas[7]
            val ordinarioPracticas = columnas[8]

            // Crear un mapa con los datos del estudiante
            val datosEstudiante = mapOf(
                "Nombre" to nombre,
                "Asistencia" to asistencia,
                "Parcial1" to parcial1,
                "Parcial2" to parcial2,
                "Ordinario1" to ordinario1,
                "Ordinario2" to ordinario2,
                "Practicas" to practicas,
                "OrdinarioPracticas" to ordinarioPracticas
            )

            datos[apellidos] = datosEstudiante
        }
    }

    return datos
}

fun calcularNotaFinal(estudiantes: MutableMap<String, Map<String, String>>): MutableMap<String, Map<String, String>> {
    val estudiantesConNotasFinales: MutableMap<String, Map<String, String>> = mutableMapOf()

    for ((apellidos, datosEstudiante) in estudiantes) {

        // Obtenemos los valores necesarios del diccionario si es ordinario y no hay nada lo vuelve nulo
        val parcial1 = datosEstudiante["Parcial1"]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
        val parcial2 = datosEstudiante["Parcial2"]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
        val ordinario1 = datosEstudiante["Ordinario1"]?.replace(",", ".")?.toDoubleOrNull()
        val ordinario2 = datosEstudiante["Ordinario2"]?.replace(",", ".")?.toDoubleOrNull()
        val practicas = datosEstudiante["Practicas"]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
        val ordinarioPracticas = datosEstudiante["OrdinarioPracticas"]?.replace(",", ".")?.toDoubleOrNull()

        // Si no hay ordinario, usamos la nota del parcial
        val notaTeoria1 = ordinario1 ?: parcial1
        val notaTeoria2 = ordinario2 ?: parcial2
        val notaPracticas = ordinarioPracticas ?: practicas

        // Cálculo de la nota final
        val notaFinal = (notaTeoria1 * 0.30) + (notaTeoria2 * 0.30) + (notaPracticas * 0.40)

        // Creamos un nuevo mapa con los mismos datos del estudiante y agregamos la nota final
        val nuevoMapaEstudiante = datosEstudiante.toMutableMap()
        nuevoMapaEstudiante["NotaFinal"] = String.format("%.2f", notaFinal)

        // Añadimos el nuevo diccionario al resultado
        estudiantesConNotasFinales[apellidos] = nuevoMapaEstudiante
    }

    return estudiantesConNotasFinales
}

fun aprobadoSuspenso(estudiantes: MutableMap<String, Map<String, String>>): Pair<List<Map<String, String>>, List<Map<String, String>>> {
    val aprobados = mutableListOf<Map<String, String>>()//he tenido que buscarlo
    val suspensos = mutableListOf<Map<String, String>>()

    // Recorremos el mapa de estudiantes
    for ((_, estudiante) in estudiantes) {
        val asistencia = estudiante["Asistencia"]?.replace("%", "")?.toDoubleOrNull() ?: 0.0
        val parcial1 = estudiante["Parcial1"]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
        val parcial2 = estudiante["Parcial2"]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
        val ordinario1 = estudiante["Ordinario1"]?.replace(",", ".")?.toDoubleOrNull()
        val ordinario2 = estudiante["Ordinario2"]?.replace(",", ".")?.toDoubleOrNull()
        val practicas = estudiante["Practicas"]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
        val ordinarioPracticas = estudiante["OrdinarioPracticas"]?.replace(",", ".")?.toDoubleOrNull()
        val notaFinal = estudiante["NotaFinal"]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0

        // Reglas para aprobar
        val notaTeoria1 = ordinario1 ?: parcial1
        val notaTeoria2 = ordinario2 ?: parcial2
        val notaPracticas = ordinarioPracticas ?: practicas

        val cumpleAsistencia = asistencia >= 75
        val cumpleNotasTeoria = notaTeoria1 >= 4 && notaTeoria2 >= 4
        val cumpleNotaPracticas = notaPracticas >= 4
        val cumpleNotaFinal = notaFinal >= 5

        // si cumple con todas las condiciones
        if (cumpleAsistencia && cumpleNotasTeoria && cumpleNotaPracticas && cumpleNotaFinal) {
            aprobados.add(estudiante)
        } else {
            suspensos.add(estudiante)
        }
    }

    return Pair(aprobados, suspensos)//he tenido que buscarlo
}
