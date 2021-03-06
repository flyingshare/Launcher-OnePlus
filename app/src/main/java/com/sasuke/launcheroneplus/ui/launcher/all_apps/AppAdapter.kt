package com.sasuke.launcheroneplus.ui.launcher.all_apps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sasuke.launcheroneplus.R
import com.sasuke.launcheroneplus.data.model.App
import com.sasuke.launcheroneplus.data.model.DrawerStyle
import com.sasuke.launcheroneplus.ui.widget.recyclerview_fastscroll.views.FastScrollRecyclerView
import com.sasuke.launcheroneplus.util.OnCustomEventListeners

class AppAdapter(private val glide: RequestManager) :
    RecyclerView.Adapter<AppViewHolder>(),
    FastScrollRecyclerView.SectionedAdapter,
    OnCustomEventListeners {

    init {
        setHasStableIds(true)
    }

    lateinit var appList: MutableList<App>
    private lateinit var onClickListeners: OnCustomEventListeners
    private var drawerStyle: DrawerStyle = DrawerStyle.VERTICAL

    private var primaryColor: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val layoutId = when (drawerStyle) {
            DrawerStyle.VERTICAL -> R.layout.cell_app_info
            DrawerStyle.LIST -> R.layout.cell_app_info_list
        }
        val view =
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return AppViewHolder(view, glide).apply {
            // The rotation pivot should be at the center of the top edge.
            itemView.doOnLayout { v -> v.pivotX = v.width / 2f }
            itemView.pivotY = 0f
            setOnCustomEventListeners(this@AppAdapter)
        }
    }

    override fun getItemCount(): Int {
        return if (::appList.isInitialized) appList.size else 0
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        if (::appList.isInitialized) {
            holder.setAppInfo(appList[position])
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setApps(list: MutableList<App>) {
        this.appList = list
        notifyDataSetChanged()
    }

    fun setOnCustomEventListeners(onClickListeners: OnCustomEventListeners) {
        this.onClickListeners = onClickListeners
    }

    override fun onItemClick(position: Int, parent: View, appInfo: App) {
        if (::onClickListeners.isInitialized)
            onClickListeners.onItemClick(position, parent, appInfo)
    }

    override fun onItemLongClick(position: Int, parent: View, appInfo: App) {
        if (::onClickListeners.isInitialized)
            onClickListeners.onItemLongClick(position, parent, appInfo)
    }

    override fun onDragStart(position: Int, parent: View, appInfo: App) {
        if (::onClickListeners.isInitialized)
            onClickListeners.onDragStart(position, parent, appInfo)
    }

    override fun onEventCancel(position: Int, appInfo: App) {
        if (::onClickListeners.isInitialized)
            onClickListeners.onEventCancel(position, appInfo)
    }

    fun updatePrimaryColor(color: Int) {
        this.primaryColor = color
    }

    fun updateDrawerStyle(drawerStyle: DrawerStyle) {
        this.drawerStyle = drawerStyle
    }

    override fun getSectionName(position: Int): String {
        return appList[position].label[0].toUpperCase().toString()
    }
}