package com.ayursha.herbals.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.ayursha.herbals.R;
import com.ayursha.herbals.models.ProductModel;
import com.ayursha.herbals.presenters.HomePresenter;
import com.ayursha.herbals.ui.adapters.StoryListAdapter;

public class HomeFragment extends Fragment {

	private OnFragmentInteractionListener listener;
	private HomePresenter homePresenter;

    @BindView(R.id.recycler_category)
    RecyclerView recyclerCategory;

	public static HomeFragment newInstance() {
		return new HomeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
		homePresenter = new HomePresenter(this);
		return view;
	}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homePresenter.loadProducts();
    }

    @Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			listener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	public interface OnFragmentInteractionListener {
	}

	public void onProductListReceived(ArrayList<ProductModel> storyModels){
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		recyclerCategory.setLayoutManager(linearLayoutManager);
		StoryListAdapter storyListAdapter = new StoryListAdapter(getActivity(), storyModels);
		recyclerCategory.setAdapter(storyListAdapter);
	}

    /*public void callFromPresenter(ArrayList<ContentCategoryModel> contentCategoryModels){
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerCategory.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {

                if (position % 3 == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerCategory.setLayoutManager(gridLayoutManager);
        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(getActivity(), contentCategoryModels);
        recyclerCategory.setAdapter(categoryListAdapter);
    }

*/
}