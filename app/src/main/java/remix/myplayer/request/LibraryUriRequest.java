package remix.myplayer.request;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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

    public LibraryUriRequest(@NonNull SimpleDraweeView image, @NonNull NSearchRequest request, RequestConfig config) {
        super(config);
        mImage = image;
        mRequest = request;
    }

    public void onError(String errMsg){
        if(mGlideOption != null){
            mImage.setImageDrawable(mGlideOption.getErrorPlaceholder());
        }
        LogUtil.d("UriRequest","Error: " + errMsg);
    }

    public void onSuccess(String result) {
        if(mImage instanceof SimpleDraweeView){
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) mImage;
            ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(result));
            if(mConfig.isResize()){
                imageRequestBuilder.setResizeOptions(ResizeOptions.forDimensions(mConfig.getWidth(),mConfig.getHeight()));
            }
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequestBuilder.build())
                    .setOldController(simpleDraweeView.getController())
                    .build();

            simpleDraweeView.setController(controller);
        } else {
            Glide.with(mImage.getContext())
                    .load(result)
                    .apply(mGlideOption)
                    .into(mImage);
        }
    }

    @Override
    public void load() {
        getThumbObservable(mRequest)
                .compose(RxUtil.applyScheduler())
                .subscribe(this::onSuccess, throwable -> onError(throwable.toString()));
    }
}
