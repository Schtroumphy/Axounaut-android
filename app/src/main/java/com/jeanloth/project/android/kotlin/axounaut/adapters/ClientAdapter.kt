package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import kotlinx.android.synthetic.main.item_client.view.*

class ClientAdapter(
    private var clientList : List<AppClient>,
    private val context : Context
) : RecyclerView.Adapter<ClientAdapter.ClientHolder>()  {

    var onPhoneClick : ((AppClient) -> Unit)? = null
    var onFavoriteClick : ((AppClient) -> Unit)? = null
    var onClick : ((AppClient) -> Unit)? = null
    var onCheckboxClick : ((AppClient, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_client, parent, false)
        return ClientHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClientHolder, position: Int) {
        val client: AppClient = clientList[position]
        holder.bind(client)

    }

    override fun getItemCount(): Int {
        return clientList.size
    }

    fun setItems(clients : List<AppClient>){
        this.clientList = clients
        notifyDataSetChanged()
    }

    inner class ClientHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(client : AppClient){
            itemView.tv_client_name.text= context.getString(R.string.client_name_label, client.lastname?.toUpperCase(), client.firstname.toLowerCase())
            itemView.tv_fidelity.text= context.getString(R.string.client_fidelity_label, client.fidelityPoint.toString())

            if(client.isFavorite){
                itemView.ib_favorite.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.salamander));
            } else {
                itemView.ib_favorite.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.gray_1));
            }

            itemView.cb_client.isChecked = false
            itemView.cb_client.setOnCheckedChangeListener { _, isChecked ->
                onCheckboxClick?.invoke(client, isChecked )
            }

            itemView.ib_call.setOnClickListener {
                onPhoneClick?.invoke(client)
            }

            itemView.ib_favorite.setOnClickListener {
                onFavoriteClick?.invoke(client)
            }

            itemView.tv_client_name.setOnClickListener {
                onClick?.invoke(client)
            }
        }

    }

}