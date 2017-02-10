package com.even.mfilechooser;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.even.mfilechooser.adapter.ItemAdapter;
import com.even.mfilechooser.interfaces.OnChooserSelectedListener;
import com.even.mfilechooser.interfaces.OnFileSelectedListener;
import com.even.mfilechooser.model.Item;
import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * File Chooser
 * Created by Even on 17/2/10.
 */

public class FileChooser extends DialogFragment {
    public static final int MODE_FILE = 0;
    public static final int MODE_DIRECTORY = 1;

    private List<String> fileFormats;

    private int fileIconId;
    private int directoryIconId;

    private String confirmText;
    private @ColorInt int confirmTextColor;

    @Retention(RetentionPolicy.RUNTIME) @IntDef({
        MODE_FILE, MODE_DIRECTORY
    }) public @interface ChooserMode {
    }

    private @ChooserMode int chooserMode;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    private TextView tvCurrentDirectory;
    private TextView tvConfirm;

    private String initialDirectoryPath;
    private String currentDirectoryPath;

    private OnChooserSelectedListener onChooserSelectedListener;

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() == null) {
            return dialog;
        }
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        return dialog;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_file_chooser, container, false);
        initView(rootView);
        initRecyclerView(rootView);
        getFileList(initialDirectoryPath);
        return rootView;
    }

    private void initView(View rootView) {
        (rootView.findViewById(R.id.iv_close_chooser)).setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View v) {
                    dismiss();
                }
            });

        (rootView.findViewById(R.id.ll_path_container)).setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View v) {
                    goBackDirectory();
                }
            });

        tvCurrentDirectory = (TextView) rootView.findViewById(R.id.tv_current_directory);
        tvConfirm = (TextView) rootView.findViewById(R.id.tv_confirm);

        if (chooserMode == MODE_FILE) {
            tvConfirm.setVisibility(View.INVISIBLE);
        }
        tvConfirm.setText(confirmText);
        tvConfirm.setTextColor(confirmTextColor);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (onChooserSelectedListener != null) {
                    onChooserSelectedListener.onSelect(currentDirectoryPath);
                }
                dismiss();
            }
        });
    }

    private void initRecyclerView(View rootView) {
        adapter = new ItemAdapter();
        adapter.setOnFileSelectedListener(new OnFileSelectedListener() {
            @Override public void onFileSelected(Item item, int position) {
                if (item.isDirectory()) {
                    getFileList(item.getPath());
                } else {
                    if (onChooserSelectedListener != null) {
                        onChooserSelectedListener.onSelect(item.getPath());
                    }
                    dismiss();
                }
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void getFileList(String path) {
        currentDirectoryPath = path;

        tvCurrentDirectory.setText(path);

        File[] files = new File(path).listFiles(new FileFilter() {
            @Override public boolean accept(File file) {
                if (file.canRead()) {
                    if (chooserMode == MODE_DIRECTORY) {
                        return file.isDirectory();
                    } else if (chooserMode == MODE_FILE) {
                        return file.isDirectory() || isFileMatchFormat(file);
                    }
                }
                return false;
            }
        });

        List<Item> items = new ArrayList<>();
        if (files == null) {
            return;
        }
        for (File f : files) {
            int drawableId = f.isFile() ? fileIconId : directoryIconId;
            Drawable drawable =
                ContextCompat.getDrawable(getActivity().getApplicationContext(), drawableId);
            items.add(new Item(f.getPath(), drawable));
        }
        Collections.sort(items);

        adapter.setItems(items);
    }

    private void goBackDirectory() {
        File parent = new File(currentDirectoryPath).getParentFile();
        if (parent != null && parent.canRead()) {
            getFileList(parent.getPath());
        }
    }

    private boolean isFileMatchFormat(File file) {
        if (fileFormats != null && fileFormats.size() > 0) {
            for (String fileFormat : fileFormats) {
                if (file.getName().endsWith(fileFormat)) {
                    return true;
                }
            }
            return false;
        }
        return file.isFile();
    }

    public static class Builder {
        private Context mContext;
        private int style;
        private int theme;
        private String initialDirectoryPath = Environment.getExternalStorageDirectory().getPath();
        private int fileIconId;
        private int directoryIconId;
        private String confirmText;
        private List<String> fileFormats;
        private OnChooserSelectedListener onChooserSelectedListener;
        private @ColorInt int confirmTextColor;
        private @ChooserMode int chooserMode = MODE_FILE;

        public Builder(Context Context) {
            this.mContext = Context;
        }

        public Builder setStyle(int style) {
            this.style = style;
            return this;
        }

        public Builder setTheme(int theme) {
            this.theme = theme;
            return this;
        }

        public Builder setInitialDirectoryPath(String initialDirectoryPath) {
            this.initialDirectoryPath = initialDirectoryPath;
            return this;
        }

        public Builder setFileIcon(@DrawableRes int fileIconId) {
            this.fileIconId = fileIconId;
            return this;
        }

        public Builder setDirectoryIcon(@DrawableRes int directoryIconId) {
            this.directoryIconId = directoryIconId;
            return this;
        }

        public Builder setConfirmText(String confirmText) {
            this.confirmText = confirmText;
            return this;
        }

        public Builder setConfirmTextColor(@ColorInt int confirmTextColor) {
            this.confirmTextColor = confirmTextColor;
            return this;
        }

        public Builder setChooserMode(@ChooserMode int chooserMode) {
            this.chooserMode = chooserMode;
            return this;
        }

        public Builder setOnChooserSelectedListener(
            OnChooserSelectedListener onChooserSelectedListener) {
            this.onChooserSelectedListener = onChooserSelectedListener;
            return this;
        }

        public Builder setFileFormats(List<String> fileFormats) {
            this.fileFormats = fileFormats;
            return this;
        }

        public FileChooser build() {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setStyle(getStyle(), getTheme());
            fileChooser.initialDirectoryPath = this.initialDirectoryPath;
            fileChooser.fileIconId = getFileIconId();
            fileChooser.directoryIconId = getDirectoryIconId();
            fileChooser.chooserMode = this.chooserMode;
            fileChooser.confirmText = getConfirmText();
            fileChooser.confirmTextColor = getConfirmTextColor();
            fileChooser.onChooserSelectedListener = this.onChooserSelectedListener;
            fileChooser.fileFormats = this.fileFormats;

            return fileChooser;
        }

        private int getStyle() {
            return style == 0 ? DialogFragment.STYLE_NORMAL : style;
        }

        private int getTheme() {
            return theme == 0 ? R.style.DialogTheme : theme;
        }

        private int getFileIconId() {
            return fileIconId == 0 ? R.drawable.ic_file : fileIconId;
        }

        private int getDirectoryIconId() {
            return directoryIconId == 0 ? R.drawable.ic_directory : directoryIconId;
        }

        private String getConfirmText() {
            return (confirmText == null || confirmText.isEmpty()) ? mContext.getString(
                R.string.chooser_save) : confirmText;
        }

        private @ColorInt int getConfirmTextColor() {
            return confirmTextColor == 0 ? ContextCompat.getColor(mContext, R.color.chooser_blue)
                : confirmTextColor;
        }
    }
}
