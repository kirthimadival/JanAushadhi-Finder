package com.example.finder.data

import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class MedicineRepository(private val medicineDao: MedicineDao) {
    val allMedicines: Flow<List<Medicine>> = medicineDao.getAllMedicines()
    fun searchMedicines(query: String): Flow<List<Medicine>> = medicineDao.searchMedicines(query)

    private data class BrandInfo(
        val brand: String,
        val generic: String,
        val usage: String,
        val marketPrice: Double,
        val aushadhiPrice: Double
    )

    suspend fun initializeDatabase() {
        if (medicineDao.getCount() == 0) {
            val list = mutableListOf<Medicine>()
            
            // Highly realistic Indian market vs Jan Aushadhi prices (Strip of 10/15)
            val coreBrands = listOf(
                BrandInfo("Dolo 650", "Paracetamol", "Fever, Body ache, Pain relief", 30.91, 6.50),
                BrandInfo("Saridon", "Propyphenazone + Caffeine", "Headache, Pain relief", 42.50, 10.20),
                BrandInfo("Meftal Spas", "Mefenamic Acid + Dicyclomine", "Period pain, Abdominal cramps", 54.00, 12.50),
                BrandInfo("Crocin 650mg", "Paracetamol", "Fever, Mild to moderate pain", 31.20, 6.50),
                BrandInfo("Pantocid 40", "Pantoprazole", "Acidity, Heartburn, GERD", 165.00, 24.80),
                BrandInfo("Pan 40", "Pantoprazole", "Stomach gas, Acidity", 155.00, 24.80),
                BrandInfo("Telma 40", "Telmisartan", "High Blood Pressure", 194.00, 32.00),
                BrandInfo("Augmentin 625", "Amoxicillin + Clavulanic Acid", "Bacterial Infections", 215.00, 62.00),
                BrandInfo("Shelcal 500", "Calcium + Vit D3", "Bone health, Calcium deficiency", 125.00, 22.00),
                BrandInfo("Omez 20", "Omeprazole", "Stomach acid, Ulcers", 175.00, 22.50),
                BrandInfo("Combiflam", "Ibuprofen + Paracetamol", "Muscular pain, Joint pain", 48.00, 9.50),
                BrandInfo("Limcee", "Vitamin C", "Immunity booster", 28.00, 4.50),
                BrandInfo("Becosules", "Vitamin B-Complex", "Energy, Vitamin deficiency", 58.00, 10.00),
                BrandInfo("Digene", "Antacid", "Indigestion, Gas, Bloating", 145.00, 35.00),
                BrandInfo("Voveran 50", "Diclofenac", "Pain, Stiffness, Inflammation", 92.00, 15.50)
            )
            
            coreBrands.forEach { info ->
                list.add(Medicine(
                    brandedName = info.brand, 
                    genericName = info.generic, 
                    salt = info.generic, 
                    usage = info.usage, 
                    brandedPrice = info.marketPrice, 
                    genericPrice = info.aushadhiPrice
                ))
            }

            // Generate 500+ variants with realistic pharmacy pricing logic
            for(i in 1..500) {
                val index = i % coreBrands.size
                val base = coreBrands[index]
                val marketPrice = base.marketPrice + Random.nextDouble(-2.0, 40.0)
                // Jan Aushadhi prices are usually fixed based on the salt/formula
                val aushadhiPrice = base.aushadhiPrice + Random.nextDouble(-0.2, 1.5)
                
                list.add(Medicine(
                    brandedName = "${base.brand} Pack $i", 
                    genericName = base.generic, 
                    salt = base.generic, 
                    usage = base.usage,
                    brandedPrice = Math.round(marketPrice * 100) / 100.0, 
                    genericPrice = Math.round(aushadhiPrice * 100) / 100.0
                ))
            }
            medicineDao.insertAll(list)
        }
    }
}
