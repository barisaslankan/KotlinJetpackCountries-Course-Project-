package util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.barisaslankan.kotlinjetpackcountries.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.net.URL

fun ImageView.downloadFromUrl(url: String?, progressBarDrawable: CircularProgressDrawable){

    val options = RequestOptions()
        .placeholder(progressBarDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}

fun placeHolderProgressBar(context: Context) : CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}