package nl.arieck.blockpages.domain.features.article

import nl.arieck.blockpages.domain.features.article.models.Article

/**
 * Created by Rick van 't Hof on 16/06/2023.
 */
interface ArticleRepository {
    fun getArticles(page: Int, limit: Int): List<Article>
}