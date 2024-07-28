package com.amartyasingh.linkmanager.data.models

data class DataModel  (
    val recent_links:List<LinksModel>,
    val top_links:List<LinksModel>,
    val favourite_links:List<LinksModel>,
    val overall_url_chart: Map<String,Long>
)