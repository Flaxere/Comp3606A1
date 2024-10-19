package jaredDeFour.example.comp3606a1

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jaredDeFour.example.comp3606a1.StudentList.StudentListAdapter
import jaredDeFour.example.comp3606a1.chatlist.ChatListAdapter
import jaredDeFour.example.comp3606a1.network.Client
import jaredDeFour.example.comp3606a1.network.NetworkMessageInterface
import jaredDeFour.example.comp3606a1.network.Server
import jaredDeFour.example.comp3606a1.wifidirect.WifiDirectInterface
import jaredDeFour.example.comp3606a1.wifidirect.WifiDirectManager

class CommunicationActivity : AppCompatActivity(), WifiDirectInterface, NetworkMessageInterface {

    private var wfdManager: WifiDirectManager? = null

    private val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    private var wfdAdapterEnabled = false
    private var studentListAdapter:StudentListAdapter? = null
    private var chatListAdapter: ChatListAdapter? = null


    private var wfdHasConnection = false
    private var hasDevices = false
    private var server: Server? = null
    private var client: Client? = null
    private var deviceIp: String = ""
    private var groupCreated: Boolean = false
    private var studentList:MutableList<Student> = mutableListOf()
//    private var student_chat: MutableList<Chat> = mutableListOf()
    private var studentChat: MutableMap<String, Chat> = mutableMapOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_communication)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView : RecyclerView =findViewById(R.id.student_list)
        Log.e("STUDENTLIST","$studentChat")
        setUpStudents()
        studentListAdapter = StudentListAdapter(this,studentList)
        recyclerView.adapter = studentListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        sampleChat()
        Log.e("STUDENTLIST","$studentChat")



        val manager: WifiP2pManager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        val channel = manager.initialize(this, mainLooper, null)
        wfdManager = WifiDirectManager(manager, channel, this)

    }

    private fun setUpStudents(){
        val students = resources.getStringArray(R.array.studentids)

        for (s in students){
            val m: MutableList<ContentModel> = mutableListOf()
            studentList.add(Student(s))
            studentChat[s] = Chat(m)
        }

    }


    fun openChat(studentID: String){
        Log.e("STUDENTLIST","$studentChat")
        val chatHistory = studentChat[studentID]


        if (chatHistory != null) {
            Log.e("TEST","IT REACHES")

            chatListAdapter = ChatListAdapter(this,chatHistory.chatHistory)
            val studentChatList: RecyclerView = findViewById(R.id.message_history)
            studentChatList.adapter = chatListAdapter
            studentChatList.layoutManager = LinearLayoutManager(this)
        }
    }
    private fun sampleChat(){
        val students = resources.getStringArray(R.array.sampleChat)
        var list: MutableList<ContentModel> = mutableListOf()
        var chat: Chat = Chat(list)
        var count: Int = 0

        for( s in students){
            if(count %5 == 0)
                studentChat["816117992"]?.chatHistory?.add(ContentModel(s,"192.168.49.1"))
            if(count %2 == 0)
                 chat.chatHistory.add(ContentModel(s,"1"))
            else
                chat.chatHistory.add(ContentModel(s,"192.168.49.1"))
            count++


        }
        chatListAdapter = ChatListAdapter(this,chat.chatHistory)
        val studentChatList: RecyclerView = findViewById(R.id.message_history)
        studentChatList.adapter = chatListAdapter
        studentChatList.layoutManager = LinearLayoutManager(this)

    }
//    private fun setUpChat(){
//        chatListAdapter = ChatListAdapter(this,student_chat[0].chatHistory)
//        val studentChatList: RecyclerView = findViewById(R.id.student_chat)
//        studentChatList.adapter = chatListAdapter
//        studentChatList.layoutManager = LinearLayoutManager(this)
//
//    }


    override fun onResume() {
        super.onResume()
        wfdManager?.also {
            registerReceiver(it, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        wfdManager?.also {
            unregisterReceiver(it)
        }
    }

    override fun onWiFiDirectStateChanged(isEnabled: Boolean) {
        Log.e("PLEASE", "PLEASE WORK AND END MY SUFFERING")
        var text = "There was a state change in the WiFi Direct. Currently it is "
        text = if (isEnabled){
            "$text enabled!"
        } else {
            "$text disabled! Try turning on the WiFi adapter"
        }

        val toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast.show()
        wfdAdapterEnabled = isEnabled
        updateUI()
    }


    override fun onDeviceStatusChanged(thisDevice: WifiP2pDevice) {
        val toast = Toast.makeText(this, "Device parameters have been updated" , Toast.LENGTH_SHORT)

        toast.show()
    }



    fun startClass(view: View) {
        wfdManager?.createGroup()
        groupCreated = true
//        val thisDevice = when{
//            Build.VERSION.SDK_INT >= 33 -> intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE, WifiP2pDevice::class.java)!!
//            else -> @Suppress("DEPRECATION") intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)!!
//        }
        val SSID: TextView = findViewById(R.id.classSSID)
//
//        val thisDeviceName: String = thisDevice!!.deviceName
//        Log.e("error",thisDevice!!.deviceName)
        resources.getStringArray(R.array.studentids)
        SSID.text = "Class Network:"

        updateUI()
    }

    fun endClass(view: View){
        wfdManager?.disconnect()
        groupCreated = false
        updateUI()
    }

    private fun updateUI(){
        //The rules for updating the UI are as follows:
        // IF the WFD adapter is NOT enabled then
        //      Show UI that says turn on the wifi adapter
        // ELSE IF there is NO WFD connection then i need to show a view that allows the user to either
        // 1) create a group with them as the group owner OR
        //  IF i have created a group i need to show classroom d

        val wfdClassStartedView: LinearLayout = findViewById(R.id.classPage)
        wfdClassStartedView.visibility = if (wfdAdapterEnabled && groupCreated ) View.VISIBLE else View.GONE

        val wfdAdapterErrorView: ConstraintLayout = findViewById(R.id.clWfdAdapterDisabled)
        wfdAdapterErrorView.visibility = if (!wfdAdapterEnabled) View.VISIBLE else View.GONE

        val wfdNoConnectionView: ConstraintLayout = findViewById(R.id.clNoClassStarted)
        wfdNoConnectionView.visibility = if (wfdAdapterEnabled && !groupCreated) View.VISIBLE else View.GONE




    }



    override fun onGroupStatusChanged(groupInfo: WifiP2pGroup?) {
        val text: String
        if (groupInfo == null){
            text =  "Group is not formed"
        } else {
            text = "Group has been formed"
            groupCreated = true
        }
        val toast = Toast.makeText(this, text , Toast.LENGTH_SHORT)
        toast.show()
        wfdHasConnection = groupInfo != null

        if (groupInfo == null){
            server?.close()
            client?.close()
        } else if (groupInfo.isGroupOwner && server == null){
            server = Server(this)
            deviceIp = "192.168.49.1"
        }
        updateUI()
    }

    override fun onContent(content: ContentModel) {
        TODO("Not yet implemented")
    }
//    fun sendMessage(view: View) {
//        val etMessage: EditText = findViewById(R.id.etMessage)
//        val etString = etMessage.text.toString()
//        val content = ContentModel(etString, deviceIp)
//        etMessage.text.clear()
//        client?.sendMessage(content)
//        chatListAdapter?.addItemToEnd(content)
//
//    }
}