import java.lang.StringBuilder

enum class PriceCode {
    ECONOMY,
    SUPERCAR,
    MUSCLE
}

data class Car(
    val title: String,
    var priceCode: PriceCode
)

data class Rental(
    val car: Car,
    val daysRented: Int
) {
    init {
        if (daysRented < 0) {
            throw IllegalArgumentException("DaysRented must be greater than 0")
        }
    }

    val getRentalCost: Double
        get() = when (car.priceCode) {
            PriceCode.ECONOMY -> maxOf(0, daysRented - 2) * 30.0 + 80.0
            PriceCode.SUPERCAR -> daysRented.toDouble() * 200.0
            PriceCode.MUSCLE -> maxOf(0, daysRented - 3) * 50.0 + 200.0
        }

    val isBonusesIncluded: Boolean
        get() = car.priceCode == PriceCode.SUPERCAR && daysRented > 1
}

data class Customer(val name: String) {

    val rentals = mutableListOf<Rental>()

    fun addRental(rental: Rental) {
        rentals.add(rental)
    }
}

class BillingCalculation(private val customer: Customer) {

    fun billingStatement(): String {
        var totalAmount = 0.0
        var frequentRenterPoints = 0
        val result = StringBuilder()
            .append("Rental Record for ${customer.name}\n")

        customer.rentals.forEach { rental ->
            val thisAmount = rental.getRentalCost
            frequentRenterPoints++

            if (rental.isBonusesIncluded) {
                frequentRenterPoints++
            }

            result.append("\t${rental.car.title}\t$thisAmount\n")
            totalAmount += thisAmount
        }

        result.append("Final rental payment owed $totalAmount\n")
        result.append("You received an additional $frequentRenterPoints frequent customer points")
        return result.toString()
    }
}

fun main() {
    val rental1 = Rental(Car("Mustang", PriceCode.MUSCLE), 5)
    val rental2 = Rental(Car("Lambo", PriceCode.SUPERCAR), 20)
    val customer = Customer("Liviu")
    customer.addRental(rental1)
    customer.addRental(rental2)

    println(BillingCalculation(customer).billingStatement())
}