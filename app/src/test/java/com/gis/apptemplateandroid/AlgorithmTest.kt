package com.gis.apptemplateandroid

import org.junit.Test

class AlgorithmTest {

  @Test
  fun bubbleSortTest() {
    val array = arrayOf(1, 3, 5, 2, 4, 6, 10, 9, 8, 7)

    for (outI in 1 until array.size) {
      for (inI in array.size - 1 downTo 1) {
        if (array[inI] < array[inI - 1]) {
          swap(array, inI, inI - 1)
        }
      }
    }

    println("Bubble sort: ")
    for (i in 0 until array.size) {
      print("${array[i]} ")
    }
  }

  @Test
  fun selectionSortTest() {
    val array = arrayOf(1, 3, 5, 2, 4, 6, 10, 9, 8, 7)

    for (outI in 0 until array.size) {
      var minI = outI
      for (inI in outI until array.size) {
        if (array[inI] < array[minI]) {
          minI = inI
        }
      }
      swap(array, outI, minI)
    }

    println()
    println("Selection sort: ")
    for (i in 0 until array.size) {
      print("${array[i]} ")
    }
  }

  @Test
  fun insertionSortTest() {
    val array = arrayOf(1, 3, 5, 2, 4, 6, 10, 9, 8, 7)

    for (outI in 0 until array.size) {
      val curValue = array[outI]
      var prevI = outI - 1
      while (prevI >= 0 && curValue < array[prevI]) {
        array[prevI + 1] = array[prevI]
        prevI--
      }
      array[prevI + 1] = curValue
    }

    println()
    println("Insertion sort: ")
    for (i in 0 until array.size) {
      print("${array[i]} ")
    }
  }

  @Test
  fun shuttleSortTest() {
    val array = arrayOf(1, 3, 5, 2, 4, 6, 10, 9, 8, 7)

    for (outI in 1 until array.size) {
      if (array[outI] < array[outI - 1]) {
        swap(array, outI, outI - 1)
        var inI = outI - 1
        while (inI >= 0 && inI - 1 >= 0 && array[inI] < array[inI - 1]) {
          swap(array, inI, inI - 1)
          inI--
        }
      }
    }

    println()
    println("Shuttle sort: ")
    for (i in 0 until array.size) {
      print("${array[i]} ")
    }
  }

  @Test
  fun shellSortTest() {
    val array = arrayOf(1, 3, 5, 2, 4, 6, 10, 9, 8, 7)

    var gap = array.size / 2

    while (gap >= 1) {
      for (outI in 0 until array.size) {
        var diff = outI - gap

        while (diff >= 0) {
          if (array[diff] > array[diff + gap]) {
            swap(array, diff, diff + gap)
          }
          diff -= gap
        }
      }
      gap /= 2
    }

    println()
    println("Shell sort: ")
    for (i in 0 until array.size) {
      print("${array[i]} ")
    }
  }

  @Test
  fun mergeSortTest() {
    val array = arrayOf(1, 3, 5, 2, 4, 6, 10, 9, 8, 7)

    mergeSort(array, 0, array.size - 1)

    println()
    println("Merge sort: ")
    for (i in 0 until array.size) {
      print("${array[i]} ")
    }
  }

  private fun mergeSort(a: Array<Int>, start: Int, end: Int) {
    var delimiter = start + ((end - start) / 2) + 1

    if (delimiter > 0 && end > (start + 1)) {
      mergeSort(a, start, delimiter - 1)
      mergeSort(a, delimiter, end)
    }

    val buffer = arrayOfNulls<Int>(end - start + 1)
    var cursor = start
    for (i in 0 until buffer.size) {
      if (delimiter > end || a[cursor] > a[delimiter]) {
        buffer[i] = a[cursor]
        cursor++
      } else {
        buffer[i] = a[delimiter]
        delimiter++
      }
    }

    System.arraycopy(buffer, 0, a, start, buffer.size)
  }

  private fun swap(a: Array<Int>, firstI: Int, secondI: Int) {
    val temp = a[firstI]
    a[firstI] = a[secondI]
    a[secondI] = temp
  }
}