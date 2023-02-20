package com.RafalEngiWork.MLTraders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class MLRunAdapter(
    private val mLRuns: MutableList<MLRun>
) : RecyclerView.Adapter<MLRunAdapter.MLRunViewHolder>() {

    class MLRunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val title : TextView = itemView.findViewById(R.id.textViewMLRunTitle)
        val iterations : TextView = itemView.findViewById(R.id.textViewMLRunNumber)
        val button : Button = itemView.findViewById(R.id.buttonOpenMLRun)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MLRunViewHolder {
        return MLRunViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rv_firebase_dataset,
                parent,
                false
            )
        )
    }

    fun addMLRun(mlRun: MLRun){
        mLRuns.add(mlRun)
        notifyItemInserted(mLRuns.size - 1)
    }

    fun clearMLRuns(){
        mLRuns.removeAll{
            true
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MLRunViewHolder, position: Int) {
        val currentMLRun = mLRuns[position]
        holder.title.text = currentMLRun.name
        holder.iterations.text = currentMLRun.iterations.toString()

        holder.itemView.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_SecondFragment_to_statisticsFragment)
        )

        /*holder.button.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_statisticsFragment)
        }*/
    }

    override fun getItemCount(): Int {
        return mLRuns.size
    }
}