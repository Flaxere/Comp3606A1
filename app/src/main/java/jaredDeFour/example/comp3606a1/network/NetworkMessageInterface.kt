package jaredDeFour.example.comp3606a1.network

import jaredDeFour.example.comp3606a1.ContentModel

/// This [NetworkMessageInterface] acts as an interface.
interface NetworkMessageInterface {
    fun onContent(content: ContentModel, studentID: String)
    fun addToList(studentID: String)
    fun removeFromList(studentID: String)
}