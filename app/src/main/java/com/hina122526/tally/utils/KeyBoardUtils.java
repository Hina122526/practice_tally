package com.hina122526.tally.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.hina122526.tally.R;

public class KeyBoardUtils {
    private final Keyboard k1; //自定義鍵盤
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnsureListener{
        public void onEnsure();
    }
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);
        k1 = new Keyboard(this.editText.getContext(), R.xml.key);

        this.keyboardView.setKeyboard(k1); //設定要顯示自定義的鍵盤K1
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener); //設定鍵盤被叫出的監聽
    }

    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }
        @Override
        public void onRelease(int primaryCode) {

        }
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText(); //取得可以輸入的資料
            int start = editText.getSelectionStart();
            switch (primaryCode){
                case Keyboard.KEYCODE_DELETE: //按下退格(刪除)
                    if (editable!=null && editable.length()>0){
                       if (start>0){
                           editable.delete(start-1,start); //刪除一格
                       }
                    }
                    break;
                    case Keyboard.KEYCODE_CANCEL: //按下歸零
                        editable.clear();
                        break;
                        case Keyboard.KEYCODE_DONE: //按下完成
                            onEnsureListener.onEnsure(); //當點下確定之後可以調用這個方法
                            break;
                default: //按下數字鍵
                    editable.insert(start,Character.toString((char) primaryCode));
                    break;
            }
        }
        @Override
        public void onText(CharSequence text) {
        }
        @Override
        public void swipeLeft() {
        }
        @Override
        public void swipeRight() {
        }
        @Override
        public void swipeDown() {
        }
        @Override
        public void swipeUp() {
        }
    };
    //顯示鍵盤的方法
    public void showKeyboard(){
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.GONE){
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
    //隱藏鍵盤的方法
   public void holdKeyboard(){
    int visibility = keyboardView.getVisibility();
    if (visibility == View.VISIBLE ||visibility == View.INVISIBLE){
        keyboardView.setVisibility(View.GONE);
       }
    }

}
