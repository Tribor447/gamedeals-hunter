package com.example.gamedealshunter.util

object StoreNames {

    private val map = mapOf(
        1  to "Steam",
        2  to "GamersGate",
        3  to "GameStop",
        4  to "GOG",
        5  to "Origin",
        6  to "Direct2Drive",
        7  to "GreenManGaming",
        8  to "Amazon",
        9  to "GameFly",
        25 to "Epic Games",
        27 to "Humble",
        28 to "Fanatical",

    )

    operator fun get(id: Int): String = map[id] ?: "Store $id"
}