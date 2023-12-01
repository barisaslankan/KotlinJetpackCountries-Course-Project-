package viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import model.Country
import service.CountryAPIService
import service.CountryDAO
import service.CountryDatabase
import util.CustomSharedPreferences

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val countryAPIService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customSharedPreferences = CustomSharedPreferences(getApplication())

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    private val refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    fun refreshData(){
        val updateTime = customSharedPreferences.getTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            getDataFromSQLite()
        } else{
            getDataFromAPI()
        }
    }

    fun refreshFromAPI(){
        getDataFromAPI()
    }

    private fun getDataFromAPI(){
        countryLoading.value = true

        disposable.add(countryAPIService.getCountryData()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                override fun onSuccess(countriesFromAPI : List<Country>) {
                    storeCountriesInSQLite(countriesFromAPI)
                }

                override fun onError(e: Throwable) {
                    countryLoading.value = false
                    countryError.value = true
                    e.printStackTrace()
                }
            })
        )
    }

    private fun getDataFromSQLite(){
        countryLoading.value = true

        launch {
            val countryDAO = CountryDatabase(getApplication()).countryDao()
            val countries = countryDAO.getAllCountries()
            showCountries(countries)
        }
    }

    private fun storeCountriesInSQLite(countriesToBeStoredInSQLite : List<Country>){
        launch {
            val countryDAO = CountryDatabase(getApplication()).countryDao()
            countryDAO.getAllCountries()

            val countryUuids = countryDAO.insertAllCountries(*countriesToBeStoredInSQLite.toTypedArray())
            var i = 0

            while (i<countriesToBeStoredInSQLite.size){
                countriesToBeStoredInSQLite[i].uuid = countryUuids[i].toInt()
                i++
            }
            showCountries(countriesToBeStoredInSQLite)
        }

        customSharedPreferences.saveTime(System.nanoTime())

    }

    private fun showCountries(countriesToBeShown : List<Country>){
        countryLoading.value = false
        countryError.value = false
        countries.value = countriesToBeShown
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}