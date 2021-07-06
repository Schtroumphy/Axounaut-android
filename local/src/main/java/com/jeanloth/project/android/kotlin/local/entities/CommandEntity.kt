package com.jeanloth.project.android.kotlin.local.entities

import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import com.jeanloth.project.android.kotlin.local.converters.DateConverter
import io.objectbox.BoxStore
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@io.objectbox.annotation.Entity
class CommandEntity(
    @Id
    var idCommand : Long = 0L, // IMPORTANT to make the assignable id "var"

    //@Convert(converter = DateConverter::class, dbType = String::class)
    val deliveryDate : String? = null,

    var statusCode : Int = CommandStatusType.TO_DO.code,
    var totalPrice : Double? = null,
    var paymentAmount : Double? = null,
    var paymentTypeCode : String? = null

    //var client : AppClient? = null,
    //var articleWrappers : List<ArticleWrapper>,

) : Entity {

    @Backlink(to = "command") // command is the name of the attribute
    var articleWrappers: ToMany<ArticleWrapperEntity> = ToMany(this, CommandEntity_.articleWrappers)

    var client: ToOne<AppClientEntity> = ToOne(this, CommandEntity_.client)

    // Add BoxStore field
    @JvmField
    @Transient
    var __boxStore: BoxStore? = null
}
