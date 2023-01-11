package ru.beeline.vafs.m1hw1

import kotlin.test.Test
import kotlin.test.assertEquals

class MyFirstTest {
    @Test
    fun firstTest() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun testSumFun() {
        assertEquals( 5, sum(2, 3))
    }
}