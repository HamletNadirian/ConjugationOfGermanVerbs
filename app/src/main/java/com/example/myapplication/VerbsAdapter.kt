package com.example.myapplication

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.VerbsRecyclerBinding

class VerbsAdapter(private var verbsList: Map<String, TenseData>) :
    RecyclerView.Adapter<VerbsAdapter.VerbsViewHolder>() {
    class VerbsViewHolder(private val binding: VerbsRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun format(
            pronoun: String,
            forms: List<String>,
            color: String = "#1565C0"  // Используем синий цвет по умолчанию
        ): CharSequence {
            val coloredForms = forms.joinToString("  ") { "<font color='$color'>$it</font>" }
            return Html.fromHtml("<b>$pronoun</b> $coloredForms", Html.FROM_HTML_MODE_LEGACY)
        }

        fun bind(data: Map.Entry<String, TenseData>) {
            binding.zeit.text = data.key
            binding.ich.text = format("ich", data.value.S1)
            binding.du.text = format("du", data.value.S2)
            binding.ersiees.text = format("er/sie/es", data.value.S3)
            binding.wir.text = format("wir", data.value.P1)
            binding.ihr.text = format("ihr", data.value.P2)
            binding.sie.text = format("sie/Sie", data.value.P3)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerbsViewHolder {
        val binding =
            VerbsRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VerbsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return verbsList.size
    }

    override fun onBindViewHolder(holder: VerbsViewHolder, position: Int) {
        holder.bind(verbsList.entries.elementAt(position))
    }

    fun updateNewData(newVerbsList: Map<String, TenseData>) {
        verbsList = newVerbsList
        notifyDataSetChanged()
    }
}