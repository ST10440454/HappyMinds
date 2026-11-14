package com.happyminds.app.data

/**
 * Learners must set up a child profile (name + grade) before using the app.
 * Volunteers don't have a child, so they skip that step.
 */
fun User.needsChildSetup(): Boolean =
    !isVolunteer && (childName.isEmpty() || grade.isEmpty())
