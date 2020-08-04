package com.rimcodeasg.luasforecast.data.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "stopInfo")
class StopInfo(
    @Attribute(name = "created")
    val createdDate : String,
    @Attribute(name = "stop")
    val stopName : String,
    @Attribute(name = "stopAbv")
    val stopAvb : String,
    @PropertyElement(name = "message")
    val msg : String,
    @Element
    val diretions : List<Direction>
)