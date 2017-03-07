package com.thinkmobiles.easyerp.presentation.screens.inventory.stock_returns;

import com.thinkmobiles.easyerp.domain.inventory.StockReturnRepository;
import com.thinkmobiles.easyerp.presentation.adapters.inventory.StockReturnsAdapter;
import com.thinkmobiles.easyerp.presentation.base.rules.master.selectable.MasterSelectableFragment;
import com.thinkmobiles.easyerp.presentation.base.rules.master.selectable.SelectableAdapter;
import com.thinkmobiles.easyerp.presentation.base.rules.master.selectable.SelectablePresenter;
import com.thinkmobiles.easyerp.presentation.managers.GoogleAnalyticHelper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

/**
 * @author Michael Soyma (Created on 3/6/2017).
 *         Company: Thinkmobiles
 *         Email: michael.soyma@thinkmobiles.com
 */
@EFragment
public class StockReturnsListFragment extends MasterSelectableFragment implements StockReturnsListContract.StockReturnsListView {

    private StockReturnsListContract.StockReturnsListPresenter presenter;

    @Bean
    protected StockReturnRepository stockReturnRepository;
    @Bean
    protected StockReturnsAdapter stockReturnsAdapter;

    @AfterInject
    @Override
    public void initPresenter() {
        new StockReturnsListPresenter(this, stockReturnRepository);
    }

    @Override
    public void setPresenter(StockReturnsListContract.StockReturnsListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getScreenName() {
        return "Stock Returns list screen";
    }

    @AfterViews
    protected void initAnalytics() {
        GoogleAnalyticHelper.trackScreenView(this, getResources().getConfiguration());
    }

    @Override
    protected SelectablePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected SelectableAdapter getAdapter() {
        return stockReturnsAdapter;
    }

    @Override
    public void openStockReturnsDetail(String id) {
        if (id != null) {
            //TODO open Stock Return detail
        } else {
            mActivity.replaceFragmentContentDetail(null);
        }
    }
}
