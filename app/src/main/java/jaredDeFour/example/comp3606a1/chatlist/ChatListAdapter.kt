package jaredDeFour.example.comp3606a1.chatlist


import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jaredDeFour.example.comp3606a1.R
import jaredDeFour.example.comp3606a1.ContentModel
import jaredDeFour.example.comp3606a1.Chat
import jaredDeFour.example.comp3606a1.CommunicationActivity

class ChatListAdapter(var comAct: CommunicationActivity, context: Context, var chatList:MutableList<ContentModel>, val studID: String) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>(){
//    private val allChatList:MutableList<MutableList<ContentModel>> = mutableListOf()



    class ViewHolder(var studID:String,var comAct: CommunicationActivity,itemView: View) : RecyclerView.ViewHolder(itemView){//, View.OnClickListener {
        lateinit var buttonClickListener: OnItemClickListener
        val messageView: TextView = itemView.findViewById(R.id.messageTextView)
//        var button: Button? = itemView.findViewById(R.id.sendButton)
//        init{
//            button?.setOnClickListener(this)
//        }
//        val messageContent: EditText?=itemView.findViewById(R.id.messageContent)
//
//        override fun onClick(view: View?) {
//            comAct.sendMessage(studID,messageContent?.text.toString())
//            messageContent?.text?.clear()
//        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ViewHolder(studID,comAct,view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        (holder.messageView.parent as RelativeLayout).gravity = if (chat.senderIp=="192.168.49.1") Gravity.START else Gravity.END
        holder.messageView.text = chat.message

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

//    fun addNewChat(contentModel: ContentModel){
//        chatList.add(contentModel)
//        notifyItemInserted(chatList.size)
//    }
//
//    fun removeChat(position: Int){
//        allChatList.removeAt(position)
//        notifyItemRemoved()
//    }

    fun addItemToEnd(contentModel: ContentModel){
        chatList.add(contentModel)
        notifyItemInserted(chatList.size)

    }

    fun getStudentID(): String{
        return studID
    }

//    fun getChatListPos(){
//
//    }
//
//    fun chatNum(chatNum: Int){
//        studentNum = chatNum
//    }

}