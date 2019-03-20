package ae.bluecast.library.Adapter

import ae.bluecast.library.Model.Books
import ae.bluecast.library.R
import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.lang.Exception

class SuggestedBookAdapter(val bookList: ArrayList<Books>) : RecyclerView.Adapter<SuggestedBookAdapter.cViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cViewHolder {
        return cViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_suggestedbooks, parent, false))
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(p0: cViewHolder, position: Int) {

        try {
            Glide.with(p0.tvBookname).load(bookList[position].bookImageUrl).into(p0.ivBookCover)
        } catch (e: Throwable) {

        }
        p0.tvBookDescription.text = bookList[position].authorName
        p0.tvBookname.text = bookList[position].BookName
    }

    inner class cViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvBookname = itemView.findViewById<TextView>(R.id.tvBookname)!!
        var ivBookCover = itemView.findViewById<ImageView>(R.id.ivBookCover)!!
        var tvBookDescription = itemView.findViewById<TextView>(R.id.tvBookDescription)!!
    }
}