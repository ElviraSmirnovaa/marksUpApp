package ru.bmstu.marksUpTeam.android.marksUpApp.data.network.favourites

import retrofit2.Response
import ru.bmstu.marksUpTeam.android.marksUpApp.data.FavouritesItem
import ru.bmstu.marksUpTeam.android.marksUpApp.domain.FavouritesItemDomain
import java.io.IOException

class FavouritesRepository(private val favouritesApi: FavouritesApi) {

    private suspend fun getFavourites(): Response<List<FavouritesItem>> {
        return favouritesApi.getFavourites()
    }

    private suspend fun postFavourite(favouritesItem: FavouritesItem): Response<FavouritesItem>{
        return favouritesApi.addFavourite(favouritesItem)
    }

    suspend fun getFavouritesDomain(): Result<List<FavouritesItemDomain>>{
        val response = getFavourites()
        if (response.isSuccessful && response.body() != null) {
            val favouritesItemDomainList = FavouritesMapper().mapList(response.body()!!)
            return Result.success(favouritesItemDomainList)
        }
        else return Result.failure(IOException(response.errorBody().toString()))
    }

    suspend fun postFavouriteDomain(domain: FavouritesItemDomain): Result<FavouritesItemDomain>{
        val toPost = FavouritesMapper().toDto(domain)
        val postResult = postFavourite(toPost)
        return if (postResult.isSuccessful && postResult.body() != null) {
            Result.success(FavouritesMapper().map(postResult.body()!!))
        } else Result.failure(IOException(postResult.errorBody().toString()))
    }


}