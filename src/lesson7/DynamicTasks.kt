@file:Suppress("UNUSED_PARAMETER")

package lesson7

import kotlin.math.max

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
//время: O(mn)
//память: O(mn) , где m - кол-во символов 1 строки, n - кол-во символов 2 строки
fun longestCommonSubSequence(first: String, second: String): String {
    var result = ""
    if (first == "" || second == "")
        return result
    var firstLen = first.length
    var secondLen = second.length
    val matrix = Array(first.length + 1) { IntArray(second.length + 1) }
    for (i in 0 until firstLen) {
        for (j in 0 until secondLen) {
            if (first[i] == second[j])
                matrix[i + 1][j + 1] = matrix[i][j] + 1
            else
                matrix[i + 1][j + 1] = maxOf(matrix[i][j + 1], matrix[i + 1][j])
        }
    }
    while (firstLen > 0 && secondLen > 0) {
        when {
            first[firstLen - 1] == second[secondLen - 1] -> {
                result += first[firstLen - 1]
                firstLen--
                secondLen--
            }
            matrix[firstLen - 1][secondLen] == matrix[firstLen][secondLen] -> firstLen--
            else -> secondLen--
        }
    }
    return result.reversed()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
//время: O(N^2)
//память: O(N)
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    if (list.isEmpty() || list.size == 1)
        return list
    val lengthOfSub = Array(list.size) { 1 }
    val prev = Array(list.size) { -1 }
    for (i in list.indices) {
        for (j in 0 until i) {
            if (list[i] > list[j]) {
                if (lengthOfSub[i] < lengthOfSub[j] + 1) {
                    lengthOfSub[i] = lengthOfSub[j] + 1
                    prev[i] = j
                }
            }
        }
    }
    var max = lengthOfSub[0]
    var pos = 0
    for (i in list.indices) {
        if (lengthOfSub[i] > max) {
            max = lengthOfSub[i]
            pos = i
        }
    }
    val result = mutableListOf<Int>()
    while (pos != -1) {
        result.add(list[pos])
        pos = prev[pos]
    }
    return result.reversed()
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    TODO()
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5