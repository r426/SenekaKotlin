package com.ryeslim.senekakotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import java.util.ArrayList

class ItemArrayAdapter(@NonNull context: Context, resource: Int, @NonNull objects: ArrayList<Proverb>) :
    ArrayAdapter<Proverb>(context, resource, objects) {

    @NonNull
    override fun getView(position: Int, @Nullable convertView: View?, @NonNull parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.one_line, parent, false)
        val text: TextView
        val theDeleteButton: Button
        text = view.findViewById(R.id.list_item)
        text.text = this.getItem(position)!!.proverb

        theDeleteButton = view.findViewById(R.id.delete_one)
        theDeleteButton.setOnClickListener {
            remove(getItem(position)) // bug: removes items incorrectly
            WorkWithProverbs.getInstance().saveTheUpdatedList()
        }
        return view
    }
}
