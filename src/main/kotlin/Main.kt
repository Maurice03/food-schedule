import java.io.File
import java.io.FileWriter
import java.io.InputStream

fun main() {

    val foodList = mutableListOf<FoodListItem>()

    val rawFoodList = readFile("placeholder.txt") // reads the input file, format has to be -> id,meal(in double-quotes)

    for (i in rawFoodList) {
        // filters and formats the meals and the ids according to the data class
        val id = i.filter { it.isDigit() }
        val meal = i
            .substring(id.length, i.length)
            .trim()

        foodList.add(FoodListItem(meal, id.toInt()))
    }

    var counter = 0
    var mealCategory = 0

    val usedInLast30Days = arrayListOf<Int>()

    val target = "placeholder.txt"

    val dateList = readFile("placeholder.txt") // reads the input file, format has to be -> day,date,month

    outerLoop@ for (i in dateList) {

        // reset every 30 days, allows duplicates
        if (counter == 30) {
            counter = 0
            usedInLast30Days.clear()
        }

        // extracting the important information
        val day = i.takeWhile { it.isLetter() }
        val month = i.substring(i.lastIndexOf(" ") + 1)
        val date = i.filter { it.isDigit() }

        foodList.shuffle()

        // chooses the category
        when (day) {
            "Montag" -> mealCategory += 20
            "Dienstag" -> mealCategory += 40
            "Mittwoch" -> mealCategory += 60
            "Donnerstag" -> writeFile(target, "$day, $date. $month\nplaceholder\n", true) // same meal every thursday
            "Freitag" -> mealCategory += 80
            "Samstag" -> mealCategory += 100
            "Sonntag" -> continue@outerLoop // no food on Sunday
        }

        innerLoop@ for (k in foodList) {
            // compares id with category and whether it has been used in the last 30 days
            if (mealCategory > k.id && k.id > mealCategory - 20 && !usedInLast30Days.contains(k.id)) {
                usedInLast30Days.add(k.id)
                counter++
                writeFile(target, "$day, $date. $month\n${k.meal}\n", true)
                mealCategory = 0
                continue@outerLoop
            } else {
                continue@innerLoop
            }
        }
    }
}

fun readFile(data: String): MutableList<String> {

    val inputStream: InputStream = File(data).inputStream()
    val lineList = mutableListOf<String>()

    inputStream.bufferedReader().forEachLine { lineList.add(it) }

    return lineList
}

fun writeFile(target: String, input: String, addAtEnd: Boolean) {

    val fileWriter = FileWriter(target, addAtEnd)

    fileWriter.write(input)
    fileWriter.close()
}

