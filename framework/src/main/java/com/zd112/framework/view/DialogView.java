package com.zd112.framework.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zd112.framework.R;
import com.zd112.framework.utils.LogUtils;

public class DialogView extends Dialog {
    private View mView, mDialogLine;
    private DialogViewListener mDialogViewListener;
    private String mContent, mSureStr, mCancelStr;
    private TextView mContentView;
    private CusButton mSure, mCancel;
    private DialogOnClickListener mDialogOnClickListener;
    private boolean mIsReturn;
    private boolean mIsFull = false;

    public DialogView(@NonNull Context context, int style) {
        super(context, style);
    }

    public DialogView(@NonNull Context context, int style, int layout) {
        super(context, style);
        this.mView = LayoutInflater.from(context).inflate(layout, null);
    }

    public DialogView(@NonNull Context context, int style, View view) {
        super(context, style);
        this.mView = view;
    }

    public DialogView(@NonNull Context context, int style, View view, DialogViewListener dialogViewListener) {
        super(context, style);
        this.mView = view;
        this.mDialogViewListener = dialogViewListener;
    }

    public DialogView(@NonNull Context context, int style, int layout, DialogViewListener dialogViewListener) {
        super(context, style);
        this.mView = LayoutInflater.from(context).inflate(layout, null);
        this.mDialogViewListener = dialogViewListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mView != null) {
            setContentView(mView);
        } else {
            setContentView(R.layout.default_dialog);
            defaultView();
        }
        if (mView != null) {
            ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
            layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
            if (mIsFull) {
                layoutParams.height = getContext().getResources().getDisplayMetrics().heightPixels;
            }
            mView.setLayoutParams(layoutParams);
        }
        if (mDialogViewListener != null) {
            mDialogViewListener.onView(mView);
        }
    }

    public DialogView setFull(boolean isFull) {
        this.mIsFull = isFull;
        return this;
    }

    private void defaultView() {
        mContentView = this.findViewById(R.id.dialogContent);
        if (!TextUtils.isEmpty(mContent)) {
            mContentView.setText(mContent);
        }
        mSure = this.findViewById(R.id.dialogSure);
        mCancel = this.findViewById(R.id.dialogCancel);
        mDialogLine = this.findViewById(R.id.dialogLine);
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogOnClickListener != null) {
                    mDialogOnClickListener.onDialogClick(false);
                }
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogOnClickListener != null) {
                    mDialogOnClickListener.onDialogClick(true);
                }
            }
        });
    }

    public DialogView setAttr() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        return this;
    }

    public ShapeDrawable createRoundCornerShapeDrawable(float radius, float borderLength, int borderColor) {
        float[] outerRadii = new float[8];
        float[] innerRadii = new float[8];
        for (int i = 0; i < 8; i++) {
            outerRadii[i] = radius + borderLength;
            innerRadii[i] = radius;
        }
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(outerRadii, new RectF(borderLength, borderLength, borderLength, borderLength), innerRadii));
        shapeDrawable.getPaint().setColor(borderColor);
        return shapeDrawable;
    }

    public DialogView setOnlySure() {
        mSure.setCurrCorner(0, 0, getContext().getResources().getDimension(R.dimen.default_corner), getContext().getResources().getDimension(R.dimen.default_corner));
        mDialogLine.setVisibility(View.GONE);
        mCancel.setVisibility(View.GONE);
        return this;
    }

    public DialogView setGravity(int gravity) {
        getWindow().setGravity(gravity);
        return this;
    }

    public DialogView setAnim(int anim) {
        getWindow().setWindowAnimations(anim);
        return this;
    }

    public DialogView setContent(String content) {
        this.mContent = content;
        return this;
    }

    public DialogView setContentPosition(int position) {
        if (mContentView != null) {
            mContentView.setGravity(position);
        }
        return this;
    }

    public DialogView setSure(String sure) {
        this.mSureStr = sure;
        if (mSure != null) {
            mSure.setText(mSureStr);
        }
        return this;
    }

    public DialogView setCancel(String cancel) {
        this.mCancelStr = cancel;
        if (mCancel != null) {
            mCancel.setText(mCancelStr);
        }
        return this;
    }

    public DialogView setReturn(boolean isReturn) {
        this.mIsReturn = isReturn;
        return this;
    }

    public DialogView setListener(DialogOnClickListener l) {
        this.mDialogOnClickListener = l;
        return this;
    }

    public DialogView setOutsideClose(boolean isClose) {
        this.setCanceledOnTouchOutside(isClose);
        return this;
    }

    public interface DialogViewListener extends DialogListener {
        void onView(View view);
    }

    public interface DialogOnClickListener extends DialogListener {
        void onDialogClick(boolean isCancel);
    }

    public interface DialogListener {

    }

    @Override
    public void onBackPressed() {
        LogUtils.e("---------mIsReturn:", mIsReturn, " | mDialogOnClickListener:", mDialogOnClickListener);
        if (mDialogOnClickListener != null) {
            mDialogOnClickListener.onDialogClick(false);
            return;
        }
        if (!mIsReturn) {
            super.onBackPressed();
        }
    }
}
