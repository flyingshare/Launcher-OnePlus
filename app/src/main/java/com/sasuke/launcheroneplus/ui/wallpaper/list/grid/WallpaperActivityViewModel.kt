package com.sasuke.launcheroneplus.ui.wallpaper.list.grid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sasuke.launcheroneplus.data.model.Error
import com.sasuke.launcheroneplus.data.model.Resource
import com.sasuke.launcheroneplus.data.model.Result
import com.sasuke.launcheroneplus.data.model.Wallpaper
import com.sasuke.launcheroneplus.data.network.UnsplashRepository
import javax.inject.Inject

class WallpaperActivityViewModel @Inject constructor(private val unsplashRepository: UnsplashRepository) :
    ViewModel(), UnsplashRepository.OnGetWallpaperListener,
    UnsplashRepository.OnGetPopularListener {

    var page = 1

    private val _popularWallpaperLiveData = MutableLiveData<Resource<List<Result>>>()
    val popularWallpaperLiveData: LiveData<Resource<List<Result>>>
        get() = _popularWallpaperLiveData

    private val _wallpaperLiveData = MutableLiveData<Resource<List<Result>>>()
    val wallpaperLiveData: LiveData<Resource<List<Result>>>
        get() = _wallpaperLiveData

    fun getWallpapersForQuery(query: String) {
        _wallpaperLiveData.postValue(Resource.loading())
        unsplashRepository.getWallpapers(query, page, this)
    }

    fun getPopularWalls() {
        _popularWallpaperLiveData.postValue(Resource.loading())
        unsplashRepository.getPopular(this)
    }

    fun refresh() {
        page = 1
    }

    override fun onGetWallpaperSuccess(wallpaper: Wallpaper) {
        page++
        _wallpaperLiveData.postValue(Resource.success(wallpaper.results))
    }

    override fun onGetWallpaperFailure(error: Error) {
        _wallpaperLiveData.postValue(Resource.error(error))
    }

    override fun onGetPopularSuccess(list: List<Result>) {
        _popularWallpaperLiveData.postValue(Resource.success(list))
    }

    override fun onGetPopularFailure(error: Error) {
        _popularWallpaperLiveData.postValue(Resource.error(error))
    }
}