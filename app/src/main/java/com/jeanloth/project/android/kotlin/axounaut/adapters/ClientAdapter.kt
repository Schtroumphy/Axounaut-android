package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeanloth.project.android.kotlin.axounaut.extensions.capitalize
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.databinding.ItemClientBinding
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import java.util.*

class ClientAdapter(
    private var clientList : List<AppClient>,
    private val context : Context
) : RecyclerView.Adapter<ClientAdapter.ClientHolder>()  {

    var onPhoneClick : ((Int?) -> Unit)? = null
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

        private val binding = ItemClientBinding.bind(view)
        fun bind(client : AppClient){
            binding.tvClientName.text= context.getString(R.string.client_name_label, client.lastname?.uppercase(),
                client.firstname.lowercase().capitalize())
            binding.tvFidelity.text= context.getString(R.string.client_fidelity_label, client.fidelityPoint.toString())

            if(client.isFavorite){
                binding.ibFavorite.backgroundTintList = ColorStateList.valueOf(getColor(context, R.color.ginger_2))
            } else {
                binding.ibFavorite.backgroundTintList = ColorStateList.valueOf(getColor(context, R.color.gray_1))
            }

            binding.cbClient.isChecked = false
            binding.cbClient.setOnCheckedChangeListener { _, isChecked ->
                onCheckboxClick?.invoke(client, isChecked )
            }

            binding.ibCall.setOnClickListener {
                onPhoneClick?.invoke(client.phoneNumber)
            }

            binding.ibFavorite.setOnClickListener {
                onFavoriteClick?.invoke(client)
            }

            binding.tvClientName.setOnClickListener {
                onClick?.invoke(client)
            }
        }

    }

}