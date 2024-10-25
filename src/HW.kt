import java.util.Scanner

abstract class Ticket(private val movieName: String, private val showTime: String, private val price: Double) {
    abstract fun printDetails()

    fun getMovieName(): String {
        return movieName
    }

    fun getShowTime(): String {
        return showTime
    }

    fun getPrice(): Double {
        return price
    }
}

interface TicketType {
    val type: String
}

data class TicketPurchase(val ticket: Ticket, val buyDecision: String) {
    fun getTicketDetails(): String {
        return ticket.toString()
    }
}

class RegularTicket(movieName: String, showTime: String) : Ticket(movieName, showTime, 10.0), TicketType {
    override val type: String = "Regular"

    override fun printDetails() {
        println("Movie: ${getMovieName()}, Show Time: ${getShowTime()}, Type: $type, Price: ${getPrice()}$")
    }
}

class VIPTicket(movieName: String, showTime: String) : Ticket(movieName, showTime, 20.0), TicketType {
    override val type: String = "VIP"

    override fun printDetails() {
        println("Movie: ${getMovieName()}, Show Time: ${getShowTime()}, Type: $type, Price: ${getPrice()}$")
    }
}

class StudentTicket(movieName: String, showTime: String) : Ticket(movieName, showTime, 8.0), TicketType {
    override val type: String = "Student"

    override fun printDetails() {
        println("Movie: ${getMovieName()}, Show Time: ${getShowTime()}, Type: $type, Price: ${getPrice()}$")
    }
}

object TicketManager {
    val movies = listOf("Kung Fu Panda 4", "The Silent Voice", "Demon Slayer: Mugen Train", "Your Name")

    fun buyTicket(ticketType: TicketTypeEnum, movieIndex: Int, showTime: String): TicketPurchase? {
        val selectedMovie = movies.getOrNull(movieIndex)
        return when (ticketType) {
            TicketTypeEnum.REGULAR -> selectedMovie?.let { TicketPurchase(RegularTicket(it, showTime), "") }
            TicketTypeEnum.VIP -> selectedMovie?.let { TicketPurchase(VIPTicket(it, showTime), "") }
            TicketTypeEnum.STUDENT -> selectedMovie?.let { TicketPurchase(StudentTicket(it, showTime), "") }
        }
    }
}

enum class TicketTypeEnum(val code: String) {
    REGULAR("r"),
    VIP("v"),
    STUDENT("s");

    companion object {
        fun fromCode(code: String): TicketTypeEnum? {
            return values().find { it.code.equals(code, ignoreCase = true) }
        }
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val purchasedTickets = mutableListOf<TicketPurchase>()
    while (true) {
        try {
            println("Welcome to the Ticket App!")
            println("Do you want to buy a Regular, VIP, or Student ticket?")
            println("Regular--> r")
            println("VIP--> v")
            println("Student--> s")
            val ticketTypeCode = scanner.nextLine()
            val ticketType = TicketTypeEnum.fromCode(ticketTypeCode)
            if (ticketType != null) {
                println("Select a movie:")
                TicketManager.movies.forEachIndexed { index, movie ->
                    println("$index. $movie")
                }
                val movieIndex = scanner.nextLine().toIntOrNull()
                if (movieIndex != null && movieIndex in TicketManager.movies.indices) {
                    println("Available time:")
                    val times = listOf("28.05.2024 6PM", "29.05.2024 4PM", "30.05.2024 8PM")
                    times.forEachIndexed { index, time ->
                        println("$time  ---> ${index + 1}")
                    }
                    val showTimeIndex = scanner.nextLine().toIntOrNull()
                    val showTime = if (showTimeIndex != null && showTimeIndex in 1..times.size) {
                        times[showTimeIndex - 1]
                    } else {
                        println("Invalid show time, please select again.")
                        continue
                    }
                    val ticketPurchase = TicketManager.buyTicket(ticketType, movieIndex, showTime)
                    if (ticketPurchase != null) {
                        println("Ticket Details:")
                        ticketPurchase.ticket.printDetails()
                        println("Do you want to buy this ticket? (yes/no)")
                        val buyDecision = scanner.nextLine()
                        if (buyDecision.equals("yes", ignoreCase = true)) {
                            purchasedTickets.add(ticketPurchase.copy(buyDecision = "yes"))
                            println("Ticket purchased successfully. Enjoy the movie!")
                        } else {
                            purchasedTickets.add(ticketPurchase.copy(buyDecision = "no"))
                            println("Ticket not purchased.")
                        }
                    } else {
                        println("Invalid ticket type or movie selection.")
                    }
                } else {
                    println("Invalid movie selection, please choose a valid movie index.")
                }
            } else {
                println("Invalid ticket type. Please choose r, v, or s.")
            }
        } catch (e: Exception) {
            println("An error occurred: ${e.message}. Please try again.")
        }
    }
}
