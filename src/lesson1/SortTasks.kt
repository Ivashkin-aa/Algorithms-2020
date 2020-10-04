@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
//время: O(NLogN)
//память: O(N)
fun sortTimes(inputName: String, outputName: String) {
    val times = mutableListOf<String>();
    val result = mutableListOf<Int>();
    val constForTime = 120000;

    for (line in File(inputName).readLines()) {
        require(line.matches(Regex("""\d\d:\d\d:\d\d [A|P]M""")));
        times += line.replace(":", "");
    }
    for (time in times) {
        result += when {
            time.take(2) == "12" && time.takeLast(2) == "AM" ->
                time.dropLast(3).toInt() - constForTime;
            time.take(2) != "12" && time.takeLast(2) == "PM" ->
                time.dropLast(3).toInt() + constForTime;
            else -> time.dropLast(3).toInt();
        }
    }

    insertionSort(result);

    File(outputName).bufferedWriter().use {
        for (element in result) {
            var hours: Int;
            var min: Int;
            var sec: Int;
            val const = 10000;
            when {
                element <= constForTime / 20 -> {
                    hours = 12;
                    min = element / 100;
                    sec = element - min * 100;
                    it.write(String.format("%02d:%02d:%02d", hours, min, sec) + " AM");
                }
                element >= (constForTime + const) -> {
                    hours = (element - constForTime) / const;
                    min = (element % ((hours + 12) * const)) / 100;
                    sec = (element % ((hours + 12) * const)) % 100;
                    it.write(String.format("%02d:%02d:%02d", hours, min, sec) + " PM");
                }
                element >= constForTime && element < (constForTime + const) -> {
                    hours = element / const;
                    min = (element - hours * const) / 100;
                    sec = (element - hours * const) - min * 100;
                    it.write(String.format("%02d:%02d:%02d", hours, min, sec) + " PM");
                }
                else -> {
                    hours = element / const;
                    min = (element - hours * const) / 100;
                    sec = (element - hours * const) - min * 100;
                    it.write(String.format("%02d:%02d:%02d", hours, min, sec) + " AM"); }
            }
            it.newLine();
        }
    }
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
//время: O(NlogN)
//память: O(N)
fun sortAddresses(inputName: String, outputName: String) {
    val addr = mutableMapOf<String, List<String>>();
    for (line in File(inputName).readLines()) {
        require(line.matches(Regex("""\S+ \S+ - \S+ \d+""")));
        val del = line.trim().split(" - ");
        addr[del.last()] = addr.getOrDefault(del.last(), listOf()) + del.first();
    }
    val result = addr.keys.sortedWith(
        compareBy({ it.substringBeforeLast(" ") },
            { it.substringAfterLast(" ").toInt() })
    );
    File(outputName).bufferedWriter().use {
        for (home in result) {
            val names = addr[home]!!.sorted().joinToString().trim().replace(" , ", ", ");
            it.write(home.trim() + " - $names");
            it.newLine();
        }
    }
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
//время: O(N^2)
//память: O(N)
fun sortTemperatures(inputName: String, outputName: String) {
    val listForMinus = mutableListOf<Double>();
    val listForPlus = mutableListOf<Double>();
    for (line in File(inputName).readLines()) {
        if (line.contains("-"))
            listForMinus += line.toDouble();
        else listForPlus += line.toDouble();
    }
    listForMinus.sort();
    listForPlus.sort();
    File(outputName).bufferedWriter().use {
        for (element in listForMinus) {
            it.write(element.toString());
            it.newLine()
        }
        for (element in listForPlus) {
            it.write(element.toString());
            it.newLine();
        }
    }
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    TODO()
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}

