package remix.myplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.util.DialogUtils;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import remix.myplayer.R;
import remix.myplayer.adapter.holder.BaseViewHolder;
import remix.myplayer.interfaces.ModeChangeCallback;
import remix.myplayer.request.RequestConfig;
import remix.myplayer.ui.MultiChoice;
import remix.myplayer.util.ColorUtil;
import remix.myplayer.util.Constants;
import remix.myplayer.util.DensityUtil;

import static remix.myplayer.request.ImageUriRequest.BIG_IMAGE_SIZE;
import static remix.myplayer.request.ImageUriRequest.SMALL_IMAGE_SIZE;

/**
 * @ClassName
 * @Description
 * @Author Xiaoborui
 * @Date 2017/1/17 16:36
 */

public abstract class HeaderAdapter<M, B extends RecyclerView.ViewHolder> extends BaseAdapter<M,BaseViewHolder> {

    static final int TYPE_HEADER = 0;
    static final int TYPE_NORMAL = 1;
    protected MultiChoice mMultiChoice;
    private ModeChangeCallback mModeChangeCallback;
    protected RequestOptions mGlideOption;
    //当前列表模式 1:列表 2:网格
    int mListModel = Constants.GRID_MODEL;

    HeaderAdapter(Context context, int layoutId, MultiChoice multiChoice) {
        super(context,layoutId);
        this.mMultiChoice = multiChoice;
    }

    public void setModeChangeCallback(ModeChangeCallback modeChangeCallback){
        mModeChangeCallback = modeChangeCallback;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEADER;
        return mListModel;
    }

    @Override
    protected M getItem(int position) {
        return mDatas != null ? position == 0 ? null : position - 1 < mDatas.size() ? mDatas.get(position - 1) : null : null;
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? super.getItemCount() + 1 : 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 列表模式切换
     * @param headerHolder
     * @param v
     */
    void switchMode(AlbumAdapter.HeaderHolder headerHolder, View v){
        int newModel = v.getId() == R.id.list_model ? Constants.LIST_MODEL : Constants.GRID_MODEL;
        if(newModel == mListModel)
            return;
        mListModel = newModel;
        headerHolder.mListModelBtn.setColorFilter(mListModel == Constants.LIST_MODEL ? ColorUtil.getColor(R.color.select_model_button_color) : ColorUtil.getColor(R.color.default_model_button_color));
        headerHolder.mGridModelBtn.setColorFilter(mListModel == Constants.GRID_MODEL ? ColorUtil.getColor(R.color.select_model_button_color) : ColorUtil.getColor(R.color.default_model_button_color));
        headerHolder.mDivider.setVisibility(mListModel == Constants.LIST_MODEL ? View.VISIBLE : View.GONE);
        saveMode();
        if(mModeChangeCallback != null){
            mModeChangeCallback.OnModeChange(mListModel);
        }
    }

    protected void saveMode(){}

    @SuppressLint("CheckResult")
    public void setUpGlideOption(int model,@AttrRes int defaultRes) {
        final int imageSize = model == Constants.LIST_MODEL ? SMALL_IMAGE_SIZE : BIG_IMAGE_SIZE;
        RequestConfig config = new RequestConfig.Builder(imageSize, imageSize).build();
        Drawable error = DialogUtils.resolveDrawable(mContext,defaultRes);
        mGlideOption = new RequestOptions()
                .override(config.getWidth(),config.getHeight())
                .placeholder(error)
                .error(error)
                .dontAnimate();
        if(model == Constants.GRID_MODEL){
            mGlideOption.transform(new RoundedCorners(DensityUtil.dip2px(mContext,2)));
        }
    }
}
