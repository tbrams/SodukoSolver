package dk.brams.android.soduko;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DigitFragment extends Fragment implements InputDigitDialog.NoticeDialogListener {

    private static final String TAG = "TBR:";
    RecyclerView mRecyclerView;
    Button mSolveBtn;
    int[][] mBoard;
    int mRow=-1;
    int mCol=-1;


    public static DigitFragment newInstance() {
        return new DigitFragment();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main_menu, menu);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_clear) {
            int[][] board = generateBoard();
            updateAdapterView(board, mRecyclerView);
        }

        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        mBoard = generateBoard();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
        mSolveBtn = (Button) view.findViewById(R.id.solveBtn);
        mSolveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSolveBtn.getText().toString().toUpperCase().equals("SOLVE")) {
                    // Solve pressed
                    SudoSolver sk = new SudoSolver(new SudokuBoard(mBoard));

                    if (!sk.guess(0, 0)) {
                        Toast.makeText(getActivity(), "No solution found :-(", Toast.LENGTH_LONG).show();
                        mBoard = generateBoard();
                        mSolveBtn.setText("CLEAR");

                    } else {
                        updateAdapterView(mBoard, mRecyclerView);
                        mSolveBtn.setText("CLEAR");

                    }
                } else {
                    // Clear pressed
                    mBoard = generateBoard();
                    updateAdapterView(mBoard, mRecyclerView);
                    mSolveBtn.setText("SOLVE");
                }
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 9));
        updateAdapterView(mBoard, mRecyclerView);

        return view;
    }



    private int[][] generateBoard() {
        int[][] newBoard =
                {{3, 2, 9, 0, 0, 0, 0, 0, 0},
                 {0, 6, 5, 0, 0, 0, 1, 3, 0},
                 {0, 0, 0, 0, 0, 0, 0, 0, 2},
                 {0, 0, 0, 0, 0, 1, 3, 0, 0},
                 {9, 5, 0, 0, 0, 3, 0, 0, 1},
                 {0, 0, 1, 4, 0, 0, 0, 0, 9},
                 {0, 0, 7, 1, 0, 0, 0, 2, 0},
                 {0, 0, 0, 0, 9, 4, 5, 0, 8},
                 {0, 0, 0, 0, 0, 8, 4, 0, 0}};
         return newBoard;
    }



    private void updateAdapterView(int[][] board, RecyclerView rv) {
        List<Digit> testList = new ArrayList<>();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Digit d = new Digit(r, c, board[r][c]);
                testList.add(d);
            }
        }

        rv.setAdapter(new DigitAdapter(testList));
    }

    @Override
    public void onDialogClick(String number) {
        // Do stuff with number clicked...

        Log.d(TAG, "onDialogClicked: "+number);

        mBoard[mRow][mCol]=Integer.parseInt(number);
        updateAdapterView(mBoard, mRecyclerView);
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
            if (((mDigit.getCol() < 3) || (mDigit.getCol() > 5)) &&
                ((mDigit.getRow() < 3) || (mDigit.getRow() > 5))            ||
                   ((mDigit.getCol() > 2) && (mDigit.getCol() < 6)) &&
                   ((mDigit.getRow() > 2) && (mDigit.getRow() < 6))) {
                mButton.setBackgroundResource(R.drawable.button_selector_2);
                mButton.setTextColor(Color.WHITE);
            } else {
                mButton.setBackgroundResource(R.drawable.button_selector);
                mButton.setTextColor(Color.BLACK);
            }
            mButton.setText(mDigit.getValue());
        }

        @Override
        public void onClick(View v) {

            //
            Log.d(TAG, "you clicked: "+mDigit.getRow()+", "+mDigit.getCol());
            mRow = mDigit.getRow();
            mCol = mDigit.getCol();

            showDialog();
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


    private void showDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        InputDigitDialog inputDigitDialog = new InputDigitDialog();
        inputDigitDialog.setTargetFragment(this, 0);
        inputDigitDialog.show(fm, "input_digit_dialog");
    }
}

