package remix.myplayer.request;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import remix.myplayer.bean.netease.NSearchRequest;
import remix.myplayer.request.network.RxUtil;
import remix.myplayer.util.LogUtil;

/**
 * Created by Remix on 2017/12/4.
 */

public class LibraryUriRequest extends ImageUriRequest<String> {
    protected ImageView mImage;
    NSearchRequest mRequest;
    RequestOptions mGlideOption;
    public LibraryUriRequest(@NonNull ImageView image, @NonNull NSearchRequest request,@NonNull RequestOptions glideOption) {
        super();
        mImage = image;
        mRequest = request;
        mGlideOption = glideOption;
    }


    public void onError(String errMsg){
        if(mGlideOption != null){
            mImage.setImageDrawable(mGlideOption.getErrorPlaceholder());
        }
        LogUtil.d("UriRequest","Error: " + errMsg);
    }

    public void onSuccess(String result) {
        Glide.with(mImage.getContext())
                .load(result)
                .apply(mGlideOption)
                .into(mImage);
    }

    @Override
    public void load() {
        getThumbObservable(mRequest)
                .compose(RxUtil.applyScheduler())
                .subscribe(this::onSuccess, throwable -> onError(throwable.toString()));
    }
}
