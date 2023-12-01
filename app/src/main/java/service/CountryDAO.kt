package service

import androidx.room.Insert
import androidx.room.Query
import model.Country

interface CountryDAO {

    @Query("SELECT * FROM country")
    suspend fun getAllCountries() : List<Country>

    @Query("SELECT * FROM country WHERE uuid = :countryID")
    suspend fun getCountry(countryID : Int) : Country

    @Insert
    suspend fun insertAllCountries(vararg countries : Country) : List<Long>

    @Query("DELETE FROM country")
    suspend fun deleteAllCountries()

}