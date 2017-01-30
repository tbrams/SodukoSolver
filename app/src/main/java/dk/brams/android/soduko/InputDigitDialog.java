package dk.brams.android.soduko;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;


public class InputDigitDialog extends DialogFragment {
    public static final String TAG = "TBR:InputDigitDialog";

    private RecyclerView mRecyclerView=null;

    public interface NoticeDialogListener {
        public void onDialogClick(String number);
    }


    NoticeDialogListener mListener;


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) getTargetFragment();
//            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyAlertDialogStyle);
 //       builder.setTitle("Input digit");


        // Inflate our special dialog layout for the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.input_digit, null);


        builder.setView(dialogView);

        mRecyclerView = (RecyclerView) dialogView.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        updateAdapterView(mRecyclerView);



        return builder.create();
    }


    private void updateAdapterView(RecyclerView rv) {
        List<Digit> testList = new ArrayList<>();

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                Digit d = new Digit(r, c, r*3+(c+1));
                testList.add(d);
            }
        }

        rv.setAdapter(new DigitAdapter(testList));
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
            mListener.onDialogClick(mDigit.getValue());
            getDialog().cancel();
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
