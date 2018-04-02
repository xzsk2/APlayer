package remix.myplayer.request;

import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;

import io.reactivex.Observable;
import remix.myplayer.bean.netease.NSearchRequest;
import remix.myplayer.request.network.RxUtil;
import remix.myplayer.util.LogUtil;
import remix.myplayer.util.PlayListUtil;

import static remix.myplayer.util.ImageUriUtil.getSearchRequestWithAlbumType;

/**
 * Created by Remix on 2017/11/30.
 */

public class PlayListUriRequest extends LibraryUriRequest {

    public PlayListUriRequest(ImageView image, NSearchRequest request, RequestOptions glideOption){
        super(image,request,glideOption);
    }

    @Override
    public void onError(String errMsg) {
        mImage.setImageDrawable(mGlideOption.getErrorPlaceholder());
        LogUtil.d("Cover","Err: " + errMsg);
    }

    @Override
    public void load() {
        LogUtil.d("Cover","Request: " + mRequest);
        Observable.concat(
                getCustomThumbObservable(mRequest),
                Observable.fromIterable(PlayListUtil.getMP3ListByIds(PlayListUtil.getIDList(mRequest.getID()),mRequest.getID())).concatMapDelayError(song -> getThumbObservable(getSearchRequestWithAlbumType(song))))
        .firstOrError()
        .toObservable()
        .compose(RxUtil.applyScheduler())
        .subscribe(this::onSuccess, throwable -> onError(throwable.toString()));
    }
}
