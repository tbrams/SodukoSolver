package dk.brams.android.soduko;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class InputFragment extends Fragment {
    private static final String TAG = "TBR:InputFragment";
    private RecyclerView mRecyclerView=null;


    public static InputFragment newInstance() {
        return new InputFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        updateAdapterView(mRecyclerView);
        return view;
    }



    private void updateAdapterView(RecyclerView rv) {
        List<Digit> testList = new ArrayList<>();

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                Digit d = new Digit(r, c, r*3+c);
                testList.add(d);
            }
        }

        rv.setAdapter(new InputFragment.DigitAdapter(testList));
    }

    private class DigitHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button mButton;
        private Digit mDigit;


        public DigitHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.digit, container, false));

            mButton = (Button) itemView.findViewById(R.id.button);
            mButton.setOnClickListener(this);
        }


        public void bindDigit(Digit digit) {
            mDigit = digit;
            mButton.setText(mDigit.getValue());
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: "+mDigit.getRow()+", "+mDigit.getCol()+", "+mDigit.getValue());
        }
    }





    private class DigitAdapter extends RecyclerView.Adapter<DigitHolder> {
        private List<Digit> mDigits;

        public DigitAdapter(List<Digit> digits) {
            mDigits = digits;
        }


        @Override
        public DigitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new DigitHolder(inflater, parent);
        }


        @Override
        public void onBindViewHolder(DigitHolder holder, int position) {
            Digit digit = mDigits.get(position);
            holder.bindDigit(digit);

        }

        @Override
        public int getItemCount() {
            return mDigits.size();
        }
    }
}
