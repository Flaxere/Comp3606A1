package jaredDeFour.example.comp3606a1.network

import android.util.Log
import com.google.gson.Gson
import jaredDeFour.example.comp3606a1.ContentModel
import jaredDeFour.example.comp3606a1.Student
import jaredDeFour.example.comp3606a1.network.NetworkMessageInterface
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import kotlin.Exception
import kotlin.concurrent.thread
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.SecretKey
import javax.crypto.Cipher
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random


/// The [Server] class has all the functionality that is responsible for the 'server' connection.
/// This is implemented using TCP. This Server class is intended to be run on the GO.

class Server(private val iFaceImpl: NetworkMessageInterface, val studentList: Array< String>) {
    companion object {
        const val PORT: Int = 9999

    }

    private val svrSocket: ServerSocket = ServerSocket(PORT, 0, InetAddress.getByName("192.168.49.1"))
    private val clientMap: HashMap<String, Socket> = HashMap()
    private val seedMap: HashMap<String, String> = HashMap()

    init {
        thread{
            while(true){
                try{
                    val clientConnectionSocket = svrSocket.accept()
                    Log.e("SERVER", "The server has accepted a connection: ")
                    handleSocket(clientConnectionSocket)

                }catch (e: Exception){
                    Log.e("SERVER", "An error has occurred in the server!")
                    e.printStackTrace()
                }
            }
        }
    }



    private fun handleSocket(socket: Socket){
        socket.inetAddress.hostAddress?.let {
            clientMap[it] = socket
            Log.e("SERVER", "A new connection has been detected!")
            thread {
                val clientReader = socket.inputStream.bufferedReader()
                val clientWriter = socket.outputStream.bufferedWriter()
                var receivedJson: String?

                fun sendMessage(content: ContentModel){
                    thread {

                        val contentAsStr:String = Gson().toJson(content)
                        clientWriter.write("$contentAsStr\n")
                        clientWriter.flush()
                    }
                }
//
                try{
                    receivedJson = clientReader.readLine()
                    var clientContent = Gson().fromJson(receivedJson, ContentModel::class.java)
                    val num = generateRandNum()
//                    val num = "816035980000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                    Log.e("NUMBER", "$num")
                    var response = ContentModel(num.toString(),clientContent.senderIp)
                    var fResponse = Gson().toJson(response)
                    clientWriter.write("$fResponse\n")
                    clientWriter.flush()
                    receivedJson = clientReader.readLine()
                    while(receivedJson == null)
                        receivedJson = clientReader.readLine()
                    clientContent = Gson().fromJson(receivedJson, ContentModel::class.java)
                    val strongSeed = clientContent.senderIp
                    val aesK = generateAESKey(strongSeed)
                    val aesIv = generateIV(strongSeed)
                    val hashedSID = clientContent.message
                    val test = decryptMessage(hashedSID,aesK,aesIv)
                    if(test==num.toString()){
                        response = ContentModel("ACK()",clientContent.senderIp)
                        fResponse = Gson().toJson(response)
                        clientWriter.write("$fResponse\n")
                        clientWriter.flush()

                        receivedJson = clientReader.readLine()
                        while(receivedJson == null)
                            receivedJson = clientReader.readLine()

                        clientContent = Gson().fromJson(receivedJson, ContentModel::class.java)
                        val studentID: String = decryptMessage(clientContent.message,aesK,aesIv)
                        Log.e("SIDDID", studentID)
                        if(isValidID(studentID)) {
                            Log.e("NUMBER3441", clientContent.message)
                            iFaceImpl.addToList(studentID)
                            Log.e("NUMBER3", clientContent.message)
//                            seedMap[studentID] = strongSeed'
                            while (socket.isConnected) {
                                try {
                                    receivedJson = clientReader.readLine()
                                    if (receivedJson != null) {
                                        Log.e("SERVER", "Received a message from client $it")
                                        clientContent = Gson().fromJson(receivedJson, ContentModel::class.java)

                                        val decryptedMsg = ContentModel(decryptMessage(clientContent.message,aesK,aesIv),"192.168.49.1")
                                        if(decryptedMsg.message != ""){
                                            val tmpIp = clientContent.senderIp
                                            iFaceImpl.onContent(decryptedMsg,studentID)
                                        }
//                                        val reversedContent = ContentModel(
//                                            clientContent.message.reversed(),
//                                            "192.168.49.1"
//                                        )
////
//                                        val clientMessage =
//                                            decryptMessage(clientContent.message, aesK, aesIv)
//                                        val reversedContentStr = Gson().toJson(reversedContent)
//                                        clientWriter.write("`$reversedContentStr\n")
//                                        clientWriter.flush()

                                        // To show the correct alignment of the items (on the server), I'd swap the IP that it came from the client
                                        // This is some OP hax that gets the job done but is not the best way of getting it done.

//                            clientContent.senderIp = reversedContent.senderIp
//                            reversedContent.senderIp = tmpIp


                                    }
                                } catch (e: Exception) {
                                    close()
                                    iFaceImpl.removeFromList(studentID)

                        Log.e("SERVER", "An error has occurred with the client $it")
                        e.printStackTrace()
                                }
//                                try {
//                                    val message = hasMessage()
//                                    if(hasMessage()!="_NO_MSG___DETECTED"){
//                                        val finalMessage = ContentModel(message,"192.168.49.1")
//                                        sendMessage(message)
//                                    }
//                                } catch (e: Exception) {
//                                    close()
////                        Log.e("SERVER", "An error has occurred with the client $it")
////                        e.printStackTrace()
//                                }

                            }
                            iFaceImpl.removeFromList(studentID)
                        }else{
                            Log.e("ERROR","This user does not belong in the class ")
//                            close()
                    }
                    }






                } catch (e: Exception){
                    Log.e("SERVER", "An error has occurred with establishing the connection $it")
                    e.printStackTrace()
                }


            }
        }
    }




    fun close(){
        svrSocket.close()
        clientMap.clear()
    }


    private fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }

    private fun getFirstNChars(str: String, n:Int) = str.substring(0,n)

    private fun generateRandNum(): Int{
        val num: Int = Random.nextInt(Int.MAX_VALUE)
        return num
    }

    fun hashStrSha256(str: String): String{
        val algorithm = "SHA-256"
        val hashedString = MessageDigest.getInstance(algorithm).digest(str.toByteArray(UTF_8))
        return hashedString.toHex();
    }


    fun generateAESKey(seed: String): SecretKeySpec {
        Log.e("FORYOUMRATTENTIONSEEKER",seed)
        val first32Chars = getFirstNChars(seed,32)
        val secretKey = SecretKeySpec(first32Chars.toByteArray(), "AES")

        return secretKey
    }
    fun generateIV(seed: String): IvParameterSpec {
        Log.e("FORYOUMRATTENTIONSEEKER",seed)
        val first16Chars = getFirstNChars(seed, 16)
        return IvParameterSpec(first16Chars.toByteArray())
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decryptMessage(encryptedText: String, aesKey:SecretKey, aesIv: IvParameterSpec):String{
        val textToDecrypt = Base64.Default.decode(encryptedText)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, aesKey,aesIv)
        val decrypt = cipher.doFinal(textToDecrypt)

        return String(decrypt)

    }

    fun isValidID(studentID: String): Boolean{
        var valid: Boolean = false
        for(studID in studentList){
            Log.e("STUDID",studentID)
            if(studID == studentID){
                Log.e("VALID",studID)
                valid = true
                break
            }
            Log.e("INVALID",studID)
        }

        return if(valid ){
            true
        }else{
            false
        }
    }


}