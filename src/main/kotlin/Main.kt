enum class PriceCode {
    ECONOMY,
    SUPERCAR,
    MUSCLE
}

data class Car(val title: String, var priceCode: PriceCode)
data class Rental(val car: Car, private val _daysRented: Int) {

    val daysRented: Int
        get() = maxOf(0, _daysRented)
}

class Customer(private val name: String) {

    private val rentals = mutableListOf<Rental>()

    fun addRental(rental: Rental) {
        rentals.add(rental)
    }

    fun billingStatement(): String {
        var totalAmount = 0.0
        var frequentRenterPoints = 0
        var result = "Rental Record for $name\n"

        rentals.forEach { rental ->
            val thisAmount = getRentalCost(rental)
            frequentRenterPoints++

            if (isBonusesIncluded(rental)) {
                frequentRenterPoints++
            }

            result += "\t${rental.car.title}\t$thisAmount\n"
            totalAmount += thisAmount
        }

        result += "Final rental payment owed $totalAmount\n"
        result += "You received an additional $frequentRenterPoints frequent customer points"
        return result
    }

    private fun isBonusesIncluded(rental: Rental): Boolean {
        return rental.car.priceCode == PriceCode.SUPERCAR && rental.daysRented > 1
    }

    private fun getRentalCost(rental: Rental): Double {
        return when (rental.car.priceCode) {
            PriceCode.ECONOMY -> maxOf(0, rental.daysRented - 2) * 30.0 + 80.0
            PriceCode.SUPERCAR -> rental.daysRented.toDouble() * 200.0
            PriceCode.MUSCLE -> maxOf(0, rental.daysRented - 3) * 50.0 + 200.0
        }
    }
}

fun main() {
    val rental1 = Rental(Car("Mustang", PriceCode.MUSCLE), 5)
    val rental2 = Rental(Car("Lambo", PriceCode.SUPERCAR), 20)
    val rental3 = Rental(Car("Audi", PriceCode.ECONOMY), 8)
    val customer = Customer("Liviu")
    customer.addRental(rental1)
    customer.addRental(rental2)
    customer.addRental(rental3)

    println(customer.billingStatement())
}