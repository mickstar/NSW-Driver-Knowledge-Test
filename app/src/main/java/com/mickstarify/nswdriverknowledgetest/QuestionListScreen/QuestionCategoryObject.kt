package com.mickstarify.nswdriverknowledgetest.QuestionListScreen

import com.mickstarify.nswdriverknowledgetest.Database.Question

class QuestionCategoryObject {
    private var question: Question? = null
    private var category: Category? = null

    fun isCategory(): Boolean {
        return category != null
    }

    fun getQuestion(): Question {
        return this.question!!
    }

    fun getCategory(): Category {
        return this.category!!
    }

    constructor(question: Question) {
        this.question = question
    }

    constructor(category: Category) {
        this.category = category
    }
}

data class Category(val title: String) {

}
