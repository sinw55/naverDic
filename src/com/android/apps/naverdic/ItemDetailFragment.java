package com.android.apps.naverdic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.apps.naverdic.dummy.DummyContent;
import com.android.apps.naverdic.servicecall.RequestTaskHandler;
import com.android.apps.naverdic.xmlclass.realtime.Result;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
	private String contentXml="";
	private Result result;
	private RequestTaskHandler rth;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
        	mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        	//rth.getNewsSearchResult();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        rth = new RequestTaskHandler(rootView);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
        	if (mItem.id.equals("1")) {
        		rth.getRealTimeSearchOrder(mItem.id);
        	} else if(mItem.id.equals("2")) {
        		rth.getNewsSearchResult(mItem.id);
        	} else if(mItem.id.equals("3")) {
        		rth.getMovieSearchResult(mItem.id);
        	}
        	
        }
        return rootView;
    }
    
    
}
