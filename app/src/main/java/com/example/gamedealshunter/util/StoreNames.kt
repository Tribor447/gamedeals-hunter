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
        10 to "Apple App Store",
        11 to "Battle.net",
        12 to "EA App",
        13 to "Uplay",
        14 to "Microsoft Store",
        15 to "itch.io",
        16 to "Epic Games Store",
        17 to "Windows Store",
        18 to "Discord Store",
        19 to "Google Play",
        20 to "Bethesda",
        21 to "Rockstar Store",
        22 to "Paradox Store",
        23 to "CDKeys",
        24 to "Voidu",
        25 to "Epic Games",
        26 to "Gamesplanet",
        27 to "Humble",
        28 to "Fanatical",
        29 to "Newegg",
        30 to "IndieGala",
        31 to "DLGamer",
        32 to "2Game",
        33 to "WinGameStore",
        34 to "Allyouplay"

    )

    operator fun get(id: Int): String = map[id] ?: "Store $id"
}