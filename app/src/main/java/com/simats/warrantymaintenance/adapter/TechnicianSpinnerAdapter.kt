package com.simats.warrantymaintenance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.simats.warrantymaintenance.R
import com.simats.warrantymaintenance.data.Technician

class TechnicianSpinnerAdapter(
    context: Context,
    private val technicians: List<Technician?>
) : ArrayAdapter<Technician?>(context, R.layout.spinner_item, technicians) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        
        val technician = technicians[position]
        textView.text = technician?.name ?: "Select a technician..."
        textView.setTextColor(context.resources.getColor(R.color.black, null))
        
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_technician_dropdown_item, parent, false)
        
        val nameTextView = view.findViewById<TextView>(R.id.technician_name)
        val detailsTextView = view.findViewById<TextView>(R.id.technician_details)
        
        val technician = technicians[position]
        
        if (technician == null) {
            // Placeholder item
            nameTextView.text = "Select a technician..."
            nameTextView.setTextColor(context.resources.getColor(R.color.text_hint, null))
            detailsTextView.visibility = View.GONE
        } else {
            nameTextView.text = technician.name
            nameTextView.setTextColor(context.resources.getColor(R.color.black, null))
            detailsTextView.text = "${technician.specialization} • ${technician.experience} years exp • ${technician.status}"
            detailsTextView.visibility = View.VISIBLE
        }
        
        return view
    }
}
