import java.nio.file.Path

fun main() {
    val raiz = Path.of("src")
    val ficheroCalificaciones = raiz.resolve("main").resolve("resources").resolve("calificaciones.csv")
    val datos = leerCalificaciones(ficheroCalificaciones)
    println(datos)
    // notas finales
    val datosConNotas = calcularNotaFinal(datos)
    println(datosConNotas)

    // aprobados y suspensos
    val (aprobados, suspensos) = aprobadoSuspenso(datosConNotas)

    // Imprimir los resultados
    println("Aprobados: $aprobados")
    println("Suspensos: $suspensos")
}
