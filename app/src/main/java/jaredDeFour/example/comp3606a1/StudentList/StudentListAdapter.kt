package jaredDeFour.example.comp3606a1.StudentList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jaredDeFour.example.comp3606a1.ContentModel
import jaredDeFour.example.comp3606a1.R

class StudentListAdapter : RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {

    private val studentList:MutableList<ContentModel> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentView: TextView = itemView.findViewById(R.id.studentID)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.studentinformation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = studentList[position]
        holder.studentView.text = student.message

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

}