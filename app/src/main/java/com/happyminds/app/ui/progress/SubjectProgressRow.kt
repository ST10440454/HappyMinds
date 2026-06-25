package com.happyminds.app.ui.progress

import com.happyminds.app.data.Subject

data class SubjectProgressRow(
    val subject: Subject,
    val percent: Int,
    val doneLessons: Int,
    val total: Int
)
