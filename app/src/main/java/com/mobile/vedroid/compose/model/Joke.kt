package com.mobile.vedroid.compose.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.Int
import kotlin.String

@Entity(tableName = "jokes")
data class Joke (
    @PrimaryKey
    val id: Int,
    val isSingle: Boolean,
    val setup: String,
    val delivery: String? = null){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Joke

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        val txt = StringBuilder()
        txt.append("id:")
        txt.append(id)
        txt.append("\nsingle:")
        txt.append(isSingle)
        txt.append("\nsetup:")
        txt.append(setup.replace("\n", ""))
        txt.append("\ndelivery:")
        txt.append(delivery?.replace("\n", ""))
        txt.append("\n")
        txt.append(javaClass.simpleName)
        txt.append("\n")

        return txt.toString()
    }

    companion object JokeBuilder {
        private var id : Int = -1
        private var setup : String? = null
        private var delivery : String? = null
        private var isSingle : Boolean = false

        fun create() : Joke = Joke(id, isSingle, setup!!, delivery)

        fun parseToJokeAttr(txt: String) : Boolean {
            if (txt.startsWith("id:")) {
                id = txt.replaceFirst("id:".toRegex(), "").toInt()
                return true
            }
            if (txt.startsWith("single:")) {
                isSingle = txt.replaceFirst("single:".toRegex(), "").toBoolean()
                return true
            }
            if (txt.startsWith("setup:")) {
                setup = txt.replaceFirst("setup:".toRegex(), "")
                return true
            }
            if (txt.startsWith("delivery:")) {
                delivery = txt.replaceFirst("delivery:".toRegex(), "")
                return true
            }
            return false
        }
    }
}