//package jaredDeFour.example.comp3606a1.network
//
//import android.util.Log
//import com.google.gson.Gson
//import jaredDeFour.example.comp3606a1.ContentModel
//import java.io.BufferedReader
//import java.io.BufferedWriter
//import java.net.Socket
//import kotlin.concurrent.thread
//
//class Client (private val networkMessageInterface: NetworkMessageInterface){
//    private lateinit var clientSocket: Socket
//    private lateinit var reader: BufferedReader
//    private lateinit var writer: BufferedWriter
//    private var seed =
//    var ip:String = ""
//
//    init {
//        thread {
//            clientSocket = Socket("192.168.49.1", Server.PORT)
//            reader = clientSocket.inputStream.bufferedReader()
//            writer = clientSocket.outputStream.bufferedWriter()
//            ip = clientSocket.inetAddress.hostAddress!!
//            while(true){
//                try{
//                     val serverResponse = reader.readLine()
//                     val serverContent = Gson().fromJson(serverResponse, ContentModel::class.java)
//                     val number = serverContent.message (do anything with the .msg or .senderIp
//
//                    if (serverResponse != null){
//                        val serverContent = Gson().fromJson(serverResponse, ContentModel::class.java)
//                        networkMessageInterface.onContent(serverContent, studentID)
//                    }
//                } catch(e: Exception){
//                    Log.e("CLIENT", "An error has occurred in the client")
//                    e.printStackTrace()
//                    break
//                }
//            }
//        }
//    }
//
//    fun sendMessage(content: ContentModel){
//        thread {
//            val contentAsStr:String = Gson().toJson(content)
//            writer.write("$contentAsStr\n")
//            writer.flush()
//        }
//
//    }
//
//    fun close(){
//        clientSocket.close()
//    }
//}