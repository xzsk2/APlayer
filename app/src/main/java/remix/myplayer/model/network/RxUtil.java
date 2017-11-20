package remix.myplayer.model.network;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Remix on 2017/11/20.
 */

public class RxUtil {
    private static final String TAG = "GvgNetwork";
    private RxUtil(){}

    public static <T> ObservableTransformer<T,T> applyScheduler(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
//
//    public static <T> ObservableTransformer<CommonBean<T>,T> handleResult(){
//        return new ObservableTransformer<CommonBean<T>, T>() {
//            @Override
//            public ObservableSource<T> apply(@NonNull Observable<CommonBean<T>> upstream) {
//                return upstream
//                        .flatMap(new Function<CommonBean<T>, ObservableSource<T>>() {
//                            @Override
//                            public ObservableSource<T> apply(@NonNull CommonBean<T> commonBean) throws Exception {
//                                if(commonBean == null || commonBean.getT() == null){
//                                    return Observable.error(new ApiException(ApiCode.RETURN_NULL,"返回为空"));
//                                }
//                                if(commonBean.getCode() == ApiCode.SUCCESS){
//                                    LogUtils.e(TAG,"Response:{\n    msg: " + URLDecoder.decode(commonBean.getInfo(), "UTF-8") + "\n    code:" + commonBean.getCode() + "\n    data:" + commonBean.getT() + "}");
//                                    return createData(commonBean.getT());
//                                } else{
//                                    return Observable.error(new ApiException(commonBean.getCode(),commonBean.getInfo()));
//                                }
//                            }
//                        });
//            }
//        };
//    }

    private static <T> ObservableSource<T> createData(final T t) {
        return Observable.create(e -> {
            e.onNext(t);
            e.onComplete();
        });
    }
}
