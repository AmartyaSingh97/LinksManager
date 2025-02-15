package com.amartyasingh.linkmanager.data.models

data class ResponseModel (
    val status:Boolean,
    val statusCode:Int,
    val message:String,
    val support_whatsapp_number:String,
    val extra_income:Double,
    val total_links:Int,
    val total_clicks:Int,
    val today_clicks:Int,
    val top_source:String,
    val top_location:String,
    val startTime:String,
    val link_created_today:Int,
    val applied_campaign:Int,
    val data:DataModel
)