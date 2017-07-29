package com.ycl.tabview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by gameben on 2017-06-02.
 */

public class RegisterDialog extends Dialog {

    public RegisterDialog(Context context) {
        super(context);
    }

    public RegisterDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String cancelText;
        private String shareText;
        private OnClickListener cancelTextonClickListener;
        private OnClickListener shareTextonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        public Builder setcancelText(int cancelText,
                                         OnClickListener listener) {
            this.cancelText = (String) context
                    .getText(cancelText);
            this.cancelTextonClickListener = listener;
            return this;
        }

        public Builder setcancelText(String cancelText,
                                         OnClickListener listener) {
            this.cancelText = cancelText;
            this.cancelTextonClickListener = listener;
            return this;
        }

        public Builder setshareText(int shareText,
                                         OnClickListener listener) {
            this.shareText = (String) context
                    .getText(shareText);
            this.shareTextonClickListener = listener;
            return this;
        }

        public Builder setshareText(String shareText,
                                         OnClickListener listener) {
            this.shareText = shareText;
            this.shareTextonClickListener = listener;
            return this;
        }

        public RegisterDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final RegisterDialog dialog = new RegisterDialog(context,R.style.CustomProgressDialog);
            View layout = inflater.inflate(R.layout.register_link_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ((TextView) layout.findViewById(R.id.register_title)).setText(title);

            if (cancelText != null) {
                ((TextView) layout.findViewById(R.id.dialog_button_cancel))
                        .setText(cancelText);
                if (cancelTextonClickListener != null) {
                    layout.findViewById(R.id.dialog_button_cancel)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    cancelTextonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.dialog_button_cancel).setVisibility(
                        View.GONE);
            }
            if (shareText != null) {
                ((TextView) layout.findViewById(R.id.dialog_button_ok))
                        .setText(shareText);
                if (shareTextonClickListener != null) {
                    layout.findViewById(R.id.dialog_button_ok)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    shareTextonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.dialog_button_ok).setVisibility(
                        View.GONE);
            }
            if (message != null) {
                ((TextView) layout.findViewById(R.id.register_message)).setText(message);
            }



            return dialog;
        }
    }


}
