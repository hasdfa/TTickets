package com.vadim.hasdfa.traintickets.Model

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import rx.annotations.Experimental
import java.util.*
import kotlin.collections.ArrayList

/**
* Created by Raksha Vadim on 29.09.2017.
*/
class GenerateData private constructor(private val citesArray: Array<String>) {
    companion object {
        private var data: GenerateData? = null
        fun shared(citesArray: Array<String> = arrayOf()): GenerateData {
            if (data == null)
                data = GenerateData(citesArray)
            return data!!
        }
    }

    @Experimental
    suspend fun generateDataAsync(count: Int = 10, partsCount: Int = 4, onComplete: (List<Event>) -> Unit) {
        var list: List<Event>? = null
        launch {
            val countPart = count / partsCount
            list = (0 until partsCount)
                    .map { async { generateAllData(countPart) } }
                    .flatMap { it.await() }
        }.invokeOnCompletion {
            list?.let(onComplete)
        }
    }

    private suspend fun generateAllData(count: Int = 10): ArrayList<Event> {
        val rnd = Random(System.currentTimeMillis())

        val data = ArrayList<Event>()
        for (it in 0 until count) {
            val event = Event.create()

            val (startDate, endDate) = generateRandomTime()
            event.startTime = startDate
            event.endTime = endDate

            val startStation = rnd.nextInt(citesArray.count())

            citesArray[startStation].also {
                event.startStation = when (Locale.getDefault().language) {
                    "RU", "UKR" -> it
                    else -> it.transliterate()
                }
            }
            citesArray[rnd.nextInt(citesArray.count(), exclude = arrayListOf(startStation))].also {
                event.endStation = when (Locale.getDefault().language) {
                    "RU", "UKR" -> it
                    else -> it.transliterate()
                }
            }

            event.startPlatform = rnd.nextInt(1, 10)
            event.endPlatform = rnd.nextInt(1, 10)

            val train = Train.create(rnd.nextInt(1, 555))
            train.wagons.clear()
            val prices = rnd.getPrice(event.endTime.time.time - event.startTime.time.time).map { it.toInt() }

            for (i in 0..rnd.nextInt(14) + 10) {
                val wagon = Wagon.create()

                when (rnd.nextInt(3)) {
                    0 -> {
                        wagon.wagonType = Wagon.WagonType.Platzkart
                        wagon.places.clear()
                        (0 until 30).forEach { wagon.places.add(Place.create(i+1, it *(i+1), prices[0])) }
                    }
                    1 -> {
                        wagon.wagonType = Wagon.WagonType.Coupe
                        wagon.places.clear()
                        (0 until 20).forEach { wagon.places.add(Place.create(i+1, it *(i+1), prices[1])) }
                    }
                    2 -> {
                        wagon.wagonType = Wagon.WagonType.SV
                        wagon.places.clear()
                        (0 until 10).forEach { wagon.places.add(Place.create(i+1, it *(i+1), prices[2])) }
                    }
                }
                train.minPrice = prices.distinct()[0]
                train.midPrice = prices.distinct()[1]
                train.maxPrice = prices.distinct()[2]

                train.wagons.add(wagon)
            }
            event.train = train

            data.add(event)
        }

        return data
    }

    private fun Random.getPrice(range: Long): ArrayList<Long> {
        var r = range / 4_000_000
        if (r <= 0)
            r = 1
        return arrayListOf(
                nextInt(15, 20) * r ,
                nextInt(20, 35) * r,
                nextInt(35, 60) * r
        )
    }

    private fun generateRandomTime(): Pair<Calendar, Calendar>{
        val random = Random(System.currentTimeMillis())

        val date1 = Calendar.getInstance()
        val date2 = Calendar.getInstance()

        date1.add(Calendar.DAY_OF_MONTH, random.nextInt(20))
        date1.add(Calendar.HOUR_OF_DAY, random.nextInt(24))
        date1.add(Calendar.MINUTE, random.nextInt(60))
        date1.set(Calendar.SECOND, 0)

        date2.time = date1.time

        val _1 = random.nextInt()
        if (_1 % 10 == 0) {
            date2.add(Calendar.DAY_OF_MONTH, random.nextInt(20))
        } else if (_1 % 10 == 1 || _1 % 10 == 7) {
            date2.add(Calendar.DAY_OF_MONTH, random.nextInt(10))
        } else if (_1 % 10 == 3 || _1 % 10 == 5 || _1 % 10 == 8) {
            date2.add(Calendar.DAY_OF_MONTH, random.nextInt(5))
        }

        if (_1 % 5 == 0) {
            date2.add(Calendar.HOUR_OF_DAY, random.nextInt(24))
        } else if (_1 % 5 == 1 || _1 % 5 == 4) {
            date2.add(Calendar.HOUR_OF_DAY, random.nextInt(12))
        } else {
            date2.add(Calendar.HOUR_OF_DAY, random.nextInt(6))
        }

        date2.add(Calendar.MINUTE, random.nextInt(60))
        date2.set(Calendar.SECOND, 0)

        return Pair(date1, date2)
    }


    private fun Random.nextInt(from: Int = 0, to: Int): Int{
        return this.nextInt(to-from) + from
    }
    private fun Random.nextInt(from: Int = 0, to: Int, exclude: ArrayList<Int> = arrayListOf()): Int{
        var result = this.nextInt(from, to)
        while (result in exclude) {
            result = this.nextInt(from, to)
        }
        return result
    }
    private fun Random.nextInt(bound: Int, exclude: ArrayList<Int> = arrayListOf()): Int{
        var result = this.nextInt(bound)
        while (result in exclude) {
            result = this.nextInt(bound)
        }
        return result
    }


    private fun String.transliterate(): String {
        val abcCyr = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
        val abcLat = arrayOf("a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja")
        val builder = StringBuilder()
        forEach { i ->
            val index = abcCyr.indexOf(i)
            if (index >= 0) {
                builder.append(abcLat[index])
            } else {
                builder.append(i)
            }
        }
        return builder.toString()
    }
}