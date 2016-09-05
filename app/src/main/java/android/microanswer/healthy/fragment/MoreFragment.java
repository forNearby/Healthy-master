package android.microanswer.healthy.fragment;

import android.microanswer.healthy.R;
import android.microanswer.healthy.adapter.MoreGridviewAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

/**
 * 由 Micro 创建于 2016/6/30.
 */

public class MoreFragment extends Fragment implements AdapterView.OnItemClickListener {
    private String TAG = "MoreFragment";

    private View root = null;
    private AsymmetricGridView gridView;
    private MoreGridviewAdapter adapter;
    private AsymmetricGridViewAdapter asymadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_more, null);
        }
        if (gridView == null) {
            gridView = (AsymmetricGridView) root.findViewById(R.id.fragment_more_gridview);
        }
        gridView.setOnItemClickListener(this);
        gridView.setRequestedColumnCount(4);
        gridView.setRequestedHorizontalSpacing(0);

        if (gridView.getAdapter() == null) {
            adapter = new MoreGridviewAdapter(getActivity());
            asymadapter = new AsymmetricGridViewAdapter(getActivity(), gridView, adapter);
            gridView.setAdapter(asymadapter);
        }
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_shake));
    }
}
