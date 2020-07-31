package com.rimcodeasg.luasforecast.data.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml
import java.io.Serializable

@Xml
class Tram(
    @Attribute(name = "destination")
    val destination : String,
    @Attribute(name = "dueMins")
    val dueMins : String
) : Serializable {}