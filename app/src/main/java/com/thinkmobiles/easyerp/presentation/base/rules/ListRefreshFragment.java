package com.thinkmobiles.easyerp.presentation.base.rules;

import android.support.annotation.CallSuper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thinkmobiles.easyerp.R;
import com.thinkmobiles.easyerp.presentation.adapters.crm.SuggestionAdapter;
import com.thinkmobiles.easyerp.presentation.dialogs.FilterDialogFragment;
import com.thinkmobiles.easyerp.presentation.dialogs.FilterDialogFragment_;
import com.thinkmobiles.easyerp.presentation.holders.data.crm.FilterDH;
import com.thinkmobiles.easyerp.presentation.listeners.EndlessScrollListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * @author Alex Michenko (Created on 09.02.17).
 *         Company: Thinkmobiles
 *         Email: alex.michenko@thinkmobiles.com
 */

@EFragment
public abstract class ListRefreshFragment extends RefreshFragment {

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_list;
    }

    @ViewById
    protected RecyclerView listRecycler;

    protected EndlessScrollListener scrollListener;

    protected SuggestionAdapter suggestionAdapter;
    protected MenuItem menuFilters;
    protected MenuItem menuSearch;

    protected abstract void onLoadNextPage();

    @AfterViews
    protected void initList() {
        suggestionAdapter = new SuggestionAdapter(getActivity());

        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getActivity());

        scrollListener = new EndlessScrollListener(recyclerLayoutManager, () -> {
            if (srlHolderRefresh.isRefreshing()) {
                return false;
            } else {
                onLoadNextPage();
                return true;
            }
        });

        listRecycler.setLayoutManager(recyclerLayoutManager);
        listRecycler.addOnScrollListener(scrollListener);
    }

    @CallSuper
    protected void onRefreshData() {
        scrollListener.reset();
    }

    @Override
    protected void showErrorToast(String message) {
        scrollListener.reset();
        super.showErrorToast(message);
    }

    @Override
    protected View getHiddenView() {
        return listRecycler;
    }

    public void optionsMenuInitialized(Menu menu) {
        menuFilters = menu.findItem(R.id.menuFilters);
        menuSearch = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setSuggestionsAdapter(suggestionAdapter);
        searchView.setFocusable(false);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener()
        {
            @Override
            public boolean onSuggestionClick(int position)
            {
                FilterDH item = suggestionAdapter.getSuggestion(position);
                searchView.setQuery(item.name, false);
                searchView.clearFocus();
                onClickSearchSuggestion(item);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position)
            {
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                onSubmitSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    protected void onClickSearchSuggestion(FilterDH item){}
    protected void onSubmitSearch(String text){}

    protected void showDialogFiltering(ArrayList<FilterDH> filterDHs, int requestCode, String filterName) {
        listRecycler.requestFocus();
        FilterDialogFragment dialogFragment = FilterDialogFragment_.builder()
                .filterList(filterDHs)
                .filterName(filterName)
                .build();
        dialogFragment.setTargetFragment(this, requestCode);
        dialogFragment.show(getFragmentManager(), getClass().getName());
    }
}
