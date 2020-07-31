package com.rimcodeasg.luasforecast.data.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class Direction(
    @Attribute(name = "name")
    val directionName : String,
    @Element
    val listOfTrams : List<Tram>
) {}