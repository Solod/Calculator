package ua.killer.jack.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {
    private final String FIRST_NUM = "first num";
    private final String OPERATION = "oper";
    private final String IS_FN = "is first num";
    private final String DISP_ENTER = "display enter";
    private final String DISP_RES = "display res";


    private TextView mDisplayEnter;
    private TextView mDisplayPreResult;
    private ImageView mCleanBtn;
    private int mFirstNum;
    private boolean isFirstNum = false;
    private char mOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DISP_ENTER, String.valueOf(mDisplayEnter.getText()));
        outState.putString(DISP_RES, String.valueOf(mDisplayPreResult.getText()));
        outState.putInt(FIRST_NUM, mFirstNum);
        outState.putBoolean(IS_FN, isFirstNum);
        outState.putChar(OPERATION, mOperation);
        }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDisplayEnter.setText(savedInstanceState.getString(DISP_ENTER));
        mDisplayPreResult.setText(savedInstanceState.getString(DISP_RES));
        mFirstNum = savedInstanceState.getInt(FIRST_NUM);
        isFirstNum = savedInstanceState.getBoolean(IS_FN);
        mOperation = savedInstanceState.getChar(OPERATION);

    }

    private void initView() {
        mDisplayEnter = (TextView) findViewById(R.id.display);
        mDisplayPreResult = (TextView) findViewById(R.id.result_str);
        mCleanBtn = (ImageView) findViewById(R.id.btn_clean);
        mCleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanAll();
            }
        });
    }


    @Override
    public void onClick(View view) {
        String num = String.valueOf(((Button) view).getText()).toLowerCase();
        switch (num) {
            case "=":
                mEqual();
                break;
            case "+":
                if (mOperation == '\u0000') {
                    preOperation('+');
                } else {
                    postOperation('+');
                }
                break;
            case "-":
                if (mOperation == '\u0000') {
                    preOperation('-');
                } else {
                    postOperation('-');
                }
                break;
            case "*":
                if (mOperation == '\u0000') {
                    preOperation('*');
                } else {
                    postOperation('*');
                }
                break;
            case "/":
                if (mOperation == '\u0000') {
                    preOperation('/');
                } else {
                    postOperation('/');
                }
                break;
            default:
                if (mDisplayEnter.getText().equals("0")) {
                    mDisplayEnter.setText("");
                }
                mDisplayEnter.setText(mDisplayEnter.getText() + num);
        }
    }

    private void preOperation(char c) {
        mFirstNum = Integer.valueOf(String.valueOf(mDisplayEnter.getText()));
        isFirstNum = true;
        mOperation = c;
        mDisplayPreResult.setText(mFirstNum + " " + mOperation);
        mDisplayEnter.setText("0");
    }

    private void postOperation(char c) {
        mEqual();
        mFirstNum = Integer.valueOf(String.valueOf(mDisplayEnter.getText()));
        mDisplayPreResult.setText(mDisplayPreResult.getText() + " " + c);
        mDisplayEnter.setText("0");
        mOperation = c;
    }

    private void mEqual() {
        if (isFirstNum) {
            mDisplayPreResult.setText(
                    mDisplayPreResult.getText() + " " + mDisplayEnter.getText() + " = " + calculate()
            );
            mDisplayEnter.setText(String.valueOf(calculate()));
            mOperation = '\u0000';
            isFirstNum = false;
        }
    }

    private int calculate() {
        int result;
        switch (mOperation) {
            case '+':
                result = mFirstNum + Integer.parseInt((String) mDisplayEnter.getText());
                break;
            case '-':
                result = mFirstNum - Integer.parseInt((String) mDisplayEnter.getText());
                break;
            case '*':
                result = mFirstNum * Integer.parseInt((String) mDisplayEnter.getText());
                break;
            case '/':
                try {
                    result = mFirstNum / Integer.parseInt((String) mDisplayEnter.getText());
                } catch (ArithmeticException ae) {
                    Toast.makeText(this, R.string.arithmetic_exception, Toast.LENGTH_LONG).show();
                    cleanAll();
                    result = 0;
                }
                break;

            default:
                return 0;
        }
        return result;
    }

    private void cleanAll() {
        mDisplayEnter.setText("0");
        mDisplayPreResult.setText("");
        isFirstNum = false;
        mOperation = '\u0000';
    }
}