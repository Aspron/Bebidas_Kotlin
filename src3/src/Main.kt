
open class Bebida(
    val id: Int,
    val litros: Double,
    val precio: Double,
    val marca: String
)

class AguaMineral(
    val origen: String,
    id: Int,
    litros: Double,
    precio: Double,
    marca: String
) : Bebida(id, litros, precio, marca)

class BebidaAzucarada(
    val porcentajeAzucar: Double,
    val tienePromocion: Boolean,
    id: Int,
    litros: Double,
    precio: Double,
    marca: String
) : Bebida(id, litros, precio, marca)

class Almacen {
    private val estanteria: Array<Array<Bebida?>> = Array(5) { arrayOfNulls(5) }

    fun agregarBebida(bebida: Bebida): Boolean {
        if (buscarPorId(bebida.id) != null) return false // Evita IDs duplicados

        for (i in estanteria.indices) {
            for (j in estanteria[i].indices) {
                if (estanteria[i][j] == null) {
                    estanteria[i][j] = bebida
                    return true
                }
            }
        }
        return false
    }

    fun eliminarBebida(id: Int): Boolean {
        for (i in estanteria.indices) {
            for (j in estanteria[i].indices) {
                if (estanteria[i][j]?.id == id) {
                    estanteria[i][j] = null
                    return true
                }
            }
        }
        return false
    }

    fun mostrarBebidas() {
        estanteria.forEachIndexed { i, fila ->
            fila.forEachIndexed { j, bebida ->
                bebida?.let {
                    println("Fila $i, Columna $j: ID=${it.id}, Marca=${it.marca}, Precio=${calcularPrecio(it)}")
                }
            }
        }
    }

    fun calcularPrecioTotal(): Double {
        return estanteria.flatten().filterNotNull().sumOf { calcularPrecio(it) }
    }

    fun calcularPrecioTotal(marca: String): Double {
        return estanteria.flatten().filterNotNull()
            .filter { it.marca.equals(marca, ignoreCase = true) }
            .sumOf { calcularPrecio(it) }
    }

    fun calcularPrecioTotal(columna: Int): Double {
        if (columna !in 0..4) return 0.0
        return estanteria.map { it[columna] }.filterNotNull().sumOf { calcularPrecio(it) }
    }

    private fun calcularPrecio(bebida: Bebida): Double {
        return when (bebida) {
            is BebidaAzucarada -> if (bebida.tienePromocion) bebida.precio * 0.9 else bebida.precio
            else -> bebida.precio
        }
    }

    private fun buscarPorId(id: Int): Bebida? {
        return estanteria.flatten().filterNotNull().find { it.id == id }
    }
}

fun main() {
    val almacen = Almacen()

    val agua = AguaMineral("Manantial", 1, 1.5, 0.8, "MarcaA")
    val cocaCola = BebidaAzucarada(10.0, true, 2, 0.5, 1.0, "Coca-Cola")
    val fanta = BebidaAzucarada(8.0, false, 3, 0.5, 0.9, "Fanta")

    almacen.agregarBebida(agua)
    almacen.agregarBebida(cocaCola)
    almacen.agregarBebida(fanta)

    println("--- Mostrar bebidas ---")
    almacen.mostrarBebidas()

    println("--- Precio total de todas las bebidas ---")
    println(almacen.calcularPrecioTotal())

    println("--- Precio total de las bebidas de Coca-Cola ---")
    println(almacen.calcularPrecioTotal("Coca-Cola"))

    println("--- Precio total de la columna 0 ---")
    println(almacen.calcularPrecioTotal(0))
}
