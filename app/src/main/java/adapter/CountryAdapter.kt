package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.barisaslankan.kotlinjetpackcountries.R
import com.barisaslankan.kotlinjetpackcountries.databinding.CountryRowItemBinding
import com.barisaslankan.kotlinjetpackcountries.view.FeedFragmentDirections
import model.Country
import util.downloadFromUrl
import util.placeHolderProgressBar

class CountryAdapter(private val countryList:ArrayList<Country>) : Adapter<CountryAdapter.CountryViewHolder>() {

class CountryViewHolder(val binding : CountryRowItemBinding) : ViewHolder(binding.root){

}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = CountryRowItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CountryViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        holder.binding.countryRowCountryName.text = countryList[position].countryName
        holder.binding.countryRowCountryRegion.text = countryList[position].countryRegion

        holder.binding.countryRowCountryImage.downloadFromUrl(
            countryList[position].countryFlagUrl,
            placeHolderProgressBar(holder.itemView.context)
        )

        holder.itemView.setOnClickListener{
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment()
            action.countryUUID = countryList[position].uuid
            Navigation.findNavController(it).navigate(action)
        }

    }

    fun updateCountryList(newCountryList : List<Country>){
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }
}