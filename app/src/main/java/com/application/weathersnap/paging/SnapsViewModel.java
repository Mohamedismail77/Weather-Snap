package com.application.weathersnap.paging;


import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.application.weathersnap.data.DataRepository;
import com.application.weathersnap.data.WeatherSnap;

public class SnapsViewModel extends ViewModel {

    private DataRepository dataRepository;
    private SavedStateHandle savedStateHandle;
    public static int PAGE_SIZE = 30;
    public static boolean PLACEHOLDERS = true;

    @ViewModelInject
    public SnapsViewModel(DataRepository dataRepository,@Assisted SavedStateHandle savedStateHandle) {
        this.dataRepository = dataRepository;
        this.savedStateHandle = savedStateHandle;
    }

    public LiveData<PagedList<WeatherSnap>> getSnapsList() {
        PagedList.Config myPagingConfig = new PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setEnablePlaceholders(PLACEHOLDERS)
                .build();
        return new LivePagedListBuilder<>(dataRepository.getSnapsList(), myPagingConfig).build();
    }


    public void saveSnap(WeatherSnap weatherSnap) {
        dataRepository.saveSnap(weatherSnap);
    }

    public void deleteSnap(WeatherSnap weatherSnap) {
        dataRepository.deleteSnap(weatherSnap);
    }

}
