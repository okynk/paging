package com.vk.latestmovies.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.vk.latestmovies.service.Movie


/**
 * EpoxyController which works with PagedLists
 */
class MovieListEpoxyController : PagedListEpoxyController<Movie>() {

    private var isError: Boolean = false
    private var error: String? = ""

    override fun buildItemModel(currentPosition: Int, item: Movie?): EpoxyModel<*> {
        item?.let {
            return MovieItemModel_()
                .id("movie${currentPosition}")
                .title(item.title ?: "Unknown")
                .description(item.overview ?: "Uknown")
                .thumbnailUrl("http://image.tmdb.org/t/p/w185/${item.posterPath}")

        } ?: run {
            return LoadingEpoxyModel_()
                .id("loading")
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        if (isError) {
            super.addModels(
                models.plus(
                    ErrorEpoxyModel_()
                        .id("Error")
                        .errorStr(error)
                ).filter { !(it is LoadingEpoxyModel_) }
            )
        } else {
            super.addModels(models.distinct())
        }
    }

    override fun onExceptionSwallowed(exception: RuntimeException) {

    }

    fun setError(text: String?) {
        isError = text?.let {
            error = it
            true
        } ?: run {
            error = ""
            false
        }
        if (isError) {
            requestModelBuild()
        }
    }
}