package com.jeanloth.project.android.kotlin.local.mappers

interface Mapper<DOMAIN, ENTITY> {

    fun from( t : ENTITY ) : DOMAIN

    fun to( t : DOMAIN ) : ENTITY
}