package jaredDeFour.example.comp3606a1.network

import jaredDeFour.example.comp3606a1.ContentModel
import java.io.BufferedWriter
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/// This [NetworkMessageInterface] acts as an interface.
interface NetworkMessageInterface {
    fun onContent(content: ContentModel, studentID: String)
    fun addToList(studentID: String)
    fun removeFromList(studentID: String)
}