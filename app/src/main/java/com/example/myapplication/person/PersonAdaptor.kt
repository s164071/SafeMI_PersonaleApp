package com.example.myapplication.person

import android.util.Log
import android.view.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class PersonAdaptor(): RecyclerView.Adapter<PersonAdaptor.ViewHolder>() {

    val list = mutableListOf<PersonDataElement>()

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
            PersonDataType.NAME -> {
                holder.title.text = "Navn"


            }
            PersonDataType.CPR -> {
                holder.title.text = "CPR"

            }
            PersonDataType.BLOD -> {
                holder.title.text = "Blodtype"

            }
            PersonDataType.MEDICIN -> {
                holder.title.text = "Medicin"

            }
            PersonDataType.ALLERGIE -> {
                holder.title.text = "Allergi"

            }
            PersonDataType.DONER -> {
                holder.title.text = "Doner"

            }
            PersonDataType.EMERGENCY -> {
                holder.title.text = "Nød kontakt information"

            }
            PersonDataType.OTHER -> {
                holder.title.text = "Sygdomstilstand og andet"

            }
        }


    }
}



