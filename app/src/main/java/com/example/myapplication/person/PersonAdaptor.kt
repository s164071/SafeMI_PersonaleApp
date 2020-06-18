package com.example.myapplication.person

import android.util.Log
import android.view.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class PersonAdaptor(): RecyclerView.Adapter<PersonAdaptor.ViewHolder>() {

    val list = mutableListOf<HomeDataElement>()

   inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val text:TextView = itemView.findViewById(R.id.home_item_text)
       val title:TextView = itemView.findViewById(R.id.home_item_title)
       val image: ImageView = itemView.findViewById(R.id.home_item_image_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_list_item,parent,false))

    }

    override fun getItemCount() = list.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val element = list[position]
        holder.text.text = element.text
        Log.d("Apaptor","elementText: "+element.text)
        Log.d("Apaptor", "elementPos: $position")


        when(element.type){
            HomeDataType.NAME -> {
                holder.title.text = "Navn"


            }
            HomeDataType.CPR -> {
                holder.title.text = "CPR"

            }
            HomeDataType.BLOD -> {
                holder.title.text = "Blod type"

            }
            HomeDataType.MEDICIN -> {
                holder.title.text = "Medicin"

            }
            HomeDataType.ALLERGIE -> {
                holder.title.text = "Allergi"

            }
            HomeDataType.DONER -> {
                holder.title.text = "Doner"

            }
            HomeDataType.EMERGENCY -> {
                holder.title.text = "Nød kontak information"

            }
            HomeDataType.OTHER -> {
                holder.title.text = "Sygdomstilstand og andet"

            }
        }


    }
}



