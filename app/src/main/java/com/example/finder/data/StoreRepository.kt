package com.example.finder.data

class StoreRepository {
    private val stores = listOf(
        Store(1, "Aushadhi Seva, Civil Lines", "Opposite General Hospital, Civil Lines", "3.5 km", true, "011-23456789", 28.4595, 77.0266),
        Store(2, "PMBJP Kendra, Indiranagar", "100ft Road, Near Metro Station, Indiranagar", "5.8 km", false, "9876543210", 28.4500, 77.0100),
        Store(3, "Generic Care, Salt Lake", "Sector 2, CJ Block, Kolkata", "0.8 km", true, "011-9876543", 28.4700, 77.0400),
        Store(4, "Health Savings Store, Powai", "Hiranandani Gardens, Powai, Mumbai", "4.2 km", true, "8888877777", 28.4400, 77.0500),
        Store(5, "Jan Aushadhi Kendra, Sector 15", "Shop No. 12, Main Market, Sector 15", "1.2 km", true, "9999900000", 28.4800, 77.0200)
    )

    fun getNearbyStores(): List<Store> = stores
}
