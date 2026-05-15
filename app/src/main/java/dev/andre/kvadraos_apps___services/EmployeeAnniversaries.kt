package dev.andre.kvadraos_apps___services

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Employee(val name: String, val daysInCompany: Int)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun main() {
    System.setOut(PrintStream(System.out, true, StandardCharsets.UTF_8))
    val employees = listOf(
        Employee("Иван Иванов", 993),
        Employee("Пётр Петров", 994),
        Employee("Алексей Сидоров", 995),
        Employee("Дмитрий Смирнов", 999),
        Employee("Сергей Кузнецов", 1000),
        Employee("Никита Попов", 1001),
        Employee("Андрей Васильев", 1993),
        Employee("Михаил Новиков", 1994),
        Employee("Владимир Фёдоров", 1995),
        Employee("Егор Морозов", 1996),
        Employee("Максим Волков", 2000),
        Employee("Роман Алексеев", 2001),
        Employee("Артём Лебедев", 2002),
        Employee("Кирилл Семёнов", 2005),
        Employee("Олег Егоров", 2006),
        Employee("Иван Петров", 2007),
        Employee("Пётр Иванов", 2008)
    )

    val today = LocalDate.now()
    
    val resultMatrix = getAnniversariesForCurrentWeek(employees, today)

    println("Date\tText")
    resultMatrix.forEach { row ->
        val dateStr = row[0]
        val eventsStr = row[1].replace("\n", "\n\t") 
        
        println("$dateStr\t$eventsStr")
    }
}

fun getAnniversariesForCurrentWeek(employees: List<Employee>, currentDate: LocalDate): Array<Array<String>> {
    val formatter = DateTimeFormatter.ofPattern("dd.MM")
    val monday = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - 1)
    val weekDates = (0..6L).map { monday.plusDays(it) }

    return weekDates.map { targetDate ->
        val offsetDays = ChronoUnit.DAYS.between(currentDate, targetDate).toInt()
        val dayAnniversaries = employees
            .filter { emp -> 
                val targetDays = emp.daysInCompany + offsetDays
                targetDays > 0 && targetDays % 1000 == 0 
            }
            .joinToString("\n") { emp -> "${emp.name} – ${emp.daysInCompany + offsetDays} дней" }
        arrayOf(targetDate.format(formatter), dayAnniversaries)
    }.toTypedArray()
}