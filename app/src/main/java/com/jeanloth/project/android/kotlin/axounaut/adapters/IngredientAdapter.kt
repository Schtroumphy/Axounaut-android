package com.jeanloth.project.android.kotlin.axounaut.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.ProductWrapper
import kotlinx.android.synthetic.main.item_article.view.ib_add
import kotlinx.android.synthetic.main.item_article.view.ib_minus
import kotlinx.android.synthetic.main.item_article.view.tv_count
import kotlinx.android.synthetic.main.item_article.view.tv_name
import kotlinx.android.synthetic.main.item_stock_product.view.*

class ProductAdapter(
    private var products : List<ProductWrapper>,
    private var isEditMode : Boolean = true,
    private val context : Context
) : RecyclerView.Adapter<ProductAdapter.ArticleHolder>()  {

    var onAddMinusClick : ((ProductWrapper) -> Unit)? = null
    var displayNoArticlesError : ((Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_stock_product, parent, false)
        return ArticleHolder(itemView)
    }

    enum class PositionInList{
        ALONE, FIRST, BETWEEN, LAST
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        val product = products[position]

        val productsByCountStatus = products.filter { it.countStatusType == product.countStatusType  }
        val size = productsByCountStatus.size -1

        val positionOfMyItem = productsByCountStatus.indexOf(product)
        val positionInList = when{
            positionOfMyItem == size -> if(size == 0) PositionInList.ALONE else PositionInList.LAST
            positionOfMyItem == 0 && size > 0 -> PositionInList.FIRST
            positionOfMyItem < size  && size > 0 -> PositionInList.BETWEEN
            else -> PositionInList.BETWEEN
        }

        holder.bind(product, position, positionInList)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    init {
        sortProducts(false)
    }

    fun setItems(products : List<ProductWrapper>, isEditMode : Boolean = true){
        this.products = products
        this.isEditMode = isEditMode
        sortProducts()
    }

    inner class ArticleHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(productWrapper: ProductWrapper, position: Int, positionInList: PositionInList){

            itemView.tv_name.text= productWrapper.product.label
            itemView.tv_quantity_type.text= productWrapper.quantityType.label

            val count = productWrapper.quantity

            setupElementVisibility(count.toString(), isEditMode)

            // Line background drawable
            itemView.iv_line.background = getDrawable(itemView.context, when(positionInList){
                PositionInList.FIRST -> R.drawable.rounded_top_line
                PositionInList.BETWEEN -> R.drawable.stock_line
                PositionInList.LAST -> R.drawable.rounded_bottom_line
                PositionInList.ALONE -> R.drawable.rounded_line
            })

            // Line color by count status type
            itemView.iv_line.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(when(productWrapper.countStatusType){
                ProductWrapper.CountStatus.LOW -> R.color.red_002
                ProductWrapper.CountStatus.MEDIUM -> R.color.orange_002
                ProductWrapper.CountStatus.LARGE -> R.color.green_light_2
            }))

            // Add margin bottom if last item of count status type list
            val params = itemView.iv_line.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = if(positionInList == PositionInList.LAST || positionInList == PositionInList.ALONE )  20 else 0

            if(isEditMode){
                itemView.ib_minus.visibility = if(count > 0) VISIBLE else GONE

                itemView.ib_add.setOnClickListener {
                    productWrapper.quantity = count + 0.5
                    onAddMinusClick?.invoke(productWrapper)
                    sortProducts()
                }

                itemView.ib_minus.setOnClickListener {
                    if(count > 0) {
                        productWrapper.quantity = count - 0.5
                    }
                    onAddMinusClick?.invoke(productWrapper)
                    sortProducts()
                }
            }

        }

        private fun setupElementVisibility(count : String, editMode: Boolean) {
            itemView.tv_count.text = if(isEditMode) count else context.resources.getString(R.string.x_count, count)
            itemView.ib_add.visibility = if(isEditMode) VISIBLE else GONE
            if(!isEditMode) itemView.ib_minus.visibility = GONE
        }

    }

    private fun sortProducts(reload : Boolean = true){
        products = products.sortedBy { it.quantityType }.sortedBy { it.countStatusType }
        if(reload) notifyDataSetChanged()
    }

}