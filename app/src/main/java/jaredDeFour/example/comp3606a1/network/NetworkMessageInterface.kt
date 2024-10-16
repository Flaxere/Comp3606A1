package jaredDeFour.example.comp3606a1.network

import jaredDeFour.example.comp3606a1.ContentModel

/// This [NetworkMessageInterface] acts as an interface.
interface NetworkMessageInterface {
    fun onContent(content: ContentModel)
}