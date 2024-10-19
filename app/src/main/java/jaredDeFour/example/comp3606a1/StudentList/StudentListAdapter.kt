package jaredDeFour.example.comp3606a1.StudentList

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jaredDeFour.example.comp3606a1.R
import jaredDeFour.example.comp3606a1.Student
import jaredDeFour.example.comp3606a1.CommunicationActivity

class StudentListAdapter(context: Context, var studentList:MutableList<Student>) : RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        lateinit var buttonClickListener: OnItemClickListener
        val studentView: TextView = itemView.findViewById(R.id.studentID)
        var button: Button = itemView.findViewById(R.id.questionButton)
     init{
            button.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val studentID: String = studentView.text.toString()
            CommunicationActivity().openChat(studentID)
            Log.e("StudentID", studentID)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.studentinformation, parent, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = studentList[position]
        holder.studentView.text = student.studentID

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

}