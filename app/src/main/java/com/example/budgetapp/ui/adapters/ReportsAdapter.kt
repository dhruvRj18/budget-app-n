package com.example.budgetapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetapp.databinding.ItemBudgetBinding
import com.example.budgetapp.entities.Budget
import java.text.SimpleDateFormat
import java.util.*

class ReportsAdapter : RecyclerView.Adapter<ReportsAdapter.MyViewHolder>() {

    inner class MyViewHolder(val itemBudgetBinding: ItemBudgetBinding) :
        RecyclerView.ViewHolder(itemBudgetBinding.root){}
    private val differCallback = object : DiffUtil.ItemCallback<Budget>(){
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
           return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            ItemBudgetBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            with(differ.currentList[position]){
                itemBudgetBinding.budgetItemAmount.text = amount.toString()
                itemBudgetBinding.budgetItemDate.text = dateMillisToString(date.toLong())
                itemBudgetBinding.budgetItemPerpose.text = purpose
                itemBudgetBinding.budgetItemType.text = creditOrDebit
            }
        }
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun dateMillisToString(dateInMillis:Long):String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
        val cal = Calendar.getInstance()
        cal.timeInMillis = dateInMillis
        return dateFormat.format(cal.time)
    }
}